package com.veelsplusfueldealerapp.managers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.models.ErDigitalModel;

public class AddDigitalTransDetails extends AsyncTask<String, Void, String> {
    private Context context;

    public AddDigitalTransDetails(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings) {
        CommonCodeManager commonCodeManager = new CommonCodeManager(context);
        String[] details = commonCodeManager.getDigitalTransDetails(context);
        String corporateId = commonCodeManager.getCorporateId(context);

        ErDigitalModel erDigitalModel = new ErDigitalModel(details[0], details[1], details[2],
                details[3], details[4], details[5], details[6], details[7], details[8], details[9],
                details[10], "", "", corporateId,"");

        Log.d("TAG", "initUI:details[0]id :  " + details[10]);


        APIHandlerManager apiHandlerManager = new APIHandlerManager(context);
        apiHandlerManager.apiCallForAddAccountTransactionLogForDigital(context, erDigitalModel);

        return null;
    }
}
