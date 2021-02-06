package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.FuelPriceListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.FuelPriceModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelPriceListActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "FuelPriceListActivity";
    RecyclerView recyclerViewFuelPriceList;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    ImageView imageViewNodataFound;
    private FloatingActionButton fabSetPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_price_list);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(FuelPriceListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(FuelPriceListActivity.this, FuelPriceListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }else {
             /*
        String[] fuelPrice = commonCodeManager.getFuelPriceDetails(FuelPriceListActivity.this);
        if (fuelPrice[0].equals("setprice")) {
            Toast.makeText(this, fuelPrice[1], Toast.LENGTH_SHORT).show();
        }*/
            apiCallForGetFuelPrice(FuelPriceListActivity.this);
        }


    }

    private void apiCallForGetFuelPrice(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final String[] dateOnly = {""};


        String[] dealerDetails = commonCodeManager.getDealerDetails(context);

        final List<FuelPriceModel> priceList = new ArrayList<>();

        Log.d(TAG, "apiCallForGetFuelPrice: dealer id veelsid : " + dealerDetails[2]);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelPriceDetails(dealerDetails[2]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetFuelPrice :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);

                                String productCategory = duObject.getString("productCategory");
                                String productName = duObject.getString("productName");
                                String productSellingPrice = duObject.getString("productSellingPrice");
                                String productPriceDate = duObject.getString("productPriceDate");

                                if (productPriceDate != null && !productPriceDate.equals("null") && !productPriceDate.isEmpty()) {
                                    String[] datetime = productPriceDate.split("T");
                                    dateOnly[0] = datetime[0];

                                } else {
                                    dateOnly[0] = "";
                                }

                                FuelPriceModel fuelPriceModel = new FuelPriceModel
                                        (productSellingPrice, dateOnly[0], productCategory, productName);
                                priceList.add(fuelPriceModel);
                            }

                            fillPriceListCardsData(priceList);
                        } else {
                            //no data found
                            imageViewNodataFound.setVisibility(View.VISIBLE);
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

    private void fillPriceListCardsData(List<FuelPriceModel> priceList) {
        FuelPriceListAdapter fuelPriceListAdapter = new FuelPriceListAdapter(FuelPriceListActivity.this, priceList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewFuelPriceList.setLayoutManager(mLayoutManager);
        recyclerViewFuelPriceList.setItemAnimator(new DefaultItemAnimator());
        fuelPriceListAdapter.notifyDataSetChanged();
        recyclerViewFuelPriceList.setAdapter(fuelPriceListAdapter);
    }


    private void initUI() {
        recyclerViewFuelPriceList = findViewById(R.id.recyclerview_price_list);

        commonCodeManager = new CommonCodeManager(FuelPriceListActivity.this);
        commonTaskManager = new CommonTaskManager(FuelPriceListActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_fuelprice);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Fuel Price");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewNodataFound = findViewById(R.id.imageview_no_data);

        fabSetPrice = findViewById(R.id.fab_set_fuel_price);


        fabSetPrice.setOnClickListener(this);
        imageViewBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_set_fuel_price:
                Intent intent = new Intent(FuelPriceListActivity.this, SetFuelPriceActivity.class);
                startActivity(intent);
                break;

            case R.id.imageview_back_arrow:
                Intent intent1 = new Intent(FuelPriceListActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;

        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FuelPriceListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}