# Vulcan

## About
Repository for the course project for CSCI-B 649 Applied Distributed Systems in Spring 2022. This project is based on a multi-user system with a distributed architecture for consuming and processing weather data in various forms, logging its usage and presenting the weather information to the user in a web-based interface.

The project is in its initial stages of development and the README will be updated as new features are added.

## Team

- Akshat Arvind
- Amber Ramesh
- Ratchahan Anbarasan

# Project 1

## System Architecture

![image](https://user-images.githubusercontent.com/96559018/152918648-57a1ad5e-b9a4-4d73-9b40-77c584aaae17.png)

## Tech Stack

- Spring boot
- Maven
- JPA
- Lombok
- MySQL
- Java 17
- grpc
- kafka
- Python 3
- Flask
- Docker
- Express
- NodeJS
- Web Sockets

## Installation Procedure:

### Pre-requisites:

- Docker
- openjdk 17

### Installation Order

1. Kafka and Zookeeper server
2. MySQL Database
3. Mongo Database
4. Services

# Project 2

## Design Plan

<img width="1167" alt="image" src="https://user-images.githubusercontent.com/96559018/156868986-0741bfc0-d23d-4d7a-9a93-a13be8de1ce5.png">

## Kubernetes

- Minikube cluster is used to deploy scale each of the microservices. The application was tested for various amounts of load with Apache Jmeter. 

## Pre-requisites

- Minikube cluster with kubectl installed and configured to use your cluster
- Docker cli installed, you must be signed into your Docker Hub account

## Setup

- Run the following command deploy the application in the kubernetes cluster

                kubectl apply -f kubernetes/. --recursive

- To manually scale the application to 3 replicas run the following script file with -r as 3

                sh kube-scale.sh -r 3

---
## Jmeter 

[Jmeter Test details and Report](https://github.com/airavata-courses/vulcan/tree/develop/Jmeter)
