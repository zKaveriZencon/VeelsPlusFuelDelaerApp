package com.veelsplusfueldealerapp.managers;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppUpdateCheckManager {
    public static final String VERSION_CODE_KEY = "version";
    private static final String TAG = "AppUpdateCheckManager";
    Context mContext;
    CommonCodeManager mCommonCodeManager;
    int gradleVersion;
    CommonTaskManager commonTaskManager;
    int versionCode;

    public AppUpdateCheckManager(Context context) {
        mContext = context;
        mCommonCodeManager = new CommonCodeManager();
        commonTaskManager = new CommonTaskManager(mContext);
    }


    public int getCurrentVersionCode() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void checkForUpdate() {
       /* int latestAppVersion = (int) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);
        int gradleVersion = getCurrentVersionCode();*/

        //apiCallForGetAppCurrentVersion();


        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<JsonObject> call = service.getInfoFromSetup();
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                try {
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse: jsonData master : " + jsonData);
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();
                    final String message = mainObject.getString("msg").trim();

                    if (!status.isEmpty() && status.equals("ok")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            JSONObject jsonObject = dataArray.getJSONObject(0);

                            String dealerAppVersion = jsonObject.getString("dealerAppVersion");
                            if (!dealerAppVersion.isEmpty()) {
                                int dealerAppVer = Integer.parseInt(dealerAppVersion);

                                Log.d(TAG, "onResponse: gradleVersion : " + gradleVersion);
                                showUpdateAppDialog(dealerAppVer);
                            }

                        } else {

                        }

                    } else if (!status.isEmpty() && status.equals("error")) {
                        commonTaskManager.showToast(mContext, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.showToast(mContext, mContext.getResources().getString(R.string.unable_to_connect));
                t.printStackTrace();
            }
        });


    }

    private void showUpdateAppDialog(int driverAppVerFromDB) {
        //int latestAppVersionByFirebase = 3;  //now hardcoded
        int currentGradleVersion = getVersionCode(); //which apk is already installed on device

        Log.d(TAG, "showUpdateAppDialog: latestAppVersionByFirebase :" + currentGradleVersion);
        Log.d(TAG, "showUpdateAppDialog:driverAppVerFromDB :  " + driverAppVerFromDB);

        if (driverAppVerFromDB > currentGradleVersion) {
            Log.d(TAG, "checkForUpdate: new version available");
            UpdateAppDialog updateAppDialog = new UpdateAppDialog(mContext);
            updateAppDialog.showSimpleDialog();

        } else {
            Log.d(TAG, "checkForUpdate: app is up-to-date.");
        }

    }


    private int getVersionCode() {
        //getting version from gradle

        PackageManager manager = mContext.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(mContext.getPackageName(), 0);
            versionCode = info.versionCode;
            Log.d(TAG, "onCreate: versionCode :" + versionCode);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
