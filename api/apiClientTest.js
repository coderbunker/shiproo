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
        console.log(`${AFFILIATION}: ${m.toString()}`)
    })
    config.reduce((p, c) => {
        if(c.invoke) {
            return p.then(() => {
                const invoke = lib.loadQueryText(c.invoke, {
                    username: orgConfig.users[0].username,
                    affiliation: orgConfig.affiliation,
                })
                ws.send(invoke)
            })
        }

        if(c.wait) {
            return p.then(() => {
                return new Promise((resolve, reject) => {
                    const callback = (m) => {
                        if(JSON.parse(m).message === c.wait) {
                            ws.removeListener('message', callback)
                            resolve(m)
                        }
                    }
                    ws.on('message', callback)
                    ws.once('error', (e) => {
                        reject(e)
                    })
                })
            })  
        }

        if(c.poll) {
            return p.then(() => {
                return new Promise((resolve, reject) => {
                    var t
                    ws.on('message', (m) => {
                        const mObject = JSON.parse(m)
                        const currentResults = JSON.stringify(mObject.results)
                        const expectedResults = JSON.stringify(c.expect)
                        if(mObject.message === c.poll) {
                            if( currentResults === expectedResults) {
                                console.log(`${currentResults} MATCHES ${expectedResults}`)
                                if(t) {
                                    clearInterval(t)
                                }
                                resolve(m)
                            } else {
                                console.log(`${currentResults} does not match ${expectedResults}`)
                                if(!t) {
                                    t = setInterval(() => ws.send(JSON.stringify({message: 'logins'})), 5000) 
                                }
                            }
                        }
                    })

                    ws.once('error', (e) => {
                        reject(e)
                    })

                    ws.send(JSON.stringify({message: 'logins'}))
                })
            })  
        }

        return Promise.reject(`Unsupported script: ${JSON.stringify(c)}`)

    }, Promise.resolve())
});