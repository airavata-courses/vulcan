# Storm Clustering Service

Service that works in conjunction with the storm detection service (currently inactive) to group events into spatial clusters and trigger a weather forecast.

Currently due to lack of realtime weather data, the coordinates for the GeoJSON is being populated with dummy values to facilitate end-to-end communication between services. Eventually, the service is meant to work with KML or GeoJSON based data to provide data for prediction at the forecast service.

## Project Setup
 This service requires Python 3.8 to be installed.
To install Python, follow this link:
https://www.python.org/downloads/release/python-380/


### Install project dependencies
```
pip install -r requirements.txt
```
### Run the app
```
python3 main.py
```