package com.veelsplusfueldealerapp.models;

public class InfraMappingDbModel {
    private String tankNo;
    private String duNo;
    private String nozzleNo;
    private String tankDuNzMap;
    private String productId;
    private String prodBrandName;
    private String prodName;
    private String prodCategory;
    private String prodCode;
    private String mappingStatus;
    private String fuelInfraMapId;
    private String fuelDealerId;


    public InfraMappingDbModel(String tankNo, String duNo, String nozzleNo, String tankDuNzMap, String productId, String prodBrandName, String prodName, String prodCategory, String prodCode, String mappingStatus, String fuelInfraMapId, String fuelDealerId) {
        this.tankNo = tankNo;
        this.duNo = duNo;
        this.nozzleNo = nozzleNo;
        this.tankDuNzMap = tankDuNzMap;
        this.productId = productId;
        this.prodBrandName = prodBrandName;
        this.prodName = prodName;
        this.prodCategory = prodCategory;
        this.prodCode = prodCode;
        this.mappingStatus = mappingStatus;
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelDealerId = fuelDealerId;
    }

    public String getTankDuNzMap() {
        return tankDuNzMap;
    }

    public void setTankDuNzMap(String tankDuNzMap) {
        this.tankDuNzMap = tankDuNzMap;
    }

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }

    public String getDuNo() {
        return duNo;
    }

    public void setDuNo(String duNo) {
        this.duNo = duNo;
    }

    public String getNozzleNo() {
        return nozzleNo;
    }

    public void setNozzleNo(String nozzleNo) {
        this.nozzleNo = nozzleNo;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProdBrandName() {
        return prodBrandName;
    }

    public void setProdBrandName(String prodBrandName) {
        this.prodBrandName = prodBrandName;
    }

    public String getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(String prodCategory) {
        this.prodCategory = prodCategory;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public String getMappingStatus() {
        return mappingStatus;
    }

    public void setMappingStatus(String mappingStatus) {
        this.mappingStatus = mappingStatus;
    }

    public String getFuelDealerId() {
        return fuelDealerId;
    }

    public void setFuelDealerId(String fuelDealerId) {
        this.fuelDealerId = fuelDealerId;
    }

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }
}
