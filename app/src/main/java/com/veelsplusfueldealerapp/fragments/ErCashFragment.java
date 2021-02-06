package com.veelsplusfueldealerapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.util.LogTime;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.DailySalesActivity;
import com.veelsplusfueldealerapp.activities.DailyStockReadingActivity;
import com.veelsplusfueldealerapp.activities.EarningsReportActivity;
import com.veelsplusfueldealerapp.activities.OperatorShiftStartEndActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.UpdateSalesDeatail;
import com.veelsplusfueldealerapp.models.AddNewPayDetailsModel;
import com.veelsplusfueldealerapp.models.ErCashModel;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.OperatorEndShiftModel;
import com.veelsplusfueldealerapp.models.PaymentModelLocal;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ErCashFragment extends Fragment {
    //ercash var response parse karaycha aahe
    //daily sales list chya response mdhe trans status pahije
    //daily sales adapter var trans status var condition
    ///parse latest api response on all 4 tabs
    private static final String TAG = "ErCashFragment";
    View view;
    EditText num1, num2, num3, num4, num5, num6, num7, num8, num9, num10, numrs2;
    TextView res, res2, res3, res4, res5, res6, res7, res8, res9, res10, res11, textViewResultFor2;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    List<String> allFuelInfras;
    String activityId = "";
    String[] details;
    ErCashModel erCashModel = null;
    LinearLayout layoutCashParent;
    TextView textviewMeterSaleRs, textViewAmountTally, textViewDifference;
    DatabaseHelper databaseHelper;
    Activity activity;
    String totalBatchAmtExpted, totalBatchAmtRecovered, totalShiftWiseCreditAmount;
    MaterialButton buttonAddCash;
    //RadioGroup radioGroupCashType;
    RadioButton radioBtnCashCount, radioBtnLumpSum;
    LinearLayout layoutCashCount;
    TextView textViewTotalLumpSum, textViewTotalCashPaid;
    String finalLumpSumCash;
    String lumpSum;
    TextView tvDifferCheck;
    private WeakReference<UpdateSalesDeatail> mCallBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_cash_frag_er, container, false);
        initUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


         /*   if (tallyAmountDb != null && recoveryAmount != null) {
            Double difference = Double.parseDouble(recoveryAmount) -
                    Double.parseDouble(tallyAmountDb);
            textViewDifference.setText(String.valueOf(difference));
        }

        */
        Log.d(TAG, "onResume: in onresume ercash");


        details = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
        String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());
        Log.d(TAG, "onResume: dailySales[0] : " + dailySales[0]);


        if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {
            Log.d(TAG, "onResume: for add new ");
            apiCallForGetFuelStaffPerformForAllPendingPayments(getActivity(), details[0]);

        } else {
            Log.d(TAG, "onResume: existing");

            apiCallForDoneTransDetails(getActivity(), details[0], dailySales[1]);
        }

    }

    private void apiCallForDoneTransDetails(final Context context,
                                            final String fuelDealerStaffId, final String batchId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForDoneTransDetails: fuelDealerStaffId : " + fuelDealerStaffId);
        Log.d(TAG, "apiCallForDoneTransDetails: batchId : " + batchId);
        final List<ErCashModel> ercashList = new ArrayList<>();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllTransactionDetailsByBatchIdForCashOnly(fuelDealerStaffId, batchId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForDoneTransDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    if (status.equals("ok")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            // for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(0);
                            String cashTotal = duObject.getString("totalCashSum");
                            String paytmTotalAmount = duObject.getString("totalPaytmSum");
                            String cardTotalAmount = duObject.getString("totalCardSum");
                            String grandTotalAmount = duObject.getString("grandTotalAmount");

                            totalBatchAmtExpted = duObject.getString("totalBatchAmtExpted");
                            String lumpSumCash = duObject.getString("totalLumSumCash");
                            String corporateId = duObject.getString("corporateId");


                            //try for new value
                            totalBatchAmtRecovered = duObject.getString("recoveredAmtByApp");

                            Log.d(TAG, "onResponse: totalBatchAmtRecovered new: " + totalBatchAmtRecovered);

                            totalShiftWiseCreditAmount = duObject.getString("shiftWiseCreditSumForApp");

                            Log.d(TAG, "onResponse: totalShiftWiseCreditAmount : " + totalShiftWiseCreditAmount);


                            //save details from api response about existing trans details
                            AddNewPayDetailsModel addNew = new AddNewPayDetailsModel(batchId, corporateId,
                                    totalBatchAmtExpted, totalBatchAmtRecovered, totalShiftWiseCreditAmount);

                            commonCodeManager.saveNewPayDetailsModelForExistingBatch(context, addNew);
                            //batchID, corporateid,recoveryamt,  tallyamt or recovered


                            //need to check condition avoiding null pointer exception
                            String differenceValue = "";
                            if (totalBatchAmtExpted != null && !totalBatchAmtExpted.equals("null") && !totalBatchAmtExpted.isEmpty() && !totalBatchAmtExpted.equals("undefined")) {
                                if (totalBatchAmtRecovered != null && !totalBatchAmtRecovered.equals("null") && !totalBatchAmtRecovered.isEmpty() && !totalBatchAmtRecovered.equals("undefined")) {
                                    String creditAmt;
                                    if (totalShiftWiseCreditAmount != null && !totalShiftWiseCreditAmount.equals("null") && !totalShiftWiseCreditAmount.isEmpty() && !totalShiftWiseCreditAmount.equals("undefined") && !totalShiftWiseCreditAmount.equals("new")) {
                                        creditAmt = totalShiftWiseCreditAmount;
                                    } else {
                                        creditAmt = "0";
                                    }

                                    double difference1 = Double.parseDouble(totalBatchAmtExpted) - Double.parseDouble(creditAmt);
                                    double difference2 = difference1 - Double.parseDouble(totalBatchAmtRecovered);

                                    // Double difference = Double.parseDouble(totalBatchAmtExpted) - Double.parseDouble(totalBatchAmtRecovered);
                                    differenceValue = String.format("%.2f", difference2);
                                } else {
                                    differenceValue = "Error getting value";
                                }
                            } else {
                                differenceValue = "Error getting value";

                            }


                            String finalTotalBatchAmtRecovered = String.format("%.2f", Double.parseDouble(totalBatchAmtRecovered));
                            String finaTotalBatchAmtExpted = String.format("%.2f", Double.parseDouble(totalBatchAmtExpted));
                            String finalDifferenceValue = String.format("%.2f", Double.parseDouble(differenceValue));

                            String[] details = {finalTotalBatchAmtRecovered, finaTotalBatchAmtExpted, finalDifferenceValue};
                            try {
                                ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                            } catch (ClassCastException cce) {
                                cce.printStackTrace();

                            }

                            Log.d(TAG, "onResponse: cashTotal : " + cashTotal);
                            /*Log.d(TAG, "onResponse: paytmTotalAmount : " + paytmTotalAmount);
                            Log.d(TAG, "onResponse: cardTotalAmount : " + cardTotalAmount);
*/
                            if (cashTotal != null && !cashTotal.equals("null") && !cashTotal.isEmpty()) {
                                String transacDate = duObject.getString("transacDate");
                                String twoThousands = duObject.getString("Rs2000Total");
                                String oneThousand = duObject.getString("Rs1000Total");
                                String fiveHundread = duObject.getString("Rs500Total");
                                String twoHundread = duObject.getString("Rs200Total");
                                String hundread = duObject.getString("Rs100Total");
                                String fifty = duObject.getString("Rs50Total");
                                String twenty = duObject.getString("Rs20Total");
                                String ten = duObject.getString("Rs10Total");
                                String five = duObject.getString("Rs5Total");
                                String two = duObject.getString("Rs2Total");
                                String one = duObject.getString("Rs1Total");


                                erCashModel = new ErCashModel(transacDate, fuelDealerStaffId,
                                        cashTotal, grandTotalAmount, twoThousands, oneThousand,
                                        fiveHundread, twoHundread, hundread, fifty, twenty,
                                        ten, five, two, one, "",
                                        "", "",
                                        "",
                                        "");
                                //ercashList.add(erCashModel);

                                //  updateCashUIAsPerResponse(ercashList);
                                String payBy = "cash";
                                if (lumpSumCash != null && !lumpSumCash.isEmpty()) {
                                    payBy = "lumpsum";
                                }

                                updateCashUIAsPerResponse(erCashModel, finaTotalBatchAmtExpted,
                                        finalTotalBatchAmtRecovered, payBy);


                            }

                            /*String finalLumpSum;
                            if (lumpSumCash != null && !lumpSumCash.equals("null") && !lumpSumCash.isEmpty()) {

                                finalLumpSum = lumpSumCash;
                            }else {
                                finalLumpSum = "0";
                            }*/


                            if (paytmTotalAmount.isEmpty() && cardTotalAmount.isEmpty()) {
                                commonCodeManager.saveTotalAmtDigitalForDoneCardClickView
                                        (context, grandTotalAmount, finaTotalBatchAmtExpted,
                                                finalTotalBatchAmtRecovered);
                            }

                            //}


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

    private void apiCallForAddNewValuesForCashIfDigitalPaid(final Context context,
                                                            final ErCashModel erCashModel) {

        Log.d(TAG, "apiCallForAddNewValuesForCashIfDigitalPaid: ");

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Log.d(TAG, "apiCallForAddNewValuesForCashIfDigitalPaid:cash total :  " + erCashModel.getCashTotal());
        Log.d(TAG, "apiCallForAddNewValuesForCashIfDigitalPaid: lumpsum : " + erCashModel.getLumpSumCash());


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addAccountTransactionLogForCash(erCashModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddNewValuesForCashIfDigitalPaid : " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");

                    if (status.equals("ok")) {
                        final AddNewPayDetailsModel addNewModel = commonCodeManager.getNewPayDetailsModelForExistingBatch(context);

                        //local db handling for info cards
                        commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(context, "noapicall");


                        PaymentModelLocal paymentModelLocal = new PaymentModelLocal(erCashModel.getBatchId(),
                                "cash", erCashModel.getCashTotal(), "");
                        databaseHelper.addPaymentDetails(paymentModelLocal);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                commonTaskManager.showToast(context, msg);
                                commonTaskManager.dismissProgressDialog(context, progressDialog);

                                String tallyAmountDb = databaseHelper.getPaymentDetails(erCashModel.getBatchId());
                                final String recoveryAmount = addNewModel.getTotalBatchAmtExpected();

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

                                Log.d(TAG, "onResponse: cash:addentry  tallyAmountDb : " + tallyAmountDb);
                                Log.d(TAG, "onResponse: cash addentry  recoveryAmount :" + recoveryAmount);

                                String totalCreditAmount = addNewModel.getShiftWiseCreditSumForApp();
                                // String totalCreditAmount = commonCodeManager.getTotalCreditAmountFinal(context);
                                Log.d(TAG, "onResponse: cash addentry totalCreditAmount  :  " + totalCreditAmount);


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

                                Log.d(TAG, "onResponse:finalDueForCalc cash :after loop :   " + finalDueForCalc);


                       /* double differ = Double.parseDouble(recoveryAmount);
                        Log.d(TAG, "onResponse: digital addentry differ : " + differ);
*/
                                double difference = finalDueForCalc - finalTallyAmtDB;
                                Log.d(TAG, "onResponse: cash finalDueForCalc : " + finalDueForCalc);
                                Log.d(TAG, "onResponse: cash finalTallyAmtDB : " + finalTallyAmtDB);
                                Log.d(TAG, "onResponse: cash addentry difference: " + difference);

                                String[] details = {String.valueOf(finalTallyAmtDB), recoveryAmount,
                                        String.format("%.2f", difference)};
                                try {
                                    ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                                } catch (ClassCastException cce) {
                                    cce.printStackTrace();

                                }

                       /*
                        **********Working code for without update cash
                        String tallyAmountDb = databaseHelper.getPaymentDetails(erCashModel.getBatchId());
                        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));

                        Log.d(TAG, "onResponse: cash:addentry  tallyAmountDb : " + tallyAmountDb);
                        Log.d(TAG, "onResponse: cash addentry  recoveryAmount :" + recoveryAmount);

                        String totalCreditAmount = commonCodeManager.getTotalCreditAmountFinal(context);
                        Log.d(TAG, "onResponse: cash addentry totalCreditAmount  :  " + totalCreditAmount);


                        //check for total due
                        double finalDueForCalc;
                        String checkTotalDue = commonCodeManager.getTotalDueInSales(context);
                        if (checkTotalDue != null && !checkTotalDue.isEmpty() && checkTotalDue.equals("nothing")) {
                            double totalDue = Double.parseDouble(recoveryAmount) - Double.parseDouble(totalCreditAmount);
                            Log.d(TAG, "onResponse: cash addentry totalDue : " + totalDue);

                            finalDueForCalc = totalDue;
                            //nothing paid by cash or digital
                            commonCodeManager.saveTotalDueInSales(context, String.valueOf(totalDue));

                        } else {
                            //check for null
                            finalDueForCalc = Double.parseDouble(checkTotalDue);
                        }

                        Log.d(TAG, "onResponse:finalDueForCalc cash :after loop :   " + finalDueForCalc);


                        double differ = Double.parseDouble(recoveryAmount);
                        Log.d(TAG, "onResponse: cash addentry differ : " + differ);

                        double difference = finalDueForCalc - Double.parseDouble(tallyAmountDb);
                        Log.d(TAG, "onResponse: cash addentry difference: " + difference);

                        String[] details = {tallyAmountDb, recoveryAmount, String.valueOf(difference)};
                        try {
                            ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                        } catch (ClassCastException cce) {
                            cce.printStackTrace();

                        }*/
                                commonCodeManager.saveConfirmRecoveryFlag(context, true);
                                commonCodeManager.saveLocalErData(context, erCashModel.getCashTotal(), "", "");
                                commonCodeManager.saveIsTransited(context, true);
                                doOnMainThread(erCashModel, context, String.valueOf(finalTallyAmtDB), recoveryAmount, totalCreditAmount);

                            }
                        }, 1000);


                    } else {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        commonTaskManager.showToast(context, msg);

                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, context.getResources().getString(R.string.unable_to_connect));

            }

        });

    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }

    private void updateCashUIAsPerResponse(ErCashModel erCashModel, String
            totalBatchAmtExpted, String totalBatchAmtRecovered, String payBy) {
        Log.d(TAG, "updateCashUIAsPerResponse: ");
        // num1, num2, num3, num4, num5, num6, num7, num8, num9, num10, numrs2
        Log.d(TAG, "updateCashUIAsPerResponse:2000:  " + erCashModel.getTwoThousands());

        radioBtnCashCount.setEnabled(false);
        radioBtnLumpSum.setEnabled(false);
        /*
         *******************************************************************
         *
         * Check for lump sum ui after done trans - enable/disable
         */
        Log.d(TAG, "updateCashUIAsPerResponse: payby : " + payBy);


        if (payBy.equals("cash")) {
            Log.d(TAG, "updateCashUIAsPerResponse: if pay cash");
            /*radioBtnCashCount.setChecked(true);
            radioBtnLumpSum.setChecked(false);*/
            textViewTotalCashPaid.setVisibility(View.VISIBLE);
            textViewTotalCashPaid.append(erCashModel.getCashTotal());
        } else {
         /*   radioBtnCashCount.setChecked(false);
            radioBtnLumpSum.setChecked(true);*/
            Log.d(TAG, "updateCashUIAsPerResponse:else lumpsum");

            textViewTotalLumpSum.setVisibility(View.VISIBLE);
            textViewTotalLumpSum.append(erCashModel.getCashTotal());
            Log.d(TAG, "updateCashUIAsPerResponse  " + erCashModel.getCashTotal());
        }


        num1.setEnabled(false);
        num2.setEnabled(false);
        num3.setEnabled(false);
        num4.setEnabled(false);
        num5.setEnabled(false);
        num6.setEnabled(false);
        num7.setEnabled(false);
        num8.setEnabled(false);
        num9.setEnabled(false);
        num10.setEnabled(false);
        numrs2.setEnabled(false);
        res11.setEnabled(false);
        buttonAddCash.setEnabled(false);

        num1.setText(erCashModel.getTwoThousands());
        num2.setText(erCashModel.getOneThousand());
        num3.setText(erCashModel.getFiveHundread());
        num4.setText(erCashModel.getTwoHundread());
        num5.setText(erCashModel.getHundread());
        num6.setText(erCashModel.getFifty());
        num7.setText(erCashModel.getTwenty());
        num8.setText(erCashModel.getTen());
        num9.setText(erCashModel.getFive());
        num10.setText(erCashModel.getOne());
        numrs2.setText(erCashModel.getTwo());
        res11.setText(erCashModel.getCashTotal());

    }

    private void initUI() {

        databaseHelper = new DatabaseHelper(getActivity());
        allFuelInfras = new ArrayList<>();
        View allValues = view.findViewById(R.id.layout_collection_tally_cash);

        textviewMeterSaleRs = allValues.findViewById(R.id.textview_meter_sale_rs);
        textViewAmountTally = allValues.findViewById(R.id.textview_amount_tally);
        textViewDifference = allValues.findViewById(R.id.textview_difference);

        commonTaskManager = new CommonTaskManager(getActivity());
        commonCodeManager = new CommonCodeManager(getActivity());
        num1 = (EditText) view.findViewById(R.id.editText_count2000);
        res = view.findViewById(R.id.textView_result2000);
        num2 = (EditText) view.findViewById(R.id.editText_count1000);
        res2 = view.findViewById(R.id.textView_result1000);
        num3 = (EditText) view.findViewById(R.id.editText_count500);
        res3 = view.findViewById(R.id.textView_result500);
        num4 = (EditText) view.findViewById(R.id.editText_count200);
        res4 = view.findViewById(R.id.textView_result200);
        num5 = (EditText) view.findViewById(R.id.editText_count100);
        res5 = view.findViewById(R.id.textView_result100);
        num6 = (EditText) view.findViewById(R.id.editText_count50);
        res6 = view.findViewById(R.id.textView_result50);
        num7 = (EditText) view.findViewById(R.id.editText_count20);
        res7 = view.findViewById(R.id.textView_result20);
        num8 = (EditText) view.findViewById(R.id.editText_count10);
        res8 = view.findViewById(R.id.textView_result10);
        num9 = (EditText) view.findViewById(R.id.editText_count5);
        res9 = view.findViewById(R.id.textView_result5);
        num10 = (EditText) view.findViewById(R.id.editText_count1);
        res10 = view.findViewById(R.id.textView_result1);
        res11 = view.findViewById(R.id.textView_resultcashTotal);
        numrs2 = view.findViewById(R.id.editText_count2);
        textViewResultFor2 = view.findViewById(R.id.textView_resultfor2);
        buttonAddCash = (MaterialButton) view.findViewById(R.id.button_calculate);
        layoutCashParent = view.findViewById(R.id.layout_cash_parent);

        layoutCashCount = view.findViewById(R.id.layout_cash_count);
        textViewTotalLumpSum = view.findViewById(R.id.tv_total_lumpsum);
        textViewTotalCashPaid = view.findViewById(R.id.tv_total_cashpaid);


        //radioGroupCashType = view.findViewById(R.id.radiogr_cash_type);
        radioBtnCashCount = view.findViewById(R.id.radiobtn_cash_count);
        radioBtnLumpSum = view.findViewById(R.id.radiobtn_lumpsum);

        tvDifferCheck = Objects.requireNonNull(getActivity()).findViewById(R.id.textview_difference);


        if (radioBtnCashCount.isChecked()) {
            buttonAddCash.setEnabled(true);
            layoutCashParent.setEnabled(true);
            radioBtnLumpSum.setChecked(false);
        } else {
            buttonAddCash.setEnabled(false);
            layoutCashParent.setEnabled(false);
            radioBtnCashCount.setEnabled(false);
        }


        radioBtnCashCount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    buttonAddCash.setEnabled(true);
                    layoutCashParent.setEnabled(true);
                    radioBtnLumpSum.setChecked(false);

                } else {
                    buttonAddCash.setEnabled(false);
                    layoutCashParent.setEnabled(false);
                    radioBtnCashCount.setChecked(false);

                }
            }
        });

        radioBtnLumpSum.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    buttonAddCash.setEnabled(false);
                    layoutCashParent.setEnabled(false);
                    radioBtnCashCount.setChecked(false);

                    showalertDialogForLumpSumCount();
                } else {
                    buttonAddCash.setEnabled(true);
                    layoutCashParent.setEnabled(true);
                    radioBtnLumpSum.setChecked(false);


                }
            }
        });

     /*   radioGroupCashType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioBtnCashCount = (RadioButton) group.findViewById(checkedId);
                if (null != radioBtnCashCount) {
                    String whichBtn = radioBtnCashCount.getText().toString().toLowerCase().trim();
                    Log.d(TAG, "onCheckedChanged:whichBtnReq:  " + whichBtn);

                    if (whichBtn.contains("cash")) {

                        buttonAdd.setEnabled(true);
                        layoutCashParent.setEnabled(true);

                    }

                    if (whichBtn.contains("lump")) {
                        buttonAdd.setEnabled(false);
                        layoutCashParent.setEnabled(false);

                        showalertDialogForLumpSumCount();
                    }
                }
            }
        });
*/
        setHeaderParams();

        buttonAddCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));


                //check for difference zero
                String difference = tvDifferCheck.getText().toString();
                Log.d(TAG, "onClick: diff check : " + difference);

                if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {
                    //Whole amount details submitted. You can't enter more.
                    Toast.makeText(getActivity(), "Whole amount details submitted. You can't enter more", Toast.LENGTH_LONG).show();
                    return;
                }


                String mynum1 = num1.getText().toString();
                if (mynum1.isEmpty()) {
                    mynum1 = "0";
                }
                int mnum1 = Integer.parseInt(mynum1);
                int textView_result2000 = (2000 * mnum1);
                String finalres = String.valueOf(textView_result2000);
                res.setText(finalres);

                String mynum2 = num2.getText().toString();
                if (mynum2.isEmpty()) {
                    mynum2 = "0";
                }
                int mnum2 = Integer.parseInt(mynum2);
                int textView_result1000 = (1000 * mnum2);
                String finalres2 = String.valueOf(textView_result1000);
                res2.setText(finalres2);

                String mynum3 = num3.getText().toString();
                if (mynum3.isEmpty()) {
                    mynum3 = "0";
                }
                int mnum3 = Integer.parseInt(mynum3);
                int textView_result500 = (500 * mnum3);
                String finalres3 = String.valueOf(textView_result500);
                res3.setText(finalres3);

                String mynum4 = num4.getText().toString();
                if (mynum4.isEmpty()) {
                    mynum4 = "0";
                }
                int mnum4 = Integer.parseInt(mynum4);
                int textView_result200 = (200 * mnum4);
                String finalres4 = String.valueOf(textView_result200);
                res4.setText(finalres4);

                String mynum5 = num5.getText().toString();
                if (mynum5.isEmpty()) {
                    mynum5 = "0";
                }
                int mnum5 = Integer.parseInt(mynum5);
                int textView_result100 = (100 * mnum5);
                String finalres5 = String.valueOf(textView_result100);
                res5.setText(finalres5);

                String mynum6 = num6.getText().toString();
                if (mynum6.isEmpty()) {
                    mynum6 = "0";
                }
                int mnum6 = Integer.parseInt(mynum6);
                int textView_result50 = (50 * mnum6);
                String finalres6 = String.valueOf(textView_result50);
                res6.setText(finalres6);

                String mynum7 = num7.getText().toString();
                if (mynum7.isEmpty()) {
                    mynum7 = "0";
                }
                int mnum7 = Integer.parseInt(mynum7);
                int textView_result20 = (20 * mnum7);
                String finalres7 = String.valueOf(textView_result20);
                res7.setText(finalres7);

                String mynum8 = num8.getText().toString();
                if (mynum8.isEmpty()) {
                    mynum8 = "0";
                }
                int mnum8 = Integer.parseInt(mynum8);
                int textView_result10 = (10 * mnum8);
                String finalres8 = String.valueOf(textView_result10);
                res8.setText(finalres8);

                String mynum9 = num9.getText().toString();
                if (mynum9.isEmpty()) {
                    mynum9 = "0";
                }
                int mnum9 = Integer.parseInt(mynum9);
                int textView_result5 = (5 * mnum9);
                String finalres9 = String.valueOf(textView_result5);
                res9.setText(finalres9);

                String for2 = numrs2.getText().toString();
                if (for2.isEmpty()) {
                    for2 = "0";
                }
                int mnumfor2 = Integer.parseInt(for2);
                int textView_result2 = (2 * mnumfor2);
                String finalresfor2 = String.valueOf(textView_result2);
                textViewResultFor2.setText(finalresfor2);


                String mynum10 = num10.getText().toString();
                if (mynum10.isEmpty()) {
                    mynum10 = "0";
                }
                int mnum10 = Integer.parseInt(mynum10);
                int textView_result1 = (1 * mnum10);
                String finalres10 = String.valueOf(textView_result1);
                res10.setText(finalres10);

                int textView_resultcashTotal = textView_result2000 + textView_result1000
                        + textView_result500 + textView_result200 + textView_result100
                        + textView_result50 + textView_result20 + textView_result10
                        + textView_result5 + textView_result2 + textView_result1;
                String finalResult = String.valueOf(textView_resultcashTotal);
                res11.setText(finalResult);

                //check for new entry or add new to existing
                String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());

                if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {


                    activityId = "";

                    //  String activityId = "";
                    if (allFuelInfras.size() != 0) {
                        String[] allInfraId = new String[allFuelInfras.size()];

                        for (int i = 0; i < allFuelInfras.size(); i++) {
                            allInfraId[i] = allFuelInfras.get(i);
                            activityId = activityId + allFuelInfras.get(i) + "_";
                            Log.d(TAG, "onClick: activity id: " + activityId);

                        }
                    }
                    int position = activityId.lastIndexOf("_");
                    if (position != -1)
                        activityId = activityId.substring(0, position);
                    Log.d(TAG, "onClick: activityID : " + activityId);
                    // mmddhh(random6digit)
                    String[] dealerDetails = commonCodeManager.getDealerDetails(getActivity());


                    String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
                    String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());
                    String corporateId = commonCodeManager.getCorporateId(getActivity());
                    Log.d(TAG, "onClick:tallyAmountDb :  " + tallyAmountDb);
                    Log.d(TAG, "onClick: recoveryAmount : " + recoveryAmount);

             /*    final ErCashModel erCashModel = new ErCashModel
                        (commonTaskManager.getCurrentDateNew(),
                                dealerDetails[0], finalResult, finalResult, mynum1, mynum2, mynum3,
                                mynum4, mynum5, mynum6, mynum7, mynum8, mynum9, for2,
                                mynum10, activityId,
                                commonCodeManager.getBatchIdForErReport(getActivity()),
                                recoveryAmount, tallyAmountDb, corporateId);
*/
                    final ErCashModel erCashModel = new ErCashModel
                            (commonTaskManager.getCurrentDateNew(),
                                    dealerDetails[0], finalResult, finalResult, mynum1, mynum2, mynum3,
                                    mynum4, mynum5, mynum6, mynum7, mynum8, mynum9, for2,
                                    mynum10, activityId,
                                    commonCodeManager.getBatchIdForErReport(getActivity()),
                                    recoveryAmount, finalResult, corporateId, "",
                                    tallyAmountDb);


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    String dialogTitle = "Cash Total : " + finalResult;

                    builder.setTitle(dialogTitle);
                    builder.setMessage("Confirm your cash amount by clicking 'Confirm' or 'Review' for review")
                            .setCancelable(false)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    apiCallForAddAccountTransactionLogForCash(getActivity(), erCashModel);

                                    activityId = "";
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




                /*
                textviewMeterSaleRs.setText("");
                textViewAmountTally.setText(finalResult);*/
                 /*  String[] details = {erCashModel.getCashTotal()};
                final UpdateSalesDeatail callBack = mCallBack.get();
                if (callBack != null) {
                    callBack.updateSalesDeatails(details);
                }*/

                } else {
                    //for add new to existing


                    AddNewPayDetailsModel addNewModel = commonCodeManager.getNewPayDetailsModelForExistingBatch(getActivity());
                    String activityId = commonCodeManager.getActivityId(Objects.requireNonNull(getActivity()));

                    final ErCashModel erCashModel = new ErCashModel
                            (commonTaskManager.getCurrentDateNew(),
                                    details[0], finalResult, finalResult, mynum1, mynum2, mynum3,
                                    mynum4, mynum5, mynum6, mynum7, mynum8, mynum9, for2,
                                    mynum10, activityId,
                                    commonCodeManager.getBatchIdForErReport(Objects.requireNonNull(getActivity())),
                                    addNewModel.getTotalBatchAmtExpected(), finalResult,
                                    addNewModel.getCorporateID(), "",
                                    addNewModel.getTotalBatchAmtRecovered());

                    if (erCashModel != null) {
                        apiCallForAddNewValuesForCashIfDigitalPaid(getActivity(), erCashModel);

                    }


                }
            }
        });
    }

    private void setHeaderParams() {
        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
        textviewMeterSaleRs.setText(recoveryAmount);
        Log.d(TAG, "setHeaderParams: recoveryAmount : " + recoveryAmount);

        String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
        if (tallyAmountDb != null && tallyAmountDb.isEmpty()) {
            textViewAmountTally.setText("0");
            Log.d(TAG, "setHeaderParams: if part tallyAmountDb");

        } else {
            textViewAmountTally.setText(tallyAmountDb);

        }

    }

    private void apiCallForAddAccountTransactionLogForCash(final Context context,
                                                           final ErCashModel erCashModel) {
        Log.d(TAG, "apiCallForAddAccountTransactionLogForCash: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addAccountTransactionLogForCash(erCashModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddAccountTransactionLogForCash :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");

                    if (status.equals("ok")) {
                        commonTaskManager.showToast(context, msg);
                        //local db handling for info cards
                        commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(context, "noapicall");


                        PaymentModelLocal paymentModelLocal = new PaymentModelLocal(erCashModel.getBatchId(),
                                "cash", erCashModel.getCashTotal(), "");
                        databaseHelper.addPaymentDetails(paymentModelLocal);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                String tallyAmountDb = databaseHelper.getPaymentDetails(erCashModel.getBatchId());
                                String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
                                Log.d(TAG, "onResponse: cash:addentry  tallyAmountDb : " + tallyAmountDb);
                                Log.d(TAG, "onResponse: cash addentry  recoveryAmount :" + recoveryAmount);

                                String totalCreditAmount = commonCodeManager.getTotalCreditAmountFinal(context);
                                Log.d(TAG, "onResponse: cash addentry totalCreditAmount  :  " + totalCreditAmount);


                                //check for total due
                                double finalDueForCalc;
                                String checkTotalDue = commonCodeManager.getTotalDueInSales(context);
                                if (checkTotalDue != null && !checkTotalDue.isEmpty() && checkTotalDue.equals("nothing")) {
                                    double totalDue = Double.parseDouble(recoveryAmount) - Double.parseDouble(totalCreditAmount);
                                    Log.d(TAG, "onResponse: cash addentry totalDue : " + totalDue);

                                    finalDueForCalc = totalDue;
                                    //nothing paid by cash or digital
                                    commonCodeManager.saveTotalDueInSales(context, String.valueOf(totalDue));

                                } else {
                                    //check for null
                                    finalDueForCalc = Double.parseDouble(checkTotalDue);
                                }

                                Log.d(TAG, "onResponse:finalDueForCalc cash :after loop :   " + finalDueForCalc);


                                double differ = Double.parseDouble(recoveryAmount);
                                Log.d(TAG, "onResponse: cash addentry differ : " + differ);

                                double difference = finalDueForCalc - Double.parseDouble(tallyAmountDb);
                                Log.d(TAG, "onResponse: cash addentry difference: " + difference);

                                String[] details = {tallyAmountDb, recoveryAmount,
                                        String.format("%.2f", difference)};
                                try {
                                    ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                                } catch (ClassCastException cce) {
                                    cce.printStackTrace();

                                }

                                commonCodeManager.saveConfirmRecoveryFlag(context, true);
                                commonCodeManager.saveLocalErData(context, erCashModel.getCashTotal(), "", "");
                                commonCodeManager.saveIsTransited(context, true);
                                doOnMainThread(erCashModel, context, "noupdate", "", "");


                            }
                        }, 1000);


                    } else {
                        commonTaskManager.showToast(context, msg);

                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, context.getResources().getString(R.string.unable_to_connect));

            }

        });

    }

    private void doOnMainThread(final ErCashModel erCashModel,
                                final Context context, final String finalTallAmtDb,
                                final String recoveryAmount1,
                                final String shiftWiseCredit) {


        Log.d(TAG, "doOnMainThread: ");

        radioBtnLumpSum.setEnabled(false);
        radioBtnCashCount.setEnabled(false);

        //

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait!");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //disable submit cash button

        buttonAddCash.setEnabled(false);

        String lumpSumCash = erCashModel.getLumpSumCash();
        if (lumpSumCash != null && !lumpSumCash.isEmpty() && !lumpSumCash.equals("null") && !lumpSumCash.equals("undefined")) {

            textViewTotalLumpSum.setVisibility(View.VISIBLE);
            textViewTotalLumpSum.append(erCashModel.getLumpSumCash());
        } else {
            textViewTotalCashPaid.setVisibility(View.VISIBLE);
            textViewTotalCashPaid.append(erCashModel.getCashTotal());
        }

        if (!finalTallAmtDb.isEmpty() && finalTallAmtDb.equals("noupdate")) {
            Log.d(TAG, "doOnMainThread: in no update ");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    String tallyAmountDb = databaseHelper.getPaymentDetails(erCashModel.getBatchId());
                    textViewAmountTally.setText(tallyAmountDb);

                    String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(context);
                    Log.d(TAG, "doOnMainThread: recoveryAmount : " + recoveryAmount);
                    textviewMeterSaleRs.setText(recoveryAmount);

                    String finalTallyAmt;
                    if (tallyAmountDb != null && !tallyAmountDb.equals("null") && !tallyAmountDb.isEmpty()) {
                        finalTallyAmt = tallyAmountDb;
                    } else {
                        finalTallyAmt = "0";
                    }

                    double differenceFinal = Double.parseDouble(recoveryAmount) - Double.parseDouble(finalTallyAmt);
                    String formatedDiffer = String.format("%.2f", differenceFinal);
                    textViewDifference.setText(formatedDiffer);

                    Log.d(TAG, "doOnMainThread: ");
                    apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(getActivity(),
                            erCashModel.getActivityId(), erCashModel.getBatchId());

                    String totalCreditAmt = commonCodeManager.getTotalCreditAmountFinal(Objects.requireNonNull(getActivity()));
                    Log.d(TAG, "doOnMainThread: totalCreditAmt : " + totalCreditAmt);

                    apiCallForUpdateTallAmtAndShiftWiseCreditAmt(getActivity(), tallyAmountDb, totalCreditAmt, erCashModel);

                    progressDialog.dismiss();
                }
            }, 3000);


        } else {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    // String tallyAmountDb = databaseHelper.getPaymentDetails(erCashModel.getBatchId());
                    textViewAmountTally.setText(finalTallAmtDb);

                  /*  String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(context);
                    Log.d(TAG, "doOnMainThread: recoveryAmount : " + recoveryAmount);*/
                    textviewMeterSaleRs.setText(recoveryAmount1);

                    String finalTallyAmt;
                    if (finalTallAmtDb != null && !finalTallAmtDb.equals("null") && !finalTallAmtDb.isEmpty()) {
                        finalTallyAmt = finalTallAmtDb;
                    } else {
                        finalTallyAmt = "0";
                    }

                    double differenceFinal = Double.parseDouble(recoveryAmount1) - Double.parseDouble(finalTallyAmt);
                    textViewDifference.setText(String.valueOf(differenceFinal));

                    Log.d(TAG, "doOnMainThread: ");
                    apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(getActivity(),
                            erCashModel.getActivityId(), erCashModel.getBatchId());

                    apiCallForUpdateTallAmtAndShiftWiseCreditAmt(getActivity(), finalTallAmtDb, shiftWiseCredit, erCashModel);


                    progressDialog.dismiss();
                }
            }, 3000);


        }

     /*   UpdateRecoveryAmountAsync async = new UpdateRecoveryAmountAsync(getActivity());
        async.execute("");*/

/*
        APIHandlerManager apiHandlerManager = new APIHandlerManager(getActivity());
        apiHandlerManager.apiCallForAddRecoveryBatchIdAfterTransUpdateStatus
                (getActivity(), this.erCashModel.getActivityId(),
                        this.erCashModel.getBatchId());*/
/*

        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());
        commonCodeManager.saveCashDetails(getActivity(), erCashModel.getCashTotal());

        Log.d(TAG, "doOnMainThread:recoveryAmount :  " + recoveryAmount);
        String[] digitalDetails = commonCodeManager.getDigitalDetails(getActivity());
        textviewMeterSaleRs.setText(recoveryAmount);
        Double tallyAmount = Double.parseDouble(erCashModel.getCashTotal()) + Double.parseDouble(digitalDetails[0]) + Double.parseDouble(digitalDetails[1]);

        textViewAmountTally.setText(String.valueOf(tallyAmount));
        Double difference = Double.parseDouble(recoveryAmount) - Double.parseDouble(erCashModel.getCashTotal());
        textViewDifference.setText(String.valueOf(difference));
        Log.d(TAG, "doOnMainThread:textViewDifference :  " + difference);
*/


    }

    private void apiCallForUpdateTallAmtAndShiftWiseCreditAmt(Context context,
                                                              String tallyAmountDb,
                                                              String totalCreditAmt,
                                                              ErCashModel erCashModel) {
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GetDataService.class);
        Call<JsonObject> call =
                service.updateTotalBatchAmtRecoveredAfterAddTransaction
                        (erCashModel.getActivityId(), erCashModel.getBatchId(),
                                tallyAmountDb, totalCreditAmt);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:for tally amount :  " + jsonData);
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

    private void goToSalesListActivity(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent(getActivity(), DailySalesActivity.class);
        startActivity(intent);*/

    }

    private void apiCallForGetFuelStaffPerformForAllPendingPayments(final Context context,
                                                                    final String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformForAllPendingPayments(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOperatorShiftDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);
                            String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                            allFuelInfras.add(fuelStaffPerformId);
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

    public void apiCallForAddRecoveryBatchIdAfterTransUpdateStatus(
            final Context context, String activityId, String batchId) {
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

    private void showalertDialogForLumpSumCount() {
        AlertDialog.Builder builder
                = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_dialog_lumpsum, null);
        builder.setView(customLayout);

        final EditText etLumpSum = customLayout.findViewById(R.id.edittext_lumpsum);
        MaterialButton buttonLumpSum = customLayout.findViewById(R.id.button_submit_lumpsum);
        ImageView imageviewClose = customLayout.findViewById(R.id.iv_close_lumpsum);


        final AlertDialog dialog
                = builder.create();
        dialog.show();

        imageviewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        //check for new entry or add new to existing
        String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());

        if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {
            //for new entry
            buttonLumpSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //check for difference zero
                    String difference = tvDifferCheck.getText().toString();
                    Log.d(TAG, "onClick: diff check : " + difference);

                    if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {
                        //Whole amount details submitted. You can't enter more.
                        Toast.makeText(getActivity(), "Whole amount details submitted. You can't enter more", Toast.LENGTH_LONG).show();
                        return;
                    }

                    lumpSum = etLumpSum.getText().toString();
                    Log.d(TAG, "onClick: lump sum = : " + lumpSum);
                    String[] dealerDetails = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));
                    String corporateId = commonCodeManager.getCorporateId(getActivity());
                    String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
                    String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());


                    activityId = "";

                    //  String activityId = "";
                    if (allFuelInfras.size() != 0) {
                        String[] allInfraId = new String[allFuelInfras.size()];

                        for (int i = 0; i < allFuelInfras.size(); i++) {
                            allInfraId[i] = allFuelInfras.get(i);
                            activityId = activityId + allFuelInfras.get(i) + "_";
                            Log.d(TAG, "onClick: activity id: " + activityId);

                        }
                    }
                    int position = activityId.lastIndexOf("_");
                    if (position != -1)
                        activityId = activityId.substring(0, position);
                    Log.d(TAG, "onClick: activityID : " + activityId);

                    ErCashModel erCashModel = new ErCashModel
                            (commonTaskManager.getCurrentDateNew(),
                                    dealerDetails[0], lumpSum, lumpSum, "0", "0", "0",
                                    "0", "0", "0", "0", "0", "0", "0",
                                    "0", activityId,
                                    commonCodeManager.getBatchIdForErReport(getActivity()),
                                    recoveryAmount, lumpSum, corporateId, lumpSum,
                                    tallyAmountDb);

                    Log.d(TAG, "onClick: adding lump sum");


                    apiCallForAddAccountTransactionLogForCash(getActivity(), erCashModel);
                    dialog.dismiss();

                }
            });

        } else {
            //add pay details for existing batch
            //check for difference zero
            //textview_difference
            Log.d(TAG, "showalertDialogForLumpSumCount: tvDifferCheck : " + tvDifferCheck.getText().toString());


            String difference = textViewDifference.getText().toString();
            Log.d(TAG, "onClick: diff check : " + difference);

            if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {
                return;
            }


            buttonLumpSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //check for difference zero
                    String difference = tvDifferCheck.getText().toString();
                    Log.d(TAG, "onClick: diff check : " + difference);

                    if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {
                        //Whole amount details submitted. You can't enter more.
                        Toast.makeText(getActivity(), "Whole amount details submitted. You can't enter more", Toast.LENGTH_LONG).show();
                        return;
                    }

                    AddNewPayDetailsModel addNewModel = commonCodeManager.getNewPayDetailsModelForExistingBatch(getActivity());
                    String activityId = commonCodeManager.getActivityId(Objects.requireNonNull(getActivity()));
                    lumpSum = etLumpSum.getText().toString();

                    Log.d(TAG, "onClick: lumpsum  : " + lumpSum);

                    ErCashModel erCashModel = new ErCashModel
                            (commonTaskManager.getCurrentDateNew(),
                                    details[0], lumpSum, lumpSum, "0", "0", "0",
                                    "0", "0", "0", "0", "0", "0", "0",
                                    "0", activityId,
                                    commonCodeManager.getBatchIdForErReport(getActivity()),
                                    addNewModel.getTotalBatchAmtExpected(),
                                    lumpSum, addNewModel.getCorporateID(), lumpSum,
                                    addNewModel.getTotalBatchAmtRecovered());


                    if (erCashModel != null) {
                        apiCallForAddNewValuesForCashIfDigitalPaid(getActivity(), erCashModel);
                        dialog.dismiss();

                    }

                }
            });


        }

    }


     /*
       private boolean checkForExceededPayment() {//check for difference zero
        String difference = tvDifferCheck.getText().toString();
        Log.d(TAG, "onClick: diff check : " + difference);

        if (difference.equals("0") || difference.equals("0.0") || difference.equals("0.00")) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Alert !");
            builder.setMessage("Whole amount details submitted. You can't enter more.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            final AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
            alertDialog.show();


            return;
        }
    }*/

}



