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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.AllOperatorsListAdapter;
import com.veelsplusfueldealerapp.adapters.RecipeListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.Recipe;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShimmerEffectActivity extends AppCompatActivity {
    private static final String TAG = "ShimmerEffectActivity";
    CommonCodeManager commonCodeManager;
    private ShimmerFrameLayout mShimmerViewContainer;
    RecyclerView recyclerViewOPList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shimmer_effect);

        commonCodeManager = new CommonCodeManager(ShimmerEffectActivity.this);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        recyclerViewOPList = findViewById(R.id.recycler_view);
        // making http call and fetching menu json
        apiCallForGetOPPerformList(ShimmerEffectActivity.this);
    }

    private void apiCallForGetOPPerformList(final Context context) {


        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        final List<OperatorDailyWorkListModel> dailyWorkList = new ArrayList<>();
        Log.d(TAG, "apiCallForGetOPPerformList: dealer id : " + dealerDetails[1]);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelStaffPerformByDealerId(dealerDetails[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetOPPerformList :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
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
                            OperatorDailyWorkListModel dailyWorkListModel =
                                    new OperatorDailyWorkListModel(openMetDateTime,
                                            pumpNozzle, performStatus, recoveryStatus,
                                            fuelStaffPerformId, OPName);
                            dailyWorkList.add(dailyWorkListModel);
                        }

                        fillOPDetailsListCards(dailyWorkList);

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
    private void fillOPDetailsListCards(List<OperatorDailyWorkListModel> dailyWorkList) {
        AllOperatorsListAdapter allOperatorsListAdapter = new AllOperatorsListAdapter(ShimmerEffectActivity.this, dailyWorkList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewOPList.setLayoutManager(mLayoutManager);
        recyclerViewOPList.setItemAnimator(new DefaultItemAnimator());
        allOperatorsListAdapter.notifyDataSetChanged();
        recyclerViewOPList.setAdapter(allOperatorsListAdapter);
        // stop animating Shimmer and hide the layout
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}