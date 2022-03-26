//set the Kafka bootstrap server
var KAFKA_HOST = process.env.KAFKA_HOST || 'localhost'
var KAFKA_PORT = process.env.KAFKA_PORT || '9092'
const kafkaBootstrapServer = ''+KAFKA_HOST+':'+KAFKA_PORT+''

//kafka topics
const topic_user_register_request = 'user-register-request'
const topic_user_register_response = 'user-register-response'
const topic_user_login_request = 'user-login-request'
const topic_user_login_response = 'user-login-response'

module.exports = {
    kafkaBootstrapServer, 
    topic_user_register_request, 
    topic_user_register_response, 
    topic_user_login_request, 
    topic_user_login_response,
};