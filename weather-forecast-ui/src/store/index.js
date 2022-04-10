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

    async fetchRadarData(ctx, {
      startTime,
      endTime,
      longitude,
      latitude,
    }) {
      const request = {
        startTime,
        endTime,
        longitude,
        latitude,
        token: ctx.state.accessToken,
      };
      // Sample request:
      // const request = {
      //   startTime: '2014-07-03T18:00:00.000Z',
      //   endTime: '2014-07-03T19:00:00.000Z',
      //   longitude: -76,
      //   latitude: 36.5,
      // };
      const response = await fetch(`${process.env.VUE_APP_WEB_API_BASE_URL}/weather/radar`, {
        headers: {
          Accept: 'application/x-zip-compressed',
          'Content-Type': 'application/json',
        },
        method: 'POST',
        body: JSON.stringify(request),
      });
      return response.blob();
    },
  },
});
