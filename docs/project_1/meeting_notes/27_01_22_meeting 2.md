### Notes from the Lecture: 25-01-2022

- Not ideal to have huge data file to be moved across the microservices
- Also storing them in the database for later access by the services is not ideal either.
- Metadata and Data has to be treated separately. Ideal to store Meta data and have the referances shared via messaging queues among the services
- In order to avoid incoherency an independent microservice has to be implemented to perform database related transactions.

Based the discussion and lecture,

- Implement a Database Management microservice that would be reached by other services to access the database.
- POC on gRPC framework (to avoid two producer and two consumer kafka calls just to have the referance returned) since the referance has to be returned the service. 
