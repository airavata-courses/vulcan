const axios = require('axios').default

const identityApi = axios.create({
  baseURL: 'http://localhost:8081/api/user',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})
const historyApi = axios.create({
  baseURL: 'http://localhost:34535/api/history',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})
const ingestorApi = axios.create({
  baseURL: 'http://localhost:12552',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

module.exports = {
  identityApi,
  historyApi,
  ingestorApi
}