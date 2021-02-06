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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.ArraivalListActivity;
import com.veelsplusfueldealerapp.activities.NewArrivalListActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.managers.ApiHandlerForErrorLog;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.ErrorLogModel;
import com.veelsplusfueldealerapp.models.MappedPersonModel;
import com.veelsplusfueldealerapp.models.NewCreditRequestModel;
import com.veelsplusfueldealerapp.models.PersonModelNew;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonFragmentForFuelReq extends Fragment implements View.OnClickListener {
    private static final String TAG = "PersonFragmentForFuelRe";
    View view;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    TextView textViewDriverName, textViewFuelType, textViewProdName, textViewPrice,
            textViewReqAmount, textViewReqQuantity, textViewTentativeDate,
            textViewPanNo, textViewCustMapId;
    EditText editTextVehicleNo, editTextQuantity;
    MaterialButton materialButton, buttonAddDriver;
    Spinner spinnerSelectFuelType, spinnerSelectProduct, spinnerMappedPerson;
    DatabaseHelper databaseHelper;
    MaterialButton buttonSubmit;
    NewCreditRequestModel newModel;
    String vehicleId, personId = "";
    EditText editTextMobileNo, editTextFirstName, editTextLastName, editTextVehicleID;
    boolean isPersonIdFound = false;
    EditText editTextReqQuan, editTextReqAmt, editTextActualQuantity, editTextActualAmount;
    TextView textViewFuelPrice;
    ImageView imageViewReqQuan, imageviewReqAmt, imageviewActualQuan, imageviewActualAmt;
    RadioGroup radioGroupReq, radioGroupActual;
    RadioButton radioBtnReq, radioBtnActual;
    CheckBox checkboxSameAsReq;
    MappedPersonModel mappedPersonModel;
    private TextView spinnerErrorText, spinnerErrorTextFuelTypr, spinnerErrorTextProd;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    ApiHandlerForErrorLog apiHandlerForErrorLog;

    @Override
    public void onResume() {
        super.onResume();
        //need to check

        apiCallForGetMappedPersons(getActivity());

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
            fillDropDownDataFromLocalDb();

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_person_req_frag, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        databaseHelper = new DatabaseHelper(getActivity());

        commonCodeManager = new CommonCodeManager(getActivity());
        commonTaskManager = new CommonTaskManager(getActivity());
        apiHandlerForErrorLog = new ApiHandlerForErrorLog(getActivity());


        //editTextDriverNo = view.findViewById(R.id.edittext_driver_no_per);
        editTextVehicleNo = view.findViewById(R.id.edittext_vehicle_no_per);
        // editTextAmount = view.findViewById(R.id.textview_amount_nw_per);
        textViewDriverName = view.findViewById(R.id.textview_driver_name_per);
        buttonSubmit = view.findViewById(R.id.button_submit_nw_per);
        textViewTentativeDate = view.findViewById(R.id.textview_date_per);

        editTextVehicleID = view.findViewById(R.id.edittext_vehicle_id_per);

        textViewPanNo = view.findViewById(R.id.textview_pan);
        textViewCustMapId = view.findViewById(R.id.textview_custmap_id);
        textViewFuelPrice = view.findViewById(R.id.textview_fuel_price_per);
        checkboxSameAsReq = view.findViewById(R.id.checkbox_same_As_req_per);
        spinnerMappedPerson = view.findViewById(R.id.spinner_mapped_per);


        //ImageView ivSearchDriver, ivSearchVehicle, ivAddDriver, imageViewSelectDate;
        ImageView ivSearchVehicle, imageViewSelectDate;

        //ivSearchDriver = view.findViewById(R.id.iv_search_driver_per);
        //ivAddDriver = view.findViewById(R.id.iv_add_driver_per);
        ivSearchVehicle = view.findViewById(R.id.iv_search_vehicle_per);
        imageViewSelectDate = view.findViewById(R.id.imageview_calendar_per);

        spinnerSelectFuelType = view.findViewById(R.id.spinner_select_fueltype_per);
        spinnerSelectProduct = view.findViewById(R.id.spinner_select_product_per);

        editTextReqQuan = view.findViewById(R.id.edit_req_quan_p);
        editTextReqAmt = view.findViewById(R.id.edit_req_amt_p);

        editTextActualQuantity = view.findViewById(R.id.edittext_actual_quanper);
        editTextActualAmount = view.findViewById(R.id.edittext_actual_amtper);

        radioGroupReq = (RadioGroup) view.findViewById(R.id.radiogr_required);
        radioGroupActual = (RadioGroup) view.findViewById(R.id.radiogr_actual);

        imageViewReqQuan = view.findViewById(R.id.imageview_req_quan);
        imageviewReqAmt = view.findViewById(R.id.imageview_req_amt);
        imageviewActualQuan = view.findViewById(R.id.imageview_actual_quan);
        imageviewActualAmt = view.findViewById(R.id.imageview_actual_amt);

        imageViewReqQuan.setOnClickListener(this);
        imageviewReqAmt.setOnClickListener(this);
        imageviewActualQuan.setOnClickListener(this);
        imageviewActualAmt.setOnClickListener(this);

        checkboxSameAsReq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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

                textViewTentativeDate.setText(date);

            }
        };


     /*   ivSearchDriver.setOnClickListener(this);
        ivAddDriver.setOnClickListener(this);*/
        ivSearchVehicle.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);

    }

    private void fillDropDownDataFromLocalDb() {
        List<String> fuelTypes = databaseHelper.getFuelType();
        String[] fuelTypesArr = new String[fuelTypes.size()];
        for (int i = 0; i < fuelTypes.size(); i++) {
            fuelTypesArr[i] = fuelTypes.get(i);
        }

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, fuelTypesArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectFuelType.setAdapter(arrayAdapter1);

        spinnerSelectFuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFuelType = (String) spinnerSelectFuelType.getSelectedItem();
                spinnerErrorTextFuelTypr = (TextView) spinnerSelectFuelType.getSelectedView();

                if (selectedFuelType.contains("Select Fuel Type")) {

                } else {
                    Log.d(TAG, "onItemSelected:selectedFuelType :  " + selectedFuelType);

                    List<String> prodNamesList = databaseHelper.getProductNameByFuelType(selectedFuelType);
                    fillProductdropdown(prodNamesList, selectedFuelType);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void fillProductdropdown(List<String> prodNamesList, final String selectedFuelType) {
        Log.d(TAG, "fillProductdropdown: prodNamesList : " + prodNamesList.size());
        String[] prodNameArr = new String[prodNamesList.size()];
        for (int i = 0; i < prodNamesList.size(); i++) {
            prodNameArr[i] = prodNamesList.get(i);
        }


        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, prodNameArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectProduct.setAdapter(arrayAdapter1);

        spinnerSelectProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selctedProd = (String) spinnerSelectProduct.getSelectedItem();
                spinnerErrorTextProd = (TextView) spinnerSelectProduct.getSelectedView();

                if (selctedProd.contains("Select Product")) {

                } else {
                    Log.d(TAG, "onItemSelected:selectedFuelType :  " + selctedProd);
                    String productId = databaseHelper.getProductId(selectedFuelType, selctedProd);
                    apiCallForGetTodaysFuelPrice(getActivity(), productId);
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
         /*   case R.id.iv_search_driver_per:
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));
                String phone1 = editTextDriverNo.getText().toString();
                if (!phone1.isEmpty()) {
                    textViewDriverName.setText("");
                    apiCallForSearchDriver(getActivity(), phone1);

                } else {
                    editTextDriverNo.setError("Please Enter Mobile Number");
                    Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_add_driver_per:
                addNewDriver();

                break;*/

            case R.id.iv_search_vehicle_per:
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

            case R.id.button_submit_nw_per:
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));


                String fuelRateText = textViewFuelPrice.getText().toString();


                if (fuelRateText != null && !fuelRateText.equals("null") && !fuelRateText.equals("undefined") && !fuelRateText.isEmpty()) {
                    //  if (radioBtnReq.isChecked() || radioBtnActual.isChecked()) {
                    if (areValidateForAddNewRequest()) {

                        String acQuan = editTextActualQuantity.getText().toString();
                        String acAmt = editTextActualAmount.getText().toString();

                        String reqQuan = editTextReqQuan.getText().toString();
                        String reqAmt = editTextReqAmt.getText().toString();

                        String message = "Are you sure want to confirm submit Values ?";
                        sendProperMessageForSubmission(message);

                       /* if (!acQuan.isEmpty() && !acAmt.isEmpty() && !reqQuan.isEmpty() && !reqAmt.isEmpty()) {
                            String message = "Are you sure want to confirm submit Values ?";
                            sendProperMessageForSubmission(message);
                        } else if (!reqQuan.isEmpty() && !reqAmt.isEmpty() && acQuan.isEmpty() && acAmt.isEmpty()) {
                            String message = "It seems Actual Quantity and Actual Amount not selected. Are you sure want to proceed with requested values?";
                            sendProperMessageForSubmission(message);
                        } else {
                            Toast.makeText(getActivity(), "Required Quantity and Required Amount must not be empty!", Toast.LENGTH_LONG).show();
                        }*/
/*
                    } else {
                        Toast.makeText(getActivity(), "Please select quantity or amount for both", Toast.LENGTH_SHORT).show();
                    }*/
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
                  /*  builder.setMessage("Fuel price not assigned. Please set fuel price first.")
                            .setCancelable(false)
                            .setPositiveButton("Set Price", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(getActivity(), SetFuelPriceActivity.class);
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
                }
                //}
                break;


            case R.id.imageview_actual_quan:
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

            case R.id.imageview_actual_amt:

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

            case R.id.imageview_req_quan:
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

            case R.id.imageview_req_amt:

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

    private boolean areValidateForAddNewRequest() {
        boolean result = false;
        String selectedFuelType = (String) spinnerSelectFuelType.getSelectedItem();
        String selectedProductName = (String) spinnerSelectProduct.getSelectedItem();

        if (TextUtils.isEmpty(textViewDriverName.getText().toString())) {
            result = false;
            Toast.makeText(getActivity(), "Please Search Person Number", Toast.LENGTH_SHORT).show();
            textViewDriverName.setError("Please Search Person Number");
        } else if (selectedFuelType.contains("Select Fuel Type")) {
            result = false;
            spinnerErrorTextFuelTypr.setError("Please Select Fuel Type");
            Toast.makeText(getActivity(), "Please Select Fuel Type", Toast.LENGTH_SHORT).show();
        } else if (selectedProductName.contains("Select Product")) {
            result = false;
            spinnerErrorTextProd.setError("Please Select Product");
        } else if (TextUtils.isEmpty(textViewTentativeDate.getText().toString())) {
            result = false;
            textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_SHORT).show();
        } else if (radioGroupReq.getCheckedRadioButtonId() == -1) {
            result = false;
            //  textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please select either Required Quantity or Required Amount", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(editTextReqQuan.getText().toString()) || TextUtils.isEmpty(editTextReqAmt.getText().toString())) {
            result = false;
            //  textViewTentativeDate.setError("Please Select Date");
            Toast.makeText(getActivity(), "Please enter either Required Quantity or Required Amount and calcualte", Toast.LENGTH_LONG).show();
        } else if (checkboxSameAsReq.isChecked()) {
            result = true;
        } else {
            result = false;

            Log.d(TAG, "areValidateForAddNewRequest: in if : " + checkboxSameAsReq.isChecked());
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

    private void sendNewFuelCreditRequestDetails() {
        //String personNo = editTextDriverNo.getText().toString();
        String vehicleNo = editTextVehicleNo.getText().toString();
        String custName = "";
        String personName = textViewDriverName.getText().toString();
        String fuelType = (String) spinnerSelectFuelType.getSelectedItem();
        String product = (String) spinnerSelectProduct.getSelectedItem();
        String quantity = editTextReqQuan.getText().toString();
        String amount = editTextReqAmt.getText().toString();

        String acQuan = editTextActualQuantity.getText().toString();
        String acAmt = editTextActualAmount.getText().toString();

        if (acQuan.isEmpty() && acAmt.isEmpty()) {
            acQuan = quantity;
            acAmt = amount;
        }
        Log.d(TAG, "sendNewFuelCreditRequestDetails:acQuan :  " + acQuan);
        Log.d(TAG, "sendNewFuelCreditRequestDetails:acAmt :  " + acAmt);


        String fuelPrice = textViewFuelPrice.getText().toString();

        final String[] dealerDetails = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
        String finalVehicleId = "";
        if (personId != null && !personId.equals("null") && !personId.isEmpty()) {
            if (vehicleId == null || vehicleId.equals("null") || vehicleId.isEmpty() || vehicleId.equals("undefined")) {
                finalVehicleId = "";
            }
            String productId = databaseHelper.getProductId(fuelType, product);
            PersonModelNew personModel;
            if (vehicleId != null && !vehicleId.equals("null") && !vehicleId.isEmpty() && !vehicleId.equals("undefined")) {

                personModel = new PersonModelNew(quantity, amount, textViewTentativeDate.getText().toString(),
                        dealerDetails[1], personId, finalVehicleId, productId,
                        textViewPanNo.getText().toString(),
                        textViewCustMapId.getText().toString(), acAmt,
                        acQuan, commonTaskManager.getCurrentDateTimeNewFormat(),
                        commonTaskManager.getCurrentDateNew(), commonTaskManager.getCurrentTime(), "COMPLETE", editTextVehicleNo.getText().toString(),
                        "TRUE", fuelPrice, dealerDetails[0]);
            } else {
                personModel = new PersonModelNew(quantity, amount, textViewTentativeDate.getText().toString(),
                        dealerDetails[1], personId, finalVehicleId, productId,
                        textViewPanNo.getText().toString(),
                        textViewCustMapId.getText().toString(), acAmt,
                        acQuan, commonTaskManager.getCurrentDateTimeNewFormat(),
                        commonTaskManager.getCurrentDateNew(), commonTaskManager.getCurrentTime(), "COMPLETE", editTextVehicleNo.getText().toString(),
                        "FALSE", fuelPrice, dealerDetails[0]);

                /*personModel = new PersonModel(quantity, amount,
                        textViewTentativeDate.getText().toString(), dealerDetails[1],
                        personId, finalVehicleId,
                        productId, textViewPanNo.getText().toString(), textViewCustMapId.getText().toString(),
                        commonTaskManager.getCurrentDateNew(),
                        editTextVehicleNo.getText().toString(), "FALSE");*/
            }
          /*  if (vehicleId == null || vehicleId.equals("null") || vehicleId.isEmpty()) {
                vehicleId = "";
            }*/

            Log.d(TAG, "sendNewFuelCreditRequestDetails:personId :" + personId);
            Log.d(TAG, "sendNewFuelCreditRequestDetails: vehicleId : " + vehicleId);
            Log.d(TAG, "sendNewFuelCreditRequestDetails: productId : " + productId);

            final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
            progressDialog.setMessage(getResources().getString(R.string.loading_data));
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            final String[] message = {""};
            String []details = commonCodeManager.getDealerDetails(getActivity());


            GetDataService service = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GetDataService.class);
            Call<JsonObject> call = service.addCreditReqPersonByDealer(personModel);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse: jsondata add new for person: " + jsonData);
                    try {
                        JSONObject mainObject = new JSONObject(jsonData);
                        final String status = mainObject.getString("status");
                        final String msg = mainObject.getString("msg");

                        if (status.equals("OK")) {
                            // commonTaskManager.dismissDialogWithToast(getActivity(), progressDialog, msg);
                            Handler handler1 = new Handler(getActivity().getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();

                                    }
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getActivity(), NewArrivalListActivity.class);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            commonTaskManager.dismissDialogWithToast(Objects.requireNonNull(getActivity()), progressDialog, msg);

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        commonTaskManager.dismissProgressDialog(Objects.requireNonNull(getActivity()), progressDialog);
                        String finalMessage = message[0] +" : "+e.getLocalizedMessage();
                        final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,dealerDetails[0],finalMessage,Constants.type_exec,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,dealerDetails[1]);
                        apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    t.printStackTrace();
                    commonTaskManager.dismissProgressDialog(Objects.requireNonNull(getActivity()), progressDialog);
                    String finalMessage = message[0] +" : "+t.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,dealerDetails[0],finalMessage,Constants.type_failure,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,dealerDetails[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                }

            });

        }

    }

    private void apiCallForSearchVehicleNumber(final Context context, String vehicleNo) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String []details = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
        final String[] message = {""};



        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.searchVehicleByRegistrationNumber(vehicleNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchVehicleNumber :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject duObject = dataArray.getJSONObject(0);
                        String ownerName = duObject.getString("ownerName");
                        vehicleId = duObject.getString("vehicleId");

                        editTextVehicleID.setText(vehicleId);

                        updateUiAsPerVehicleDetails(ownerName);

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    String finalMessage = message[0] +" : "+e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,details[0],finalMessage,Constants.type_exec,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message[0] +" : "+t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,details[0],finalMessage,Constants.type_failure,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

            }

        });

    }

    private void updateUiAsPerVehicleDetails(String ownerName) {
        //textViewDriverName.setText(ownerName);
    }

    private void addNewDriver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).
                inflate(R.layout.layout_add_driver, viewGroup,
                        false);
        builder.setView(dialogView);

        editTextMobileNo = dialogView.findViewById(R.id.edittext_mobile_no);
        editTextFirstName = dialogView.findViewById(R.id.edittext_first_name);
        editTextLastName = dialogView.findViewById(R.id.edittext_last_name);
        buttonAddDriver = dialogView.findViewById(R.id.button_add_driver);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonAddDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                if (areValidateForAddDriver()) {
                    String firstName = editTextFirstName.getText().toString();
                    String lastName = editTextLastName.getText().toString();
                    String phone = editTextMobileNo.getText().toString();

                    apiCallForAddDriver(getActivity(), firstName, lastName, phone);

                }
            }
        });


    }

    private void apiCallForAddDriver(final Context context, String firstName, String lastName, String phone) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String[] deatils = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addPerson(firstName, phone, deatils[0], lastName);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddDriver :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    commonTaskManager.dismissDialogWithToast(context, progressDialog, message);


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

    private void apiCallForSearchDriver(final Context context, final String personIdSearch) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<String> personData = new ArrayList<>();
        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.searchPersonByPhoneNumberNew(personIdSearch, dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchDriver :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    if (status.equals("ok")) {
                       /* Handler handler1 = new Handler(context.getMainLooper());
                        handler1.post(new Runnable() {
                            public void run() {
                              buttonSubmit.setEnabled(true);
                            }
                        });
*/
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        JSONObject duObject = dataArray.getJSONObject(0);
                        personId = duObject.getString("personId");
                        String firstName = duObject.getString("firstName");
                        String lastName = duObject.getString("lastName");
                        String PANno = duObject.getString("PANno");
                        String fuelDealerCustomerMapId = duObject.getString("fuelDealerCustomerMapId");


                        personData.add(personId);
                        personData.add(firstName);
                        personData.add(lastName);

                        updateUIAsPerSearchDriverResult(personData, PANno, fuelDealerCustomerMapId);

                    } else {
                      /*  final String message = phone1 + " is Not Registerd With Us, Please create mapping account for this number. Kindly contact Manager.";
                        // commonTaskManager.showToast(context, message);
                        Handler handler1 = new Handler(context.getMainLooper());
                        handler1.post(new Runnable() {
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                                builder.setTitle("Note");
                                builder.setMessage(message)
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });
                                       *//* .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();

                                            }
                                        });*//*

                                final AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(true);
                                alertDialog.show();

                            }
                        });
*/
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

    private void updateUIAsPerSearchDriverResult(List<String> personData,
                                                 String panNo, String fuelDealerCustomerMapId) {
        String personName = personData.get(1) + " " + personData.get(2);
        textViewDriverName.setText(personName);
        textViewPanNo.setText(panNo);
        textViewCustMapId.setText(fuelDealerCustomerMapId);

    }

    private boolean areValidateForAddDriver() {
        boolean result = false;
       /* if (TextUtils.isEmpty(editTextDriverNo.getText().toString())) {
            result = false;
            editTextDriverNo.setError("Please Enter Mobile Number");
        } else if (TextUtils.isEmpty(editTextFirstName.getText().toString())) {
            result = false;
            editTextFirstName.setError("Please Enter First Name");
        } else if (TextUtils.isEmpty(editTextLastName.getText().toString())) {
            result = false;
            editTextLastName.setError("Please Enter Last Name");
        } else {
            result = true;
        }*/

        return result;
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

    private void apiCallForGetTodaysFuelPrice(final Context context, String fuelProdId) {


        Log.d(TAG, "apiCallForGetTodaysFuelPrice:fuelProdId :  " + fuelProdId);
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait, Retrieving Today's Fuel Price");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] details = commonCodeManager.getDealerDetails(context);
        final String[] message = {""};

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getPriceByDealerProductId(details[1], fuelProdId);
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
                    String finalMessage = message[0] +" : "+e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,details[0],finalMessage,Constants.type_exec,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,details[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message[0] +" : "+t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,details[0],finalMessage,Constants.type_failure,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,details[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

            }

        });
    }

    private void apiCallForGetMappedPersons(final Context context) {


        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please wait, retrieving details");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        final String[] message = {""};

        final List<MappedPersonModel> mappedPerList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getPersonAccByDealerId(dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetMappedPersons :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject detailsObj = dataArray.getJSONObject(i);
                                String personId = detailsObj.getString("personId");
                                String firstName = detailsObj.getString("firstName");
                                String lastName = detailsObj.getString("lastName");
                                String name = firstName + " " + lastName;

                                if (i == 0) {
                                    mappedPersonModel = new MappedPersonModel("Select Person", "");
                                    mappedPerList.add(mappedPersonModel);

                                }
                                mappedPersonModel = new MappedPersonModel(name, personId);
                                mappedPerList.add(mappedPersonModel);
                            }

                            fillMappedPersons(mappedPerList);

                        } else {
                            commonTaskManager.showToast(context, "No fuel price found");
                        }


                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    String finalMessage = message[0] +" : "+e.getLocalizedMessage();
                    final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,dealerDetails[0],finalMessage,Constants.type_exec,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,dealerDetails[1]);
                    apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);
                String finalMessage = message[0] +" : "+t.getLocalizedMessage();
                final ErrorLogModel errorLogModel = new ErrorLogModel(Constants.APP_NAME,dealerDetails[0],finalMessage,Constants.type_failure,commonTaskManager.getCurrentDateTimeNewFormat(),Constants.refuel_person,dealerDetails[1]);
                apiHandlerForErrorLog.apiCallForAddErrorLogInDb(getActivity(), errorLogModel);

            }

        });
    }


    private void fillMappedPersons(final List<MappedPersonModel> mappedPerList) {

        String[] mappedPerArr = new String[mappedPerList.size()];
        for (int i = 0; i < mappedPerList.size(); i++) {
            MappedPersonModel mappedPersonModel = mappedPerList.get(i);
            mappedPerArr[i] = mappedPersonModel.getName();
            Log.d(TAG, "fillMappedPersons: i : " + i + " Person Id : " + mappedPersonModel.getPersonId());
        }

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, mappedPerArr);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMappedPerson.setAdapter(arrayAdapter1);

        spinnerMappedPerson.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPersonName = (String) spinnerMappedPerson.getSelectedItem();
                //  spinnerErrorTextFuelTypr = (TextView) spinnerMappedPerson.getSelectedView();

                if (selectedPersonName.contains("Select Person")) {

                } else {
                    Log.d(TAG, "onItemSelected:selected  :  " + selectedPersonName);
                    MappedPersonModel mappedPersonModel = mappedPerList.get(position);
                    Log.d(TAG, "onItemSelected: personId : " + mappedPersonModel.getPersonId());
                    apiCallForSearchDriver(getActivity(), mappedPersonModel.getPersonId());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
