package com.coderbunker.hyperledger.parcel;


public class ParcelService {

    private static ParcelService INSTANCE = null;

    private String companySend;
    private String companyReceive;
    private String parcelSend;
    private String parcelReceive;
    private String size;
    private String weight;

    public static ParcelService getInstance() {
        if (INSTANCE == null)
            INSTANCE = new ParcelService();
        return INSTANCE;
    }

    public String getCompanySend() {
        return companySend;
    }

    public void setCompanySend(String companySend) {
        this.companySend = companySend;
    }

    public String getCompanyReceive() {
        return companyReceive;
    }

    public void setCompanyReceive(String companyReceive) {
        this.companyReceive = companyReceive;
    }

    public String getParcelSend() {
        return parcelSend;
    }

    public void setParcelSend(String parcelSend) {
        this.parcelSend = parcelSend;
    }

    public String getParcelReceive() {
        return parcelReceive;
    }

    public void setParcelReceive(String parcelReceive) {
        this.parcelReceive = parcelReceive;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
}
