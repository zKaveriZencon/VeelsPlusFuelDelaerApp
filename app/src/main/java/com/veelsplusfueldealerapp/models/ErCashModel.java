package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class ErCashModel {
    @SerializedName("transacDate")
    public String transacDate;

    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;

    public String getLumpSumCash() {
        return lumpSumCash;
    }

    public void setLumpSumCash(String lumpSumCash) {
        this.lumpSumCash = lumpSumCash;
    }

    @SerializedName("cashTotal")
    public String cashTotal;
    @SerializedName("grandTotalAmount")
    public String grandTotalAmount;
    @SerializedName("twoThousands")
    public String twoThousands;
    @SerializedName("oneThousand")
    public String oneThousand;
    @SerializedName("fiveHundread")
    public String fiveHundread;
    @SerializedName("twoHundread")
    public String twoHundread;
    @SerializedName("hundread")
    public String hundread;
    @SerializedName("fifty")
    public String fifty;
    @SerializedName("twenty")
    public String twenty;
    @SerializedName("ten")
    public String ten;
    @SerializedName("five")
    public String five;
    @SerializedName("two")
    public String two;
    @SerializedName("one")
    public String one;
    @SerializedName("activityId")
    public String activityId;
    @SerializedName("batchId")
    public String batchId;
    @SerializedName("totalBatchAmtExpted")
    public String totalBatchAmtExpted;
    @SerializedName("totalBatchAmtRecovered")
    public String totalBatchAmtRecovered;
    @SerializedName("corporateId")
    public String corporateId;
    @SerializedName("lumpSumCash")
    public String lumpSumCash;

    @SerializedName("recoveredAmtByApp")
    public String recoveredAmtByApp;





    public ErCashModel(String transacDate, String fuelDealerStaffId, String cashTotal,
                       String grandTotalAmount, String twoThousands, String oneThousand,
                       String fiveHundread, String twoHundread, String hundread,
                       String fifty, String twenty, String ten, String five, String two,
                       String one, String activityId, String batchId,
                       String totalBatchAmtExpted,
                       String totalBatchAmtRecovered, String corporateId, String lumpSumCash, String recoveredAmtByApp) {
        this.transacDate = transacDate;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.cashTotal = cashTotal;
        this.grandTotalAmount = grandTotalAmount;
        this.twoThousands = twoThousands;
        this.oneThousand = oneThousand;
        this.fiveHundread = fiveHundread;
        this.twoHundread = twoHundread;
        this.hundread = hundread;
        this.fifty = fifty;
        this.twenty = twenty;
        this.ten = ten;
        this.five = five;
        this.two = two;
        this.one = one;
        this.activityId = activityId;
        this.batchId = batchId;
        this.totalBatchAmtExpted = totalBatchAmtExpted;
        this.totalBatchAmtRecovered = totalBatchAmtRecovered;
        this.corporateId = corporateId;
        this.lumpSumCash = lumpSumCash;
        this.recoveredAmtByApp = recoveredAmtByApp;

    }

    public ErCashModel(String transacDate, String fuelDealerStaffId, String cashTotal,
                       String grandTotalAmount, String twoThousands, String oneThousand,
                       String fiveHundread, String twoHundread, String hundread,
                       String fifty, String twenty, String ten, String five, String two,
                       String one, String activityId, String batchId,
                       String totalBatchAmtExpted,
                       String totalBatchAmtRecovered,String recoveredAmtByApp) {
        this.transacDate = transacDate;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.cashTotal = cashTotal;
        this.grandTotalAmount = grandTotalAmount;
        this.twoThousands = twoThousands;
        this.oneThousand = oneThousand;
        this.fiveHundread = fiveHundread;
        this.twoHundread = twoHundread;
        this.hundread = hundread;
        this.fifty = fifty;
        this.twenty = twenty;
        this.ten = ten;
        this.five = five;
        this.two = two;
        this.one = one;
        this.activityId = activityId;
        this.batchId = batchId;
        this.totalBatchAmtExpted = totalBatchAmtExpted;
        this.totalBatchAmtRecovered = totalBatchAmtRecovered;
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

    public String getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(String cashTotal) {
        this.cashTotal = cashTotal;
    }

    public String getGrandTotalAmount() {
        return grandTotalAmount;
    }

    public void setGrandTotalAmount(String grandTotalAmount) {
        this.grandTotalAmount = grandTotalAmount;
    }

    public String getTwoThousands() {
        return twoThousands;
    }

    public void setTwoThousands(String twoThousands) {
        this.twoThousands = twoThousands;
    }

    public String getOneThousand() {
        return oneThousand;
    }

    public void setOneThousand(String oneThousand) {
        this.oneThousand = oneThousand;
    }

    public String getFiveHundread() {
        return fiveHundread;
    }

    public void setFiveHundread(String fiveHundread) {
        this.fiveHundread = fiveHundread;
    }

    public String getTwoHundread() {
        return twoHundread;
    }

    public void setTwoHundread(String twoHundread) {
        this.twoHundread = twoHundread;
    }

    public String getHundread() {
        return hundread;
    }

    public void setHundread(String hundread) {
        this.hundread = hundread;
    }

    public String getFifty() {
        return fifty;
    }

    public void setFifty(String fifty) {
        this.fifty = fifty;
    }

    public String getTwenty() {
        return twenty;
    }

    public void setTwenty(String twenty) {
        this.twenty = twenty;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getFive() {
        return five;
    }

    public void setFive(String five) {
        this.five = five;
    }

    public String getTwo() {
        return two;
    }

    public void setTwo(String two) {
        this.two = two;
    }

    public String getOne() {
        return one;
    }

    public void setOne(String one) {
        this.one = one;
    }

    public String getRecoveredAmtByApp() {
        return recoveredAmtByApp;
    }

    public String getTotalBatchAmtExpted() {
        return totalBatchAmtExpted;
    }

    public void setTotalBatchAmtExpted(String totalBatchAmtExpted) {
        this.totalBatchAmtExpted = totalBatchAmtExpted;
    }

    public void setRecoveredAmtByApp(String recoveredAmtByApp) {
        this.recoveredAmtByApp = recoveredAmtByApp;
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
   /* transacDate,fuelDealerStaffId, cashTotal, grandTotalAmount,
    twoThousands, oneThousand, fiveHundread, twoHundread, hundread, fifty,
    twenty, ten, five,
    two, one, activityId, batchId, totalBatchAmtExpted, totalBatchAmtRecovered*/
}
