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

chain.getUser('test_user0')
	.then((user) => {
		return pify(user).enroll('MS9qrN8hFjlE').then(e => user)
	})
	.then((user) => {
		// Construct the query request
		var invokeRequest = {
			chaincodeID: CHAINCODE_ID,
			fcn: "login",
			args: ["shipper1", "s1"]
		};

		return new Promise((resolve, reject) => {
			user
				.invoke(invokeRequest)
				.on('submitted', (e) => {
					console.log('submitted!')
				})
				.on('complete', (e) => {
					console.log('completed!')
					resolve(JSON.stringify(e))
				})
				.on('error', (e) => {
					console.log('rejected!')
					reject(e)
				})
		})
	})
	.then((result) => {
		console.log(result)
	})
	.catch((err) => {
		console.error(err)
	});