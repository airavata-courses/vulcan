const kafka = require("kafka-node");

var Consumer = kafka.Consumer,
 client = new kafka.KafkaClient("localhost:9092"),
 consumer = new Consumer(
 client, [ { topic: "gateway", partition: 0 }, 
            {topic : "gateway-save", partition:1},
            {topic : "gateway-get", partition : 2} ],
             { autoCommit: false });


var Producer = kafka.Producer,
client = new kafka.KafkaClient("localhost:9092"),
producer = new Producer(client)


module.exports = { producer, consumer };

