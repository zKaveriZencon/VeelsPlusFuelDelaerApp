package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.QRCardRefuelModel;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewCreditRequestDetailsActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "ViewCreditRequestDetail";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    TextView textViewDriverNamee, textViewdRiverNo, textViewVehicleNo, textViewCustName,
            textViewCustNo, textViewFuelType, textViewProdName, textViewQuantity,
            textViewReqAmount, textViewReqQuantity,
            textViewFuelPrice, textviewVehicleNoLabel, labelCustName, labelCustNo;
    MaterialButton materialButton;
    ImageView imageViewBack;
    String reqQuantity, reqAmount;
    LinearLayout layoutParentManual;
    TextView textViewActualQuanNew, textViewActualAmtNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credit_request_details);
        commonCodeManager = new CommonCodeManager(ViewCreditRequestDetailsActivity.this);
        commonTaskManager = new CommonTaskManager(ViewCreditRequestDetailsActivity.this);

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

        textviewVehicleNoLabel = findViewById(R.id.label_vehicle_no_me);


        labelCustName = findViewById(R.id.label_custName_per);
        labelCustNo = findViewById(R.id.label_cust_no);


        materialButton = findViewById(R.id.button_submitm);
        materialButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();


        boolean result = commonTaskManager.compareDates(ViewCreditRequestDetailsActivity.this);

        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(ViewCreditRequestDetailsActivity.this, ViewCreditRequestDetailsActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String[] details = commonCodeManager.getDetailsForCreditReq(ViewCreditRequestDetailsActivity.this);
            if (details[1] != null && !details[1].isEmpty() && details[1].equals("forview")) {
                materialButton.setVisibility(View.INVISIBLE);


            } else {
                // materialButton.setText("UPDATE");

            }
            String fuelCreditId = commonCodeManager.getFuelCreditId(ViewCreditRequestDetailsActivity.this);


            if (fuelCreditId != null && !fuelCreditId.isEmpty()) {
                apiCallForGetScannedCardDetails(ViewCreditRequestDetailsActivity.this, fuelCreditId);
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
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(ViewCreditRequestDetailsActivity.this, EarningsReportActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ViewCreditRequestDetailsActivity.this, EarningsReportActivity.class);
        startActivity(intent);
    }
}
