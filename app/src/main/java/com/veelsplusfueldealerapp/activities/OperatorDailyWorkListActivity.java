package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.OperatorDailyWorkListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
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

public class OperatorDailyWorkListActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "DailyWorkListActivity";
    List<OperatorDailyWorkListModel> dailyWorkList;
    RecyclerView recyclerDailyWork;
    FloatingActionButton fabShiftStartEnd;
    ImageView imageViewNoData;
    private CommonTaskManager commonTaskManager;
    private CommonCodeManager commonCodeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work_list);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();


        boolean result = commonTaskManager.compareDates(OperatorDailyWorkListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(OperatorDailyWorkListActivity.this, OperatorDailyWorkListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }else {

            String[] details = commonCodeManager.getDealerDetails(OperatorDailyWorkListActivity.this);
            apiCallForGetOperatorShiftDetails(OperatorDailyWorkListActivity.this, details[0]);

        }


    }


    private void apiCallForGetOperatorShiftDetails(final Context context,
                                                   String fuelDealerStaffId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Log.d(TAG, "apiCallForGetOperatorShiftDetails:fuelDealerStaffId  :" + fuelDealerStaffId);
        dailyWorkList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getOpertaorShiftDetails(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOperatorShiftDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String msg = mainObject.getString("msg");

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


                                String pumpNozzle = pumpNo + nozzleNo;

                                String dateOnly = "";

                                if (openMetDateTime != null && !openMetDateTime.equals("null") && !openMetDateTime.isEmpty() && openMetDateTime.contains("T") ) {
                                    String[] datetime = openMetDateTime.split("T");
                                    dateOnly = datetime[0];

                                } else {
                                    dateOnly = "";
                                }

                                OperatorDailyWorkListModel dailyWorkListModel =
                                        new OperatorDailyWorkListModel(dateOnly,
                                                pumpNozzle, performStatus, recoveryStatus, fuelStaffPerformId);


                                dailyWorkList.add(dailyWorkListModel);
                            }
                            fillAllDailyWorkListCards(dailyWorkList);

                        } else {
                            //no data found
                            imageViewNoData.setVisibility(View.VISIBLE);
                            commonTaskManager.showToast(context, "No Data Found");
                        }

                    } else {
                        commonTaskManager.showToast(context, msg);
                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, context.getResources().getString(R.string.unable_to_connect));
                t.printStackTrace();
            }

        });
    }

    private void fillAllDailyWorkListCards(List<OperatorDailyWorkListModel> dailyWorkList) {
        OperatorDailyWorkListAdapter dailyWorkAdapter = new OperatorDailyWorkListAdapter(OperatorDailyWorkListActivity.this, dailyWorkList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerDailyWork.setLayoutManager(mLayoutManager);
        recyclerDailyWork.setItemAnimator(new DefaultItemAnimator());
        dailyWorkAdapter.notifyDataSetChanged();
        recyclerDailyWork.setAdapter(dailyWorkAdapter);

    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(OperatorDailyWorkListActivity.this);
        commonCodeManager = new CommonCodeManager(OperatorDailyWorkListActivity.this);
        View toolbar = findViewById(R.id.layout_toolbar_daily_work_o);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Daily Work List");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        imageViewNoData = findViewById(R.id.imageview_no_data);

        recyclerDailyWork = findViewById(R.id.recyclerview_daily_work_list);


        fabShiftStartEnd = findViewById(R.id.fab_shift_start_end);
        fabShiftStartEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operatorShiftStartEnd();
            }
        });


    }

    private void operatorShiftStartEnd() {

        List<String> paramsList = new ArrayList<>();
        paramsList.add("");
        paramsList.add("");
        paramsList.add("");
        commonCodeManager.saveParamsForManageOPShift(OperatorDailyWorkListActivity.this, paramsList);
        Intent intent = new Intent(OperatorDailyWorkListActivity.this, OperatorShiftStartEndActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(OperatorDailyWorkListActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(OperatorDailyWorkListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}