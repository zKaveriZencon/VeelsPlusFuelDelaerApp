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
import com.veelsplusfueldealerapp.activities.NewArrivalListActivity;
import com.veelsplusfueldealerapp.adapters.NonFleetArrivalListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.FleetListModel;
import com.veelsplusfueldealerapp.models.NonFleetListModel;
import com.veelsplusfueldealerapp.models.NonFleetModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NonFleetListFragment extends Fragment {
    private static final String TAG = "NonFleetListFragment";
    View view;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerArraivals;
    APIHandlerManager apiHandlerManager;
    RadioButton radioButtonType;

    @Override
    public void onResume() {
        super.onResume();

        apiCallForGetAllFuelCreditRequestsPerson(getActivity());
    }

    private void apiCallForGetAllFuelCreditRequestsPerson(final Context context) {
        final String[] details = commonCodeManager.getDealerDetails(context);

        Log.d(TAG, "apiCallForGetAllFuelCreditRequestDealer: dealerId :" + details[2]);

        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<NonFleetModel> fleetList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getFuelCreditReqForPersonByDealer(details[1]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllFuelCreditRequestsPerson :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject detailsObj = dataArray.getJSONObject(i);
                            String estimatedRefuelDate = detailsObj.getString("estimatedRefuelDate");
                            String vehicleNumber = detailsObj.getString("vehicleNumber");

                            String fleetNoFleetStatus = detailsObj.getString("fleetNoFleetStatus").toLowerCase().trim();
                            String firstName = detailsObj.getString("firstName");
                            String lastName = detailsObj.getString("lastName");
                            String transactionStatus = detailsObj.getString("transactionStatus");
                            String fuelCreditId = detailsObj.getString("fuelCreditId");
                            String phone1 = detailsObj.getString("phone1");


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

                            NonFleetModel nonFleet = new NonFleetModel(driverName, phone1, dateOnly, vehicleNumber, transactionStatus, phone1, fuelCreditId);

                            fleetList.add(nonFleet);
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

    private void fillFleetListCards(List<NonFleetModel> fleetList) {
        NonFleetArrivalListAdapter nonFleet = new NonFleetArrivalListAdapter(getActivity(), fleetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerArraivals.setLayoutManager(mLayoutManager);
        recyclerArraivals.setItemAnimator(new DefaultItemAnimator());
        nonFleet.notifyDataSetChanged();
        recyclerArraivals.setAdapter(nonFleet);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_non_fleet_list_frag, container, false);
        initUI();
        return view;
    }

    private void initUI() {
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
                        etVehicleNo.setVisibility(View.INVISIBLE);
                        buttonSearchPerson.setVisibility(View.VISIBLE);
                        buttonSearchVehicle.setVisibility(View.INVISIBLE);

                    }

                    if (whichBtn.contains("vehicle")) {
                        etVehicleNo.setVisibility(View.VISIBLE);
                        etPersonNo.setVisibility(View.INVISIBLE);
                        buttonSearchVehicle.setVisibility(View.VISIBLE);
                        buttonSearchPerson.setVisibility(View.INVISIBLE);
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
        int selectedTab = commonCodeManager.getSeletctedTabForRefuel(getActivity());

        Log.d(TAG, "showAlertForSearchBoth:selectedTabNo :  " + selectedTab);
        if (selectedTab == 1) {
            buttonSearchPerson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String phone1 = etPersonNo.getText().toString();

                    if (!phone1.isEmpty()) {
                        dialog.dismiss();
                        List<NonFleetModel> searchDetailsList = apiHandlerManager.apiCallForSearchDriverNonFleet(getActivity(), phone1);
                        fillFleetListCards(searchDetailsList);
                    } else {
                        etPersonNo.setError("Please Enter Person Number");
                    }

                }
            });
            buttonSearchVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String vehicleNo = etVehicleNo.getText().toString();
                    commonTaskManager.hideKeyboard(Objects.requireNonNull(getActivity()));
                    if (!etVehicleNo.getText().toString().isEmpty()) {

                        List<NonFleetModel> searchDetailsList = apiHandlerManager.apiCallForSerachByVehicleNumberNonFleet(getActivity(), vehicleNo);
                        fillFleetListCards(searchDetailsList);
                    } else {
                        etVehicleNo.setError("Please Enter Vehicle Number");
                    }
                }
            });
        }

    }

}
