package com.veelsplusfueldealerapp.models;

public class FleetListModel {
    String customerName;
    String date;
    String vehicleNo;
    String driverNo;
    String fuelCreditId;
    String personName;
    String transactionStatus;
    String personNo;
    String creditAmount;
    String productName;
    String actualCreditQuantity;

    public FleetListModel() {
    }

    public String getPersonNo() {
        return personNo;
    }

    public void setPersonNo(String personNo) {
        this.personNo = personNo;
    }

    public FleetListModel(String customerName, String date, String vehicleNo,
                          String driverNo, String fuelCreditId, String transactionStatus) {
        this.customerName = customerName;
        this.date = date;
        this.vehicleNo = vehicleNo;
        this.driverNo = driverNo;
        this.fuelCreditId = fuelCreditId;
        this.transactionStatus = transactionStatus;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getActualCreditQuantity() {
        return actualCreditQuantity;
    }

    public void setActualCreditQuantity(String actualCreditQuantity) {
        this.actualCreditQuantity = actualCreditQuantity;
    }

    public FleetListModel(String customerName, String date, String vehicleNo,
                          String driverNo, String fuelCreditId, String transactionStatus,
                          String creditAmount, String actualCreditQuantity, String productName) {
        this.customerName = customerName;
        this.date = date;
        this.vehicleNo = vehicleNo;
        this.driverNo = driverNo;
        this.fuelCreditId = fuelCreditId;
        this.transactionStatus = transactionStatus;
        this.creditAmount = creditAmount;
        this.actualCreditQuantity = actualCreditQuantity;
        this.productName = productName;

    }

   /* public FleetListModel(String customerName, String date, String vehicleNo,
                          String driverNo, String fuelCreditId,
                          String transactionStatus, String personNo) {
        this.customerName = customerName;
        this.date = date;
        this.vehicleNo = vehicleNo;
        this.driverNo = driverNo;
        this.fuelCreditId = fuelCreditId;
        this.transactionStatus = transactionStatus;
        this.personNo = personNo;

    }
*/
    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getFuelCreditId() {
        return fuelCreditId;
    }

    public void setFuelCreditId(String fuelCreditId) {
        this.fuelCreditId = fuelCreditId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getDriverNo() {
        return driverNo;
    }

    public void setDriverNo(String driverNo) {
        this.driverNo = driverNo;
    }
}
