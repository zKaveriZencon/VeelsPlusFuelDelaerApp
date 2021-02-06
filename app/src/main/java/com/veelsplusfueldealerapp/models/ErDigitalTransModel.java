package com.veelsplusfueldealerapp.models;

public class ErDigitalTransModel {
    String date;
    String transactioId;
    String transactionAmt;
    String paymentType;

    public String getDate() {
        return date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getTransactioId() {
        return transactioId;
    }

    public void setTransactioId(String transactioId) {
        this.transactioId = transactioId;
    }

    public String getTransactionAmt() {
        return transactionAmt;
    }

    public ErDigitalTransModel() {
    }

    public void setTransactionAmt(String transactionAmt) {
        this.transactionAmt = transactionAmt;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public ErDigitalTransModel(String paymentType, String transactioId, String transactionAmt) {
        this.paymentType = paymentType;
        this.transactioId = transactioId;
        this.transactionAmt = transactionAmt;
    }
}
