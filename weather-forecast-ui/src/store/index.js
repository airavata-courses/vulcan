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
    isSessionActive: false,
  },
  mutations: {
    setAccessToken(ctx, accessToken) {
      api.interceptors.request.use(
        (config) => {
          config.headers = {
            Authorization: `Bearer ${accessToken}`,
            Accept: 'application/json',
            'Content-Type': 'application/x-www-form-urlencoded',
          };
          ctx.isSessionActive = true;
          return config;
        },
        Promise.reject,
      );
    },

    setUserDetails(ctx, { firstName, lastName, email }) {
      Object.assign(ctx.user, {
        firstName,
        lastName,
        email,
      });
    },
  },
  actions: {
    async authenticate(ctx, { username, password }) {
      const { data } = await api.post('/login', {
        username,
        password,
      });
      const { token, ...userDetails } = data;
      ctx.commit('setAccessToken', token);
      ctx.commit('setUserDetails', userDetails);
    },

    async createAccount(ctx, {
      firstName, lastName, username, password,
    }) {
      return api.post('/register', {
        firstName,
        lastName,
        email: username,
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
