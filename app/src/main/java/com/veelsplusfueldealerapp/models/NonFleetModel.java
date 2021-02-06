package com.veelsplusfueldealerapp.models;

public class NonFleetModel {

    String personName;
    String personNumber;
    String date;
    String vehicleNo;
    String transactionStatus;
    String driverNo;
    String fuelCreditId;

    public NonFleetModel(String personName, String personNumber, String date, String vehicleNo, String transactionStatus, String driverNo, String fuelCreditId) {
        this.personName = personName;
        this.personNumber = personNumber;
        this.date = date;
        this.vehicleNo = vehicleNo;
        this.transactionStatus = transactionStatus;
        this.driverNo = driverNo;
        this.fuelCreditId = fuelCreditId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(String personNumber) {
        this.personNumber = personNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getDriverNo() {
        return driverNo;
    }

    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }

    public String getFuelCreditId() {
        return fuelCreditId;
    }

    public void setFuelCreditId(String fuelCreditId) {
        this.fuelCreditId = fuelCreditId;
    }


}
