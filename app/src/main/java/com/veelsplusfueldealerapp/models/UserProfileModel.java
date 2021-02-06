package com.veelsplusfueldealerapp.models;

public class UserProfileModel {
    String username;
    String companyName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDealerAddress() {
        return dealerAddress;
    }

    public void setDealerAddress(String dealerAddress) {
        this.dealerAddress = dealerAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getDealerPhone() {
        return dealerPhone;
    }

    public void setDealerPhone(String dealerPhone) {
        this.dealerPhone = dealerPhone;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public UserProfileModel() {
    }

    public UserProfileModel(String username, String companyName, String dealerAddress, String userPhone, String dealerPhone, String userImagePath, String brandName) {
        this.username = username;
        this.companyName = companyName;
        this.dealerAddress = dealerAddress;
        this.userPhone = userPhone;
        this.dealerPhone = dealerPhone;
        this.userImagePath = userImagePath;
        this.brandName = brandName;
    }

    String dealerAddress;
    String userPhone;
    String dealerPhone;
    String userImagePath;
    String brandName;
}
