package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class DailyInventoryDetailsModel {
    @SerializedName("fuelInfraMapId")
    public String fuelInfraMapId;

    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;

    @SerializedName("tankNo")
    public String tankNo;
    //fuelInfraMapId,fuelDealerStaffId,tankNo,openDipReading,closeDipReading,openDipScaleReading,
    //closeDipScaleReading,openDipReadDate,closeDipReadDate,densityRecorded,vehicleNo
    @SerializedName("openDipReading")
    public String openDipReading;

    @SerializedName("closeDipReading")
    public String closeDipReading;

    @SerializedName("openDipScaleReading")
    public String openDipScaleReading;

    @SerializedName("closeDipScaleReading")
    public String closeDipScaleReading;

    @SerializedName("openDipReadDate")
    public String openDipReadDate;
    @SerializedName("closeDipReadDate")
    public String closeDipReadDate;
    @SerializedName("densityRecorded")
    public String densityRecorded;
    @SerializedName("vehicleNo")
    public String vehicleNo;
    @SerializedName("tankFuelInventoryId")
    public String tankFuelInventoryId;
    @SerializedName("closeDensity")
    public String closeDensity;

    @SerializedName("fuelDealerId")
    public String fuelDealerId;

    @SerializedName("dealerTankMap")
    public String dealerTankMap;

    @SerializedName("openDipReadTime")
    public String openDipReadTime;
    @SerializedName("closeDipReadTime")
    public String closeDipReadTime;

    @SerializedName("stockCheckStatus")
    public String stockCheckStatus;


    public DailyInventoryDetailsModel(String tankFuelInventoryId, String openDipReading,
                                      String closeDipReading, String openDipScaleReading,
                                      String closeDipScaleReading, String closeDipReadDate,
                                      String densityRecorded,
                                      String closeDensity, String closeDipReadTime,
                                      String stockCheckStatus) {
        this.tankFuelInventoryId = tankFuelInventoryId;
        this.openDipReading = openDipReading;
        this.closeDipReading = closeDipReading;
        this.openDipScaleReading = openDipScaleReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.closeDipReadDate = closeDipReadDate;
        this.densityRecorded = densityRecorded;
        this.closeDensity = closeDensity;
        this.closeDipReadTime = closeDipReadTime;
        this.stockCheckStatus = stockCheckStatus;
    }
/*
    fuelInfraMapId,fuelDealerStaffId,tankNo,
    openDipReading,closeDipReading,openDipScaleReading,
    closeDipScaleReading,openDipReadDate,closeDipReadDate,
    densityRecorded,closeDensity,fuelDealerId,
    dealerTankMap*/

    public DailyInventoryDetailsModel(String fuelInfraMapId, String fuelDealerStaffId,
                                      String tankNo, String openDipReading,
                                      String closeDipReading,
                                      String openDipScaleReading,
                                      String closeDipScaleReading,
                                      String openDipReadDate, String closeDipReadDate,
                                      String densityRecorded, String closeDensity,
                                      String fuelDealerId, String dealerTankMap,
                                      String openDipReadTime, String closeDipReadTime,
                                      String stockCheckStatus) {
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.tankNo = tankNo;
        this.openDipReading = openDipReading;
        this.closeDipReading = closeDipReading;
        this.openDipScaleReading = openDipScaleReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.openDipReadDate = openDipReadDate;
        this.closeDipReadDate = closeDipReadDate;
        this.densityRecorded = densityRecorded;
        this.closeDensity = closeDensity;
        this.fuelDealerId = fuelDealerId;
        this.dealerTankMap = dealerTankMap;
        this.openDipReadTime = openDipReadTime;
        this.closeDipReadTime = closeDipReadTime;
        this.stockCheckStatus = stockCheckStatus;

    }


  /*  fuelInfraMapId,fuelDealerStaffId,tankNo,
    openDipReading,closeDipReading,openDipScaleReading,
    closeDipScaleReading,openDipReadDate,closeDipReadDate,
    densityRecorded,closeDensity,fuelDealerId,
    dealerTankMap*/


    /*tankFuelInventoryId,openDipReading,closeDipReading,openDipScaleReading,
    closeDipScaleReading,closeDipReadDate,densityRecorded,closeDensity*/
    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }

    public String getFuelDealerStaffId() {
        return fuelDealerStaffId;
    }

    public void setFuelDealerStaffId(String fuelDealerStaffId) {
        this.fuelDealerStaffId = fuelDealerStaffId;
    }

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }

    public String getOpenDipReading() {
        return openDipReading;
    }

    public void setOpenDipReading(String openDipReading) {
        this.openDipReading = openDipReading;
    }

    public String getCloseDipReading() {
        return closeDipReading;
    }

    public void setCloseDipReading(String closeDipReading) {
        this.closeDipReading = closeDipReading;
    }

    public String getOpenDipScaleReading() {
        return openDipScaleReading;
    }

    public void setOpenDipScaleReading(String openDipScaleReading) {
        this.openDipScaleReading = openDipScaleReading;
    }

    public String getCloseDipScaleReading() {
        return closeDipScaleReading;
    }

    public void setCloseDipScaleReading(String closeDipScaleReading) {
        this.closeDipScaleReading = closeDipScaleReading;
    }

    public String getOpenDipReadDate() {
        return openDipReadDate;
    }

    public void setOpenDipReadDate(String openDipReadDate) {
        this.openDipReadDate = openDipReadDate;
    }

    public String getCloseDipReadDate() {
        return closeDipReadDate;
    }

    public void setCloseDipReadDate(String closeDipReadDate) {
        this.closeDipReadDate = closeDipReadDate;
    }

    public String getDensityRecorded() {
        return densityRecorded;
    }

    public void setDensityRecorded(String densityRecorded) {
        this.densityRecorded = densityRecorded;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTankFuelInventoryId() {
        return tankFuelInventoryId;
    }

    public void setTankFuelInventoryId(String tankFuelInventoryId) {
        this.tankFuelInventoryId = tankFuelInventoryId;
    }

}
