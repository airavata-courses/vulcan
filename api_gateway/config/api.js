const axios = require('axios').default

const nexradApi = axios.create({
  baseURL: 'http://nexrad-ingestor-service:8080',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

const merraApi = axios.create({
  baseURL: 'http://merra-ingestor-service:8080',
  headers: {
    'Content-Type': 'application/json; charset=utf-8'
  }
})

module.exports = {
  nexradApi,
  merraApi
}