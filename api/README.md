# API server

## install

```
nvm use
npm install
```

## run

```
node api.js
```

suggest nodemon for reloading changed files

by default, runs using hyperledgerFake.js. to run against the blockchain:

```
PRODUCTION=1 node api.js
```

## client connection

connect to port 6666 or use apiClientTest.js

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

## output example

```
sfexpress: invoking login: {"message":"login","username":"abhishek","affiliation":"sfexpress","password":"password"}
fedex: invoking login: {"message":"login","username":"dmitry","affiliation":"fedex","password":"password"}
lenovo: invoking login: {"message":"login","username":"shuyu","affiliation":"lenovo","password":"password"}
sfexpress: {"message":"loginReply","token":"abhishek","txId":"14760734-07da-4680-83b2-d8aad6208015"}
sfexpress: 'loginReply' ignored, still waiting for buyRouteNotification
fedex: {"message":"loginReply","token":"dmitry","txId":"98e9bae4-d780-4a81-aa1b-63a2b51da9c4"}
fedex: 'loginReply' ignored, still waiting for pickupNotification
lenovo: {"message":"loginReply","token":"shuyu","txId":"d29d6f26-d27e-44c8-b6c9-c41a19d26ea4"}
lenovo: {"message":"logins","results":["abhishek","dmitry","shuyu"]}
lenovo: ["abhishek","dmitry","shuyu"] does not match ["abhishek","dmitry","ricky","shuyu"]
google: invoking login: {"message":"login","username":"ricky","affiliation":"google","password":"password"}
google: {"message":"loginReply","token":"ricky","txId":"c42fd5b5-08f9-414d-bb74-78a3f516e84c"}
google: 'loginReply' ignored, still waiting for createParcelNotification
lenovo: {"message":"logins","results":["abhishek","dmitry","ricky","shuyu"]}
lenovo: ["abhishek","dmitry","ricky","shuyu"] MATCHES ["abhishek","dmitry","ricky","shuyu"]
lenovo: invoking createParcel: {"message":"createParcel","shipper":"lenovo","receiver":"google","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95","orderId":"order1","pickupAddress":"Xuhui, Shanghai, PRC","destinationAddress":"Mountain View, California, USA","size":[61,46,46],"weight":5000,"manifest":"Lenovo X220i laptop","declaredValue":"500","currency":"USD"}
lenovo: {"message":"createParcelReply","reply":"PENDING","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95","txId":"febdc041-aac6-4404-b202-8d683f8aae4a"}
lenovo: {"message":"createParcelNotification","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95"}
sfexpress: {"message":"createParcelNotification","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95"}
sfexpress: 'createParcelNotification' ignored, still waiting for buyRouteNotification
fedex: {"message":"createParcelNotification","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95"}
fedex: 'createParcelNotification' ignored, still waiting for pickupNotification
google: {"message":"createParcelNotification","parcelId":"3efa81b6-d771-4a20-92c4-ed44cf7f9d95"}
google: createParcelNotification received, next step
google: invoking findRoute: {"message":"findRoute","parcelId":"0e69ed8c-8e1f-4492-bb1c-75623a1d0d76"}
google: invoking buyRoute: {"message":"buyRoute","routeId":"322ef2ae-8243-41ba-af2e-6405f0fb1501","parcelId":"$UUID","payment":100,"currency":"USD"}
google: {"message":"routes","results":{"route":[{"number":"1","hops":"Lenovo(SF Express)-Shanghai(SF Express)-San Francisco(UPS)-Google","time":"1day4h","price":"$8"},{"number":"2","hops":"Lenovo(EMS)-Shanghai(EMS)-San Francisco(UPS)-Google","time":"2day9h","price":"$6"},{"number":"3","hops":"Lenovo(DHL)-Shanghai(DHL)-San Francisco(DHL)-Google","time":"22h","price":"$10"},{"number":"4","hops":"Lenovo(SF Express)-Shanghai(DHL)-San Francisco(DHL)-Google","time":"1day2h","price":"$9"}]}}
google: 'routes' ignored, still waiting for pickupNotification
google: {"message":"buyRouteReply","reply":"BUYING","parcelId":"$UUID","txId":"82862d48-8deb-40d4-9e0e-278c516270ee"}
google: 'buyRouteReply' ignored, still waiting for pickupNotification
lenovo: {"message":"buyRouteNotification","parcelId":"$UUID"}
sfexpress: {"message":"buyRouteNotification","parcelId":"$UUID"}
fedex: {"message":"buyRouteNotification","parcelId":"$UUID"}
sfexpress: buyRouteNotification received, next step
google: {"message":"buyRouteNotification","parcelId":"$UUID"}
google: 'buyRouteNotification' ignored, still waiting for pickupNotification
fedex: 'buyRouteNotification' ignored, still waiting for pickupNotification
sfexpress: invoking pickup: {"message":"pickup","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf"}
sfexpress: {"message":"pickupReply","reply":"TRANSIT","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf","txId":"caae0746-1e47-4e7e-b7ec-bfabee251871"}
lenovo: {"message":"pickupNotification","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf"}
fedex: {"message":"pickupNotification","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf"}
sfexpress: {"message":"pickupNotification","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf"}
fedex: pickupNotification received, next step
google: {"message":"pickupNotification","parcelId":"09817f06-0cec-4c9d-9089-bd3fba6a0baf"}
google: pickupNotification received, next step
fedex: invoking pickup: {"message":"pickup","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a"}
sfexpress: {"message":"pickupNotification","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a"}
fedex: {"message":"pickupReply","reply":"TRANSIT","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a","txId":"ec345a51-ba71-49c0-94e8-fbbed867c7a4"}
fedex: {"message":"pickupNotification","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a"}
lenovo: {"message":"pickupNotification","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a"}
google: {"message":"pickupNotification","parcelId":"4b0a50fa-318d-4c2d-8169-d18daea35a0a"}
google: pickupNotification received, next step
google: invoking pickup: {"message":"pickup","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1"}
google: SUCCESS
google: {"message":"pickupReply","reply":"TRANSIT","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1","txId":"280faa46-36fb-493f-bac8-cf85326aa0b0"}
sfexpress: {"message":"pickupNotification","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1"}
lenovo: {"message":"pickupNotification","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1"}
fedex: {"message":"pickupNotification","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1"}
google: {"message":"pickupNotification","parcelId":"cf504e4f-fdb9-4b81-907c-8bc8c4fff0e1"}
```