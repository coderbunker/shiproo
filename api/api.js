'use strict'

const WebSocket = require('ws');
const lib = require('./lib.js')
const path = require('path')
const invoke = require('./invoke.js')
const config = require('./config.json')

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
        username: null,
        registrar: null,
        affiliation: null,
        parcels: {
        },
        orgConfig: null
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
                if(!msgObject.username || !msgObject.affiliation || !msgObject.password) {
                    ws.send(errorMessage('login needs the following properties: username, affiliation, password'))
                    return
                }
                state.orgConfig = config.orgs.find((f) => f.affiliation === msg.affiliation)
                if(!state.orgConfig) {
                    ws.send(errorMessage(`config not found for affiliate ${msg.affiliation}`))
                    return    
                }
                state.username = msgObject.username
                
                var registrarPromise;
                if(!state.registrar) {
                    registrarPromise = invoke.getUser(
                        state.orgConfig.registrar, 
                        state.orgConfig.enroll)
                } else {
                    registrarPromise = Promise.resolve(state.registrar)
                }
                registrarPromise
                    .then((registrar) => {
                        return invoke.invoke(registrar, 'login', msgObject)
                    })
                    .then((txId) => {
                        ws.send({
                            "message": "loginReply",
                            "token": msgObject.username,
                            "txId": txId
                        });
                    })
                break;
                
            case 'createParcel':
                if(!msgObject.username) {
                    ws.send(errorMessage('need to be logged in first'))
                    return
                }
                const query = lib.loadQuery('createParcel', state.username)
                state.parcels[query.parcelId] = 'PENDING'
                invoke.invoke(
                        state.registrar,
                        'createParcel',
                        query)
                    .then((txId) => {
                        ws.send({
                            message: "createParcelReply",
                            reply: "PENDING",
                            parcelId: query.parcelId,
                            txId: txId
                        });
                    })
                break;

            default:
                const reply = lib.loadJsonText(path.join(__dirname, 'fixtures', `${msgObject.message}Reply.json`))
                console.log(`reply: ${reply}`)
                ws.send(errorMessage(`unsupported message: ${msgObject.message}`))
                return
        }
    });
});