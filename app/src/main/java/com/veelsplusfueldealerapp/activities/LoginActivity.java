package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.APIHandlerManager;
import com.veelsplusfueldealerapp.models.NewCreditRequestModel;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    String check = "";
    CommonTaskManager commonTaskManager;
    CommonCodeManager commonCodeManager;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewForgotPass;
    private Button buttonLogin;
    private ImageView imageViewShowHide;
    private Spinner spinnerSelectLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_actvity);

        initUI();

    }

    private void initUI() {
        editTextUsername = findViewById(R.id.edittext_username);
        editTextPassword = findViewById(R.id.edittext_password1);
        buttonLogin = findViewById(R.id.button_login);
        textViewForgotPass = findViewById(R.id.textview_forgot_pass);
        imageViewShowHide = findViewById(R.id.imageview_show_hide_pass);
        spinnerSelectLang = findViewById(R.id.spinner_select_language);

        commonTaskManager = new CommonTaskManager(LoginActivity.this);
        commonCodeManager = new CommonCodeManager(LoginActivity.this);


        buttonLogin.setOnClickListener(this);
        textViewForgotPass.setOnClickListener(this);
        imageViewShowHide.setOnClickListener(this);


        ArrayAdapter arrayAdapter1 = new ArrayAdapter(LoginActivity.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.select_language));
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectLang.setAdapter(arrayAdapter1);

        spinnerSelectLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  String selectedLang = (String) spinnerSelectLang.getSelectedItem();
                String selectedLang = "";
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        selectedLang = "en";
                        break;
                    case 2:
                        selectedLang = "hi";

                        break;
                    default:
                        selectedLang = "en";
                        break;

                }
                commonCodeManager.saveSelectedLanguage(LoginActivity.this, selectedLang);
                changeLocale();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:
                commonTaskManager.hideKeyboard(LoginActivity.this);
                if (areValidate()) {
                    String username = editTextUsername.getText().toString();
                    String password = editTextPassword.getText().toString();

                    APIHandlerManager apiHandlerManager = new APIHandlerManager(LoginActivity.this);
                    apiHandlerManager.apiCallForLogin(LoginActivity.this, username, password);
                }

                break;
            case R.id.textview_forgot_pass:

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

                break;

            case R.id.imageview_show_hide_pass:
                if (editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    imageViewShowHide.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);

                    //Show Password
                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    imageViewShowHide.setImageResource(R.drawable.hide);

                    //Hide Password
                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
               /* if (editTextPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide.setImageDrawable(getDrawable(R.drawable.ic_remove_red_eye_black_24dp));
                    }

                    editTextPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        imageViewShowHide.setImageDrawable(getDrawable(R.drawable.hide));
                    }

                    editTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }*/
                break;

        }
    }

    private void selectLanguageChooser() {
        final String[] listItems = {"None", "en", "hi"};

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Select Language");

        final int checkedItem = 0;
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    commonCodeManager.saveSelectedLanguage(LoginActivity.this, listItems[which]);
                    textViewForgotPass.setText(listItems[which]);
                    changeLocale();

                    // textViewForgotPass.setTypeface(textViewForgotPass.getTypeface(), Typeface.BOLD);

                } else {
                    textViewForgotPass.setText("Select Language");
                    commonTaskManager.sleepProcess(dialog);

                }


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finishAffinity();
    }

    private boolean areValidate() {
        boolean result = false;
        if (TextUtils.isEmpty(editTextUsername.getText().toString())) {
            editTextUsername.setError(getResources().getString(R.string.enter_phone_number));
            result = false;
        } else if (TextUtils.isEmpty(editTextPassword.getText().toString())) {
            result = false;
            editTextPassword.setError(getResources().getString(R.string.enter_password));

        } else {
            result = true;
        }

        return result;
    }

    public void changeLocale() {
        String selectedLanguage = commonCodeManager.getSelectedLanguage(LoginActivity.this);
        if (selectedLanguage.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(selectedLanguage);
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());//Update the config
        updateTexts();
    }

    private void updateTexts() {
        editTextUsername.setHint(getResources().getString(R.string.phone));
        editTextPassword.setHint(getResources().getString(R.string.password));
        buttonLogin.setText(getResources().getString(R.string.login));
        textViewForgotPass.setText(getResources().getString(R.string.forgot_pass));

    }

}