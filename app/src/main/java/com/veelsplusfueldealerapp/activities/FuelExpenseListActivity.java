package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;

public class FuelExpenseListActivity extends AppCompatActivity {
    private static final String TAG = "FuelExpenseListActivity";
    RecyclerView recyclerDailyWork;
    FloatingActionButton fabShiftStartEnd;
    private CommonTaskManager commonTaskManager;
    private CommonCodeManager commonCodeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_expense_list);


    }
}