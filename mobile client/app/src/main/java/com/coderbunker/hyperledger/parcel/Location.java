package com.coderbunker.hyperledger.parcel;


public class Location {
    private String locationFrom;
    private String locationTo;

    public Location(String locationFrom, String locationTo) {
        this.locationFrom = locationFrom;
        this.locationTo = locationTo;
    }

    public String getLocationFrom() {
        return locationFrom;
    }

    public void setLocationFrom(String locationFrom) {
        this.locationFrom = locationFrom;
    }

    public String getLocationTo() {
        return locationTo;
    }

    public void setLocationTo(String locationTo) {
        this.locationTo = locationTo;
    }
}
