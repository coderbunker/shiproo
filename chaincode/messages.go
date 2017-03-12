package main

import (
	"time"
)

type Legstate int

const (
	Planned Legstate = iota
)

type Login struct {
	Message  string    `json:"message"`
	UserName string    `json:"username"`
	Password string    `json:"password"`
	Token    string    `json:"token,omitempty"`
	LasLogin time.Time `json:"lastLogin"`
}

type LogingReply struct {
	Token string `json:"token"`
}

type QueryLoginReply struct {
	Message string  `json:"message"`
	Logins  []Login `json:"logins"`
}

type Parcel struct {
	OrderID     string `json:"orderId"`
	Origin      string `json:"origin"`
	Destination string `json:"destination"`
	Size        []int  `json:"size"`
	Weight      int    `json:"int"`

	CurrentHop int
	Hops       []Hop
}

type CreateParcel struct {
	Message  string            `json:"message"`
	Shipper  string            `json:"shipper"`
	Receiver string            `json:"receiver"`
	Parcel   Parcel            `json:"parcel"`
	Customs  map[string]string `json:"customs"`
	ParcelID string            `json:"parcelId"` // passed by R
}

type CreateParcelReply struct {
	ParcelID string `json:"parcelId"`
}

type ParcelNotification struct {
	Message string `json:"message"`
}

type QueryRoute struct {
	Message  string `json:"message"`
	ParcelID string `json:"parcelId"` // passed by R
}
type QueryRouteReply struct {
	Message string  `json:"message"`
	Routes  []Route `json:"routes"`
}

type Route struct {
	Hops     []Hop
	Payment  float64 `json:"payment"`
	Currency string  `json:"currency"`
}

type BuyRoute struct {
	Message  string `json:"message"`
	ParcelID string `json:"parcelID"`
	Route    Route
}

type BuyRouteReply struct {
	Message string `json:"message"`
	RouteID string `json:"routeId"`
}

type Hop struct {
	HopID       string  `json:"hopId"` // set by R when sending buy
	Price       float64 `json:"price"`
	Currency    string  `json:"currency"`
	Origin      string  `json:"origin"`
	Destination string  `json:"destination"`
}

type Courier struct {
	Name string `json:"name"`
	Hops []Hop  `json:"hops"`
}

type Pickup struct {
	Message  string `json:"message"`
	ParcelID string `json:"parcelID"`
	HopID    string `json:"hopId"`
}

type PickupReply struct {
	Message string `json:"message"`
	Reply   string `josn:"reply"`
}

type PickupNotification struct {
	Message string `json:"message"`
	RouteID string `json:"routeId"`
}
