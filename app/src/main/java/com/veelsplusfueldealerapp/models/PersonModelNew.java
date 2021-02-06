package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class PersonModelNew {

    @SerializedName("reqQuantity")
    public String reqQuantity;

    public PersonModelNew(String reqQuantity, String reqCreditAmount, String estimatedRefuelDate, String fuelDealerId, String driverId, String vehicleId, String fuelProductId, String panNumber, String fuelDealerCustomerMapId, String creditAmount, String actualCreditQuantity, String createdAt, String transDateTime, String transactionTime, String transactionStatus, String vehicleNumber, String vehicleVPStatus,String productRate, String fuelDealerStaffId) {
        this.reqQuantity = reqQuantity;
        this.reqCreditAmount = reqCreditAmount;
        this.estimatedRefuelDate = estimatedRefuelDate;
        this.fuelDealerId = fuelDealerId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.fuelProductId = fuelProductId;
        this.panNumber = panNumber;
        this.fuelDealerCustomerMapId = fuelDealerCustomerMapId;
        this.creditAmount = creditAmount;
        this.actualCreditQuantity = actualCreditQuantity;
        this.createdAt = createdAt;
        this.transDateTime = transDateTime;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.vehicleNumber = vehicleNumber;
        this.vehicleVPStatus = vehicleVPStatus;
        this.productRate = productRate;
        this.fuelDealerStaffId = fuelDealerStaffId;

    }
   /* reqQuantity,reqCreditAmount,estimatedRefuelDate,
    fuelDealerId,driverId,vehicleId,fuelProductId,
    panNumber,fuelDealerCustomerMapId,creditAmount,
    actualCreditQuantity,createdAt,transDateTime,
    transactionTime,transactionStatus,
    vehicleNumber,vehicleVPStatus,productRate*/
    @SerializedName("reqCreditAmount")
    public String reqCreditAmount;

    @SerializedName("estimatedRefuelDate")
    public String estimatedRefuelDate;

    @SerializedName("fuelDealerId")
    public String fuelDealerId;

    @SerializedName("driverId")
    public String driverId;

    @SerializedName("vehicleId")
    public String vehicleId;

    @SerializedName("fuelProductId")
    public String fuelProductId;

    @SerializedName("panNumber")
    public String panNumber;

    @SerializedName("fuelDealerCustomerMapId")
    public String fuelDealerCustomerMapId;

    @SerializedName("creditAmount")
    public String creditAmount;

    @SerializedName("actualCreditQuantity")
    public String actualCreditQuantity;

    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("transDateTime")
    public String transDateTime;

    @SerializedName("transactionTime")
    public String transactionTime;

    @SerializedName("transactionStatus")
    public String transactionStatus;

    @SerializedName("vehicleNumber")
    public String vehicleNumber;

    @SerializedName("vehicleVPStatus")
    public String vehicleVPStatus;

    @SerializedName("productRate")
    public String productRate;

    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;



   /* reqQuantity,reqCreditAmount,estimatedRefuelDate,
    fuelDealerId,driverId,vehicleId,fuelProductId,
    panNumber,fuelDealerCustomerMapId,creditAmount,
    actualCreditQuantity,createdAt,transDateTime,
    transactionTime,
    transactionStatus,vehicleNumber,vehicleVPStatus*/



}
