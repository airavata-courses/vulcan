<template>
  <div id="mapboxgl-container"></div>
</template>

<script>
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';

mapboxgl.accessToken = 'pk.eyJ1IjoiYW1iZXJyYW1lc2giLCJhIjoiY2t6Nmp6bGJvMTE2YjJ2b2Y5ZGV5aGtyMiJ9.4ZAXvkU71l48rhecb0Vs0Q';
export default {
  props: {
    mapCenter: {
      type: Array,
      required: true,
    },
    mapLayer: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      map: null,
    };
  },
  mounted() {
    this.map = new mapboxgl.Map({
      container: 'mapboxgl-container',
      style: 'mapbox://styles/mapbox/streets-v11',
      center: this.mapCenter,
      zoom: 7,
      maxZoom: 12,
      minZoom: 3,
      attributionControl: false,
    });
    this.map.addControl(new mapboxgl.AttributionControl(), 'top-left');
    this.map.addControl(new mapboxgl.FullscreenControl());
    // // Snippet to add raster layer with radar data over
    // // Indiana and neighboring states
    // this.map.on('load', () => {
    //   this.map.addSource('radar', {
    //     type: 'image',
    //     url: 'https://docs.mapbox.com/mapbox-gl-js/assets/radar.gif',
    //     coordinates: [
    //       [-89.425, 44.437],
    //       [-80.516, 44.437],
    //       [-80.516, 35.936],
    //       [-89.425, 35.936],
    //     ],
    //   });
    //   this.map.addLayer({
    //     id: 'radar-layer',
    //     type: 'raster',
    //     source: 'radar',
    //     paint: {
    //       'raster-fade-duration': 0,
    //     },
    //   });
    // });
  },
  watch: {
    mapCenter() {
      this.map.flyTo({ center: this.mapCenter, zoom: 7 });
    },
    mapLayer() {
      console.log('Updating map layer...');
    },
  },
};
</script>

<style>
#mapboxgl-container {
  min-height: 30rem;
  height: 100%;
}
</style>
