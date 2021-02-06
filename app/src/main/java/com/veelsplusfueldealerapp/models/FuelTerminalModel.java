package com.veelsplusfueldealerapp.models;

public class FuelTerminalModel {
    String fuelTerminalsId;
    String terminalName;
    String terminalType;
    String attachedBankName;
    public FuelTerminalModel(String fuelTerminalsId, String terminalName, String attachedBankName, String terminalType) {
        this.fuelTerminalsId = fuelTerminalsId;
        this.terminalName = terminalName;
        this.attachedBankName = attachedBankName;
        this.terminalType = terminalType;

    }

    public FuelTerminalModel() {
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getFuelTerminalsId() {
        return fuelTerminalsId;
    }

    public void setFuelTerminalsId(String fuelTerminalsId) {
        this.fuelTerminalsId = fuelTerminalsId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getAttachedBankName() {
        return attachedBankName;
    }

    public void setAttachedBankName(String attachedBankName) {
        this.attachedBankName = attachedBankName;
    }

}
