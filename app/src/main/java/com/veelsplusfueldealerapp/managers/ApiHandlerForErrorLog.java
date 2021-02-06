package com.veelsplusfueldealerapp.managers;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.ErrorLogModel;
import com.veelsplusfueldealerapp.models.OperatorStartShiftModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiHandlerForErrorLog {
    private static final String TAG = "ApiHandlerForErrorLog";
    Context context;
    CommonCodeManager commonCodeManager;
    CommonTaskManager commonTaskManager;

    public ApiHandlerForErrorLog(Context context) {
        this.context = context;
        commonCodeManager = new CommonCodeManager(context);
        commonTaskManager = new CommonTaskManager(context);
    }

    public void apiCallForAddErrorLogInDb(final Context context,
                                          final ErrorLogModel errorLogModel) {
      /*  progressDialogStart = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        //progressDialogStart.setMessage(context.getResources().getString(R.string.sending_request));
        progressDialogStart.setMessage("Shift start");
        progressDialogStart.setCancelable(false);
        progressDialogStart.setCanceledOnTouchOutside(false);
        progressDialogStart.show();
*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addErrorDetails(errorLogModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddErrorLogInDb :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                 //   commonTaskManager.dismissProgressDialog(context, progressDialogStart);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");
                    if (status.equals("OK")) {
                    } else {
                        commonTaskManager.showToast(context, message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();

            }

        });
    }

}
