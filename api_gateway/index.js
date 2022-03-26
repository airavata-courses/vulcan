const express = require('express')
const app = express()
const server = require('http').createServer(app)
const cors = require('cors')
const bodyParser = require('body-parser')
app.use(cors())
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json())

const { identityApi, historyApi, ingestorApi } = require('./config/api')
const { producer, register_response, login_response } = require('./config/kafka-config')
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

const kafkaHandler = async function (topic, request, response, consumer) {
  const data = JSON.stringify(request.body)
  let payloads = [
    {
      topic,
      messages: data
    }
  ];

  try {
    const messageValue = await new Promise((resolve, reject) => {
      producer.send(payloads, (err) => {
        if (err) {
          logger.error(err)
        }
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
app.post('/register', (req, res) => kafkaHandler(config.topic_user_register_request, req, res, register_response))

// Login
app.post('/login', (req, res) => standardForwarder(req, res, identityApi, '/login'))

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
