package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.DecimalDigitsInputFilter;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.DailyInventoryDetailsModel;
import com.veelsplusfueldealerapp.models.TankModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyStockReadingActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "DailyStockReadingActivi";
    Spinner spinnerSelectTank;
    CommonTaskManager commonTaskManager;
    String spinnerSelectedTank;
    EditText editTextOpenDipRead, editTextOpenDipStock, editTextCloseDipRead,
            editTextCloseDipStock, editTextOpenDensity, editTextCloseDensity;
    TextView textViewProduct, textViewPageTitle, textViewDate, textViewtime,
            textViewSelTank, spinnerErrorText;
    MaterialButton buttonSubmit, buttonUpdate;
    CommonCodeManager commonCodeManager;
    DatabaseHelper databaseHelper;
    RelativeLayout layoutParent;
    TextView textViewNote;
    DailyInventoryDetailsModel dm = null;
    DailyInventoryDetailsModel dm1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_stock_reading);

        initUI();
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(DailyStockReadingActivity.this);
        commonCodeManager = new CommonCodeManager(DailyStockReadingActivity.this);
        databaseHelper = new DatabaseHelper(DailyStockReadingActivity.this);


        View toolbar = findViewById(R.id.layout_toolbar_dim);
        textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Add Stock Details");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        spinnerSelectTank = findViewById(R.id.spinner_select_tank_aim);
        textViewProduct = findViewById(R.id.textview_product_dim);


        editTextOpenDipRead = findViewById(R.id.edittext_before_dip_reading_dim);
        editTextOpenDipStock = findViewById(R.id.edittext_before_dip_stock_dim);
        editTextCloseDipRead = findViewById(R.id.edittext_end_dip_reading_dim);
        editTextCloseDipStock = findViewById(R.id.edittext_after_dip_stock_dim);
        editTextOpenDensity = findViewById(R.id.edittext_open_density_dim);
        editTextCloseDensity = findViewById(R.id.edittext_close_density_dim);

        setTwoDigitFilters();

        textViewDate = findViewById(R.id.textview_date_label_dsr);
        textViewtime = findViewById(R.id.textview_time_label_dsr);
        textViewSelTank = findViewById(R.id.textview_selected_tank);
        textViewNote = findViewById(R.id.textview_note_dsr);
        layoutParent = findViewById(R.id.layout_parent_dsr);
        buttonSubmit = findViewById(R.id.button_submit_dim);
        buttonUpdate = findViewById(R.id.button_update_dsu);

        buttonSubmit.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);


    }

    private void setTwoDigitFilters() {
        editTextOpenDipRead.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextOpenDipStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextCloseDipRead.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextCloseDipStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextOpenDensity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextCloseDensity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean result = commonTaskManager.compareDates(DailyStockReadingActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(DailyStockReadingActivity.this, DailyStockReadingActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {

            String noinfra = commonCodeManager.getNoInfraDetailsMessage(DailyStockReadingActivity.this);
            if (noinfra.equals("noinfra")) {
                buttonSubmit.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyStockReadingActivity.this);
                builder.setTitle("Note !");
                builder.setMessage("Unable to get proper Infra Setup data from server. Please check infra setup once or Logout from App and Login again.")

                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent(DailyStockReadingActivity.this, DailyStockReadingListActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
            String[] cardClickDetails = commonCodeManager.getDailyStockOnCardClick(DailyStockReadingActivity.this);
            String checkForUpdate = cardClickDetails[1];
            String fuelTankRefuelId = cardClickDetails[0];
            String status = cardClickDetails[2];


            Log.d(TAG, "onResume: checkForUpdate stock :" + checkForUpdate);

            if (checkForUpdate != null && checkForUpdate.equals("updatestock")) {

                if (status != null && status.equals("completed")) {
                    textViewNote.setVisibility(View.VISIBLE);
                    //for view only
                    Snackbar snackbar = Snackbar
                            .make(layoutParent, "Activity Completed !", Snackbar.LENGTH_SHORT);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params1.gravity = Gravity.TOP;
                    view.setLayoutParams(params1);
                    view.setBackgroundColor(getResources().getColor(R.color.colorCommonGreen));
                    snackbar.show();


                    textViewPageTitle.setText("Daily Stock Details");
                    textViewDate.setVisibility(View.INVISIBLE);
                    textViewtime.setVisibility(View.INVISIBLE);

                    editTextOpenDipRead.setEnabled(false);
                    editTextOpenDipStock.setEnabled(false);
                    editTextOpenDensity.setEnabled(false);
                    editTextCloseDipRead.setEnabled(false);
                    editTextCloseDipStock.setEnabled(false);
                    editTextCloseDensity.setEnabled(false);
                    buttonSubmit.setEnabled(false);
                    apiCallForGetDailyStockDetailsById(DailyStockReadingActivity.this,
                            fuelTankRefuelId, "completed");

                } else {
                    //update ui for update
                    textViewPageTitle.setText("Update Daily Stock Details");
                    buttonSubmit.setText("UPDATE");
                    editTextOpenDipRead.setEnabled(false);
                    editTextOpenDipStock.setEnabled(false);
                    editTextOpenDensity.setEnabled(false);

                    textViewDate.setText("");
                    textViewtime.setText("");

                    textViewDate.setText("Date : " + commonTaskManager.getCurrentDate()[0]);
                    textViewtime.setText("Time : " + commonTaskManager.getCurrentTime());


                    textViewPageTitle.setText("Update Stock Details");
                    apiCallForGetDailyStockDetailsById(DailyStockReadingActivity.this,
                            fuelTankRefuelId, "forupdate");
                }

            } else {
                //for new
                textViewDate.append(commonTaskManager.getCurrentDate()[0]);
                String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                textViewtime.append(currentTime1);

                Log.d(TAG, "onResume: checkForUpdate : " + checkForUpdate);
                String[] details = commonCodeManager.getDealerDetails(DailyStockReadingActivity.this);
                //textViewPageTitle.setText("Daily Stock Readings");
           /* apiCallForGetAllTanks(DailyStockReadingActivity.this,
                    details[1]);
            */

                fillTankDataFromLocal();
                String spinnerSelectedTank = (String) spinnerSelectTank.getSelectedItem();

                List<String> prodDetails = databaseHelper.getProductNameAndInfraIdByTankId(spinnerSelectedTank);
                if (prodDetails.size() > 0) {
                    textViewProduct.setText(prodDetails.get(1));

                }

            }
        }
    }

    private void fillTankDataFromLocal() {
        List<String> dusList = databaseHelper.getAllTanksFromInfraMapping();
        String[] allTanks = new String[dusList.size()];
        for (int i = 0; i < dusList.size(); i++) {
            allTanks[i] = dusList.get(i);
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(DailyStockReadingActivity.this, android.R.layout.simple_spinner_item, allTanks);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectTank.setAdapter(arrayAdapter1);
        spinnerSelectTank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedTank = (String) spinnerSelectTank.getSelectedItem();
                spinnerErrorText = (TextView) spinnerSelectTank.getSelectedView();

                if (spinnerSelectedTank.contains("Select Tank")) {

                } else {
                    List<String> prodDetails = databaseHelper.getProductNameAndInfraIdByTankId(spinnerSelectedTank);
                    textViewProduct.setText(prodDetails.get(1));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(DailyStockReadingActivity.this, DailyStockReadingListActivity.class);
                startActivity(intent);
                break;
            case R.id.button_submit_dim:
                commonTaskManager.hideKeyboard(DailyStockReadingActivity.this);

                getAllParams();
            case R.id.button_update_dsu:
                // commonTaskManager.hideKeyboard(DailyStockReadingActivity.this);

                // getAllParams();
                break;
        }
    }

    private void getAllParams() {
        String openDipRead = editTextOpenDipRead.getText().toString();
        String openDipStock = editTextOpenDipStock.getText().toString();
        String closeDipRead = editTextCloseDipRead.getText().toString();
        String closeDipstock = editTextCloseDipStock.getText().toString();
        String openDensity = editTextOpenDensity.getText().toString();
        String closeDensity = editTextCloseDensity.getText().toString();

        String[] cardDetails = commonCodeManager.getDailyStockOnCardClick(DailyStockReadingActivity.this);
        String checkForUpdate = cardDetails[1];
        String status = cardDetails[2];
        Log.d(TAG, "getAllParams: opendensity : " + openDensity);

        if (checkForUpdate != null && checkForUpdate.equals("updatestock")) {
            if (status != null && status.equals("completed")) {

                //if completed
                Log.d(TAG, "getAllParams: if completedd");
            } else {
                Log.d(TAG, "getAllParams: if for update");
                String refuelId = commonCodeManager.getFuelTankRefuelId(DailyStockReadingActivity.this);
                Log.d(TAG, "getAllParams: refuelId : " + refuelId);

                if (areValidateForUpdate()) {
                    dm1 = new DailyInventoryDetailsModel
                            (refuelId, openDipStock, closeDipstock, openDipRead,
                                    closeDipRead, commonTaskManager.getCurrentDateNew(),
                                    openDensity, closeDensity, commonTaskManager.getCurrentTime(),
                                    "COMPLETE");

                 /*   dm1 = new DailyInventoryDetailsModel
                            (refuelId, openDipRead, closeDipRead, openDipStock,
                                    closeDipstock, commonTaskManager.getCurrentDateNew(),
                                    openDensity, closeDensity, commonTaskManager.getCurrentTime(),
                                    "COMPLETE");;*/

                    AlertDialog.Builder builder = new AlertDialog.Builder(DailyStockReadingActivity.this);

                    builder.setTitle("Confirm Details !");
                    builder.setMessage("Please confirm details before submit by clicking 'Confirm' or 'Review' to review.")
                            .setCancelable(false)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    apiCallForUpdateDailyStockInventory(DailyStockReadingActivity.this, dm1);

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

            }
        } else {
            //if for new entry

            if (areValidateForNew()) {


                String[] details = commonCodeManager.getDealerDetails(DailyStockReadingActivity.this);

                String spinnerSelectedTank = (String) spinnerSelectTank.getSelectedItem();

                String[] dealerDetails = commonCodeManager.getDealerDetails(DailyStockReadingActivity.this);
                String infraMapID = databaseHelper.getInfraMapIdByTankNo(spinnerSelectedTank);
                Log.d(TAG, "getAllParams:infraMapID : " + infraMapID);
                String dealerTankMap = details[2] + spinnerSelectedTank;
                if (closeDipRead.isEmpty() || closeDipstock.isEmpty() || closeDensity.isEmpty()) {
                    //for stock check status false

                    dm = new DailyInventoryDetailsModel
                            (infraMapID, details[0], spinnerSelectedTank, openDipStock,
                                    closeDipRead, openDipRead, closeDipRead,
                                    commonTaskManager.getCurrentDateNew(),
                                    commonTaskManager.getCurrentDateNew(),
                                    openDensity, closeDensity, details[1],
                                    dealerTankMap, commonTaskManager.getCurrentTime(),
                                    commonTaskManager.getCurrentTime(), "PARTIAL");

                } else {
                    //for stock check status true
                    dm = new DailyInventoryDetailsModel
                            (infraMapID, details[0], spinnerSelectedTank, openDipStock,
                                    closeDipstock, openDipRead, closeDipRead,
                                    commonTaskManager.getCurrentDateNew(),
                                    commonTaskManager.getCurrentDateNew(),
                                    openDensity, closeDensity, details[1],
                                    dealerTankMap, commonTaskManager.getCurrentTime(),
                                    commonTaskManager.getCurrentTime(), "COMPLETE");


                }
                AlertDialog.Builder builder = new AlertDialog.Builder(DailyStockReadingActivity.this);

                builder.setTitle("Confirm Details !");
                builder.setMessage("Please confirm details before submit by clicking 'Confirm' or 'Review' to review.")
                        .setCancelable(false)
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                apiCallForAddDailyInventoryDetails(DailyStockReadingActivity.this, dm);

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
        }


    }

    private void apiCallForUpdateDailyStockInventory(final Context context, DailyInventoryDetailsModel dailyInventoryDetailsModel) {
        Log.d(TAG, "apiCallForUpdateTankFuelInventory: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateTankFuelInventoryDetails(dailyInventoryDetailsModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateTankFuelInventory :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        backToStockList(message);
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

    private void backToStockList(String message) {
        String refuelId = commonCodeManager.getFuelTankRefuelId(DailyStockReadingActivity.this);
        //   apiCallForUpdateStockStatus(DailyStockReadingActivity.this, refuelId, "True");
        Intent intent = new Intent(DailyStockReadingActivity.this, DailyStockReadingListActivity.class);
        startActivity(intent);
    }

    private void apiCallForUpdateStockStatus(final Context context, String refuelId, String status) {
        Log.d(TAG, "apiCallForUpdateInventoryStatus: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateStatusForFuelInventory(refuelId, status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateInventoryStatus :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
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

    private void apiCallForAddDailyInventoryDetails(final Context context, DailyInventoryDetailsModel dm) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<DailyInventoryDetailsModel> dailyInventoryDetailsModels = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addDailyFuelInventoryDetails(dm);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddDailyInventoryDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        goToTheDailyStockListActivity();
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

    private void goToTheDailyStockListActivity() {
        Log.d(TAG, "goToTheDailyStockListActivity: ");
        Intent intent = new Intent(DailyStockReadingActivity.this, DailyStockReadingListActivity.class);
        startActivity(intent);
    }

    private void apiCallForGetDailyStockDetailsById(final Context context,
                                                    final String fuelTankRefuelId,
                                                    String checkStatus) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<String> stockDetails = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getInventoryDetailsByFuelTankRefuelId(fuelTankRefuelId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetDailyStockDetailsById :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mainObj = dataArray.getJSONObject(i);

                            String tankNo = mainObj.getString("tankNo");
                            String productCategory = mainObj.getString("productCategory");
                            String openDipScaleReading = mainObj.getString("openDipScaleReading");
                            String closeDipScaleReading = mainObj.getString("closeDipScaleReading");
                            String openStockByDipReading = mainObj.getString("openStockByDipReading");
                            String closeStockByDipReading = mainObj.getString("closeStockByDipReading");
                            String densityRecorded = mainObj.getString("densityRecorded");
                            String closeDensity = mainObj.getString("closeDensity");

                            stockDetails.add(tankNo);
                            stockDetails.add(productCategory);
                            stockDetails.add(openDipScaleReading);
                            stockDetails.add(closeDipScaleReading);
                            stockDetails.add(openStockByDipReading);
                            stockDetails.add(closeStockByDipReading);
                            stockDetails.add(densityRecorded);
                            stockDetails.add(fuelTankRefuelId);
                            stockDetails.add(closeDensity);


                        }

                        updateDailyStock(stockDetails);

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

    private void updateDailyStock(List<String> stockDetails) {
        // spinnerSelectTank.setPrompt(stockDetails.get(0));
        textViewSelTank.setVisibility(View.VISIBLE);
        textViewSelTank.append(stockDetails.get(0));
        textViewProduct.setText(stockDetails.get(1));
        editTextOpenDipRead.setText(stockDetails.get(2));
        editTextOpenDipStock.setText(stockDetails.get(4));
        editTextCloseDipRead.setText(stockDetails.get(3));
        editTextCloseDipStock.setText(stockDetails.get(5));
        editTextOpenDensity.setText(stockDetails.get(6));
        editTextCloseDensity.setText(stockDetails.get(8));


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DailyStockReadingActivity.this, DailyStockReadingListActivity.class);
        startActivity(intent);
    }

    private boolean areValidateForNew() {
        boolean result = false;
        if (spinnerSelectedTank.contains("Select")) {
            result = false;
            spinnerErrorText.setError("Please Select Tank");
            Toast.makeText(this, "Please Select Tank", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(editTextOpenDipRead.getText().toString())) {
            result = false;
            editTextOpenDipRead.setError("Please Enter Open Dip Reading");
        } else if (TextUtils.isEmpty(editTextOpenDipStock.getText().toString())) {
            result = false;
            editTextOpenDipStock.setError("Please Enter Open Dip Stock Reading");
        } else if (TextUtils.isEmpty(editTextOpenDensity.getText().toString())) {
            result = false;
            editTextOpenDensity.setError("Please Enter Open Density");
        } else {
            result = true;
        }

        return result;
    }

    private boolean areValidateForUpdate() {
        boolean result = false;
        if (TextUtils.isEmpty(editTextCloseDipRead.getText().toString())) {
            result = false;
            editTextCloseDipRead.setError("Please Enter Close Dip Reading");
        } else if (TextUtils.isEmpty(editTextCloseDipStock.getText().toString())) {
            result = false;
            editTextCloseDipStock.setError("Please Enter Close Dip Stock Reading");
        } else if (TextUtils.isEmpty(editTextCloseDensity.getText().toString())) {
            result = false;
            editTextCloseDensity.setError("Please Enter Close Density");
        } else {
            result = true;
        }

        return result;
    }
}