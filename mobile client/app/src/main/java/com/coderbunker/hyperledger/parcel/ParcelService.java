package com.coderbunker.hyperledger.parcel;


import org.json.JSONException;
import org.json.JSONObject;

public class ParcelService {

    private static ParcelService INSTANCE = null;

    private String companySend;
    private String companyReceive;
    private String parcelSend;
    private String parcelReceive;
    private String size;
    private String weight;
    private String insurance;

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

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    // TODO replace with real data
    public JSONObject getJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("shipper", companySend);
        json.put("receiver", companyReceive);
        json.put("parcelId", "FAKEUUID");
        json.put("orderId", "order1");
        json.put("pickupAddress", parcelSend);
        json.put("destinationAddress", parcelReceive);
        json.put("size", "[61,46,46]");
        json.put("weight", weight);
        json.put("manifest", "Lenovo X220i laptop");
        json.put("declaredValue", insurance);
        json.put("currency", "USD");
        return json;
    }
}
