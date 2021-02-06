package com.veelsplusfueldealerapp.models;

public class DailySalesInfoModel {
    String transacDate, accountTransacLogId, batchId;

    String date, product, unitSale, pumpNz,totalRecoveredAmount;

    public String getTotalRecoveredAmount() {
        return totalRecoveredAmount;
    }

    public void setTotalRecoveredAmount(String totalRecoveredAmount) {
        this.totalRecoveredAmount = totalRecoveredAmount;
    }

    public DailySalesInfoModel(String date, String product, String unitSale,
                               String pumpNz, String transacDate, String accountTransacLogId,
                               String batchId, String totalRecoveredAmount
    ) {
        this.date = date;
        this.product = product;
        this.unitSale = unitSale;
        this.pumpNz = pumpNz;
        this.transacDate = transacDate;
        this.accountTransacLogId = accountTransacLogId;
        this.batchId = batchId;
        this.totalRecoveredAmount = totalRecoveredAmount;

    }

    public DailySalesInfoModel(String transacDate, String accountTransacLogId, String batchId) {
        this.transacDate = transacDate;
        this.accountTransacLogId = accountTransacLogId;
        this.batchId = batchId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getUnitSale() {
        return unitSale;
    }

    public void setUnitSale(String unitSale) {
        this.unitSale = unitSale;
    }

    public String getPumpNz() {
        return pumpNz;
    }

    public void setPumpNz(String pumpNz) {
        this.pumpNz = pumpNz;
    }

    public String getTransacDate() {
        return transacDate;
    }

    public void setTransacDate(String transacDate) {
        this.transacDate = transacDate;
    }

    public String getAccountTransacLogId() {
        return accountTransacLogId;
    }

    public void setAccountTransacLogId(String accountTransacLogId) {
        this.accountTransacLogId = accountTransacLogId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

}
