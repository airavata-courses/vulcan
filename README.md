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

