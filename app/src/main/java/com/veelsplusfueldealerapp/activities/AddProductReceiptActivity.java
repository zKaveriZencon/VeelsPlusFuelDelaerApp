package com.veelsplusfueldealerapp.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.DatabaseHelper;
import com.veelsplusfueldealerapp.managers.DecimalDigitsInputFilter;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.AddTankFuelInventoryModel;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductReceiptActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "AddFuelTankInventoryAct";
    EditText editTextTankerNumber, editTextPreStock, editTextPreDipReading,
            editTextQuantityDecant, editTextPostStock, editTextPostDipReading, editTextReceivedQuantity,
            editTextDensityOnInvoice, editTextDensityRecorded, editTextProductCost, editTextPreWaterDipStock,
            editTextPreWaterDipScale, editTextPostWaterDipStock, editTextPostWaterDipScale, editTextInvoiceNo;
    Button buttonSubmit;
    Spinner spinnerSelectTank;
    TextView textViewTitle, textViewProduct, textViewDate, textViewTime, textViewInvoiceDate;
    String spinnerSelectedTank;
    CommonCodeManager commonCodeManager;
    DatabaseHelper databaseHelper;
    ImageView imageViewSelectDate;
    private CommonTaskManager commonTaskManager;
    //private TextView spinnerErrorText;
    private int spinnerSelectedPosition;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);


        initUI();
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(AddProductReceiptActivity.this);
        commonCodeManager = new CommonCodeManager(AddProductReceiptActivity.this);
        databaseHelper = new DatabaseHelper(AddProductReceiptActivity.this);
        View toolbar = findViewById(R.id.layout_toolbar_inventory);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Add Product Receipt");

        editTextTankerNumber = findViewById(R.id.edittext_tanker_number);

        editTextPreStock = findViewById(R.id.edittext_before_dip_stock_reading);
        editTextPreDipReading = findViewById(R.id.edittext_before_dip);
        editTextQuantityDecant = findViewById(R.id.edittext_quantity_ui);
        editTextPostStock = findViewById(R.id.edittext_after_dip_stock);
        editTextPostDipReading = findViewById(R.id.edittext_after_dip_read);
        editTextReceivedQuantity = findViewById(R.id.edittext_received_qty);
        editTextDensityOnInvoice = findViewById(R.id.edittext_density_invoice);
        editTextDensityRecorded = findViewById(R.id.edittext_density_recorded);
        editTextProductCost = findViewById(R.id.edittext_product_cost);
        editTextPreWaterDipScale = findViewById(R.id.edittext_pre_water_scale);
        editTextPreWaterDipStock = findViewById(R.id.edittext_pre_water_stock);
        editTextPostWaterDipScale = findViewById(R.id.edittext_post_water_scale);
        editTextPostWaterDipStock = findViewById(R.id.edittext_post_water_stock);


        setTwoDigitFilters();


        textViewProduct = findViewById(R.id.textview_product_ui);
        textViewDate = findViewById(R.id.textview_date_label);
        textViewTime = findViewById(R.id.textview_time_label);
        imageViewSelectDate = findViewById(R.id.imageview_select_date_in);
        textViewInvoiceDate = findViewById(R.id.textview_invoice_date);
        editTextInvoiceNo = findViewById(R.id.edittext_invoice_no);

        spinnerSelectTank = findViewById(R.id.spinner_select_tank_ui);
        buttonSubmit = findViewById(R.id.button_submit_ui);
        buttonSubmit.setOnClickListener(this);
        imageViewSelectDate.setOnClickListener(this);


        imageViewSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddProductReceiptActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(datePickerDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                String sDay = "";
                if (day < 10) {
                    sDay = "0" + String.valueOf(day);
                } else {
                    sDay = String.valueOf(day);
                }

                String sMonth = "";
                if (month < 10) {
                    sMonth = "0" + String.valueOf(month);
                } else {
                    sMonth = String.valueOf(month);
                }
                String date = year + "-" + sMonth + "-" + sDay;


                textViewInvoiceDate.setText(date);
                Log.d(TAG, "onDateSet:textViewTentativeDate :  " + date);

            }
        };

    }

    private void setTwoDigitFilters() {

        editTextPreStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPreDipReading.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextQuantityDecant.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPostStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPostDipReading.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextReceivedQuantity.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextDensityOnInvoice.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextDensityRecorded.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextProductCost.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPreWaterDipScale.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPreWaterDipStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPostWaterDipScale.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});
        editTextPostWaterDipStock.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(20, 2)});


    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentTime1 = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        textViewDate.setText("");
        textViewTime.setText("");
        textViewDate.setText("Date : " + commonTaskManager.getCurrentDate()[0]);
        textViewTime.setText("Time : " + currentTime1);

        fillTankDataFromLocal();

        boolean result = commonTaskManager.compareDates(AddProductReceiptActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(AddProductReceiptActivity.this, AddProductReceiptActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String noinfra = commonCodeManager.getNoInfraDetailsMessage(AddProductReceiptActivity.this);
            if (noinfra.equals("noinfra")) {
                buttonSubmit.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductReceiptActivity.this);
                builder.setTitle("Note !");
                builder.setMessage("Unable to get proper Infra Setup data from server. Please check infra setup once or Logout from App and Login again.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                                Intent intent = new Intent(AddProductReceiptActivity.this, ProductReceiptListActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }


    private void getAllTheParams() {
        String tankerNumber = editTextTankerNumber.getText().toString();
        String preStockRead = editTextPreStock.getText().toString();
        String preDipRead = editTextPreDipReading.getText().toString();
        String quanDecant = editTextQuantityDecant.getText().toString();
        String postStock = editTextPostStock.getText().toString();
        String postDip = editTextPostDipReading.getText().toString();
        String recceivedQuan = editTextReceivedQuantity.getText().toString();
        String densityInv = editTextDensityOnInvoice.getText().toString();
        String densityRec = editTextDensityRecorded.getText().toString();
        String prodCost = editTextProductCost.getText().toString();
        String preWaterScale = editTextPreWaterDipScale.getText().toString();
        String preWaterStock = editTextPreWaterDipStock.getText().toString();
        String postWaterScale = editTextPostWaterDipScale.getText().toString();
        String postWaterStock = editTextPostWaterDipStock.getText().toString();
        String invoiceNumber = editTextInvoiceNo.getText().toString();

    /*    String[] paramsArray = {fuelInfraMapId, preDipRead, postDip, preStockRead,
                postStock,
                commonTaskManager.getCurrentDateTime(),
                commonTaskManager.getCurrentDateTime(),
                preWaterScale, postWaterScale,
                preWaterStock, postWaterStock,
                "", tankerNumber,
                quanDecant, densityInv, recceivedQuan,
                densityRec, prodCost};

        for (int i = 0; i < paramsArray.length; i++) {
            if (paramsArray[i].isEmpty()) {
                paramsArray[i] = "Empty";
                Log.d(TAG, "getAllTheParams: after check : " + paramsArray[i]);
            }
        }*/

        /*AddTankFuelInventoryModel addTankFuelInventoryModel =
                new AddTankFuelInventoryModel(paramsArray[0], paramsArray[1], paramsArray[2], paramsArray[3],
                        paramsArray[4], paramsArray[5], paramsArray[6], paramsArray[7], paramsArray[8],
                        paramsArray[9], paramsArray[10],
                        paramsArray[11], paramsArray[12], paramsArray[13],
                        paramsArray[14], paramsArray[15], paramsArray[16], paramsArray[17]);*/
       /* AddTankFuelInventoryModel addTankFuelInventoryModel =
                new AddTankFuelInventoryModel(fuelInfraMapId, "24", "TK2", preDipRead, postDip, preStockRead, postStock,
                        commonTaskManager.getCurrentDateTime(), commonTaskManager.getCurrentDateTime(), preWaterScale,
                        postWaterScale, preWaterStock, postWaterStock, tankerNumber, "", quanDecant,
                        densityInv, recceivedQuan, densityRec, prodCost);
*/
        String[] details = commonCodeManager.getDealerDetails(AddProductReceiptActivity.this);
        String spinnerSelectedTank = (String) spinnerSelectTank.getSelectedItem();
        List<String> prodDetails = databaseHelper.getProductNameAndInfraIdByTankId
                (spinnerSelectedTank);

        String dealerTankMap = details[2] + spinnerSelectedTank;


       /* AddTankFuelInventoryModel addTankFuelInventoryModel = new AddTankFuelInventoryModel
                (prodDetails.get(0), details[0], spinnerSelectedTank, preStockRead,
                        postStock, preDipRead, postDip, commonTaskManager.getCurrentDateTime(),
                        commonTaskManager.getCurrentDateTime(), preWaterScale, postWaterScale,
                        preWaterStock, postWaterStock, tankerNumber,
                        "", quanDecant, densityInv, recceivedQuan, densityRec,
                        prodCost, details[1], dealerTankMap);*/
        Log.d(TAG, "getAllTheParams: prodCost : " + prodCost);
        AddTankFuelInventoryModel addModel = null;

        Log.d(TAG, "getAllTheParams: postDip:" + postDip);
        Log.d(TAG, "getAllTheParams: postStock:" + postStock);
        Log.d(TAG, "getAllTheParams: postWaterScale:" + postWaterScale);
        Log.d(TAG, "getAllTheParams: postWaterStock:" + postWaterStock);

        if (postDip.isEmpty() || postStock.isEmpty() || postWaterScale.isEmpty()
                || postWaterStock.isEmpty()) {
            //for deacnsttaus  - false
            Log.d(TAG, "getAllTheParams: for false decan");
            Log.d(TAG, "getAllTheParams: postDip : " + postDip + ", postStock : " + postStock + ", postWaterScale =  " + postWaterScale + ", postWaterStock : " + postWaterStock);

            addModel = new AddTankFuelInventoryModel(prodDetails.get(0),
                    details[0], spinnerSelectedTank, preStockRead, postStock, preDipRead,
                    postDip, commonTaskManager.getCurrentDateNew(),
                    commonTaskManager.getCurrentDateNew(), preWaterScale,
                    postWaterScale, preWaterStock, postWaterStock, tankerNumber,
                    "", quanDecant, densityInv, recceivedQuan, densityRec,
                    prodCost, details[1], dealerTankMap, commonTaskManager.getCurrentTime(),
                    commonTaskManager.getCurrentTime(), "PARTIAL",
                    textViewInvoiceDate.getText().toString(), editTextInvoiceNo.getText().toString());
        } else {
            //decan status true
            Log.d(TAG, "getAllTheParams: for true decan");
            Log.d(TAG, "getAllTheParams: postDip : " + postDip + ", postStock : " +
                    "" + postStock + ", postWaterScale =  " + postWaterScale + "," +
                    " postWaterStock : " + postWaterStock);

            addModel = new AddTankFuelInventoryModel(prodDetails.get(0),
                    details[0], spinnerSelectedTank, preStockRead, postStock, preDipRead,
                    postDip, commonTaskManager.getCurrentDateNew(),
                    commonTaskManager.getCurrentDateNew(), preWaterScale,
                    postWaterScale, preWaterStock, postWaterStock, tankerNumber,
                    "", quanDecant, densityInv, recceivedQuan, densityRec,
                    prodCost, details[1], dealerTankMap, commonTaskManager.getCurrentTime(),
                    commonTaskManager.getCurrentTime(), "COMPLETE",
                    textViewInvoiceDate.getText().toString(), editTextInvoiceNo.getText().toString());
        }


        apiCallForAddTankFuelInventory(AddProductReceiptActivity.this, addModel);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_ui:
                commonTaskManager.hideKeyboard(AddProductReceiptActivity.this);

                Log.d(TAG, "onClick: ");
                if (areValidate()) {


                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProductReceiptActivity.this);

                    builder.setTitle("Confirm Details !");
                    builder.setMessage("Please confirm decantation details before submit by clicking 'Confirm' or 'Review' to review.")
                            .setCancelable(false)
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getAllTheParams();

                                }
                            })
                            .setNegativeButton("Review", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });
                    //Creating dialog box

                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.show();

                }


                break;
            case R.id.imageview_back_arrow:
                Intent intent = new Intent(AddProductReceiptActivity.this, ProductReceiptListActivity.class);
                startActivity(intent);
                break;


        }
    }

    private boolean areValidate() {
        boolean result = false;

        if (TextUtils.isEmpty(editTextTankerNumber.getText().toString())) {
            result = false;
            editTextTankerNumber.setError("Please Enter Tanker Number");
        } else if (spinnerSelectedTank.contains("Select")) {
            result = false;
            Toast.makeText(this, "Please Select Tank", Toast.LENGTH_SHORT).show();
            //spinnerErrorText.setError("Please Select Tank");
        } else if (TextUtils.isEmpty(editTextPreStock.getText().toString())) {
            result = false;
            editTextPreStock.setError("Please Enter Pre Stock Decantation");
        } else if (TextUtils.isEmpty(editTextPreDipReading.getText().toString())) {
            result = false;
            editTextPreDipReading.setError("Please Enter Pre Dip Reading");
        } else if (TextUtils.isEmpty(editTextQuantityDecant.getText().toString())) {
            result = false;
            editTextQuantityDecant.setError("Please Enter Quantity Decantation");
        } else if (TextUtils.isEmpty(editTextPreWaterDipScale.getText().toString())) {
            result = false;
            editTextPreWaterDipScale.setError("Please Enter Pre Water Dip Scale");
        } else if (TextUtils.isEmpty(editTextPreWaterDipStock.getText().toString())) {
            result = false;
            editTextPreWaterDipStock.setError("Please Enter Pre Water Dip Stock");
        } else if (TextUtils.isEmpty(editTextProductCost.getText().toString())) {
            result = false;
            editTextProductCost.setError("Please Enter Price");
        } else {
            result = true;
        }

        return result;
    }


    private void apiCallForAddTankFuelInventory(final Context context, AddTankFuelInventoryModel addTankFuelInventoryModel) {
        Log.d(TAG, "apiCallForAddTankFuelInventory: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.addTankFuelInventoryDetails(addTankFuelInventoryModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForAddTankFuelInventory :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        createInventoryCardDetails(message);
                    } else {
                        commonTaskManager.showToast(context, message);
                    }
                } catch (Exception e) {
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                commonTaskManager.dismissDialogWithToast(context, progressDialog, context.getResources().getString(R.string.unable_to_connect));

            }

        });
    }

    private void createInventoryCardDetails(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AddProductReceiptActivity.this, ProductReceiptListActivity.class);
        startActivity(intent);
    }

    private void fillTankDataFromLocal() {
        List<String> dusList = databaseHelper.getAllTanksFromInfraMapping();
        Log.d(TAG, "fillTankDataFromLocal: tanks size :" + dusList);
        String[] allTanks = new String[dusList.size()];
        for (int i = 0; i < dusList.size(); i++) {
            allTanks[i] = dusList.get(i);
        }
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(AddProductReceiptActivity.this, android.R.layout.simple_spinner_item, allTanks);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectTank.setAdapter(arrayAdapter1);
        spinnerSelectTank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerSelectedTank = (String) spinnerSelectTank.getSelectedItem();
                if (spinnerSelectedTank.contains("Select Tank")) {

                } else {
                    List<String> prodDetails = databaseHelper.getProductNameAndInfraIdByTankId(spinnerSelectedTank);
                    textViewProduct.setText(prodDetails.get(1));
                    //spinnerErrorText = (TextView) spinnerSelectTank.getSelectedView();
                    spinnerSelectedPosition = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddProductReceiptActivity.this, ProductReceiptListActivity.class);
        startActivity(intent);
    }
}