# Weather Forecast UI

Project built using Vue.js and MapBox GL for providing interactive maps to display NEXRAD radar data.

A user can submit jobs by to the backend services through the API gateway to fetch information for the selected time, date and location coordinates. Currently only certain parameters from the interface are validated, and most of the data from the heatmap geoJSON uses mock data. The map layers will be updated in future iterations to show a raster layer on the original map for realtime radar data after the backend services have been developed completely.

## Project setup
### Install project dependencies
```
npm install
```
### Build for production
```
npm run build
```

### Hosting the app on a lightweight HTTP server
```
npm install -g serve
npx serve -s dist -p 8085
```

## Development Support
Compiles and hot-reloads
```
npm run serve
```
### Lints and fixes files
```
npm run lint
```

