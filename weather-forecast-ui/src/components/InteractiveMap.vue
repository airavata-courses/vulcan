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
      type: Object,
      default: () => ({}),
    },
  },
  data() {
    return {
      map: null,
      defaultZoom: 7,
    };
  },
  mounted() {
    this.map = new mapboxgl.Map({
      container: 'mapboxgl-container',
      style: 'mapbox://styles/mapbox/streets-v11',
      center: this.mapCenter,
      zoom: this.defaultZoom,
      maxZoom: 12,
      minZoom: 3,
      attributionControl: false,
    });
    this.map.addControl(new mapboxgl.AttributionControl(), 'top-left');
    this.map.addControl(new mapboxgl.FullscreenControl());
    this.map.addControl(new mapboxgl.NavigationControl());
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

    this.map.on('load', () => {
      // Add a geojson point source.
      // Heatmap layers also work with a vector tile source.
      this.map.addSource('nexrad-radar', {
        type: 'geojson',
        data: this.mapLayer,
      });

      this.map.addLayer(
        {
          id: 'radar-heatmap',
          type: 'heatmap',
          source: 'nexrad-radar',
          paint: {
            // Increase the heatmap weight based on frequency and property magnitude
            'heatmap-weight': [
              'interpolate',
              ['linear'],
              ['get', 'mag'],
              0, 0,
              6, 1,
            ],
            // Increase the heatmap color weight weight by zoom level
            // heatmap-intensity is a multiplier on top of heatmap-weight
            'heatmap-intensity': [
              'interpolate',
              ['linear'],
              ['zoom'],
              0, 1,
              12, 3,
            ],
            // Color ramp for heatmap.  Domain is 0 (low) to 1 (high).
            // Begin color ramp at 0-stop with a 0-transparancy color
            // to create a blur-like effect.
            'heatmap-color': [
              'interpolate',
              ['linear'],
              ['heatmap-density'],
              0,
              'rgba(33,102,172,0)',
              0.2,
              'rgb(103,169,207)',
              0.4,
              'rgb(209,229,240)',
              0.6,
              'rgb(253,219,199)',
              0.8,
              'rgb(239,138,98)',
              1,
              'rgb(178,24,43)',
            ],
            'heatmap-radius': [
              'interpolate',
              ['linear'],
              ['zoom'],
              0, 2,
              9, 20,
            ],
            'heatmap-opacity': [
              'interpolate',
              ['linear'], ['zoom'],
              7, 1,
              12, 0.2,
            ],
          },
        },
        'waterway-label',
      );
    });
  },
  watch: {
    mapCenter() {
      this.map.flyTo({ center: this.mapCenter, zoom: this.defaultZoom });
    },
    mapLayer: {
      deep: true,
      handler() {
        this.map.getSource('nexrad-radar').setData(this.mapLayer);
      },
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
