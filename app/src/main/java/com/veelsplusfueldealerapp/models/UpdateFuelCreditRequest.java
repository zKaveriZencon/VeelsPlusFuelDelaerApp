package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class UpdateFuelCreditRequest {
    @SerializedName("fuelCreditId")
    public String fuelCreditId;
    @SerializedName("transDateTime")
    public String transDateTime;
    @SerializedName("transactionTime")
    public String transactionTime;
    @SerializedName("creditAmount")
    public String creditAmount;
    @SerializedName("transactionStatus")
    public String transactionStatus;
    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;
    @SerializedName("actualCreditQuantity")
    public String actualCreditQuantity;
    @SerializedName("productRate")
    public String productRate;

    public UpdateFuelCreditRequest(String fuelCreditId, String transDateTime,
                                   String transactionTime, String creditAmount,
                                   String transactionStatus, String fuelDealerStaffId,
                                   String actualCreditQuantity, String productRate) {
        this.fuelCreditId = fuelCreditId;
        this.transDateTime = transDateTime;
        this.transactionTime = transactionTime;
        this.creditAmount = creditAmount;
        this.transactionStatus = transactionStatus;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.actualCreditQuantity = actualCreditQuantity;
        this.productRate = productRate;

    }
    /*fuelCreditId,transDateTime,transactionTime,creditAmount,transactionStatus,
    fuelDealerStaffId,actualCreditQuantity,productRate
*/
   /* Update parameters::fuelCreditId,transDateTime,transactionTime,
    creditAmount,transactionStatus,fuelDealerStaffId*/
}
