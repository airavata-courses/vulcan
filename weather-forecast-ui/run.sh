docker rm user-interface
docker rmi vulcans/user-interface
docker build -t vulcans/user-interface . &&
docker-compose up
