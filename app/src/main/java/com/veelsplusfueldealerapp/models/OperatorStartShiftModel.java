package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class OperatorStartShiftModel {
    @SerializedName("fuelInfraMapId")
    public String fuelInfraMapId;

    public String getFuelInfraMapId() {
        return fuelInfraMapId;
    }

    public void setFuelInfraMapId(String fuelInfraMapId) {
        this.fuelInfraMapId = fuelInfraMapId;
    }

    public String getFuelDealerStaffId() {
        return fuelDealerStaffId;
    }

    public void setFuelDealerStaffId(String fuelDealerStaffId) {
        this.fuelDealerStaffId = fuelDealerStaffId;
    }

    public String getOpenMetReading() {
        return openMetReading;
    }

    public void setOpenMetReading(String openMetReading) {
        this.openMetReading = openMetReading;
    }

    public String getOpenMetDateTime() {
        return openMetDateTime;
    }

    public void setOpenMetDateTime(String openMetDateTime) {
        this.openMetDateTime = openMetDateTime;
    }


    @SerializedName("fuelDealerStaffId")
    public String fuelDealerStaffId;

    public OperatorStartShiftModel(String fuelInfraMapId, String fuelDealerStaffId,
                                   String openMetReading, String openMetDateTime,
                                   String fuelPriceByDate, String dealerTankMapId,
                                   String fuelDealerId, String openTime) {
        this.fuelInfraMapId = fuelInfraMapId;
        this.fuelDealerStaffId = fuelDealerStaffId;
        this.openMetReading = openMetReading;
        this.openMetDateTime = openMetDateTime;
        this.fuelPriceByDate = fuelPriceByDate;
        this.dealerTankMapId = dealerTankMapId;
        this.fuelDealerId = fuelDealerId;
        this.openTime = openTime;

    }

    @SerializedName("openMetReading")
    public String openMetReading;

    @SerializedName("openMetDateTime")
    public String openMetDateTime;
    
    @SerializedName("fuelPriceByDate")
    public String fuelPriceByDate;

    @SerializedName("dealerTankMapId")
    public String dealerTankMapId;


    @SerializedName("fuelDealerId")
    public String fuelDealerId;


    @SerializedName("openTime")
    public String openTime;

   /* fuelInfraMapId, fuelDealerStaffId, openMetReading,
    openMetDateTime, fuelPriceByDate,
    dealerTankMapId, fuelDealerId, openTime*/
}
