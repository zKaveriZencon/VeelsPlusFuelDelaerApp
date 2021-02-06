package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.DailystockReadingAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.DailyStockMgModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyStockReadingListActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "DailyStockReadingListAc";
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerInventory;
    CommonCodeManager commonCodeManager;
    ImageView imageViewNodataFound;
    private FloatingActionButton fabUpdateInventory;
    private FloatingActionButton fabDailyInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_fuel_tank_inventory_list);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(DailyStockReadingListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(DailyStockReadingListActivity.this, DailyStockReadingListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String[] details = commonCodeManager.getDealerDetails(DailyStockReadingListActivity.this);

            apiCallForGetAllDailyIDetails(DailyStockReadingListActivity.this, details[0]);
        }

    }

    private void apiCallForGetAllDailyIDetails(final Context context, String fuelDealerStaffId) {

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<DailyStockMgModel> dailyList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getAllStockFuelInventoryById(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllDailyIDetails :  " + jsonData);
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
                                String openDipScaleReading = duObject.getString("openDipScaleReading");
                                String closeDipScaleReading = duObject.getString("closeDipScaleReading");
                                String tankNo = duObject.getString("tankNo");
                                String openDipReadDate = duObject.getString("openDipReadDate");
                                String fuelTankRefuelId = duObject.getString("fuelTankRefuelId");
                                String fuelInfraMapId = duObject.getString("fuelInfraMapId");
                                String stockCheckStatus = duObject.getString("stockCheckStatus");
                                String vehicleNo = duObject.getString("vehicleNo");
                                String closeDensity = duObject.getString("closeDensity");


                                Log.d(TAG, "onResponse: vehicle no : " + vehicleNo);
                                if (vehicleNo != null) {
                                    Log.d(TAG, "onResponse: if part");
                                    DailyStockMgModel dailyStockMgModel = new DailyStockMgModel
                                            (openDipScaleReading, closeDipScaleReading, tankNo, openDipReadDate,
                                                    fuelTankRefuelId, fuelInfraMapId, stockCheckStatus, closeDensity);
                                    dailyList.add(dailyStockMgModel);
                                } else {
                                    Log.d(TAG, "onResponse: else part");
                                }


                            }

                        } else {
                            //no data found
                            imageViewNodataFound.setVisibility(View.VISIBLE);
                            commonTaskManager.showToast(context, "No Data Found");
                        }
                        fillDailyStockDetails(dailyList);


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

    private void fillDailyStockDetails(List<DailyStockMgModel> daliyList) {
        DailystockReadingAdapter dailystockReadingAdapter = new DailystockReadingAdapter(DailyStockReadingListActivity.this, daliyList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerInventory.setLayoutManager(mLayoutManager);
        recyclerInventory.setItemAnimator(new DefaultItemAnimator());
        dailystockReadingAdapter.notifyDataSetChanged();
        recyclerInventory.setAdapter(dailystockReadingAdapter);
    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager(DailyStockReadingListActivity.this);
        View toolbar = findViewById(R.id.layout_toolbar_daily_inventory);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Daily Stock Details");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        commonTaskManager = new CommonTaskManager(DailyStockReadingListActivity.this);
        fabUpdateInventory = findViewById(R.id.fab_daily_inventory);

        imageViewNodataFound = findViewById(R.id.imageview_no_data);


        fabUpdateInventory.setOnClickListener(this);

        recyclerInventory = findViewById(R.id.recyclerview_daily_inventory_list);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_daily_inventory:
                commonCodeManager.saveDailyStockOnCardClick(DailyStockReadingListActivity.this, "", "", "");

                Intent intent = new Intent(DailyStockReadingListActivity.this, DailyStockReadingActivity.class);
                startActivity(intent);
                break;
            case R.id.imageview_back_arrow:
                Intent intent1 = new Intent(DailyStockReadingListActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(DailyStockReadingListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}