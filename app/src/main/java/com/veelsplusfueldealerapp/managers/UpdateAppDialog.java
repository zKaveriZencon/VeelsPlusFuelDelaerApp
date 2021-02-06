package com.veelsplusfueldealerapp.managers;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.button.MaterialButton;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;

public class UpdateAppDialog {
    private static final String TAG = "UpdateAppDialog";
    public Context mContext;
    public Dialog dialog;
    private CommonTaskManager mTaskManager;

    public UpdateAppDialog(Context context) {
        this.mContext = context;
    }

    public void showSimpleDialog() {


        mTaskManager = new CommonTaskManager(mContext);

        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {


            View titleView = inflater.inflate(R.layout.layout_update_app, null);
            builder.setCustomTitle(titleView);
           /* TextView textViewTitle = titleView.findViewById(R.id.textview_title_dialog);
            TextView textviewMsg1 = titleView.findViewById(R.id.textview_message_dialog);
            TextView textViewUpdateMsg = titleView.findViewById(R.id.textview_message_update);

            textViewTitle.setText("Notification");
            textviewMsg1.setText("New update is now available!");
            textViewUpdateMsg.setText("New version of Veels Saathi is available.Please update to continue using this APP");
*/
            MaterialButton buttonOk = titleView.findViewById(R.id.button_update_app);
            MaterialButton buttonCancel = titleView.findViewById(R.id.button_cancel_update);

            final AlertDialog dialog = builder.create();
            dialog.setCancelable(false);
            dialog.show();
            buttonOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://play.google.com/store/apps/details?id=com.veelssathi";
                    // redirectStore(url);
                    final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        Log.d(TAG, "onClick: try part : ");
                    } catch (android.content.ActivityNotFoundException anfe) {
                        Log.d(TAG, "onClick: catch part : ");
                        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    Log.d(TAG, "onClick: before clear data.");
                    FuelDealerApplication.getInstance().clearApplicationData();

                    dialog.dismiss();

                }
            });

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
}