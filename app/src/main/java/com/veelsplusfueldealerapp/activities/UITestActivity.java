package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.veelsplusfueldealerapp.MainActivity;
import com.veelsplusfueldealerapp.R;

public class UITestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_u_i_test);

        RadioGroup radioGroupReq = (RadioGroup) findViewById(R.id.radiogr_required);
        RadioGroup radioGroupActual = (RadioGroup) findViewById(R.id.radiogr_actual);

        radioGroupReq.clearCheck();
        radioGroupReq.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbReq = (RadioButton) group.findViewById(checkedId);
                if (null != rbReq) {
                    Toast.makeText(UITestActivity.this, rbReq.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

        radioGroupActual.clearCheck();
        radioGroupActual.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rbActual = (RadioButton) group.findViewById(checkedId);
                if (null != rbActual) {
                    Toast.makeText(UITestActivity.this, rbActual.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });

       /* int selectedId1 = radioGroupReq.getCheckedRadioButtonId();
        RadioButton radioReq = (RadioButton) findViewById(selectedId1);
        Toast.makeText(UITestActivity.this, radioReq.getText(), Toast.LENGTH_SHORT).show();

        int selectedId2 = radioGroupActual.getCheckedRadioButtonId();
        RadioButton radioActual = (RadioButton) findViewById(selectedId2);
        Toast.makeText(UITestActivity.this, radioActual.getText(), Toast.LENGTH_SHORT).show();*/
    }
}