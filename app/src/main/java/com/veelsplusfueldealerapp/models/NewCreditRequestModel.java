package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class NewCreditRequestModel {
    @SerializedName("fuelDealerCustomerMapId")
    public String fuelDealerCustomerMapId;
    @SerializedName("reqQuantity")
    public String reqQuantity;
    @SerializedName("reqCreditAmount")
    public String reqCreditAmount;
    @SerializedName("estimatedRefuelDate")
    public String estimatedRefuelDate;
    @SerializedName("fuelDealerId")
    public String fuelDealerId;
    @SerializedName("vehicleId")
    public String vehicleId;
    @SerializedName("driverId")
    public String driverId;
    @SerializedName("fleetNoFleetStatus")
    public String fleetNoFleetStatus;
    @SerializedName("fuelProductId")
    public String fuelProductId;
    @SerializedName("fuelCorporateId")
    public String fuelCorporateId;
    @SerializedName("companyName")
    public String companyName;
    @SerializedName("creditSource")
    public String creditSource;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("PANno")
    public String PANno;

    @SerializedName("vehicleNumber")
    public String vehicleNumber;

    @SerializedName("vehicleVPStatus")
    public String vehicleVPStatus;

    @SerializedName("transDateTime")
    public String transDateTime;

    @SerializedName("transactionTime")
    public String transactionTime;

    @SerializedName("creditAmount")
    public String creditAmount;

    @SerializedName("transactionStatus")
    public String transactionStatus;

    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;

    @SerializedName("actualCreditQuantity")
    public String actualCreditQuantity;

    @SerializedName("productRate")
    public String productRate;

    @SerializedName("manualCrNumber")
    public String manualCrNumber;

    @SerializedName("fuelStaffPerformId")
    public String fuelStaffPerformId;


   /* fuelDealerCustomerMapId,reqQuantity,reqCreditAmount,S
    estimatedRefuelDate,fuelDealerId,vehicleId,driverId,
    fleetNoFleetStatus,fuelProductId,fuelCorporateId,
    creditSource,PANno,transDateTime,transactionTime,
    creditAmount,transactionStatus,fuelDealerStaffId,
    actualCreditQuantity,createdAt,
    productRate,vehicleNumber,vehicleVPStatus,
    manualCrNumber*/


    public NewCreditRequestModel() {
    }

    public NewCreditRequestModel(String fuelDealerCustomerMapId, String reqQuantity,
                                 String reqCreditAmount, String estimatedRefuelDate,
                                 String fuelDealerId, String vehicleId,
                                 String driverId, String fleetNoFleetStatus,
                                 String fuelProductId, String fuelCorporateId,
                                 String creditSource, String PANno, String createdAt,
                                 String vehicleNumber, String vehicleVPStatus, String transDateTime,
                                 String transactionTime, String creditAmount, String transactionStatus,
                                 String fuelDealerStaffId, String actualCreditQuantity, String productRate,
                                 String manualCrNumber, String fuelStaffPerformId) {
        this.fuelDealerCustomerMapId = fuelDealerCustomerMapId;
        this.reqQuantity = reqQuantity;
        this.reqCreditAmount = reqCreditAmount;
        this.estimatedRefuelDate = estimatedRefuelDate;
        this.fuelDealerId = fuelDealerId;
        this.vehicleId = vehicleId;
        this.driverId = driverId;
        this.fleetNoFleetStatus = fleetNoFleetStatus;
        this.fuelProductId = fuelProductId;
        this.fuelCorporateId = fuelCorporateId;
        this.creditSource = creditSource;
        this.PANno = PANno;
        this.createdAt = createdAt;
        this.vehicleNumber = vehicleNumber;
        this.vehicleVPStatus = vehicleVPStatus;
        this.transDateTime = transDateTime;
        this.transactionTime = transactionTime;
        this.creditAmount = creditAmount;
        this.transactionStatus = transactionStatus;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.actualCreditQuantity = actualCreditQuantity;
        this.productRate = productRate;
        this.manualCrNumber = manualCrNumber;
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

 /*   fuelDealerCustomerMapId,reqQuantity,reqCreditAmount
            estimatedRefuelDate,fuelDealerId,vehicleId,driverId,fleetNoFleetStatus,
            fuelProductId,fuelCorporateId,creditSource,
            PANno,createdAt,vehicleNumber,vehicleVPStatus*/

/*
    fuelDealerCustomerMapId,reqQuantity,reqCreditAmount,
    estimatedRefuelDate,fuelDealerId,vehicleId,
    driverId,fleetNoFleetStatus,fuelProductId,
    fuelCorporateId,creditSource,PANno,
    createdAt,vehicleNumber,vehicleVPStatus*/

    public NewCreditRequestModel(String fuelDealerCustomerMapId, String fuelCorporateId, String companyName) {
        this.fuelDealerCustomerMapId = fuelDealerCustomerMapId;
        this.fuelCorporateId = fuelCorporateId;
        this.companyName = companyName;
    }

    public String getFuelDealerCustomerMapId() {
        return fuelDealerCustomerMapId;
    }

    public void setFuelDealerCustomerMapId(String fuelDealerCustomerMapId) {
        this.fuelDealerCustomerMapId = fuelDealerCustomerMapId;
    }

    public String getReqQuantity() {
        return reqQuantity;
    }

    public void setReqQuantity(String reqQuantity) {
        this.reqQuantity = reqQuantity;
    }

    public String getReqCreditAmount() {
        return reqCreditAmount;
    }

    public void setReqCreditAmount(String reqCreditAmount) {
        this.reqCreditAmount = reqCreditAmount;
    }

    public String getEstimatedRefuelDate() {
        return estimatedRefuelDate;
    }

    public void setEstimatedRefuelDate(String estimatedRefuelDate) {
        this.estimatedRefuelDate = estimatedRefuelDate;
    }

    public String getFuelDealerId() {
        return fuelDealerId;
    }

    public void setFuelDealerId(String fuelDealerId) {
        this.fuelDealerId = fuelDealerId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getFleetNoFleetStatus() {
        return fleetNoFleetStatus;
    }

    public void setFleetNoFleetStatus(String fleetNoFleetStatus) {
        this.fleetNoFleetStatus = fleetNoFleetStatus;
    }

    public String getFuelProductId() {
        return fuelProductId;
    }
   /* fuelDealerCustomerMapId,reqQuantity,reqCreditAmount,
    estimatedRefuelDate,fuelDealerId,vehicleId,driverId,
    fleetNoFleetStatus,fuelProductId,fuelCorporateId*/

    public void setFuelProductId(String fuelProductId) {
        this.fuelProductId = fuelProductId;
    }

    public String getFuelCorporateId() {
        return fuelCorporateId;
    }

    public void setFuelCorporateId(String fuelCorporateId) {
        this.fuelCorporateId = fuelCorporateId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


  /*  fuelDealerCustomerMapId,reqQuantity,reqCreditAmount,estimatedRefuelDate,fuelDealerId,
    vehicleId,driverId,fleetNoFleetStatus,fuelProductId,fuelCorporateId*/


}
