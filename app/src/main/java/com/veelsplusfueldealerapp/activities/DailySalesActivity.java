package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.DailySalesAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.DailySalesInfoModel;
import com.veelsplusfueldealerapp.models.InventoryDetailsListModel;
import com.veelsplusfueldealerapp.models.OperatorDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailySalesActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "DailySalesActivity";
    Chip chipSelectProduct;
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    RecyclerView recyclerDailySales;
    DatabaseHelper databaseHelper;
    ImageView imageViewNoData;
    private FloatingActionButton fabAddErDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_sales);
        initUI();
    }

    private void initUI() {

        commonTaskManager = new CommonTaskManager(DailySalesActivity.this);
        commonCodeManager = new CommonCodeManager(DailySalesActivity.this);
        databaseHelper = new DatabaseHelper(DailySalesActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_daily_sales);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Product Sales");


        chipSelectProduct = toolbar.findViewById(R.id.chip_select_product);
        chipSelectProduct.setVisibility(View.INVISIBLE);
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);


        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);

        fabAddErDetails = findViewById(R.id.fab_add_er_details);
        imageViewNoData = findViewById(R.id.imageview_no_data);

        fabAddErDetails.setOnClickListener(this);
        chipSelectProduct.setOnClickListener(this);

        recyclerDailySales = findViewById(R.id.recyclerview_daily_sales_list);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageview_back_arrow:
                Intent intent1 = new Intent(DailySalesActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;

            case R.id.chip_select_product:
                selectProductType();
                break;

            case R.id.fab_add_er_details:
                databaseHelper.deleteDailySales();
                databaseHelper.deleteSalesInfoCardTable();
                // commonCodeManager.saveConfirmRecoveryFlag(DailySalesActivity.this, false);
                commonCodeManager.saveBatchIdForErReport(DailySalesActivity.this, commonTaskManager.getBatchIdDetails());
                commonCodeManager.saveDailySalesCardEssentials(DailySalesActivity.this, "addnew", "");
                //save is exist as false for new
                //local db handling for info cards
                commonCodeManager.saveIsGiveCallForAPIInfoTabDailySales(DailySalesActivity.this, "apicall");
                commonCodeManager.saveIsDigitalSubmitTransClicked(DailySalesActivity.this, "");
                commonCodeManager.saveTotalAmtDigitalForDoneCardClickView(DailySalesActivity.this, "", "", "");
                commonCodeManager.saveDailySalesCardDetailsForView(DailySalesActivity.this, "new");

                commonCodeManager.saveTotalCreditAmountFinal(DailySalesActivity.this, "new");
                commonCodeManager.saveTotalDueInSales(DailySalesActivity.this, "nothing");

                getSharedPreferences(Constants.RECOVERY, 0).edit().clear().apply();
                getSharedPreferences(Constants.CASH, 0).edit().clear().apply();
                getSharedPreferences(Constants.DIGITAL, 0).edit().clear().apply();
                getSharedPreferences(Constants.RECOVERY_AMOUNT, 0).edit().clear().apply();
                getSharedPreferences(Constants.DONE, 0).edit().clear().apply();

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(DailySalesActivity.this, EarningsReportActivity.class);
                        startActivity(intent);
                    }
                }, 500);

                break;

        }
    }

    private void selectProductType() {
        final String[] listItems = {"None", "Petrol", "Diesel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(DailySalesActivity.this);
        builder.setTitle("Choose item");

        final int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    chipSelectProduct.setText(listItems[which]);
                    chipSelectProduct.setTypeface(chipSelectProduct.getTypeface(), Typeface.BOLD);
                    commonTaskManager.sleepProcess(dialog);
                } else {
                    chipSelectProduct.setText("Select Product");
                    commonTaskManager.sleepProcess(dialog);

                }


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();


        boolean result = commonTaskManager.compareDates(DailySalesActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(DailySalesActivity.this, DailySalesActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {


            apiCallForGettingAllDailySalesCards(DailySalesActivity.this);
        }

    }

    private void apiCallForGettingAllDailySalesCards(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        imageViewNoData.setVisibility(View.GONE);

        final List<DailySalesInfoModel> salesList = new ArrayList<>();

        String[] dealerDetails = commonCodeManager.getDealerDetails(context);
        Log.d(TAG, "apiCallForGettingAllDailySalesCards:dealerstaffid :  " + dealerDetails[0]);
        Log.d(TAG, "apiCallForGettingAllDailySalesCards: batchid  : " + commonCodeManager.getBatchIdForErReport(context));


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getDailySalesTransactionDetailsByStaffId
                (dealerDetails[0]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGettingAllDailySalesCards :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        Log.d(TAG, "onResponse: data array length : " + dataArray.length());

                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String transacDate = duObject.getString("transacDate");
                                String accountTransacLogId = duObject.getString("accountTransacLogId");
                                String batchId = duObject.getString("batchId");
                                String duNo = duObject.getString("duNo");
                                String nozNo = duObject.getString("nozNo");
                                String unitSales = duObject.getString("unitSales");
                                String productCategory = duObject.getString("productCategory");
                                String productName = duObject.getString("productName");
                                //String recoveredAmtByApp = duObject.getString("recoveredAmtByApp");

                                String product = productCategory + "-" + productName;
                                String pumpNozzle = duNo + nozNo;

                                DailySalesInfoModel dm = new DailySalesInfoModel
                                        (transacDate, product, unitSales, pumpNozzle,
                                                transacDate, accountTransacLogId, batchId,
                                                "");

                                salesList.add(dm);
                            }

                        } else {
                            //problem in it
                            imageViewNoData.setVisibility(View.VISIBLE);
                        }
                        fillDailySalesCards(salesList);

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

    private void fillDailySalesCards(List<DailySalesInfoModel> salesList) {

        DailySalesAdapter dailySalesAdapter =
                new DailySalesAdapter(DailySalesActivity.this, salesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerDailySales.setLayoutManager(mLayoutManager);
        recyclerDailySales.setItemAnimator(new DefaultItemAnimator());
        dailySalesAdapter.notifyDataSetChanged();
        recyclerDailySales.setAdapter(dailySalesAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseHelper.deleteDailySales();

        Intent intent = new Intent(DailySalesActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}