const kafka = require("kafka-node");
const config  = require("./appconfig")

var Consumer = kafka.Consumer,
client = new kafka.KafkaClient({kafkaHost:config.kafkaBootstrapServer}),
register_response = new Consumer(client, [ { topic: config.topic_user_register_response, partition: 0 } ], { autoCommit: false });
login_response = new Consumer(client, [ { topic: config.topic_user_login_response, partition: 0 } ], { autoCommit: false });

var Producer = kafka.Producer,

producer = new Producer(client)
module.exports = { producer, register_response, login_response };