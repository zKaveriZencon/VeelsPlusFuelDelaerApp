package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class UpdateFuelInventoryModel {
    @SerializedName("tankFuelInventoryId")
    public String tankFuelInventoryId;

    @SerializedName("closeDipReading")
    public String closeDipReading;

    @SerializedName("closeDipScaleReading")
    public String closeDipScaleReading;

    @SerializedName("closeDipReadDate")
    public String closeDipReadDate;

    @SerializedName("closeWaterDip")
    public String closeWaterDip;

    @SerializedName("closeWaterdipStock")
    public String closeWaterdipStock;

    @SerializedName("invoiceDensity")
    public String invoiceDensity;

    @SerializedName("invoiceQuantity")
    public String invoiceQuantity;

    @SerializedName("densityRecorded")
    public String densityRecorded;
    @SerializedName("closeDipReadTime")
    public String closeDipReadTime;

    @SerializedName("decanStatus")
    public String decanStatus;

    public UpdateFuelInventoryModel(String tankFuelInventoryId,
                                    String closeDipReading, String closeDipScaleReading,
                                    String closeDipReadDate, String closeWaterDip,
                                    String closeWaterdipStock, String invoiceDensity,
                                    String invoiceQuantity, String densityRecorded,
                                    String closeDipReadTime, String decanStatus) {
        this.tankFuelInventoryId = tankFuelInventoryId;
        this.closeDipReading = closeDipReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.closeDipReadDate = closeDipReadDate;
        this.closeWaterDip = closeWaterDip;
        this.closeWaterdipStock = closeWaterdipStock;
        this.invoiceDensity = invoiceDensity;
        this.invoiceQuantity = invoiceQuantity;
        this.densityRecorded = densityRecorded;
        this.closeDipReadTime = closeDipReadTime;
        this.decanStatus = decanStatus;
    }

   /* tankFuelInventoryId,closeDipReading,closeDipScaleReading,
    closeDipReadDate,closeWaterDip,closeWaterdipStock,
    invoiceDensity,invoiceQuantity,densityRecorded*/
}
