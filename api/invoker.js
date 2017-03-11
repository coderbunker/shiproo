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
  
    })
    .then((result) => console.log(result))
    .catch((err) => {
        console.error(err)
    });