package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class ErrorLogModel {

    @SerializedName("appName")
    public String appName;

    @SerializedName("loginId")
    public String loginId;

    @SerializedName("errorMessage")
    public String errorMessage;

    public ErrorLogModel(String appName, String loginId, String errorMessage,
                         String errorType, String dateTime, String sectionName,
                         String fuelDealerId) {
        this.appName = appName;
        this.loginId = loginId;
        this.errorMessage = errorMessage;
        this.errorType = errorType;
        this.dateTime = dateTime;
        this.sectionName = sectionName;
        this.fuelDealerId = fuelDealerId;
    }

    @SerializedName("errorType")
    public String errorType;

    @SerializedName("dateTime")
    public String dateTime;

    @SerializedName("sectionName")
    public String sectionName;

    @SerializedName("fuelDealerId")
    public String fuelDealerId;
}
