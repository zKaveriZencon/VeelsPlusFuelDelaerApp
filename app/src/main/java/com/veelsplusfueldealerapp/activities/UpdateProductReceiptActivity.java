package com.veelsplusfueldealerapp.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.admin.SystemUpdateInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.UpdateFuelInventoryModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProductReceiptActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "UpdateProductReceiptAct";
    EditText editTextTankerNumber, editTextPreStock, editTextPreDipReading,
            editTextQuantityDecant, editTextPostStock, editTextPostDipReading, editTextReceivedQuantity,
            editTextDensityOnInvoice, editTextDensityRecorded, editTextProductCost, editTextPreWaterDipStock,
            editTextPreWaterDipScale, editTextPostWaterDipStock,
            editTextPostWaterDipScale, edittextInvoiceNumber;
    Button buttonSubmit;
    Spinner spinnerSelectTank;
    TextView textViewTitle, textViewProduct, textViewDate, textViewTime,
            textviewSelectedTank, textViewProductReceipt, textViewInvoiceDate;
    String fuelDealerStaffId, fuelInfraMapId;
    LinearLayout layoutParent;
    private CommonTaskManager commonTaskManager;
    private CommonCodeManager commonCodeManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_inventory);
        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(UpdateProductReceiptActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(UpdateProductReceiptActivity.this, UpdateProductReceiptActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {
            String[] cardDetails = commonCodeManager.getFuelnventoryOnCardClick(UpdateProductReceiptActivity.this);

            String forview = cardDetails[0];
            String fuelTankRefuelId = cardDetails[1];
            Log.d(TAG, "onResume: for view :" + cardDetails[0]);

            manageAllfieldsForUpdateInventory(forview);
            Log.d(TAG, "onResume: fuelTankRefuelId :" + fuelTankRefuelId);

            if (fuelTankRefuelId != null && !fuelTankRefuelId.isEmpty()) {
                apiCallForGetInventoryDetailsByFuelTankRefuelId
                        (UpdateProductReceiptActivity.this, fuelTankRefuelId);
            }
        }


    }

    private void manageAllfieldsForUpdateInventory(String forview) {
        if (forview.equals("forview")) {
            Log.d(TAG, "manageAllfieldsForUpdateInventory:forview :  " + forview);
            Snackbar snackbar = Snackbar
                    .make(layoutParent, "Activity Completed !", Snackbar.LENGTH_SHORT);
            View view = snackbar.getView();
            FrameLayout.LayoutParams params1 = (FrameLayout.LayoutParams) view.getLayoutParams();
            params1.gravity = Gravity.TOP;
            view.setLayoutParams(params1);
            view.setBackgroundColor(getResources().getColor(R.color.colorCommonGreen));
            snackbar.show();


            textViewProductReceipt.setVisibility(View.VISIBLE);
            textViewDate.setVisibility(View.INVISIBLE);
            textViewTime.setVisibility(View.INVISIBLE);

            //update title for view case
            textViewTitle.setText("Product Receipt Details");

            textviewSelectedTank.setVisibility(View.VISIBLE);
            editTextTankerNumber.setEnabled(false);
            editTextPreDipReading.setEnabled(false);
            editTextPreStock.setEnabled(false);
            editTextPreWaterDipStock.setEnabled(false);
            editTextQuantityDecant.setEnabled(false);
            editTextPreWaterDipScale.setEnabled(false);
            spinnerSelectTank.setVisibility(View.GONE);
            editTextPostDipReading.setEnabled(false);
            editTextPostStock.setEnabled(false);
            editTextPostWaterDipScale.setEnabled(false);
            editTextPostWaterDipStock.setEnabled(false);
            editTextReceivedQuantity.setEnabled(false);
            editTextDensityOnInvoice.setEnabled(false);
            editTextDensityRecorded.setEnabled(false);
            editTextProductCost.setEnabled(false);
            buttonSubmit.setEnabled(false);
        } else {

            textViewDate.setText("");
            textViewTime.setText("");

            String date = "Date : " + commonTaskManager.getCurrentDate()[0];
            String time = "Time : " + commonTaskManager.getCurrentTime();
            textViewDate.setText(date);
            textViewTime.setText(time);


            textViewTitle.setText("Update Product Receipt");
            buttonSubmit.setText("UPDATE");
            textviewSelectedTank.setVisibility(View.VISIBLE);
            editTextTankerNumber.setEnabled(false);
            editTextPreDipReading.setEnabled(false);
            editTextPreStock.setEnabled(false);
            editTextPreWaterDipStock.setEnabled(false);
            editTextQuantityDecant.setEnabled(false);
            editTextPreWaterDipScale.setEnabled(false);
            spinnerSelectTank.setVisibility(View.GONE);
        }


    }

    private void apiCallForGetInventoryDetailsByFuelTankRefuelId(final Context context, final String fuelTankRefuelId) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final List<String> inventoryDetails = new ArrayList<>();
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getInventoryDetailsByFuelTankRefuelId(fuelTankRefuelId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:ByFuelTankRefuelId :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);

                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject mainObj = dataArray.getJSONObject(i);
                            String vehicleNo = mainObj.getString("vehicleNo");
                            String tankNo = mainObj.getString("tankNo");
                            String productCategory = mainObj.getString("productCategory");
                            String openStockByDipReading = mainObj.getString("openStockByDipReading");
                            String openDipScaleReading = mainObj.getString("openDipScaleReading");
                            String boughtQuantity = mainObj.getString("boughtQuantity");//quantity decanted
                            String openWaterDipScale = mainObj.getString("openWaterDipScale");
                            String openWaterdipStock = mainObj.getString("openWaterdipStock");
                            String invoiceQuantity = mainObj.getString("invoiceQuantity");// received quantity
                            String invoiceDensity = mainObj.getString("invoiceDensity");
                            String densityRecorded = mainObj.getString("densityRecorded");
                            String buyingPrice = mainObj.getString("buyingPrice");
                            String closeStockByDipReading = mainObj.getString("closeStockByDipReading");
                            String closeDipScaleReading = mainObj.getString("closeDipScaleReading");
                            String closeWaterdipStock = mainObj.getString("closeWaterdipStock");
                            String closeWaterDipScale = mainObj.getString("closeWaterDipScale");
                            String invoiceDate = mainObj.getString("invoiceDate");
                            String invoiceNumber = mainObj.getString("invoiceNumber");
                            String closeDipReadDate = mainObj.getString("closeDipReadDate");


                            fuelInfraMapId = mainObj.getString("fuelInfraMapId");
                            fuelDealerStaffId = mainObj.getString("fuelDealerStaffId");


                            inventoryDetails.add(vehicleNo);
                            inventoryDetails.add(tankNo);
                            inventoryDetails.add(productCategory);
                            inventoryDetails.add(openStockByDipReading);
                            inventoryDetails.add(openDipScaleReading);
                            inventoryDetails.add(boughtQuantity);
                            inventoryDetails.add(openWaterDipScale);
                            inventoryDetails.add(openWaterdipStock);
                            inventoryDetails.add(invoiceQuantity);
                            inventoryDetails.add(invoiceDensity);
                            inventoryDetails.add(densityRecorded);
                            inventoryDetails.add(buyingPrice);
                            inventoryDetails.add(closeStockByDipReading);
                            inventoryDetails.add(closeDipScaleReading);
                            inventoryDetails.add(closeWaterdipStock);
                            inventoryDetails.add(closeWaterDipScale);
                            inventoryDetails.add(fuelInfraMapId);
                            inventoryDetails.add(fuelDealerStaffId);
                            inventoryDetails.add(invoiceDate);
                            inventoryDetails.add(invoiceNumber);
                            inventoryDetails.add(closeDipReadDate);

                            Log.d(TAG, "onResponse: closeDipReadDate : " + closeDipReadDate);
                        }
                        String[] ids = {fuelInfraMapId, fuelDealerStaffId};
                        commonCodeManager.saveInfraMapIdForInventory(context, ids);
                        updateInventory(inventoryDetails);

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

    private void updateInventory(List<String> inventoryDetailsList) {
        textviewSelectedTank.setText("");
        textviewSelectedTank.append("Selected Tank : " + inventoryDetailsList.get(1));
        //spinnerSelectTank.setPrompt(inventoryDetailsList.get(1));
        editTextTankerNumber.setText(inventoryDetailsList.get(0));
        textViewProduct.setText(inventoryDetailsList.get(2));
        editTextPreDipReading.setText(inventoryDetailsList.get(3));
        editTextPreStock.setText(inventoryDetailsList.get(4));
        editTextQuantityDecant.setText(inventoryDetailsList.get(5));
        editTextPreWaterDipScale.setText(inventoryDetailsList.get(6));
        editTextPreWaterDipStock.setText(inventoryDetailsList.get(7));
        editTextReceivedQuantity.setText(inventoryDetailsList.get(8));
        editTextDensityOnInvoice.setText(inventoryDetailsList.get(9));
        editTextDensityRecorded.setText(inventoryDetailsList.get(10));
        editTextProductCost.setText(inventoryDetailsList.get(11));
        editTextPostStock.setText(inventoryDetailsList.get(12));
        editTextPostDipReading.setText(inventoryDetailsList.get(13));
        editTextPostWaterDipScale.setText(inventoryDetailsList.get(14));
        editTextPostWaterDipStock.setText(inventoryDetailsList.get(15));
        edittextInvoiceNumber.setText(inventoryDetailsList.get(19));
        textViewInvoiceDate.setText(inventoryDetailsList.get(20));
       /* textViewDate.setVisibility(View.VISIBLE);
        textViewDate.setText("Date new: "+inventoryDetailsList.get(21));
*/
    }

    private void initUI() {
        commonTaskManager = new CommonTaskManager(UpdateProductReceiptActivity.this);
        commonCodeManager = new CommonCodeManager(UpdateProductReceiptActivity.this);


        View toolbar = findViewById(R.id.layout_toolbar_inventory);
        textViewTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewTitle.setText("Update Product Receipt");

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
        spinnerSelectTank = findViewById(R.id.spinner_select_tank_ui);
        textViewProduct = findViewById(R.id.textview_product_ui);
        textViewDate = findViewById(R.id.textview_date_label);
        textViewTime = findViewById(R.id.textview_time_label);
        textviewSelectedTank = findViewById(R.id.textview_selected_tank);
        textViewProductReceipt = findViewById(R.id.textview_note_pr);

        edittextInvoiceNumber = findViewById(R.id.edittext_invoice_no);
        textViewInvoiceDate = findViewById(R.id.textview_invoice_date);

        layoutParent = findViewById(R.id.layout_parent_pr);


        buttonSubmit = findViewById(R.id.button_submit_ui);
        buttonSubmit.setOnClickListener(this);
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

        String[] ids = commonCodeManager.getInfraMapIdForInventory(UpdateProductReceiptActivity.this);
        Log.d(TAG, "getAllTheParams: inframaid for update :" + ids[0]);

        String refuelId = commonCodeManager.getFuelTankRefuelId(UpdateProductReceiptActivity.this);
        /* UpdateFuelInventoryModel updateFuelInventoryModel = new
                UpdateFuelInventoryModel(ids[0],
                postDip, postStock, commonTaskManager.getCurrentDateTime(), postWaterScale,
                postWaterStock, densityInv, recceivedQuan, densityRec);

            */
        UpdateFuelInventoryModel updateFuelInventoryModel = new
                UpdateFuelInventoryModel(refuelId, postStock, postDip,
                commonTaskManager.getCurrentDateNew()
                , postWaterScale, postWaterStock, densityInv, recceivedQuan, densityRec,
                commonTaskManager.getCurrentTime(), "COMPLETE");


        apiCallForUpdateTankFuelInventory(UpdateProductReceiptActivity.this,
                updateFuelInventoryModel);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_submit_ui:
                if (areValidate()) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProductReceiptActivity.this);

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
                Intent intent = new Intent(UpdateProductReceiptActivity.this, ProductReceiptListActivity.class);
                startActivity(intent);
                break;

        }
    }

    private void apiCallForUpdateTankFuelInventory(final Context context, UpdateFuelInventoryModel updateFuelInventoryModel) {
        Log.d(TAG, "apiCallForUpdateTankFuelInventory: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateFuelTankInventoryDetails(updateFuelInventoryModel);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateTankFuelInventory :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
                        backToInventoryList(message);
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

    private void backToInventoryList(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        String refuelId = commonCodeManager.getFuelTankRefuelId(UpdateProductReceiptActivity.this);
        //apiCallForUpdateInventoryStatus(UpdateProductReceiptActivity.this,refuelId, "COMPLETE");
        Intent intent = new Intent(UpdateProductReceiptActivity.this, ProductReceiptListActivity.class);
        startActivity(intent);
    }

    private void apiCallForUpdateInventoryStatus(final Context context, String refuelId, String status) {
        Log.d(TAG, "apiCallForUpdateInventoryStatus: ");
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.updateStatusForFuelInventory(refuelId, status);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForUpdateInventoryStatus :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    final String message = mainObject.getString("msg");

                    if (status.equals("OK")) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateProductReceiptActivity.this, ProductReceiptListActivity.class);
        startActivity(intent);
    }

    private boolean areValidate() {
        boolean result = false;

        if (TextUtils.isEmpty(editTextPostStock.getText().toString())) {
            result = false;
            editTextPostStock.setError("Please Enter Post Stock Reading");
        } else if (TextUtils.isEmpty(editTextPostDipReading.getText().toString())) {
            result = false;
            editTextPostDipReading.setError("Please Enter Post Dip Reading");
        } else if (TextUtils.isEmpty(editTextPostWaterDipScale.getText().toString())) {
            result = false;
            editTextPostWaterDipScale.setError("Please Enter Post Water Dip Scale");
        } else if (TextUtils.isEmpty(editTextPostWaterDipStock.getText().toString())) {
            result = false;
            editTextPostWaterDipStock.setError("Please Enter Post Water Dip Stock");
        } else {
            result = true;
        }
        return result;

    }
}