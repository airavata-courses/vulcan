const MapCenter = Object.freeze({
  SF: [-122.419418, 37.774929],
  SEA: [-122.332069, 47.606209],
  BL: [-86.51599513135886, 39.164571048470805],
  ORD: [-87.629799, 41.878113],
  NYC: [-74.005974, 40.712776],
  MIA: [-80.191790, 25.761680],
});

const CityNames = Object.freeze({
  SF: 'San Francisco, CA',
  SEA: 'Seattle, WA',
  BL: 'Bloomington, IN',
  ORD: 'Chicago, IL',
  NYC: 'New York City, NY',
  MIA: 'Miami, FL',
});

const LayerSource = Object.freeze({
  Radar: 'radar',
  Satellite: 'satellite',
});

const RadarExtent = Object.freeze({
  Longitude: 10,
  Latitude: 5,
});

const SatelliteExtent = Object.freeze({
  LongitudeMin: -180,
  LongitudeMax: 180,
  LatitudeMin: -90,
  LatitudeMax: 90,
});

const SatelliteParameters = Object.freeze({
  Temperature: 'T',
  Ozone: 'O3',
  Humidity: 'RH',
});

const MAP_LAYER_PLACEHOLDER_URL = 'https://upload.wikimedia.org/wikipedia/commons/c/ca/1x1.png';

export {
  MapCenter,
  CityNames,
  LayerSource,
  RadarExtent,
  SatelliteExtent,
  SatelliteParameters,
  MAP_LAYER_PLACEHOLDER_URL,
};
