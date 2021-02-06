package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.NewFuelReqPagerAdapter;
import com.veelsplusfueldealerapp.adapters.NewFuelReqPagerAdapterManager;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;

public class CreateCreditReqManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateCreditReqManagerA";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        commonCodeManager.saveIfCameFromCorporateOrPerson(CreateCreditReqManagerActivity.this, "");

        Intent intent = new Intent(CreateCreditReqManagerActivity.this, ArraivalListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_credit_req_manager);
        initUI();
    }

    private void initUI() {
        configureTabLayout();

    }

    public void configureTabLayout() {
        commonCodeManager = new CommonCodeManager(CreateCreditReqManagerActivity.this);
        commonTaskManager = new CommonTaskManager(CreateCreditReqManagerActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_form_cre_mg);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Create Fuel Credit Request");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);


        TabLayout tabLayout =
                findViewById(R.id.tab_layout_createreq_mg);

        tabLayout.addTab(tabLayout.newTab().setText("For Corporate"));
        //tabLayout.addTab(tabLayout.newTab().setText("Person"));


        final ViewPager viewPager =
                findViewById(R.id.pager_createreq_mg);
        final PagerAdapter adapter = new NewFuelReqPagerAdapterManager
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            public void onTabReselected(TabLayout.Tab tab) {

            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean result = commonTaskManager.compareDates(CreateCreditReqManagerActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(CreateCreditReqManagerActivity.this, CreateCreditReqManagerActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(CreateCreditReqManagerActivity.this, ArraivalListActivity.class);
                startActivity(intent);
                break;

            case R.id.imageview_goto_home:
                Intent intent1 = new Intent(CreateCreditReqManagerActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;


        }
    }
}