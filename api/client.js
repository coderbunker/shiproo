'use strict'

const WebSocket = require('ws');
const ws = new WebSocket('ws://localhost:6666')
const path = require('path')
const fs = require('fs')
const lib = require('./lib.js')


var USERNAME = process.argv[2]

if(!USERNAME) {
    console.log('specify username: lenovo, google, sfexpress, fedex')
    process.exit(1)
}
console.log(USERNAME)

function loadQuery(name) {
    const query = lib.loadJsonText(path.join(__dirname, 'fixtures', `${name}.json`))
    return JSON.stringify(JSON.parse(query.replace('$USERNAME', USERNAME)))
}


ws.on('open', function open() {
    const config = lib.loadJson(path.join(__dirname, 'fixtures', `${USERNAME}.json`)) 
    ws.on('message', (m) => {
        console.log(m)
    })
    config.forEach((c)=> {
        const query = loadQuery(c.query)
        console.log(query)
        ws.send(query)
    })
});

