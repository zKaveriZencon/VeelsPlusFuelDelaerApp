package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;

public class TestUIActivity extends AppCompatActivity {
    String selectedLang = "en";
    CommonCodeManager commonCodeManager;
    TextView textViewFirst, textViewLast;
    private Spinner spinnerLanguages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_u_i);


        textViewFirst = findViewById(R.id.textview_first);
        textViewLast = findViewById(R.id.textview_last);
        commonCodeManager = new CommonCodeManager(TestUIActivity.this);
        spinnerLanguages = findViewById(R.id.spinner_languages);

/*

        ArrayAdapter arrayAdapter = new ArrayAdapter(TestUIActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.select_language));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguages.setAdapter(arrayAdapter);

        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                switch (position) {
                    case 0:
                        selectedLang = "en";
                        commonCodeManager.saveSelectedLanguage(TestUIActivity.this, selectedLang);
                        break;
                    case 1:
                        selectedLang = "hi";

                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
*/

    }

   /* @Override
    protected void attachBaseContext(Context newBase) {
        LocalizationUtil localizationUtil = new LocalizationUtil();
        super.attachBaseContext(localizationUtil.applyLanguage(newBase, selectedLang));
    }*/
}