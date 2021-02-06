package com.veelsplusfueldealerapp.models;

public class AddNewPayDetailsModel {

    String batchId;
    String shiftWiseCreditSumForApp;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getCorporateID() {
        return corporateID;
    }

    public void setCorporateID(String corporateID) {
        this.corporateID = corporateID;
    }

    public String getTotalBatchAmtExpected() {
        return totalBatchAmtExpected;
    }

    public void setTotalBatchAmtExpected(String totalBatchAmtExpected) {
        this.totalBatchAmtExpected = totalBatchAmtExpected;
    }

    public String getTotalBatchAmtRecovered() {
        return totalBatchAmtRecovered;
    }

    public void setTotalBatchAmtRecovered(String totalBatchAmtRecovered) {
        this.totalBatchAmtRecovered = totalBatchAmtRecovered;
    }

    public String getShiftWiseCreditSumForApp() {
        return shiftWiseCreditSumForApp;
    }

    public void setShiftWiseCreditSumForApp(String shiftWiseCreditSumForApp) {
        this.shiftWiseCreditSumForApp = shiftWiseCreditSumForApp;
    }

    public AddNewPayDetailsModel(String batchId, String corporateID,
                                 String totalBatchAmtExpected, String totalBatchAmtRecovered,
                                 String shiftWiseCreditSumForApp) {
        this.batchId = batchId;
        this.corporateID = corporateID;
        this.totalBatchAmtExpected = totalBatchAmtExpected;
        this.totalBatchAmtRecovered = totalBatchAmtRecovered;
        this.shiftWiseCreditSumForApp = shiftWiseCreditSumForApp;


    }

    String corporateID;
    String totalBatchAmtExpected;
    String totalBatchAmtRecovered;
}
