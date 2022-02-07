const kafka = require("kafka-node");
// const Producer = kafka.Producer;
// const Consumer = kafka.Consumer;

var Consumer = kafka.Consumer,
 client = new kafka.KafkaClient("localhost:9092"),
 consumer = new Consumer(
 client, [ { topic: "test1", partition: 0 } ], { autoCommit: false });


var Producer = kafka.Producer,
// KeyedMessage = kafka.KeyedMessage,
client = new kafka.KafkaClient("localhost:9092"),
producer = new Producer(client)

// const ConsumerGroup = kafka.ConsumerGroup
// const client = new kafka.KafkaClient({ kafkaHost: "kafka-service:9092" });
// let config = require("./config");
// producer = new Producer(client);


module.exports = { producer, consumer };

// consumer = new Consumer(
//     client,
//     [{ topic: config.kafkaTopics.consumers.apiConsumer }],
//     {
//       autoCommit: true,
//       encoding: 'utf8',
//       groupId: 'api-gateway'
//     }
//   )

// const consumerOptions = {
//   kafkaHost: 'kafka-service:9092',
//   autoCommit: true,
//   encoding: "utf8",
//   groupId: "api-gateway",
//   protocol: ["roundrobin"],
//   fromOffset: "latest"
// };
// const consumer = new ConsumerGroup(
//   consumerOptions,
//   "sessionManagementConsumerF"
// );
