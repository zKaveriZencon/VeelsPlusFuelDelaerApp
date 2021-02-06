package com.veelsplusfueldealerapp.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.AddProductReceiptActivity;
import com.veelsplusfueldealerapp.activities.ArraivalListActivity;
import com.veelsplusfueldealerapp.activities.NewArrivalListActivity;
import com.veelsplusfueldealerapp.activities.OperatorShiftStartEndActivity;
import com.veelsplusfueldealerapp.activities.ProductReceiptListActivity;
import com.veelsplusfueldealerapp.activities.SetFuelPriceActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.managers.ApiHandlerForErrorLog;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.ErrorLogModel;
import com.veelsplusfueldealerapp.models.NewCreditRequestModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.ShiftWiseCreditModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CorporateFragmentForFuelReq extends Fragment implements View.OnClickListener {
    private static final String TAG = "CorporateFragmentForFue";
    View view;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    TextView textViewDriverName, textViewCustName, textViewFuelType, textViewProdName, textViewFuelPrice, textViewReqAmount,
            textViewReqQuantity, textViewCorpName, textViewCorpLocation,
            textViewTentativeDate, textViewPanNo, labelShift;
    EditText editTextVehicleNo, editTextCustNo, editTextManualCrNo;
    MaterialButton materialButton, buttonAddDriver;
    Spinner spinnerSelectCust, spinnerSelectFuelType, spinnerSelectProduct;
    DatabaseHelper databaseHelper;
    MaterialButton buttonSubmit;
    NewCreditRequestModel newModel;
    String vehicleId, personId = "";
    EditText editTextMobileNo, editTextFirstName, editTextLastName, editTextVehicleID;
    LinearLayout layoutParentCorporate;
    EditText editTextReqQuan, editTextReqAmt, editTextActualQuantity, editTextActualAmount;
    ImageView imageViewReqQuan, imageviewReqAmt, imageviewActualQuan, imageviewActualAmt;
    RadioGroup radioGroupReq, radioGroupActual;
    RadioButton radioBtnReq, radioBtnActual;
    CheckBox checkBoxSameAsReq;
    APIHandlerManager apiHandlerManager;
    RadioButton radioButtonType;
    String fuelSataffPerformIdFinal;
    ApiHandlerForErrorLog apiHandlerForErrorLog;
    Spinner spinnerDuNozzle;
    String selectedProductId = "";
    String message;
    private TextView spinnerErrorText, spinnerErrorTextFuelTypr, spinnerErrorTextProd;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public void onResume() {
        super.onResume();

        String noinfra = commonCodeManager.getNoInfraDetailsMessage(Objects.requireNonNull(getActivity()));
        if (noinfra.equals("noinfra")) {
            buttonSubmit.setEnabled(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Note !");
            builder.setMessage("Unable to get proper Infra Setup data from server. Please check infra setup once or Logout from App and Login again.")

                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            Intent intent = new Intent(getActivity(), ArraivalListActivity.class);
                            startActivity(intent);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            // apiCallForGetRequestDetails(getActivity(), "");
            apiCallForCustomerDropdown(getActivity());
            fillDropDownDataFromLocalDb();


        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_corporate_req_frag, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        databaseHelper = new DatabaseHelper(getActivity());
        apiHandlerManager = new APIHandlerManager(getActivity());

        commonCodeManager = new CommonCodeManager(getActivity());
        commonTaskManager = new CommonTaskManager(getActivity());
        apiHandlerForErrorLog = new ApiHandlerForErrorLog(getActivity());

        //editTextDriverNo = view.findViewById(R.id.edittext_driver_no_nw);
        editTextVehicleNo = view.findViewById(R.id.edittext_vehicle_no_nw);

        textViewDriverName = view.findViewById(R.id.textview_driver_name_nw);
        textViewCustName = view.findViewById(R.id.textview_custname);
        buttonSubmit = view.findViewById(R.id.button_submit_nw);
        textViewCorpName = view.findViewById(R.id.textview_corp_name);
        textViewCorpLocation = view.findViewById(R.id.textview_corp_location);
        textViewTentativeDate = view.findViewById(R.id.textview_tenta_date);
        textViewTentativeDate.setText(commonTaskManager.getCurrentDate()[0]);
        editTextVehicleID = view.findViewById(R.id.edittext_vehicle_id);
        layoutParentCorporate = view.findViewById(R.id.layout_parent_corporate);
        textViewPanNo = view.findViewById(R.id.textview_pan_co);
        editTextManualCrNo = view.findViewById(R.id.edittext_manual_credit_no);

        textViewFuelPrice = view.findViewById(R.id.textview_fuel_price_corp);
        checkBoxSameAsReq = view.findViewById(R.id.checkbox_same_As_req);
        spinnerDuNozzle = view.findViewById(R.id.spinner_select_dunozzle);

        labelShift = view.findViewById(R.id.textview_label_shift);


        ImageView ivSearchVehicle, imageViewSelectDate;
        ivSearchVehicle = view.findViewById(R.id.iv_search_vehicle);
        imageViewSelectDate = view.findViewById(R.id.imageview_calendar_corp);

        spinnerSelectFuelType = view.findViewById(R.id.spinner_select_fueltype);
        spinnerSelectProduct = view.findViewById(R.id.spinner_select_product);
        spinnerSelectCust = view.findViewById(R.id.spinner_select_cust);

        editTextReqQuan = view.findViewById(R.id.edit_req_quan_corp);
        editTextReqAmt = view.findViewById(R.id.edit_req_amt_corp);

        editTextActualQuantity = view.findViewById(R.id.edittext_actual_quan_corp);
        editTextActualAmount = view.findViewById(R.id.edittext_actual_amt_corp);

        radioGroupReq = (RadioGroup) view.findViewById(R.id.radiogr_required_corp);
        radioGroupActual = (RadioGroup) view.findViewById(R.id.radiogr_actual_corp);

        imageViewReqQuan = view.findViewById(R.id.imageview_req_quan_corp);
        imageviewReqAmt = view.findViewById(R.id.imageview_req_amt_corp);
        imageviewActualQuan = view.findViewById(R.id.imageview_actual_quan_corp);
        imageviewActualAmt = view.findViewById(R.id.imageview_actual_amt_corp);

        imageViewReqQuan.setOnClickListener(this);
        imageviewReqAmt.setOnClickListener(this);
        imageviewActualQuan.setOnClickListener(this);
        imageviewActualAmt.setOnClickListener(this);


        checkBoxSameAsReq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editTextActualQuantity.setText(editTextReqQuan.getText().toString());
                    editTextActualAmount.setText(editTextReqAmt.getText().toString());
                }

            }
        });


        radioGroupReq.clearCheck();
        radioGroupReq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioBtnReq = (RadioButton) group.findViewById(checkedId);
                if (null != radioBtnReq) {
                    String whichBtn = radioBtnReq.getText().toString().toLowerCase().trim();
                    Log.d(TAG, "onCheckedChanged:whichBtnReq:  " + whichBtn);


                    if (whichBtn.contains("quantity")) {
                        editTextReqAmt.setText("");
                        editTextReqQuan.setText("");

                        editTextReqAmt.setEnabled(false);
                        editTextReqQuan.setEnabled(true);

                        editTextReqAmt.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                        editTextReqQuan.setBackgroundColor(Color.TRANSPARENT);
                        editTextReqAmt.setHint("Required Amount");
                        editTextReqQuan.setHint("Enter Quantity");

                        imageviewReqAmt.setEnabled(false);
                        imageViewReqQuan.setEnabled(true);

                    }

                    if (whichBtn.contains("amount")) {
                        editTextReqQuan.setText("");
                        editTextReqAmt.setText("");

                        editTextReqAmt.setEnabled(true);
                        editTextReqQuan.setEnabled(false);

                        editTextReqQuan.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                        editTextReqAmt.setBackgroundColor(Color.TRANSPARENT);

                        editTextReqQuan.setHint("Required Quantity");
                        editTextReqAmt.setHint("Enter Amount");

                        imageviewReqAmt.setEnabled(true);
                        imageViewReqQuan.setEnabled(false);

                    }

                }
            }
        });
        radioGroupActual.clearCheck();
        radioGroupActual.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioBtnActual = (RadioButton) group.findViewById(checkedId);

                if (null != radioBtnActual) {

                    String whichBtn = radioBtnActual.getText().toString().toLowerCase().trim();
                    Log.d(TAG, "onCheckedChanged:whichBtnactual:  " + whichBtn);

                    if (whichBtn.contains("quantity")) {
                        editTextActualAmount.setText("");
                        editTextActualQuantity.setText("");

                        editTextActualAmount.setEnabled(false);
                        editTextActualQuantity.setEnabled(true);

                        editTextActualAmount.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                        editTextActualQuantity.setBackgroundColor(Color.TRANSPARENT);

                        editTextActualAmount.setHint("Actual Amount");
                        editTextActualQuantity.setHint("Enter Quantity");

                        imageviewActualAmt.setEnabled(false);
                        imageviewActualQuan.setEnabled(true);


                    }

                    if (whichBtn.contains("amount")) {
                        editTextActualQuantity.setText("");
                        editTextActualAmount.setText("");

                        editTextActualAmount.setEnabled(true);
                        editTextActualQuantity.setEnabled(false);

                        editTextActualQuantity.setBackgroundColor(getResources().getColor(R.color.colorRowBg));
                        editTextActualAmount.setBackgroundColor(Color.TRANSPARENT);

                        editTextActualQuantity.setHint("Actual Quantity");
                        editTextActualAmount.setHint("Enter Amount");

                        imageviewActualQuan.setEnabled(false);
                        imageviewActualAmt.setEnabled(true);
                    }

                }


            }
        });


        imageViewSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Objects.requireNonNull(getActivity()),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                //String date = day + "-" + month + "-" + year;
                String sDay = "";
                if (day < 10) {
                    sDay = "0" + String.valueOf(day);
                } else {
                    sDay = String.valueOf(day);
                }

                String sMonth = "";
                if (month < 10) {
                    sMonth = "0" + String.valueOf(month);
                } else {
                    sMonth = String.valueOf(month);
                }
                String date = year + "-" + sMonth + "-" + sDay;

                textViewTentativeDate.setText(date);
                Log.d(TAG, "onDateSet:textViewTentativeDate :  " + date);

            }
        };


        ivSearchVehicle.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);


        materialButton = view.findViewById(R.id.button_submitm);

        layoutParentCorporate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                commonTaskManager.hideKeyboard(getActivity());
                return false;
            }
        });
    }


    private void apiCallForCustomerDropdown(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        final String[] details = commonCodeManager.getDealerDetails(context);
        Log.d(TAG, "apiCallForCustomerDropdown:dealerid :  " + details[1]);

        final List<NewCreditRequestModel> custDetailsList = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getCorporatesAllMappedRequestByDealer(details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForCustomerDropdown :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);
                        //need to check
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String fuelDealerCustomerMapId = duObject.getString("fuelDealerCustomerMapId");
                                String fuelCorporateId = duObject.getString("fuelCorporateId");
                                if (i == 0) {
                                    newModel = new NewCreditRequestModel("", "", "Select Customer");
                                    custDetailsList.add(newModel);

                                }
                                newModel = new NewCreditRequestModel(fuelDealerCustomerMapId, fuelCorporateId, companyName);


                                custDetailsList.add(newModel);
                                //commonCodeManager.saveFuelCreditDetails(context, fuelDealerCustomerMapId,fuelCorporateId);

                            }
                            updateUIAsPerCustomerAPI(custDetailsList);

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
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    String finalMessage = message + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage + " :API Name : Customer dropdown", Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage + " :API Name :Customer dropdown", Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });


    }

    private void updateUIAsPerCustomerAPI(final List<NewCreditRequestModel> custDetailsList) {
        String[] custDetailsArr = new String[custDetailsList.size()];

        for (int i = 0; i < custDetailsList.size(); i++) {
            NewCreditRequestModel newCreditRequestModel = custDetailsList.get(i);
            custDetailsArr[i] = newCreditRequestModel.getCompanyName();
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, custDetailsArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectCust.setAdapter(arrayAdapter1);

        spinnerSelectCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFuelType = (String) spinnerSelectCust.getSelectedItem();
                spinnerErrorText = (TextView) spinnerSelectCust.getSelectedView();

                if (selectedFuelType.contains("Select Customer")) {

                } else {
                    NewCreditRequestModel newCreditRequestModel = custDetailsList.get(position);

                    commonCodeManager.saveFuelCreditDetails(Objects.requireNonNull(getActivity()),
                            newCreditRequestModel.getFuelDealerCustomerMapId(),
                            newCreditRequestModel.getFuelCorporateId());


                    String fuelDealerCustMapId = newCreditRequestModel.getFuelDealerCustomerMapId();
                    if (fuelDealerCustMapId != null && !fuelDealerCustMapId.isEmpty() && !fuelDealerCustMapId.equals("null") && !fuelDealerCustMapId.equals("undefined")) {
                        apiCallForGetCorporateInfoByfuelDealerCustomerMapId(getActivity(), fuelDealerCustMapId);

                    } else {
                      showMessageIfCustomerMapIdNotFound(fuelDealerCustMapId);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void showMessageIfCustomerMapIdNotFound( String fuelDealerCustMapId) {

        String[] details = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
        final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], fuelDealerCustMapId + " : get corp info else part : ", Constants.type_custom, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, details[1]);
        apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

        final Snackbar snackbar = Snackbar
                .make(layoutParentCorporate, "Unable to get details from server, Please try later !", Snackbar.LENGTH_INDEFINITE);
        View view1 = snackbar.getView();
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view1.getLayoutParams();
        params1.gravity = Gravity.CENTER;
        view1.setLayoutParams(params1);
        view1.setBackgroundColor(getResources().getColor(R.color.colorZencon));
        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    private void apiCallForGetCorporateInfoByfuelDealerCustomerMapId(final Context context,
                                                                     String custMapId) {

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForGetCorporateInfoByfuelDealerCustomerMapId:custMapId :  " + custMapId);
        final String[] message = {""};

        final String[] details = commonCodeManager.getDealerDetails(context);


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getCorporateInfoByfuelDealerCustomerMapId(custMapId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse: apiCallForGetCorporateInfoBy :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    if (status.equals("ok")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            JSONObject detailsObj = dataArray.getJSONObject(0);
                            final String PANno = detailsObj.getString("PANno");
                            personId = detailsObj.getString("personId");
                            final String firstName = detailsObj.getString("firstName");
                            final String lastName = detailsObj.getString("lastName");
                            final String corpNAme = firstName + lastName;

                            Log.d(TAG, "onResponse: person id : " + personId);

                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    textViewPanNo.setText(PANno);
                                    textViewDriverName.setText(corpNAme);


                                }
                            });
                        }

                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    // String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    Log.d(TAG, "onResponse: e : corp info : " + e.getLocalizedMessage());
                    e.printStackTrace();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], e.getLocalizedMessage() + " : Get Corporate Info", Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, details[0], finalMessage + " : Get Corporate Info", Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });

    }

    private void fillDropDownDataFromLocalDb() {
        //  List<String> fuelTypes = databaseHelper.getFuelType();
        final List<ShiftWiseCreditModel> fuelTypes = databaseHelper.getWholeProductName();

        String[] fuelTypesArr = new String[fuelTypes.size()];
        for (int i = 0; i < fuelTypes.size(); i++) {
            ShiftWiseCreditModel shiftWiseCreditModel = fuelTypes.get(i);
            fuelTypesArr[i] = shiftWiseCreditModel.getFullProductName();
        }

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, fuelTypesArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectFuelType.setAdapter(arrayAdapter1);

        spinnerSelectFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFuelType = (String) spinnerSelectFuelType.getSelectedItem();
                spinnerErrorTextFuelTypr = (TextView) spinnerSelectFuelType.getSelectedView();
                if (selectedFuelType.contains("Select Product")) {

                } else {
                    Log.d(TAG, "onItemSelected:selectedFuelType :  " + selectedFuelType);

                    ShiftWiseCreditModel shiftWiseCreditModel = fuelTypes.get(position);
                    Log.d(TAG, "onItemSelected:shiftWiseCreditModel.getFuelType() :  " + shiftWiseCreditModel.getFuelType());
                    Log.d(TAG, "onItemSelected:shiftWiseCreditModel.getProductName() :  " + shiftWiseCreditModel.getProductName());
                    selectedProductId = databaseHelper.getProductId(shiftWiseCreditModel.getFuelType(), shiftWiseCreditModel.getProductName());
                    Log.d(TAG, "onItemSelected: selectedProductId : " + selectedProductId);

                    if (selectedProductId != null && !selectedFuelType.isEmpty()) {
                        apiCallForGetTodaysFuelPrice(getActivity(), selectedProductId);

                    } else {
                        Toast.makeText(getActivity(), "Error while getting product information", Toast.LENGTH_SHORT).show();
                    }
                  /*  List<String> prodNamesList = databaseHelper.getProductNameByFuelType(selectedFuelType);
                    fillProductdropdown(prodNamesList, selectedFuelType);*/
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search_vehicle:
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));

                String vehicleNo = editTextVehicleNo.getText().toString();
                if (!vehicleNo.isEmpty()) {
                    editTextVehicleID.setText("");
                    apiCallForSearchVehicleNumber(getActivity(), vehicleNo);
                } else {
                    editTextVehicleNo.setError("Please Enter Vehicle Number");
                    Toast.makeText(getActivity(), "Please Enter Vehicle Number", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.button_submit_nw:
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));
                String fuelRateText = textViewFuelPrice.getText().toString();

                if (fuelRateText != null && !fuelRateText.equals("null") &&
                        !fuelRateText.equals("undefined") && !fuelRateText.isEmpty()) {

                    //check for any ongoing shifts
                    if (fuelSataffPerformIdFinal != null && !fuelSataffPerformIdFinal.isEmpty() && !fuelSataffPerformIdFinal.contains("no")) {
                        //there is ongoing shift


                        if (areValidateForAddNewRequest()) {

                            String message = "Are you sure want to confirm submit Values ?";
                            sendProperMessageForSubmission(message);
                        }
                    } else {
                        //no ongoing shifts running
                        showAlertForNoOngoingShifts("Selected fuel type.");

                    }
                } else {
                    Log.d(TAG, "onClick: fuelRateText if");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Can't Proceed !");
                    builder.setMessage("Fuel price not assigned. Please set fuel price first. Please contact manager")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;

            case R.id.imageview_actual_quan_corp:
                //for calcualating required quantity

                Log.d(TAG, "onClick: imageview_calc_amt_per");
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

            case R.id.imageview_actual_amt_corp:

                Log.d(TAG, "onClick: imageview_calc_quan_per");
                //for calcualating required amount

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

                }


                break;

            case R.id.imageview_req_quan_corp:
                //for calcualating required quantity

                Log.d(TAG, "onClick: imageview_calc_amt_per");
                String aqTextReq = editTextReqQuan.getText().toString();

                String fuelPriceTextReq = textViewFuelPrice.getText().toString();


                if (fuelPriceTextReq != null && !fuelPriceTextReq.isEmpty() && !fuelPriceTextReq.equals("0")) {
                    if (!aqTextReq.isEmpty()) {
                        double actualQuantity = Double.parseDouble(editTextReqQuan.getText().toString());

                        double fuelPrice = Double.parseDouble(textViewFuelPrice.getText().toString());
                        double actualAmount = fuelPrice * actualQuantity;

                        String actualAmountValue = "" + String.format("%.2f", actualAmount);
                        editTextReqAmt.setText(actualAmountValue);
                    }

                }

                break;

            case R.id.imageview_req_amt_corp:

                Log.d(TAG, "onClick: imageview_calc_quan_per");
                //for calcualating required amount

                String amTextReq = editTextReqAmt.getText().toString();


                String fuelPriceTextReqa = textViewFuelPrice.getText().toString();

                if (fuelPriceTextReqa != null && !fuelPriceTextReqa.isEmpty() && !fuelPriceTextReqa.equals("0")) {

                    if (!amTextReq.isEmpty()) {
                        double actualAmount = Double.parseDouble(editTextReqAmt.getText().toString());

                        double fuelPrice1 = Double.parseDouble(textViewFuelPrice.getText().toString());
                        double actualQuantity1 = actualAmount / fuelPrice1;
                        String actualQuantityValue = "" + String.format("%.2f", actualQuantity1);

                        editTextReqQuan.setText(actualQuantityValue);
                    }

                }


                break;


        }
    }

    private void sendNewFuelCreditRequestDetails() {
        // String personNo = editTextDriverNo.getText().toString();
        String vehicleNo = editTextVehicleNo.getText().toString();
        String custName = "";
        String personName = textViewDriverName.getText().toString();
        String fuelType = (String) spinnerSelectFuelType.getSelectedItem();
        String productSubType = (String) spinnerSelectProduct.getSelectedItem();
     /*   String quantity = editTextQuantity.getText().toString();
        String amount = editTextAmount.getText().toString();*/
        String productName = fuelType + productSubType;
        String quantity = editTextReqQuan.getText().toString();
        String amount = editTextReqAmt.getText().toString();
        String actualQuantity = editTextActualQuantity.getText().toString();
        String actualAmount = editTextActualAmount.getText().toString();
        String manualCrNo = editTextManualCrNo.getText().toString();

        String[] fuelDetalils = commonCodeManager.getFuelCreditDetails(Objects.requireNonNull(getActivity()));
        final String[] dealerDetails = commonCodeManager.getDealerDetails(getActivity());

        Log.d(TAG, "sendNewFuelCreditRequestDetails: vehicle Id : " + vehicleId);
        NewCreditRequestModel newModel;
        String finalVehicleId = "";
        Log.d(TAG, "sendNewFuelCreditRequestDetails: personid corp : " + personId);
        Log.d(TAG, "sendNewFuelCreditRequestDetails: name corp : " + personName);
        Log.d(TAG, "sendNewFuelCreditRequestDetails:fuelSataffPerformIdFinal:  " + fuelSataffPerformIdFinal);

        if (personId != null && !personId.equals("null") && !personId.isEmpty() && personId.equals("undefined")) {
            final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], personId, "PersonID : " + personId, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, dealerDetails[1]);
            apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

        }

        if (personId != null && !personId.equals("null") && !personId.isEmpty()) {

            if (vehicleId == null || vehicleId.equals("null") || vehicleId.isEmpty() || vehicleId.equals("undefined")) {
                finalVehicleId = "";
            }
            Log.d(TAG, "sendNewFuelCreditRequestDetails: date check : " + commonTaskManager.getCurrentDateNew());

            if (vehicleId != null && !vehicleId.equals("null") && !vehicleId.isEmpty() && !vehicleId.equals("undefined")) {

                Log.d(TAG, "sendNewFuelCreditRequestDetails: textViewPanNo : " + textViewPanNo.getText().toString());


                // if (fuelSataffPerformIdFinal != null && !fuelSataffPerformIdFinal.equals("null") && !fuelSataffPerformIdFinal.isEmpty() && !fuelSataffPerformIdFinal.equals("undefined")) {

                newModel = new NewCreditRequestModel(fuelDetalils[0],
                        quantity, amount, commonTaskManager.getCurrentDateNew(),
                        dealerDetails[1],
                        finalVehicleId,
                        personId, "", selectedProductId,
                        fuelDetalils[1], "DEALER", textViewPanNo.getText().toString(),
                        commonTaskManager.getCurrentDateNew(),
                        editTextVehicleNo.getText().toString(), "TRUE", commonTaskManager.getCurrentDateNew(),
                        commonTaskManager.getCurrentTime(), actualAmount, "COMPLETE", dealerDetails[0],
                        actualQuantity, textViewFuelPrice.getText().toString(), manualCrNo, fuelSataffPerformIdFinal);
            } else {
                newModel = new NewCreditRequestModel(fuelDetalils[0],
                        quantity, amount, commonTaskManager.getCurrentDateNew(),
                        dealerDetails[1],
                        finalVehicleId,
                        personId, "", selectedProductId,
                        fuelDetalils[1], "DEALER", textViewPanNo.getText().toString(),
                        commonTaskManager.getCurrentDateNew(),
                        editTextVehicleNo.getText().toString(), "FALSE", commonTaskManager.getCurrentDateNew(),
                        commonTaskManager.getCurrentTime(), actualAmount, "COMPLETE", dealerDetails[0],
                        actualQuantity, textViewFuelPrice.getText().toString(), manualCrNo, fuelSataffPerformIdFinal);
            }


            final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            progressDialog.setMessage(getResources().getString(R.string.sending_request));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final String[] message = {""};

            GetDataService service = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GetDataService.class);
            Call<JsonObject> call = service.addNewCreditReqByCorp(newModel);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse: jsondata add new : " + jsonData);
                    try {
                        JSONObject mainObject = new JSONObject(jsonData);
                        final String status = mainObject.getString("status");
                        final String msg = mainObject.getString("msg");

                        if (status.equals("OK")) {
                            // commonTaskManager.dismissDialogWithToast(getActivity(), progressDialog, msg);
                            Handler handler1 = new Handler(Objects.requireNonNull(getActivity()).getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();

                                    }
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), ArraivalListActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            commonTaskManager.dismissDialogWithToast(Objects.requireNonNull(getActivity()), progressDialog, msg);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        commonTaskManager.dismissProgressDialog(Objects.requireNonNull(getActivity()), progressDialog);
                        String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                        final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, dealerDetails[1]);
                        apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                    commonTaskManager.dismissProgressDialog(Objects.requireNonNull(getActivity()), progressDialog);
                    String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, dealerDetails[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                }

            });

        } else {
            final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], personId, "Check Empty ID ", commonTaskManager.getCurrentDateTimeNewFormat(), Constants.op_shift, dealerDetails[1]);
            apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);
        }


    }
//}
    /*else {
            Toast.makeText(getActivity(), "Please search vehicle number", Toast.LENGTH_SHORT).show();
        }*/


    private void apiCallForSearchVehicleNumber(final Context context, String vehicleNo) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.searchVehicleByRegistrationNumber(vehicleNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchVehicleNumber :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    if (!status.contains("not")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject duObject = dataArray.getJSONObject(0);
                        String ownerName = duObject.getString("ownerName");
                        vehicleId = duObject.getString("vehicleId");

                        editTextVehicleID.setText(vehicleId);

                        //updateUiAsPerVehicleDetails(ownerName);

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Number Not Found");

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

    private boolean areValidateForAddNewRequest() {
        boolean result = false;
        String selectedCust = (String) spinnerSelectCust.getSelectedItem();
        Log.d(TAG, "areValidateForAddNewRequest: selectedCust : " + selectedCust);
        Log.d(TAG, "areValidateForAddNewRequest: in method : " + checkBoxSameAsReq.isChecked());
        String corporateName = textViewDriverName.getText().toString();
        if (selectedCust != null && !selectedCust.equals("null") && !selectedCust.equals("undefined")) {
            selectedCust = "";
        }
        String selectedFuelType = (String) spinnerSelectFuelType.getSelectedItem();
        String selectedProductName = (String) spinnerSelectProduct.getSelectedItem();

        if (Objects.requireNonNull(selectedCust).contains("Select Customer")) {
            result = false;
            // spinnerErrorText.setError("Please Select Customer");
            Toast.makeText(getActivity(), "Please Select Customer", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(corporateName)) {
            result = false;
            // spinnerErrorTextFuelTypr.setError("Please Select Fuel Type");
            Toast.makeText(getActivity(), "Corporate not found.Please select customer", Toast.LENGTH_LONG).show();
        } else if (selectedFuelType.contains("Select Fuel Type")) {
            result = false;
            // spinnerErrorTextFuelTypr.setError("Please Select Fuel Type");
            Toast.makeText(getActivity(), "Please Select Fuel Type", Toast.LENGTH_LONG).show();
        } /*else if (selectedProductName.contains("Select Product")) {
            result = false;
            // spinnerErrorTextProd.setError("Please Select Product");
            Toast.makeText(getActivity(), "Please Select Product", Toast.LENGTH_LONG).show();

        } */ else if (TextUtils.isEmpty(textViewFuelPrice.getText().toString())) {
            result = false;
            //textViewFuelPrice.setError("Please select fuel type and product name again");
            Toast.makeText(getActivity(), "Please select fuel type and product name again", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(textViewTentativeDate.getText().toString())) {
            result = false;
            //textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_LONG).show();
        } else if (radioGroupReq.getCheckedRadioButtonId() == -1) {
            result = false;
            //  textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please select either Required Quantity or Required Amount", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextReqQuan.getText().toString()) || TextUtils.isEmpty(editTextReqAmt.getText().toString())) {
            result = false;
            //  textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please enter either Required Quantity or Required Amount and calcualte", Toast.LENGTH_LONG).show();
        } else if (checkBoxSameAsReq.isChecked()) {
            result = true;
        } else {
            result = false;

            Log.d(TAG, "areValidateForAddNewRequest: in if : " + checkBoxSameAsReq.isChecked());
            if (radioGroupActual.getCheckedRadioButtonId() == -1) {
                result = false;
                //  textViewTentativeDate.setError("Please Select Date");
                Toast.makeText(getActivity(), "Please select either Actual Quantity or Actual Amount", Toast.LENGTH_LONG).show();
            } else if (TextUtils.isEmpty(editTextActualQuantity.getText().toString()) || TextUtils.isEmpty(editTextActualAmount.getText().toString())) {
                result = false;
                Toast.makeText(getActivity(), "Please enter either Actual Quantity or Actual Amount and calcualte", Toast.LENGTH_LONG).show();
            } else {
                result = true;
            }
        }
        Log.d(TAG, "areValidateForAddNewRequest: resule final : " + result);
        return result;
    }


    private void apiCallForGetTodaysFuelPrice(final Context context, final String fuelProdId) {


        Log.d(TAG, "apiCallForGetTodaysFuelPrice:fuelProdId :  " + fuelProdId);
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait, Retrieving Today's Fuel Price");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dealerDetails = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};

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

                            String[] details = commonCodeManager.getDealerDetails(context);

                            //for shift wise credit
                            apiCallForGetOperatorShiftDetails(getActivity(), details[0], fuelProdId);
                        } else {
                            commonTaskManager.showToast(context, "No fuel price found");
                        }


                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    String finalMessage = message[0] + " : " + e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], finalMessage, Constants.type_exec, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, dealerDetails[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message[0] + " : " + t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME, dealerDetails[0], finalMessage, Constants.type_failure, commonTaskManager.getCurrentDateTimeNewFormat(), Constants.refuel_corporate, dealerDetails[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(context, errorLogModel);

            }

        });
    }

    private void sendProperMessageForSubmission(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));

        builder.setTitle("Confirm Submit !");
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sendNewFuelCreditRequestDetails();

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

    private void apiCallForGetOperatorShiftDetails(final Context context,
                                                   String fuelDealerStaffId, final String fuelProdId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForGetOperatorShiftDetails:fuelDealerStaffId  :" + fuelDealerStaffId);
        final List<OperatorDailyWorkListModel> dailyWorkList = new ArrayList<>();
        final List<ShiftWiseCreditModel> fuelInfraMapIdsShiftWise = new ArrayList<>();
        final List<String> fuelInfraMapIdsShiftWiseOnly = new ArrayList<>();
        final List<ShiftWiseCreditModel> startedShiftDetailsProductWise = new ArrayList<>();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getOpertaorShiftDetails(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOperatorShiftDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String msg = mainObject.getString("msg");

                    if (status.equals("OK")) {

                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);

                                String openMetDateTime = duObject.getString("openMetDateTime");
                                String pumpNo = duObject.getString("duNo");
                                String nozzleNo = duObject.getString("nozNo");
                                String performStatus = duObject.getString("performStatus").toLowerCase().trim();
                                String recoveryStatus = duObject.getString("recoveryStatus");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                                String fuelInfraMapId = duObject.getString("fuelInfraMapId");
                                String productCategory = duObject.getString("productCategory");
                                String productName = duObject.getString("productName");
                                String fuelProductsId = duObject.getString("fuelProductsId");
                                Log.d(TAG, "onResponse: selected product id : " + fuelProdId);
                                String finalProductName = productCategory + "-" + productName;
                                String duNozzle = pumpNo + nozzleNo;
                                String duNozzleProduct = duNozzle + ":" + finalProductName;

                                String dateOnly = "";

                                if (openMetDateTime != null && !openMetDateTime.equals("null") && !openMetDateTime.isEmpty() && openMetDateTime.contains("T")) {
                                    String[] datetime = openMetDateTime.split("T");
                                    dateOnly = datetime[0];

                                } else {
                                    dateOnly = "";
                                }
                                Log.d(TAG, "onResponse: performStatus : " + performStatus);

                                if (performStatus.equals("started") && fuelProdId.equals(fuelProductsId)) {
                                    Log.d(TAG, "onResponse: ");
                                    ShiftWiseCreditModel shiftWiseCreditModel = new ShiftWiseCreditModel(fuelInfraMapId, fuelStaffPerformId);
                                    fuelInfraMapIdsShiftWise.add(shiftWiseCreditModel);
                                    fuelInfraMapIdsShiftWiseOnly.add(fuelInfraMapId);


                                    if (i == 0) {
                                        ShiftWiseCreditModel shiftWiseCreditModel1 = new ShiftWiseCreditModel("", "", "", "", "Select Shift","","");
                                        startedShiftDetailsProductWise.add(shiftWiseCreditModel1);
                                    }
                                    ShiftWiseCreditModel shiftWiseCreditModel1 = new ShiftWiseCreditModel(fuelInfraMapId, fuelStaffPerformId, finalProductName, duNozzle, duNozzleProduct,"","");
                                    startedShiftDetailsProductWise.add(shiftWiseCreditModel1);
                                }
                                OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(dateOnly,
                                                duNozzle, performStatus, recoveryStatus, fuelStaffPerformId);


                                dailyWorkList.add(dailyWorkListModel);
                            }
                            getOngoingShiftDetails(dailyWorkList, fuelInfraMapIdsShiftWise,
                                    fuelInfraMapIdsShiftWiseOnly, startedShiftDetailsProductWise);

                        } else {
                            showAlertForNoOngoingShifts("");
                            //no data found
                            //imageViewNoData.setVisibility(View.VISIBLE);
                            //commonTaskManager.showToast(context, "No Data Found");
                        }

                    } else {
                        commonTaskManager.showToast(context, msg);
                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, context.getResources().getString(R.string.unable_to_connect));
                t.printStackTrace();
            }

        });
    }

    private void getOngoingShiftDetails(List<OperatorDailyWorkListModel> dailyWorkList,
                                        List<ShiftWiseCreditModel> fuelInfraMapIdsShiftWise,
                                        List<String> fuelInfraMapIdsShiftWiseOnly,
                                        final List<ShiftWiseCreditModel> startedShiftDetailsProductWise) {


        String selectedFuelType = (String) spinnerSelectFuelType.getSelectedItem();
        String selectedProductName = (String) spinnerSelectProduct.getSelectedItem();
        Log.d(TAG, "getOngoingShiftDetails:fuelInfraMapIdsShiftWise size before:  " + fuelInfraMapIdsShiftWiseOnly.size());


        //new- logic for getting shiftwise fuel credit request details
        List<String> inframapIdsList = databaseHelper.getInfraMapIdByProdCategoryAndProdName
                (selectedFuelType, selectedProductName);
        fuelInfraMapIdsShiftWiseOnly.retainAll(inframapIdsList);

        Log.d(TAG, "getOngoingShiftDetails: Common elements are : " + fuelInfraMapIdsShiftWise);
        Log.d(TAG, "getOngoingShiftDetails:fuelInfraMapIdsShiftWise size :  " + fuelInfraMapIdsShiftWiseOnly.size());
        Log.d(TAG, "getOngoingShiftDetails: inframapIdsList size : " + inframapIdsList.size());

        //logic for ui
        if (startedShiftDetailsProductWise.size() > 0) {

            labelShift.setVisibility(View.VISIBLE);
            spinnerDuNozzle.setVisibility(View.VISIBLE);

            //atleast one shift must be started
          /*  String fuelInfraId = fuelInfraMapIdsShiftWiseOnly.get(0);
            Log.d(TAG, "getOngoingShiftDetails:fuelInfraId : " + fuelInfraId);
           */
            String[] arrFuelInfrasShiftWise = new String[startedShiftDetailsProductWise.size()];

            for (int i = 0; i < startedShiftDetailsProductWise.size(); i++) {
                ShiftWiseCreditModel shiftWiseCreditModel = startedShiftDetailsProductWise.get(i);
                arrFuelInfrasShiftWise[i] = shiftWiseCreditModel.getDuNozzleProduct();
            }
            ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, arrFuelInfrasShiftWise);
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDuNozzle.setAdapter(arrayAdapter1);
            spinnerDuNozzle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedShift = (String) spinnerDuNozzle.getSelectedItem();
                    if (selectedShift.contains("Select Shift")) {

                    } else {

                        ShiftWiseCreditModel shiftWiseCreditModel = startedShiftDetailsProductWise.get(position);
                        Log.d(TAG, "onItemSelected: final Shift perform id : " + shiftWiseCreditModel.getFuelStaffPerformId());
                        fuelSataffPerformIdFinal = shiftWiseCreditModel.getFuelStaffPerformId();


                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


        } else {
            //no shifts running
            fuelSataffPerformIdFinal = "no ongoing shift";
            showAlertForNoOngoingShifts(selectedFuelType);
            //  Toast.makeText(getActivity(), "No ongoing shifts", Toast.LENGTH_SHORT).show();

        }

        /*
        if (fuelInfraMapIdsShiftWiseOnly.size() > 0) {
            //atleast one shift started
            String fuelInfraId = fuelInfraMapIdsShiftWiseOnly.get(0);
            Log.d(TAG, "getOngoingShiftDetails:fuelInfraId : " + fuelInfraId);

            for (int i = 0; i < fuelInfraMapIdsShiftWise.size(); i++) {
                ShiftWiseCreditModel shiftWiseCreditModel = fuelInfraMapIdsShiftWise.get(i);
                Log.d(TAG, "getOngoingShiftDetails: FuelInfraMapId :" + shiftWiseCreditModel.getFuelInfraMapId());

                if (fuelInfraId.equals(shiftWiseCreditModel.getFuelInfraMapId())) {
                    String fuelStaffPerformId = shiftWiseCreditModel.getFuelStaffPerformId();
                    Log.d(TAG, "getOngoingShiftDetails: fuelStaffPerformId : " + fuelStaffPerformId);

                    //fuelstaffperformid for api
                    fuelSataffPerformIdFinal = fuelStaffPerformId;
                } else {
                }
            }


        } else {
            //no shifts running


            Toast.makeText(getActivity(), "No ongoing shifts", Toast.LENGTH_SHORT).show();

        }*/


    }

    private void showAlertForNoOngoingShifts(String selectedFuelType) {
        //no option for start shift for manager
        String message = "";
        String[] details = commonCodeManager.getEssentialsForDealer(Objects.requireNonNull(getActivity()));
        String designation = details[4].toLowerCase().trim();
        Log.d(TAG, "showAlertForNoOngoingShifts:designation :  " + designation);

        if (designation != null && !designation.equals("null") && !designation.isEmpty() && designation.equals("operator")) {

            labelShift.setVisibility(View.GONE);
            spinnerDuNozzle.setVisibility(View.GONE);

            message = "You don't have ongoing shift for selected product : " + selectedFuelType + ". You have to start shift first";
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("No Ongoing Shifts !");
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Start Shift", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            List<String> params = new ArrayList<>();
                            params.add("");
                            params.add("");
                            params.add("");

                            commonCodeManager.saveFuelStaffPerformId(Objects.requireNonNull(getActivity()), "");
                            commonCodeManager.saveParamsForManageOPShift(getActivity(), params);
                            commonCodeManager.saveIfCameFromCorporateOrPerson(Objects.requireNonNull(getActivity()), "corporate");
                            Intent intent = new Intent(getActivity(), OperatorShiftStartEndActivity.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            labelShift.setVisibility(View.GONE);
            spinnerDuNozzle.setVisibility(View.GONE);
            message = "No ongoing shifts found";
            AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
            builder.setTitle("No Ongoing Shifts !");
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            List<String> params = new ArrayList<>();
                            params.add("");
                            params.add("");
                            params.add("");

                          /*  commonCodeManager.saveFuelStaffPerformId(Objects.requireNonNull(getActivity()), "");
                            commonCodeManager.saveParamsForManageOPShift(getActivity(), params);
                            commonCodeManager.saveIfCameFromCorporateOrPerson(Objects.requireNonNull(getActivity()), "corporate");
                            */
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

}
