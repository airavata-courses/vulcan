import axios from 'axios';
import qs from 'qs';

const instance = axios.create({
  baseURL: process.env.VUE_APP_WEB_API_BASE_URL,
  paramsSerializer: (params) => qs.stringify(params, { arrayFormat: 'repeat', allowDots: true }),
});

export default instance;
