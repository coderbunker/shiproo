'use strict'

const WebSocket = require('ws');
const path = require('path')

//const invoke = require('./invoke.js')
const invoke = require('./invokeTest.js')
const config = require('./config.json')
const lib = require('./lib.js')

function errorMessage(errorString) {
    return JSON.stringify({
        message: 'error',
        description: errorString
    })
}

const connectionState = {
}

const wss = new WebSocket.Server({ port: 6666 });

wss.broadcast = function broadcast(data) {
  wss.clients.forEach(function each(client) {
    if (client.readyState === WebSocket.OPEN) {
      client.send(data);
    }
  });
};

wss.broadcastJson = (json) => {
    wss.broadcast(JSON.stringify(json))
}
wss.on('connection', function connection(ws) {
    const state = {
        username: null,
        registrar: null,
        affiliation: null,
        parcels: {
        },
        orgConfig: null
    }

    ws.sendDebug = (str) => {
        console.log(`REPLYING(${state.username}): ${str}`)
        ws.send(str)
    }

    ws.sendError = (err) => {
        if(err.stack)
            console.error(`ERROR(${state.username}): ${err.stack}`)
        else
            console.error(`ERROR(${state.username}): ${err}`)
        ws.send(errorMessage(err))
    }

    ws.sendJson = (json) => {
        console.error(`REPLYING(${state.username}): ${JSON.stringify(json)}`)
        ws.send(JSON.stringify(json))
    }

    ws.on('message', (msg) => {
        console.log(`RECEIVING(${state.username}): ${msg}`);
        const msgObject = JSON.parse(msg)
        if(!msgObject.message) {
            return ws.sendDebug(errorMessage('invalid message: no message field specified'))
        }
        if(!state.username && msgObject.message != 'login') {
           return ws.sendDebug(errorMessage('login first using login message!'))
        }
        switch(msgObject.message) {
            case 'login':
                if(!msgObject.username || !msgObject.affiliation || !msgObject.password) {
                    return ws.sendError('login needs the following properties: username, affiliation, password')
                }
                state.orgConfig = config.orgs.find((f) => f.affiliation === msgObject.affiliation)
                if(!state.orgConfig) {
                    return ws.sendError(`config not found for affiliation ${msgObject.affiliation}`)
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
                        ws.sendJson({
                            "message": "loginReply",
                            "token": msgObject.username,
                            "txId": txId
                        });
                    })
                    .catch((err) => {
                        return ws.sendError(err)
                    })
                break;
                
            case 'createParcel':
                if(!state.username) {
                    return ws.sendError('need to be logged in first')
                }
                const query = lib.loadQuery('createParcel', {
                    username: state.username
                })
                state.parcels[query.parcelId] = 'PENDING'
                invoke.invoke(
                        state.registrar,
                        'createParcel',
                        query)
                    .then((txId) => {
                        ws.sendJson({
                            message: "createParcelReply",
                            reply: "PENDING",
                            parcelId: query.parcelId,
                            txId: txId
                        });
                        wss.broadcastJson({
                            "message": "createParcelNotification",
                            "parcelId": query.parcelId
                        })
                    })
                    .catch((err) => {
                        return ws.sendError(err)
                    })
                break;

            default:
                return ws.sendError(`unsupported message: ${msgObject.message}`)
                // const reply = lib.loadJsonText(path.join(__dirname, 'fixtures', `${msgObject.message}Reply.json`))
                // console.log(`reply: ${reply}`)
                // return
        }
    });
});