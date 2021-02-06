package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DecimalDigitsInputFilter;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.FuelPriceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetFuelPriceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SetFuelPriceActivity";
    Spinner spinnerSelectProduct;
    String selectedProduct;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    FuelPriceModel fuelPriceModel;
    MaterialButton buttonSetPrice;
    EditText editTextSetPrice;
    int i;
    String msg;
    private TextView spinnerErrorText;
    private List<String> dealerTankMapList;

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(SetFuelPriceActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(SetFuelPriceActivity.this, SetFuelPriceActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_fuel_price);

        initUI();
    }

    private void initUI() {

        View toolbar = findViewById(R.id.layout_toolbar_setfuelprice);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Set Fuel Price");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);

        commonCodeManager = new CommonCodeManager(SetFuelPriceActivity.this);
        commonTaskManager = new CommonTaskManager(SetFuelPriceActivity.this);

        spinnerSelectProduct = findViewById(R.id.spinner_select_productP);
        buttonSetPrice = findViewById(R.id.button_set_price);
        editTextSetPrice = findViewById(R.id.edittext_set_price);
        editTextSetPrice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});


        buttonSetPrice.setOnClickListener(this);


        apiCallForGetProductNames(SetFuelPriceActivity.this);
    }

    private void apiCallForGetProductNames(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<FuelPriceModel> prodDetails = new ArrayList<>();
        String[] dealerDetails = commonCodeManager.getDealerDetails(context);


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelProductIdByDealerId(dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetProductNames :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mainObj = dataArray.getJSONObject(i);
                            String productCategory = mainObj.getString("productCategory");
                            String productName = mainObj.getString("productName");
                            String fuelProductsId = mainObj.getString("fuelProductsId");
                            String product = productCategory + " " + productName;

                            if (i == 0) {
                                fuelPriceModel = new FuelPriceModel("Select Product", "");
                                prodDetails.add(fuelPriceModel);
                            }
                            fuelPriceModel = new FuelPriceModel(product, fuelProductsId);
                            prodDetails.add(fuelPriceModel);

                        }


                        fillProductDropdown(prodDetails);
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

    private void fillProductDropdown(final List<FuelPriceModel> prodDetails) {

        String[] products = new String[prodDetails.size()];

        for (int i = 0; i < prodDetails.size(); i++) {
            FuelPriceModel fuelPriceModel = prodDetails.get(i);
            products[i] = fuelPriceModel.getProductName();
        }


        ArrayAdapter arrayAdapter1 = new ArrayAdapter(SetFuelPriceActivity.this, android.R.layout.simple_spinner_item, products);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectProduct.setAdapter(arrayAdapter1);
        spinnerSelectProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedProduct = (String) spinnerSelectProduct.getSelectedItem();
                spinnerErrorText = (TextView) spinnerSelectProduct.getSelectedView();

                if (selectedProduct.contains("Select Product")) {

                } else {
                    FuelPriceModel fuelPriceModel = prodDetails.get(position);
                    apiCallForGetDealerTankMap(SetFuelPriceActivity.this, fuelPriceModel.getProductId());

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void apiCallForGetDealerTankMap(final Context context, final String productId) {
        Log.d(TAG, "apiCallForGetDealerTankMap:productId :  " + productId);

        dealerTankMapList = new ArrayList<>();

        final String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getDealerTankMapCodeByfuelProductIdDealerId(dealerDetails[1], productId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetDealerTankMap :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);

                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {

                            JSONObject mainObj = dataArray.getJSONObject(i);
                            String dealerTankMapCode = mainObj.getString("dealerTankMapCode");
                            dealerTankMapList.add(dealerTankMapCode);
                            Log.d(TAG, "onResponse: dealerTankMapCode : " + dealerTankMapCode);
                            for (int j = 0; j < dealerTankMapList.size(); j++) {
                                Log.d(TAG, "apiCallForSetFuelPrice: dealertankmapfromlist : " + dealerTankMapList.get(j));
                            }
                        }
                        apiCallForGetTankCount(context, dealerTankMapList, dealerDetails[1], productId);

                    }


                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });
    }

    private void apiCallForGetTankCount(final Context context,
                                        final List<String> dealerTankMapList, String dealerId,
                                        String productId) {

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getTankCountByfuelProductIdDealerId(dealerId, productId);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetTankCount :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);

                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject mainObj = dataArray.getJSONObject(0);
                        String TotalTank = mainObj.getString("TotalTank");

                        commonCodeManager.savePriceEssentials(context, "", TotalTank);

                    }


                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.showToast(context, context.getResources().getString(R.string.unable_to_connect));
            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(SetFuelPriceActivity.this, FuelPriceListActivity.class);
                startActivity(intent);
                break;
            case R.id.button_set_price:
                if (areValidate()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(SetFuelPriceActivity.this);

                    builder.setTitle("Confirm Fuel Price !");
                    builder.setMessage("Please confirm fuel price before submit.")
                            .setCancelable(false)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    apiCallForSetFuelPrice(SetFuelPriceActivity.this);

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
                break;

            case R.id.imageview_goto_home:
                Intent intent1 = new Intent(SetFuelPriceActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SetFuelPriceActivity.this, FuelPriceListActivity.class);
        startActivity(intent);
    }

    private void apiCallForSetFuelPrice(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String[] dealerDetails = commonCodeManager.getDealerDetails(context);
        String[] priceEssen = commonCodeManager.getPriceEssentials(context);

        Log.d(TAG, "apiCallForSetFuelPrice:dealerTankMapList size :  " + dealerTankMapList.size());
        Log.d(TAG, "apiCallForSetFuelPrice:tank count:  " + priceEssen[1]);


        final int tankCount = Integer.parseInt(priceEssen[1]);

        if (dealerTankMapList.size() > 0) {

            for (i = 0; i < tankCount; i++) {
                Log.d(TAG, "apiCallForSetFuelPrice: before api i:" + i);
                FuelPriceModel fuelPriceModel = new FuelPriceModel(editTextSetPrice.getText().toString(), dealerDetails[2],
                        "", commonTaskManager.getCurrentDateNew(), dealerTankMapList.get(i));

                GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
                Call<JsonObject> call = service.setFuelPrice(fuelPriceModel);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        final String jsonData = response.body().toString();
                        Log.d(TAG, "onResponse:apiCai:llForSetFuelPrice :  " + jsonData);
                        try {
                            JSONObject mainObject = new JSONObject(jsonData);

                            final String status = mainObject.getString("status");
                            msg = mainObject.getString("msg");

                            if (status.equals("OK")) {
                                JSONArray dataArray = mainObject.getJSONArray("data");
                                Log.d(TAG, "onResponse: i before : " + i);

                             /*   if (i == tankCount - 1) {
                                    Log.d(TAG, "onResponse: i : " + i);
                                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                                    goToThePriceListActivity(msg);
                                }*/
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
            Log.d(TAG, "apiCallForSetFuelPrice: msg : " + msg);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    goToThePriceListActivity(msg);

                }
            }, 500);
        }
    }

    private void goToThePriceListActivity(String message) {
        commonCodeManager.saveFuelPriceDetails(SetFuelPriceActivity.this, "setprice", message);

        Intent intent = new Intent(SetFuelPriceActivity.this, FuelPriceListActivity.class);
        startActivity(intent);
    }

    private boolean areValidate() {
        boolean result = false;
        String selectedProduct = (String) spinnerSelectProduct.getSelectedItem();

        if (selectedProduct.contains("Select")) {
            result = false;
            spinnerErrorText.setError("Please Select Product");
        } else if (TextUtils.isEmpty(editTextSetPrice.getText().toString())) {
            result = false;
            editTextSetPrice.setError("Please Enter Price");
        } else {
            result = true;
        }
        return result;
    }


}