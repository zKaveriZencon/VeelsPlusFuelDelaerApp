package com.veelsplusfueldealerapp.models;

public class InventoryDetailsListModel {
    private String openMetDate;
    private String quantity;
    private String tankNo;
    private String fuelTankRefuelId;
    private String decanStatus;
    private String productCategory;
    private String refuelId;





    public String getFuelTankRefuelId() {
        return fuelTankRefuelId;
    }

    public void setFuelTankRefuelId(String fuelTankRefuelId) {
        this.fuelTankRefuelId = fuelTankRefuelId;
    }

    public String getDecanStatus() {
        return decanStatus;
    }

    public void setDecanStatus(String decanStatus) {
        this.decanStatus = decanStatus;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getRefuelId() {
        return refuelId;
    }

    public void setRefuelId(String refuelId) {
        this.refuelId = refuelId;
    }

    public InventoryDetailsListModel(String openMetDate, String quantity, String tankNo,
                                     String fuelTankRefuelId, String decanStatus,
                                     String productCategory, String refuelId) {
        this.openMetDate = openMetDate;
        this.quantity = quantity;
        this.tankNo = tankNo;
        this.fuelTankRefuelId = fuelTankRefuelId;
        this.decanStatus= decanStatus;
        this.productCategory= productCategory;
        this.refuelId= refuelId;

    }

    public String getOpenMetDate() {
        return openMetDate;
    }

    public void setOpenMetDate(String openMetDate) {
        this.openMetDate = openMetDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTankNo() {
        return tankNo;
    }

    public void setTankNo(String tankNo) {
        this.tankNo = tankNo;
    }
}
