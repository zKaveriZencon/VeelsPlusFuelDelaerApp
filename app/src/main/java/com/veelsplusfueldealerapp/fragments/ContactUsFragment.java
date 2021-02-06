package com.veelsplusfueldealerapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.models.ContactUsModel;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsFragment extends Fragment {
    private static final String TAG = "ContactUsFragment";
    private View view;
    private MaterialButton buttonSendReq;
    private CommonTaskManager commonTaskManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_contact_us_frag, container, false);
        initUI();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        return view;
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(getActivity());

        buttonSendReq = view.findViewById(R.id.button_submit_req);
        buttonSendReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactUsModel contactUsModel = new ContactUsModel();

                apiCallForContactUs(getActivity(), contactUsModel);
            }
        });
    }

    public void apiCallForContactUs(final Context context, final ContactUsModel contactUsModel) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity(), R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.contactUs(contactUsModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse: response : " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();
                    final String message = mainObject.getString("msg");

                    if (!status.isEmpty() && status.equals("ok")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                    } else if (!status.isEmpty() && status.equals("error")) {
                        commonTaskManager.dismissProgressDialog(context, progressDialog);

                    }

                } catch (Exception e) {
                    Log.d(TAG, "onFailure: e: " + e.getLocalizedMessage());
                    e.printStackTrace();
                    commonTaskManager.dismissProgressDialog(context, progressDialog);


                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: t: " + t.getLocalizedMessage());
                t.printStackTrace();

            }

        });
    }

}