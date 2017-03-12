package main

import (
	"errors"
	"fmt"

	"encoding/json"

	"time"

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
		{UserName: "abhishek", Password: "abhishek", Token: "0"},
		{UserName: "ricky", Password: "ricky", Token: "0"},
		{UserName: "dmitry", Password: "dmitry", Token: "0"},
	}
	put(stub, logins, "logins")
	parcels := []CreateParcel{}
	put(stub, parcels, "parcels")
	pickups := []Pickup{}
	put(stub, pickups, "pickups")
	c1 := Courier{
		Name: "SFexpress",
		Hops: []Hop{
			Hop{
				HopID:       uuid.NewUUID().String(),
				Price:       20,
				Currency:    "CNY",
				Origin:      "Lenovo",
				Destination: "ShanghaiPudong",
			},
			Hop{
				HopID:       uuid.NewUUID().String(),
				Price:       30,
				Currency:    "CNY",
				Origin:      "Lenovo",
				Destination: "ShanghaiHongQiao",
			},
		}}
	c2 := Courier{
		Name: "FedEx",
		Hops: []Hop{
			Hop{
				HopID:       uuid.NewUUID().String(),
				Price:       30,
				Currency:    "USD",
				Origin:      "ShanghaiPudong",
				Destination: "SanFrancisco",
			},
			Hop{
				HopID:       uuid.NewUUID().String(),
				Price:       30,
				Currency:    "USD",
				Origin:      "ShanghaiHongQiao",
				Destination: "SanFrancisco",
			},
		}}
	couriers := []Courier{
		c1, c2,
	}
	put(stub, couriers, "couriers")

	// create routes
	// return all routes for now - TODO
	routes := []string{}
	// all couriers have same number of routes
	nHops := len(couriers[0].Hops)
	for n := 0; n < nHops; n++ {
		hops := []Hop{}
		for _, courier := range couriers {
			hops = append(hops, courier.Hops[n])
		}
		price := 0.0
		for _, h := range hops {
			price += h.Price * convertToCny[h.Currency]
		}
		route := Route{
			Hops:     hops,
			RouteID:  uuid.NewUUID().String(),
			Currency: "CNY",
			Payment:  price,
		}
		// write route
		put(stub, route, route.RouteID)
		spew.Dump("route", route)
		routes = append(routes, route.RouteID)
	}
	put(stub, routes, "routes")
	return
}

func (o *chain) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump("invoke", function)
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
	spew.Dump("query:", function)
	switch function {
	case "queryroutes":
		return queryRoute(stub, args)
	case "queryLogins":
		return queryLogins(stub, args)
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
	login.LasLogin = time.Now()
	if err = put(stub, logins, "logins"); err != nil {
		return
	}
	spew.Dump("loggedin:", login)
	return
}

func queryLogins(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	var logins []Login
	get(stub, &logins, "logins")
	ret, err = json.Marshal(QueryLoginReply{Logins: logins})
	return
}

func handleCreateParcel(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	spew.Dump(args)
	var parcel CreateParcel
	if err = json.Unmarshal([]byte(args[0]), &parcel); err != nil {
		spew.Dump(err)
		return
	}
	err = put(stub, parcel, parcel.ParcelID)
	spew.Dump("created", parcel)
	return
}

var convertToCny = map[string]float64{
	"CNY": 1, "USD": 6.92,
}

func queryRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
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
	if parcel.ParcelID == "" {
		err = errors.New("Missing Parcels")
	}
	// return all routes for now - TODO
	rids := []string{}
	if err = get(stub, rids, "routes"); err != nil {
		spew.Dump(err)
		return
	}
	routes := []Route{}
	for _, rid := range rids {
		var route Route
		if err = get(stub, &route, rid); err != nil {
			routes = append(routes, route)
		}
	}
	ret, err = json.Marshal(QueryRouteReply{Routes: routes})
	spew.Dump("reply", ret)
	return
}

// create HopIDs based on routeId
func handleBuyRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	spew.Dump(args)
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
	if parcel.ParcelID == "" {
		err = errors.New("Parcel not found")
		return
	}
	// find HOPS using routeId
	var route Route
	if err = get(stub, route, bRoute.RouteID); err != nil {
		spew.Dump(err)
		return nil, err
	}
	parcel.Parcel.Hops = route.Hops
	err = put(stub, parcel, parcel.ParcelID)
	spew.Dump("bought", parcel)
	return
}

func handlePickup(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	spew.Dump(args)
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
	found := false
	for i, h := range parcel.Parcel.Hops {
		if h.HopID == pickup.HopID {
			found = true
		}
		parcel.Parcel.CurrentHop = i
	}
	if !found {
		err = errors.New("Hop not found for parcel")
		return
	}
	err = put(stub, parcel, parcel.ParcelID)
	spew.Dump("pickup", parcel)
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
