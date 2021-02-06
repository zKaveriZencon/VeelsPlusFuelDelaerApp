package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class UpdateDailyStockModel {
    @SerializedName("tankFuelInventoryId")
    public String tankFuelInventoryId;

    public String getTankFuelInventoryId() {
        return tankFuelInventoryId;
    }

    public void setTankFuelInventoryId(String tankFuelInventoryId) {
        this.tankFuelInventoryId = tankFuelInventoryId;
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

    public UpdateDailyStockModel(String tankFuelInventoryId, String openDipReading, String closeDipReading, String openDipScaleReading, String closeDipScaleReading, String closeDipReadDate, String densityRecorded) {
        this.tankFuelInventoryId = tankFuelInventoryId;
        this.openDipReading = openDipReading;
        this.closeDipReading = closeDipReading;
        this.openDipScaleReading = openDipScaleReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.closeDipReadDate = closeDipReadDate;
        this.densityRecorded = densityRecorded;
    }

    @SerializedName("openDipReading")
    public String openDipReading;

    @SerializedName("closeDipReading")
    public String closeDipReading;

    @SerializedName("openDipScaleReading")
    public String openDipScaleReading;

    @SerializedName("closeDipScaleReading")
    public String closeDipScaleReading;

    @SerializedName("closeDipReadDate")
    public String closeDipReadDate;

    @SerializedName("densityRecorded")
    public String densityRecorded;

}
