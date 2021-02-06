package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class FuelPriceModel {
    @SerializedName("productSellingPrice")
    public String productSellingPrice;

    @SerializedName("sellingSetBy")
    public String sellingSetBy;

    @SerializedName("priceChangeMeterReading")
    public String priceChangeMeterReading;

    @SerializedName("productPriceDate")
    public String productPriceDate;

    @SerializedName("dealerTankMap")
    public String dealerTankMap;

    @SerializedName("productCategory")
    public String productCategory;

    @SerializedName("productName")
    public String productName;


    String productId;

    public FuelPriceModel(String productName, String productId) {
        this.productName = productName;
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductSellingPrice() {
        return productSellingPrice;
    }

    public void setProductSellingPrice(String productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public String getSellingSetBy() {
        return sellingSetBy;
    }

    public void setSellingSetBy(String sellingSetBy) {
        this.sellingSetBy = sellingSetBy;
    }

    public String getPriceChangeMeterReading() {
        return priceChangeMeterReading;
    }

    public void setPriceChangeMeterReading(String priceChangeMeterReading) {
        this.priceChangeMeterReading = priceChangeMeterReading;
    }

    public String getProductPriceDate() {
        return productPriceDate;
    }

    public void setProductPriceDate(String productPriceDate) {
        this.productPriceDate = productPriceDate;
    }

    public String getDealerTankMap() {
        return dealerTankMap;
    }

    public void setDealerTankMap(String dealerTankMap) {
        this.dealerTankMap = dealerTankMap;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public FuelPriceModel(String productSellingPrice, String sellingSetBy, String priceChangeMeterReading, String productPriceDate, String dealerTankMap) {
        this.productSellingPrice = productSellingPrice;
        this.sellingSetBy = sellingSetBy;
        this.priceChangeMeterReading = priceChangeMeterReading;
        this.productPriceDate = productPriceDate;
        this.dealerTankMap = dealerTankMap;
    }

    public FuelPriceModel(String productSellingPrice, String productPriceDate,
                          String productCategory, String productName) {
        this.productSellingPrice = productSellingPrice;
        this.productPriceDate = productPriceDate;
        this.productCategory = productCategory;
        this.productName = productName;
    }
}
