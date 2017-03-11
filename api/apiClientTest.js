'use strict'

const WebSocket = require('ws');
const path = require('path')
const fs = require('fs')

const lib = require('./lib.js')
const config = require('./config.json')
const spawn = require('child_process').spawn;

var AFFILIATION = process.argv[2]

if(!AFFILIATION) {
    config.orgs.map((org) => {
        console.log(`spawning for ${org.affiliation}`)
        spawn('node', [__filename, org.affiliation], { stdio: 'inherit' })
    })
} 

const orgConfig = config.orgs.find((f) => f.affiliation === AFFILIATION)
if(!orgConfig) {
    console.log(`config not found for ${AFFILIATION}`)
    process.exit(1)  
}

const ws = new WebSocket('ws://localhost:6666')

ws.on('open', function open() {
    const config = lib.loadJson(path.join(__dirname, 'fixtures', `${AFFILIATION}.json`))
    ws.on('message', (m) => {
        console.log(m.toString())
    })
    config.reduce((p, c) => {
        if(c.query) {
            return p.then(() => {
                const query = lib.loadQueryText(c.query, {
                    username: orgConfig.users[0].username,
                    affiliation: orgConfig.affiliation,
                })
                console.log(query)
                ws.send(query)
            })
        }

        if(c.wait) {
            return p.then(() => {
                return new Promise((resolve, reject) => {
                    ws.once('message', (m) => {
                        if(JSON.parse(m).message === c.wait) {
                            resolve(m)
                        }
                    })
                    ws.once('error', (e) => {
                        reject(e)
                    })
                })
            })  
        }
    }, Promise.resolve())
});