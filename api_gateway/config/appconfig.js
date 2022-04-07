//set the Kafka bootstrap server
var KAFKA_HOST = process.env.KAFKA_HOST || 'localhost'
var KAFKA_PORT = process.env.KAFKA_PORT || '9092'
const kafkaBootstrapServer = ''+KAFKA_HOST+':'+KAFKA_PORT+''

//kafka topics
const topic_user_register_request = 'user-register-request'
const topic_user_register_response = 'user-register-response'
const topic_user_login_request = 'user-login-request'
const topic_user_login_response = 'user-login-response'
const topic_user_history_get_request = 'user-history-get-request'
const topic_user_history_get_response = 'user-history-get-response'
const topic_user_history_save_request = 'user-history-save-request'
const topic_user_history_save_response = 'user-history-save-response'

module.exports = {
    kafkaBootstrapServer, 
    topic_user_register_request, 
    topic_user_register_response, 
    topic_user_login_request, 
    topic_user_login_response,
    topic_user_history_get_request,
    topic_user_history_get_response,
    topic_user_history_save_request,
    topic_user_history_save_response
};