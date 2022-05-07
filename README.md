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

                bash kube-scale.sh -r 3

---
## Jmeter 

[Jmeter Test details and Report](https://github.com/airavata-courses/vulcan/tree/develop/Jmeter)

# Project 3

## Architecture

![project-3-architecture](https://user-images.githubusercontent.com/96559018/162100682-1af10e73-7cfe-42f2-a3a4-9a464f72bc10.png)

- The REST based communication between the services from project 2 is replaced with Kafka based message streaming.


## Continuous Integration / Continuous Deployment

![project-3-ci-cd-pipeline](https://user-images.githubusercontent.com/96559018/162099242-5d2c3790-b086-4762-97dd-3afb9e1cc17e.png)

- Github Actions were used for Continuous integration and deployment.
- On pushing the code to Github the Github workflow is triggered to build and push the image to registry and simultaneously a rolling update is made on the Kubernetes cluster in the remote server.

## Branch setup for the CI/CD pipeline

![project-3-branch-pipeline](https://user-images.githubusercontent.com/96559018/162100336-09933416-4cd9-4a7f-8e03-8ac0dfb6afb1.png)

- Each service has a feature and a development branch.
- The initial development for the service is done in the feature branch and on completion it is pushed to the develop branch
- A push to the develop branch triggers a workflow where the Github actions are implemented to build, create image and push the image to docker registry.
- The individual service development branches are merged to the develop branch and from there on push to the main branch an ssh connection is made to the remote server where the Kubernetes cluster is hosted and a rolling update is made.
- All the credentials and sensitive data are stored in the Github secrets.

## Launching Jetstream Instances with Ansible and K8S setup

### Plan of action:
* Using terraform and OpenStack CLI we will create the instances. (master and worker nodes).
* Using Ansible playbooks we will connect to the above instances and run the playbooks to setup kubernetes.
* Finally connect to the master node and run Kubernetes yaml files for starting deployments and services. 

### Steps:
We need an instance to set up Ansible first. This can be a local or a remote machine. We chose a remote machine with Ubuntu image. Login to the machine and open the terminal
1. Create SSH-key.  
```
sudo su
ssh-keygen
```
This will generate public and private keys. we will use this key later to SSH connect to the instances we create.

2. Copy the openrc.sh file to this instance. You can follow the steps in this [Link]([open-rc-setup](https://docs.jetstream-cloud.org/ui/cli/openrc/)) to download the openrc file.  And then Run the following command,
``` 
source <openrc.sh>
```  
This will just save the environment variables to our localhost which we need to connect to the OpenStack python client. 
   
3. Make sure you have a public IP available. This can be created in the exosphere environment or running the following command. Ignore this step if you have an public IP address available.
```
pip3 install python-openstackclient
openstack floating ip create public
```
3. Paste the IP address you created in the first line below and run the following code. 
We need to modify certain attributes from the [cluster.tfvars](https://github.com/ratchahan-anbarasan/jetstream_kubespray/blob/branch_v2.15.0/inventory/kubejetstream/cluster.tfvars) file
 * master node IP address (k8s_master_fips)
 * availability zones(az_list)
 * external network(external_net)
 * number of nodes (number_of_k8s_nodes)  

**Note**: The shell script below only modifies the IP address and the rest of the above attributes are already set based on the cloud environment provided to us. 
```
  export IP=149.165.152.125
  bash instance-creation.sh
```
What does the [instance-creation.sh](https://github.com/airavata-courses/terra/blob/ansible-kub-setup/instance-creation.sh) shell script do?
  * It will install terraform, ansible. 
  * Initiate the terraform scripts for creation of the VM instances.
  * Setup SSH as an agent to connect to the remote machines
  * Run ansible playbooks to install kubernetes.

4. Kubernetes is installed successfully in our cluster( at the end of the above script you will be logged into the master node) and all the necessary deployment files are copied to the master node. 
```
cd deploy
bash deploy.sh
```

5. To test the setup, you can ssh to the master machine and check the nodes connected. (Optional)
```
ssh ubuntu@$IP
sudu su
kubectl get nodes
```

## REFERENCE

- The ansible implementation to setup jetstream instance and Kubernetes Cluster is adapted from [Team-terra](https://github.com/airavata-courses/terra/wiki/Launching-instances-on-Jetstream-using-Ansible-and-setting-up-Kubernetes) 's implementation


# Project 4

## Case Study - Apache Airavata Custos

## About:

Custos is a software framework that provides common security operations for science gateways, including user identity and access management, gateway tenant profile management, resource secrets management, and groups and sharing management. It is implemented with scalable microservice architecture that can provide highly available, fault-tolerant operations. Custos exposes these services through a language-independent API that encapsulates science gateway usage scenarios.

For this project the Custos framework was initially deployed on Jetstream instances and later stress and load testing was done based on the testing strategies mentioned in the course lecture. 

## Setup:

### Jetstream-1:

1. Create a ssh key pair in your local machine and copy the public key to the Jetstream settings 
(https://use.jetstream-cloud.org/application/settings). So that when a new instance is created your local machine’s public key will be registered for authorized keys.

2. Spawn five Jetstream - 1 Ubuntu 20.04 LTS machines of medium size and with the following configurations

<img width="421" alt="image" src="https://user-images.githubusercontent.com/96559018/167050784-b5e4bb9c-04a0-4e40-ae1b-1f7b0e8d9996.png">

3. Among the five instances one is used for deployment purpose, one to setup rancher and three others are used for Kubernetes master and worker nodes.

<img width="625" alt="image" src="https://user-images.githubusercontent.com/96559018/167050815-04afb6f6-e475-4ce5-83f1-9c1858a4260b.png">

### Rancher:

1. download the cloudman folder from this link (https://airavata.slack.com/files/U030JR7JXDF/ F03CA28HZ6J/cloudman.zip)
2. Open the sample ini configuration file from cloudman/cloudman-boot/inventory and include the following changes.
•	Update the agent and controllers to the domain name of your Jetstream 1 instance used for rancher setup (In this case the rancher instance ip is 149.165.168.199 and the domain name is js-168-199.jetstream-cloud.org).
•	Update ansible user to your xsede username.
•	Enter the location to your private key in your local machine.

<img width="532" alt="image" src="https://user-images.githubusercontent.com/96559018/167050747-1e10ffa2-d1b8-4470-aee9-bfeeda3df97b.png">

3. Follow step tree to four from team [terra's](https://github.com/airavata-courses/terra/wiki/Installing-Rancher---Step--1) 

### Kubernetes - Rancher

1. Follow this [link](https://github.com/airavata-courses/terra/wiki/Step-2:-Setting-up-Kubernetes-cluster-using-Rancher) to setup K8S with Rancher. 

Deployment of cert-manager, keycloak, consul, vault, mysql and custos was adapted from the procedure from the following link (https://github.com/airavata-courses/DSDummies/wiki/Project-4)

## Python-Client

- The python client application which is a wrapper around the custos sdk is built and can be referred at (https://github.com/airavata-courses/vulcan/tree/feature-custos/custos)
- Few of the custos api relating to user and group management was tested using j-meter and the inferences are attached below.

## Load Testing Custos Endpoints

- Initially, we did our testing on a custos dev environment deployed on custos.scigap.org. 
- Later, the same tests was done on our own custos deployment to compare the test results. 

Some of the load testing scenarios and the results obtained are shown below

•	Register Users Endpoint

We load tested the register user endpoint of custos using two different loads

o	100 concurrent requests (threads)
o	500 concurrent requests (threads)


<img width="462" alt="image" src="https://user-images.githubusercontent.com/96559018/167051517-bd616c6c-e468-4cd8-96df-3f02e66a5e72.png">


In this case, we saw that the average response time was around 19ms for each register requests. And, Custos had a throughput of 164.998 requests/min

After this we incremented the load to 500 requests. Results are below

<img width="454" alt="image" src="https://user-images.githubusercontent.com/96559018/167051640-d8d4fd55-7fd3-45af-b120-e93db8b570f8.png">

In this case, we saw that the average response time was around 91ms for each register requests. And, Custos had a throughput of 162.618 requests/min which is same as when the load was 100 requests.

•	Create_group Endpoint

We load tested the create group endpoint of custos using two different loads –

o	100 concurrent requests (threads)
o	500 concurrent requests (threads)

<img width="478" alt="image" src="https://user-images.githubusercontent.com/96559018/167051727-23067db0-e574-4b2f-bb33-b2f35a53e7a4.png">

In this case, we saw that the average response time was around 14ms for each create group requests. And, Custos had a throughput of 221.435 requests/min.

<img width="438" alt="image" src="https://user-images.githubusercontent.com/96559018/167051751-b948f70d-26d7-4bca-ad7b-c41c7ae2cbe1.png">

In this case, we saw that the average response time was around 93ms for each create group requests. And, Custos had a throughput of 147.226 requests/min. So, as the load was increased the response time increased, and the throughput also reduced. But, it was easily able to handle 500 requests.

## Testing on our own custos deployment

**custos_host**: js-156-79.jetstream-cloud.org
**custos_port**: 30367

For analyzing and comparing the results from both the deployments we did the 500 requests test for both the register user and create group endpoints to see the difference. 

500 requests test for register endpoint

<img width="481" alt="image" src="https://user-images.githubusercontent.com/96559018/167051806-2239551c-f1f2-404d-b5fa-c69e277c0668.png">

In our custos deployment, we got an average response time of 100ms which is similar to the dev custos deployment, The throughput was a little more at 157.252 requests/min.

## Challenges:

•	We tried to deploy custos was initially on Jetstream 2 for a couple of times and it failed. Later we had to switch to Jetstream 1 to setup the instance

## Reference:

1. The current rancher setup for Jetstream 1 was referred from team [terra's](https://github.com/airavata-courses/terra/wiki/) implementation on Jetstream-2.
2. The keycloak, cert-manager, vault, consul, vault was adapted from team [DSDummies](https://github.com/airavata-courses/DSDummies/wiki/) wiki page and thanks to team [Garuda](https://github.com/airavata-courses/Garuda) for helping us with fixing the issues with the setup.
