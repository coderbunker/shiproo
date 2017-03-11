'use strict'

const invoke = require('./invoke.js')
const config = require('./config.json')

const orgConfig = config.orgs.find((f) => f.name === 'Lenovo')

invoke.invoke(orgConfig.registrar, orgConfig.enroll, 'login', [
        JSON.stringify(orgConfig.users[0])
    ])
    .then((result) => {
        console.log(result)
    })
    .catch((err) => {
        console.error(err)
    });