package com.veelsplusfueldealerapp.models;

import com.google.gson.annotations.SerializedName;

public class ContactUsModel {
    @SerializedName("ticketSource")
    public String ticketSource;
    @SerializedName("ticketDescription")
    public String ticketDescription;
    @SerializedName("ticketStatus")
    public String ticketStatus;

    public ContactUsModel() {
    }

    @SerializedName("ticketRaisedByPersonId")
    public String ticketRaisedByPersonId;
    @SerializedName("ticketCreatedDate")
    public String ticketCreatedDate;

    public ContactUsModel(String ticketSource, String ticketDescription, String ticketStatus, String ticketRaisedByPersonId, String ticketCreatedDate) {
        this.ticketSource = ticketSource;
        this.ticketDescription = ticketDescription;
        this.ticketStatus = ticketStatus;
        this.ticketRaisedByPersonId = ticketRaisedByPersonId;
        this.ticketCreatedDate = ticketCreatedDate;
    }

    public String getTicketSource() {
        return ticketSource;
    }

    public void setTicketSource(String ticketSource) {
        this.ticketSource = ticketSource;
    }

    public String getTicketDescription() {
        return ticketDescription;
    }

    public void setTicketDescription(String ticketDescription) {
        this.ticketDescription = ticketDescription;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketRaisedByPersonId() {
        return ticketRaisedByPersonId;
    }

    public void setTicketRaisedByPersonId(String ticketRaisedByPersonId) {
        this.ticketRaisedByPersonId = ticketRaisedByPersonId;
    }

    public String getTicketCreatedDate() {
        return ticketCreatedDate;
    }

    public void setTicketCreatedDate(String ticketCreatedDate) {
        this.ticketCreatedDate = ticketCreatedDate;
    }
}
