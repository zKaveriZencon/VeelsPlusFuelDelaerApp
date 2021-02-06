package com.veelsplusfueldealerapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.NewFuelReqPagerAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;

public class NewFuelCreditRequestActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "NewFuelCreditRequestAct";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        commonCodeManager.saveIfCameFromCorporateOrPerson(NewFuelCreditRequestActivity.this, "");

        Intent intent = new Intent(NewFuelCreditRequestActivity.this, ArraivalListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fuel_credit_request);
        initUI();
    }

    private void initUI() {
        configureTabLayout();

    }

    public void configureTabLayout() {
        commonCodeManager = new CommonCodeManager(NewFuelCreditRequestActivity.this);
        commonTaskManager = new CommonTaskManager(NewFuelCreditRequestActivity.this);

        View toolbar = findViewById(R.id.layout_toolbar_form_cre);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Create Fuel Credit Request");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);

        ImageView imageViewHome = toolbar.findViewById(R.id.imageview_goto_home);
        imageViewHome.setVisibility(View.VISIBLE);
        imageViewHome.setOnClickListener(this);


        TabLayout tabLayout =
                findViewById(R.id.tab_layout_createreq);

        tabLayout.addTab(tabLayout.newTab().setText("For Corporate"));
        //  tabLayout.addTab(tabLayout.newTab().setText("Person"));


        final ViewPager viewPager =
                findViewById(R.id.pager_createreq);
        final PagerAdapter adapter = new NewFuelReqPagerAdapter
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
        boolean result = commonTaskManager.compareDates(NewFuelCreditRequestActivity.this);

        if (result) {

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(NewFuelCreditRequestActivity.this, NewFuelCreditRequestActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(NewFuelCreditRequestActivity.this, ArraivalListActivity.class);
                startActivity(intent);
                break;

            case R.id.imageview_goto_home:
                Intent intent1 = new Intent(NewFuelCreditRequestActivity.this, HomeActivity.class);
                startActivity(intent1);
                break;
        }
    }
}