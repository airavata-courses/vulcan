docker stop nexrad-ingestor-service
docker rmi vulcans/nexrad-ingestor-service
docker build --no-cache -t vulcans/nexrad-ingestor-service . &&
docker-compose up
