package com.veelsplusfueldealerapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.EarningsReportActivity;
import com.veelsplusfueldealerapp.adapters.ErDigitalTransDetailsAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.DecimalDigitsInputFilter;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.UpdateDigitalTotalDetails;
import com.veelsplusfueldealerapp.managers.UpdateSalesDeatail;
import com.veelsplusfueldealerapp.models.AddNewPayDetailsModel;
import com.veelsplusfueldealerapp.models.ErCashModel;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.ErDigitalTransModel;
import com.veelsplusfueldealerapp.models.FuelTerminalModel;
import com.veelsplusfueldealerapp.models.PaymentModelLocal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ErDigitalFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ErDigitalFragment";
    String[] details;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    View view;
    List<String> allStaffPerformIds;
    String activityId = "";
    EditText editTextTransCount, editTextTotalAmt;
    Button buttonSubmitTrans, buttonAddDetails;
    RecyclerView recyclerArraivals;
    EditText editTextTransactionId, editTextAmount;
    Button buttonSubmit, buttonCancel;
    Spinner spinnerSelectDevice;
    String selectedTerminal;
    String grandTotal;
    ErDigitalTransModel erDigitalTransModel;
    PaymentModelLocal paymentModelLocal;
    TextView textviewMeterSaleRs, textViewAmountTally, textViewDifference;
    Activity activity;
    boolean isCancelButtonClicked = false;
    boolean isSubmitButtonClicked = false;
    String recoveryAmount;
    APIHandlerManager apiHandlerManager;
    TextView tvDifferCheck;
    private DatabaseHelper databaseHelper;
    private int spinnerSelectedPosition;
    private TextView spinnerErrorText;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_er_digital_frag, container, false);


        initUI();
        return view;
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(getActivity());
        commonCodeManager = new CommonCodeManager(getActivity());
        databaseHelper = new DatabaseHelper(getActivity());
        apiHandlerManager = new APIHandlerManager(getActivity());

        View allValues = view.findViewById(R.id.layout_collection_tally_dg);

        textviewMeterSaleRs = allValues.findViewById(R.id.textview_meter_sale_rs);
        textViewAmountTally = allValues.findViewById(R.id.textview_amount_tally);
        textViewDifference = allValues.findViewById(R.id.textview_difference);

        allStaffPerformIds = new ArrayList<>();
        recyclerArraivals = view.findViewById(R.id.recyclerview_arrivals);


        buttonAddDetails = view.findViewById(R.id.button_add_details_digital);
        buttonSubmitTrans = view.findViewById(R.id.button_submit_trans_digital);
        ImageView imageViewSubmitInfo = view.findViewById(R.id.iv_submit_trans_info);


        editTextTransCount = view.findViewById(R.id.edittext_trans_count);
        editTextTotalAmt = view.findViewById(R.id.edittext_total_amount_trans);

        tvDifferCheck = Objects.requireNonNull(getActivity()).findViewById(R.id.textview_difference);


        buttonAddDetails.setOnClickListener(this);
        buttonSubmitTrans.setOnClickListener(this);
        imageViewSubmitInfo.setOnClickListener(this);


        setHeaderParams();

    }

    private void setHeaderParams() {
        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
        textviewMeterSaleRs.setText(recoveryAmount);

        String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
        if (tallyAmountDb != null && tallyAmountDb.isEmpty()) {
            textViewAmountTally.setText("0");

        } else {
            textViewAmountTally.setText(tallyAmountDb);

        }

    }

    @Override
    public void onResume() {
        super.onResume();


        details = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
        String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());
        Log.d(TAG, "onResume: dailySales[0] : " + dailySales[0]);

        if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {

        } else {
            //for view or update

            buttonSubmitTrans.setEnabled(false);
            buttonAddDetails.setEnabled(true);
            Log.d(TAG, "onResume: existing");
            String[] totalDonePays = commonCodeManager.getTotalAmtDigitalForDoneCardClickView(getActivity());
            editTextTotalAmt.setText(totalDonePays[0]);



         /*   List<PaymentModelLocal> payList = databaseHelper.getDonePaymentDetails();
            Log.d(TAG, "onResume:payList size :  "+payList.size());
            if (payList != null && payList.size() > 0) {
                fillDigitalTransDetails(payList);
            }*/
            apiCallForDoneTransDetails(getActivity(), details[0], dailySales[1]);
        }


    }

    private void apiCallForGetAccountTransactionLogForDigital(final Context context) {
        Log.d(TAG, "apiCallForGetAccountTransactionLogForDigital: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] message = new String[1];
        //String batchId = commonCodeManager.getBatchIdForDigital(context);
        String batchId = commonCodeManager.getBatchIdForErReport(context);

        String[] details = commonCodeManager.getDealerDetails(context);


        final List<ErDigitalTransModel> transDetails = new ArrayList<>();
        Log.d(TAG, "apiCallForGetAccountTransactionLogForDigital: dealerstaffid : " + details[0]);
        Log.d(TAG, "apiCallForGetAccountTransactionLogForDigital:batchid :  " + batchId);


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAccountTransactionLogForDigital(details[0], batchId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetdigitalDetails :  " + jsonData);
                try {
                    ErDigitalTransModel erDigitalTransModel = new ErDigitalTransModel();

                    JSONObject mainObject = new JSONObject(jsonData);
                    //commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    message[0] = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonObject = dataArray.getJSONObject(i);
                                String transacId = jsonObject.getString("transacId");
                                String transacDate = jsonObject.getString("transacDate");
                                String paytmTotalAmount = jsonObject.getString("paytmTotalAmount");
                                String cardTotalAmount = jsonObject.getString("cardTotalAmount");


                                // String deviceName = commonCodeManager.getSelectedDevice(context);
                                String deviceName = databaseHelper.getPaymentType();


                                if (paytmTotalAmount != null && !paytmTotalAmount.isEmpty() && !paytmTotalAmount.equals("0")) {
                                    erDigitalTransModel = new ErDigitalTransModel(deviceName, transacId, paytmTotalAmount);

                                }
                                if (cardTotalAmount != null && !cardTotalAmount.isEmpty() && !cardTotalAmount.equals("0")) {
                                    erDigitalTransModel = new ErDigitalTransModel(deviceName, transacId, cardTotalAmount);

                                }


                                transDetails.add(erDigitalTransModel);
                            }
                            //fillDigitalTransDetails(transDetails);
                        }


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_details_digital:

                //check for difference zero
                String difference = tvDifferCheck.getText().toString();
                Log.d(TAG, "onClick: diff check : " + difference);

                if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {
                    //Whole amount details submitted. You can't enter more.
                    Toast.makeText(getActivity(), "Whole amount details submitted. You can't enter more", Toast.LENGTH_LONG).show();
                    return;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(view.getContext()).
                        inflate(R.layout.frag_add_details_digital, viewGroup,
                                false);

                builder.setView(dialogView);

                final AlertDialog alertDialog = builder.create();
                Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.setCancelable(false);
                alertDialog.show();

                editTextTransactionId = dialogView.findViewById(R.id.edittext_trans_id_digital);
                editTextAmount = dialogView.findViewById(R.id.edittext_amount_digital);
                editTextAmount.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});

                buttonSubmit = dialogView.findViewById(R.id.button_submit_digital);
                buttonCancel = dialogView.findViewById(R.id.button_cancel_digital);


                spinnerSelectDevice = dialogView.findViewById(R.id.spinnr_select_device_digital);
                fillTerminalDetailsSpinner();

                buttonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grandTotal = editTextAmount.getText().toString();
                        selectedTerminal = (String) spinnerSelectDevice.getSelectedItem();
                        if (areValidateAddDetails()) {

                            isSubmitButtonClicked = true;
                            alertDialog.dismiss();

                        }
                    }
                });
                buttonCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isCancelButtonClicked = true;
                        isSubmitButtonClicked = false;
                        Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();

                    }
                });

                String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());
                Log.d(TAG, "onResume: account transaction log id :  : " + dailySales[0]);


                if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {

                    //new entry in sales
                    final String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
                    recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());

               /* AddDigitalDetailsDialogFrag ad = new AddDigitalDetailsDialogFrag(getActivity());
                ad.show(getFragmentManager(), "digital");
*/

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (areValidateAddDetails()) {
                                Log.d(TAG, "onDismiss: isSubmitButtonClicked : " + isSubmitButtonClicked);
                                if (isSubmitButtonClicked) {

                                    Log.d(TAG, "onDismiss: if part isCancelButtonClicked : " + isCancelButtonClicked);
                                    String[] dealerLogin = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
                                    String selcTermType = "";
                                    Log.d(TAG, "onDismiss: selectedTerminal : " + selectedTerminal);
                                    List<String> detailsTerms = databaseHelper.getAllTerminalsType(selectedTerminal);

                                    if (detailsTerms.size() > 0) {
                                        selcTermType = detailsTerms.get(1).toLowerCase().trim();

                                    }

                                    String activityId = commonCodeManager.getActivityId(getActivity());
                                    String batchId = commonCodeManager.getBatchIdForErReport(getActivity());

                                    //String batchId = commonCodeManager.getBatchIdForDigital(getActivity());
                                    ErDigitalModel erDigitalModel = null;

                                    String corporateId = commonCodeManager.getCorporateId(getActivity());
                                    Log.d(TAG, "onDismiss: selcTermType : " + selcTermType);

                                    if (selcTermType.contains("card")) {

                                        erDigitalModel = new ErDigitalModel(
                                                commonTaskManager.getCurrentDateNew(), dealerLogin[0],
                                                grandTotal, activityId, batchId, "",
                                                editTextTransactionId.getText().toString(),
                                                "",
                                                grandTotal, "",
                                                detailsTerms.get(0), recoveryAmount, grandTotal,
                                                corporateId, tallyAmountDb);


                                    } else if (selcTermType.equals("paytm") || selcTermType.equals("upi") || selcTermType.equals("other") || selcTermType.contains("oil") || selcTermType.equals("amex") || selcTermType.equals("fleet")) {

                                        erDigitalModel = new ErDigitalModel(
                                                commonTaskManager.getCurrentDateNew(),
                                                dealerLogin[0], grandTotal,
                                                activityId,
                                                batchId, "",
                                                editTextTransactionId.getText().toString(),
                                                grandTotal,
                                                "", "",
                                                detailsTerms.get(0), recoveryAmount,
                                                grandTotal, corporateId, tallyAmountDb

                                        );


                                    } else {
                                        Log.d(TAG, "onDismiss: else part : " + isCancelButtonClicked);
                                    }
                                    if (erDigitalModel != null) {

                                        apiCallForSubmitTransactionDetails(getActivity(),
                                                erDigitalModel, "adddetails");

                                    }
                                }
                            } //for if end
                            else {
                                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    //add new entry to exsting batch

                    alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialogInterface) {
                            if (areValidateAddDetails()) {
                                Log.d(TAG, "onDismiss: isSubmitButtonClicked : " + isSubmitButtonClicked);
                                if (isSubmitButtonClicked) {

                                    ErDigitalModel erDigitalModel = null;
                                    String[] dealerLogin = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
                                    String activityId = commonCodeManager.getActivityId(getActivity());

                                    Log.d(TAG, "onClick:add new entry to exsting batch ");

                                    String selcTermType = "";
                                    Log.d(TAG, "onDismiss: selectedTerminal : " + selectedTerminal);
                                    List<String> detailsTerms = databaseHelper.getAllTerminalsType(selectedTerminal);

                                    if (detailsTerms.size() > 0) {
                                        selcTermType = detailsTerms.get(1).toLowerCase().trim();

                                    }

                                    AddNewPayDetailsModel addNewModel = commonCodeManager.getNewPayDetailsModelForExistingBatch(getActivity());

                                    if (selcTermType.contains("card")) {

                                        erDigitalModel = new ErDigitalModel(
                                                commonTaskManager.getCurrentDateNew(), dealerLogin[0],
                                                grandTotal, activityId, addNewModel.getBatchId(), "",
                                                editTextTransactionId.getText().toString(),
                                                "",
                                                grandTotal, "",
                                                detailsTerms.get(0), addNewModel.getTotalBatchAmtExpected(), grandTotal,
                                                addNewModel.getCorporateID(), addNewModel.getTotalBatchAmtRecovered());


                                    } else if (selcTermType.equals("paytm") || selcTermType.equals("upi") || selcTermType.equals("other") || selcTermType.contains("oil") || selcTermType.equals("amex") || selcTermType.equals("fleet")) {

                                        erDigitalModel = new ErDigitalModel(
                                                commonTaskManager.getCurrentDateNew(),
                                                dealerLogin[0], grandTotal,
                                                activityId,
                                                addNewModel.getBatchId(), "",
                                                editTextTransactionId.getText().toString(),
                                                grandTotal,
                                                "", "",
                                                detailsTerms.get(0), addNewModel.getTotalBatchAmtExpected(),
                                                grandTotal, addNewModel.getCorporateID(),
                                                addNewModel.getTotalBatchAmtRecovered()

                                        );
                                    }

                                    if (erDigitalModel != null) {
                                        apiCallForAddMorePayDetailsByDigital(getActivity(), erDigitalModel);

                                    }
                                }
                            } //for if end
                            else {
                                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }

                break;
            case R.id.button_submit_trans_digital:
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));
                String corporateId = commonCodeManager.getCorporateId(getActivity());

                Log.d(TAG, "onClick:button_submit_trans clicked ");
                String isClicked = commonCodeManager.getIsDigitalSubmitTransClicked(Objects.requireNonNull(getActivity()));
                Log.d(TAG, "onClick: isClicked : " + isClicked);

                if (!isClicked.equals("clicked")) {


                    String transCount = editTextTransCount.getText().toString();
                    String grandTotal = editTextTotalAmt.getText().toString();
                    if (!grandTotal.isEmpty()) {


                        String activityId1 = commonCodeManager.getActivityId(getActivity());
                        String[] dealerDetails = commonCodeManager.getDealerDetails(getActivity());

                        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());

                        ErDigitalModel erDigitalModel1 = new ErDigitalModel
                                (commonTaskManager.getCurrentDateNew(),
                                        dealerDetails[0],
                                        grandTotal, activityId1,
                                        commonCodeManager.getBatchIdForErReport(getActivity()),
                                        "", "", "",
                                        "", "", "",
                                        recoveryAmount, grandTotal, corporateId,
                                        "");
                        apiCallForSubmitTransactionDetails(getActivity(), erDigitalModel1,
                                "submit");

                    } else {
                        editTextTotalAmt.setError("Please Enter Amount");

                    }

                }
                break;

            case R.id.iv_submit_trans_info:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
                builder1.setTitle("Note!");
                builder1.setMessage("Please enter total digital amount at one time or use 'Add Details'")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });


                final AlertDialog alertDialog1 = builder1.create();
                alertDialog1.setCancelable(true);
                alertDialog1.show();
                break;
        }
    }


    private void fillTerminalDetailsSpinner() {
        final List<FuelTerminalModel> terminalsList = databaseHelper.getAllTerminals();
        Log.d(TAG, "fillTerminalDetailsSpinner: termianal list size : " + terminalsList.size());

        String[] terminals = new String[terminalsList.size()];
        //  terminals[0] = "Select Device";

        for (int i = 0; i < terminalsList.size(); i++) {
            FuelTerminalModel fm = terminalsList.get(i);
            if (!fm.getTerminalName().isEmpty()) {
                terminals[i] = fm.getTerminalName();

            }
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, terminals);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectDevice.setAdapter(arrayAdapter1);
        spinnerSelectDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTerminal = (String) spinnerSelectDevice.getSelectedItem();
                spinnerErrorText = (TextView) spinnerSelectDevice.getSelectedView();

                if (selectedTerminal.contains("Select Device")) {

                } else {
                    spinnerSelectedPosition = position;

                    commonCodeManager.saveSelectedDevice(Objects.requireNonNull(getActivity()), selectedTerminal);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void apiCallForSubmitTransactionDetails(final Context context,
                                                    final ErDigitalModel erDigitalModel,
                                                    final String whichButton) {
        Log.d(TAG, "apiCallForSubmitTransactionDetails: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] message = new String[1];
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
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");
                    commonCodeManager.saveBatchIdForDigital(context, erDigitalModel.getBatchId());

                    if (status.equals("ok")) {

                        //local db handling for info cards
                        commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(context, "noapicall");

                        commonCodeManager.saveConfirmRecoveryFlag(context, true);
                        commonCodeManager.saveLocalErData(context, erDigitalModel.getGrandTotalAmount(), "", "");
                        String payType = "";

                        if (whichButton.equals("adddetails")) {
                            payType = (String) spinnerSelectDevice.getSelectedItem();

                        }
                        if (whichButton.equals("submit")) {
                            commonCodeManager.saveIsDigitalSubmitTransClicked(context, "clicked");
                            payType = "submit";

                            //make submit button disable
                            String isClicked = commonCodeManager.getIsDigitalSubmitTransClicked(Objects.requireNonNull(getActivity()));
                            Log.d(TAG, "onClick: isClicked : " + isClicked);

                            if (isClicked.equals("clicked")) {

                                Handler handler1 = new Handler(context.getMainLooper());
                                handler1.post(new Runnable() {
                                    public void run() {
                                        buttonSubmitTrans.setEnabled(false);

                                    }
                                });
                            }

                        }

                        PaymentModelLocal paymentModelLocal = new
                                PaymentModelLocal(erDigitalModel.getBatchId(),
                                payType, erDigitalModel.getGrandTotalAmount(),
                                erDigitalModel.getTransacId());

                        databaseHelper.addPaymentDetails(paymentModelLocal);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String tallyAmountDb = databaseHelper.getPaymentDetails(erDigitalModel.getBatchId());
                                String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
                                Log.d(TAG, "onResponse: digital:addentry  tallyAmountDb : " + tallyAmountDb);
                                Log.d(TAG, "onResponse: digital addentry  recoveryAmount :" + recoveryAmount);

                                String totalCreditAmount = commonCodeManager.getTotalCreditAmountFinal(context);
                                Log.d(TAG, "onResponse: digital addentry totalCreditAmount  :  " + totalCreditAmount);


                                //check for total due
                                double finalDueForCalc;
                                String checkTotalDue = commonCodeManager.getTotalDueInSales(context);
                                if (checkTotalDue != null && !checkTotalDue.isEmpty() && checkTotalDue.equals("nothing")) {
                                    double totalDue = Double.parseDouble(recoveryAmount) - Double.parseDouble(totalCreditAmount);
                                    Log.d(TAG, "onResponse: digital addentry totalDue : " + totalDue);

                                    finalDueForCalc = totalDue;
                                    //nothing paid by cash or digital
                                    commonCodeManager.saveTotalDueInSales(context, String.valueOf(totalDue));

                                } else {
                                    //check for null
                                    finalDueForCalc = Double.parseDouble(checkTotalDue);
                                }

                                Log.d(TAG, "onResponse:finalDueForCalc digital :after loop :   " + finalDueForCalc);


                                double differ = Double.parseDouble(recoveryAmount);
                                Log.d(TAG, "onResponse: digital addentry differ : " + differ);

                                double difference = finalDueForCalc - Double.parseDouble(tallyAmountDb);
                                Log.d(TAG, "onResponse: digital addentry difference: " + difference);

                                String[] details = {tallyAmountDb, recoveryAmount, String.format("%.2f", difference)};
                                try {
                                    ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                                } catch (ClassCastException cce) {
                                    cce.printStackTrace();

                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                        Log.d(TAG, "run: whichButton for adddestails : " + whichButton);
                                        showTransDetailsToList(erDigitalModel, "noupdate", "", "");

                              /*  if (whichButton.equals("submit")) {
                                    Handler handler1 = new Handler(context.getMainLooper());
                                    handler1.post(new Runnable() {
                                        public void run() {
                                            editTextTotalAmt.setText("");
                                            editTextTransCount.setText("");
                                        }
                                    });
                                    apiHandlerManager.apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(getActivity(),
                                            erDigitalModel.getActivityId(),
                                            erDigitalModel.getBatchId());
                                    apiCallForUpdateTallyAmt(erDigitalModel.getActivityId(), erDigitalModel.getBatchId());


                                }*/

                                    }
                                }, 500);
                                // commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

                            }
                        }, 1000);


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

            }

        });

    }

    private void apiCallForUpdateTallyAmt(String activityId, String batchId,
                                          String tallyAmountDb, String totalCreditAmt) {


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GetDataService.class);
        Call<JsonObject> call =
                service.updateTotalBatchAmtRecoveredAfterAddTransaction
                        (activityId, batchId, tallyAmountDb, totalCreditAmt);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:digital for tally amount :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }

        });
    }

    private void showTransDetailsToList(ErDigitalModel erDigitalModel,
                                        String finalTallAmtDb, String recoveryAmount1,
                                        String shiftWiseCredit) {
        Log.d(TAG, "showTransDetailsToList: finalTallAmtDb new or existing : " + finalTallAmtDb);

        List<PaymentModelLocal> payList = databaseHelper.getDonePaymentDetails();


        apiHandlerManager.apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(getActivity(),
                erDigitalModel.getActivityId(), erDigitalModel.getBatchId());

        if (!finalTallAmtDb.isEmpty() && finalTallAmtDb.equals("noupdate")) {

            if (payList != null && payList.size() > 0) {
                fillDigitalTransDetails(payList);
            }


            String tallyAmountDb = databaseHelper.getPaymentDetails(erDigitalModel.getBatchId());
            textViewAmountTally.setText(tallyAmountDb);

            String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
            Log.d(TAG, "doOnMainThread: recoveryAmount : " + recoveryAmount);
            textviewMeterSaleRs.setText(recoveryAmount);

            String shiftwiseCreditAmt = commonCodeManager.getTotalCreditAmountFinal(getActivity());

            apiCallForUpdateTallyAmt(erDigitalModel.getActivityId(),
                    erDigitalModel.getBatchId(), tallyAmountDb, shiftwiseCreditAmt);


        } else {
            textViewAmountTally.setText(finalTallAmtDb);

            textviewMeterSaleRs.setText(recoveryAmount1);

            apiCallForUpdateTallyAmt(erDigitalModel.getActivityId(),
                    erDigitalModel.getBatchId(), finalTallAmtDb, shiftWiseCredit);

            String[] dealerDetails = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
            String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());


            apiCallForShowAddedTransactionDetails(getActivity(), dealerDetails[0], dailySales[1]);

        }

      /*  if(tallyAmountDb!=null && recoveryAmount!=null) {
            Double difference = Double.parseDouble(recoveryAmount) - Double.parseDouble(tallyAmountDb);
            textViewDifference.setText(String.valueOf(difference));
        }*/
        // apiCallForGetAccountTransactionLogForDigital(getActivity());


    }

    private void fillDigitalTransDetails(List<PaymentModelLocal> digitalTransList) {
        String transCountDetails = "" + digitalTransList.size();
        editTextTransCount.setText(transCountDetails);
        double totalAmount = 0.0;

        for (int i = 0; i < digitalTransList.size(); i++) {
            PaymentModelLocal paymentModelLocal = digitalTransList.get(i);
            double digitalAmt = Double.parseDouble(paymentModelLocal.getAmount());
            totalAmount = totalAmount + digitalAmt;
            Log.d(TAG, "fillDigitalTransDetails: totalAmount : " + "" + totalAmount);

            editTextTotalAmt.setText("" + totalAmount);
        }


        ErDigitalTransDetailsAdapter erDigitalTransDetailsAdapter =
                new ErDigitalTransDetailsAdapter(getActivity(), digitalTransList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerArraivals.setLayoutManager(mLayoutManager);
        recyclerArraivals.setItemAnimator(new DefaultItemAnimator());
        erDigitalTransDetailsAdapter.notifyDataSetChanged();
        recyclerArraivals.setAdapter(erDigitalTransDetailsAdapter);


    }

    private void apiCallForAddMorePayDetailsByDigital(final Context context, final ErDigitalModel erDigitalModel) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] message = new String[1];
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addAccountTransactionLogForDigital(erDigitalModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddMorePayDetailsByDigital :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    //commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");
                    commonCodeManager.saveBatchIdForDigital(context, erDigitalModel.getBatchId());

                    if (status.equals("ok")) {
                        final AddNewPayDetailsModel addNewModel = commonCodeManager.getNewPayDetailsModelForExistingBatch(context);

                        //local db handling for info cards
                        commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(context, "noapicall");

                        commonCodeManager.saveConfirmRecoveryFlag(context, true);
                        commonCodeManager.saveLocalErData(context, erDigitalModel.getGrandTotalAmount(), "", "");
                        String payType = "";

                        payType = (String) spinnerSelectDevice.getSelectedItem();


                        PaymentModelLocal paymentModelLocal = new
                                PaymentModelLocal(erDigitalModel.getBatchId(),
                                payType, erDigitalModel.getGrandTotalAmount(),
                                erDigitalModel.getTransacId());

                        databaseHelper.addPaymentDetails(paymentModelLocal);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String tallyAmountDb = databaseHelper.getPaymentDetails(erDigitalModel.getBatchId());
                                // String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
                                //local db + prev entry from api

                                String totalRecoveredAlready = addNewModel.getTotalBatchAmtRecovered();
                                Log.d(TAG, "onResponse: totalRecoveredAlready : " + totalRecoveredAlready);
                                double finalRecoverdAlready;
                                if (totalRecoveredAlready != null && !totalRecoveredAlready.isEmpty() && !totalRecoveredAlready.equals("null")) {
                                    finalRecoverdAlready = Double.parseDouble(totalRecoveredAlready);

                                } else {
                                    finalRecoverdAlready = 0;
                                }
                                final double finalTallyAmtDB = Double.parseDouble(tallyAmountDb) + finalRecoverdAlready;
                                Log.d(TAG, "onResponse:finalTallyAmtDB :  " + finalTallyAmtDB);


                                final String recoveryAmount = addNewModel.getTotalBatchAmtExpected();


                                Log.d(TAG, "onResponse: digital:addentry  tallyAmountDb : " + finalTallyAmtDB);
                                Log.d(TAG, "onResponse: digital addentry  recoveryAmount :" + recoveryAmount);

                      /*  String totalCreditAmount = commonCodeManager.getTotalCreditAmountFinal(context);
                        Log.d(TAG, "onResponse: digital addentry totalCreditAmount  :  " + totalCreditAmount);
*/
                                String totalCreditAmount = addNewModel.getShiftWiseCreditSumForApp();
                                Log.d(TAG, "onResponse: digital addentry totalCreditAmount  :  " + totalCreditAmount);
                                //check for total due
                                double finalDueForCalc;
                                String checkTotalDue = commonCodeManager.getTotalDueInSales(context);


                                if (checkTotalDue != null && !checkTotalDue.isEmpty() && checkTotalDue.equals("nothing")) {
                                    double totalDue = Double.parseDouble(recoveryAmount) - Double.parseDouble(totalCreditAmount);
                                    Log.d(TAG, "onResponse: digital addentry totalDue : " + totalDue);

                                    finalDueForCalc = totalDue;
                                    //nothing paid by cash or digital
                                    commonCodeManager.saveTotalDueInSales(context, String.valueOf(totalDue));

                                } else {
                                    //check for null
                                    finalDueForCalc = Double.parseDouble(checkTotalDue);
                                }

                                Log.d(TAG, "onResponse:finalDueForCalc digital :after loop :   " + finalDueForCalc);


                       /* double differ = Double.parseDouble(recoveryAmount);
                        Log.d(TAG, "onResponse: digital addentry differ : " + differ);
*/
                                double difference = finalDueForCalc - finalTallyAmtDB;
                                Log.d(TAG, "onResponse: finalDueForCalc : " + finalDueForCalc);
                                Log.d(TAG, "onResponse: finalTallyAmtDB : " + finalTallyAmtDB);
                                Log.d(TAG, "onResponse: digital addentry difference: " + difference);

                                String[] details = {String.valueOf(finalTallyAmtDB), recoveryAmount, String.format("%.2f", difference)};
                                try {
                                    ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                                } catch (ClassCastException cce) {
                                    cce.printStackTrace();

                                }

                      /*
                      ****************previous working code
                      String tallyAmountDb = databaseHelper.getPaymentDetails(erDigitalModel.getBatchId());
                        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());
                        Double difference = Double.parseDouble(recoveryAmount) - Double.parseDouble(tallyAmountDb);

                        String[] details = {tallyAmountDb, recoveryAmount, String.valueOf(difference)};
                        try {
                            ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();

                        }*/

                       /* try {
                            ((UpdateDigitalTotalDetails) activity).updateDigitalDetails(details);
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();

                        }*/
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                                        showTransDetailsToList(erDigitalModel, String.valueOf(finalTallyAmtDB), recoveryAmount, addNewModel.getShiftWiseCreditSumForApp());


                                    }
                                }, 500);
                                // commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

                            }
                        }, 1000);


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissDialogWithToast(context, progressDialog, message[0]);

            }

        });

    }

    private void apiCallForDoneTransDetails(final Context context,
                                            final String fuelDealerStaffId,
                                            final String batchId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForDoneTransDetails: fuelDealerStaffId : " + fuelDealerStaffId);
        Log.d(TAG, "apiCallForDoneTransDetails: batchId : " + batchId);
        final List<PaymentModelLocal> erDigitalList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllTransactionDetailsByBatchId(fuelDealerStaffId, batchId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForDoneTransDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);
                            //String cashTotal = duObject.getString("cashTotal");
                            paymentModelLocal = new PaymentModelLocal();
                            String transacDate = duObject.getString("transacDate");
                            String transacId = duObject.getString("transacId");

                            String cardTotalAmount = duObject.getString("cardTotalAmount");
                            String paytmTotalAmount = duObject.getString("paytmTotalAmount");

                            String batchId = duObject.getString("batchId");
                            String corporateId = duObject.getString("corporateId");
                            String totalBatchAmtExpted = duObject.getString("totalBatchAmtExpted");
                            String recoveredAmtByApp = duObject.getString("recoveredAmtByApp");
                            String shiftWiseCreditSumForApp = duObject.getString("shiftWiseCreditSumForApp");


                            AddNewPayDetailsModel addNew = new AddNewPayDetailsModel(batchId, corporateId,
                                    totalBatchAmtExpted, recoveredAmtByApp, shiftWiseCreditSumForApp);


                            commonCodeManager.saveNewPayDetailsModelForExistingBatch(context, addNew);
                            //batchID, corporateid,recoveryamt,  tallyamt or recovered


                            if (transacId != null && !transacId.equals("null") && !transacId.isEmpty() && !transacId.equals("undefined")) {

                                if (cardTotalAmount != null && !cardTotalAmount.equals("null") && !cardTotalAmount.isEmpty() && !cardTotalAmount.equals("undefined")) {

                                    paymentModelLocal = new PaymentModelLocal("", "Card", cardTotalAmount, transacId);
                                }
                                if (paytmTotalAmount != null && !paytmTotalAmount.equals("null") && !paytmTotalAmount.isEmpty() && !paytmTotalAmount.equals("undefined")) {

                                    paymentModelLocal = new PaymentModelLocal("", "Paytm", paytmTotalAmount, transacId);

                                }
                                erDigitalList.add(paymentModelLocal);

                            }


                        }


                        fillDigitalTransDetails(erDigitalList);
                        //  updateCashUIAsPerResponse(ercashList);
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


    private boolean areValidateAddDetails() {
        boolean result = false;
        /* if (spinnerSelectedPosition == 0) {
            result = false;
            spinnerErrorText.setError("Please Select Device");
        }*/
        if (selectedTerminal.contains("Select")) {
            result = false;
            spinnerErrorText.setError("Please Select Device");
        } else if (TextUtils.isEmpty(editTextTransactionId.getText().toString())) {
            result = false;
            editTextTransactionId.setError("Please Enter Transaction ID");
        } else if (TextUtils.isEmpty(editTextAmount.getText().toString())) {
            result = false;
            editTextAmount.setError("Please Enter Amount");
        } else {
            result = true;
        }

        return result;
    }


    private void apiCallForShowAddedTransactionDetails(final Context context,
                                                       final String fuelDealerStaffId,
                                                       final String batchId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForShowAddedTransactionDetails: fuelDealerStaffId : " + fuelDealerStaffId);
        Log.d(TAG, "apiCallForShowAddedTransactionDetails: batchId : " + batchId);
        final List<PaymentModelLocal> erDigitalList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllTransactionDetailsByBatchId(fuelDealerStaffId, batchId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForShowAddedTransactionDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);
                            //String cashTotal = duObject.getString("cashTotal");
                            paymentModelLocal = new PaymentModelLocal();
                            String transacDate = duObject.getString("transacDate");
                            String transacId = duObject.getString("transacId");

                            String cardTotalAmount = duObject.getString("cardTotalAmount");
                            String paytmTotalAmount = duObject.getString("paytmTotalAmount");

                            String batchId = duObject.getString("batchId");
                            String corporateId = duObject.getString("corporateId");
                            String totalBatchAmtExpted = duObject.getString("totalBatchAmtExpted");
                            String recoveredAmtByApp = duObject.getString("recoveredAmtByApp");
                            String shiftWiseCreditSumForApp = duObject.getString("shiftWiseCreditSumForApp");


                            AddNewPayDetailsModel addNew = new AddNewPayDetailsModel(batchId, corporateId,
                                    totalBatchAmtExpted, recoveredAmtByApp, shiftWiseCreditSumForApp);


                         /*   commonCodeManager.saveNewPayDetailsModelForExistingBatch(context, addNew);
                            //batchID, corporateid,recoveryamt,  tallyamt or recovered
*/

                            if (transacId != null && !transacId.equals("null") && !transacId.isEmpty() && !transacId.equals("undefined")) {

                                if (cardTotalAmount != null && !cardTotalAmount.equals("null") && !cardTotalAmount.isEmpty() && !cardTotalAmount.equals("undefined")) {

                                    paymentModelLocal = new PaymentModelLocal("", "Card", cardTotalAmount, transacId);
                                }
                                if (paytmTotalAmount != null && !paytmTotalAmount.equals("null") && !paytmTotalAmount.isEmpty() && !paytmTotalAmount.equals("undefined")) {

                                    paymentModelLocal = new PaymentModelLocal("", "Paytm", paytmTotalAmount, transacId);

                                }
                                erDigitalList.add(paymentModelLocal);

                            }


                        }


                        showDetails(erDigitalList);
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

    private void showDetails(List<PaymentModelLocal> erDigitalList) {

        String transCountDetails = "" + erDigitalList.size();
        editTextTransCount.setText(transCountDetails);
        double totalAmount = 0.0;

        for (int i = 0; i < erDigitalList.size(); i++) {
            PaymentModelLocal paymentModelLocal = erDigitalList.get(i);
            double digitalAmt = Double.parseDouble(paymentModelLocal.getAmount());
            totalAmount = totalAmount + digitalAmt;
            Log.d(TAG, "fillDigitalTransDetails: totalAmount : " + "" + totalAmount);

            editTextTotalAmt.setText("" + totalAmount);
        }

        ErDigitalTransDetailsAdapter erDigitalTransDetailsAdapter =
                new ErDigitalTransDetailsAdapter(getActivity(), erDigitalList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerArraivals.setLayoutManager(mLayoutManager);
        recyclerArraivals.setItemAnimator(new DefaultItemAnimator());
        erDigitalTransDetailsAdapter.notifyDataSetChanged();
        recyclerArraivals.setAdapter(erDigitalTransDetailsAdapter);

    }
}
