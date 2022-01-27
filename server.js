const express = require('express')
const {ROUTES} = require("./routes");
const {setupProxies} = require("./proxy");
const app = express()
const port = 3000;

setupProxies(app, ROUTES);
// app.get('/hello', (req, resp) => {
//     return resp.send('HELLO WORLD!');
// })

app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})