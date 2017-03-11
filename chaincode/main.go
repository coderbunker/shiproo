package main

import (
	"errors"
	"fmt"

	"encoding/json"

	"github.com/davecgh/go-spew/spew"
	"github.com/hyperledger/fabric/core/chaincode/shim"
	"github.com/pborman/uuid"
)

type chain struct {
}

func put(stub shim.ChaincodeStubInterface, item interface{}, key string) error {
	js, err := json.Marshal(item)
	if err != nil {
		spew.Dump(err)
		return err
	}
	if err = stub.PutState(key, js); err != nil {
		spew.Dump(err)
	}
	return err
}

func get(stub shim.ChaincodeStubInterface, item interface{}, key string) error {
	ljs, err := stub.GetState(key)
	if err != nil {
		spew.Dump(err)
		return err
	}
	if err = json.Unmarshal(ljs, item); err != nil {
		spew.Dump(err)
	}
	return err
}

func (o *chain) Init(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump("init")
	logins := []Login{
		{UserName: "shuyu", Password: "shuyu", Token: "0"},
	}
	put(stub, logins, "logins")
	parcels := []CreateParcel{}
	put(stub, parcels, "parcels")
	routes := []Route{}
	put(stub, routes, "routes")
	pickups := []Pickup{}
	put(stub, pickups, "pickups")
	return
}

func (o *chain) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump(function)
	switch function {
	case "login":
		return handleLogin(stub, args)
	case "createParcel":
		return handleCreateParcel(stub, args)
	case "buyRoute":
		return handleBuyRoute(stub, args)
	case "pickup":
		return handlePickup(stub, args)
	}
	return
}

func (o *chain) Query(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump(function)
	switch function {
	case "queryRoute":
		return queryRoute(stub, args)
	}
	return
}

func main() {
	err := shim.Start(new(chain))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}

func handleLogin(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	var logins []Login
	get(stub, &logins, "logins")

	spew.Dump(args)

	var lx Login
	if err = json.Unmarshal([]byte(args[0]), &lx); err != nil {
		spew.Dump(err)
		return
	}

	var login *Login
	for _, l := range logins {
		if l.UserName == lx.UserName && l.Password == lx.Password {
			login = &l
			break
		}
	}
	if login == nil {
		err = errors.New("matching user not found")
		spew.Dump(err)
		return
	}
	login.Token = uuid.NewUUID().String()
	if err = put(stub, logins, "logins"); err != nil {
		return
	}
	spew.Dump("loggedin:", login)
	return
}

func handleCreateParcel(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	var parcel CreateParcel
	if err = json.Unmarshal([]byte(args[0]), &parcel); err != nil {
		spew.Dump(err)
		return
	}
	err = put(stub, parcel, parcel.ParcelID)
	return
}

func queryRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	// pre declare routes, load them
	var routes []Route
	get(stub, &routes, "routes")
	spew.Dump(args)

	var fRoute QueryRoute
	if err = json.Unmarshal([]byte(args[0]), &fRoute); err != nil {
		spew.Dump(err)
		return
	}
	// find parcel
	var parcel CreateParcel
	if err = get(stub, &parcel, fRoute.ParcelID); err != nil {
		spew.Dump(err)
		return
	}
	// return all routes for now - TODO
	ret, err = json.Marshal(QueryRouteReply{Routes: routes})
	return
}

// create HopIDs based on routeId
func handleBuyRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	var bRoute BuyRoute
	if err = json.Unmarshal([]byte(args[0]), &bRoute); err != nil {
		spew.Dump(err)
		return
	}
	// find parcel
	var parcel CreateParcel
	if err = get(stub, &parcel, bRoute.ParcelID); err != nil {
		spew.Dump(err)
		return
	}
	// for each hop in route, create a Leg
	for _, hop := range bRoute.Route.Hops {
		put(stub, hop, hop.HopID)
		spew.Dump("saved hop", hop)
	}
	return
}

func handlePickup(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	var pickup Pickup
	if err = json.Unmarshal([]byte(args[0]), &pickup); err != nil {
		spew.Dump(err)
		return
	}
	// find parcel
	var parcel CreateParcel
	if err = get(stub, &parcel, pickup.ParcelID); err != nil {
		spew.Dump(err)
		return
	}

	return
}

/*
	spew.Dump(stub)

	cert, err := stub.GetCallerCertificate()
	if err != nil {
		return
	}
	tcert, err := primitives.DERToX509Certificate(cert)
	if err != nil {
		return nil, err
	}
	spew.Dump(tcert)

	role, err := stub.ReadCertAttribute("role")
	spew.Dump(role)
	spew.Dump(err)

*/
