package com.veelsplusfueldealerapp.models;

public class ShiftWiseCreditModel {
    String fuelInfraMapId, fuelStaffPerformId, productName, duNozzle,duNozzleProduct;
    String fuelType, fullProductName,fuelPriceByDate,selectedProductId;

    public String getFuelPriceByDate() {
        return fuelPriceByDate;
    }

    public String getSelectedProductId() {
        return selectedProductId;
    }

    public void setSelectedProductId(String selectedProductId) {
        this.selectedProductId = selectedProductId;
    }

    public void setFuelPriceByDate(String fuelPriceByDate) {
        this.fuelPriceByDate = fuelPriceByDate;
    }

    public ShiftWiseCreditModel(String fuelType, String productName, String fullProductName) {
        this.fuelType = fuelType;
        this.productName = productName;
        this.fullProductName = fullProductName;
    }

    public String getFuelType() {
        return fuelType;
    }



    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFullProductName() {
        return fullProductName;
    }

    public void setFullProductName(String fullProductName) {
        this.fullProductName = fullProductName;
    }

    public ShiftWiseCreditModel(String fuelInfraMapId, String fuelStaffPerformId) {
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

    public String getDuNozzle() {
        return duNozzle;
    }

    public void setDuNozzle(String duNozzle) {
        this.duNozzle = duNozzle;
    }

    public String getDuNozzleProduct() {
        return duNozzleProduct;
    }

    public void setDuNozzleProduct(String duNozzleProduct) {
        this.duNozzleProduct = duNozzleProduct;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }

    public ShiftWiseCreditModel(String fuelInfraMapId, String fuelStaffPerformId,
                                String productName,String duNozzle, String duNozzleProduct,
                                String fuelPriceByDate, String selectedProductId) {
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelStaffPerformId = fuelStaffPerformId;
        this.productName = productName;
        this.duNozzle = duNozzle;
        this.duNozzleProduct = duNozzleProduct;
        this.fuelPriceByDate = fuelPriceByDate;
        this.selectedProductId = selectedProductId;



    }

    public void setFuelStaffPerformId(String fuelStaffPerformId) {
        this.fuelStaffPerformId = fuelStaffPerformId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public String getFuelStaffPerformId() {
        return fuelStaffPerformId;
    }
}
