package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class AddTankFuelInventoryModel {

    @SerializedName("fuelInfraMapId")
    public String fuelInfraMapId;
    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;
    @SerializedName("tankNo")
    public String tankNo;
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
    @SerializedName("openWaterDip")
    public String openWaterDip;
    @SerializedName("closeWaterDip")
    public String closeWaterDip;
    @SerializedName("openWaterdipStock")
    public String openWaterdipStock;
    @SerializedName("closeWaterdipStock")
    public String closeWaterdipStock;
    @SerializedName("vehicleNo")
    public String vehicleNo;
    @SerializedName("driverId")
    public String driverId;
    @SerializedName("boughtQuantity")
    public String boughtQuantity;
    @SerializedName("invoiceDensity")
    public String invoiceDensity;
    @SerializedName("invoiceQuantity")
    public String invoiceQuantity;
    @SerializedName("densityRecorded")
    public String densityRecorded;
    @SerializedName("buyingPrice")
    public String buyingPrice;
    @SerializedName("fuelDealerId")
    public String fuelDealerId;
    @SerializedName("dealerTankMap")
    public String dealerTankMap;
    @SerializedName("openDipReadTime")
    public String openDipReadTime;
    @SerializedName("closeDipReadTime")
    public String closeDipReadTime;

    @SerializedName("decanStatus")
    public String decanStatus;

    @SerializedName("invoiceDate")
    public String invoiceDate;

    @SerializedName("invoiceNumber")
    public String invoiceNumber;


    public AddTankFuelInventoryModel(String fuelInfraMapId, String fuelDealerStaffId,
                                     String tankNo, String openDipReading, String closeDipReading,
                                     String openDipScaleReading, String closeDipScaleReading,
                                     String openDipReadDate, String closeDipReadDate,
                                     String openWaterDip, String closeWaterDip,
                                     String openWaterdipStock, String closeWaterdipStock,
                                     String vehicleNo, String driverId, String boughtQuantity,
                                     String invoiceDensity, String invoiceQuantity,
                                     String densityRecorded, String buyingPrice,
                                     String fuelDealerId, String dealerTankMap,
                                     String openDipReadTime, String closeDipReadTime,
                                     String decanStatus, String invoiceDate, String invoiceNumber) {
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.tankNo = tankNo;
        this.openDipReading = openDipReading;
        this.closeDipReading = closeDipReading;
        this.openDipScaleReading = openDipScaleReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.openDipReadDate = openDipReadDate;
        this.closeDipReadDate = closeDipReadDate;
        this.openWaterDip = openWaterDip;
        this.closeWaterDip = closeWaterDip;
        this.openWaterdipStock = openWaterdipStock;
        this.closeWaterdipStock = closeWaterdipStock;
        this.vehicleNo = vehicleNo;
        this.driverId = driverId;
        this.boughtQuantity = boughtQuantity;
        this.invoiceDensity = invoiceDensity;
        this.invoiceQuantity = invoiceQuantity;
        this.densityRecorded = densityRecorded;
        this.buyingPrice = buyingPrice;
        this.fuelDealerId = fuelDealerId;
        this.dealerTankMap = dealerTankMap;
        this.openDipReadTime = openDipReadTime;
        this.closeDipReadTime = closeDipReadTime;
        this.decanStatus = decanStatus;
        this.invoiceDate = invoiceDate;
        this.invoiceNumber = invoiceNumber;
    }

   /* fuelInfraMapId,fuelDealerStaffId,tankNo,
    openDipReading,closeDipReading,openDipScaleReading,
    closeDipScaleReading,openDipReadDate,closeDipReadDate,
    openWaterDip,closeWaterDip,openWaterdipStock,
    closeWaterdipStock,vehicleNo,driverId,
    boughtQuantity,invoiceDensity,invoiceQuantity,
    densityRecorded,buyingPrice,fuelDealerId,
    dealerTankMap*/

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
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

    public String getOpenWaterDip() {
        return openWaterDip;
    }

    public void setOpenWaterDip(String openWaterDip) {
        this.openWaterDip = openWaterDip;
    }

    public String getCloseWaterDip() {
        return closeWaterDip;
    }

    public void setCloseWaterDip(String closeWaterDip) {
        this.closeWaterDip = closeWaterDip;
    }

    public String getOpenWaterdipStock() {
        return openWaterdipStock;
    }

    public void setOpenWaterdipStock(String openWaterdipStock) {
        this.openWaterdipStock = openWaterdipStock;
    }

    public String getCloseWaterdipStock() {
        return closeWaterdipStock;
    }

    public void setCloseWaterdipStock(String closeWaterdipStock) {
        this.closeWaterdipStock = closeWaterdipStock;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getBoughtQuantity() {
        return boughtQuantity;
    }

    public void setBoughtQuantity(String boughtQuantity) {
        this.boughtQuantity = boughtQuantity;
    }

    public String getInvoiceDensity() {
        return invoiceDensity;
    }

    public void setInvoiceDensity(String invoiceDensity) {
        this.invoiceDensity = invoiceDensity;
    }

    public String getInvoiceQuantity() {
        return invoiceQuantity;
    }

    public void setInvoiceQuantity(String invoiceQuantity) {
        this.invoiceQuantity = invoiceQuantity;
    }

    public String getDensityRecorded() {
        return densityRecorded;
    }

    public void setDensityRecorded(String densityRecorded) {
        this.densityRecorded = densityRecorded;
    }

    public String getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(String buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

}
