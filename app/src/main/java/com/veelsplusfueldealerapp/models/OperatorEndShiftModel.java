package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

import retrofit2.http.Field;

public class OperatorEndShiftModel {


    @SerializedName("fuelStaffPerformId")
    public String fuelStaffPerformId;
    @SerializedName("closeMetReading")
    public String closeMetReading;
    @SerializedName("closeMetDateTime")
    public String closeMetDateTime;
    @SerializedName("unitSales")
    public String unitSales;
    @SerializedName("testStockByMeter")
    public String testStockByMeter;
    @SerializedName("recoveryAmount")
    public String recoveryAmount;
    @SerializedName("closeTime")
    public String closeTime;

    @SerializedName("creditAmount")
    public String creditAmount;


    public OperatorEndShiftModel(String fuelStaffPerformId, String closeMetReading,
                                 String closeMetDateTime, String unitSales,
                                 String testStockByMeter, String recoveryAmount,
                                 String closeTime, String creditAmount) {
        this.fuelStaffPerformId = fuelStaffPerformId;
        this.closeMetReading = closeMetReading;
        this.closeMetDateTime = closeMetDateTime;
        this.unitSales = unitSales;
        this.testStockByMeter = testStockByMeter;
        this.recoveryAmount = recoveryAmount;
        this.closeTime = closeTime;
        this.creditAmount = creditAmount;

    }

    public String getFuelStaffPerformId() {
        return fuelStaffPerformId;
    }

    public void setFuelStaffPerformId(String fuelStaffPerformId) {
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

    public String getCloseMetReading() {
        return closeMetReading;
    }

    public void setCloseMetReading(String closeMetReading) {
        this.closeMetReading = closeMetReading;
    }

    public String getCloseMetDateTime() {
        return closeMetDateTime;
    }

    public void setCloseMetDateTime(String closeMetDateTime) {
        this.closeMetDateTime = closeMetDateTime;
    }

    public String getUnitSales() {
        return unitSales;
    }

    public void setUnitSales(String unitSales) {
        this.unitSales = unitSales;
    }

    public String getTestStockByMeter() {
        return testStockByMeter;
    }

    public void setTestStockByMeter(String testStockByMeter) {
        this.testStockByMeter = testStockByMeter;
    }

    public String getRecoveryAmount() {
        return recoveryAmount;
    }

    public void setRecoveryAmount(String recoveryAmount) {
        this.recoveryAmount = recoveryAmount;
    }
  /* fuelStaffPerformId,closeMetReading,
   closeMetDateTime,unitSales,
   testStockByMeter,recoveryAmount,closeTime*/

}
