# Vulcan

## About
Repository for the course project for CSCI-B 649 Applied Distributed Systems in Spring 2022. This project is based on a multi-user system with a distributed architecture for consuming and processing weather data in various forms, logging its usage and presenting the weather information to the user in a web-based interface.

The project is in its initial stages of development and the README will be updated as new features are added.

# Project 1

## Architecture Diagram

<img width="914" alt="Screen Shot 2022-03-04 at 11 31 24 PM" src="https://user-images.githubusercontent.com/96559018/156868170-b6e0576d-5004-467c-a25e-c937a41b8520.png">

## Napkin Diagram

<img width="473" alt="image" src="https://user-images.githubusercontent.com/96559018/156868201-3a14886d-9171-4d79-a74a-cc4e4ba117db.png">

## Technology stack

The microservices from the architecture above is built with the following tech stack

- Java 17
- SpringBoot
- Maven
- Lombok
- JPA
- Python 3
- FastAPI
- Express
- NodeJS
- MongoDB
- PostgresSQL
- Docker

## Pre-requisites

- Docker 

## Setup

The application is containerized and is hosted in docker registry. To setup the application run the [docker-compose](https://github.com/airavata-courses/vulcan/blob/main/docker-compose.yml) file.

## Sample GIF

# Project 2

## Design Plan

<img width="1167" alt="image" src="https://user-images.githubusercontent.com/96559018/156868986-0741bfc0-d23d-4d7a-9a93-a13be8de1ce5.png">

## Kubernetes

- Minikube cluster is used to deploy scale each of the microservices. The application was tested for various amounts of load with Apache Jmeter. 

## Pre-requisites

- Minikube cluster with kubectl installed and configured to use your cluster
- Docker cli installed, you must be signed into your Docker Hub account

## Setup

- Navigate to this [folder](https://github.com/airavata-courses/vulcan) and run the following command deploy the application in the Kubernetes cluster

                kubectl apply -f kubernetes/. --recursive

- To manually scale the application to 3 replicas run the following [kube-scale.sh](https://github.com/airavata-courses/vulcan/blob/main/kube-scale.sh) script file with -r as 3

                sh kube-scale.sh -r 3

---
## Jmeter 

[Jmeter Test details and Report](https://github.com/airavata-courses/vulcan/tree/develop/Jmeter)
