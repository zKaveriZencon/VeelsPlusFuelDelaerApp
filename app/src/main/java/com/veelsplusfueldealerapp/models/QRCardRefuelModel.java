package com.veelsplusfueldealerapp.models;

public class QRCardRefuelModel {
    String driverName;
    String driverNumber;
    String vehicleNumber;

    public String getFuelProdId() {
        return fuelProdId;
    }

    public void setFuelProdId(String fuelProdId) {
        this.fuelProdId = fuelProdId;
    }

    String custName;
    String custNumber;
    String fuelType;
    String prodName;
    String fuelQuant;
    String price;
    String fuelProdId;

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getActualCreditQuantity() {
        return actualCreditQuantity;
    }

    public void setActualCreditQuantity(String actualCreditQuantity) {
        this.actualCreditQuantity = actualCreditQuantity;
    }

    String totalPrice;
    String creditAmount;
    String actualCreditQuantity;

    public QRCardRefuelModel(String driverName, String driverNumber, String vehicleNumber, String custName, String custNumber, String fuelType, String prodName, String fuelQuant, String price, String totalPrice) {
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.vehicleNumber = vehicleNumber;
        this.custName = custName;
        this.custNumber = custNumber;
        this.fuelType = fuelType;
        this.prodName = prodName;
        this.fuelQuant = fuelQuant;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public QRCardRefuelModel(String driverName, String driverNumber, String vehicleNumber,
                             String custName, String custNumber, String fuelType,
                             String prodName, String fuelQuant, String price,
                             String totalPrice, String creditAmount,
                             String actualCreditQuantity, String fuelProdId) {
        this.driverName = driverName;
        this.driverNumber = driverNumber;
        this.vehicleNumber = vehicleNumber;
        this.custName = custName;
        this.custNumber = custNumber;
        this.fuelType = fuelType;
        this.prodName = prodName;
        this.fuelQuant = fuelQuant;
        this.price = price;
        this.totalPrice = totalPrice;
        this.creditAmount = creditAmount;
        this.actualCreditQuantity = actualCreditQuantity;
        this.fuelProdId = fuelProdId;


    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(String driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        this.vehicleNumber = vehicleNumber;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustNumber() {
        return custNumber;
    }

    public void setCustNumber(String custNumber) {
        this.custNumber = custNumber;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getFuelQuant() {
        return fuelQuant;
    }

    public void setFuelQuant(String fuelQuant) {
        this.fuelQuant = fuelQuant;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

}
