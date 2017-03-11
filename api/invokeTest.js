'use strict'

const uuid = require('uuid/v4');

const users = {}
const transactions = {}
const CHAINCODE_ID = 'testchain'

function getUser(username, enroll) {
	if(!users[username])
		users[username] = 'enrolled'
	return Promise.resolve(users[username])
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
	return Promise.resolve(txId)
}

module.exports = {
	invoke,
	getUser
}