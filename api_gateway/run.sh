docker rm api-gateway
docker rmi vulcans/api-gateway
docker build --tag vulcans/api-gateway . &&
docker-compose up
