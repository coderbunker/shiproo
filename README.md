# design

## features

* open market for couriers capacity on which can be issued temporary reservation for a fee
* receiver selects is own shipping with options
* payment on delivery

## users perspective (simplified flow for hackathon)

Entities: shipper, courier, receiver

Examples

* ShipperA: Lenovo (Shuyu)
* CourierA: SF (Abhishek)
* CourierB: Fedex (Dmitry)
* ReceiverA : Google (employee: Ricky)

Users ShipperA, ReceiverA, CourierA, CourierB all login to their respective peer run by their organization

```text
Outside the scope: (ReceiverA has ordered from ShipperA, creating an orderId)

ShipperA publishes ParcelA information related to orderId
  ...publishes to the blockchain so that everyone can see the ParcelA information
ReceiverA selects ParcelA and searches published courier legs that best fit its needs
  ...or this could be done by logistics or courier companies bidding on unrouted Parcel?
ReceiverA buys a shipping route as RouteA for ParcelA
  ...by buying from the different courier on each leg
CourierA picks up ParcelA from ShipperA by scanning its QRCode
  ...blockchain contains route information which confirms that the first leg is for CourierA
CourierB picks up ParcelA from CourierA by scanning its QRCode
  ...blockchain contains route information which confirms that the second leg is for CourierB
ReceiverA receivers ParcelA from CourierB by scanning its QRCode
  ...shipping completed, payment back to ShipperA
```

## Websocket API

generally, all messages contain message id, identifier of user sending

### login

login flow

```
{
    message: "login"
    username: "$USERNAME"
    password: "password
}
```

we set the channel to be associated to this USERNAME

```
{
    token: "...."
}
```

### create parcel

create a parcel

```
{
    message: "createParcel",
    shipper: "lenovo"
    receiver: "google",
    parcel: {
        // stored as-is
        orderId,
        pickupAddress,
        destinationAddress
        size[x,y,z],
        weigth[g],
    },
    customs: {
     manifest,
     declaredValue=500FC
    }
}
```

returns:

```
{
    parcelId: "TRANSACTION_ID"
}
```

separetely, on the receiver side, notification

```
{
    message: "parcelNotification",
    parcelId: "TRANSACTION_ID"
}
```

### route finding

```
{
    message: "findRoute",
    parcelId: "TRANSACTION_ID"
}
```

gets the legs in the API that matches (could multiple routes)

```
{

    message: "findRouteReply",
    routes: {
        $ROUTE_ID: {
            state: "PROPOSED",
            hops: [
                {legATransactionId, state: "PLANNED"}, 
                {legBTransactionId, state: "PLANNED"}, 
                {legCTransactionId, state: "PLANNED"}, 
            ],
            price: 100
        }
    }
}
```

### route buying

receiver buys the route

```
{
    message: "buyRoute",
    routeId: "TRANSACTION_ID",
    payment: 100
}
```

change state the route as ACTIVE

```
{
   message: "buyRouteReply",
   transactionId: ""
}
```

### pickup


```
{
    message: "pickup",
    parcelId: PARCEL_ID
}
```

we know current holder from PARCEL information

we know who's picking up by the username

