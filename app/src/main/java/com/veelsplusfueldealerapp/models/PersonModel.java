package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class PersonModel {
    @SerializedName("reqQuantity")
    public String reqQuantity;
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
    @SerializedName("createdAt")
    public String createdAt;

    @SerializedName("vehicleNumber")
    public String vehicleNumber;

    @SerializedName("vehicleVPStatus")
    public String vehicleVPStatus;

    public PersonModel(String reqQuantity, String reqCreditAmount,
                       String estimatedRefuelDate, String fuelDealerId,
                       String driverId, String vehicleId, String fuelProductId,
                       String panNumber, String fuelDealerCustomerMapId,
                       String createdAt,String vehicleNumber,String vehicleVPStatus) {
        this.reqQuantity = reqQuantity;
        this.reqCreditAmount = reqCreditAmount;
        this.estimatedRefuelDate = estimatedRefuelDate;
        this.fuelDealerId = fuelDealerId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.fuelProductId = fuelProductId;
        this.panNumber = panNumber;
        this.fuelDealerCustomerMapId = fuelDealerCustomerMapId;
        this.createdAt = createdAt;
        this.vehicleNumber = vehicleNumber;
        this.vehicleVPStatus = vehicleVPStatus;

    }


   /* reqQuantity,reqCreditAmount,estimatedRefuelDate,
    fuelDealerId,driverId,vehicleId,fuelProductId,panNumber,fuelDealerCustomerMapId,
    createdAt,vehicleNumber,vehicleVPStatus*/

 /*   reqQuantity,reqCreditAmount,estimatedRefuelDate,
    fuelDealerId,driverId,vehicleId,fuelProductId,
    panNumber,fuelDealerCustomerMapId,createdAt*/


}
