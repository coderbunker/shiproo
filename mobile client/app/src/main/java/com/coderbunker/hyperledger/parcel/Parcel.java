package com.coderbunker.hyperledger.parcel;


public class Parcel {
    private String shipper;
    private String receiver;
    private String parcelOrderId;
    private String parcelPickupAddress;
    private String parcelDestinationAddress;
    private String parcelSize;
    private String parcelWeight;
    private String parcelManifest;
    private String parcelDeclaredValue;

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getParcelOrderId() {
        return parcelOrderId;
    }

    public void setParcelOrderId(String parcelOrderId) {
        this.parcelOrderId = parcelOrderId;
    }

    public String getParcelPickupAddress() {
        return parcelPickupAddress;
    }

    public void setParcelPickupAddress(String parcelPickupAddress) {
        this.parcelPickupAddress = parcelPickupAddress;
    }

    public String getParcelDestinationAddress() {
        return parcelDestinationAddress;
    }

    public void setParcelDestinationAddress(String parcelDestinationAddress) {
        this.parcelDestinationAddress = parcelDestinationAddress;
    }

    public String getParcelSize() {
        return parcelSize;
    }

    public void setParcelSize(String parcelSize) {
        this.parcelSize = parcelSize;
    }

    public String getParcelWeight() {
        return parcelWeight;
    }

    public void setParcelWeight(String parcelWeight) {
        this.parcelWeight = parcelWeight;
    }

    public String getParcelManifest() {
        return parcelManifest;
    }

    public void setParcelManifest(String parcelManifest) {
        this.parcelManifest = parcelManifest;
    }

    public String getParcelDeclaredValue() {
        return parcelDeclaredValue;
    }

    public void setParcelDeclaredValue(String parcelDeclaredValue) {
        this.parcelDeclaredValue = parcelDeclaredValue;
    }
}
