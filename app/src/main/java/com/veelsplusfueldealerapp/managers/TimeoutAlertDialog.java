package com.veelsplusfueldealerapp.managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.activities.LoginActivity;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;

public class TimeoutAlertDialog {
    public Context mContext;
    public Dialog dialog;
    private Activity mActivity;
    ImageView imageViewClose;
    String forWhat;

    private CommonTaskManager mTaskManager;

    public TimeoutAlertDialog(Activity activity, Context context, String forWhat) {
        this.mActivity = activity;
        this.mContext = context;
        this.forWhat = forWhat;
    }

    public void showSimpleDialog() {

        mTaskManager = new CommonTaskManager(mContext);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {


            View titleView = inflater.inflate(R.layout.custom_title_bank_dialog, null);
            builder.setCustomTitle(titleView);
            TextView textViewTitle = titleView.findViewById(R.id.textview_title_dialog);
            TextView textviewMsg1 = titleView.findViewById(R.id.textview_message_dialog);
            textViewTitle.setText("Alert!");
            textviewMsg1.setText("Your session has timed out, Please Log In again.");
            Button buttonOk = titleView.findViewById(R.id.button_ok_dialog);

            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();


                    /*mTaskManager.deleteLoginDetails();
                    mTaskManager.deleteAccessToken();
                    mTaskManager.deleteLogouttime();*/
                    //clear data
                    FuelDealerApplication.getInstance().clearApplicationData();

                    Intent intent = new Intent(mContext, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    mActivity.finish();
                }
            });

        } else {
        }

    }
}
