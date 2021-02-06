package com.veelsplusfueldealerapp.managers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.veelsplusfueldealerapp.activities.TestUIActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;

import java.io.File;

public class FuelDealerApplication extends Application {
    private static FuelDealerApplication instance;
    CommonCodeManager commonCodeManager;

    public static FuelDealerApplication getInstance() {
        return instance;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

}