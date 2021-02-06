package com.veelsplusfueldealerapp.models;

public class LoginCredentialsModel {

    private String phone;

    private String password;

    public LoginCredentialsModel() {
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LoginCredentialsModel(String phone, String password, boolean isLoggedIn) {
        this.phone = phone;
        this.password = password;
        this.isLoggedIn = isLoggedIn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    private boolean isLoggedIn;
}
