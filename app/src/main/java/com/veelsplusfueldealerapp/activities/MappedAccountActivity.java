package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.FuelPriceListAdapter;
import com.veelsplusfueldealerapp.adapters.MappedCorporateAccountsAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.FuelPriceModel;
import com.veelsplusfueldealerapp.models.MappedCorporatesModel;
import com.veelsplusfueldealerapp.models.NewCreditRequestModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MappedAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MappedAccountActivity";
    RecyclerView recyclerViewMappedAccs;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    MappedCorporatesModel mappedCorporatesModel;
    NewCreditRequestModel newModel;
    Spinner spinnerSelectCorp;
    AutoCompleteTextView autotvCorpNames;
    String[] custDetailsArr;
    EditText corpNameTv;
    TextView textviewTotalPendingAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapped_account);


        //this functionality for mapped corporates only
        initUI();
    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager();
        commonTaskManager = new CommonTaskManager(MappedAccountActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_mapped_acc);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Mapped Corporates");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        spinnerSelectCorp = findViewById(R.id.spinner_select_corp);
        textviewTotalPendingAmount = findViewById(R.id.textview_total_pending_amount);

        recyclerViewMappedAccs = findViewById(R.id.recyclerview_mapped_accs);

        autotvCorpNames = findViewById(R.id.autotv_corp_names);
        corpNameTv = findViewById(R.id.corp_name_tv);


    }

    @Override
    protected void onResume() {
        super.onResume();
        apiCallForGetAllMappedCorporates(MappedAccountActivity.this);
        apiCallForCustomerDropdown(MappedAccountActivity.this);

        //getTypedText();
    }

    private void getTypedText() {

        autotvCorpNames.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                Log.d(TAG, "onTextChanged: s : " + s);
                if (s.length() >= 3) {
                    //apiCallForCustomerDropdown(MappedAccountActivity.this, s.toString());
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent1 = new Intent(MappedAccountActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void apiCallForGetAllMappedCorporates(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};


        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        final List<MappedCorporatesModel> mappedCorpsDetailsList = new ArrayList<>();

        Log.d(TAG, "apiCallForGetAllMappedCorporates: " + dealerDetails[1]);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllCreditAccByDealerId(dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllMappedCorporates :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String maxCreditAmount = duObject.getString("maxCreditAmount");//creditLimit
                                String totalInvCreditAmt = duObject.getString("totalInvCreditAmt");//total billed amt
                                String totalInvPaidAmt = duObject.getString("totalInvPaidAmt");//total paid amount
                                // String invoiceOutstand = duObject.getString("invoiceOutstand");//pendingAmt

                                double finalInvCreditAmt;
                                double finalInvPaidAmt = 0;
                                double finalPendingAmt;

                                if (totalInvCreditAmt != null && !totalInvCreditAmt.isEmpty() && !totalInvCreditAmt.equals("null") && !totalInvCreditAmt.equals("undefined")) {

                                    finalInvCreditAmt = Double.parseDouble(totalInvCreditAmt);
                                 /*
                                    if (totalInvPaidAmt != null && !totalInvPaidAmt.isEmpty() && !totalInvPaidAmt.equals("null") && !totalInvPaidAmt.equals("undefined")) {

                                        finalInvPaidAmt = Double.parseDouble(totalInvPaidAmt);
                                    } else {
                                        finalInvPaidAmt = 0;
                                    }*/
                                } else {
                                    finalInvCreditAmt = 0;
                                }
                                if (totalInvCreditAmt.startsWith("-")) {
                                    finalInvCreditAmt = finalInvCreditAmt * (-1);

                                    String pendingAmtDisplay = String.format("%.2f", finalInvCreditAmt);
                                    String pendingAmt = String.format("%.2f", finalInvCreditAmt) + " CR";

                                    Log.d(TAG, "onResponse: pendingAmt : " + pendingAmt);

                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, pendingAmt, pendingAmtDisplay);

                                } else {
                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, String.format("%.2f", finalInvCreditAmt),
                                                    String.format("%.2f", finalInvCreditAmt));


                                }

                             /*   double finalInvCreditAmt;
                                double finalInvPaidAmt = 0;
                                double finalPendingAmt;

                                if (totalInvCreditAmt != null && !totalInvCreditAmt.isEmpty() && !totalInvCreditAmt.equals("null") && !totalInvCreditAmt.equals("undefined")) {
                                    finalInvCreditAmt = Double.parseDouble(totalInvCreditAmt);
                                    if (totalInvPaidAmt != null && !totalInvPaidAmt.isEmpty() && !totalInvPaidAmt.equals("null") && !totalInvPaidAmt.equals("undefined")) {
                                        finalInvPaidAmt = Double.parseDouble(totalInvPaidAmt);
                                    } else {
                                        finalInvPaidAmt = 0;
                                    }
                                } else {
                                    finalInvCreditAmt = 0;
                                }

                                if (totalInvCreditAmt.startsWith("-")) {
                                    finalInvCreditAmt = finalInvCreditAmt * (-1);
                                    String pendingAmt = String.format("%.2f", finalInvCreditAmt) + " CR";
                                    Log.d(TAG, "onResponse: pendingAmt : " + pendingAmt);

                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, pendingAmt, String.format("%.2f", finalInvCreditAmt));

                                } else {
                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, totalInvCreditAmt, totalInvCreditAmt);


                                }*/

                                mappedCorpsDetailsList.add(mappedCorporatesModel);


                            }

                            fillMappedCorporatesDetails(mappedCorpsDetailsList);
                        } else {
                            //no data found
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

    private void fillMappedCorporatesDetails(List<MappedCorporatesModel> mappedCorpsDetailsList) {
        MappedCorporateAccountsAdapter mappedDetails = new MappedCorporateAccountsAdapter(MappedAccountActivity.this, mappedCorpsDetailsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewMappedAccs.setLayoutManager(mLayoutManager);
        recyclerViewMappedAccs.setItemAnimator(new DefaultItemAnimator());
        mappedDetails.notifyDataSetChanged();
        recyclerViewMappedAccs.setAdapter(mappedDetails);

        double totalPendingAmt = 0;
        for (int i = 0; i < mappedCorpsDetailsList.size(); i++) {
            MappedCorporatesModel mappedCorporatesModel = mappedCorpsDetailsList.get(i);
            String pendingAmt = mappedCorporatesModel.getPendingAmt();

            if (pendingAmt != null && !pendingAmt.isEmpty() && !pendingAmt.equals("null") && !pendingAmt.equals("undefined") && !pendingAmt.contains("CR")) {
                totalPendingAmt = totalPendingAmt + Double.parseDouble(pendingAmt);
            }
        }

        Log.d(TAG, "fillMappedCorporatesDetails: totalPendingAmt : " + totalPendingAmt);
        textviewTotalPendingAmount.setText("");
        String amount = "Total Pending Amount = " + String.format("%.2f", totalPendingAmt) + " Rs";
        textviewTotalPendingAmount.setText(amount);

    }

    private void apiCallForCustomerDropdown(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        String[] details = commonCodeManager.getDealerDetails(context);
        Log.d(TAG, "apiCallForCustomerDropdown:dealerid :  " + details[1]);

        final List<NewCreditRequestModel> custDetailsList = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllCreditAccByDealerId(details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForCustomerDropdown :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);
                        //need to check
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        Log.d(TAG, "onResponse:dataArray :  " + dataArray.length());
                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String fuelDealerCustomerMapId = duObject.getString("fuelDealerCustomerMapId");
                                String fuelCorporateId = duObject.getString("fuelCorporateId");
                                if (i == 0) {
                                    newModel = new NewCreditRequestModel("", "", "Search Corporate");
                                    custDetailsList.add(newModel);

                                }
                                if (i == 0) {
                                    newModel = new NewCreditRequestModel("", "", "All Corporates");
                                    custDetailsList.add(newModel);
                                }
                                newModel = new NewCreditRequestModel(fuelDealerCustomerMapId, fuelCorporateId, companyName);


                                custDetailsList.add(newModel);
                                //commonCodeManager.saveFuelCreditDetails(context, fuelDealerCustomerMapId,fuelCorporateId);

                            }
                            //updateUIAsPerCustomerAPI(custDetailsList);

                            updateAutotv(custDetailsList);

                        } else {
                            // show msg that no mapped customers found
                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    String dialogMessage = "No mapped customers found";

                                    builder.setTitle("Note");
                                    builder.setMessage(dialogMessage)
                                            .setCancelable(false)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent = new Intent(context, ArraivalListActivity.class);
                                                    startActivity(intent);


                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();

                                                }
                                            });

                                    final AlertDialog alertDialog = builder.create();
                                    alertDialog.setCancelable(true);
                                    alertDialog.show();

                                }
                            });
                        }


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Details Not Found");

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


    }

    private void updateAutotv(final List<NewCreditRequestModel> custDetailsList) {


        custDetailsArr = new String[custDetailsList.size()];

        for (int i = 0; i < custDetailsList.size(); i++) {
            NewCreditRequestModel newCreditRequestModel = custDetailsList.get(i);
            custDetailsArr[i] = newCreditRequestModel.getCompanyName();
        }

        for (int i = 0; i < custDetailsArr.length; i++) {

            Log.d(TAG, "Details arr  : " + i + " , Name : " + custDetailsArr[i]);

        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, R.layout.layout_autotv_item, custDetailsArr);
        autotvCorpNames.setThreshold(1);
        autotvCorpNames.setAdapter(adapter);
        autotvCorpNames.setTextColor(getResources().getColor(R.color.colorGreen1));

  /* NewCreditRequestModel newCreditRequestModel = custDetailsList.get(position);
                    Log.d(TAG, "onItemClick: FuelDealerCustomerMapId autotv2 : " + newCreditRequestModel.getFuelDealerCustomerMapId());
                    apiCallForGetDetailsOfSelectedCorporate(MappedAccountActivity.this, newCreditRequestModel.getFuelDealerCustomerMapId());*/

        autotvCorpNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick:getItemAtPosition :" + i);

                String selectedValue = (String) adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemClick: selectedValue :" + selectedValue);
                autotvCorpNames.setText(selectedValue);

                if (selectedValue.contains("Search Corporate")) {

                } else if (selectedValue.contains("All Corporates")) {
                    apiCallForGetAllMappedCorporates(MappedAccountActivity.this);

                } else {
                    int pos = Arrays.asList(custDetailsArr).indexOf(selectedValue);
                    Log.d(TAG, "onItemClick: pos: " + pos);

                    NewCreditRequestModel newCreditRequestModel = custDetailsList.get(pos);
                    Log.d(TAG, "onItemClick: FuelDealerCustomerMapId : " + newCreditRequestModel.getFuelDealerCustomerMapId());
                    //last parameter for search with filter

                    apiCallForGetDetailsOfSelectedCorporate(MappedAccountActivity.this, newCreditRequestModel.getFuelDealerCustomerMapId());


                }
            }
        });
    }

    private void updateUIAsPerCustomerAPI(final List<NewCreditRequestModel> custDetailsList) {
        String[] custDetailsArr = new String[custDetailsList.size()];

        for (int i = 0; i < custDetailsList.size(); i++) {
            NewCreditRequestModel newCreditRequestModel = custDetailsList.get(i);
            custDetailsArr[i] = newCreditRequestModel.getCompanyName();
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(MappedAccountActivity.this, android.R.layout.simple_spinner_item, custDetailsArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectCorp.setAdapter(arrayAdapter1);

        spinnerSelectCorp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFuelType = (String) spinnerSelectCorp.getSelectedItem();
                //spinnerErrorText = (TextView) spinnerSelectCust.getSelectedView();

                if (selectedFuelType.contains("Search Corporate")) {

                } else if (selectedFuelType.contains("All Corporates")) {
                    apiCallForGetAllMappedCorporates(MappedAccountActivity.this);

                } else {

                    Log.d(TAG, "onItemSelected:updateUIAsPerCustomerAPI :  " + selectedFuelType);
                /*    NewCreditRequestModel newCreditRequestModel = custDetailsList.get(position);

                    commonCodeManager.saveFuelCreditDetails(Objects.requireNonNull(getActivity()),
                            newCreditRequestModel.getFuelDealerCustomerMapId(),
                            newCreditRequestModel.getFuelCorporateId());

                    apiCallForGetCorporateInfoByfuelDealerCustomerMapId(getActivity(), newCreditRequestModel.getFuelDealerCustomerMapId());
*/
                    NewCreditRequestModel newCreditRequestModel = custDetailsList.get(position);
                    Log.d(TAG, "onItemSelected: FuelDealerCustomerMapId : " + newCreditRequestModel.getFuelDealerCustomerMapId());
                    apiCallForGetDetailsOfSelectedCorporate(MappedAccountActivity.this, newCreditRequestModel.getFuelDealerCustomerMapId());


                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void apiCallForGetDetailsOfSelectedCorporate(final Context context, String fuelDealerCustomerMapId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};

        final List<MappedCorporatesModel> mappedCorpsDetailsList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.searchByCorporateName(fuelDealerCustomerMapId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetDetailsOfSelectedCorporate :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String maxCreditAmount = duObject.getString("maxCreditAmount");
                                String totalInvCreditAmt = duObject.getString("totalInvCreditAmt").trim();//total billed amt
                                String totalInvPaidAmt = duObject.getString("totalInvPaidAmt");//total paid amount
                                String invoiceOutstand = duObject.getString("invoiceOutstand");
                                String mappingStatus = duObject.getString("mappingStatus").toLowerCase().trim();


                                double finalInvCreditAmt;
                                double finalInvPaidAmt = 0;
                                double finalPendingAmt;

                                if (totalInvCreditAmt != null && !totalInvCreditAmt.isEmpty() && !totalInvCreditAmt.equals("null") && !totalInvCreditAmt.equals("undefined")) {
                                    String invCreditAmtForDec = String.format("%.2f", Double.parseDouble(totalInvCreditAmt));

                                    finalInvCreditAmt = Double.parseDouble(invCreditAmtForDec);
                                    if (totalInvPaidAmt != null && !totalInvPaidAmt.isEmpty() && !totalInvPaidAmt.equals("null") && !totalInvPaidAmt.equals("undefined")) {
                                        finalInvPaidAmt = Double.parseDouble(totalInvPaidAmt);
                                    } else {
                                        finalInvPaidAmt = 0;
                                    }
                                } else {
                                    finalInvCreditAmt = 0;
                                }

                                if (totalInvCreditAmt.startsWith("-")) {
                                    String invCreditAmtForDec = String.format("%.2f", Double.parseDouble(totalInvCreditAmt));

                                    finalInvCreditAmt = Double.parseDouble(invCreditAmtForDec) * (-1);

                                    String pendingAmt = String.format("%.2f", finalInvCreditAmt) + " CR";
                                    Log.d(TAG, "onResponse: pendingAmt : " + pendingAmt);

                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, pendingAmt, String.format("%.2f", finalInvCreditAmt));

                                } else {
                                    mappedCorporatesModel = new MappedCorporatesModel
                                            (companyName, maxCreditAmount, totalInvCreditAmt,
                                                    totalInvPaidAmt, String.format("%.2f", finalInvCreditAmt),
                                                    String.format("%.2f", finalInvCreditAmt));


                                }

                                mappedCorpsDetailsList.add(mappedCorporatesModel);

                            }

                            fillMappedCorporatesDetails(mappedCorpsDetailsList);
                        } else {
                            //no data found
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
}