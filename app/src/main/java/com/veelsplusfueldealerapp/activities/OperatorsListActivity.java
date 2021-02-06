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


import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.AllOperatorsListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.OperatorDetailsModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OperatorsListActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "OperatorsListActivity";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerViewOPList;
    ImageView imageViewNoData;

    @Override
    protected void onResume() {
        super.onResume();
        boolean result = commonTaskManager.compareDates(OperatorsListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(OperatorsListActivity.this, OperatorsListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            apiCallForGetOPPerformList(OperatorsListActivity.this);
        }
    }


    private void apiCallForGetOPPerformList(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        final List<OperatorDailyWorkListModel> dailyWorkList = new ArrayList<>();
        Log.d(TAG, "apiCallForGetOPPerformList: dealer id : " + dealerDetails[1]);
        final String[] dateOnly = {""};

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformByDealerId(dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOPPerformList :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String openMetDateTime = duObject.getString("openMetDateTime");
                                String pumpNo = duObject.getString("duNo");
                                String nozzleNo = duObject.getString("nozNo");
                                String performStatus = duObject.getString("performStatus");
                                String recoveryStatus = duObject.getString("recoveryStatus");
                                String fuelStaffPerformId = duObject.getString("fuelStaffPerformId");
                                String firstName = duObject.getString("firstName");
                                String lastName = duObject.getString("lastName");

                                String OPName = firstName + " " + lastName;

                                String pumpNozzle = pumpNo + nozzleNo;


                                if (openMetDateTime != null && !openMetDateTime.equals("null") && !openMetDateTime.isEmpty() && openMetDateTime.contains("T")) {
                                    String[] datetime = openMetDateTime.split("T");
                                    dateOnly[0] = datetime[0];

                                } else {
                                    dateOnly[0] = "";
                                }


                                OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(dateOnly[0],
                                                pumpNozzle, performStatus, recoveryStatus,
                                                fuelStaffPerformId, OPName);
                                dailyWorkList.add(dailyWorkListModel);
                            }

                            fillOPDetailsListCards(dailyWorkList);
                        } else {
                            //no data found
                            imageViewNoData.setVisibility(View.VISIBLE);
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

    private void fillOPDetailsListCards(List<OperatorDailyWorkListModel> dailyWorkList) {
        AllOperatorsListAdapter allOperatorsListAdapter = new AllOperatorsListAdapter(OperatorsListActivity.this, dailyWorkList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewOPList.setLayoutManager(mLayoutManager);
        recyclerViewOPList.setItemAnimator(new DefaultItemAnimator());
        allOperatorsListAdapter.notifyDataSetChanged();
        recyclerViewOPList.setAdapter(allOperatorsListAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators_list);

        initUI();
    }

    private void initUI() {
        commonCodeManager = new CommonCodeManager(OperatorsListActivity.this);
        commonTaskManager = new CommonTaskManager(OperatorsListActivity.this);


        View toolbar = findViewById(R.id.layout_toolbar_operators_list);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Operators List");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        recyclerViewOPList = findViewById(R.id.recyclerview_operators_list);
        imageViewNoData = findViewById(R.id.imageview_no_data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(OperatorsListActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OperatorsListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}