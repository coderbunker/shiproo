# API server

## install

```
npm install
```

## run

```
nodemon api.js
```

## client connection

connect to port 6666

## Done so far

Created a websocket API in api.js. that can process the following messages:

* login
* createParcel
* logins
* findRoute
* buyRoute
* pickup

That code is exercised by apiClientTest.js that does the following:

* spawn 4 subprocesses, one for each organization
 * creates a connection
 * use the organization declarative script in fixtures/$ORGNAME.json to invoke, query and wait on notifications 
 * example: before we start the flow, we need a checkpoint for all the organizations to be logged in. This is done by pooling at regular interval logins

Since we want to exercise 