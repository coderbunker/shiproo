'use strict'

const invoke = require('./invoke.js')
const config = require('./config.json')
const lib = require('./lib.js')

const orgConfig = config.orgs.find((f) => f.name === 'Lenovo')

var registrar;

invoke.getUser(orgConfig.registrar, orgConfig.enroll)
    .then((user) => {
        registrar = user
        return invoke.invoke(registrar, 'login', orgConfig.users[0])
    })
    .then((result) => console.log(result))
    .then(() => {
        return invoke.invoke(
            registrar,
            'createParcel',  
            lib.loadQuery('createParcel', orgConfig.users[0].username))
    })
    .then((result) => console.log(result))
    .catch((err) => {
        console.error(err)
    });