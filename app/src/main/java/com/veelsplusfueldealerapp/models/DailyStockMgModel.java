package com.veelsplusfueldealerapp.models;

public class DailyStockMgModel {
    String openDipScaleReading;
    String closeDipScaleReading;
    String tankNo;
    String openDipReadDate;
    String stockCheckStatus;
    String closeDensity;
    String fuelTankRefuelId;
    String fuelInfraMapId;

    public DailyStockMgModel(String openDipScaleReading, String closeDipScaleReading,
                             String tankNo, String openDipReadDate, String fuelTankRefuelId,
                             String fuelInfraMapId, String stockCheckStatus, String closeDensity) {
        this.openDipScaleReading = openDipScaleReading;
        this.closeDipScaleReading = closeDipScaleReading;
        this.tankNo = tankNo;
        this.openDipReadDate = openDipReadDate;
        this.fuelTankRefuelId = fuelTankRefuelId;
        this.fuelInfraMapId = fuelInfraMapId;
        this.stockCheckStatus = stockCheckStatus;
        this.closeDensity = closeDensity;
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

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }

    public String getOpenDipReadDate() {
        return openDipReadDate;
    }

    public void setOpenDipReadDate(String openDipReadDate) {
        this.openDipReadDate = openDipReadDate;
    }

    public String getFuelTankRefuelId() {
        return fuelTankRefuelId;
    }

    public void setFuelTankRefuelId(String fuelTankRefuelId) {
        this.fuelTankRefuelId = fuelTankRefuelId;
    }

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }

    public String getStockCheckStatus() {
        return stockCheckStatus;
    }

    public void setStockCheckStatus(String stockCheckStatus) {
        this.stockCheckStatus = stockCheckStatus;
    }


}
