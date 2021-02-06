package com.veelsplusfueldealerapp.managers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.veelsplusfueldealerapp.R;


public class AboutAppDialog extends Dialog {
    public Context mContext;
    public Dialog dialog;
    private Activity mActivity;
    ImageView imageViewClose;
    private TextView textViewVersion;


    public AboutAppDialog(Activity activity, Context context) {
        super(activity);
        this.mActivity = activity;
        this.mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_about_app);

    }

}