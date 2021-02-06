package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.FleetArrivalListAdapter;
import com.veelsplusfueldealerapp.adapters.TabPagerForArraivalListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.FleetListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewArrivalListActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewArrivalListActivity";
    CommonTaskManager commonTaskManager;
    ImageView imageViewQr, imageViewRefresh;
    CommonCodeManager commonCodeManager;
    RecyclerView recyclerArraivals;
    ImageView imageViewNoData;
    FloatingActionButton fabNewRequest;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_arrival_list);
        initUI();
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(NewArrivalListActivity.this);
        commonCodeManager = new CommonCodeManager(NewArrivalListActivity.this);

        recyclerArraivals = findViewById(R.id.recyclerview_arrivals_main);


        View toolbar = findViewById(R.id.layout_toolbar_arraivalslist_new);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Credit Requests");

        imageViewQr = toolbar.findViewById(R.id.imageview_qr);
        imageViewQr.setImageDrawable(getResources().getDrawable(R.drawable.qr_white));
        imageViewQr.setVisibility(View.VISIBLE);

        ImageView imageViewSearch = toolbar.findViewById(R.id.imageview_search_refuel);
        imageViewSearch.setVisibility(View.VISIBLE);
        //  imageViewSearch.setOnClickListener(this);



      /*  imageViewAddMore = toolbar.findViewById(R.id.imageview_add_more);
        imageViewAddMore.setVisibility(View.GONE);
        imageViewAddMore.setOnClickListener(this);
*/
        fabNewRequest = findViewById(R.id.fab_new_req);

        fabNewRequest.setOnClickListener(this);

        imageViewRefresh = toolbar.findViewById(R.id.imageview_refresh);
        imageViewRefresh.setVisibility(View.VISIBLE);

        ImageView imageViewVehicle = findViewById(R.id.iv_search_vehicle_arr);
        ImageView imageViewPerson = findViewById(R.id.iv_search_person_arr);

        imageViewNoData = findViewById(R.id.imageview_no_data);

        imageViewVehicle.setOnClickListener(this);
        imageViewPerson.setOnClickListener(this);



        /*ImageView imageViewSearch = toolbar.findViewById(R.id.imageview_search);
        imageViewSearch.setImageDrawable(getResources().getDrawable(R.drawable.search_white));
        imageViewSearch.setVisibility(View.VISIBLE);
*/
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        imageViewQr.setOnClickListener(this);
        imageViewRefresh.setOnClickListener(this);

        //apiCallForGetAllFuelCreditRequestDealer(NewArrivalListActivity.this);

        configureTabLayout();

    }

    private void configureTabLayout() {
        tabLayout =
                findViewById(R.id.tab_layout_arraival_list_new);

        tabLayout.addTab(tabLayout.newTab().setText("Corporate"));
        tabLayout.addTab(tabLayout.newTab().setText("Person"));


        viewPager =
                findViewById(R.id.pager_arraival_list_new);
        final PagerAdapter adapter = new TabPagerForArraivalListAdapter
                (getSupportFragmentManager(),
                        tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: tab selected pos : " + tab.getPosition());
                commonCodeManager.saveSeletctedTabForRefuel(NewArrivalListActivity.this, tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(NewArrivalListActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.imageview_qr:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setOrientationLocked(false);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
                break;
            case R.id.fab_new_req:

                String[] details = commonCodeManager.getEssentialsForDealer(NewArrivalListActivity.this);
                String designation = details[4].toLowerCase().trim();
                Log.d(TAG, "onClick: designation : "+designation);

                if (designation != null && !designation.isEmpty() && !designation.equals("null") &&  designation.equals("operator")) {

                    Intent intent1 = new Intent(NewArrivalListActivity.this, NewFuelCreditRequestActivity.class);
                    startActivity(intent1);
                } else {
                    Intent intent1 = new Intent(NewArrivalListActivity.this, CreateCreditReqManagerActivity.class);
                    startActivity(intent1);
                }
                break;

            case R.id.imageview_refresh:
                //refresh ;list back to normal

                commonTaskManager.hideKeyboard(NewArrivalListActivity.this);

                apiCallForGetAllFuelCreditRequestDealer(NewArrivalListActivity.this);

                break;

         /*   case R.id.imageview_search_refuel:

                break;
*/

        }
    }

    private void apiCallForSearchDriver(final Context context, final String phone1) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        imageViewNoData.setVisibility(View.INVISIBLE);

        final String[] dateOnly = {""};
        final List<FleetListModel> fleetList = new ArrayList<>();

        String[] details = commonCodeManager.getDealerDetails(context);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByPersonNumber(phone1, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSearchDriver credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");
                                Log.d(TAG, "onResponse:transactionStatus :  " + transactionStatus);
/*
                                if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                    String[] datetime = estimatedRefuelDate.split("T");
                                    dateOnly[0] = datetime[0];

                                } else {
                                    dateOnly[0] = "";
                                }*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }

                                FleetListModel fleetListModel = new FleetListModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);
                                fleetList.add(fleetListModel);


                            }

                        } else {
                            imageViewNoData.setVisibility(View.VISIBLE);
                            //commonTaskManager.showToast(context, "No fuel credit requests found for " + phone1);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No fuel credit requests found for " + phone1)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                        }
                        fillFleetListCards(fleetList);


                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });


    }

    private void apiCallForSerachByVehicleNumber(final Context context, final String vehicleNumber) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        imageViewNoData.setVisibility(View.INVISIBLE);

        final String[] dateOnly = {""};
        final List<FleetListModel> fleetList = new ArrayList<>();
        String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForSerachByVehicleNumber: vehicleNumber: " + vehicleNumber);

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getRequestListByVehicleNumber(vehicleNumber, details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForSerachByVehicleNumber credit :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {

                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {

                                JSONObject duObject = dataArray.getJSONObject(i);
                                String companyName = duObject.getString("companyName");
                                String estimatedRefuelDate = duObject.getString("estimatedRefuelDate");
                                String vehicleNumber = duObject.getString("vehicleNumber");
                                String phone1 = duObject.getString("phone1");
                                String fuelCreditId = duObject.getString("fuelCreditId");
                                String transactionStatus = duObject.getString("transactionStatus");


                          /*  if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly[0] = datetime[0];

                            } else {
                                dateOnly[0] = "";
                            }
*/
                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }
                                FleetListModel fleetListModel = new FleetListModel(companyName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);
                                fleetList.add(fleetListModel);


                            }
                        } else {
                            imageViewNoData.setVisibility(View.VISIBLE);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("No Data Found!");
                            builder.setMessage("No pending requests found against " + vehicleNumber)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.dismiss();
                                        }
                                    });

                            //Creating dialog box

                            final AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.show();
                          /*  String msg = "No pending requests found against " + vehicleNumber;
                            commonTaskManager.showToast(context, msg);*/
                        }
                        fillFleetListCards(fleetList);

                    } else {
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, "Vehicle Details Not Found");

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();

            } else {
                String resultData = "";
                try {
                    resultData = result.getContents();
                    Log.d(TAG, "onActivityResult: resultData :" + resultData);
                    //byte[] byteArray = Base64.decode(resultData, Base64.DEFAULT);
                    //String decodeQrData = new String(byteArray, "UTF-8");

                    JSONObject mainObj = new JSONObject(resultData);
                    String owner = mainObj.getString("Owner");
                    String fuelCreditId = mainObj.getString("FuelCreditId");
                    goToTheManualFormActivity(fuelCreditId);

                } catch (Exception e) {
                    Log.d(TAG, "onActivityResult: exception : " + e.getLocalizedMessage());
                    Toast.makeText(this, "Please scan correcet QR code.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {

            super.onActivityResult(requestCode, resultCode, data);

        }

    }

    private void goToTheManualFormActivity(String fuelCreditId) {
        commonCodeManager.saveFuelCreditId(NewArrivalListActivity.this, fuelCreditId);

        Intent intent = new Intent(NewArrivalListActivity.this, ManualEntryFormActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(NewArrivalListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(NewArrivalListActivity.this, NewArrivalListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewArrivalListActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void apiCallForGetAllFuelCreditRequestDealer(final Context context) {
        final String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: dealerId :" + details[1]);

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<FleetListModel> fleetList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelCreditRequestByfuelDealerId(details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllFuelCreditRequestDealer :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");

                        if (dataArray.length() > 0) {


                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject detailsObj = dataArray.getJSONObject(i);
                                String custName = detailsObj.getString("companyName");
                                String estimatedRefuelDate = detailsObj.getString("estimatedRefuelDate");
                                // String registrationNumber = detailsObj.getString("registrationNumber");
                                String refuelForDriver = detailsObj.getString("refuelForDriver");
                                String fuelCreditId = detailsObj.getString("fuelCreditId");
                                String fleetNoFleetStatus = detailsObj.getString("fleetNoFleetStatus").toLowerCase().trim();
                                String firstName = detailsObj.getString("firstName");
                                String lastName = detailsObj.getString("lastName");
                                String transactionStatus = detailsObj.getString("transactionStatus");
                                String phone1 = detailsObj.getString("phone1");
                                String vehicleNumber = detailsObj.getString("vehicleNumber");

                                if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                    vehicleNumber = "Not Provided";
                                }
                                Log.d(TAG, "onResponse:transactionStatus :  " + transactionStatus);

                                String driverName = firstName + " " + lastName;
                            /*String dateOnly = "";

                            if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly = datetime[0];

                            } else {
                                dateOnly = "";
                            }*/


                                FleetListModel fleetListModel = new FleetListModel(custName,
                                        estimatedRefuelDate, vehicleNumber,
                                        phone1, fuelCreditId, transactionStatus);
                                fleetList.add(fleetListModel);


                            }

                            fillFleetListCards(fleetList);
                        } else {

                            //no data
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

    private void fillFleetListCards(List<FleetListModel> fleetList) {
        FleetArrivalListAdapter fleetArrivalListAdapter = new FleetArrivalListAdapter(NewArrivalListActivity.this, fleetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(NewArrivalListActivity.this);
        recyclerArraivals.setLayoutManager(mLayoutManager);
        recyclerArraivals.setItemAnimator(new DefaultItemAnimator());
        fleetArrivalListAdapter.notifyDataSetChanged();
        recyclerArraivals.setAdapter(fleetArrivalListAdapter);
    }

    public void selectTab(int pageIndex) {
        tabLayout.setScrollPosition(pageIndex, 0f, true);
        viewPager.setCurrentItem(pageIndex);
    }

    public int getSelectedTab() {
        int selectedTab = tabLayout.getSelectedTabPosition();
        return selectedTab;
    }
}