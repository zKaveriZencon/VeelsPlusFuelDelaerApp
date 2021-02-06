package com.veelsplusfueldealerapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.ArraivalListActivity;
import com.veelsplusfueldealerapp.activities.NewArrivalListActivity;
import com.veelsplusfueldealerapp.adapters.FleetArrivalListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.FleetListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FleetListFragment extends Fragment {
    private static final String TAG = "FleetListFragment";
    View view;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerArraivals;
    APIHandlerManager apiHandlerManager;
    RadioButton radioButtonType;

    @Override
    public void onResume() {
        super.onResume();

        apiCallForGetAllFuelCreditRequestDealer(getActivity());
    }

    private void apiCallForGetAllFuelCreditRequestDealer(final Context context) {
        final String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: dealerId :" + details[2]);

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
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject detailsObj = dataArray.getJSONObject(i);
                            String custName = detailsObj.getString("companyName");
                            String estimatedRefuelDate = detailsObj.getString("estimatedRefuelDate");
                            String vehicleNumber = detailsObj.getString("vehicleNumber");
                            String refuelForDriver = detailsObj.getString("refuelForDriver");
                            String fuelCreditId = detailsObj.getString("fuelCreditId");
                            String fleetNoFleetStatus = detailsObj.getString("fleetNoFleetStatus").toLowerCase().trim();
                            String firstName = detailsObj.getString("firstName");
                            String lastName = detailsObj.getString("lastName");
                            String transactionStatus = detailsObj.getString("transactionStatus");


                            String driverName = firstName + " " + lastName;
                            String dateOnly = "";

                            if (estimatedRefuelDate != null && !estimatedRefuelDate.equals("null") && !estimatedRefuelDate.isEmpty() && estimatedRefuelDate.contains("T")) {
                                String[] datetime = estimatedRefuelDate.split("T");
                                dateOnly = datetime[0];

                            } else {
                                dateOnly = estimatedRefuelDate;
                            }
                            if (vehicleNumber == null || vehicleNumber.equals("null") || vehicleNumber.isEmpty() || vehicleNumber.equals("undefined")) {
                                vehicleNumber = "Not Provided";
                            }

                            FleetListModel fleetListModel = new FleetListModel(custName,
                                    dateOnly, vehicleNumber,
                                    driverName, fuelCreditId, transactionStatus);
                            fleetList.add(fleetListModel);


                        }
                        Log.d(TAG, "onResponse: fleetlistsize : " + fleetList.size());
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
        FleetArrivalListAdapter fleetArrivalListAdapter = new FleetArrivalListAdapter(getActivity(), fleetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerArraivals.setLayoutManager(mLayoutManager);
        recyclerArraivals.setItemAnimator(new DefaultItemAnimator());
        fleetArrivalListAdapter.notifyDataSetChanged();
        recyclerArraivals.setAdapter(fleetArrivalListAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_fleet_list_fragment, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        Log.d(TAG, "initUI: ");
        commonCodeManager = new CommonCodeManager(getActivity());
        commonTaskManager = new CommonTaskManager(getActivity());
        apiHandlerManager = new APIHandlerManager(getActivity());

        recyclerArraivals = view.findViewById(R.id.recyclerview_arrivals);


        //accessing common search functionality from newArraivallistactivity
        ImageView imageViewSearchCommon = Objects.requireNonNull(getActivity()).findViewById(R.id.imageview_search_refuel);
        imageViewSearchCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertForSearchBoth();
            }
        });
    }

    private void showAlertForSearchBoth() {

        commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));

        AlertDialog.Builder builder
                = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setCancelable(false);
        final View customLayout = getLayoutInflater().inflate(R.layout.layout_dialog_search_perveh, null);
        builder.setView(customLayout);

        final EditText etVehicleNo = customLayout.findViewById(R.id.edittext_searchby_vehicle_new);
        final EditText etPersonNo = customLayout.findViewById(R.id.edittext_searchby_person_new);
        final RadioGroup radioGrCommon = customLayout.findViewById(R.id.radiogr_common_search);
        final MaterialButton buttonSearchPerson = customLayout.findViewById(R.id.button_search_common_person);
        final MaterialButton buttonSearchVehicle = customLayout.findViewById(R.id.button_search_common_vehicle);
        ImageView imageviewClose = customLayout.findViewById(R.id.iv_close_common);
        radioGrCommon.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonType = (RadioButton) group.findViewById(checkedId);
                if (null != radioButtonType) {
                    String whichBtn = radioButtonType.getText().toString().toLowerCase().trim();
                    Log.d(TAG, "onCheckedChanged:whichBtnReq:  " + whichBtn);

                    if (whichBtn.contains("person")) {
                        etPersonNo.setVisibility(View.VISIBLE);
                        etVehicleNo.setVisibility(View.GONE);
                        buttonSearchPerson.setVisibility(View.VISIBLE);
                        buttonSearchVehicle.setVisibility(View.GONE);

                    }

                    if (whichBtn.contains("vehicle")) {
                        etVehicleNo.setVisibility(View.VISIBLE);
                        etPersonNo.setVisibility(View.GONE);
                        buttonSearchVehicle.setVisibility(View.VISIBLE);
                        buttonSearchPerson.setVisibility(View.GONE);
                    }
                }
            }
        });


        final AlertDialog dialog
                = builder.create();
        dialog.show();

        imageviewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();

            }
        });

        int selectedTabNo = ((NewArrivalListActivity) Objects.requireNonNull(getActivity())).getSelectedTab();

        int selctedTab = commonCodeManager.getSeletctedTabForRefuel(getActivity());
        Log.d(TAG, "showAlertForSearchBoth:selectedTabNo :  " + selctedTab);

        if (selctedTab == 0) {
            buttonSearchPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone1 = etPersonNo.getText().toString();

                    if (!phone1.isEmpty()) {
                        Log.d(TAG, "onClick: person clicked");
                        List<FleetListModel> searchDetailsList = apiHandlerManager.apiCallForSearchDriver(getActivity(), phone1);
                        dialog.dismiss();

                        fillFleetListCards(searchDetailsList);
                    } else {
                        etPersonNo.setError("Please Enter Person Number");
                    }

                }
            });
            buttonSearchVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d(TAG, "onClick: vehicle clicked");
                    String vehicleNo = etVehicleNo.getText().toString();
                    commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));
                    if (!etVehicleNo.getText().toString().isEmpty()) {
                        dialog.dismiss();
                        List<FleetListModel> searchDetailsList = apiHandlerManager.apiCallForSerachByVehicleNumber(getActivity(), vehicleNo);

                        fillFleetListCards(searchDetailsList);
                    } else {
                        etVehicleNo.setError("Please Enter Vehicle Number");
                    }
                }
            });

        }


    }


}
