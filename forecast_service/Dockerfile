FROM openjdk:17-slim
COPY target/*.jar forecast-service.jar
ENTRYPOINT ["java", "-jar", "/forecast-service.jar"]