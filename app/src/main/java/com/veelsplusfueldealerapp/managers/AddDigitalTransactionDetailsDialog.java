package com.veelsplusfueldealerapp.managers;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.AddProductReceiptActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.FuelTerminalModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDigitalTransactionDetailsDialog {
    private static final String TAG = "UpdateAppDialog";
    public Context mContext;
    EditText editTextTransactionId, editTextAmount;
    Button buttonSubmit, buttonCancel;
    Spinner spinnerSelectDevice;
    DatabaseHelper databaseHelper;
    String selectedTerminal;
    CommonTaskManager commonTaskManager;
    String terminalId, terminalType;

    public AddDigitalTransactionDetailsDialog(Context context) {
        this.mContext = context;
        databaseHelper = new DatabaseHelper(context);
        commonTaskManager = new CommonTaskManager(mContext);

    }

    public void showSimpleDialog(final String activityId, List<String> staffPerformIds) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater != null) {
            View dialogView = inflater.inflate(R.layout.frag_add_details_digital, null);
            builder.setView(dialogView);
            editTextTransactionId = dialogView.findViewById(R.id.edittext_trans_id_digital);
            editTextAmount = dialogView.findViewById(R.id.edittext_amount_digital);
            buttonSubmit = dialogView.findViewById(R.id.button_submit_digital);
            spinnerSelectDevice = dialogView.findViewById(R.id.spinnr_select_device_digital);


            final String[] termDetails = fillTerminalDetailsSpinner();
            //termdetails][2] - terminal type


            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonCodeManager cm = new CommonCodeManager(mContext);
                    String[] details = cm.getDealerDetails(mContext);

                    ErDigitalModel erDigitalModel = new ErDigitalModel(
                            commonTaskManager.getCurrentDateNew(),
                            details[0], editTextAmount.getText().toString(), activityId,
                            commonTaskManager.getBatchIdDetails(), "",
                            editTextTransactionId.getText().toString(), "",
                            "", editTextAmount.getText().toString(),
                            termDetails[1],"","",
                            "","");

                    apiCallForAddAccountTransactionLogForDigital(mContext, erDigitalModel);


                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    private void apiCallForAddAccountTransactionLogForDigital(final Context context, ErDigitalModel erDigitalModel) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        final List<OperatorDailyWorkListModel> pendingPays = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addAccountTransactionLogForDigital(erDigitalModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForForDigital :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        commonTaskManager.dismissDialogWithToast(context, progressDialog, message);

                    }


                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissProgressDialog(context, progressDialog);

            }

        });

    }

    private String[] fillTerminalDetailsSpinner() {
        final List<FuelTerminalModel> terminalsList = databaseHelper.getAllTerminals();
        String[] terminals = new String[terminalsList.size()];

        for (int i = 0; i < terminalsList.size(); i++) {
            FuelTerminalModel fm = terminalsList.get(i);
            terminals[i] = fm.getTerminalName();

        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, terminals);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectDevice.setAdapter(arrayAdapter1);
        spinnerSelectDevice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTerminal = (String) spinnerSelectDevice.getSelectedItem();
                if (selectedTerminal.contains("Select Device")) {

                } else {
                    FuelTerminalModel fm = terminalsList.get(position);
                    terminalId = fm.getFuelTerminalsId();
                    terminalType = fm.getTerminalType();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        String[] termDetails = {selectedTerminal, terminalId, terminalType};
        return termDetails;
    }


}
