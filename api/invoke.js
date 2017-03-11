'use strict'

const hfc = require('hfc');
const pify = require('pify-proto');

var CHAINCODE_ID = 'hc'

const PEER_SERVER = 'd00bd00b.local'

var caAddr   = process.env.SDK_MEMBERSRVC_ADDRESS
	? process.env.SDK_MEMBERSRVC_ADDRESS
	: `${PEER_SERVER}:7054` ;

var peerAddrs = process.env.SDK_PEER_ADDRESS
	? process.env.SDK_PEER_ADDRESS
	: [`${PEER_SERVER}:7051`] ;

const grpcOpts = {}

var chain = pify(hfc.newChain(CHAINCODE_ID));
chain.setKeyValStore(hfc.newFileKeyValStore( "/tmp/keyValStore"));

console.log("Setting membersrvc address to grpc://" + caAddr);
chain.setMemberServicesUrl("grpc://" + caAddr, grpcOpts);

console.log("Setting peer address to grpcs://" + peerAddrs);
peerAddrs.map((a) => chain.addPeer("grpc://" + a, grpcOpts));

function getUser(username, enroll) {
	 return chain.getUser(username)
		.then((user) => {
			user.setTCertBatchSize(1)
			return pify(user).enroll(enroll).then(e => user)
		})
}

function invoke(user, fcn, args) {
	const argsString =  JSON.stringify(args)
	console.log(`invoking fnc ${fcn} with args ${argsString}`)

	// Construct the query request
	var invokeRequest = {
		chaincodeID: CHAINCODE_ID,
		fcn: fcn,
		args: [argsString]
	};

	return new Promise((resolve, reject) => {
		var txid;
		user
			.invoke(invokeRequest)
			.on('submitted', (results) => {
				txid = results.uuid
				console.log(`submitted ${txid}`)
			})
			.on('complete', (results) => {
				console.log('completed!')
				resolve(txid)
			})
			.on('error', (e) => {
				console.log('rejected!')
				reject(e)
			})
	})

}

module.exports = {
	invoke,
	getUser
}