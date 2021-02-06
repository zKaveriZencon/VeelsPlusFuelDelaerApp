package com.veelsplusfueldealerapp.models;

import android.widget.Toast;

public class OperatorDailyWorkListModel {
    String totalAmount;
    private String startTime;
    private String pumpNozzle;
    private String activityStatus;
    private String collectionStatus;
    private String fuelStaffPerformId;
    private String dateTime;
    private String product;
    private String meterSaleLiters;
    private String name;
    String price;

    public OperatorDailyWorkListModel(String product, String meterSaleLiters,
                                      String totalAmount) {
        this.product = product;
        this.meterSaleLiters = meterSaleLiters;
        this.totalAmount = totalAmount;

    }

    public OperatorDailyWorkListModel(String dateTime, String product,
                                      String meterSaleLiters, String totalAmount) {
        this.dateTime = dateTime;
        this.product = product;
        this.meterSaleLiters = meterSaleLiters;
        this.totalAmount = totalAmount;
    }

    public OperatorDailyWorkListModel(String startTime, String pumpNozzle,
                                      String activityStatus, String collectionStatus,
                                      String fuelStaffPerformId) {
        this.startTime = startTime;
        this.pumpNozzle = pumpNozzle;
        this.activityStatus = activityStatus;
        this.collectionStatus = collectionStatus;
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

    public OperatorDailyWorkListModel(String startTime, String pumpNozzle,
                                      String activityStatus, String collectionStatus,
                                      String fuelStaffPerformId, String name) {
        this.startTime = startTime;
        this.pumpNozzle = pumpNozzle;
        this.activityStatus = activityStatus;
        this.collectionStatus = collectionStatus;
        this.fuelStaffPerformId = fuelStaffPerformId;
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getFuelStaffPerformId() {
        return fuelStaffPerformId;
    }

    public void setFuelStaffPerformId(String fuelStaffPerformId) {
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getPumpNozzle() {
        return pumpNozzle;
    }

    public void setPumpNozzle(String pumpNozzle) {
        this.pumpNozzle = pumpNozzle;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getCollectionStatus() {
        return collectionStatus;
    }

    public void setCollectionStatus(String collectionStatus) {
        this.collectionStatus = collectionStatus;
    }

}
