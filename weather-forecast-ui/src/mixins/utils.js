export default {
  methods: {
    minutesTimeString(minutes) {
      const MIN_TO_MS = 60 * 1000;
      const tzOffset = new Date().getTimezoneOffset() * MIN_TO_MS;
      const milliseconds = minutes * MIN_TO_MS;
      const timeString = new Date(milliseconds + tzOffset).toTimeString().slice(0, 5);
      return timeString;
    },
  },
};
