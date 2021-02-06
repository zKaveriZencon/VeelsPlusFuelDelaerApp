package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Region;
import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.managers.ApiHandlerForErrorLog;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.DecimalDigitsInputFilter;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.ErrorLogModel;
import com.veelsplusfueldealerapp.models.OperatorEndShiftModel;
import com.veelsplusfueldealerapp.models.OperatorStartShiftModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorShiftStartEndActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "OperatorShiftStartEndAc";
    RelativeLayout opParent;
    ApiHandlerForErrorLog apiHandlerForErrorLog;
    boolean isAlreadyShiftStarted = false;
    String totalCRAmt = "";
    String finalCRTotal = "0";
    private Spinner spinnerSelectDu, spinnerSelectNossel;
    private TextInputEditText editTextStartReading, editTextEndReading, editTextTestStock;
    private TextView textViewFuelType, textViewRate, textViewCurrentDate, textViewNote,
            labelEndReading, labelTestStock, textViewUnitSales, textViewSelectedPump,
            textViewSelectedNozzle, textViewActualSale;
    private MaterialButton buttonStartShift, buttonEndshift;
    private String spinnerSelectedPump, spinnerSelectedNozzle;
    private CommonTaskManager commonTaskManager;
    private ProgressDialog progressDialogStart;
    private DatabaseHelper databaseHelper;
    private CommonCodeManager commonCodeManager;
    private TextView spinnerErrorTextDu, spinnerErrorTextNozzle;
    private int spinnerSelectedPositionDu, spinnerSelectedPositionNoz;

    @Override
    protected void onResume() {
        super.onResume();


        textViewSelectedNozzle.setText("");
        textViewSelectedPump.setText("");
        textViewUnitSales.setText("");
        textViewActualSale.setText("");
        editTextStartReading.setText("");
        textViewCurrentDate.setText("");
        editTextEndReading.setText("");

        boolean result = commonTaskManager.compareDates(OperatorShiftStartEndActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(OperatorShiftStartEndActivity.this, OperatorShiftStartEndActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {

       /* String shiftName = getIntent().getStringExtra("forendshift");
        String fuelStaffPerformId = getIntent().getStringExtra("fuelStaffPerformId");
        String status = getIntent().getStringExtra("completed");
        Log.d(TAG, "onResume: status check :"+status);*/

            List<String> params = commonCodeManager.getParamsForManageOPShift(OperatorShiftStartEndActivity.this);

            Log.d(TAG, "onResume: status check :" + params.get(2));

            if (params.get(1) != null && params.get(1).equals("endshift")) {
                Log.d(TAG, "onResume: for end shift");

                if (params.get(2) != null && params.get(2).equals("completed")) {
                    Log.d(TAG, "onResume: for end shift with status completed");

                    apiCallForGetOperatorShiftDetailsById(OperatorShiftStartEndActivity.this,
                            params.get(0), "completed");

                    spinnerSelectDu.setEnabled(false);
                    spinnerSelectNossel.setEnabled(false);
                    editTextStartReading.setEnabled(false);
                    buttonStartShift.setVisibility(View.GONE);
                    buttonEndshift.setEnabled(false);
                    editTextEndReading.setEnabled(false);
                    editTextTestStock.setEnabled(false);

                    labelEndReading.setVisibility(View.VISIBLE);
                    labelTestStock.setVisibility(View.VISIBLE);
                    editTextEndReading.setVisibility(View.VISIBLE);
                    editTextTestStock.setVisibility(View.VISIBLE);
                    textViewUnitSales.setVisibility(View.VISIBLE);
                    buttonEndshift.setVisibility(View.VISIBLE);
                    spinnerSelectDu.setVisibility(View.GONE);
                    spinnerSelectNossel.setVisibility(View.GONE);
                    textViewSelectedPump.setVisibility(View.VISIBLE);
                    textViewSelectedNozzle.setVisibility(View.VISIBLE);

                    textViewNote.setVisibility(View.VISIBLE);
                    textViewActualSale.setVisibility(View.VISIBLE);

                    Snackbar snackbar = Snackbar
                            .make(opParent, "Shift Ended !", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    params1.gravity = Gravity.TOP;
                    view.setLayoutParams(params1);
                    view.setBackgroundColor(getResources().getColor(R.color.colorCommonGreen));
                    snackbar.show();
                } else {
                    Log.d(TAG, "onResume: for end shift with status not completed");

                    apiCallForGetOperatorShiftDetailsById
                            (OperatorShiftStartEndActivity.this,
                                    params.get(0), "nocheck");
                    spinnerSelectDu.setEnabled(false);
                    spinnerSelectNossel.setEnabled(false);
                    editTextStartReading.setEnabled(false);
                    buttonStartShift.setVisibility(View.GONE);

                    labelEndReading.setVisibility(View.VISIBLE);
                    labelTestStock.setVisibility(View.VISIBLE);
                    editTextEndReading.setVisibility(View.VISIBLE);
                    editTextTestStock.setVisibility(View.VISIBLE);
                    //textViewUnitSales.setVisibility(View.VISIBLE);
                    buttonEndshift.setVisibility(View.VISIBLE);
                    spinnerSelectDu.setVisibility(View.GONE);
                    spinnerSelectNossel.setVisibility(View.GONE);
                    textViewSelectedPump.setVisibility(View.VISIBLE);
                    textViewSelectedNozzle.setVisibility(View.VISIBLE);


                }


            } else {
                Log.d(TAG, "onResume: for start shift");

                textViewCurrentDate.setText("");
                String[] currentDateDetails = commonTaskManager.getCurrentDate();
                String currentDate = currentDateDetails[0] + ", " + currentDateDetails[1];
                textViewCurrentDate.append(currentDate);
                Log.d(TAG, "getAllStartShiftParams:currentDate: " + currentDate);

                fillPumpDetails();
            }

            // check whether infra setup is correct or not

            String noinfra = commonCodeManager.getNoInfraDetailsMessage(OperatorShiftStartEndActivity.this);
            if (noinfra.equals("noinfra")) {
                buttonStartShift.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);
                builder.setTitle("Note !");
                builder.setMessage("Unable to get proper Infra Setup data from server. Please check infra setup once or Logout from App and Login again.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorDailyWorkListActivity.class);
                                startActivity(intent);
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            String fromWhere = commonCodeManager.getIfCameFromCorporateOrPerson(OperatorShiftStartEndActivity.this);
            if (fromWhere.equals("corporate")) {
                fillPumpDetails();

                /*commonCodeManager.saveFuelStaffPerformId(OperatorShiftStartEndActivity.this, operatorDailyWorkListModel.getFuelStaffPerformId());
                commonCodeManager.saveParamsForManageOPShift(context, params);*/

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operator_shift_start_end);

        initUI();
        //2020-10-05T01:23:20.000Z
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
        Log.e(TAG, "onCreate:date formatted string: " + sdf.format(c.getTime()));


    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager(OperatorShiftStartEndActivity.this);
        commonTaskManager = new CommonTaskManager(OperatorShiftStartEndActivity.this);
        apiHandlerForErrorLog = new ApiHandlerForErrorLog(OperatorShiftStartEndActivity.this);

        databaseHelper = new DatabaseHelper(OperatorShiftStartEndActivity.this);

        View toolbar = findViewById(R.id.layout_toobar_shift);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText(getResources().getString(R.string.shift_start_end));
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);


        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);


        spinnerSelectDu = findViewById(R.id.spinner_select_du);
        spinnerSelectNossel = findViewById(R.id.spinner_select_nossel);
        editTextStartReading = findViewById(R.id.edittext_start_reading);
        editTextEndReading = findViewById(R.id.edittext_end_reading);
        textViewFuelType = findViewById(R.id.textview_fuel_type);
        textViewRate = findViewById(R.id.textview_fuel_rate);
        textViewCurrentDate = findViewById(R.id.textview_current_date);
        buttonStartShift = findViewById(R.id.button_submit_start_shift);
        buttonEndshift = findViewById(R.id.button_submit_end_shift);
        textViewUnitSales = findViewById(R.id.textview_unit_sales);
        textViewSelectedPump = findViewById(R.id.textview_selected_pump);
        textViewSelectedNozzle = findViewById(R.id.textview_selected_nozzle);

        labelEndReading = findViewById(R.id.label_end_reading);
        labelTestStock = findViewById(R.id.label_test_stock);
        editTextTestStock = findViewById(R.id.edittext_test_stock);
        textViewFuelType = findViewById(R.id.textview_fuel_type);
        textViewNote = findViewById(R.id.textview_note_opshift);
        textViewActualSale = findViewById(R.id.textview_actual_sale);


        opParent = findViewById(R.id.op_parent);

        /*editTextEndReading.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0)
                    textViewUnitSales.setText("");
            }
        });*/


        buttonStartShift.setOnClickListener(this);
        buttonEndshift.setOnClickListener(this);

        setTwoDigitFilters();


    }

    private void setTwoDigitFilters() {
        editTextStartReading.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextEndReading.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextTestStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
    }

    private void fillPumpDetails() {

        List<String> dusList = databaseHelper.getAllPumpsFromInfraMapping();
        List<String> pumpsList = new ArrayList<>();

        for (int i = 0; i < dusList.size(); i++) {
            if (i == 0) {
                pumpsList.add("Select Pump");
            }
            pumpsList.add(dusList.get(i));

        }
        String[] allDus = new String[pumpsList.size()];
        for (int i = 0; i < pumpsList.size(); i++) {
            allDus[i] = pumpsList.get(i);
        }

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(OperatorShiftStartEndActivity.this, android.R.layout.simple_spinner_item, allDus);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectDu.setAdapter(arrayAdapter1);

        spinnerSelectDu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedPump = (String) spinnerSelectDu.getSelectedItem();
                spinnerErrorTextDu = (TextView) spinnerSelectDu.getSelectedView();
                if (spinnerSelectedPump.contains("Select Pump")) {

                } else {

                    getNozzleDetails(spinnerSelectedPump);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void apiCallForGetFuelPriceByDealerTankMap(final Context context, String dealerId, String dealerTankMap) {
        // (unit sales-teststock )*price
        final ProgressDialog progressDialogStart = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        //progressDialogStart.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialogStart.setMessage("Loading Data, Please wait");
        progressDialogStart.setCancelable(false);
        progressDialogStart.setCanceledOnTouchOutside(false);
        progressDialogStart.show();

        final String[] details = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelPriceByDealerTankMap(dealerId, dealerTankMap);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetFuelPriceByDealerTankMap :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            JSONObject duObject = dataArray.getJSONObject(0);
                            final String productSellingPrice = duObject.getString("productSellingPrice");
                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    if (progressDialogStart != null) {
                                        progressDialogStart.dismiss();
                                        textViewRate.setText(productSellingPrice);

                                    }
                                }
                            });
                        } else {
                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    if (progressDialogStart != null) {
                                        progressDialogStart.dismiss();
                                        textViewRate.setText("Fuel Price Not Assigned");

                                    }
                                }
                            });
                            commonTaskManager.dismissProgressDialog(context, progressDialogStart);

                        }

                    }

                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });
    }

    private void getNozzleDetails(final String spinnerSelectedPump) {
        Log.d(TAG, "getNozzleDetails: spinnerSelectedPump : " + spinnerSelectedPump);
        //shared pref
        //commonCodeManager.saveInfraDetailsForOPShift(OperatorShiftStartEndActivity.this, allDetails);
        List<String> nozzlesList = databaseHelper.getAllNozzlesFromInfraMapping(spinnerSelectedPump);

        List<String> nozsList = new ArrayList<>();

        for (int i = 0; i < nozzlesList.size(); i++) {
            if (i == 0) {
                nozsList.add("Select Nozzle");
            }
            nozsList.add(nozzlesList.get(i));

        }
        String[] allNozzles = new String[nozsList.size()];

        for (int i = 0; i < nozsList.size(); i++) {
            allNozzles[i] = nozsList.get(i);
        }
        ArrayAdapter arrayAdapter2 = new ArrayAdapter(OperatorShiftStartEndActivity.this, android.R.layout.simple_spinner_item, allNozzles);
        arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectNossel.setAdapter(arrayAdapter2);
        spinnerSelectNossel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedNozzle = (String) spinnerSelectNossel.getSelectedItem();
                spinnerErrorTextNozzle = (TextView) spinnerSelectNossel.getSelectedView();

                if (spinnerSelectedNozzle.contains("Select Nozzle")) {

                } else {
                    String tankNo = databaseHelper.getTankByPumpAndNozzle
                            (spinnerSelectedPump, spinnerSelectedNozzle);

                    String prodName = databaseHelper.getProductByTank(tankNo);
                    Log.d(TAG, "onItemSelected:tankNo :  " + tankNo);
                    //Log.d(TAG, "onItemSelected:prodName :  " + prodName);

                    textViewFuelType.setText(prodName);


                    //check whether already there is ongoing shift whit selected du-nozzle

                    // checkForOngoingShift(tankNo, spinnerSelectedPump, spinnerSelectedNozzle);
                    String infraMapId = databaseHelper.getFuelInfraMapIdForShiftCheck(tankNo, spinnerSelectedPump, spinnerSelectedNozzle);
                    Log.d(TAG, "checkForOngoingShift: infraMapId : " + infraMapId);

                    apiCallForCheckShiftIsOngoingOrNot(OperatorShiftStartEndActivity.this, infraMapId, spinnerSelectedPump, spinnerSelectedNozzle);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void checkForOngoingShift(String tankNo, String spinnerSelectedPump, String spinnerSelectedNozzle) {


    }

    private void apiCallForCheckShiftIsOngoingOrNot(final Context context,
                                                    String infraMapId,
                                                    final String spinnerSelectedPump,
                                                    final String spinnerSelectedNozzle) {
        progressDialogStart = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialogStart.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialogStart.setCancelable(false);
        progressDialogStart.setCanceledOnTouchOutside(false);
        progressDialogStart.show();

        final String[] details = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.checkPumpNzStatus(infraMapId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForCheckShiftIsOngoingOrNot :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    message[0] = mainObject.getString("msg");
                    JSONArray dataArray = mainObject.getJSONArray("data");

                    if (status.equals("ok")) {

                        if (dataArray.length() > 0) {
                            giveProperMessageToUserAboutShift(spinnerSelectedPump, spinnerSelectedNozzle, "started");

                        } else {
                            giveProperMessageToUserAboutShift(spinnerSelectedPump, spinnerSelectedNozzle, "continue");

                        }
                    } else {
                        commonTaskManager.showToast(context, message[0]);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    //call api for error log

                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissDialogWithToast(context, progressDialogStart, context.getResources().getString(R.string.unable_to_connect));
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });

    }

    private void giveProperMessageToUserAboutShift(String spinnerSelectedPump, String spinnerSelectedNozzle, String shiftStatus) {
        String pumpNozzle = spinnerSelectedPump + "-" + spinnerSelectedNozzle;


        if (shiftStatus.equals("started")) {
            String message = "There is already ongoing shift for " + pumpNozzle + "." +
                    " Please try another pump-nozzle";
            isAlreadyShiftStarted = true;
            AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);
            builder.setTitle("Note !");
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();

          /*  final Snackbar snackbar = Snackbar
                    .make(opParent, message, Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
            params1.gravity = Gravity.CENTER;
            view.setLayoutParams(params1);
            view.setBackgroundColor(getResources().getColor(R.color.colorZencon));
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();*/
        }

        if (shiftStatus.equals("continue")) {
            //for fuel rate - add it after shift check
            isAlreadyShiftStarted = false;
            List<String> details = databaseHelper.getTankAndPKByDuNozzle
                    (spinnerSelectedPump, spinnerSelectedNozzle);
            String dealerTankMap = details.get(3) + details.get(1);
            Log.d(TAG, "onItemSelected: dealerTankMap : " + dealerTankMap);

            textViewRate.setText("");
            apiCallForGetFuelPriceByDealerTankMap
                    (OperatorShiftStartEndActivity.this,
                            details.get(3), dealerTankMap);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_start_shift:
                commonTaskManager.hideKeyboard(OperatorShiftStartEndActivity.this);


                if (isAlreadyShiftStarted) {
                    String message = "Already ongoing shift for this pump-nozzle. You can't start shift again.";
                    AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);
                    builder.setTitle("Note !");
                    builder.setMessage(message)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {


                    if (areValidateForShiftStart()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);

                        builder.setTitle("Confirm Start Shift");
                        builder.setMessage("Confirm entered details by clicking 'Confirm' or 'Review' for review.")
                                .setCancelable(false)
                                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        getAllStartShiftParams();


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
                //
                break;
            case R.id.button_submit_end_shift:
                commonTaskManager.hideKeyboard(OperatorShiftStartEndActivity.this);

                if (areValidateForShiftEnd()) {
                    getAllEndShiftParams();

                }


                break;

            case R.id.imageview_back_arrow:
                Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorDailyWorkListActivity.class);
                startActivity(intent);
                break;

            case R.id.imageview_goto_home:
                Intent intent1 = new Intent(OperatorShiftStartEndActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    private void getAllEndShiftParams() {
        final String endReading = editTextEndReading.getText().toString().trim();
        final String testStock = editTextTestStock.getText().toString().trim();
        final String startReading = commonCodeManager.getStartShiftDetails(OperatorShiftStartEndActivity.this);
        final String fuelPrice = textViewRate.getText().toString();


        if (fuelPrice != null & !fuelPrice.isEmpty() && !fuelPrice.equals("null") && !fuelPrice.equals("undefined")) {

            double fuelPriceValue = Double.parseDouble(fuelPrice);

            double startReadingValue = Double.parseDouble(startReading);
            double endReadingValue = Double.parseDouble(endReading);
            double testStockValue = Double.parseDouble(testStock);
            final double unitsales = endReadingValue - startReadingValue;

            final Double recoveryAmount = (unitsales - testStockValue) * fuelPriceValue;

            final String unitSale = String.valueOf(unitsales);
            Log.d(TAG, "getAllEndShiftParams: unitSale : " + unitsales);

            final double actualSale = unitsales - testStockValue;
            String finalActualSale = "" + String.format("%.2f", actualSale);
            String finalUnitSale = "" + String.format("%.2f", unitsales);
            if (totalCRAmt != null & !totalCRAmt.isEmpty() && !totalCRAmt.equals("null") && !totalCRAmt.equals("undefined")) {
                finalCRTotal = totalCRAmt;
            }
            String message1 = "Confirm entered details by clicking 'Confirm' " +
                    "or 'Review' for review. \n\n Test Stock = " + testStockValue +
                    " \n Unit Sale = " + finalUnitSale +
                    "\n Actual Sale = " + finalActualSale;

            AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);

            builder.setTitle("Confirm Readings !");
            builder.setMessage(message1)
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            String fuelStaffPerformId = commonCodeManager.getFuelStaffPerformId(OperatorShiftStartEndActivity.this);

                            String recoveryAmountFinal = "" + String.format("%.2f", recoveryAmount);
                            String finalUnitSale = "" + String.format("%.2f", unitsales);

                            OperatorEndShiftModel opendshift = new OperatorEndShiftModel
                                    (fuelStaffPerformId, endReading,
                                            commonTaskManager.getCurrentDateNew(),
                                            finalUnitSale, testStock, recoveryAmountFinal,
                                            commonTaskManager.getCurrentTime(),
                                            finalCRTotal);

                            Log.d(TAG, "onClick:test fuelStaffPerformId : " + fuelStaffPerformId);
                            Log.d(TAG, "onClick:test date : " + commonTaskManager.getCurrentDateNew());
                            Log.d(TAG, "onClick:test unitSale : " + unitSale);
                            Log.d(TAG, "onClick:test testStock : " + testStock);
                            Log.d(TAG, "onClick:test recoveryAmountFinal : " + recoveryAmountFinal);
                            Log.d(TAG, "onClick:test actualSale : " + actualSale);


                            apiCallForSendOpEndShiftDetails(OperatorShiftStartEndActivity.this,
                                    opendshift);
                        }
                    })
                    .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.show();
        } else {
            Toast.makeText(this, "Error while getting fuel price", Toast.LENGTH_SHORT).show();
        }

    }

    private void getAllStartShiftParams() {
        String startReading = editTextStartReading.getText().toString();

        String spinnerSelectedPump = (String) spinnerSelectDu.getSelectedItem();
        String spinnerSelectedNozzle = (String) spinnerSelectNossel.getSelectedItem();


        Log.d(TAG, "getAllStartShiftParams: selected pumpcheck : " + spinnerSelectedPump);

        List<String> infraDetails = databaseHelper.getTankAndPKByDuNozzle
                (spinnerSelectedPump, spinnerSelectedNozzle);

        String[] details = commonCodeManager.getDealerDetails(OperatorShiftStartEndActivity.this);
        String dealerTankMap = details[2] + infraDetails.get(1);

        String fuelPrice = textViewRate.getText().toString().toLowerCase().trim();
        String todaysFuelRate;
        if (fuelPrice.contains("price")) {
            todaysFuelRate = "";
        } else {
            todaysFuelRate = textViewRate.getText().toString();
        }

        Log.d(TAG, "getAllStartShiftParams: date test  : " + commonTaskManager.getCurrentDateTimeTestFormat());
        OperatorStartShiftModel operatorStartShiftModel =
                new OperatorStartShiftModel(infraDetails.get(4), details[0],
                        startReading, commonTaskManager.getCurrentDateNew(),
                        todaysFuelRate, dealerTankMap, details[1],
                        commonTaskManager.getCurrentTime());
        Log.d(TAG, "getAllStartShiftParams: startReading :  " + startReading);

        Log.d(TAG, "getAllStartShiftParams: fuelInfraMapId : " + infraDetails.get(4));
        Log.d(TAG, "getAllStartShiftParams: dealerstaffid : " + details[0]);
        Log.d(TAG, "getAllStartShiftParams:todaysFuelRate :  " + todaysFuelRate);
        Log.d(TAG, "getAllStartShiftParams: dealerTankMap : " + dealerTankMap);
        Log.d(TAG, "getAllStartShiftParams: dealerid :  " + details[1]);
        Log.d(TAG, "getAllStartShiftParams: date : " + commonTaskManager.getCurrentDateTimeTestFormat());
        apiCallForSendOpStartShiftDetails(OperatorShiftStartEndActivity.this,
                operatorStartShiftModel);


    }

    public void apiCallForSendOpStartShiftDetails(final Context context,
                                                  final OperatorStartShiftModel opShiftDetailsModel) {
        progressDialogStart = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialogStart.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialogStart.setCancelable(false);
        progressDialogStart.setCanceledOnTouchOutside(false);
        progressDialogStart.show();

        final String[] details = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};
        final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], message[0], Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.sendOpStartShiftDetails(opShiftDetailsModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSendOpStartShiftDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    final String status = mainObject.getString("status");
                    message[0] = mainObject.getString("msg");
                    if (status.equals("OK")) {
                        manageDailyWorkActivityForStartShift();
                    } else {
                        commonTaskManager.showToast(context, message[0]);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    //call api for error log

                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissDialogWithToast(context, progressDialogStart, context.getResources().getString(R.string.unable_to_connect));
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });
    }

    private void manageDailyWorkActivityForStartShift() {

        String fromWhere = commonCodeManager.getIfCameFromCorporateOrPerson(OperatorShiftStartEndActivity.this);
        if (fromWhere.equals("corporate")) {
            Intent intent = new Intent(OperatorShiftStartEndActivity.this, NewFuelCreditRequestActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorDailyWorkListActivity.class);
            startActivity(intent);
        }

    }

    private void manageDailyWorkActivityForEndShift(String fuelStaffPerformId) {

        apiCallForUpdateOPEndShiftStatus(OperatorShiftStartEndActivity.this, fuelStaffPerformId);


    }


    public void apiCallForSendOpEndShiftDetails(final Context context, final OperatorEndShiftModel operatorEndShiftModel) {

        // (unit sales-teststock )*price
        progressDialogStart = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        //progressDialogStart.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialogStart.setMessage("Shift end");
        progressDialogStart.setCancelable(false);
        progressDialogStart.setCanceledOnTouchOutside(false);
        progressDialogStart.show();

        final String[] details = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.sendOpEndShiftDetails(operatorEndShiftModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSendOpEndShiftDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");
                    if (status.equals("OK")) {
                        manageDailyWorkActivityForEndShift(operatorEndShiftModel.getFuelStaffPerformId());
                    } else {
                        commonTaskManager.showToast(context, message);
                    }

                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialogStart);

                    e.printStackTrace();
                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);


                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialogStart, context.getResources().getString(R.string.unable_to_connect));
                t.printStackTrace();
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);


            }

        });
    }


    private void apiCallForGetOperatorShiftDetailsById(final Context context,
                                                       String fuelStaffPerformId,
                                                       final String statusCheck) {
        Log.d(TAG, "apiCallForGetOperatorShiftDetailsById:fuelStaffPerformId :  " + fuelStaffPerformId);
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] message = new String[1];
        final String[] details = commonCodeManager.getDealerDetails(OperatorShiftStartEndActivity.this);


        final List<String> opStartShiftDetailsList = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getOpertaorShiftDetailsById(fuelStaffPerformId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse: shift details : " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    message[0] = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        Log.d(TAG, "onResponse: status ok");
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        JSONObject duObject = dataArray.getJSONObject(0);
                        String openMetDateTime = duObject.getString("openMetDateTime");
                        String openMetReading = duObject.getString("openMetReading");
                        String closeMetReading = duObject.getString("closeMetReading");
                        String testStockByMeter = duObject.getString("testStockByMeter");
                        String unitSales = duObject.getString("unitSales");

                        String pumpNo = duObject.getString("duNo");
                        String nozzleNo = duObject.getString("nozNo");
                        String activityStatus = duObject.getString("duNo");
                        String recoveryStatus = duObject.getString("recoveryStatus");
                        String productCategory = duObject.getString("productCategory");
                        String productSellingPrice = duObject.getString("fuelPriceByDate");
                        String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");

                        String pumpNozzle = pumpNo + nozzleNo;
                        String dateOnly = "";

                        if (openMetDateTime != null && !openMetDateTime.equals("null")
                                && !openMetDateTime.isEmpty() && openMetDateTime.contains("T")) {
                            String[] datetime = openMetDateTime.split("T");
                            dateOnly = datetime[0];

                        } else {
                            dateOnly = "";
                        }

                        opStartShiftDetailsList.add(dateOnly);
                        opStartShiftDetailsList.add(openMetReading);
                        opStartShiftDetailsList.add(pumpNo);
                        opStartShiftDetailsList.add(nozzleNo);
                        opStartShiftDetailsList.add(productCategory);
                        opStartShiftDetailsList.add(productSellingPrice);
                        opStartShiftDetailsList.add(fuelStaffPerformId);

                        if (statusCheck.equals("completed")) {
                            opStartShiftDetailsList.add(closeMetReading);
                            opStartShiftDetailsList.add(testStockByMeter);
                            opStartShiftDetailsList.add(unitSales);
                        }

                        commonCodeManager.saveStartShiftDetails(context, openMetReading);

                        fillUpEndShiftDetails(opStartShiftDetailsList, statusCheck);

                    } else {
                        commonTaskManager.showToast(context, message[0]);
                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialogStart, context.getResources().getString(R.string.unable_to_connect));
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });
    }

    private void fillUpEndShiftDetails(List<String> opStartShiftDetailsList, String statusCheck) {
        Log.d(TAG, "fillUpEndShiftDetails: ");
        textViewCurrentDate.setText("Date : " + opStartShiftDetailsList.get(0));
        editTextStartReading.setText(opStartShiftDetailsList.get(1));
       /* spinnerSelectDu.setPrompt(opStartShiftDetailsList.get(2));
        spinnerSelectNossel.setPrompt(opStartShiftDetailsList.get(3));*/
        textViewFuelType.setText(opStartShiftDetailsList.get(4));
        textViewRate.setText(opStartShiftDetailsList.get(5));
        textViewSelectedPump.append("Selected Pump : " + opStartShiftDetailsList.get(2));
        textViewSelectedNozzle.append("Selected Nozzle : " + opStartShiftDetailsList.get(3));
        Log.d(TAG, "fillUpEndShiftDetails: statusCheck : " + statusCheck);

        if (statusCheck.equals("nocheck")) {
            apiCallForGetShiftWiseTotalCreditAmount(OperatorShiftStartEndActivity.this, opStartShiftDetailsList.get(6));
        }

        if (statusCheck.equals("completed")) {
            editTextEndReading.setText(opStartShiftDetailsList.get(7));
            editTextTestStock.setText(opStartShiftDetailsList.get(8));
            textViewUnitSales.append("Unit Sale = " + opStartShiftDetailsList.get(9));

            if (opStartShiftDetailsList.get(9) != null && !opStartShiftDetailsList.get(9).equals("null") && !opStartShiftDetailsList.get(9).isEmpty() && !opStartShiftDetailsList.get(9).equals("undefined")) {

                if (opStartShiftDetailsList.get(8) != null && !opStartShiftDetailsList.get(8).equals("null") && !opStartShiftDetailsList.get(8).isEmpty() && !opStartShiftDetailsList.get(8).equals("undefined")) {

                    double actualSale = Double.parseDouble(opStartShiftDetailsList.get(9)) - Double.parseDouble(opStartShiftDetailsList.get(8));
                    String finalActualSale = "" + String.format("%.2f", actualSale);

                    textViewActualSale.append("Actual Meter Sale  = " + finalActualSale);

                }
            }

        }

        Log.d(TAG, "fillUpEndShiftDetails:pump: " + opStartShiftDetailsList.get(2));
        Log.d(TAG, "fillUpEndShiftDetails:nozzle: " + opStartShiftDetailsList.get(3));


    }

    private boolean areValidateForShiftStart() {
        boolean result = false;
        String rate = textViewRate.getText().toString().toLowerCase();
        Log.d(TAG, "areValidateForShiftStart: rate : " + rate);
      /*  String spinnerSelectedPump = (String) spinnerSelectDu.getSelectedItem();
        String spinnerSelectedNozzle = (String) spinnerSelectDu.getSelectedItem();
*/
        if (spinnerSelectedPump.contains("Pump")) {
            result = false;
            spinnerErrorTextDu.setError("Please Select Pump");
            Toast.makeText(this, "Please Select Pump", Toast.LENGTH_LONG).show();
        } else if (spinnerSelectedNozzle.contains("Nozzle")) {
            result = false;
            spinnerErrorTextNozzle.setError("Please Select Nozzle");
            Toast.makeText(this, "Please Select Nozzle", Toast.LENGTH_LONG).show();

        } else if (rate.isEmpty() || (rate.contains("not"))) {
            Log.d(TAG, "areValidateForShiftStart: in rate if");
            result = false;
            AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);
            builder.setTitle("Can't Proceed !");
            builder.setMessage("Fuel price not assigned. Please set fuel price first. Please contact manager")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();

                        }
                    });
           /* builder.setMessage("Fuel price not assigned. Please set fuel price first.")
                    .setCancelable(false)
                    .setPositiveButton("Set Price", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent intent = new Intent(OperatorShiftStartEndActivity.this, SetFuelPriceActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });
*/
            AlertDialog dialog = builder.create();
            dialog.show();

           /* Snackbar snackbar = Snackbar
                    .make(opParent, "Fuel Price not assigned. Please set fuel price first !", Snackbar.LENGTH_INDEFINITE);
            View view = snackbar.getView();
            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
            params1.gravity = Gravity.CENTER;
            view.setLayoutParams(params1);
            view.setBackgroundColor(getResources().getColor(R.color.colorZencon));
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(OperatorShiftStartEndActivity.this, SetFuelPriceActivity.class);
                    startActivity(intent);
                }
            });
            snackbar.show();*/

            // spinnerErrorTextNozzle.setError("Fuel Price not assigned. Please set fuel price first");
            //Toast.makeText(this, "Fuel Price not assigned. Please set fuel price first", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(editTextStartReading.getText().toString())) {
            result = false;
            editTextStartReading.setError("Please Enter Start Meter Reading");
        } else {
            result = true;
        }

        return result;
    }

    private boolean areValidateForShiftEnd() {
        boolean result = false;

        String startMeterReading = Objects.requireNonNull(editTextStartReading.getText()).toString();
        String endMeterReading = Objects.requireNonNull(editTextEndReading.getText()).toString();
        String testStock = Objects.requireNonNull(editTextTestStock.getText()).toString();

        if (TextUtils.isEmpty(Objects.requireNonNull(editTextEndReading.getText()).toString())) {
            result = false;
            editTextEndReading.setError("Please Enter End Meter Reading");
        } else if (TextUtils.isEmpty(Objects.requireNonNull(editTextTestStock.getText()).toString())) {
            result = false;
            editTextTestStock.setError("Please Enter Test Stock");
        } else if (endMeterReading != null && !endMeterReading.equals("null") && !endMeterReading.equals("undefined") && !endMeterReading.isEmpty()) {
            if (startMeterReading != null && !startMeterReading.equals("null") && !startMeterReading.equals("undefined") && !startMeterReading.isEmpty() && !testStock.isEmpty()) {
                Log.d(TAG, "areValidateForShiftEnd: ");
                double startMetValue = Double.parseDouble(startMeterReading);
                double endMetValue = Double.parseDouble(endMeterReading);

                if (startMetValue > endMetValue) {
                    result = false;

                    Toast.makeText(this, "End Meter Reading Must Be Greater Than Start Meter Reading", Toast.LENGTH_SHORT).show();
                    editTextEndReading.setError("End Meter Reading Must Be Greater Than Start Meter Reading");

                } else if (endMeterReading != null && !endMeterReading.equals("null") && !endMeterReading.equals("undefined") && !endMeterReading.isEmpty()) {
                    if (testStock != null && !testStock.equals("null") && !testStock.equals("undefined") && !testStock.isEmpty()) {

                        double testStockValue = Double.parseDouble(testStock);
                        if (testStockValue > endMetValue) {
                            result = false;

                            Toast.makeText(this, "Test Stock Must Be Smaller Than End Meter Reading", Toast.LENGTH_SHORT).show();
                            editTextTestStock.setError("Test Stock Must Be Smaller Than End Meter Reading");

                        } else {
                            result = true;
                        }
                    }
                }


            }
        } else {
            result = true;
        }

        return result;
    }

    private void apiCallForUpdateOPEndShiftStatus(final Context context, String
            fuelStaffPerformId) {
        Log.d(TAG, "apiCallForUpdateInventoryStatus: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateStatusForOperatorShiftEnd(fuelStaffPerformId, "COMPLETED");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateOPShiftStatus :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String message = mainObject.getString("msg");

                    if (status.equals("ok")) {

                        showUserActivity();
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

    private void showUserActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OperatorShiftStartEndActivity.this);

        builder.setTitle("Choose Action !");
        builder.setMessage("Please select one of the option given below")
                .setCancelable(false)
                .setPositiveButton("Submit Sales Details", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        databaseHelper.deleteDailySales();
                        databaseHelper.deleteSalesInfoCardTable();
                        // commonCodeManager.saveConfirmRecoveryFlag(DailySalesActivity.this, false);
                        commonCodeManager.saveBatchIdForErReport(OperatorShiftStartEndActivity.this, commonTaskManager.getBatchIdDetails());
                        commonCodeManager.saveDailySalesCardEssentials(OperatorShiftStartEndActivity.this, "addnew", "");
                        //save is exist as false for new
                        //local db handling for info cards
                        commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(OperatorShiftStartEndActivity.this, "apicall");
                        commonCodeManager.saveIsDigitalSubmitTransClicked(OperatorShiftStartEndActivity.this, "");
                        commonCodeManager.saveTotalAmtDigitalForDoneCardClickView(OperatorShiftStartEndActivity.this, "", "", "");
                        commonCodeManager.saveDailySalesCardDetailsForView(OperatorShiftStartEndActivity.this, "new");

                        commonCodeManager.saveTotalCreditAmountFinal(OperatorShiftStartEndActivity.this, "new");
                        commonCodeManager.saveTotalDueInSales(OperatorShiftStartEndActivity.this, "nothing");

                        getSharedPreferences(Constants.RECOVERY, 0).edit().clear().apply();
                        getSharedPreferences(Constants.CASH, 0).edit().clear().apply();
                        getSharedPreferences(Constants.DIGITAL, 0).edit().clear().apply();
                        getSharedPreferences(Constants.RECOVERY_AMOUNT, 0).edit().clear().apply();
                        getSharedPreferences(Constants.DONE, 0).edit().clear().apply();
                        dialog.dismiss();


                        Intent intent = new Intent(OperatorShiftStartEndActivity.this, EarningsReportActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Back To List", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorDailyWorkListActivity.class);
                        startActivity(intent);


                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


        String forManagerView = commonCodeManager.getManagerOPList(OperatorShiftStartEndActivity.this);
        if (forManagerView.equals("manager")) {
            commonCodeManager.saveManagerOPList(OperatorShiftStartEndActivity.this, "");

            Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorsListActivity.class);
            startActivity(intent);

        } else {
            Intent intent = new Intent(OperatorShiftStartEndActivity.this, OperatorDailyWorkListActivity.class);
            startActivity(intent);
        }

        List<String> params = new ArrayList<>();
        params.add("");
        params.add("");
        params.add("");

        commonCodeManager.saveFuelStaffPerformId(OperatorShiftStartEndActivity.this, "");
        commonCodeManager.saveParamsForManageOPShift(OperatorShiftStartEndActivity.this, params);

    }

    private void apiCallForGetShiftWiseTotalCreditAmount(final Context context, final String
            fuelStaffPerformId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getCreditAmountForfuelStaffperformId(fuelStaffPerformId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetShiftWiseTotalCreditAmount :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String message = mainObject.getString("msg");

                    if (status.equals("ok")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            JSONObject mainObj = dataArray.getJSONObject(0);
                            totalCRAmt = mainObj.getString("totalCRAmt");

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

    private void updateDetails() {

    }

}
