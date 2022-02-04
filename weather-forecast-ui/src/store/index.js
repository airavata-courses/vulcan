import Vue from 'vue';
import Vuex from 'vuex';
import api from '../api';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    user: {
      firstName: null,
      lastName: null,
      email: null,
    },
  },
  mutations: {
    setAccessToken(ctx, accessToken) {
      api.interceptors.request.use(
        async (config) => {
          config.headers = {
            Authorization: `Bearer ${accessToken}`,
            Accept: 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded',
          };
          return config;
        },
        Promise.reject,
      );
    },
  },
  actions: {
    async authenticate(ctx, { username, password }) {
      const { data } = await api.post('/login', {
        username,
        password,
      });
      ctx.commit('setAccessToken', data.token);
    },

    async createAccount(ctx, {
      firstName, lastName, username, password,
    }) {
      return api.post('/register', {
        firstName,
        lastName,
        username,
        password,
      });
    },

    async fetchWeatherData(ctx, {
      year,
      month,
      day,
      coords,
    }) {
      const message = JSON.stringify({
        year,
        month,
        day,
        coords,
      });
      console.log(message);
      const ws = new WebSocket(process.env.VUE_APP_WS_BASE_URL);
      ws.send(message);
      return ws;
    },
  },
});
