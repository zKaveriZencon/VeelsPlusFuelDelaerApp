package com.veelsplusfueldealerapp.models;

public class OperatorInfoFragModel {
    String totalAmount;
    private String startTime;
    private String pumpNozzle;
    private String activityStatus;
    private String collectionStatus;
    private String fuelStaffPerformId;
    private String dateTime;
    private String product;

    public String getPumpNozzle() {
        return pumpNozzle;
    }

    public void setPumpNozzle(String pumpNozzle) {
        this.pumpNozzle = pumpNozzle;
    }

    private String meterSaleLiters;
    private String name;
    String price;


    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMeterSaleLiters() {
        return meterSaleLiters;
    }

    public void setMeterSaleLiters(String meterSaleLiters) {
        this.meterSaleLiters = meterSaleLiters;
    }

    public OperatorInfoFragModel(String dateTime, String product,
                                 String meterSaleLiters, String totalAmount, String pumpNozzle) {
        this.dateTime = dateTime;
        this.product = product;
        this.meterSaleLiters = meterSaleLiters;
        this.totalAmount = totalAmount;
        this.pumpNozzle = pumpNozzle;



    }
    public OperatorInfoFragModel(String product, String meterSaleLiters,
                                      String totalAmount) {
        this.product = product;
        this.meterSaleLiters = meterSaleLiters;
        this.totalAmount = totalAmount;

    }
}
