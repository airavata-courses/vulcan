# maven building and packaging
FROM maven:3.6.0-jdk-11-slim AS build

COPY ./src /usr/src/app/src
COPY ./pom.xml /usr/src/app

# build the jar file  
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

FROM openjdk:11-stretch

COPY --from=build /src/usr/src/app/target/*.jar /usr/src/app/user-management.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/usr/app/user-management.jar"]
