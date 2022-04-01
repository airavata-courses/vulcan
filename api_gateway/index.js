const express = require('express')
const app = express()
const server = require('http').createServer(app)
const cors = require('cors')
const bodyParser = require('body-parser')
const kafka = require("kafka-node");
app.use(cors())
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json())

const { identityApi, historyApi, ingestorApi } = require('./config/api')
const logger = require('./config/logging')
const config = require('./config/appconfig')

const requestForwarder = async function (apiInstance, endpoint, requestBody) {
  logger.debug(`POST ${apiInstance.defaults.baseURL}${endpoint} ${JSON.stringify(requestBody) || '""'}`)
  try {
    const response = await apiInstance.post(endpoint, requestBody)
    return response.data
  } catch (err) {
    logger.error(err.message)
    throw err
  }
}

const asyncForwarder = async function (req, apiInstance, endpoint = '') {
  const data = req.body
  try {
    return await requestForwarder(apiInstance, endpoint, data)
  } catch (err) { }
}

const standardForwarder = async function (req, res, apiInstance, endpoint = '') {
  const data = req.body
  try {
    const result = await requestForwarder(apiInstance, endpoint, data)
    res.status(200).json(result)
  } catch (err) {
    res.status(500).send(err.message)
  }
}

const kafkaHandler = async function (reqTopic, resTopic, request, response) {
  const data = JSON.stringify(request.body)
  
  let payloads = [
    {
      topic: reqTopic,
      messages: data
    }
  ];

  try {
    const messageValue = await new Promise((resolve, reject) => {

      client = new kafka.KafkaClient({kafkaHost:config.kafkaBootstrapServer})
      
      producer = new kafka.Producer(client)

      producer.send(payloads, (err) => {
        if (err) {
          logger.error(err)
        }
      });

      consumer = new kafka.Consumer(client, [ 
        { 
            topic: resTopic, 
            partition: 0 
        } ], 
        { 
            autoCommit: false 
        });
        
      consumer.on('message', function (message) {
        resolve(message.value)
      });

      consumer.on('error', function (err) {
        reject(err)
      })
    })

    logger.debug(messageValue)
    response.status(200).send(messageValue)

  } catch (err) {
    logger.error(err)
    response.status(500).send('An error occurred.')
  }
}

// Register - REST based
// app.post('/register', (req, res) => standardForwarder(req, res, identityApi, '/register'))

//Register - KAFKA based
app.post('/register', (req, res) => 
kafkaHandler(
  config.topic_user_register_request, 
  config.topic_user_register_response,
  req, 
  res
  )
)

// Login
// app.post('/login', (req, res) => standardForwarder(req, res, identityApi, '/login'))

// Login - KAFKA based
app.post('/login', (req, res) => 
kafkaHandler(
  config.topic_user_login_request, 
  config.topic_user_login_response,
  req, 
  res 
  )
)
// app.post('/login', (req, res) => kafkaHandler(config.topic_user_login_request, req, res, login_response))

// User-History + (Ingestor - Storm Clustering - Forecast)
app.post('/forecast', (req, res) => {
  asyncForwarder(req, historyApi, 'save')
  standardForwarder(req, res, ingestorApi)
})

app.get('/history', async (req, res) => {
  try {
    const data = req.body
    const response = await historyApi.post('get', data)
    return res.status(200).json(response.data)
  } catch (err) {
    logger.error(err.message)
    res.status(400).json(err.message)
  }
})

logger.level = 'debug'
server.listen(3000, () => logger.info(`Service started: Server listening on ${server.address().port}`))
