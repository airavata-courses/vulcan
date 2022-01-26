Feature List -

++ User Interface
- Login page
- Input parameters for API
- Establish WebSocket communication with API

++ API Gateway - Authentication
- JWT-based login (custom or libraries from Auth0 etc.)
- Add middleware to support request authentication

+++ API Gateway - Cache Requests
- Explore language-specific in-memory cache
OR integrate with Redis/Memcached
Examples:
https://spring.io/guides/gs/caching/
https://pypi.org/project/fastapi-cache2/
- Establish communication with
-- client using WebSockets
-- forecast service (maybe using gRPC?)

+++ Message Queue
- Explore with PoC using Kafka/RabbitMQ to understand usage

+++ Auditing Service
- Explore with PoC using logstash to understand usage

++ Data Ingestion
- Read parameters from MQ to download corresponding file
- Integrate with appropriate DB for storing files (probably NoSQL)
- Output file URL

+ Storm Detection
- Extract gzipped file from NEXRAD for reading weather data
Example:
http://nbviewer.jupyter.org/gist/dopplershift/356f2e14832e9b676207
- Output KML
https://developers.google.com/kml/documentation/kml_tut

+ Storm Clustering
- Spatial clustering (using previous service's results)

+ Forecast Trigger
- Read cluster data to run forecasts if needed

+ Forecast Service
- Return forecast results to the API

Last 4 are still open ended, need to look at data to understand specific functionality