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

function log(str) {
    console.log(`${AFFILIATION}: ${str}`)
}
ws.on('open', function open() {
    const config = lib.loadJson(path.join(__dirname, 'fixtures', `${AFFILIATION}.json`))
    ws.on('message', (m) => {
        log(`${m.toString()}`)
    })
    config.reduce((p, c) => {
        if(c.skip) {
            console.log(`SKIPPING ${JSON.stringify(c)}`)
            return p
        }

        if(c.invoke) {
            return p.then(() => {
                const invoke = lib.loadQueryText(c.invoke, {
                    username: orgConfig.users[0].username,
                    affiliation: orgConfig.affiliation,
                    password: orgConfig.users[0].password
                })
                log(`invoking ${c.invoke}: ${invoke}`)
                ws.send(invoke)
            })
        }

        if(c.wait) {
            return p.then(() => {
                return new Promise((resolve, reject) => {
                    const callback = (m) => {
                        const mObj = JSON.parse(m)
                        if(mObj.message === c.wait) {
                            log(`${mObj.message} received, next step`)
                            ws.removeListener('message', callback)
                            resolve(m)
                        } else {
                            log(`'${mObj.message}' ignored, still waiting for ${c.wait}`)                            
                        }
                    }
                    ws.on('message', callback)
                    ws.once('error', (e) => {
                        reject(e)
                    })
                })
            })  
        }

        if(c.waitTime) {
            return p.then(() => {
                log(`Waiting ${c.waitTime} ms`)
                return new Promise((resolve, reject) => {
                    setTimeout(() => {
                        resolve()
                    }, c.waitTime)
                })
            })  
        }

        if(c.poll) {
            return p.then(() => {
                return new Promise((resolve, reject) => {
                    var t
                    const callback = (m) => {
                        const mObject = JSON.parse(m)
                        const currentResults = JSON.stringify(mObject.results)
                        const expectedResults = JSON.stringify(c.expect)
                        if(mObject.message === c.poll) {
                            if( currentResults === expectedResults) {
                                log(`${currentResults} MATCHES ${expectedResults}`)
                                if(t) {
                                    clearInterval(t)
                                }
                                ws.removeListener('message', callback)
                                resolve(m)
                            } else {
                                log(`${currentResults} does not match ${expectedResults}`)
                                if(!t) {
                                    t = setInterval(() => ws.send(JSON.stringify({message: 'logins'})), 5000) 
                                }
                            }
                        }
                    }

                    ws.on('message', callback)

                    ws.once('error', (e) => {
                        reject(e)
                    })

                    ws.send(JSON.stringify({message: 'logins'}))
                })
            })  
        }

        if(c.debug) {
            return p.then(() => {
                log(c.debug)
                return Promise.resolve()
            })
        }

        return Promise.reject(`Unsupported script: ${JSON.stringify(c)}`)

    }, Promise.resolve())
    .catch((err) => {
        console.error(err)
    })
});