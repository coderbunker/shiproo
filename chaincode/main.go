package main

import (
	"errors"
	"fmt"

	"encoding/json"

	"github.com/davecgh/go-spew/spew"
	"github.com/hyperledger/fabric/core/chaincode/shim"
)

type chain struct {
}

func (o *chain) Init(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump("init")
	logins := []Login{
		{UserName: "shipper1", Password: "s1"},
	}
	ljs, err := json.Marshal(logins)
	if err != nil {
		spew.Dump(err)
	}
	spew.Dump(ljs)
	err = stub.PutState("logins", ljs)
	spew.Dump(err)
	return
}
func (o *chain) Invoke(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump(function)
	switch function {
	case "login":
		return handleLogin(stub, args)
	case "createParcel":
		return handleCreateParcel(stub, args)
	case "findRoute":
		return handleFindRoute(stub, args)
	case "buyRoute":
		return handleBuyRoute(stub, args)
	case "pickup":
		return handlePickup(stub, args)
	}
	return
}
func (o *chain) Query(stub shim.ChaincodeStubInterface, function string, args []string) (ret []byte, err error) {
	spew.Dump(stub)
	return
}

func main() {
	err := shim.Start(new(chain))
	if err != nil {
		fmt.Printf("Error starting Simple chaincode: %s", err)
	}
}

func handleLogin(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	ljs, err := stub.GetState("logins")
	spew.Dump(ljs)
	spew.Dump(err)
	if err != nil {
		return
	}

	var logins []*Login
	if err = json.Unmarshal(ljs, logins); err != nil {
		return
	}
	spew.Dump(args)
	var login *Login
	for _, login = range logins {
		if login.UserName == args[0] && login.Password == args[1] {
			break
		}
	}
	if login == nil {
		err = errors.New("matching user not found")
		return
	}
	return
}
func handleCreateParcel(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	return
}
func handleFindRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	return
}
func handleBuyRoute(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
	return
}
func handlePickup(stub shim.ChaincodeStubInterface, args []string) (ret []byte, err error) {
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
