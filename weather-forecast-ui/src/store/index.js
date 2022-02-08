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
    setAccessToken(store, accessToken) {
      store.isSessionActive = true;
      api.interceptors.request.use(
        (config) => {
          config.headers = {
            Authorization: `Bearer ${accessToken}`,
            Accept: 'application/json',
          };
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
        email: username,
        password,
      });
      const { token, ...user } = data;
      ctx.commit('setAccessToken', token);
      ctx.commit('setUserDetails', user);
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
      date,
      coords,
      onMessage,
    }) {
      const message = JSON.stringify({
        year,
        month,
        date,
        coords,
      });
      console.info(message);
      const ws = new WebSocket(process.env.VUE_APP_WS_BASE_URL);
      ws.onmessage = (event) => {
        onMessage(event.data);
        ws.close();
      };
      return new Promise((resolve, reject) => {
        ws.onopen = (event) => {
          ws.send(message);
          resolve(event.data);
        };
        ws.onerror = (event) => reject(event.data);
      });
    },
  },
});
