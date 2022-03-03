const axios = require('axios').default

const identityApi = axios.create({
  baseURL: 'http://user-management-service:8080/api/user',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})
const historyApi = axios.create({
  baseURL: 'http://user-history:8080/api/history',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})
const ingestorApi = axios.create({
  baseURL: 'http://http-ingestor-service:8080',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

module.exports = {
  identityApi,
  historyApi,
  ingestorApi
}