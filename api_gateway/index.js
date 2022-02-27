const express = require('express')
const bodyParser = require('body-parser')
const jwt = require('jsonwebtoken');
const app = express()
const server = require('http').createServer(app);
const cors = require('cors');
const { consumer_get, consumer_gateway, consumer_save } = require('./config/kafka-config');
app.use(cors());
app.use(bodyParser.json())

const consumer = require('./config/kafka-config').consumer
const producer = require("./config/kafka-config").producer

// Register
app.post('/register', async function (req, res) {
  const { firstName, lastName, emailID, password } = req.body;
  const URL = 'http://localhost:8081/api/user/register'
  const data = {firstName : firstName,
  lastName : lastName,
  email : emailID,
  password : password};

  const otherParam = {
    headers : {"content-type":"aplication/json; charset = UTF-8"},
    body : data,
    method : "POST"
  };

  fetch(URL,otherParam)
  .then(res => {return res.json()})
  .then(data => console.log(data))
  .catch(error => console.log(error))

  res.status(200).json('Success')
});

// Login
app.post('/login', async (req, res) => {
  const { email, password } = req.body;
  const URL = 'http://localhost:8081/api/user/login'
  const data = {email : email, password : password};
  const otherParam = {
    headers : {"content-type":"aplication/json; charset = UTF-8"},
    body : data,
    method : "POST"
  };

  fetch(URL,otherParam)
  .then(res => {return res.json()})
  .then(data => console.log('Token'+ data))
  .catch(error => console.log(error))
  res.status(200).json(data)
});

// User-history
app.post('/user-history',async(req,res) => {
  const token = req.body.token
  let payloads = [
    {
      topic: "user-history-get",
      messages: token
    }];

  producer.send(payloads, function (err, data) {
    console.error(err);
  });

  producer.on('error', function (err) {
    console.error(err)
  })
  consumer_get.on("message", function (message) {
    console.log('received: %s', message.value);
  });
  res.send(200).json(message)
})


app.post('/ingestor',async(req,res) => {
  msg = new TextDecoder('utf-8').decode(new Uint8Array(req.body))

  // 1st Payload for user-history saving
  let payload1 = [
    {
      topic: "user-history-save",
      messages: msg
    }];
  // 2nd Payload for ingestor service
  let payload2 = [
    {
      topic: "ingestor",
      messages: msg
    }];
    producer.send(payload1, function (err, data) {
      console.error(err);
    });
    producer.on('error', function (err) {
      console.error(err)
    
    // consumer_save for consuming string response from gateway-save
    consumer_save.on("message", function (message1) {
      console.log('received: %s', message1.value);
    });
    
    producer.send(payload2, function (err, data) {
      console.error(err);
    })});
    producer.on('error', function (err) {
      console.error(err)
    })

    // consumer_gateway for consuming string response for visualization
    consumer_gateway.on("message", function (message2) {
      console.log('received: %s', message2.value);
    });

    // combining final message from both consumers
    message = {msg1 : message1, msg2 : message2}
    res.send(200).json(message)
});


server.listen(3000, () => console.log('Listenining on port : 3000'))








// wss.on('connection', function connection(ws) {
//   console.log('A new client Connection!')
//   consumer.on("message", function (message) {
//     console.log('received: %s', message.value);
//     ws.send(message.value)
//   })

//   ws.on('message', function incoming(message) {
//     msg = new TextDecoder('utf-8').decode(new Uint8Array(message))
//     console.log(msg)
//     let payloads = [
//       {
//         topic: "ingestor",
//         messages: msg
//       }];

//     producer.send(payloads, function (err, data) {
//       console.error(err);
//     });

//     producer.on('error', function (err) {
//       console.error(err)
//     })
//   });
// });