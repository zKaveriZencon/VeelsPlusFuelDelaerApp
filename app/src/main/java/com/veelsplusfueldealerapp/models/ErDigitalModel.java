package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class ErDigitalModel {
    @SerializedName("transacDate")
    public String transacDate;

    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;
    @SerializedName("grandTotalAmount")
    public String grandTotalAmount;
    @SerializedName("activityId")
    public String activityId;
    @SerializedName("batchId")
    public String batchId;
    @SerializedName("accountId")
    public String accountId;
    @SerializedName("transacId")
    public String transacId;
    /*   transacDate, fuelDealerStaffId, grandTotalAmount, activityId, batchId, accountId,
       transacId, paytmTotalAmount, cardTotalAmount, creditTotal,terminalId,
       totalBatchAmtExpted, totalBatchAmtRecovered*/
    @SerializedName("paytmTotalAmount")
    public String paytmTotalAmount;
    @SerializedName("cardTotalAmount")
    public String cardTotalAmount;
    @SerializedName("creditTotal")
    public String creditTotal;
    @SerializedName("terminalId")
    public String terminalId;
    @SerializedName("totalBatchAmtExpted")
    public String totalBatchAmtExpted;
    @SerializedName("totalBatchAmtRecovered")
    public String totalBatchAmtRecovered;

    @SerializedName("corporateId")
    public String corporateId;

    @SerializedName("recoveredAmtByApp")
    public String recoveredAmtByApp;

    public ErDigitalModel(String transacDate, String fuelDealerStaffId,
                          String grandTotalAmount, String activityId, String batchId,
                          String accountId, String transacId, String paytmTotalAmount,
                          String cardTotalAmount, String creditTotal, String terminalId,
                          String totalBatchAmtExpted,
                          String totalBatchAmtRecovered, String corporateId, String recoveredAmtByApp) {
        this.transacDate = transacDate;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.grandTotalAmount = grandTotalAmount;
        this.activityId = activityId;
        this.batchId = batchId;
        this.accountId = accountId;
        this.transacId = transacId;
        this.paytmTotalAmount = paytmTotalAmount;
        this.cardTotalAmount = cardTotalAmount;
        this.creditTotal = creditTotal;
        this.terminalId = terminalId;
        this.totalBatchAmtExpted = totalBatchAmtExpted;
        this.totalBatchAmtRecovered = totalBatchAmtRecovered;
        this.corporateId = corporateId;
        this.recoveredAmtByApp = recoveredAmtByApp;

    }

    public String getTransacDate() {
        return transacDate;
    }

    public void setTransacDate(String transacDate) {
        this.transacDate = transacDate;
    }

    public String getFuelDealerStaffId() {
        return fuelDealerStaffId;
    }

    public void setFuelDealerStaffId(String fuelDealerStaffId) {
        this.fuelDealerStaffId = fuelDealerStaffId;
    }

    public String getGrandTotalAmount() {
        return grandTotalAmount;
    }

    public void setGrandTotalAmount(String grandTotalAmount) {
        this.grandTotalAmount = grandTotalAmount;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTransacId() {
        return transacId;
    }

    public void setTransacId(String transacId) {
        this.transacId = transacId;
    }

    public String getPaytmTotalAmount() {
        return paytmTotalAmount;
    }

    public void setPaytmTotalAmount(String paytmTotalAmount) {
        this.paytmTotalAmount = paytmTotalAmount;
    }

    public String getCardTotalAmount() {
        return cardTotalAmount;
    }

    public void setCardTotalAmount(String cardTotalAmount) {
        this.cardTotalAmount = cardTotalAmount;
    }

    public String getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(String creditTotal) {
        this.creditTotal = creditTotal;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


    /*transacDate, fuelDealerStaffId, grandTotalAmount, activityId,
    batchId, accountId,
    transacId, paytmTotalAmount, cardTotalAmount, creditTotal,terminalId*/

}
