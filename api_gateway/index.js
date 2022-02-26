const express = require('express')
const mongoose = require('mongoose')
const passport = require('passport')
const bodyParser = require('body-parser')
const User = require('./Models/User')
const jwt = require('jsonwebtoken');
require('./passport')
const app = express()
const server = require('http').createServer(app);
const cors = require('cors')
const WebSocket = require('ws');
const wss = new WebSocket.Server({ server });
app.use(cors());

genToken = user => {
  return jwt.sign({
    iss: 'Joan_Louji',
    sub: user.id,
    iat: new Date().getTime(),
    exp: new Date().setDate(new Date().getDate() + 1)
  }, 'joanlouji');
}
app.use(bodyParser.json())

const consumer = require('./config/kafka-config').consumer
const producer = require("./config/kafka-config").producer

// This can go inside the post request when user makes the data request or when login is authenticated.
wss.on('connection', function connection(ws) {
  console.log('A new client Connection!')
  // ws.send('Welcome new Client');
  consumer.on("message", function (message) {
    console.log('received: %s', message.value);
    ws.send(message.value)
  })

  ws.on('message', function incoming(message) {
    msg = new TextDecoder('utf-8').decode(new Uint8Array(message))
    console.log(msg)
    let payloads = [
      {
        topic: "ingestor",
        messages: msg
      }];

    producer.send(payloads, function (err, data) {
      console.error(err);
    });

    producer.on('error', function (err) {
      console.error(err)
    })
  });
});




app.post('/register', async function (req, res, next) {
  const { email, password } = req.body;

  //Check If User Exists
  let foundUser = await User.findOne({ email });
  if (foundUser) {
    return res.status(403).json({ error: 'Email is already in use' });
  }

  const newUser = new User({ email, password })
  await newUser.save()
  // Generate JWT token
  const token = genToken(newUser)
  res.status(200).json({ token })
});


mongoose.connect("mongodb://localhost/27017", { useNewUrlParser: true, useUnifiedTopology: true });
mongoose.connection.once('open', function () {
  console.log('Connected to Mongo');
}).on('error', function (err) {
  console.log('Mongo Error', err);
})


app.post('/login', async (req, res, next) => {
  const { email, password } = req.body;
  let foundUser = await User.findOne({ email, password });
  if (foundUser) {
    return res.status(200).json("Login Succesful");
  }
  return res.status(403).json("Invalid User Name")
})

app.get('/secret', passport.authenticate('jwt', { session: false }), (req, res, next) => {
  res.json("Secret Data")
})


server.listen(3000, () => console.log('Listenining on port : 3000'))
