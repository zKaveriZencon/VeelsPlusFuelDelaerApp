package com.veelsplusfueldealerapp.activities;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.NonFleetModel;
import com.veelsplusfueldealerapp.models.OperatorEndShiftModel;
import com.veelsplusfueldealerapp.models.QRCardRefuelModel;
import com.veelsplusfueldealerapp.models.UpdateFuelCreditRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ManualEntryFormActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "ManualEntryFormActivity";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    TextView textViewDriverNamee, textViewdRiverNo, textViewVehicleNo, textViewCustName,
            textViewCustNo, textViewFuelType, textViewProdName, textViewQuantity,
            textViewReqAmount, textViewActualAmt, textViewReqQuantity,
            textViewFuelPrice, textviewVehicleNoLabel, labelCustName, labelCustNo;
    //EditText editTextActualQuan, editTextActualAmt;
    MaterialButton materialButton;
    TextInputEditText requiredQuantity;
    TextInputEditText actualQuantity;
    TextInputEditText requiredAmount;
    TextInputEditText actualAmount;
    ImageView imageViewBack;
    String reqQuantity, reqAmount;
    // EditText editTextActualQuantity, editTextActualAmount;
    // MaterialRadioButton radioActualQuantity, radioActualAmount;
    LinearLayout layoutParentManual;
    //ImageView imageViewCalcAmt, imageViewCalcQuantity;
    TextView textViewActualQuanNew, textViewActualAmtNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry_form);
        commonCodeManager = new CommonCodeManager(ManualEntryFormActivity.this);
        commonTaskManager = new CommonTaskManager(ManualEntryFormActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_form);
        imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setVisibility(View.VISIBLE);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Fuel Credit Request Details");

        initUI();

        imageViewBack.setOnClickListener(this);


    }

    private void initUI() {
        textViewDriverNamee = findViewById(R.id.textview_drivername);
        textViewdRiverNo = findViewById(R.id.textview_driverno);
        textViewVehicleNo = findViewById(R.id.textview_vehicleno);
        textViewCustName = findViewById(R.id.textview_custname);
        textViewCustNo = findViewById(R.id.textview_custno);
        textViewFuelType = findViewById(R.id.textview_fueltype);
        textViewProdName = findViewById(R.id.textview_prodname);

        textViewReqQuantity = findViewById(R.id.textview_required_quantitym);
        textViewReqAmount = findViewById(R.id.textview_required_amountm);

        textViewFuelPrice = findViewById(R.id.textview_fuel_price_new);
        layoutParentManual = findViewById(R.id.layout_parent_manual);

        textViewActualQuanNew = findViewById(R.id.textview_actual_quan_manual);
        textViewActualAmtNew = findViewById(R.id.textview_actual_amt_manual);

       /* imageViewCalcAmt = findViewById(R.id.imageview_calc_amt);
        imageViewCalcQuantity = findViewById(R.id.imageview_calc_quan);
*/
        textviewVehicleNoLabel = findViewById(R.id.label_vehicle_no_me);


        labelCustName = findViewById(R.id.label_custName_per);
        labelCustNo = findViewById(R.id.label_cust_no);

        View toolbar = findViewById(R.id.layout_toolbar_form);
        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);

/*
        imageViewCalcAmt.setOnClickListener(this);
        imageViewCalcQuantity.setOnClickListener(this);*/


        // textViewTotalPrice = findViewById(R.id.textview_total_price);

       /* editTextActualQuan = findViewById(R.id.edittext_actual_quantitym);
        editTextActualAmt = findViewById(R.id.edittext_actual_amtm);*/

     /*   editTextActualQuantity = findViewById(R.id.edittext_actual_quannew);
        editTextActualAmount = findViewById(R.id.edittext_actual_amtnew);
        radioActualQuantity = findViewById(R.id.radio_actual_quan);
        radioActualAmount = findViewById(R.id.radio_actual_amount);*/

        //

        materialButton = findViewById(R.id.button_submitm);
        materialButton.setOnClickListener(this);


      /*  radioActualQuantity.setOnClickListener(this);
        radioActualAmount.setOnClickListener(this);*/


    }

    @Override
    protected void onResume() {
        super.onResume();


        boolean result = commonTaskManager.compareDates(ManualEntryFormActivity.this);

        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(ManualEntryFormActivity.this, ManualEntryFormActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String[] details = commonCodeManager.getDetailsForCreditReq(ManualEntryFormActivity.this);
            if (details[1] != null && !details[1].isEmpty() && details[1].equals("forview")) {
                //Toast.makeText(this, "For view", Toast.LENGTH_SHORT).show();

                //old logic
            /*editTextActualQuan.setEnabled(false);
            editTextActualAmt.setEnabled(false);*/

             /*   //for view

                editTextActualQuantity.setEnabled(false);
                editTextActualAmount.setEnabled(false);

                radioActualQuantity.setEnabled(false);
                radioActualAmount.setEnabled(false);*/

                materialButton.setVisibility(View.INVISIBLE);


            } else {
                Log.d(TAG, "onResume: for update : ");

                materialButton.setText("UPDATE");

            }
            String fuelCreditId = commonCodeManager.getFuelCreditId(ManualEntryFormActivity.this);
            String[] creditDetails = commonCodeManager.getFuelCreditDetailsForView(ManualEntryFormActivity.this);

            Log.d(TAG, "onResume: fuelCreditId : " + fuelCreditId);

            if (fuelCreditId != null && !fuelCreditId.isEmpty() && !creditDetails[1].isEmpty() && creditDetails[1].equals("corporate")) {
                apiCallForGetScannedCardDetails(ManualEntryFormActivity.this, fuelCreditId);
            }

            if (fuelCreditId != null && !fuelCreditId.isEmpty() && !creditDetails[1].isEmpty() && creditDetails[1].equals("person")) {

                textViewCustName.setVisibility(View.GONE);
                textViewCustNo.setVisibility(View.GONE);
                labelCustName.setVisibility(View.GONE);
                labelCustNo.setVisibility(View.GONE);

                apiCallForGetScannedCardDetailsForPerson(ManualEntryFormActivity.this, fuelCreditId);
            }
        }
    }

    private void apiCallForGetScannedCardDetailsForPerson(final Context context, String fuelCreditId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelCreditRequestDetailsForPersonByfuelCreditId(fuelCreditId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetScannedCardDetailsForPerson :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject detailsObj = dataArray.getJSONObject(0);
                        String firstName = detailsObj.getString("firstName");
                        String lastName = detailsObj.getString("lastName");
                        String driverName = firstName + " " + lastName;
                        // String custName = detailsObj.getString("companyName");
                        String productName = detailsObj.getString("productName");
                        // String vehicleNo = detailsObj.getString("registrationNumber");
                        String productCategory = detailsObj.getString("productCategory");
                        String reqCreditAmount = detailsObj.getString("reqCreditAmount");
                        String reqQuantity = detailsObj.getString("reqQuantity");
                        String phone1 = detailsObj.getString("phone1");
                        //String hostPhone = detailsObj.getString("hostPhone");
                        String productRate = detailsObj.getString("productRate");

                        String transactionStatus = detailsObj.getString("transactionStatus").toLowerCase().trim();
                        String fuelProdId = detailsObj.getString("fuelProdId");
                        String vehicleNumber = detailsObj.getString("vehicleNumber");

                        if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                            vehicleNumber = "Not Provided";
                        }

                        String creditAmount = "", actualCreditQuantity = "";
                        if (transactionStatus.equals("complete")) {
                            creditAmount = detailsObj.getString("creditAmount");
                            actualCreditQuantity = detailsObj.getString("actualCreditQuantity");
                        } else {

                        }
                        QRCardRefuelModel qrCardRefuelModel = new QRCardRefuelModel(driverName,
                                phone1, vehicleNumber, "", "", productCategory,
                                productName, reqQuantity,
                                productRate, reqCreditAmount, creditAmount,
                                actualCreditQuantity, fuelProdId);

                        // qrCardRefuelModelList.add(qrCardRefuelModel);
                        Log.d(TAG, "onResponse: productRate : " + productRate);

                        fillUpAllFormData(qrCardRefuelModel);
                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

    }

    private void apiCallForGetTodaysFuelPrice(final Context context, String fuelProdId) {


        Log.d(TAG, "apiCallForGetTodaysFuelPrice:fuelProdId :  " + fuelProdId);
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait, Retrieving Today's Fuel Price");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getPriceByDealerProductId(dealerDetails[1], fuelProdId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetTodaysFuelPrice :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            JSONObject detailsObj = dataArray.getJSONObject(0);
                            final String productSellingPrice = detailsObj.getString("productSellingPrice");

                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    textViewFuelPrice.setText(productSellingPrice);

                                }
                            });
                        } else {
                            commonTaskManager.showToast(context, "No fuel price found");
                        }


                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });


    }

    private void apiCallForGetScannedCardDetails(final Context context,
                                                 String fuelCreditId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelCreditRequestByfuelCreditId(fuelCreditId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetScannedCardDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject detailsObj = dataArray.getJSONObject(0);
                        String firstName = detailsObj.getString("firstName");
                        String lastName = detailsObj.getString("lastName");
                        String driverName = firstName + " " + lastName;
                        String custName = detailsObj.getString("companyName");
                        String productName = detailsObj.getString("productName");
                        // String vehicleNo = detailsObj.getString("registrationNumber");
                        String productCategory = detailsObj.getString("productCategory");
                        String reqCreditAmount = detailsObj.getString("reqCreditAmount");
                        String reqQuantity = detailsObj.getString("reqQuantity");
                        String phone1 = detailsObj.getString("phone1");
                        String hostPhone = detailsObj.getString("hostPhone");
                        String productRate = detailsObj.getString("productRate");

                        String transactionStatus = detailsObj.getString("transactionStatus").toLowerCase().trim();
                        String fuelProdId = detailsObj.getString("fuelProdId");
                        String vehicleNumber = detailsObj.getString("vehicleNumber");

                        if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                            vehicleNumber = "Not Provided";
                        }

                        String creditAmount = "", actualCreditQuantity = "";
                        if (transactionStatus.equals("complete")) {
                            creditAmount = detailsObj.getString("creditAmount");
                            actualCreditQuantity = detailsObj.getString("actualCreditQuantity");
                        } else {

                        }
                        QRCardRefuelModel qrCardRefuelModel = new QRCardRefuelModel(driverName,
                                phone1, vehicleNumber, custName, hostPhone, productCategory,
                                productName, reqQuantity,
                                productRate, reqCreditAmount, creditAmount,
                                actualCreditQuantity, fuelProdId);

                        // qrCardRefuelModelList.add(qrCardRefuelModel);
                        Log.d(TAG, "onResponse: productRate : " + productRate);

                        fillUpAllFormData(qrCardRefuelModel);
                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });


    }

    private void fillUpAllFormData(QRCardRefuelModel qrCardRefuelModel) {
        textViewDriverNamee.setText(qrCardRefuelModel.getDriverName());
        textViewdRiverNo.setText(qrCardRefuelModel.getDriverNumber());
        textViewVehicleNo.setText(qrCardRefuelModel.getVehicleNumber());
        textViewCustName.setText(qrCardRefuelModel.getCustName());
        textViewCustNo.setText(qrCardRefuelModel.getCustNumber());
        textViewFuelType.setText(qrCardRefuelModel.getFuelType());
        textViewProdName.setText(qrCardRefuelModel.getProdName());

        textViewReqQuantity.setText(qrCardRefuelModel.getFuelQuant());
        textViewReqAmount.setText(qrCardRefuelModel.getTotalPrice());

        textViewActualQuanNew.setText(qrCardRefuelModel.getActualCreditQuantity());
        textViewActualAmtNew.setText(qrCardRefuelModel.getCreditAmount());

       /* editTextActualQuantity.setText(qrCardRefuelModel.getActualCreditQuantity());
        editTextActualAmount.setText(qrCardRefuelModel.getCreditAmount());*/
        //textViewTotalPrice.setText(qrCardRefuelModel.getTotalPrice());


        apiCallForGetTodaysFuelPrice(ManualEntryFormActivity.this, qrCardRefuelModel.getFuelProdId());


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(ManualEntryFormActivity.this, ArraivalListActivity.class);
                startActivity(intent);
                break;

            case R.id.imageview_goto_home:
                Intent intent1 = new Intent(ManualEntryFormActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;



       /*     case R.id.button_submitm:

                String fuelRateText = textViewFuelPrice.getText().toString();

                if (fuelRateText != null && !fuelRateText.equals("null") && !fuelRateText.equals("undefined") && !fuelRateText.isEmpty()) {
                    if (radioActualQuantity.isChecked() || radioActualAmount.isChecked()) {

                        String acQuan = editTextActualQuantity.getText().toString();
                        String acAmt = editTextActualAmount.getText().toString();

                        if (!acQuan.isEmpty() && !acAmt.isEmpty()) {
                            String message = "Are you sure want to confirm Update Values?";
                            sendProperMessageForSubmission(message);
                        } else {
                            Toast.makeText(this, "Actual Quantity and Actual Amount must not be empty!", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        String message = "It seems Actual Quantity and Actual Amount not selected. Are you sure want to proceed with requested values?";
                        sendProperMessageForSubmission(message);
                    }

                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ManualEntryFormActivity.this);
                    builder.setTitle("Can't Proceed !");
                    builder.setMessage("Fuel price not assigned. Please set fuel price first. Please contact manager")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });
                   *//* builder.setMessage("Fuel price not assigned. Please set fuel price first.")
                            .setCancelable(false)
                            .setPositiveButton("Set Price", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(ManualEntryFormActivity.this, SetFuelPriceActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });*//*

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
                break;
*/
       /*     case R.id.radio_actual_quan:
                radioActualAmount.setChecked(false);
                editTextActualAmount.setEnabled(false);
                imageViewCalcAmt.setVisibility(View.VISIBLE);

                if (radioActualQuantity.isChecked()) {
                    editTextActualQuantity.setEnabled(true);
                    editTextActualAmount.setEnabled(false);
                    editTextActualAmount.setText("");
                    editTextActualQuantity.setText("");
                    editTextActualAmount.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                    editTextActualQuantity.setBackgroundColor(Color.TRANSPARENT);
                    editTextActualAmount.setHint("Actual Amount");
                    imageViewCalcQuantity.setVisibility(View.INVISIBLE);
                    //editTextActualQuantity.setHint("Enter Actual Quantity");


                }

                break;

            case R.id.radio_actual_amount:
                radioActualQuantity.setChecked(false);
                imageViewCalcQuantity.setVisibility(View.VISIBLE);

                if (radioActualAmount.isChecked()) {
                    editTextActualAmount.setEnabled(true);
                    editTextActualQuantity.setEnabled(false);
                    editTextActualQuantity.setText("");
                    editTextActualAmount.setText("");
                    editTextActualQuantity.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                    editTextActualAmount.setBackgroundColor(Color.TRANSPARENT);
                    editTextActualQuantity.setHint("Actual Quantity");
                    // editTextActualAmount.setHint("Enter Actual Amount");
                    imageViewCalcAmt.setVisibility(View.INVISIBLE);


                }


                break;

            case R.id.imageview_calc_amt:
                String aqText = editTextActualQuantity.getText().toString();

                String fuelPriceText = textViewFuelPrice.getText().toString();

                if (fuelPriceText != null && !fuelPriceText.isEmpty() && !fuelPriceText.equals("0")) {
                    if (!aqText.isEmpty()) {
                        double actualQuantity = Double.parseDouble(editTextActualQuantity.getText().toString());

                        double fuelPrice = Double.parseDouble(textViewFuelPrice.getText().toString());
                        double actualAmount = fuelPrice * actualQuantity;

                        String actualAmountValue = "" + String.format("%.2f", actualAmount);
                        editTextActualAmount.setText(actualAmountValue);
                    }

                }

                break;

            case R.id.imageview_calc_quan:
                String amText = editTextActualAmount.getText().toString();


                String fuelPriceText1 = textViewFuelPrice.getText().toString();
                if (fuelPriceText1 != null && !fuelPriceText1.isEmpty() && !fuelPriceText1.equals("0")) {

                    if (!amText.isEmpty()) {
                        double actualAmount = Double.parseDouble(editTextActualAmount.getText().toString());

                        double fuelPrice1 = Double.parseDouble(textViewFuelPrice.getText().toString());
                        double actualQuantity1 = actualAmount / fuelPrice1;
                        String actualQuantityValue = "" + String.format("%.2f", actualQuantity1);

                        editTextActualQuantity.setText(actualQuantityValue);
                    }

                    break;

                }*/
        }
    }

    private void sendProperMessageForSubmission(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManualEntryFormActivity.this);

        builder.setTitle("Confirm Update !");
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // apiCallForUpdateFuelCreditReqByfuelCreditId();

                    }
                })
                .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        //Creating dialog box

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


    }


    /* private void apiCallForUpdateFuelCreditReqByfuelCreditId() {
         String fuelCreditId = commonCodeManager.getFuelCreditId(ManualEntryFormActivity.this);

         // String totalPrice = textViewTotalPrice.getText().toString();
         String[] dealerDetails = commonCodeManager.getDealerDetails(ManualEntryFormActivity.this);


         UpdateFuelCreditRequest upr = new UpdateFuelCreditRequest(fuelCreditId,
                 commonTaskManager.getCurrentDateNew(),
                 commonTaskManager.getCurrentTime(),
                 editTextActualAmount.getText().toString(),
                 "COMPLETE", dealerDetails[0],
                 editTextActualQuantity.getText().toString(),
                 textViewFuelPrice.getText().toString());

         final ProgressDialog progressDialog = new ProgressDialog(ManualEntryFormActivity.this, R.style.MyAlertDialogStyle);
         progressDialog.setMessage(getResources().getString(R.string.sending_request));
         progressDialog.setCancelable(false);
         progressDialog.setCanceledOnTouchOutside(false);
         progressDialog.show();

         GetDataService service = RetrofitClientInstance.getRetrofitInstance(ManualEntryFormActivity.this).create(GetDataService.class);
         Call<JsonObject> call = service.updateFuelCreditReqByfuelCreditId(upr);
         call.enqueue(new Callback<JsonObject>() {
             @Override
             public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 final String jsonData = response.body().toString();
                 Log.d(TAG, "onResponse:apiCallForUpdateFuelCreditReqByfuelCreditId :  " + jsonData);
                 try {
                     JSONObject mainObject = new JSONObject(jsonData);
                     commonTaskManager.dismissProgressDialog(ManualEntryFormActivity.this, progressDialog);
                     final String status = mainObject.getString("status");
                     final String message = mainObject.getString("msg");

                     if (status.equals("OK")) {
                         backToRefuelArraivalActivity(message);
                     }
                 } catch (Exception e) {
                     commonTaskManager.dismissProgressDialog(ManualEntryFormActivity.this, progressDialog);

                 }
             }

             @Override
             public void onFailure(Call<JsonObject> call, Throwable t) {
                 commonTaskManager.dismissProgressDialog(ManualEntryFormActivity.this, progressDialog);

             }

         });
     }
 */
    private void backToRefuelArraivalActivity(String message) {

        Intent intent = new Intent(ManualEntryFormActivity.this, ArraivalListActivity.class);
        startActivity(intent);
    }

    private void sendDataToTheManualFormActivityForAmt(String reqAmount, String fuelRate) {
        textViewActualAmt.setText(reqAmount);
    }

    private void sendDataToTheManualFormActivity(String reqQuantity, String fuelRate) {
        Log.d(TAG, "sendDataToTheManualFormActivity:reqQuantity : " + reqQuantity);
        textViewQuantity.setText(reqQuantity);

        if (fuelRate != null && !fuelRate.equals("null") && !fuelRate.isEmpty()) {
            Log.d(TAG, "sendDataToTheManualFormActivity: fuel rate not is null");
            Double reqQuan = Double.parseDouble(reqQuantity);
            Double fuelRatee = Double.parseDouble(fuelRate);

            Double totalPrice = reqQuan * fuelRatee;
            String totleUpdatePrice = String.valueOf(totalPrice);
            textViewActualAmt.setText(totleUpdatePrice);
        } else {
            Log.d(TAG, "sendDataToTheManualFormActivity: rate is  null");
            Toast.makeText(this, "Fuel rate is null", Toast.LENGTH_SHORT).show();

            Double reqQuan = Double.parseDouble(reqQuantity);
            Double fuelRatee = Double.parseDouble("80");

            Double totalPrice = reqQuan * fuelRatee;
            String totleUpdatePrice = String.valueOf(totalPrice);
            //textViewTotalPrice.setText(totleUpdatePrice);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManualEntryFormActivity.this, ArraivalListActivity.class);
        startActivity(intent);
    }
}
