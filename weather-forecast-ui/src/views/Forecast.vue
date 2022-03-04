<template>
  <v-container fill-height :style="{ 'max-width': '1200px' }">
    <v-layout row align-center align-content-center pb-16>
      <v-flex xs12>
        <InteractiveMap :map-center="mapCenter | jsonParse" :map-layer="mapLayer"></InteractiveMap>
      </v-flex>
      <v-flex xs12>
        <TimeSlider></TimeSlider>
      </v-flex>
      <v-flex shrink pa-2>
        <v-menu
          ref="dateMenu"
          v-model="dateMenu"
          :close-on-content-click="false"
          :nudge-right="40"
          transition="scale-transition"
          offset-y
          min-width="auto"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              label="Date"
              v-model="selectedDate"
              prepend-icon="mdi-calendar"
              filled
              rounded
              readonly
              hide-details
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            v-model="selectedDate"
            @input="dateMenu = false"
          >
          </v-date-picker>
        </v-menu>
      </v-flex>
      <v-flex shrink pa-2>
        <v-autocomplete
        label="Location"
        v-model="mapCenter"
        prepend-icon="mdi-map-search"
        item-text="place"
        item-value="coords"
        :items="locations"
        filled
        rounded
        hide-details
        ></v-autocomplete>
      </v-flex>
      <v-spacer />
      <v-flex shrink>
        <v-btn
        depressed
        color="primary"
        :loading="loading"
        :disabled="loading"
        @click="fetch"
        >Fetch</v-btn>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from 'vuex';
import InteractiveMap from '../components/InteractiveMap.vue';
import TimeSlider from '../components/TimeSlider.vue';
import { CityNames, MapCenter } from '../constants/forecast';
import sampleGeoJson from '../constants/sampleGeoJson.json';

export default {
  name: 'Forecast',
  components: {
    InteractiveMap,
    TimeSlider,
  },
  data() {
    return {
      dateMenu: false,
      selectedDate: new Date().toISOString().slice(0, 10),
      mapCenter: JSON.stringify(MapCenter.BL),
      mapLayer: sampleGeoJson,
      locations: Object.entries(MapCenter)
        .map(([place, coords]) => ({ place: CityNames[place], coords: JSON.stringify(coords) }))
        .sort((a, b) => a.place.localeCompare(b.place)),
      loading: false,
    };
  },
  methods: {
    ...mapActions(['fetchWeatherData']),
    async fetch() {
      this.loading = true;
      try {
        const [year, month, date] = this.selectedDate.split('-');
        const coords = JSON.parse(this.mapCenter);
        const geoJson = await this.fetchWeatherData({
          year,
          month,
          date,
          coords,
        });
        this.updateMapLayer(geoJson);
      } catch (err) {
        console.error(err);
        this.loading = false;
      }
    },
    updateMapLayer(geoJson) {
      this.loading = false;
      console.info(geoJson);
      Object.assign(this.mapLayer, geoJson);
    },
  },
  filters: {
    jsonParse(value) {
      return JSON.parse(value);
    },
  },
};
</script>
