package com.veelsplusfueldealerapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.CreditListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.ArraivalVehiclesListModel;
import com.veelsplusfueldealerapp.models.FleetListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ErCreditFragment extends Fragment {
    private static final String TAG = "ErCreditFragment";
    View view;
    RecyclerView recyclerViewCredit;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_credit_er_frag, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        recyclerViewCredit = view.findViewById(R.id.recyclerview_credit);
        commonCodeManager = new CommonCodeManager(getActivity());
        commonTaskManager = new CommonTaskManager(getActivity());

        String activityIdForCredit = commonCodeManager.getActivityIdForCredit(Objects.requireNonNull(getActivity()));
        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer:activityIdsForCredit :  " + activityIdForCredit);

        if (activityIdForCredit != null && !activityIdForCredit.isEmpty()) {
            apiCallForGetAllFuelCreditRequestDealer(getActivity(), activityIdForCredit);

        }

    }

    private void apiCallForGetAllFuelCreditRequestDealer(final Context context, String activityId) {
        final String[] details = commonCodeManager.getDealerDetails(context);


        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        String[] dates = commonTaskManager.getBeforeAfterTodaysDates();

        commonTaskManager.getCurrentDateNew();

        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: " + details[0]);
        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: " + dates[0]);
        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: " + dates[1]);


        final List<FleetListModel> fleetList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getCreditAmtListByfuelDealerStaffId(details[0], activityId);
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
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject detailsObj = dataArray.getJSONObject(i);
                            // String custName = detailsObj.getString("companyName");
                            String estimatedRefuelDate = detailsObj.getString("transDateTime");
                            //String registrationNumber = detailsObj.getString("registrationNumber");
                            //String refuelForDriver = detailsObj.getString("refuelForDriver");
                            String fuelCreditId = detailsObj.getString("fuelCreditId");
                            // String fleetNoFleetStatus = detailsObj.getString("fleetNoFleetStatus");
                            String firstName = detailsObj.getString("firstName");
                            String lastName = detailsObj.getString("lastName");
                            String transactionStatus = detailsObj.getString("transactionStatus");
                            String vehicleNumber = detailsObj.getString("vehicleNumber");
                            String creditAmount = detailsObj.getString("creditAmount");


                            String productCategory = detailsObj.getString("productCategory");
                            String productName = detailsObj.getString("productName");
                            String actualCreditQuantity = detailsObj.getString("actualCreditQuantity");
                            String finalProductName = productCategory + " - " + productName;

                            if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                vehicleNumber = "Not Provided";
                            }

                            String driverName = firstName + " " + lastName;
                            String dateOnly = "";

                            if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty()) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly = datetime[0];

                            } else {
                                dateOnly = "";
                            }
                            if (!transactionStatus.equals("null") && !transactionStatus.isEmpty() && transactionStatus.equals("COMPLETE")) {
                                FleetListModel fleetListModel = new FleetListModel("",
                                        dateOnly, vehicleNumber,
                                        driverName, fuelCreditId, transactionStatus, creditAmount, actualCreditQuantity, finalProductName);
                                fleetList.add(fleetListModel);
                            }
                        }

                        fillFleetListCards(fleetList);

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
        CreditListAdapter creditListAdapter = new CreditListAdapter(getActivity(), fleetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCredit.setLayoutManager(mLayoutManager);
        recyclerViewCredit.setItemAnimator(new DefaultItemAnimator());
        creditListAdapter.notifyDataSetChanged();
        recyclerViewCredit.setAdapter(creditListAdapter);
    }
}
