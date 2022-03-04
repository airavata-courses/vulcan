import Vue from 'vue';
import Vuex from 'vuex';
import api from '../api';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    accessToken: null,
    user: {
      firstName: null,
      lastName: null,
      email: null,
    },
  },
  getters: {
    isSessionActive(state) {
      return !!state.accessToken;
    },
  },
  mutations: {
    setAccessToken(store, accessToken) {
      store.accessToken = accessToken;
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
        emailId: username,
        password,
      });
      const { token } = data;
      if (!token) {
        throw new Error('Invalid credentials');
      }
      const { sub } = JSON.parse(decodeURIComponent(Buffer.from(token.split('.')[1], 'base64')));
      window.localStorage.setItem('accessToken', token);
      ctx.commit('setAccessToken', token);
      ctx.commit('setUserDetails', { email: sub });
      Object.assign(api.defaults.headers.common, {
        Authorization: `Bearer ${token}`,
        'Content-Type': 'application/json',
      });
    },

    retrieveSavedToken(ctx) {
      const token = window.localStorage.getItem('accessToken');
      if (token) {
        ctx.commit('setAccessToken', token);
        return true;
      }
      return false;
    },

    async createAccount(ctx, {
      firstName, lastName, username, password,
    }) {
      return api.post('/register', {
        firstName,
        lastName,
        emailId: username,
        password,
      });
    },

    async fetchWeatherData(ctx, {
      year,
      month,
      date,
      coords,
    }) {
      const request = {
        year,
        month,
        date,
        coords,
        token: ctx.state.accessToken,
      };
      const response = await api.post('/forecast', request);
      return response.data;
    },
  },
});
