package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.ErSalesInfoAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ErInfoActivity extends BaseCompatActivity {
    private static final String TAG = "ErInfoActivity";
    View view;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerView;
    String[] details;
    CommonCodeManager commonCodeManager;
    double recoveryAmt = 0;
    TextView textviewMeterSaleRs, textViewAmountTally, textViewDifference;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_er_info);

        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(ErInfoActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(ErInfoActivity.this, ErInfoActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(ErInfoActivity.this);
            textviewMeterSaleRs.setText(recoveryAmount);

            String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(ErInfoActivity.this));
            if (tallyAmountDb != null && tallyAmountDb.isEmpty()) {
                textViewAmountTally.setText("0");

            } else {
                textViewAmountTally.setText(tallyAmountDb);

            }


            details = commonCodeManager.getDealerDetails(ErInfoActivity.this);

            String[] dailySales = commonCodeManager.getDailySalesCardEssentials(ErInfoActivity.this);
            Log.d(TAG, "onResume: dailySales[0] : " + dailySales[0]);
            if (dailySales[0] != null && !dailySales[0].equals("null") && !dailySales[0].isEmpty() && dailySales[0].equals("addnew")) {
                Log.d(TAG, "onResume: for add new ");
                apiCallForGetFuelStaffPerformForAllPendingPayments(ErInfoActivity.this, details[0]);

            } else {
                Log.d(TAG, "onResume: existing");
                apiCallForGeetingDoneTransDetails(ErInfoActivity.this, details[0], dailySales[1]);
            }


       /* if (tallyAmountDb != null && recoveryAmount != null) {
            Double difference = Double.parseDouble(recoveryAmount) - Double.parseDouble(tallyAmountDb);
            textViewDifference.setText(String.valueOf(difference));
        }
*/

        }


    }

    private void apiCallForGeetingDoneTransDetails(final Context context,
                                                   String fuelDealerStaffId,
                                                   String dailySale) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final List<OperatorDailyWorkListModel> pendingPays = new ArrayList<>();
        final List<String> performIds = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformByRecoveryStatusSUBMIT
                (fuelDealerStaffId, commonCodeManager.getBatchIdForErReport(context));
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
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);
                            String closeMetDateTime = duObject.getString("closeMetDateTime");
                            //String productCategory = duObject.getString("duNo");
                            String unitSales = duObject.getString("unitSales");
                            String recoveryAmount = duObject.getString("recoveryAmount");
                            String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");

                            OperatorDailyWorkListModel dailyWorkListModel =
                                    new OperatorDailyWorkListModel(closeMetDateTime, "", unitSales, recoveryAmount);
                            pendingPays.add(dailyWorkListModel);

                            performIds.add(fuelStaffPerformId);
                        }

                      //  fillDoneCardDetails(pendingPays, performIds);

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

   /* private void fillDoneCardDetails(List<OperatorDailyWorkListModel> pendingPays, List<String> performIds) {

        ErSalesInfoAdapter erSalesInfoAdapter = new ErSalesInfoAdapter(ErInfoActivity.this, pendingPays);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ErInfoActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        erSalesInfoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(erSalesInfoAdapter);

    }*/

    private void initUI() {
        databaseHelper = new DatabaseHelper(ErInfoActivity.this);
        commonTaskManager = new CommonTaskManager(ErInfoActivity.this);
        commonCodeManager = new CommonCodeManager(ErInfoActivity.this);

        View allValues = view.findViewById(R.id.layout_collection_tally_info);

        textviewMeterSaleRs = allValues.findViewById(R.id.textview_meter_sale_rs);
        textViewAmountTally = allValues.findViewById(R.id.textview_amount_tally);
        textViewDifference = allValues.findViewById(R.id.textview_difference);

        TextView textviewDate = view.findViewById(R.id.textview_date_er_info);
        textviewDate.setText(commonTaskManager.getCurrentDateTime());
        recyclerView = view.findViewById(R.id.recyclerview_pending_payments);


    }

    private void apiCallForGetFuelStaffPerformForAllPendingPayments(final Context context, final String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final List<OperatorDailyWorkListModel> pendingPays = new ArrayList<>();
        final List<String> performIds = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformForAllPendingPayments(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOperatorShiftDetails :  " + jsonData);
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
                                String productCategory = duObject.getString("duNo");
                                String unitSales = duObject.getString("unitSales");
                                String recoveryAmount = duObject.getString("recoveryAmount");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");


                                String checkForMeter = commonCodeManager.getRecoveryAmountInSales(context);
                                double recoveryAmt = 0;
                                if (checkForMeter.isEmpty()) {
                                    double amount = Double.parseDouble(recoveryAmount);

                                    recoveryAmt = recoveryAmt + amount;
                                    Log.d(TAG, "onResponse:recoveryAmt :  " + recoveryAmt);
                                    Log.d(TAG, "onResponse: is empty");

                                }


                                OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(closeMetDateTime,
                                                productCategory, unitSales, recoveryAmount);
                                pendingPays.add(dailyWorkListModel);

                                performIds.add(fuelStaffPerformId);
                            }
                            commonCodeManager.saveRecoveryAmountInSales(context, String.valueOf(recoveryAmt));


                            fillPendingInfoCards(pendingPays, performIds, recoveryAmt);

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

    private void fillPendingInfoCards(List<OperatorDailyWorkListModel> pendingPays,
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

        commonCodeManager.saveActivityId(ErInfoActivity.this, activityId);

    /*    ErSalesInfoAdapter erSalesInfoAdapter = new ErSalesInfoAdapter(ErInfoActivity.this, pendingPays);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ErInfoActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        erSalesInfoAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(erSalesInfoAdapter);
*/
        // textviewMeterSaleRs.setText(String.valueOf(recoveryAmt));
        String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(ErInfoActivity.this);
        textviewMeterSaleRs.setText(recoveryAmount);

        String tallyAmountDb = databaseHelper.getPaymentDetails(commonCodeManager.getBatchIdForErReport(ErInfoActivity.this));
        textViewAmountTally.setText(tallyAmountDb);

        Double difference = recoveryAmt - Double.parseDouble(tallyAmountDb);

        textViewDifference.setText(String.valueOf(difference));
        Log.d(TAG, "showTransDetailsToList:textViewDifference :  " + difference);
    }
}