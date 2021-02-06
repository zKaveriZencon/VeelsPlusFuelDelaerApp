package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.veelsplusfueldealerapp.R;


public class DashboardActivityDummy extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "DashboardActivity";
    private TextView textViewarraivallist, textViewReportsView, textViewInventoryList, textViewOpertaorList, textViewWorkList, textViewShiftOperator, textViewEarnReport;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_dummy);

        initUI();
    }

    private void initUI() {
        textViewarraivallist = findViewById(R.id.textview_arraivals_list);
        textViewReportsView = findViewById(R.id.textview_reports_view);
        textViewInventoryList = findViewById(R.id.textview_inventory_list);
        textViewOpertaorList = findViewById(R.id.operator_list);
        textViewWorkList = findViewById(R.id.textview_daily_work_list);
        textViewShiftOperator = findViewById(R.id.textview_shift_operator);
        textViewEarnReport = findViewById(R.id.textview_earning_report);

        textViewarraivallist.setOnClickListener(this);
        textViewReportsView.setOnClickListener(this);
        textViewInventoryList.setOnClickListener(this);
        textViewOpertaorList.setOnClickListener(this);
        textViewWorkList.setOnClickListener(this);
        textViewShiftOperator.setOnClickListener(this);
        textViewEarnReport.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textview_arraivals_list:
                Intent arrivalsIntent = new Intent(DashboardActivityDummy.this, ArraivalListActivity.class);
                startActivity(arrivalsIntent);
                break;
            case R.id.textview_reports_view:
                break;
            case R.id.textview_inventory_list:
                Intent inventoryIntent = new Intent(DashboardActivityDummy.this, ProductReceiptListActivity.class);
                startActivity(inventoryIntent);
                break;
            case R.id.operator_list:
                Intent operatorIntent = new Intent(DashboardActivityDummy.this, OperatorsListActivity.class);
                startActivity(operatorIntent);
                break;
            case R.id.textview_daily_work_list:
                Intent dailyWorkIntent = new Intent(DashboardActivityDummy.this, OperatorDailyWorkListActivity.class);
                startActivity(dailyWorkIntent);
                break;
            case R.id.textview_shift_operator:
                Intent shiftIntent = new Intent(DashboardActivityDummy.this, OperatorShiftStartEndActivity.class);
                startActivity(shiftIntent);
                break;
            case R.id.textview_earning_report:
                Intent earnIntent = new Intent(DashboardActivityDummy.this, EarningsReportActivity.class);
                startActivity(earnIntent);
                break;
        }
    }
}