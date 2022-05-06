# Project 4 - Case Study - Apache Airavata Custos

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





 
