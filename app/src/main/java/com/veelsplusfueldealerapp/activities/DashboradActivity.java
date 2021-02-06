package com.veelsplusfueldealerapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.models.DashboardModel;

import java.util.List;

public class DashboradActivity extends BaseCompatActivity {
    private static final String TAG = "DashboradActivity";
    List<DashboardModel> menusList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashborad);

       // initUI();
    }

   /* private void initUI() {
        View toolbar = findViewById(R.id.toolbar_custom_dasboard);
        ImageView icon = toolbar.findViewById(R.id.imageview_hamburger_main);
        icon.setVisibility(View.VISIBLE);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Dashboard");

        menusList = new ArrayList<>();
        menusList = getDummyData();
        DashboardAdapter dashboardAdapter = new DashboardAdapter(DashboradActivity.this, menusList);
        RecyclerView recyclerViewDashboard = findViewById(R.id.recyclerView_dashboard);
        recyclerViewDashboard.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDashboard.setAdapter(dashboardAdapter);
    }

    private List<DashboardModel> getDummyData() {

        DashboardModel d1 = new DashboardModel("Re-Fuel List", "Re-Fuel List", "Second", "Third", "");
        DashboardModel d2 = new DashboardModel("Reports View", "Reports View", "Second", "Third", "");
        DashboardModel d3 = new DashboardModel("Inventory List", "Inventory List", "Second", "Third", "");
        //DashboardModel d4 = new DashboardModel("Operator List", "Operator List", "Second", "Third", "");
        menusList.add(d1);
        menusList.add(d2);
        menusList.add(d3);
       // menusList.add(d4);
        return menusList;
        *//*DashboardModel d5=new DashboardModel("Daily Work List", "Daily Work List", "Second", "Third", "");
        DashboardModel d4=new DashboardModel("Operator List", "Operator List", "Second", "Third", "");
            *//*

    }*/

}