package com.veelsplusfueldealerapp.models;

public class PaymentModelLocal {
    String batchId, paymentType, amount, transId;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public PaymentModelLocal() {
    }

    public PaymentModelLocal(String batchId, String paymentType,
                             String amount, String transId) {
        this.batchId = batchId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.transId = transId;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
