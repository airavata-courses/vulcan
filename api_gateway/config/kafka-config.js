const kafka = require("kafka-node");

var Consumer = kafka.Consumer,
 client = new kafka.KafkaClient("localhost:9092"),
 consumer_gateway = new Consumer(
 client, [ { topic: "gateway", partition: 0 }],
             { autoCommit: false });

var Consumer = kafka.Consumer,
client = new kafka.KafkaClient("localhost:9092"),
consumer_save = new Consumer(
client, [ { topic: "gateway-save", partition: 0 }],
            { autoCommit: false });

var Consumer = kafka.Consumer,
client = new kafka.KafkaClient("localhost:9092"),
consumer_get = new Consumer(
client, [ { topic: "gateway-get", partition: 0 }],
            { autoCommit: false });


var Producer = kafka.Producer,
client = new kafka.KafkaClient("localhost:9092"),
producer = new Producer(client)


module.exports = { producer, consumer_gateway, consumer_get, consumer_save };

