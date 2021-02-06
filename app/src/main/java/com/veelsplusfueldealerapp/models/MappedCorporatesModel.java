package com.veelsplusfueldealerapp.models;

public class MappedCorporatesModel {
    String corporateName, creditLimit, totalBilledAmt, paidAmt, pendingAmt, finalPendingAmt;

    public String getFinalPendingAmt() {
        return finalPendingAmt;
    }

    public void setFinalPendingAmt(String finalPendingAmt) {
        this.finalPendingAmt = finalPendingAmt;
    }

    public MappedCorporatesModel(String corporateName, String creditLimit, String
            totalBilledAmt, String paidAmt, String pendingAmt, String finalPendingAmt) {
        this.corporateName = corporateName;
        this.creditLimit = creditLimit;
        this.totalBilledAmt = totalBilledAmt;
        this.paidAmt = paidAmt;
        this.pendingAmt = pendingAmt;
        this.finalPendingAmt = finalPendingAmt;


    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(String creditLimit) {
        this.creditLimit = creditLimit;
    }

    public String getTotalBilledAmt() {
        return totalBilledAmt;
    }

    public void setTotalBilledAmt(String totalBilledAmt) {
        this.totalBilledAmt = totalBilledAmt;
    }

    public String getPaidAmt() {
        return paidAmt;
    }

    public void setPaidAmt(String paidAmt) {
        this.paidAmt = paidAmt;
    }

    public String getPendingAmt() {
        return pendingAmt;
    }

    public void setPendingAmt(String pendingAmt) {
        this.pendingAmt = pendingAmt;
    }
}
