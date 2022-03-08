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

## Project 2 – Scalability

## Goals

### Load testing

- Measure application behaviour and throughput by increasing the load with tools like Apache Jmeter
- Make plots of the metrics obtained and find ways to improve system's effeciency

  #### Load Test plan
  - Hardware resource usage estimation
  - Number of users
  - Expected throughout at each microservice
  - Estimated usage of resources

Scalability

- Understand the fault tolerance of the application
- Deploy the application with Kubernetes cluster in the Jetstream VM
- Determine the performance of the application by scaling with different replications.

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

### Load Test Plan

Using Jmeter, we have tested our application for the following configurations and compared the results.


- 100 requests (10 threads x 10 requests)
- 250 requests (25 threads x 10 requests)
- 500 requests (50 threads x 10 requests)


Standard Implementation Results (Replication Factor - x1 ) –
- 100 requests (10 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157150963-48903b1b-8b99-4633-be24-1be6bd527eeb.png)

In this test, the application response time was in the range of 6-10s, which is acceptable and will serve as a benchmark response time for our application as we increase the load.

![image](https://user-images.githubusercontent.com/96559018/157150737-dfd65406-e934-457b-893f-7f60ef63e3fe.png)

In this test, the application was able to serve an average of 56 requests/min.

- 250 requests (25 threads x 10 requests each)

![image](https://user-images.githubusercontent.com/96559018/157151057-5f62605a-700b-4a13-8c6b-ff25aea35529.png)

For 25 threads i.e. 25 concurrent users, the response time increased to 24-30 seconds, which is unacceptable considering our benchmark response time of 6-10 seconds.

![image](https://user-images.githubusercontent.com/96559018/157151072-272094d1-9c67-4589-9bdb-9bf102bb7f18.png)

The throughput increased slightly to 61 requests/min, which is due to the increase in incoming requests. 

- 500 requests (50 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157151114-92495c45-d990-4dc4-acbf-8d7f2a7ba0a8.png)

For 50 threads i.e. 50 concurrent users, the response time increased to 60 seconds, indicating a failed test.


![image](https://user-images.githubusercontent.com/96559018/157151134-962f2b1c-4a0c-4938-b61f-2cd970f142db.png)

The throughput for this test was 56 requests/min.

### Results Summary for Load Test on Standard Implementation

- Valid Response Time for application - 6-10s
- Application can withstand upto 10 concurrent users making 10 requests each and keep the response time under 10s for each request.
- The performance starts to deteriorate as the load is increased above 10 threads, resulting in significant increase in response times.

 ### Results with 3x Scaled Services - 

- 100 requests (10 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157151176-688bb2cf-4f4b-4bb9-a2cd-ecdace94a444.png)

When the services are scaled to 3x, the response time improves considerably to 1-4 seconds, which is well under our defined benchmark response time.

![image](https://user-images.githubusercontent.com/96559018/157151191-064f133c-5b4f-41a7-83aa-acfa8c1ed5e2.png)

After scaling to 3x, the application was able to serve 200 requests/min.

- 250 requests (25 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157151239-a2d2e40d-b9c5-4bc9-a278-a5f5ceba74ca.png)

After scaling to 3x, the application can handle 25 concurrent users and send response back in under 8 seconds for majority of the 250 requests.

![image](https://user-images.githubusercontent.com/96559018/157151262-cbc81a97-6d70-47b5-ad93-5008905bbfb9.png)

Throughput for this test - 226 requests/min

- 500 requests (50 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157151300-86f41135-3841-4040-aa29-ee4dc9e3a08b.png)

Increasing requests to 500, the application response time increased to 12-14 seconds, which is worse than our defined benchmark of 6-10 seconds, so our application failed this test.

### Results with 5x scaled

- 500 requests (50 threads x 10 requests)

![image](https://user-images.githubusercontent.com/96559018/157151350-fa5a03be-459b-4b3c-9c03-1a930d305b4c.png)

Once scaled to 5x, the application was able to handle 50 concurrent users, and keep the response time for each request under 10 seconds. 

![image](https://user-images.githubusercontent.com/96559018/157151372-9f06ff11-ed4a-4939-85b8-775a2029c1e2.png)

The throughput increased considerably to 330 requests/min.

### Spike Testing Results

For Spike Testing using Jmeter, we used the custom thread group plugin, to create a spike of requests to spike test the application. The request configuration we used is shown below - 

![image](https://user-images.githubusercontent.com/96559018/157151399-6652648e-eb36-4cb9-99bf-0c84ef4169c6.png)

### Test on 1x scaled implementation

![image](https://user-images.githubusercontent.com/96559018/157151436-ffcae5a4-87bf-4547-af39-a54d8dba2e16.png)

Response time graph for spike test with response time in request spike reaching 70 seconds.


### Test on 5x scaled implementation

![image](https://user-images.githubusercontent.com/96559018/157151462-4fc97ebb-2c89-4ce5-918f-90ea0c7bd5e3.png)

Once scaled to 5x, the application was able to handle the spike of 50 concurrent users, and keep the response time around 8-10 seconds.

### Final Summarized Results

![image](https://user-images.githubusercontent.com/96559018/157151492-a7a4dc89-c0ac-4fb4-966c-90f4dc6ce6d8.png)


