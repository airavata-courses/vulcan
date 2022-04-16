<template>
  <div id="mapboxgl-container"></div>
</template>

<script>
import mapboxgl from 'mapbox-gl';
import 'mapbox-gl/dist/mapbox-gl.css';
import {
  RadarExtent, LayerSource, SatelliteExtent, MAP_LAYER_PLACEHOLDER_URL,
} from '../constants/forecast';

mapboxgl.accessToken = 'pk.eyJ1IjoiYW1iZXJyYW1lc2giLCJhIjoiY2t6Nmp6bGJvMTE2YjJ2b2Y5ZGV5aGtyMiJ9.4ZAXvkU71l48rhecb0Vs0Q';
export default {
  props: {
    mapCenter: {
      type: Array,
      required: true,
    },
    selectedLayerSource: {
      type: String,
      required: true,
    },
    mapLayerUrl: {
      type: String,
      default: MAP_LAYER_PLACEHOLDER_URL,
    },
  },
  data() {
    return {
      map: null,
      defaultZoom: 7,
    };
  },
  methods: {
    addRasterLayer(layerName) {
      this.map.addSource(layerName, {
        type: 'image',
        url: MAP_LAYER_PLACEHOLDER_URL,
        coordinates: [[-1, -1], [1, -1], [-1, 1], [1, 1]],
      });

      this.map.addLayer({
        id: `${layerName}-layer`,
        type: 'raster',
        source: layerName,
        paint: {
          'raster-fade-duration': 0,
        },
      });
    },
    resetRasterLayers() {
      Object.values(LayerSource).forEach((layer) => {
        this.map
          .getSource(layer)
          .updateImage({
            url: MAP_LAYER_PLACEHOLDER_URL,
          });
      });
    },
    updateSelectedRasterLayer() {
      switch (this.selectedLayerSource) {
        case LayerSource.Radar:
          this.updateRadarLayer(this.mapLayerUrl);
          break;
        case LayerSource.Satellite:
          this.updateSatelliteLayer(this.mapLayerUrl);
          break;
        default:
      }
    },
    updateRadarLayer(mapLayerUrl) {
      const [long, lat] = this.mapCenter;
      this.map
        .getSource(LayerSource.Radar)
        .updateImage({
          url: mapLayerUrl,
          coordinates: [
            [long - RadarExtent.Longitude, lat + RadarExtent.Latitude],
            [long + RadarExtent.Longitude, lat + RadarExtent.Latitude],
            [long + RadarExtent.Longitude, lat - RadarExtent.Latitude],
            [long - RadarExtent.Longitude, lat - RadarExtent.Latitude],
          ],
        });
    },
    updateSatelliteLayer(mapLayerUrl) {
      this.map
        .getSource(LayerSource.Satellite)
        .updateImage({
          url: mapLayerUrl,
          coordinates: [
            [SatelliteExtent.LongitudeMin, SatelliteExtent.LatitudeMax],
            [SatelliteExtent.LongitudeMax, SatelliteExtent.LatitudeMax],
            [SatelliteExtent.LongitudeMax, SatelliteExtent.LatitudeMin],
            [SatelliteExtent.LongitudeMin, SatelliteExtent.LatitudeMin],
          ],
        });
    },
  },
  mounted() {
    this.map = new mapboxgl.Map({
      container: 'mapboxgl-container',
      style: 'mapbox://styles/mapbox/dark-v10',
      center: this.mapCenter,
      zoom: this.defaultZoom,
      maxZoom: 12,
      minZoom: 3,
      attributionControl: false,
      projection: {
        name: 'mercator',
      },
    });
    this.map.addControl(new mapboxgl.AttributionControl(), 'top-left');
    this.map.addControl(new mapboxgl.FullscreenControl());
    this.map.addControl(new mapboxgl.NavigationControl());
    // Add raster layers for registered sources
    this.map.on('load', () => {
      Object.values(LayerSource).forEach((layer) => {
        this.addRasterLayer(layer);
      });
    });
  },
  watch: {
    mapCenter(oldVal, newVal) {
      if (JSON.stringify(oldVal) === JSON.stringify(newVal)) return;
      this.map.flyTo({ center: this.mapCenter, zoom: this.defaultZoom });
    },
    selectedLayerSource() {
      this.resetRasterLayers();
      this.updateSelectedRasterLayer();
    },
    mapLayerUrl: {
      handler() {
        this.updateSelectedRasterLayer();
      },
    },
  },
};
</script>

<style>#mapboxgl-container {
  min-height: 30rem;
  height: 100%;
}</style>
