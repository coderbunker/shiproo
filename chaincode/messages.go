package main

type Legstate int

const (
	Planned Legstate = iota
)

type Login struct {
	Message  string `json:"message"`
	UserName string `json:"username"`
	Password string `json:"password"`
}

type LogingReply struct {
	Token string `json:"token"`
}

type CreateParcel struct {
	Message  string            `json:"message"`
	Shipper  string            `json:"shipper"`
	Receiver string            `json:"receiver"`
	Parcel   map[string]string `json:"parcel"`
	Customs  map[string]string `json:"customs"`
}

type CreateParcelReply struct {
	ParcelID string `json:"parcelId"`
}

type ParcelNotification struct {
	Message string `json:"message"`
}

type FindRoute struct {
	Message  string `json:"message"`
	ParcelID string `json:"parcelID`
}

type FindRouteReply struct {
	Message string `json:"message"`
	Routes  []Route
}

type BuyRoute struct {
	Message  string `json:"message"`
	RouteID  string `json:"routeId"`
	Payment  int    `json:"payment"`
	Currency string `json:"currency"`
}

type BuyRouteReply struct {
	Message string `json:"message"`
	RouteID string `json:"routeId"`
}

type Route struct {
	RouteID  string `json:"state"`
	State    string `json:"state"`
	Hops     []Hop  `json:"hop"`
	Price    int    `json:"price"`
	Currency string `json:"currency"`
}

type Hop struct {
	LegId string `json:"leg"`
	State string `json:"state"`
}

type Pickup struct {
	Message  string `json:"message"`
	ParcelId string `json:"parcelId"`
}

type PickupReply struct {
	Message string `json:"message"`
	Reply   string `josn:"reply"`
}

type PickupNotification struct {
	Message string `json:"message"`
	RouteID string `json:"routeId"`
}
