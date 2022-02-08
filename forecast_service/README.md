# Forecast Management Service

- Service that returns the forecast data to the gateway service via kafka

## Technology Stack:
- Spring boot
- Maven
- Lombok
- Java 17
- kafka

## Pre-Requisites:

- openjdk 17

## Setup:

1. Then run the following command to build the jar file

                ./mvwn clean install

2. To run the application

                java -jar ./target/forecast_service-0.0.1-SNAPSHOT.jar
                
3. The application is now hosted on port 8050.            
