const express = require('express')
const mongoose = require('mongoose')
const passport = require('passport')
const bodyParser = require('body-parser')
const User = require('./Models/User')
const jwt = require('jsonwebtoken');
require('./passport')
const app = express()
const server = require('http').createServer(app);
const WebSocket = require('ws');
const wss = new WebSocket.Server({server});

genToken = user => {
  return jwt.sign({
    iss: 'Joan_Louji',
    sub: user.id,
    iat: new Date().getTime(),
    exp: new Date().setDate(new Date().getDate() + 1)
  }, 'joanlouji');
}
app.use(bodyParser.json())
app.get('/',(req,res)=>{
  res.send('Hello world')
})

// app.get('/secret',passport.authenticate('jwt',{session:false})),
// (req,res,next) => {
//     res.json('Secret Data')
// }

const consumer=require('./config/kafka-config').consumer
const producer = require("./config/kafka-config").producer;
const { json } = require('body-parser')
const { error } = require('console')

// This can go inside the post request when user makes the data request or when login is authenticated.
wss.on('connection', function connection(ws){
  console.log('A new client Connection!')
  ws.send('Welcome new Client');
  consumer.on("message", function (message) {
    console.log(message);
    // connection.sendUTF(message.value);
  })

  ws.on('message', function incoming(message){
    msg = JSON.stringify(message)

    let payloads = [
      {
        topic : "ingestor",
        messages : msg
      }];
    
    producer.on('ready', function () {
      producer.send(payloads, function (err, data) {
          console.log(data);
      });
    });

    producer.on('error', function (err) {})

    console.log('received: %s', message);

    ws.send('Got your message its :' + message)
  });
});




app.post('/register', async function (req, res, next) {
  const { email, password } = req.body;
  
  //Check If User Exists
  let foundUser = await User.findOne({ email });
  if (foundUser) {
    return res.status(403).json({ error: 'Email is already in use'});
  }
 
  const newUser = new User({ email, password})
  await newUser.save()
  // Generate JWT token
  const token = genToken(newUser)
  res.status(200).json({token})
});


mongoose.connect("mongodb://localhost/27017", {useNewUrlParser: true, useUnifiedTopology: true});
mongoose.connection.once('open',function(){
  console.log('Connected to Mongo');
}).on('error',function(err){
  console.log('Mongo Error', err);
})


app.post('/login', async (req, res, next) => {
  const { email, password } = req.body;
  let foundUser = await User.findOne({ email });
  // let foundpass = await User.findOne({password});
  if (foundUser)  {
    return res.status(200).json("Login Succesful");
  }
  return res.status(403).json("Invalid User Name")
})

app.get('/secret', passport.authenticate('jwt',{session: false}),(req,res,next)=>{
    res.json("Secret Data")
})


server.listen(3000, () => console.log('Listenining on port : 3000'))

// let config = require("./config/config");
// app.listen(8000,()=>{
//   console.log('Server is up and running at the port 8000')
// })