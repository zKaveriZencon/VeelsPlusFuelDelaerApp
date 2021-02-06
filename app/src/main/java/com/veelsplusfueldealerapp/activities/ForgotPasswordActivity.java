package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitInstanceWithoutToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "ForgotPasswordActivity";
    private EditText editTextMobileNumber, editTextEnterOtp, editTextEnterPassword, editTextReenterPass;
    private MaterialButton buttonGetOtp, buttonSubmitOtp, buttonSubmitPass;
    private LinearLayout layoutOtpDetails, layoutChangePassword, layoutMobileNumber;
    private boolean result = false;
    private CommonCodeManager mCommonCodeManager;
    private ImageView imageViewShowHide1, imageViewShowHide2;
    private LinearLayout layoutForgotPass;
    private Locale myLocale;
    private TextView labelChangePass, labelNewPass, labelReenterPass, labelEnterOtp, labelMobNo;
    private CommonTaskManager mTaskManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mCommonCodeManager = new CommonCodeManager();
        mTaskManager = new CommonTaskManager(ForgotPasswordActivity.this);

        initUI();

    }

    private void initUI() {
        editTextMobileNumber = findViewById(R.id.edittext_phone_pass);
        editTextEnterOtp = findViewById(R.id.edittext_enter_otp_pass);
        editTextEnterPassword = findViewById(R.id.edittext_password_change);
        editTextReenterPass = findViewById(R.id.edittext_reenter_pass);
        buttonGetOtp = findViewById(R.id.button_getotp_forget);
        buttonSubmitOtp = findViewById(R.id.button_submitotp_forget);
        buttonSubmitPass = findViewById(R.id.button_submit_forget);
        layoutOtpDetails = findViewById(R.id.layout_otp_details);
        layoutChangePassword = findViewById(R.id.layout_change_password);
        layoutMobileNumber = findViewById(R.id.layout_mobile_number);
        imageViewShowHide1 = findViewById(R.id.imageview_show_hide_pass1);
        imageViewShowHide2 = findViewById(R.id.imageview_show_hide_pass2);
        layoutForgotPass = findViewById(R.id.layout_forgot_pass);


        //labels
        labelChangePass = findViewById(R.id.label_change_pass);
        labelEnterOtp = findViewById(R.id.label_otp_f);
        labelNewPass = findViewById(R.id.label_new_pass);
        labelReenterPass = findViewById(R.id.label_reenter_pass);
        labelMobNo = findViewById(R.id.label_mobile_number);


        layoutForgotPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTaskManager.hideKeyboard(ForgotPasswordActivity.this);
                return false;
            }
        });
        buttonSubmitPass.setOnClickListener(this);
        buttonSubmitOtp.setOnClickListener(this);
        buttonGetOtp.setOnClickListener(this);
        imageViewShowHide1.setOnClickListener(this);
        imageViewShowHide2.setOnClickListener(this);


      /*
          private CommonTaskManager mTaskManager;

               mTaskManager = new CommonTaskManager(LoginActivity.this);

       if (mTaskManager.haveNetworkConnection(RegisterDetailsActivity.this)) {

        } else {
            mTaskManager.showNetAlertDialog(RegisterDetailsActivity.this);
        }
        */
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_getotp_forget:
                mTaskManager.hideKeyboard(ForgotPasswordActivity.this);

                layoutOtpDetails.setVisibility(View.VISIBLE);
                String mobileNumber = editTextMobileNumber.getText().toString();
                if (TextUtils.isEmpty(mobileNumber)) {
                    editTextMobileNumber.setError(getResources().getString(R.string.please_enter_phone_number));
                    result = false;
                    String phone = editTextMobileNumber.getText().toString();
                    int phoneLength = phone.length();
                    if (phoneLength != 10)
                        editTextMobileNumber.setError(getResources().getString(R.string.phone_length));
                    result = false;
                } else {
                    result = true;

                    apiCallForFindPhoneNoForPassword(ForgotPasswordActivity.this, mobileNumber);
                }

                break;


            case R.id.button_submitotp_forget:
                mTaskManager.hideKeyboard(ForgotPasswordActivity.this);

                String enteredOtp = editTextEnterOtp.getText().toString();
                if (TextUtils.isEmpty(enteredOtp)) {
                    editTextEnterOtp.setError(getResources().getString(R.string.please_enter_otp));
                } else {
                    apiCallForCompareOTP(ForgotPasswordActivity.this, editTextMobileNumber.getText().toString(),
                            editTextEnterOtp.getText().toString());
                }
                break;

            case R.id.button_submit_forget:
                mTaskManager.hideKeyboard(ForgotPasswordActivity.this);

                String password = editTextEnterPassword.getText().toString();
                String reEnterPass = editTextReenterPass.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    editTextEnterPassword.setError(getResources().getString(R.string.please_enter_password));
                    result = false;

                } else if (password.length() != 6) {
                    editTextEnterPassword.setError(getResources().getString(R.string.password_length));
                    result = false;

                } else if (TextUtils.isEmpty(reEnterPass)) {
                    editTextReenterPass.setError(getResources().getString(R.string.please_reenter_pass));
                    result = false;

                } else if (!password.equals(reEnterPass)) {
                    editTextReenterPass.setError(getResources().getString(R.string.not_matching));
                    result = false;

                } else {
                    result = true;
                    apiCallForChangePassword(ForgotPasswordActivity.this);
                }

                break;

            case R.id.imageview_show_hide_pass1:

                if (editTextEnterPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide1.setImageDrawable(getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                    }
                    //Show Password
                    editTextEnterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide1.setImageDrawable(getDrawable(R.drawable.hide));
                    }

                    //Hide Password
                    editTextEnterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                break;

            case R.id.imageview_show_hide_pass2:
                if (editTextReenterPass.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide2.setImageDrawable(getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                    }
                    //Show Password
                    editTextReenterPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide2.setImageDrawable(getDrawable(R.drawable.hide));
                    }

                    //Hide Password
                    editTextReenterPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
                break;


        }
    }

    public void apiCallForFindPhoneNoForPassword(final Context context, String phoneNo) {

        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.findPhoneNumberForPassword(phoneNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final String jsonData = response.body().toString();
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();

                    if (!status.isEmpty() && status.equals("ok")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        if (dataArray.length() != 0) {
                            JSONObject jsonObject = dataArray.getJSONObject(0);
                            final String personId = jsonObject.getString("personId");
                            mCommonCodeManager.savePersonIDForForgotPass(context, personId);

                            apiCallForSendOtpRequest(context, editTextMobileNumber.getText().toString(), personId);
                        }
                    }


                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();

            }
        });
    }

    public void apiCallForSendOtpRequest(final Context context, final String phoneNo, final String personId) {

        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.sendOtpRequest(phoneNo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final String jsonData = response.body().toString();
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();

                    if (!status.isEmpty() && status.equals("ok")) {
                        Handler handler = new Handler(context.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                editTextMobileNumber.setEnabled(false);
                                buttonGetOtp.setEnabled(false);

                            }
                        });

                    }


                } catch (Exception e) {
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void goToChangePassword(Context context, String status) {
        if (!status.isEmpty() && status.equals("ok")) {
            //visibility of views

            layoutMobileNumber.setVisibility(View.GONE);
            layoutOtpDetails.setVisibility(View.VISIBLE);
            layoutChangePassword.setVisibility(View.VISIBLE);
            editTextEnterOtp.setEnabled(false);
            buttonSubmitOtp.setEnabled(false);
        } else {
            Toast.makeText(context, getResources().getString(R.string.otp_incorrect), Toast.LENGTH_SHORT).show();
        }

    }

    public void apiCallForChangePassword(final Context context) {

        String personId = mCommonCodeManager.getPersonIDForForgotPass(context);

        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updatePassword(personId, editTextEnterPassword.getText().toString());
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final String jsonData = response.body().toString();
                    Log.d(TAG, "onResponse: jsonData :" + jsonData);
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();
                    goToLoginPage(status);

                } catch (Exception e) {
                    Log.d(TAG, "onResponse: e : " + e.getLocalizedMessage());
                    e.printStackTrace();

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                t.printStackTrace();
            }

        });
    }

    private void goToLoginPage(String status) {
        if (!status.isEmpty() && status.equals("ok")) {
            Toast.makeText(this, R.string.password_changed_done, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {

        }
    }

    public void apiCallForCompareOTP(final Context context, final String phoneNo, String OTP) {

        GetDataService service = RetrofitInstanceWithoutToken.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.compareOTP(phoneNo, OTP);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try {
                    final String jsonData = response.body().toString();
                    JSONObject mainObject = new JSONObject(jsonData);
                    final String status = mainObject.getString("status").trim().toLowerCase();
                    goToChangePassword(context, status);

                } catch (Exception e) {

                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.printStackTrace();
                CommonCodeManager commonCodeManager = new CommonCodeManager();
                mTaskManager.showToast(context, getResources().getString(R.string.unable_to_connect));
            }
        });
    }


}
