package com.veelsplusfueldealerapp.managers;

import android.content.Context;
import android.os.AsyncTask;

import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;

public class GetFuelInfraDetailsTask extends AsyncTask<String, Void, String> {
    private Context context;

    public GetFuelInfraDetailsTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        CommonCodeManager commonCodeManager = new CommonCodeManager(context);
        String[] details = commonCodeManager.getDealerDetails(context);

        APIHandlerManager apiHandlerManager = new APIHandlerManager(context);
        apiHandlerManager.apiCallForGetFuelInfraDetails(context, details[1]);
        return null;
    }
}
