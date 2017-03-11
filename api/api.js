'use strict'

const WebSocket = require('ws');
const lib = require('./lib.js')
const path = require('path')
const hfc = require('hfc')

function errorMessage(errorString) {
    JSON.stringify({
        message: 'error',
        description: errorString
    })
}

const connectionState = {
}

const wss = new WebSocket.Server({ port: 6666 });

wss.on('connection', function connection(ws) {
    const state = {
        username: null
    }
    ws.on('message', (msg) => {
        console.log(`username: ${state.username} received: ${msg}`);
        const msgObject = JSON.parse(msg)
        if(!msgObject.message) {
            ws.send(errorMessage('invalid message: no message field specified'))
        }
        if(!state.username && msgObject.message != 'login') {
            ws.send(errorMessage('login first using login message!'))
        }
        switch(msgObject.message) {
            case 'login':
                if(!msgObject.username) {
                    ws.send(errorMessage('login needs the username specified!'))
                    return
                }
                state.username = msgObject.username
                break;
            default:
                ws.send(errorMessage(`unsupported message: ${msgObject.message}`))
                return

        }
        const reply = lib.loadJsonText(path.join(__dirname, 'fixtures', `${msgObject.message}Reply.json`))
        console.log(`reply: ${reply}`)
        ws.send(reply);
    });
});