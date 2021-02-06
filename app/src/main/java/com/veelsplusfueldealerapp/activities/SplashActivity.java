package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.models.LoginCredentialsModel;

import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {
    public static final String VERSION_CODE_KEY = "version";
    private static final String TAG = "SplashActivity";
    private CommonCodeManager commonCodeManager;
    private String mLatestVersionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        commonCodeManager = new CommonCodeManager(SplashActivity.this);
        LoginCredentialsModel loginCredentialsModel = commonCodeManager.getLoginCredentials(this);

        if (loginCredentialsModel.isLoggedIn()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }, 1000);
        }
    }

}
