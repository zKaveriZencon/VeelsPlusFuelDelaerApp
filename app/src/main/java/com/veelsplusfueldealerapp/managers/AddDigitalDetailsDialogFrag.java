package com.veelsplusfueldealerapp.managers;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.FuelTerminalModel;

import java.util.List;

public class AddDigitalDetailsDialogFrag extends DialogFragment {
    private static final String TAG = "AddDigitalDetailsDialog";
    Context mContext;
    EditText editTextTransactionId, editTextAmount;
    Button buttonSubmit, buttonCancel;
    Spinner spinnerSelectDevice;
    DatabaseHelper databaseHelper;
    String selectedTerminal;
    CommonTaskManager commonTaskManager;
    String terminalId, terminalType;
    String[] digitalDetails;
    CommonCodeManager commonCodeManager;

    public AddDigitalDetailsDialogFrag(Context mContext) {
        this.mContext = mContext;
        databaseHelper = new DatabaseHelper(mContext);
        commonTaskManager = new CommonTaskManager(mContext);
        commonCodeManager = new CommonCodeManager(mContext);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        digitalDetails = new String[11];
        if (inflater != null) {
            final View dialogView = inflater.inflate(R.layout.frag_add_details_digital, null);
            editTextTransactionId = dialogView.findViewById(R.id.edittext_trans_id_digital);
            editTextAmount = dialogView.findViewById(R.id.edittext_amount_digital);
            buttonSubmit = dialogView.findViewById(R.id.button_submit_digital);
            spinnerSelectDevice = dialogView.findViewById(R.id.spinnr_select_device_digital);


            final String[] termDetails = fillTerminalDetailsSpinner();


            buttonSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonCodeManager cm = new CommonCodeManager(mContext);
                    String[] details = cm.getDealerDetails(mContext);
                    String grandTotal = editTextAmount.getText().toString();

                    String selectedTerminal = (String) spinnerSelectDevice.getSelectedItem();

                    List<String> detailsTerms = databaseHelper.getAllTerminalsType(selectedTerminal);
                    Log.d(TAG, "onClick:trans id : " + detailsTerms.get(0));

                    String selcTermType = detailsTerms.get(1).toLowerCase().trim();
                    Log.d(TAG, "onClick:terminalTypeSelcted :  " + detailsTerms.get(1));

                    String activityId = commonCodeManager.getActivityId(mContext);
                    Log.d(TAG, "onCreateDialog: activity ID :" + activityId);

                    if (selcTermType.equals("card")) {
                        Log.d(TAG, "onClick:card ");

                        String[] digitalDetails = {commonTaskManager.getCurrentDateTimeNewFormat(),
                                details[0], grandTotal,
                                activityId, commonTaskManager.getBatchIdDetails(), "",
                                editTextTransactionId.getText().toString(), "", grandTotal,
                                "", detailsTerms.get(0)};


                        commonCodeManager.saveDigitalTransDetails(mContext, digitalDetails);

                    } else if (selcTermType.equals("paytm")) {
                        Log.d(TAG, "onClick:paytm ");

                        String[] digitalDetails = {commonTaskManager.getCurrentDateTimeNewFormat(),
                                details[0], grandTotal,
                                activityId,
                                commonTaskManager.getBatchIdDetails(), "",
                                editTextTransactionId.getText().toString(), grandTotal,
                                "", "",
                                detailsTerms.get(0)};
                        Log.d(TAG, "onClick: detailsTerms.get(0) :  "+ detailsTerms.get(0));
                        commonCodeManager.saveDigitalTransDetails(mContext, digitalDetails);

                    } else {

                    }
                    AddDigitalTransDetails addDigitalTransDetails = new AddDigitalTransDetails(mContext);
                    addDigitalTransDetails.execute("");

                    dismiss();

                }
            });

            builder.setView(dialogView);

        }
        return builder.create();

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