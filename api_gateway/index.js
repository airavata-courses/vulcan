const express = require('express')
const app = express()
const server = require('http').createServer(app)
const cors = require('cors')
const bodyParser = require('body-parser')
const kafka = require('kafka-node')
const MemoryStream = require('memorystream')
const { finished } = require('stream/promises')
app.use(cors())
app.use(bodyParser.urlencoded({ extended: false }))
app.use(bodyParser.json())

const { nexradApi, merraApi } = require('./config/api')
const logger = require('./config/logging')
const config = require('./config/appconfig')

const requestForwarder = async function (apiInstance, endpoint, requestBody, config = {}) {
  logger.debug(`POST ${apiInstance.defaults.baseURL}${endpoint} ${JSON.stringify(requestBody)}`)
  try {
    const response = await apiInstance.post(endpoint, requestBody, config)
    return response
  } catch (err) {
    logger.error(err.message)
    throw err
  }
}

const streamingForwarder = async function (req, res, apiInstance, endpoint = '') {
  const requestData = req.body
  try {
    const responseStream = new MemoryStream()
    const { data, headers } = await requestForwarder(apiInstance, endpoint, requestData, { responseType: 'stream' })
    headers['content-type'] && res.setHeader('Content-Type', headers['content-type'])
    headers['content-disposition'] && res.setHeader('Content-Disposition', headers['content-disposition'])
    data.pipe(res)
    await finished(responseStream)
    res.end()
  } catch (err) {
    res.status(500).send(err.message)
  }
}

const kafkaHandler = async function (reqTopic, resTopic, request, response = null) {
  const data = JSON.stringify(request.body)
  let payloads = [
    {
      topic: reqTopic,
      messages: data
    }
  ];

  try {
    const message = await new Promise((resolve, reject) => {

      client = new kafka.KafkaClient({ kafkaHost: config.kafkaBootstrapServer })
      producer = new kafka.Producer(client)

      producer.send(payloads, function (err) {
        if (err) {
          reject(err)
        }
      });
      consumer = new kafka.Consumer(client, [
        {
          topic: resTopic,
          partition: 0
        }],
        {
          autoCommit: false
        });

      consumer.on('message', resolve);
      consumer.on('error', reject)
    })

    // Extract only value property of the consumed message
    const { value } = message
    logger.debug(value)

    if (!!response) {
      response.status(200).send(value)
    }
  } catch (err) {
    logger.error(err)
    if (!!response) {
      response.status(500).send('An error occurred.')
    }
  }
}

//Register - KAFKA based
app.post('/register', (req, res) =>
  kafkaHandler(
    config.topic_user_register_request,
    config.topic_user_register_response,
    req,
    res
  )
)

// Login - KAFKA based
app.post('/login', (req, res) =>
  kafkaHandler(
    config.topic_user_login_request,
    config.topic_user_login_response,
    req,
    res
  )
)

// User-History + NEXRAD Ingestor
app.post('/weather/radar', (req, res) => {
  kafkaHandler(
    config.topic_user_history_save_request,
    config.topic_user_history_save_response,
    req)
  streamingForwarder(req, res, nexradApi)
})

// User-History + NEXRAD Ingestor
app.post('/weather/satellite', (req, res) => {
  kafkaHandler(
    config.topic_user_history_save_request,
    config.topic_user_history_save_response,
    req)
  streamingForwarder(req, res, merraApi)
})

// History get - KAFKA based
app.get('/history', (req, res) =>
  kafkaHandler(
    config.topic_user_history_get_request,
    config.topic_user_history_get_response,
    req,
    res
  )
)

logger.level = 'debug'
server.listen(3000, () => logger.info(`Service started: Server listening on ${server.address().port}`))
