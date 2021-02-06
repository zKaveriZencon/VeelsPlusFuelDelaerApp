package com.veelsplusfueldealerapp.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.EarningsReportActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.http.RetryAndFollowUpInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftWiseCreditAmountManager {
    public static final String TAG = "ShiftWiseCreditAmountMa";
    Context context;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    String totalCreditAmount;

    public ShiftWiseCreditAmountManager(Context context) {
        this.context = context;
        commonCodeManager = new CommonCodeManager(context);
        commonTaskManager = new CommonTaskManager(context);
    }

    public String apiCallForShowHeaderDetailsForNewEntry(final Context context,
                                                         final String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        final List<String> performIdsList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformForAllPendingPayments(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForShowHeaderDetailsForNewEntry :  " + jsonData);
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
                                String recoveryAmount = duObject.getString("recoveryAmount");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");

                                performIdsList.add(fuelStaffPerformId);

                            }

                            Handler handler1 = new Handler(context.getMainLooper());
                            handler1.post(new Runnable() {
                                public void run() {

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

                                    commonCodeManager.saveActivityIdForCredit(context, activityIdForCredit);


                                    if (activityIdForCredit != null && !activityIdForCredit.isEmpty()) {
                                        // apiCallForGetTotalCreditAmountShiftWise(context, activityIdForCredit);


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
                                                            totalCreditAmount = detailsObj.getString("totalCreditAmount");
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
                                }
                            });
                        }


                    } else {
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

        return totalCreditAmount;
    }


}
