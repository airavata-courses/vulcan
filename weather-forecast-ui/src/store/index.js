import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
  },
  mutations: {
  },
  actions: {
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
