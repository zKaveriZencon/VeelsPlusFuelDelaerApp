package com.veelsplusfueldealerapp.models;

public class SalesInfoCardModel {
    String product, meterSale, totalAmount;

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getMeterSale() {
        return meterSale;
    }

    public void setMeterSale(String meterSale) {
        this.meterSale = meterSale;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SalesInfoCardModel(String product, String meterSale, String totalAmount) {
        this.product = product;
        this.meterSale = meterSale;
        this.totalAmount = totalAmount;
    }
}
