<template>
  <div class="d-flex pa-2 align-center">
    <v-icon>
      mdi-clock
    </v-icon>
    <label class="v-label theme--light pa-2">
      Time</label>
    <v-range-slider :min="rangeMin" :max="rangeMax - rangeStep" :step="rangeStep" v-model="range" hide-details
      @input="$emit('input', $event)" :disabled="disabled">
      <template v-slot:prepend>
        <div class="pa-2 grey lighten-3 rounded">
          {{ rangeSliderLabel(range[0]) }}
        </div>
      </template>
      <template v-slot:append>
        <div class="pa-2 grey lighten-3 rounded">
          {{ rangeSliderLabel(range[1]) }}
        </div>
      </template>
      <template v-slot:thumb-label="props">
        <v-icon dark>
          {{ rangeSliderIcon(props.value) }}
        </v-icon>
      </template>
    </v-range-slider>  </div>
</template>

<script>
export default {
  props: {
    disabled: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      rangeMin: 0,
      rangeMax: 24 * 60,
      rangeStep: 15,
      range: [0, 0],
    };
  },
  computed: {
    // Old code to provide all slider labels
    // tickLabels() {
    //   const MIN_TO_MS = 60 * 1000;
    //   const tzOffset = new Date().getTimezoneOffset() * MIN_TO_MS;
    //   return Array(Math.floor((this.rangeMax - this.rangeMin) / this.rangeStep))
    //     .fill()
    //     .map((_, index) => {
    //       const milliseconds = index * this.rangeStep * MIN_TO_MS;
    //       const timeString = new Date(milliseconds + tzOffset).toTimeString().slice(0, 5);
    //       return timeString;
    //     });
    // },
  },
  methods: {
    rangeSliderIcon(minutes) {
      const hour = minutes / 60;
      if (hour >= 5 && hour < 8) {
        return 'mdi-weather-sunset-up';
      } if (hour >= 8 && hour < 16) {
        return 'mdi-weather-sunny';
      } if (hour >= 16 && hour < 19) {
        return 'mdi-weather-sunset-down';
      }
      return 'mdi-weather-night';
    },
    rangeSliderLabel(minutes) {
      const MIN_TO_MS = 60 * 1000;
      const tzOffset = new Date().getTimezoneOffset() * MIN_TO_MS;
      const milliseconds = minutes * MIN_TO_MS;
      const timeString = new Date(milliseconds + tzOffset).toTimeString().slice(0, 5);
      return timeString;
    },
  },
  mounted() {
    const now = new Date();
    const minutesElapsed = now.getHours() * 60 + now.getMinutes();
    const roundedTime = Math.round(minutesElapsed / this.rangeStep) * this.rangeStep;
    const start = Math.max(roundedTime - 120, 0);
    const end = Math.min(start + 240, this.rangeMax - this.rangeStep);
    this.range.splice(0, this.range.length, start, end);
  },
};

</script>
