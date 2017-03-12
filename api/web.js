const express = require('express')
const app = express()
const expressWs = require('express-ws')(app);
const api = require('./api.js')
api.setupWss(expressWs.getWss())

const path = require('path')

app.use('/', express.static(path.join(__dirname, '..', 'ui')))
app.use('/login', express.static(path.join(__dirname, 'login')))

app.ws('/', function(ws, req) {
  api.connection(ws)
});

app.listen(3000, function () {
  console.log('Example app listening on port 3000!')
})