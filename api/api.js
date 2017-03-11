const WebSocket = require('ws');
const lib = require('./lib.js')
const wss = new WebSocket.Server({ port: 6666 });
const path = require('path')

function errorMessage(errorString) {
    JSON.stringify({
        message: 'error',
        description: errorString
    })
}

wss.on('connection', function connection(ws) {
  ws.on('message', (msg) => {
    console.log('received: %s', msg);
    const msgObject = JSON.parse(msg)
    if(!msgObject.message) {
        ws.send(errorMessage('invalid message: no message field specified'))
    }
    const reply = lib.loadJsonText(path.join(__dirname, 'fixtures', `${msgObject.message}Reply.json`))
    console.log(`reply: ${reply}`)
    ws.send(reply);
  });
});