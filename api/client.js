'use strict'

const WebSocket = require('ws');
const path = require('path')
const fs = require('fs')

const lib = require('./lib.js')
const config = require('./config.json')

var AFFILIATION = process.argv[2]

if(!AFFILIATION) {
    console.log('specify AFFILIATION: lenovo, google, sfexpress, fedex')
    process.exit(1)
}
console.log(AFFILIATION)

const orgConfig = config.orgs.find((f) => f.affiliation === AFFILIATION)
if(!orgConfig) {
    console.log(`config not found for ${AFFILIATION}`)
    process.exit(1)  
}

const ws = new WebSocket('ws://localhost:6666')

ws.on('open', function open() {
    const config = lib.loadJson(path.join(__dirname, 'fixtures', `${AFFILIATION}.json`))
    ws.on('message', (m) => {
        console.log(m)
    })
    config.forEach((c)=> {
        const query = lib.loadQueryText(c.query, orgConfig.users[0])
        console.log(query)
        ws.send(query)
    })
});

