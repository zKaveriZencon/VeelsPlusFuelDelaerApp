package com.veelsplusfueldealerapp.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.EarningsReportActivity;
import com.veelsplusfueldealerapp.adapters.ErSalesInfoAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.ShiftWiseCreditAmountManager;
import com.veelsplusfueldealerapp.managers.UpdateSalesDeatail;
import com.veelsplusfueldealerapp.models.DailySalesInfoModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.OperatorInfoFragModel;
import com.veelsplusfueldealerapp.models.SalesInfoCardModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErInfoFragment extends Fragment {
    private static final String TAG = "ErInfoFragment";
    View view;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerView;
    String[] details;
    CommonCodeManager commonCodeManager;
    Double recoveryAmt = 0.0;
    TextView textviewMeterSaleRs, textViewAmountTally, textViewDifference, textViewTotalCreditAmtFinal;
    DatabaseHelper databaseHelper;
    Activity activity;
    String checkForMeter = "0";
    LinearLayout layoutPArent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_info_er_frag, container, false);
        initUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        details = commonCodeManager.getDealerDetails(Objects.requireNonNull(getActivity()));

        String isCallForCardAPI = commonCodeManager.getIsGiveCallForAPIInfoTabDailySales(getActivity());

        Log.d(TAG, "onResume info: for isCallForCardAPI:  " + isCallForCardAPI);

        String[] dailySales = commonCodeManager.getDailySalesCardEssentials(getActivity());
        Log.d(TAG, "onResume info: for addnew or fordone : " + dailySales[0]);


        if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {
            Log.d(TAG, "onResume info: for add new ");

            if (!isCallForCardAPI.isEmpty() && isCallForCardAPI.equals("apicall")) {
                Log.d(TAG, "onResume info: apicall : isCallForCardAPI : " + isCallForCardAPI);
                apiCallForGetFuelStaffPerformForAllPendingPayments(getActivity(), details[0]);

            }
            if (!isCallForCardAPI.isEmpty() && isCallForCardAPI.equals("noapicall")) {

                Log.d(TAG, "onResume info:noapicall: " + isCallForCardAPI);

                Snackbar snackbar = Snackbar
                        .make(layoutPArent, "Details Submitted ! !", Snackbar.LENGTH_SHORT);
                View view = snackbar.getView();
                FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
                params1.gravity = Gravity.TOP;
                view.setLayoutParams(params1);
                view.setBackgroundColor(getResources().getColor(R.color.colorCommonGreen));
                snackbar.show();

                List<OperatorInfoFragModel> infoCardsList = databaseHelper.getInfoCardsForDailySales();
                fillTheInfoCards(infoCardsList);
            }


        } else {
            Log.d(TAG, "onResume: existing");
            apiCallForGeetingDoneTransDetails(getActivity(), details[0], dailySales[1]);
        }


       /* if (tallyAmountDb != null && recoveryAmount != null) {
            Double difference = Double.parseDouble(recoveryAmount) - Double.parseDouble(tallyAmountDb);
            textViewDifference.setText(String.valueOf(difference));
        }
*/

    }

    private void apiCallForGeetingDoneTransDetails(final Context context,
                                                   String fuelDealerStaffId,
                                                   String batchID) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<OperatorInfoFragModel> pendingPays = new ArrayList<>();
        final List<String> performIds = new ArrayList<>();
        final List<String> ids = new ArrayList<>();

        Log.d(TAG, "apiCallForGeetingDoneTransDetails: " + batchID);


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformByRecoveryStatusSUBMIT
                (fuelDealerStaffId, batchID);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGeetingDoneTransDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String closeMetDateTime = duObject.getString("closeMetDateTime");
                                String productCategory = duObject.getString("productCategory");
                                String unitSales = duObject.getString("unitSales");
                                String recoveryAmount = duObject.getString("recoveryAmount");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                                String productName = duObject.getString("productName");
                                String duNo = duObject.getString("duNo");
                                String nozNo = duObject.getString("nozNo");
                                String finalProduct = productCategory + " - " + productName;
                                String duNoz = duNo + " - " + nozNo;
                                Log.d(TAG, "onResponse: duNoz : " + duNoz);
                                OperatorInfoFragModel operatorInfoFragModel = new OperatorInfoFragModel(closeMetDateTime,
                                        finalProduct, unitSales, recoveryAmount, duNoz);
                                /*OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(closeMetDateTime,
                                                finalProduct, unitSales, recoveryAmount);*/


                                pendingPays.add(operatorInfoFragModel);

                                performIds.add(fuelStaffPerformId);
                            }
                            fillDoneCardDetails(pendingPays, performIds);

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

    private void fillDoneCardDetails(List<OperatorInfoFragModel> pendingPays,
                                     List<String> performIdsList) {


        //save activity id
        String activityId = "";

        if (performIdsList.size() != 0) {
            String[] allInfraId = new String[performIdsList.size()];

            for (int i = 0; i < performIdsList.size(); i++) {
                allInfraId[i] = performIdsList.get(i);
                activityId = activityId + performIdsList.get(i) + "_";
                Log.d(TAG, "onClick: activity id on info: " + activityId);

            }
        }
        int position = activityId.lastIndexOf("_");
        if (position != -1)
            activityId = activityId.substring(0, position);
        Log.d(TAG, "onClick: activityID on info : " + activityId);

        commonCodeManager.saveActivityId(Objects.requireNonNull(getActivity()), activityId);

        ErSalesInfoAdapter erSalesInfoAdapter = new ErSalesInfoAdapter(getActivity(), pendingPays, performIdsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        erSalesInfoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(erSalesInfoAdapter);


    }

    private void initUI() {
        databaseHelper = new DatabaseHelper(getActivity());
        commonTaskManager = new CommonTaskManager(getActivity());
        commonCodeManager = new CommonCodeManager(getActivity());

        View allValues = view.findViewById(R.id.layout_collection_tally_info);

        textviewMeterSaleRs = allValues.findViewById(R.id.textview_meter_sale_rs);
        textViewAmountTally = allValues.findViewById(R.id.textview_amount_tally);
        textViewDifference = allValues.findViewById(R.id.textview_difference);


        //trying to set credit value
        textViewTotalCreditAmtFinal = allValues.findViewById(R.id.textview_credit_amt_final);

       /* String totalAmount = commonCodeManager.getTotalCreditAmountFinal(Objects.requireNonNull(getActivity()));
        textViewTotalCreditAmtFinal.setText(totalAmount);*/


        TextView textviewDate = view.findViewById(R.id.textview_date_er_info);
        textviewDate.setText(commonTaskManager.getCurrentDateTime());
        recyclerView = view.findViewById(R.id.recyclerview_pending_payments);

        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(Objects.requireNonNull(getActivity()));
        textviewMeterSaleRs.setText(recoveryAmount);

        layoutPArent = view.findViewById(R.id.layout_parent_info);

        String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
        Log.d(TAG, "initUI:tallyAmountDb :  " + tallyAmountDb);
        if (tallyAmountDb != null && tallyAmountDb.isEmpty()) {
            textViewAmountTally.setText("0");

        } else {
            textViewAmountTally.setText(tallyAmountDb);

        }

    }

    private void apiCallForGetFuelStaffPerformForAllPendingPayments(final Context context,
                                                                    final String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


/*
        final List<OperatorDailyWorkListModel> pendingPays = new ArrayList<>();
*/
        final List<OperatorInfoFragModel> pendingPays = new ArrayList<>();

        final List<String> performIds = new ArrayList<>();
        final List<OperatorDailyWorkListModel> infoCardDetails = new ArrayList<>();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformForAllPendingPayments(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOperatorShiftDetails info :  " + jsonData);
                try {
                    JSONObject mainObject
                            = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String closeMetDateTime = duObject.getString("closeMetDateTime");
                                String productCategory = duObject.getString("productCategory");
                                String unitSales = duObject.getString("unitSales");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                                String recoveryAmount = duObject.getString("recoveryAmount");
                                String duNo = duObject.getString("duNo");
                                String nozNo = duObject.getString("nozNo");
                                String pumpNozzle = duNo + " - " + nozNo;


                                checkForMeter = commonCodeManager.getRecoveryAmountInSales(context);
                                if (checkForMeter.isEmpty()) {
                                    Double amount = Double.parseDouble(recoveryAmount);

                                    recoveryAmt = recoveryAmt + amount;
                                    Log.d(TAG, "onResponse:recoveryAmt :  " + recoveryAmt);
                                    Log.d(TAG, "onResponse: is empty");

                                }


                               /* OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(closeMetDateTime,
                                                productCategory, unitSales, recoveryAmount);*/

                                OperatorInfoFragModel operatorInfoFragModel = new OperatorInfoFragModel(closeMetDateTime,
                                        productCategory, unitSales, recoveryAmount, pumpNozzle);
                                pendingPays.add(operatorInfoFragModel);

                                performIds.add(fuelStaffPerformId);

                                //save first time info card details to local db

                                OperatorDailyWorkListModel forinfocards =
                                        new OperatorDailyWorkListModel(productCategory, unitSales, recoveryAmount);
                                infoCardDetails.add(forinfocards);
                            }
                            databaseHelper.saveInfoCardDetails(infoCardDetails);

                            commonCodeManager.saveRecoveryAmountInSales(context, String.valueOf(recoveryAmt));


                         /*
                         ********************************************
                         working code for update diff header
                         String payedAmt = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(Objects.requireNonNull(getActivity())));
                            Double difference = 0.0;
                            if (payedAmt != null && !payedAmt.equals("null") && !payedAmt.isEmpty()) {
                                difference = recoveryAmt - Double.parseDouble(payedAmt);

                            } else {
                                difference = recoveryAmt;
                                payedAmt = "0.0";
                            }
                            String[] details = {payedAmt, String.valueOf(recoveryAmt), String.valueOf(difference)};
                            try {
                                ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
                            } catch (ClassCastException cce) {
                                cce.printStackTrace();

                            }
                            */

                            fillPendingInfoCards(pendingPays, performIds, recoveryAmt);

                        } else {
                            showMessageThatNoPendingDues();
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

    private void showMessageThatNoPendingDues() {

      /*  Snackbar snackbar = Snackbar
                .make(layoutPArent, "You don't have any pending due's!", Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
        params1.gravity = Gravity.TOP;
        view.setLayoutParams(params1);
        view.setBackgroundColor(getResources().getColor(R.color.colorCommonGreen));
        snackbar.show();*/
        Toast.makeText(getActivity(), "You don't have any pending due's!", Toast.LENGTH_LONG).show();
    }

    private void fillPendingInfoCards(List<OperatorInfoFragModel> pendingPays,
                                      List<String> performIdsList, Double recoveryAmt) {

        //save activity id
        String activityId = "";

        if (performIdsList.size() != 0) {
            String[] allInfraId = new String[performIdsList.size()];

            for (int i = 0; i < performIdsList.size(); i++) {
                allInfraId[i] = performIdsList.get(i);
                activityId = activityId + performIdsList.get(i) + "_";
                Log.d(TAG, "onClick: activity id on info: " + activityId);

            }
        }
        int position = activityId.lastIndexOf("_");
        if (position != -1)
            activityId = activityId.substring(0, position);
        Log.d(TAG, "onClick: activityID on info : " + activityId);

        commonCodeManager.saveActivityId(Objects.requireNonNull(getActivity()), activityId);


        fillTheInfoCards(pendingPays);


        // textviewMeterSaleRs.setText(String.valueOf(recoveryAmt));
        /*String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(getActivity());
        textviewMeterSaleRs.setText(recoveryAmount);

        String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(getActivity()));
        textViewAmountTally.setText(tallyAmountDb);

        Double difference = recoveryAmt - Double.parseDouble(tallyAmountDb);

        textViewDifference.setText(String.valueOf(difference));
        Log.d(TAG, "showTransDetailsToList:textViewDifference :  " + difference);
*/


        //activity id for credit
        String activityIdForCredit = "";

        if (performIdsList.size() != 0) {
            String[] allInfraIdCredit = new String[performIdsList.size()];

            for (int i = 0; i < performIdsList.size(); i++) {
                allInfraIdCredit[i] = performIdsList.get(i);
                activityIdForCredit = activityIdForCredit + performIdsList.get(i) + ",";
                Log.d(TAG, "onClick: activity id on info : " + activityIdForCredit);

            }
        }
        int position1 = activityIdForCredit.lastIndexOf(",");
        if (position1 != -1)
            activityIdForCredit = activityIdForCredit.substring(0, position1);
        Log.d(TAG, "onClick: activityID on info for credit : " + activityIdForCredit);

        commonCodeManager.saveActivityIdForCredit(getActivity(), activityIdForCredit);


        if (activityIdForCredit != null && !activityIdForCredit.isEmpty()) {
            apiCallForGetTotalCreditAmountShiftWise(getActivity(), activityIdForCredit);

        }
    }

    private void apiCallForGetTotalCreditAmountShiftWise(final Context context, String activityIdForCredit) {
        final String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForGetTotalCreditAmountShiftWise: dealerId :" + details[1]);
        String[] dates = commonTaskManager.getBeforeAfterTodaysDates();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getTotalCreditAmountShiftWise(details[0], activityIdForCredit);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetTotalCreditAmountShiftWise :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject detailsObj = dataArray.getJSONObject(i);
                            String totalCreditAmount = detailsObj.getString("totalCreditAmount");

                            showAllHeaderDetails(totalCreditAmount);
                        }

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }

        });
    }


    private void showAllHeaderDetails(String creditAmount) {
        Log.d(TAG, "showCreditAmount: totalCreditAmount : " + creditAmount);
        String totalCreditAmount;
        if (creditAmount != null && !creditAmount.equals("null") && !creditAmount.isEmpty() && !creditAmount.equals("undefined")) {
            //there is amount for credit
            totalCreditAmount = String.format("%.2f", Double.parseDouble(creditAmount));

        } else {
            totalCreditAmount = "0";

        }
        commonCodeManager.saveTotalCreditAmountFinal(Objects.requireNonNull(getActivity()), totalCreditAmount);


        String payedAmt = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(Objects.requireNonNull(getActivity())));

        Log.d(TAG, "onResponse: finalCreditAmt on info pending :" + totalCreditAmount);
        Log.d(TAG, "onResponse: payedAmt on info pending : " + payedAmt);
        Log.d(TAG, "onResponse: recoveryAmt on info pending : " + recoveryAmt);

        double difference = 0;
        double payedAmtFinal;
        if (payedAmt != null && !payedAmt.equals("null") && !payedAmt.isEmpty() && !payedAmt.equals("0")) {

            //if some amount paid
            payedAmtFinal = Double.parseDouble(payedAmt);
            Log.d(TAG, "onResponse: : if some amount paid payedAmtFinal : " + payedAmtFinal);


            difference = recoveryAmt - payedAmtFinal;
            Log.d(TAG, "onResponse: difference if paid :: " + difference);

        } else {
            //nothing paid
            // Difference =  meter sale rs - credit amount
                              /*  if (finalCreditAmt != null && !finalCreditAmt.equals("null") && !finalCreditAmt.isEmpty() && !finalCreditAmt.equals("new")) {
                                    difference = recoveryAmt - Double.parseDouble(finalCreditAmt);
                                    Log.d(TAG, "onResponse: difference if not paid :: " + difference);

                                }*/
            difference = recoveryAmt - Double.parseDouble(totalCreditAmount);
            Log.d(TAG, "onResponse: difference if not paid :: " + difference);


            payedAmt = "0";


        }

        String[] details = {payedAmt, String.valueOf(recoveryAmt), String.valueOf(difference)};
        try {
            ((UpdateSalesDeatail) activity).updateSalesDeatails(details);
        } catch (ClassCastException cce) {
            cce.printStackTrace();

        }
    }

    private void fillTheInfoCards(List<OperatorInfoFragModel> pendingPays) {
        List<String> ids = new ArrayList<>();

        ErSalesInfoAdapter erSalesInfoAdapter = new ErSalesInfoAdapter(getActivity(), pendingPays, ids);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        erSalesInfoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(erSalesInfoAdapter);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;

    }
}

