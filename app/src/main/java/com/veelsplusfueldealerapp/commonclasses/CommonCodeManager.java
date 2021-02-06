package com.veelsplusfueldealerapp.commonclasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.veelsplusfueldealerapp.models.AddNewPayDetailsModel;
import com.veelsplusfueldealerapp.models.LoginCredentialsModel;
import com.veelsplusfueldealerapp.models.UserProfileModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CommonCodeManager {
    private static final String TAG = "CommonCodeManager";
    Context context;

    public CommonCodeManager() {
    }

    public CommonCodeManager(Context context) {
        this.context = context;
    }

    public void saveLoginCredentials(Context context, LoginCredentialsModel loginCredentialsModel) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.IS_LOGGED_IN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLoggedIn", loginCredentialsModel.isLoggedIn());
        editor.putString("phone", loginCredentialsModel.getPhone());
        editor.putString("password", loginCredentialsModel.getPassword());

        editor.apply();
    }

    public LoginCredentialsModel getLoginCredentials(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.IS_LOGGED_IN, MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        String phone = prefs.getString("phone", null);
        String password = prefs.getString("password", null);

        LoginCredentialsModel loginCredentialsModel = new LoginCredentialsModel(phone, password, isLoggedIn);
        return loginCredentialsModel;
    }

    public void saveStartShiftDetails(Context context, String startreading) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.START_SHIFT_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("startreading", startreading);
        Log.d(TAG, "saveStartShiftDetails: startreading :" + startreading);
        editor.apply();
    }

    public String getStartShiftDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.START_SHIFT_PREF, MODE_PRIVATE);
        String startreading = prefs.getString("startreading", "");

        return startreading;
    }

    public void saveInfraMapIdForInventory(Context context, String[] ids) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.INFRA_MAP_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelInfraMapId", ids[0]);
        editor.putString("fuelDealerStaffId", ids[1]);

        editor.apply();
    }

    public String[] getInfraMapIdForInventory(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.INFRA_MAP_ID, MODE_PRIVATE);
        String fuelInfraMapId = prefs.getString("fuelInfraMapId", "");
        String fuelDealerStaffId = prefs.getString("fuelDealerStaffId", "");
        String[] details = {fuelInfraMapId, fuelDealerStaffId};
        return details;
    }

    public void saveInventoryDetails(Context context, String[] ids) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.INVENTORY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelInfraMapId", ids[0]);
        editor.putString("fuelDealerStaffId", ids[1]);

        editor.apply();
    }

    public String[] getInventoryDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.INVENTORY, MODE_PRIVATE);
        String fuelInfraMapId = prefs.getString("fuelInfraMapId", "");
        String fuelDealerStaffId = prefs.getString("fuelDealerStaffId", "");
        String[] details = {fuelInfraMapId, fuelDealerStaffId};
        return details;
    }

    public void saveDealerDetailsOnLogin(Context context, String[] ids) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DEALER_DETAILS, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelDealerStaffId", ids[0]);
        editor.putString("fuelDealerId", ids[1]);
        editor.putString("fuelVeelsId", ids[2]);
        editor.apply();
    }

    public String[] getDealerDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DEALER_DETAILS, MODE_PRIVATE);
        String fuelDealerStaffId = prefs.getString("fuelDealerStaffId", "");
        String fuelDealerId = prefs.getString("fuelDealerId", "");
        String fuelVeelsId = prefs.getString("fuelVeelsId", "");

        String[] details = {fuelDealerStaffId, fuelDealerId, fuelVeelsId};
        return details;
    }

    public void saveUserLoginDetails(Context context, String[] details) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.LOGIN_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", details[0]);
        editor.putString("password", details[1]);
        editor.putString("authtoken", details[2]);
        Log.d(TAG, "saveUserLoginDetails: username : " + details[0]);
        Log.d(TAG, "saveUserLoginDetails: password : " + details[1]);
        Log.d(TAG, "saveUserLoginDetails: authtoken : " + details[2]);

        editor.apply();
    }

    public String[] getUserLoginDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.LOGIN_PREF, MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");
        String authtoken = prefs.getString("authtoken", "");

        String[] details = {username, password, authtoken};
        return details;
    }

    public void saveEssentialsForDealer(Context context, String[] details) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ESSENTIAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userId", details[0]);
        editor.putString("personId", details[1]);
        editor.putString("firstName", details[2]);
        editor.putString("lastName", details[3]);
        editor.putString("designation", details[4]);
        editor.apply();
    }

    public String[] getEssentialsForDealer(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.ESSENTIAL, MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        String personId = prefs.getString("personId", "");
        String firstName = prefs.getString("firstName", "");
        String lastName = prefs.getString("lastName", "");
        String designation = prefs.getString("designation", "");

        String[] details = {userId, personId, firstName, lastName, designation};
        return details;
    }

    public void saveInfraDetailsForOPShift(Context context, List<String> details) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.INFRA_OP, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("dunzmap", details.get(0));
        editor.putString("tankNo", details.get(1));
        editor.putString("nozzleNo", details.get(2));
        editor.putString("prodCategory", details.get(3));
        editor.putString("dealerId", details.get(4));
        editor.putString("inframapId", details.get(5));

        editor.apply();
    }

    public String[] getInfraDetailsForOPShift(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.INFRA_OP, MODE_PRIVATE);
        String dunzmap = prefs.getString("dunzmap", "");
        String tankNo = prefs.getString("tankNo", "");
        String nozzleNo = prefs.getString("nozzleNo", "");
        String prodCategory = prefs.getString("prodCategory", "");
        String dealerId = prefs.getString("dealerId", "");
        String inframapId = prefs.getString("inframapId", "");

        String[] details = {dunzmap, tankNo, nozzleNo, prodCategory, dealerId, inframapId};
        return details;
    }

    public void saveFuelStaffPerformId(Context context, String fuelStaffPerformId) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelStaffPerformId", fuelStaffPerformId);

        editor.apply();
    }

    public String getFuelStaffPerformId(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, MODE_PRIVATE);
        String fuelStaffPerformId = prefs.getString("fuelStaffPerformId", "");

        return fuelStaffPerformId;
    }

    public void saveFuelTankRefuelId(Context context, String refuelId) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.REFUEL_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("refuelId", refuelId);

        editor.apply();
    }

    public String getFuelTankRefuelId(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.REFUEL_ID, MODE_PRIVATE);
        String fuelStaffPerformId = prefs.getString("refuelId", "");

        return fuelStaffPerformId;
    }

    public void saveParamsForManageOPShift(Context context, List<String> params) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelStaffPerformId", params.get(0));
        editor.putString("forendshift", params.get(1));
        editor.putString("status", params.get(2));
        editor.apply();
    }

    public List<String> getParamsForManageOPShift(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, MODE_PRIVATE);
        String fuelStaffPerformId = prefs.getString("fuelStaffPerformId", "");
        String forendshift = prefs.getString("forendshift", "");
        String status = prefs.getString("status", "");
        List<String> params = new ArrayList<>();
        params.add(fuelStaffPerformId);
        params.add(forendshift);
        params.add(status);
        return params;
    }

    public void saveDigitalTransDetails(Context context, String[] details) {
        Log.d(TAG, "saveDigitalTransDetails:transacDate " + details[0]);
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL_TRANS, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("transacDate", details[0]);
        editor.putString("fuelDealerStaffId", details[1]);
        editor.putString("grandTotalAmount", details[2]);
        editor.putString("activityId", details[3]);
        editor.putString("batchId", details[4]);
        editor.putString("accountId", details[5]);
        editor.putString("transacId", details[6]);
        editor.putString("paytmTotalAmount", details[7]);
        editor.putString("cardTotalAmount", details[8]);
        editor.putString("creditTotal", details[9]);
        editor.putString("terminalId", details[10]);

        editor.apply();
    }

    public String[] getDigitalTransDetails(Context context) {
        Log.d(TAG, "getDigitalTransDetails: ");
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL_TRANS, MODE_PRIVATE);
        String transacDate = prefs.getString("transacDate", "");
        String fuelDealerStaffId = prefs.getString("fuelDealerStaffId", "");
        String grandTotalAmount = prefs.getString("grandTotalAmount", "");
        String activityId = prefs.getString("activityId", "");
        String batchId = prefs.getString("batchId", "");
        String accountId = prefs.getString("accountId", "");
        String transacId = prefs.getString("transacId", "");
        String paytmTotalAmount = prefs.getString("paytmTotalAmount", "");
        String cardTotalAmount = prefs.getString("cardTotalAmount", "");
        String creditTotal = prefs.getString("creditTotal", "");
        String terminalId = prefs.getString("terminalId", "");
        Log.d(TAG, "getDigitalTransDetails: transacDate : " + transacDate);
        String[] details = {transacDate, fuelDealerStaffId, grandTotalAmount, activityId, batchId,
                accountId, transacId, paytmTotalAmount, cardTotalAmount, creditTotal, terminalId};
        return details;
    }

    public void saveActivityId(Context context, String activityid) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ACTIVITY_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("activityid", activityid);

        editor.apply();
    }

    public String getActivityId(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.ACTIVITY_ID, MODE_PRIVATE);
        String activityid = prefs.getString("activityid", "");

        return activityid;
    }

    public void saveActivityIdForCredit(Context context, String activityid) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ACTIVITY_ID_CREDIT, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("activityidc", activityid);

        editor.apply();
    }


    public String getActivityIdForCredit(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.ACTIVITY_ID_CREDIT, MODE_PRIVATE);
        String activityid = prefs.getString("activityidc", "");

        return activityid;
    }

    public void saveTotalCreditAmountFinal(Context context, String creditfinal) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.TOTAL_CREDIT_FINAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("creditfinal", creditfinal);

        editor.apply();
    }


    public String getTotalCreditAmountFinal(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.TOTAL_CREDIT_FINAL, MODE_PRIVATE);
        String creditfinal = prefs.getString("creditfinal", "");

        return creditfinal;
    }

    public void saveIfCameFromCorporateOrPerson(Context context, String creditfinal) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.CORPORATE_PERSON, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("creditfinal", creditfinal);

        editor.apply();
    }


    public String getIfCameFromCorporateOrPerson(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.CORPORATE_PERSON, MODE_PRIVATE);
        String creditfinal = prefs.getString("creditfinal", "");

        return creditfinal;
    }


    public void saveTotalDueInSales(Context context, String totalDue) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.TOTAL_DUE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("totalDue", totalDue);

        editor.apply();
    }


    public String getTotalDueInSales(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.TOTAL_DUE, MODE_PRIVATE);
        String totalDue = prefs.getString("totalDue", "nothing");

        return totalDue;
    }

    public void saveBatchIdForDigital(Context context, String batchid) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.BATCH_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("batchid", batchid);

        editor.apply();
    }

    public String getBatchIdForDigital(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.BATCH_ID, MODE_PRIVATE);
        String batchid = prefs.getString("batchid", "");

        return batchid;
    }

    public void saveDigitalAddTransDetails(Context context, List<String> params) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("device", params.get(0));
        editor.putString("transid", params.get(1));
        editor.putString("amount", params.get(2));
        Log.d(TAG, "saveDigitalAddTransDetails: device : " + params.get(0));
        Log.d(TAG, "saveDigitalAddTransDetails: transid : " + params.get(1));
        Log.d(TAG, "saveDigitalAddTransDetails: amount : " + params.get(2));
        editor.apply();
    }

    public List<String> getDigitalAddTransDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.STAFF_PERFORM_ID, MODE_PRIVATE);
        String device = prefs.getString("device", "");
        String transid = prefs.getString("transid", "");
        String amount = prefs.getString("amount", "");
        Log.d(TAG, "getDigitalAddTransDetails:device :  " + device);
        Log.d(TAG, "getDigitalAddTransDetails: transId : " + transid);
        Log.d(TAG, "getDigitalAddTransDetails: amount : " + amount);
        List<String> params = new ArrayList<>();
        params.add(device);
        params.add(transid);
        params.add(amount);
        return params;
    }

    public void saveFuelInventoryOnCardClick(Context context, String forView, String fuelTankRefuelId) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.FI_CARD_CLICK, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("forview", forView);
        editor.putString("fuelTankRefuelId", fuelTankRefuelId);

        editor.apply();
    }

    public String[] getFuelnventoryOnCardClick(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.FI_CARD_CLICK, MODE_PRIVATE);
        String forview = prefs.getString("forview", "");
        String fuelTankRefuelId = prefs.getString("fuelTankRefuelId", "");
        Log.d(TAG, "getFuelnventoryOnCardClick:forview: " + forview);
        String[] cardDetails = {forview, fuelTankRefuelId};
        return cardDetails;
    }

    public void saveDailyStockOnCardClick(Context context, String fuelTankRefuelId, String updatestock, String status) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DAILY_STOCK_CARD, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelTankRefuelId", fuelTankRefuelId);
        editor.putString("updatestock", updatestock);
        editor.putString("status", status);

        editor.apply();
    }

    public String[] getDailyStockOnCardClick(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.DAILY_STOCK_CARD, MODE_PRIVATE);
        String fuelTankRefuelId = prefs.getString("fuelTankRefuelId", "");
        String updatestock = prefs.getString("updatestock", "");
        String status = prefs.getString("status", "");
        Log.d(TAG, "getDailyStockOnCardClick:status: " + status);
        String[] cardDetails = {fuelTankRefuelId, updatestock, status};
        return cardDetails;
    }


    public void saveFuelCreditId(Context context, String fuelCreditId) {
        SharedPreferences pref = context.getApplicationContext()
                .getSharedPreferences(Constants.FUEL_CREDIT_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelCreditId", fuelCreditId);

        editor.apply();
    }

    public String getFuelCreditId(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.FUEL_CREDIT_ID, MODE_PRIVATE);
        String fuelCreditId = prefs.getString("fuelCreditId", "");

        return fuelCreditId;
    }

    public void saveFuelCreditDetailsForView(Context context, String fuelCreditId, String forCredit) {
        SharedPreferences pref = context.getApplicationContext()
                .getSharedPreferences(Constants.CREDIT_DETAILS_VIEW, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelCreditId", fuelCreditId);
        editor.putString("forCredit", forCredit);

        editor.apply();
    }

    public String[] getFuelCreditDetailsForView(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.CREDIT_DETAILS_VIEW, MODE_PRIVATE);
        String fuelCreditId = prefs.getString("fuelCreditId", "");
        String forCredit = prefs.getString("forCredit", "");

        String deatils[] = {fuelCreditId, forCredit};

        return deatils;
    }

    public void saveAccessToken(Context context, String authenticationToken) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.ACCESS_TOKEN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("authenticationToken", authenticationToken);
        editor.apply();
    }

    public String getAccessToken(Context context) {
        String access = Constants.ACCESS_TOKEN;
        SharedPreferences prefs = context.getSharedPreferences(access, MODE_PRIVATE);
        String authenticationToken = prefs.getString("authenticationToken", "");
        Log.d("sharedpref", "getAccessToken: ");
        return authenticationToken;
    }

    public void saveUserProfileInfo(Context context, UserProfileModel userProfileModel) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.PROFILE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", userProfileModel.getUsername());
        editor.putString("userphone", userProfileModel.getUserPhone());
        editor.putString("companyname", userProfileModel.getCompanyName());
        editor.putString("address", userProfileModel.getDealerAddress());
        editor.putString("phone", userProfileModel.getDealerPhone());
        editor.putString("imagepath", userProfileModel.getUserImagePath());
        editor.putString("brandname", userProfileModel.getBrandName());

        editor.apply();
    }

    public UserProfileModel getUserProfileInfo(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.PROFILE, MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String userphone = prefs.getString("userphone", "");
        String companyname = prefs.getString("companyname", "");
        String address = prefs.getString("address", "");
        String phone = prefs.getString("phone", "");
        String imagepath = prefs.getString("imagepath", "");
        String brandname = prefs.getString("brandname", "");


        UserProfileModel userProfileModel = new UserProfileModel(username, companyname, address, userphone, phone, imagepath, brandname);
        return userProfileModel;
    }

    public void saveMenuDetailsAsPerDesignation(Context context, String designation) {
        SharedPreferences pref = context.getApplicationContext()
                .getSharedPreferences(Constants.FUEL_CREDIT_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("designation", designation);

        editor.apply();
    }

    public String getMenuDetailsAsPerDesignation(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.FUEL_CREDIT_ID, MODE_PRIVATE);
        String designation = prefs.getString("designation", "");

        return designation;
    }

    public void saveBatchIdForErReport(Context context, String batchid) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.BATCH_ID_ER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("batchider", batchid);

        editor.apply();
    }

    public String getBatchIdForErReport(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.BATCH_ID_ER, MODE_PRIVATE);
        String batchid = prefs.getString("batchider", "");

        return batchid;
    }

    public void saveDailySalesCardEssentials(Context context, String accountTransacLogId, String batchId) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DAILY_SALES, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("accountTransacLogId", accountTransacLogId);
        editor.putString("batchId", batchId);

        editor.apply();
    }

    public String[] getDailySalesCardEssentials(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DAILY_SALES, MODE_PRIVATE);
        String accountTransacLogId = prefs.getString("accountTransacLogId", "");
        String batchId = prefs.getString("batchId", "");
        String[] details = {accountTransacLogId, batchId};
        return details;
    }

    public void saveConfirmRecoveryFlag(Context context, boolean recovery) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.RECOVERY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("recovery", recovery);

        editor.apply();
    }

    public boolean getConfirmRecoveryFlag(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.RECOVERY, MODE_PRIVATE);
        boolean recovery = prefs.getBoolean("recovery", false);

        return recovery;
    }

    public void saveDailySalesCardDetailsForView(Context context, String viewOnly) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DAILY_SALES_VIEW, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("view", viewOnly);

        editor.apply();
    }

    public String getDailySalesCardDetailsForView(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DAILY_SALES_VIEW, MODE_PRIVATE);
        String viewOnly = prefs.getString("view", "");

        return viewOnly;
    }

    public void saveLocalErData(Context context, String metersale, String amounttally, String difference) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("metersale", metersale);
        editor.putString("amounttally", amounttally);
        editor.putString("difference", difference);

        editor.apply();
    }

    public String[] getLocalErData(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.ER, MODE_PRIVATE);
        String metersale = prefs.getString("metersale", "");
        String amounttally = prefs.getString("amounttally", "");
        String difference = prefs.getString("difference", "");

        String[] details = {metersale, amounttally, difference};
        return details;
    }

    public void saveRecoveryAmountInSales(Context context, String recoveryamt) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.RECOVERY_AMOUNT, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("recoveryamt", recoveryamt);


        Log.d(TAG, "saveRecoveryAmountInSales: recoveryamt : " + recoveryamt);

        editor.apply();
    }

    public String getRecoveryAmountInSales(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.RECOVERY_AMOUNT, MODE_PRIVATE);
        String recoveryamt = prefs.getString("recoveryamt", "");
        Log.d(TAG, "getRecoveryAmountInSales: recoveryamt : " + recoveryamt);
        return recoveryamt;
    }

    public void saveAllSalesValues(Context context, List<String> detailsList) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ALL_SALES, 0);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("cash", detailsList.get(0));
        editor.putString("paytm", detailsList.get(1));
        editor.putString("card", detailsList.get(2));
        editor.putString("credit", detailsList.get(3));

        Log.d(TAG, "saveRecoveryAmountInSales: recoveryamt : " + detailsList.size());

        editor.apply();
    }

    public String[] saveAllSalesValues(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.ALL_SALES, MODE_PRIVATE);
        String cash = prefs.getString("cash", "");
        String paytm = prefs.getString("paytm", "");
        String card = prefs.getString("card", "");
        String credit = prefs.getString("credit", "");
        String[] details = {cash, paytm, card, credit};
        return details;
    }

    public void saveCashDetails(Context context, String cash) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.CASH, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("cash", cash);


        Log.d(TAG, "saveRecoveryAmountInSales: cash : " + cash);

        editor.apply();
    }

    public String getCashDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.CASH, MODE_PRIVATE);
        String cash = prefs.getString("cash", "0.0");
        Log.d(TAG, "getCashDetails: cash : " + cash);
        return cash;
    }

    public void saveDigitalDetails(Context context, String paytm, String card) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("paytm", paytm);
        editor.putString("card", card);


        Log.d(TAG, "saveDigitalDetails: card : " + card);

        editor.apply();
    }

    public String[] getDigitalDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL, MODE_PRIVATE);
        String paytm = prefs.getString("paytm", "0.0");
        String card = prefs.getString("card", "0.0");

        Log.d(TAG, "getDigitalDetails: paytm : " + paytm);
        String[] details = {paytm, card};
        return details;
    }

    public void saveIsTransited(Context context, boolean isdone) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DONE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isdone", isdone);

        Log.d(TAG, "saveRecoveryAmountInSales: isdone : " + isdone);

        editor.apply();
    }

    public boolean getIsTransited(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DONE, MODE_PRIVATE);
        boolean isdone = prefs.getBoolean("isdone", false);

        Log.d(TAG, "getCashDetails: cash : " + isdone);
        return isdone;
    }

    public void saveFuelCreditDetails(Context context, String fuelDealerCustomerMapId,
                                      String fuelCorporateId) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelDealerCustomerMapId", fuelDealerCustomerMapId);
        editor.putString("fuelCorporateId", fuelCorporateId);


        editor.apply();
    }

    public String[] getFuelCreditDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DIGITAL, MODE_PRIVATE);
        String paytm = prefs.getString("fuelDealerCustomerMapId", "");
        String card = prefs.getString("fuelCorporateId", "");

        String[] details = {paytm, card};
        return details;
    }

    public void saveSelectedDevice(Context context, String deviceName) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DEVICE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("deviceName", deviceName);

        editor.apply();
    }

    public String getSelectedDevice(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DEVICE, MODE_PRIVATE);
        String deviceName = prefs.getString("deviceName", "");

        return deviceName;
    }

    public void saveDetailsForCreditReq(Context context, String fuelCreditId, String forView) {
        SharedPreferences pref = context.getApplicationContext()
                .getSharedPreferences(Constants.MANUAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fuelCreditId", fuelCreditId);
        editor.putString("forview", forView);

        editor.apply();
    }

    public String[] getDetailsForCreditReq(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.MANUAL, MODE_PRIVATE);
        String fuelCreditId = prefs.getString("fuelCreditId", "");
        String forview = prefs.getString("forview", "");
        String[] details = {fuelCreditId, forview};
        return details;
    }

    public void saveSelectedLanguage(Context context, String language) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.LANG, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lang", language);
        Log.d(TAG, "saveSelectedLanguage:language :  " + language);
        editor.apply();
    }

    public String getSelectedLanguage(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.LANG, MODE_PRIVATE);
        String language = prefs.getString("lang", "");
        Log.d(TAG, "getSelectedLanguage: language : " + language);
        return language;
    }

    //delete after + click for new sales entry
    public void saveIsGiveCallForAPIInfoTabDailySales(Context context, String apicall) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.INFO_CARDS, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("apicall", apicall);

        editor.apply();
    }

    public String getIsGiveCallForAPIInfoTabDailySales(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.INFO_CARDS, MODE_PRIVATE);
        String apicall = prefs.getString("apicall", "");
        Log.d(TAG, "getIsInfoCardsExists: apicall : " + apicall);
        return apicall;
    }

    public void saveIsDigitalSubmitTransClicked(Context context, String isclicked) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.IS_SUBMIT_DIGITAL, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("isclicked", isclicked);

        editor.apply();
    }

    public String getIsDigitalSubmitTransClicked(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.IS_SUBMIT_DIGITAL, MODE_PRIVATE);
        String isclicked = prefs.getString("isclicked", "");
        Log.d(TAG, "getIsDigitalSubmitTransClicked: isclicked : " + isclicked);
        return isclicked;
    }

    public void saveTotalAmtDigitalForDoneCardClickView(Context context, String totalAmount,
                                                        String totalBatchAmtExpted, String totalBatchAmtRecovered) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.DONE_PAY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("forsubmit", totalAmount);
        editor.putString("totalBatchAmtExpted", totalBatchAmtExpted);
        editor.putString("totalBatchAmtRecovered", totalBatchAmtRecovered);

        editor.apply();
    }

    public String[] getTotalAmtDigitalForDoneCardClickView(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.DONE_PAY, MODE_PRIVATE);
        String totalAmount = prefs.getString("forsubmit", "");
        String totalBatchAmtExpted = prefs.getString("totalBatchAmtExpted", "");
        String totalBatchAmtRecovered = prefs.getString("totalBatchAmtRecovered", "");

        Log.d(TAG, "getTotalAmtDigitalForDoneCardClickView: isclicked : " + totalAmount);
        String[] allDonePAys = {totalAmount, totalBatchAmtExpted, totalBatchAmtRecovered};
        return allDonePAys;
    }

    public void savePersonIDForForgotPass(Context context, String personId) {

        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.PASSWORD, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("personIdf", personId);
        editor.apply();

    }

    public String getPersonIDForForgotPass(Context context) {
        SharedPreferences prefs = context.getApplicationContext().getSharedPreferences(Constants.PASSWORD, MODE_PRIVATE);
        String personId = prefs.getString("personIdf", "null");
        return personId;
    }


    public void saveCorporateId(Context context, String corporateId) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences
                (Constants.CORPORATE_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("corporateId", corporateId);
        editor.apply();
    }

    public String getCorporateId(Context context) {
        String access = Constants.CORPORATE_ID;
        SharedPreferences prefs = context.getSharedPreferences(access, MODE_PRIVATE);
        String corporateId = prefs.getString("corporateId", "");
        return corporateId;
    }

    public void savePriceEssentials(Context context, String dealerTankMapCode, String TotalTank) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences
                (Constants.PRICE_ESSEN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("dealerTankMapCode", dealerTankMapCode);
        editor.putString("TotalTank", TotalTank);

        editor.apply();
    }

    public String[] getPriceEssentials(Context context) {
        String access = Constants.PRICE_ESSEN;
        SharedPreferences prefs = context.getSharedPreferences(access, MODE_PRIVATE);
        String dealerTankMapCode = prefs.getString("dealerTankMapCode", "");
        String TotalTank = prefs.getString("TotalTank", "");

        String[] priceEssen = {dealerTankMapCode, TotalTank};
        return priceEssen;
    }

    public void saveFuelPriceDetails(Context context, String activity, String message) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.FUEL_PRICE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("activity", activity);
        editor.putString("message", message);

        editor.apply();
    }

    public String[] getFuelPriceDetails(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.FUEL_PRICE, MODE_PRIVATE);
        String activity = prefs.getString("activity", "");
        String message = prefs.getString("message", "");
        String[] priceDetails = {activity, message};
        return priceDetails;
    }

    public void saveManagerOPList(Context context, String manager) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.MANAGER, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("manager", manager);

        editor.apply();
    }

    public String getManagerOPList(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.MANAGER, MODE_PRIVATE);
        String manager = prefs.getString("manager", "");
        return manager;
    }

    public void saveSessionTimer(Context context, String logoutTime) {
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Constants.TIMER_SESSION, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("logouttime", logoutTime);

        editor.apply();
    }

    public String addTimeToLoginTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();


        String loginTime = df.format(calendar.getTime());

        calendar.setTime(calendar.getTime());
        calendar.add(Calendar.HOUR, 4);

        //logout time
        String logouttime = df.format(calendar.getTime());

        return logouttime;

    }

    public void saveNoInfraDetailsMessage(Context context, String noinfra) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.NO_INFRA, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("noinfra", noinfra);

        editor.apply();
    }

    public String getNoInfraDetailsMessage(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.NO_INFRA, MODE_PRIVATE);
        String noinfra = prefs.getString("noinfra", "");
        return noinfra;
    }

    public void saveSeletctedTabForRefuel(Context context, int selectedTab) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.SELECTED_TAB, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("selectedTab", selectedTab);

        editor.apply();
    }

    public int getSeletctedTabForRefuel(Context context) {
        SharedPreferences prefs = context.getApplicationContext().
                getSharedPreferences(Constants.SELECTED_TAB, MODE_PRIVATE);
        int selectedTab = prefs.getInt("selectedTab", 0);
        return selectedTab;
    }

    public void saveNewPayDetailsModelForExistingBatch(Context context, AddNewPayDetailsModel addNewPayDetailsModel) {
        SharedPreferences pref = context.getApplicationContext().
                getSharedPreferences(Constants.ADD_NEW, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("batchId", addNewPayDetailsModel.getBatchId());
        editor.putString("corporateId", addNewPayDetailsModel.getCorporateID());
        editor.putString("batchAmtExpected", addNewPayDetailsModel.getTotalBatchAmtExpected());
        editor.putString("batchAmtRecovered", addNewPayDetailsModel.getTotalBatchAmtRecovered());
        editor.putString("shiftWiseCreditSumForApp", addNewPayDetailsModel.getShiftWiseCreditSumForApp());

        editor.apply();
    }

    public AddNewPayDetailsModel getNewPayDetailsModelForExistingBatch(Context context) {
        SharedPreferences prefs = context.getApplicationContext()
                .getSharedPreferences(Constants.ADD_NEW, MODE_PRIVATE);
        String batchId = prefs.getString("batchId", "");
        String corporateId = prefs.getString("corporateId", "");
        String batchAmtExpected = prefs.getString("batchAmtExpected", "");
        String batchAmtRecovered = prefs.getString("batchAmtRecovered", "");
        String shiftWiseCreditSumForApp = prefs.getString("shiftWiseCreditSumForApp", "");


        AddNewPayDetailsModel addNewPayDetailsModel = new AddNewPayDetailsModel(batchId,
                corporateId, batchAmtExpected, batchAmtRecovered,shiftWiseCreditSumForApp);
        return addNewPayDetailsModel;
    }


}
