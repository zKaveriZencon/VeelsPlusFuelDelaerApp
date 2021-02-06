package com.veelsplusfueldealerapp.models;

public class TankModel {
    private String tankNumber;

    public String getTankNumber() {
        return tankNumber;
    }

    public void setTankNumber(String tankNumber) {
        this.tankNumber = tankNumber;
    }

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }

    public TankModel(String tankNumber, String fuelInfraMapId) {
        this.tankNumber = tankNumber;
        this.fuelInfraMapId = fuelInfraMapId;
    }

    private String fuelInfraMapId;
}
