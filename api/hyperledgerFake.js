'use strict'

const uuid = require('uuid/v4');
const lib = require('./lib.js');

const users = {}
const registrars = {}
const transactions = {}
const CHAINCODE_ID = 'testchain'

function getUser(username, enroll) {
	if(!registrars[username]) {
		registrars[username] = 'enrolled'
	}
	return Promise.resolve(registrars[username])
}

function invoke(user, fcn, args) {
	const argsString =  JSON.stringify(args)
	console.log(`invoking fnc ${fcn} with args ${argsString}`)

	var invokeRequest = {
		chaincodeID: CHAINCODE_ID,
		fcn: fcn,
		args: [argsString]
	};
	const txId = uuid()
	transactions[txId] = invokeRequest
	switch(fcn) {
		case 'login':
			users[args.username] = 'loggedIn'
			break;
		default:
			break;
	}
	return Promise.resolve(txId)
}

function query(user, rowId) {
	switch(rowId) {
		case 'logins':
			return Promise.resolve(Object.keys(users).sort())
		case 'routes':
			return Promise.resolve(lib.loadQuery('queryRouteReply', {}))
		default:
			return Promise.reject(`unsupported ledger rowId: ${rowId}`)
	}
}

module.exports = {
	invoke,
	getUser,
	query
}