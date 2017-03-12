'use strict'

const hfc = require('hfc');
const pify = require('pify-proto');
const path = require('path')

var CHAINCODE_ID = 'hc'

const PEER_SERVER = 'd00bd00b.local'

const caAddr   = process.env.SDK_MEMBERSRVC_ADDRESS
	? process.env.SDK_MEMBERSRVC_ADDRESS
	: `${PEER_SERVER}:7054` ;

const peerAddrs = process.env.SDK_PEER_ADDRESS
	? process.env.SDK_PEER_ADDRESS
	: [`${PEER_SERVER}:7051`] ;

const eventHubAddr = process.env.SDK_EVENTHUB_ADDRESS
	? process.env.SDK_EVENTHUB_ADDRESS
	: `${PEER_SERVER}:7053` ;

process.on('exit', function (){
  chain.eventHubDisconnect();
});
const grpcOpts = {}

var chain = pify(hfc.newChain(CHAINCODE_ID));
chain.setKeyValStore(hfc.newFileKeyValStore(path.join(__dirname, "keyValStore")));

console.log("Setting membersrvc address to grpc://" + caAddr);
chain.setMemberServicesUrl("grpc://" + caAddr, grpcOpts);

console.log("Setting peer address to grpcs://" + peerAddrs);
peerAddrs.map((a) => chain.addPeer("grpc://" + a, grpcOpts));

// chain.eventHubConnect("grpc://" + eventHubAddr);
// process.on('exit', function (){
//   chain.eventHubDisconnect();
// });


// TODO: get this to work
// function events() {
// 	const eh = chain.getEventHub()
// 	// not handling errors well: https://jira.hyperledger.org/browse/FAB-1507
// 	// register for chaincode event with wildcard event name
// 	var regid = eh.registerChaincodeEvent(CHAINCODE_ID, ".*", function(event) {
// 		console.log(event.payload.toString())
// 	});
// }

function getUser(username, enroll) {
	 return chain.getUser(username)
		.then((user) => {
			var registrar = user
			//user.setTCertBatchSize(1)
			return pify(user).enroll(enroll).then(e => registrar)
		})
}

function invoke(user, fcn, args) {
	const argsString =  JSON.stringify(args)
	console.log(`invoking fnc ${fcn} with args ${argsString}`)

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
  				//chain.eventHubDisconnect();

			})
			.on('error', (e) => {
				console.log('rejected!')
				reject(e)
			})
	})
}

function query(user, rowId, args) {
	if(!user) {
		return Promise.reject('query: User (registrar) parameter is required')
	}

	var queryRequest = {
		chaincodeID: CHAINCODE_ID,
		fcn: `query${rowId}`,
		args: [args]
	};

	return new Promise((resolve, reject) => {
		var txid;
		user
			.query(queryRequest)
			.on('submitted', (results) => {
				console.log(`submitted query`)
			})
			.on('complete', (results) => {
				console.log('query completed!')
				resolve(results)

			})
			.on('error', (e) => {
				console.log('query rejected!')
				reject(e)
			})
	})
}

module.exports = {
	invoke,
	getUser,
	query
}