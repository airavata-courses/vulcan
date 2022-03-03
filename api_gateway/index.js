const express = require('express')
const app = express()
const server = require('http').createServer(app)
const cors = require('cors')
const bodyParser = require('body-parser')
app.use(cors())
app.use(bodyParser.urlencoded({ extended: false }));
app.use(bodyParser.json())

const { identityApi, historyApi, ingestorApi } = require('./config/api')
const logger = require('./config/logging')

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
    const response = await requestForwarder(apiInstance, endpoint, data)
    res.status(200).json(response.data)
  } catch (err) {
    res.status(500).send(err.message)
  }
}

// Register
app.post('/register', (req, res) => res.standardForwarder(req, res, identityApi, '/register'))

// Login
app.post('/login', (req, res) => standardForwarder(req, res, identityApi, '/login'))

// User-History + (Ingestor - Storm Clustering - Forecast)
app.post('/forecast', (req, res) => {
  asyncForwarder(req, historyApi, 'save')
  standardForwarder(req, res, ingestorApi)
})

app.get('/history', async (req, res) => {
  const data = req.params
  const response = await historyApi.get('get', data)
  res.status(200).json(response.data)
})

logger.level = 'debug'
server.listen(3000, () => logger.info(`Service started: Server listening on ${server.address().port}`))
