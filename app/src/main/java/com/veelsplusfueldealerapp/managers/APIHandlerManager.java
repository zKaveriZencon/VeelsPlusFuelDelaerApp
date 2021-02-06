package com.veelsplusfueldealerapp.managers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.DailySalesActivity;
import com.veelsplusfueldealerapp.activities.HomeActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.ErCashModel;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.FleetListModel;
import com.veelsplusfueldealerapp.models.FuelTerminalModel;
import com.veelsplusfueldealerapp.models.InfraMappingDbModel;
import com.veelsplusfueldealerapp.models.LoginCredentialsModel;
import com.veelsplusfueldealerapp.models.NonFleetListModel;
import com.veelsplusfueldealerapp.models.NonFleetModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIHandlerManager {
    private static final String TAG = "APIHandlerManager";
    Context context;
    ProgressDialog dialogLogin;
    CommonTaskManager mTaskManager;
    List<InfraMappingDbModel> infraMappingDetailsList;
    DatabaseHelper databaseHelper;
    CommonTaskManager commonTaskManager;
    private CommonCodeManager commonCodeManager;

    public APIHandlerManager(Context context) {
        this.context = context;
        mTaskManager = new CommonTaskManager(context);
        commonCodeManager = new CommonCodeManager(context);
        databaseHelper = new DatabaseHelper(context);
        commonTaskManager = new CommonTaskManager(context);
    }

    public void apiCallForLogin(final Context context, final String phone,
                                final String password) {
        Log.d(TAG, "apiCallForLogin: phone : " + phone);
        Log.d(TAG, "apiCallForLogin: password : " + password);
        dialogLogin = new ProgressDialog(context);
        dialogLogin = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialogLogin.setMessage(context.getResources().getString(R.string.loading));
        dialogLogin.setCancelable(false);
        dialogLogin.setCanceledOnTouchOutside(false);
        dialogLogin.show();

        final String[] message = new String[1];

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.loginToDealer(phone, password);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:jsonData :  " + jsonData);
                try {

                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim();
                    message[0] = mainObject.getString("msg");

                    if (!status.isEmpty() && status.equals("OK")) {
                        String authenticationToken = mainObject.getString("authenticationToken").trim();
                        String[] loginDetails = {phone, password, authenticationToken};

                        JSONObject elementObj = mainObject.getJSONObject("element");
                        String fuelDealerStaffId = elementObj.getString("fuelDealerStaffId");
                        String fuelDealerId = elementObj.getString("fuelDealerId");
                        String FuelVeelsId = elementObj.getString("FuelVeelsId");
                        final String personId = elementObj.getString("personId").trim();
                        String userId = elementObj.getString("userId");
                        String firstName = elementObj.getString("firstName");
                        String lastName = elementObj.getString("lastName");
                        String designation = elementObj.getString("designation");
                        String corporateId = elementObj.getString("corporateId");

                        String logoutTime = commonCodeManager.addTimeToLoginTime();
                        commonCodeManager.saveSessionTimer(context, logoutTime);

                        commonCodeManager.saveAccessToken(context, authenticationToken);
                        LoginCredentialsModel login = new LoginCredentialsModel(phone, password, true);
                        commonCodeManager.saveLoginCredentials(context, login);

                        String[] dealerDetails = {fuelDealerStaffId, fuelDealerId, FuelVeelsId};
                        commonCodeManager.saveDealerDetailsOnLogin(context, dealerDetails);

                        commonCodeManager.saveUserLoginDetails(context, loginDetails);

                        String[] essentials = {personId, userId, firstName, lastName, designation};
                        commonCodeManager.saveEssentialsForDealer(context, essentials);

                        commonCodeManager.saveCorporateId(context, corporateId);

                        Handler handler1 = new Handler(context.getMainLooper());
                        handler1.post(new Runnable() {
                            public void run() {
                                dialogLogin.dismiss();
                                GetFuelInfraDetailsTask getFuelInfraDetailsTask = new GetFuelInfraDetailsTask(context);
                                getFuelInfraDetailsTask.execute("");


                                Toast.makeText(context, context.getResources().getString(R.string.welcome_user), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(context, HomeActivity.class);
                                intent.putExtra("personId", personId);
                                context.startActivity(intent);
                            }
                        });


                        ((Activity) context).finish();

                    }
                    if (!status.isEmpty() && status.equals("ERROR")) {
                        mTaskManager.dismissDialogWithToast(context, dialogLogin, message[0]);
                    }


                } catch (Exception e) {
                    Log.d("apihandler", "onResponse: e: " + e.getLocalizedMessage());
                    mTaskManager.dismissDialogWithToast(context, dialogLogin, message[0]);
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("apihandler", "onResponse: t: " + t.getLocalizedMessage());
                t.printStackTrace();
                // mTaskManager.dismissDialogWithToast(context, dialogLogin, context.getResources().getString(R.string.unable_to_connect));
                mTaskManager.dismissProgressDialog(context, dialogLogin);
                mTaskManager.showAlertDialogWithOneButtonHandler(context, "Error !", context.getResources().getString(R.string.unable_to_connect), "OK");

            }

        });
    }

    public void apiCallForGetFuelInfraDetails(final Context context, final String fuelVendorId) {
        Log.d(TAG, "apiCallForGetFuelInfraDetails: ");
        final String[] message = new String[1];
        infraMappingDetailsList = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelInfraDetails(fuelVendorId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse: jsonData apiCallForGetFuelInfraDetails :" + jsonData);
                try {

                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim();
                    message[0] = mainObject.getString("msg");

                    if (!status.isEmpty() && status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                String tankNo = jsonObject.getString("tankNo");
                                String duNo = jsonObject.getString("duNo");
                                String nozNo = jsonObject.getString("nozNo");
                                String tankDuNzMap = jsonObject.getString("tankDuNzMap");
                                String fuelProductId = jsonObject.getString("fuelProductId");
                                String brandName = jsonObject.getString("brandName");
                                String productName = jsonObject.getString("productName");
                                String productCategory = jsonObject.getString("productCategory");
                                String productCode = jsonObject.getString("productCode");
                                String mapstatus = jsonObject.getString("mapstatus");
                                String FuelVeelsId = jsonObject.getString("FuelVeelsId");
                                String fuelInfraMapId = jsonObject.getString("fuelInfraMapId");

                                InfraMappingDbModel infraMappingDbModel = new InfraMappingDbModel(tankNo, duNo, nozNo, tankDuNzMap, fuelProductId, brandName, productName, productCategory, productCode, mapstatus, fuelInfraMapId, FuelVeelsId);
                                infraMappingDetailsList.add(infraMappingDbModel);
                            }
                            databaseHelper.addInfraMappingDetails(infraMappingDetailsList);

                        } else {
                            commonCodeManager.saveNoInfraDetailsMessage(context, "noinfra");
                           /* Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setTitle("Note !");
                                    builder.setMessage("Unable to get proper Infra Setup data from server. Please check infra setup once.");
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }
                            });*/
                        }
                    }

                    apiCallForGetFuelTerminals(context, fuelVendorId);

                } catch (Exception e) {
                    Log.d("apihandler", "onResponse: e: " + e.getLocalizedMessage());
                    mTaskManager.dismissProgressDialog(context, dialogLogin);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: f : " + t.getLocalizedMessage());
                commonTaskManager.showToast(context, context.getResources().getString(R.string.unable_to_connect));
                t.printStackTrace();
            }

        });
    }

    private void apiCallForGetFuelTerminals(final Context context, String fuelDealerId) {
        Log.d(TAG, "apiCallForGetFuelTerminals: ");
        final String[] message = new String[1];
        final List<FuelTerminalModel> fuelTerminalModelsList = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelTerminals(fuelDealerId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse: jsonData apiCallForGetFuelTerminals :" + jsonData);
                try {

                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim();
                    message[0] = mainObject.getString("msg");

                    if (!status.isEmpty() && status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                String fuelTerminalsId = jsonObject.getString("fuelTerminalsId");
                                String terminalName = jsonObject.getString("terminalName");
                                String attachedBankName = jsonObject.getString("attachedBankName");
                                String terminalType = jsonObject.getString("terminalType");

                                FuelTerminalModel fuelTerminalModel =
                                        new FuelTerminalModel(fuelTerminalsId, terminalName, attachedBankName, terminalType);
                                fuelTerminalModelsList.add(fuelTerminalModel);


                            }
                            databaseHelper.saveFuelTerminals(fuelTerminalModelsList);

                        }
                    }


                } catch (Exception e) {
                    Log.d("apihandler", "onResponse: e: " + e.getLocalizedMessage());
                    mTaskManager.dismissProgressDialog(context, dialogLogin);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: f : " + t.getLocalizedMessage());
                commonTaskManager.showToast(context, context.getResources().getString(R.string.unable_to_connect));

                t.printStackTrace();
            }

        });

    }

    public void apiCallForAddAccountTransactionLogForDigital(final Context context,
                                                             ErDigitalModel erDigitalModel) {
        Log.d(TAG, "apiCallForAddAccountTransactionLogForDigital: ");
       /* final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();*/
        final List<OperatorDailyWorkListModel> pendingPays = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addAccountTransactionLogForDigital(erDigitalModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForForDigital :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    //commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        //     commonTaskManager.dismissDialogWithToast(context, progressDialog, message);

                    }


                } catch (Exception e) {
                    // commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                //commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

    }

    public void apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(final Context context, String activityId, String batchId) {
        Log.d(TAG, "apiCallForAddRecoveryBatchIdAfterTransUpdateStatus: ");
        if (activityId != null && !activityId.equals("null") && !activityId.isEmpty()) {


            String[] activityIDs = activityId.split("_");
            for (int i = 0; i < activityIDs.length; i++) {

                Log.d(TAG, "apiCallForAddRecoveryBatchIdAfterSalesTransaction: i : " + activityIDs[i]);

                GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
                Call<JsonObject> call = service.updateRecoveryStatusForDailySales(activityIDs[i], batchId);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        final String jsonData = response.body().toString();
                        Log.d(TAG, "onRes:AddRecoveryBatchIdAfterSalesTransaction :  " + jsonData);
                        try {
                            JSONObject mainObject = new JSONObject(jsonData);
                            //commonTaskManager.dismissProgressDialog(context, progressDialog);
                            final String status = mainObject.getString("status");
                            final String message = mainObject.getString("msg");

                            if (status.equals("OK")) {

                            }


                        } catch (Exception e) {

                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {

                    }

                });

            }
        } else {
            Log.d(TAG, "apiCallForAddRecoveryBatchIdAfterSalesTransaction: else check activity id");
        }

    }

    private void apiCallForUpdateRecoveryStatus(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String[] details = commonCodeManager.getDealerDetails(context);

        final String[] message = new String[1];
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateRecoveryStatusForDailySales(details[0],
                commonCodeManager.getBatchIdForErReport(context));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateRecoveryStatus :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");

                    if (status.equals("ok")) {
                        // commonCodeManager.saveConfirmRecoveryFlag(context, false);
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, msg);

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, msg);

                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

            }

        });

    }

    public List<FleetListModel> apiCallForSearchDriver(final Context context, final String phone1) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};
        final List<FleetListModel> fleetList = new ArrayList<>();

        String[] details = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByPersonNumber(phone1, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchDriver credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");
                                Log.d(TAG, "onResponse:transactionStatus :  " + transactionStatus);
/*
                                if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                    String[] datetime = estimatedRefuelDate.split("T");
                                    dateOnly[0] = datetime[0];

                                } else {
                                    dateOnly[0] = "";
                                }*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }

                                FleetListModel fleetListModel = new FleetListModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);
                                fleetList.add(fleetListModel);


                            }

                        } else {
                            //commonTaskManager.showToast(context, "No fuel credit requests found for " + phone1);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No fuel credit requests found for " + phone1)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                        // fillFleetListCards(fleetList);


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

        return fleetList;
    }

    public List<FleetListModel> apiCallForSerachByVehicleNumber(final Context context, final String vehicleNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};
        final List<FleetListModel> fleetList = new ArrayList<>();
        String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForSerachByVehicleNumber: vehicleNumber: " + vehicleNumber);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByVehicleNumber(vehicleNumber, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSerachByVehicleNumber credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");


                          /*  if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly[0] = datetime[0];

                            } else {
                                dateOnly[0] = "";
                            }
*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }
                                FleetListModel fleetListModel = new FleetListModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);
                                fleetList.add(fleetListModel);


                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No pending requests found against " + vehicleNumber)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                          /*  String msg = "No pending requests found against " + vehicleNumber;
                            commonTaskManager.showToast(context, msg);*/
                        }

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

        return fleetList;
    }

    public List<NonFleetModel> apiCallForSearchDriverNonFleet(final Context context, final String phone1) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};
        final List<NonFleetModel> fleetList = new ArrayList<>();

        String[] details = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByPersonNumber(phone1, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchDriver credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");
                                Log.d(TAG, "onResponse:transactionStatus :  " + transactionStatus);
/*
                                if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                    String[] datetime = estimatedRefuelDate.split("T");
                                    dateOnly[0] = datetime[0];

                                } else {
                                    dateOnly[0] = "";
                                }*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }

                                NonFleetModel fleetListModel = new NonFleetModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus, fuelCreditId);
                                fleetList.add(fleetListModel);


                            }

                        } else {
                            //commonTaskManager.showToast(context, "No fuel credit requests found for " + phone1);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No fuel credit requests found for " + phone1)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                        // fillFleetListCards(fleetList);


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

        return fleetList;
    }

    public List<NonFleetModel> apiCallForSerachByVehicleNumberNonFleet(final Context context, final String vehicleNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};
        final List<NonFleetModel> fleetList = new ArrayList<>();
        String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForSerachByVehicleNumber: vehicleNumber: " + vehicleNumber);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByVehicleNumber(vehicleNumber, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSerachByVehicleNumber credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");


                          /*  if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly[0] = datetime[0];

                            } else {
                                dateOnly[0] = "";
                            }
*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }
                              /*  FleetListModel fleetListModel = new FleetListModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);*/
                                NonFleetModel nonFleet = new NonFleetModel("driverName", phone1, "dateOnly", vehicleNumber, transactionStatus, phone1, fuelCreditId);

                                fleetList.add(nonFleet);


                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No pending requests found against " + vehicleNumber)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                          /*  String msg = "No pending requests found against " + vehicleNumber;
                            commonTaskManager.showToast(context, msg);*/
                        }

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

        return fleetList;
    }

}