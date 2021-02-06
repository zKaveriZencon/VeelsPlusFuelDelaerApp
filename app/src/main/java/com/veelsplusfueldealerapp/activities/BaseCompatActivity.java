package com.veelsplusfueldealerapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;

import java.util.Locale;

public class BaseCompatActivity extends AppCompatActivity {
    private static final String TAG = "BaseCompatActivity";
    CommonCodeManager commonCodeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_compat);

        commonCodeManager = new CommonCodeManager(BaseCompatActivity.this);
        changeLocale();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        changeLocale();
    }

    public void changeLocale() {
        String selectedLanguage = commonCodeManager.getSelectedLanguage(BaseCompatActivity.this);
        if (selectedLanguage.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(selectedLanguage);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
    }

}