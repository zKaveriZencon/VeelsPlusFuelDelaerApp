package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.FirstReportAdapter;
import com.veelsplusfueldealerapp.adapters.FleetArrivalListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.FleetListModel;

import java.util.List;

public class ReportFirstActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ReportFirstActivity";
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerViewReport1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_first);

        initUI();
    }

    private void initUI() {

        commonTaskManager = new CommonTaskManager(ReportFirstActivity.this);
        commonCodeManager = new CommonCodeManager(ReportFirstActivity.this);

        recyclerViewReport1 = findViewById(R.id.recyclerview_report1);

        View toolbar = findViewById(R.id.layout_toolbar_report1);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Report 1");
        ImageView imageviewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageviewBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(ReportFirstActivity.this, HomeActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
       // fillReportCardsData();
    }

    private void fillReportCardsData(List<FleetListModel> fleetList) {
        FirstReportAdapter firstReportAdapter = new FirstReportAdapter(ReportFirstActivity.this, fleetList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ReportFirstActivity.this);
        recyclerViewReport1.setLayoutManager(mLayoutManager);
        recyclerViewReport1.setItemAnimator(new DefaultItemAnimator());
        firstReportAdapter.notifyDataSetChanged();
        recyclerViewReport1.setAdapter(firstReportAdapter);
    }
}