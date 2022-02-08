# Database Management Service

- This service validates the given user inputs from the API gateway using NexradAWS library
- Passes the value to the Database Management service via GRPC 
- returns the id from Database Management service to storm clustering service via kafka producer.

## Tech Stack used:
- Python 3
- grpc
- kafka
- flask

## Setup:

1. Run the file run.sh with the following command
                
                sh run.sh
                
2. This installs the necessary dependencies and starts the application     
