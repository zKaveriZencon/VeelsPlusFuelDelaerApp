package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.EarningReportAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.managers.UpdateSalesDeatail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EarningsReportActivity extends BaseCompatActivity
        implements View.OnClickListener, UpdateSalesDeatail {
    private static final String TAG = "EarningsReportActivity";
    MaterialButton buttonUpdateStatus;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    FloatingActionButton fabConfirmTrans;
    TextView textviewMeterSaleRs, textViewAmountTally, textViewDifference,
            textViewTotalCreditAmt, labelTotalCredit, textViewTallyFormula, textViewTotalCreditAmtFinal;
    ImageView ivAmtTallyInfo, ivDifferenceInfo;
    //   TextView textViewSalesInLiters, textViewSalesAmtValue, textViewActualAmtValue;
    Double totalCreditAmount = 0.0;
    LinearLayout layoutTallyFormula;
    RelativeLayout layoutParent;

    //recoveredAmtByApp
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earnings_report);

        layoutParent = findViewById(R.id.layout_earning_parent);


        configureTabLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(EarningsReportActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(EarningsReportActivity.this, EarningsReportActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {

            String[] erDetails = commonCodeManager.getLocalErData(EarningsReportActivity.this);
            //apiCallForGetAllFuelCreditRequestDealer(EarningsReportActivity.this);

            String vieOnly = commonCodeManager.getDailySalesCardDetailsForView(EarningsReportActivity.this);
            Log.d(TAG, "Earning activity view check : " + vieOnly);
            String[] dealerDetails = commonCodeManager.getDealerDetails(EarningsReportActivity.this);
            String[] accountDetails = commonCodeManager.getDailySalesCardEssentials(EarningsReportActivity.this);


            if (vieOnly != null && !vieOnly.equals("null") && !vieOnly.isEmpty() && vieOnly.equals("viewonly")) {
                // for showing total amount
                //  apiCallForShowSalesHeaderDetails(EarningsReportActivity.this, "view");

                apiCallForGeetingDoneTransDetails(EarningsReportActivity.this, dealerDetails[0], accountDetails[1]);

            } else {
                //for fresh new entry
                Log.d(TAG, "onResume: in else new");

                apiCallForShowHeaderDetailsForNewEntry(EarningsReportActivity.this, dealerDetails[0]);
            }
        }

        /*textViewSalesInLiters.setText(erDetails[0]);
        textViewSalesAmtValue.setText(erDetails[1]);
        textViewActualAmtValue.setText(erDetails[2]);
*/
    }

    public void configureTabLayout() {
        buttonUpdateStatus = findViewById(R.id.update_status);
        commonCodeManager = new CommonCodeManager(EarningsReportActivity.this);
        commonTaskManager = new CommonTaskManager(EarningsReportActivity.this);
        fabConfirmTrans = findViewById(R.id.fab_confirm_trans);
/*  textViewSalesInLiters = findViewById(R.id.textview_sales_in_liters_value);
        textViewSalesAmtValue = findViewById(R.id.textview_sales_amount_value);
        textViewActualAmtValue = findViewById(R.id.textview_actual_amount_value);*/

        textViewTotalCreditAmtFinal = findViewById(R.id.textview_credit_amt_final);


        View toolbar = findViewById(R.id.layout_cust_toolbar_er);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText(getResources().getString(R.string.todays_sales));
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);
        fabConfirmTrans.setOnClickListener(this);
        buttonUpdateStatus.setOnClickListener(this);
        TabLayout tabLayout =
                findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Info"));
        tabLayout.addTab(tabLayout.newTab().setText("Cash"));
        tabLayout.addTab(tabLayout.newTab().setText("Digital"));
        tabLayout.addTab(tabLayout.newTab().setText("Credit"));

        final ViewPager viewPager =
                findViewById(R.id.pager_earn_report);
        final PagerAdapter adapter = new EarningReportAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

        textviewMeterSaleRs = findViewById(R.id.textview_meter_sale_rs);
        textViewAmountTally = findViewById(R.id.textview_amount_tally);
        textViewDifference = findViewById(R.id.textview_difference);
        textViewDifference = findViewById(R.id.textview_difference);

        ivAmtTallyInfo = findViewById(R.id.iv_amt_tally_info);
        ivDifferenceInfo = findViewById(R.id.iv_difference_info);

        ivAmtTallyInfo.setOnClickListener(this);
        ivDifferenceInfo.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(EarningsReportActivity.this, DailySalesActivity.class);
                startActivity(intent);
                break;
            case R.id.fab_confirm_trans:

                // updateRecoveryStatus("fab");
                break;
            case R.id.iv_amt_tally_info:
                //tally calculation formula

                showPopUpForDetailsForTallyInfo();
                break;
            case R.id.iv_difference_info:
                //means credit amount
                showPopUpForDetailsForDifference();

                //      showDifference();
                break;


        }
    }

    private void showDifference() {
        String meterSale = textviewMeterSaleRs.getText().toString();
        String amounttally = textviewMeterSaleRs.getText().toString();
        String creditAmt = textviewMeterSaleRs.getText().toString();


    }

    private void showPopUpForDetailsForTallyInfo() {
        Log.d(TAG, "showPopUpForDetailsForTallyInfo: ");
        //for formula
        AlertDialog.Builder builder = new AlertDialog.Builder(EarningsReportActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EarningsReportActivity.this).
                inflate(R.layout.layout_popup_totalsale, viewGroup,
                        false);
        builder.setView(dialogView);
        layoutTallyFormula = dialogView.findViewById(R.id.layout_tally_formula);
        textViewTotalCreditAmt = dialogView.findViewById(R.id.textview_total_credit_amt);
        labelTotalCredit = dialogView.findViewById(R.id.label_total_credit);
        textViewTotalCreditAmt.setVisibility(View.GONE);
        labelTotalCredit.setVisibility(View.GONE);

        //totalCreditAmount
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void showPopUpForDetailsForDifference() {
        AlertDialog.Builder builder = new AlertDialog.Builder(EarningsReportActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(EarningsReportActivity.this).
                inflate(R.layout.layout_popup_totalsale, viewGroup,
                        false);
        builder.setView(dialogView);
        layoutTallyFormula = dialogView.findViewById(R.id.layout_tally_formula);
        textViewTotalCreditAmt = dialogView.findViewById(R.id.textview_total_credit_amt);
        labelTotalCredit = dialogView.findViewById(R.id.label_total_credit);
        layoutTallyFormula.setVisibility(View.GONE);
        textViewTotalCreditAmt.setText(String.valueOf(totalCreditAmount));

        //totalCreditAmount
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();
    }

    private void updateRecoveryStatus(final String from) {
        boolean isRecovered = commonCodeManager.getConfirmRecoveryFlag(EarningsReportActivity.this);
        if (isRecovered) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm Transaction !");
            builder.setCancelable(false);

            //Setting message manually and performing action on button click
            builder.setMessage("You can't edit any details from app after 'Submit' transaction. Do you want to submit transaction ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //dialog.dismiss();
                            Log.d(TAG, "onClick: from value : " + from);
                            if (from.equals("onbackpress")) {
                                dialog.dismiss();
                                Log.d(TAG, "onClick: if part update api");
                                Intent intent = new Intent(EarningsReportActivity.this, DailySalesActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.show();

           /* alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {

                    apiCallForUpdateRecoveryStatus(EarningsReportActivity.this);
                }
            });*/
        }

        if (from.equals("fab") && !isRecovered) {
            Toast.makeText(this, "It Seems You Didn't Transacted Yet!", Toast.LENGTH_SHORT).show();
        }
    }

    private void apiCallForUpdateRecoveryStatus(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String[] details = commonCodeManager.getDealerDetails(context);

        final String[] message = new String[1];
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateRecoveryStatusForDailySales(details[0],
                commonCodeManager.getBatchIdForErReport(context));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateRecoveryStatus :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").toLowerCase().trim();
                    final String msg = mainObject.getString("msg");

                    if (status.equals("ok")) {
                        // commonCodeManager.saveConfirmRecoveryFlag(context, false);
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, msg);

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, msg);

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
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(EarningsReportActivity.this);

        builder.setTitle("Note !");
        builder.setMessage("Are you sure want to leave payment section ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DatabaseHelper databaseHelper = new DatabaseHelper(EarningsReportActivity.this);

                        databaseHelper.deleteDailySales();
                        databaseHelper.deleteSalesInfoCardTable();


                        Intent intent = new Intent(EarningsReportActivity.this, DailySalesActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        final AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();


    }

    /* @Override
     public void onBackPressed() {
         super.onBackPressed();

         commonCodeManager.saveDailySalesCardEssentials(EarningsReportActivity.this, "", "");


         //updateRecoveryStatus("onbackpress");

       *//*  AlertDialog.Builder builder = new AlertDialog.Builder(EarningsReportActivity.this);
        builder.setTitle("Note !");
        builder.setMessage("Are you sure want to leave payment section ?")

                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // dialog.dismiss();
                        commonCodeManager.saveDailySalesCardEssentials(EarningsReportActivity.this, "", "");

                        Intent intent = new Intent(EarningsReportActivity.this, DailySalesActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();*//*

    }
*/
    @Override
    public void updateSalesDeatails(String[] details) {
        Log.d(TAG, "updateSalesDeatails: details check : " + details[0]);
        textViewAmountTally.setText(details[0]);
        textviewMeterSaleRs.setText(details[1]);
        if (details[2] != null && !details[2].equals("null") && !details[2].isEmpty() && !details[2].equals("undefined")) {
            String difference = String.format("%.2f", Double.parseDouble(details[2]));
            textViewDifference.setText(difference);
        } else {
            // textViewDifference.setText("0");
        }


    }

    private void apiCallForGeetingDoneTransDetails(final Context context,
                                                   String fuelDealerStaffId,
                                                   String batchID) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<String> performIds = new ArrayList<>();

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
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                                performIds.add(fuelStaffPerformId);
                            }

                            showHEaderDetails(performIds);

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

    private void showHEaderDetails(List<String> performIdsList) {
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

        commonCodeManager.saveActivityIdForCredit(EarningsReportActivity.this, activityIdForCredit);


        if (activityIdForCredit != null && !activityIdForCredit.isEmpty()) {
            apiCallForGetTotalCreditAmountShiftWise(EarningsReportActivity.this, activityIdForCredit);

        }


     /*   String recoveryAmount = commonCodeManager.getRecoveryAmountInSales(EarningsReportActivity.this);
        Log.d(TAG, "showHEaderDetails: new total rs : "+recoveryAmount);
        textviewMeterSaleRs.setText(recoveryAmount);*/


    }

    private void apiCallForShowHeaderDetailsForNewEntry(final Context context,
                                                        final String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        final List<String> performIds = new ArrayList<>();

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


                               /* checkForMeter = commonCodeManager.getRecoveryAmountInSales(context);
                                if (checkForMeter.isEmpty()) {
                                    Double amount = Double.parseDouble(recoveryAmount);

                                    recoveryAmt = recoveryAmt + amount;
                                    Log.d(TAG, "onResponse:recoveryAmt :  " + recoveryAmt);
                                    Log.d(TAG, "onResponse: is empty");

                                }*/
                                performIds.add(fuelStaffPerformId);

                            }

                            // commented for prev check
                            //commonCodeManager.saveRecoveryAmountInSales(context, String.valueOf(recoveryAmt));


                            //save first time info card details to local db
                        }

                        showHEaderDetails(performIds);

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
                            showCreditAmount(totalCreditAmount);
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

    private void showCreditAmount(String creditAmount) {
        Log.d(TAG, "showCreditAmount: totalCreditAmount : " + creditAmount);
        if (creditAmount != null && !creditAmount.equals("null") && !creditAmount.isEmpty() && !creditAmount.equals("undefined")) {
            //there is amount for credit
            String difference = String.format("%.2f", Double.parseDouble(creditAmount));
            Log.d(TAG, "showCreditAmount: difference : " + difference);

            textViewTotalCreditAmtFinal.setText(difference);
        } else {
            textViewTotalCreditAmtFinal.setText("0");

        }
    }


 /*   private void apiCallForGetAllFuelCreditRequestDealer(final Context context) {
        final String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: dealerId :" + details[1]);
        String[] dates = commonTaskManager.getBeforeAfterTodaysDates();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getCreditAmtListByfuelDealerStaffId(details[0], dates[0], dates[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllFuelCreditRequestDealer :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject detailsObj = dataArray.getJSONObject(i);
                            String transactionStatus = detailsObj.getString("transactionStatus").toLowerCase().trim();
                            String creditAmount = detailsObj.getString("creditAmount");

                            if (transactionStatus.equals("complete")) {
                                if (!creditAmount.equals("null") && !creditAmount.isEmpty()) {
                                    totalCreditAmount = totalCreditAmount + Double.parseDouble(creditAmount);
                                    Log.d(TAG, "onResponse: totalCreditAmount : " + totalCreditAmount);
                                }
                            }

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

    }*/
}