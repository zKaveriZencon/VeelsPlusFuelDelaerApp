package com.veelsplusfueldealerapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.ArraivalListActivity;
import com.veelsplusfueldealerapp.activities.DailySalesActivity;
import com.veelsplusfueldealerapp.activities.DailyStockReadingActivity;
import com.veelsplusfueldealerapp.activities.DailyStockReadingListActivity;
import com.veelsplusfueldealerapp.activities.FuelPriceListActivity;
import com.veelsplusfueldealerapp.activities.MappedAccountActivity;
import com.veelsplusfueldealerapp.activities.NewArrivalListActivity;
import com.veelsplusfueldealerapp.activities.OperatorDailyWorkListActivity;
import com.veelsplusfueldealerapp.activities.OperatorsListActivity;
import com.veelsplusfueldealerapp.activities.ProductReceiptListActivity;
import com.veelsplusfueldealerapp.activities.ReportFirstActivity;
import com.veelsplusfueldealerapp.activities.SetFuelPriceActivity;
import com.veelsplusfueldealerapp.activities.ShimmerEffectActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.UserProfileModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "FleetListFragment";
    int visibility;
    View view;
    boolean isExpanded;
    LinearLayout expandableLayoutManager, expandableLayoutOperator, expandableLayoutReports;
    CommonCodeManager commonCodeManager;
    LinearLayout layoutParentManager, layoutOperator, layoutReportManager, layoutHomeParent;
    CommonTaskManager commonTaskManager;
    UserProfileModel userProfileModel;
    private TextInputLayout tvManagerMenuTitle, tvReportsMenuTitle,
            tvOpertaorMenuTitle;

    private ImageView ivMenuitemRefuelListMg, ivMenuitemReports, ivMenuitemInventory, ivOperator,
            ivMenuitemDailyWork, ivMenuitemEarnReport, ivMenuitemRefuelListOp,
            ivMenuiteDailyStockMg, ivMenuitemPrice, imageViewMappedAcc, ivivMenuitemFuelExpense;

    private TextInputEditText etMenuTitleManager, etMenuTitleOperator, etMenuTitleReports;

    private TextView textViewTvTest, textViewCompanyName, textViewBrandName,
            textViewOwnerName, textViewMenuSetFuelPrice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_dashborad, container, false);
        initUI();
        return view;
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(getActivity());
        commonCodeManager = new CommonCodeManager(getActivity());

        tvManagerMenuTitle = view.findViewById(R.id.tv_manager_menu_title);
        tvOpertaorMenuTitle = view.findViewById(R.id.tv_operator_menu_title);
        tvReportsMenuTitle = view.findViewById(R.id.tv_reports_menu_title);

        textViewTvTest = view.findViewById(R.id.tv_test);


        etMenuTitleManager = view.findViewById(R.id.et_menu_title_manager);
        etMenuTitleOperator = view.findViewById(R.id.et_menu_title_operator);
        etMenuTitleReports = view.findViewById(R.id.et_menu_title_reports);

        ivMenuitemRefuelListMg = view.findViewById(R.id.iv_mitem_mg_refuel);
        ivMenuitemRefuelListOp = view.findViewById(R.id.iv_mitem_op_refuel_list);
        ivMenuitemReports = view.findViewById(R.id.iv_mitem_reports);
        ivMenuitemInventory = view.findViewById(R.id.iv_mitem_inventory);
        ivOperator = view.findViewById(R.id.iv_mitem_mg_oplist);
        //ivMenuitemOpshift = view.findViewById(R.id.iv_mitem_op_shift);
        ivMenuitemDailyWork = view.findViewById(R.id.iv_mitem_daily_work);
        ivMenuitemEarnReport = view.findViewById(R.id.iv_mitem_earning_report);
        ivMenuiteDailyStockMg = view.findViewById(R.id.iv_mitem_mg_dailystock);
        ivMenuitemPrice = view.findViewById(R.id.iv_mitem_mg_price);
        imageViewMappedAcc = view.findViewById(R.id.iv_mitem_mg_mappedacc);
        ivivMenuitemFuelExpense = view.findViewById(R.id.iv_mitem_mg_fuel_expense);


        expandableLayoutManager = view.findViewById(R.id.expandableLayout_manager_menu);
        expandableLayoutOperator = view.findViewById(R.id.expandableLayout_operator);
        expandableLayoutReports = view.findViewById(R.id.expandableLayout_reports);

        textViewOwnerName = view.findViewById(R.id.textview_owner_name);
        textViewBrandName = view.findViewById(R.id.textview_brand_name);
        textViewCompanyName = view.findViewById(R.id.textview_company_name);
        textViewMenuSetFuelPrice = view.findViewById(R.id.tv_mitem_mg_fuelprice);

        layoutParentManager = view.findViewById(R.id.layout_parent_manager);
        layoutOperator = view.findViewById(R.id.layout_parent_operator_list);
        layoutReportManager = view.findViewById(R.id.layout_parent_reports);
        layoutHomeParent = view.findViewById(R.id.layout_home_parent);

       /*
       working code temporarily commented

       layoutHomeParent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                expandableLayoutManager.setVisibility(View.GONE);
                expandableLayoutOperator.setVisibility(View.GONE);
                expandableLayoutReports.setVisibility(View.GONE);

                return false;
            }
        });*/

        ivMenuitemRefuelListMg.setOnClickListener(this);
        ivMenuitemRefuelListOp.setOnClickListener(this);
        ivMenuitemReports.setOnClickListener(this);
        ivMenuitemInventory.setOnClickListener(this);
        ivOperator.setOnClickListener(this);
        //ivMenuitemOpshift.setOnClickListener(this);
        ivMenuitemDailyWork.setOnClickListener(this);
        ivMenuitemEarnReport.setOnClickListener(this);
        imageViewMappedAcc.setOnClickListener(this);


        etMenuTitleManager.setOnClickListener(this);
        etMenuTitleOperator.setOnClickListener(this);
        etMenuTitleReports.setOnClickListener(this);
        textViewTvTest.setOnClickListener(this);
        ivMenuiteDailyStockMg.setOnClickListener(this);
        ivMenuitemPrice.setOnClickListener(this);
        ivivMenuitemFuelExpense.setOnClickListener(this);

        tvManagerMenuTitle.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: refuel click");
                visibility = 1;
                isExpanded = true;
                setViewVisibility(visibility);

            }
        });

        tvOpertaorMenuTitle.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: refuel click");
                visibility = 2;
                setViewVisibility(visibility);

            }
        });


        tvReportsMenuTitle.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: refuel click");
                visibility = 3;

                setViewVisibility(visibility);

            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        String[] details = commonCodeManager.getEssentialsForDealer(Objects.requireNonNull(getActivity()));
        String designation = details[4].toLowerCase().trim();
        if (designation != null && !designation.equals("null") && !designation.isEmpty() && designation.equals("operator")) {
            //for operator
            layoutParentManager.setVisibility(View.GONE);
            layoutReportManager.setVisibility(View.GONE);
        } else {
            //for manager
            layoutOperator.setVisibility(View.GONE);
        }

        UserProfileModel userProfileModel = commonCodeManager.getUserProfileInfo(getActivity());
        Log.d(TAG, "onResume: username : " + userProfileModel.getUsername());
        Log.d(TAG, "onResume:getCompanyName : " + userProfileModel.getUserPhone());
        if (userProfileModel.getUsername().isEmpty()) {
            apiCallForGetUserProfileInfo();
            Log.d(TAG, "onResume: if part ");

        } else {
            Log.d(TAG, "onResume: else part");
            textViewCompanyName.setText(userProfileModel.getCompanyName());
            textViewBrandName.setText(userProfileModel.getBrandName());
        }


/*
        UserProfileModel userProfileModel = commonCodeManager.getUserProfileInfo(getActivity());
        textViewCompanyName.setText(userProfileModel.getCompanyName());
        textViewBrandName.setText(userProfileModel.getBrandName());*/


    }

    private void apiCallForGetUserProfileInfo() {

        String[] details = commonCodeManager.getDealerDetails(getActivity());


        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage(getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(getActivity()).create(GetDataService.class);
        Call<JsonObject> call = service.getUserProfileInfo(details[1], details[0]);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetUserProfileInfo :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(getActivity(), progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        Log.d(TAG, "onResponse: data array length : " + dataArray.length());
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);

                            String firstName = duObject.getString("firstName");
                            String lastName = duObject.getString("lastName");
                            String phone1 = duObject.getString("phone1");
                            String personImagePath = duObject.getString("personImagePath");
                            String hostPhone = duObject.getString("hostPhone");
                            String companyName = duObject.getString("companyName");
                            String cityArea = duObject.getString("cityArea");
                            String city = duObject.getString("city");
                            String pin = duObject.getString("pin");
                            String brandName = duObject.getString("brandName");

                            String userName = firstName + " " + lastName;
                            String dealerAddress = companyName + " " + cityArea + " " + city + " " + pin;
                            userProfileModel = new UserProfileModel(userName, companyName, dealerAddress, phone1, hostPhone, personImagePath, brandName);

                        }

                        updateUserInfo(userProfileModel);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(getActivity(), progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();

                commonTaskManager.dismissProgressDialog(getActivity(), progressDialog);

            }

        });
    }

    private void updateUserInfo(UserProfileModel userProfileModel) {
        commonCodeManager.saveUserProfileInfo(Objects.requireNonNull(getActivity()), userProfileModel);
        textViewCompanyName.setText(userProfileModel.getCompanyName());
        textViewBrandName.setText(userProfileModel.getBrandName());

    }

    public void setViewVisibility(int visibility) {
        expandableLayoutManager.setVisibility(View.GONE);
        expandableLayoutOperator.setVisibility(View.GONE);
        expandableLayoutReports.setVisibility(View.GONE);

        switch (visibility) {
            case 1:
                expandableLayoutManager.setVisibility(View.VISIBLE);

                break;
            case 2:
                expandableLayoutOperator.setVisibility(View.VISIBLE);

                break;
            case 3:
                expandableLayoutReports.setVisibility(View.VISIBLE);

                break;
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_mitem_mg_refuel:
                Intent arrivalsIntent = new Intent(getActivity(), ArraivalListActivity.class);
                startActivity(arrivalsIntent);
                break;
            case R.id.iv_mitem_op_refuel_list:
                Intent arrivalsIntent1 = new Intent(getActivity(), ArraivalListActivity.class);
                startActivity(arrivalsIntent1);
                break;
            case R.id.iv_mitem_reports:
                Intent reportIntent1 = new Intent(getActivity(), ReportFirstActivity.class);
                startActivity(reportIntent1);
                break;
            case R.id.iv_mitem_inventory:
                Intent inventoryIntent = new Intent(getActivity(), ProductReceiptListActivity.class);
                startActivity(inventoryIntent);
                break;
            case R.id.iv_mitem_mg_oplist:
                Intent operatorIntent = new Intent(getActivity(), OperatorsListActivity.class);
                startActivity(operatorIntent);
                break;
           /* case R.id.iv_mitem_op_shift:
                Intent shiftIntent = new Intent(getActivity(), OperatorShiftStartEndActivity.class);
                startActivity(shiftIntent);
                break;*/
            case R.id.iv_mitem_daily_work:
                Intent dailyWorkIntent = new Intent(getActivity(), OperatorDailyWorkListActivity.class);
                startActivity(dailyWorkIntent);
                break;
            case R.id.iv_mitem_earning_report:
                Intent earnIntent = new Intent(getActivity(), DailySalesActivity.class);
                startActivity(earnIntent);
                break;
            case R.id.iv_mitem_mg_dailystock:
                Intent dailyIntent = new Intent(getActivity(), DailyStockReadingListActivity.class);
                startActivity(dailyIntent);
                break;

            case R.id.et_menu_title_manager:
                visibility = 1;
                setViewVisibility(visibility);
                break;

            case R.id.et_menu_title_operator:
                visibility = 2;
                setViewVisibility(visibility);
                break;

            case R.id.et_menu_title_reports:
                visibility = 3;
                setViewVisibility(visibility);
                break;
            case R.id.tv_test:
                Intent intent = new Intent(getActivity(), ShimmerEffectActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_mitem_mg_price:
                Intent intent1 = new Intent(getActivity(), FuelPriceListActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_mitem_mg_mappedacc:
                Intent intent2 = new Intent(getActivity(), MappedAccountActivity.class);
                startActivity(intent2);
                break;

            case R.id.iv_mitem_mg_fuel_expense:

                //navigation to the fuel expense list
                break;

        }
    }
}
