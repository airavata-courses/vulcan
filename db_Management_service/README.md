# Database Management Service

- Service communicated by other microservices to communicate to a common Database

## Tech Stack used:
- Spring boot
- Maven
- JPA
- Lombok
- MySQL
- Java 17
- grpc

## Pre-Requisites:

- Docker and docker compose pre installed
- openjdk 17

## Setup:

1. Run the docker compose file with the following command:
                
                docker-compose up -d
                
2. Then run the following command to build the jar file

                ./mvwn clean install

3. To run the application

                java -jar ./target/DbMgmtService-0.0.1-SNAPSHOT.jar
                
4. The application is now hosted on port 8051.            
