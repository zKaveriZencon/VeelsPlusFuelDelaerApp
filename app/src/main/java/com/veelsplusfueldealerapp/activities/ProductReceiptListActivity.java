package com.veelsplusfueldealerapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.R;
import com.veelsplusfueldealerapp.adapters.InventoryDetailsListAdapter;
import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.CommonTaskManager;
import com.veelsplusfueldealerapp.managers.GetDataService;
import com.veelsplusfueldealerapp.managers.RetrofitClientInstance;
import com.veelsplusfueldealerapp.managers.TimeoutAlertDialog;
import com.veelsplusfueldealerapp.models.InventoryDetailsListModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductReceiptListActivity extends BaseCompatActivity implements View.OnClickListener {
    private static final String TAG = "ProductReceiptListActiv";
    Chip chipSelectProduct;
    CommonTaskManager commonTaskManager;
    RecyclerView recyclerInventory;
    CommonCodeManager commonCodeManager;
    String[] details;
    ImageView imageViewNoData;
    private FloatingActionButton fabUpdateInventory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);

        initUI();
    }

    private void initUI() {

        View toolbar = findViewById(R.id.layout_toolbar_inventorylist);
        TextView textViewPageTitle = toolbar.findViewById(R.id.textview_page_title);
        textViewPageTitle.setText("Product Receipt");
        ImageView imageViewBack = toolbar.findViewById(R.id.imageview_back_arrow);
        imageViewBack.setOnClickListener(this);
        chipSelectProduct = toolbar.findViewById(R.id.chip_select_product);
        chipSelectProduct.setVisibility(View.VISIBLE);
        chipSelectProduct.setOnClickListener(this);

        commonTaskManager = new CommonTaskManager(ProductReceiptListActivity.this);
        commonCodeManager = new CommonCodeManager(ProductReceiptListActivity.this);

        fabUpdateInventory = findViewById(R.id.fab_add_inventory);
        fabUpdateInventory.setOnClickListener(this);

        recyclerInventory = findViewById(R.id.recyclerview_inventory_list);
        imageViewNoData = findViewById(R.id.imageview_no_data);

    }

    private void addInventory() {
        /*UpdateInventoryDialog updateInventoryDialog = new UpdateInventoryDialog(InventoryListActivity.this, InventoryListActivity.this);
        updateInventoryDialog.showSimpleDialog();*/

        Intent intent = new Intent(ProductReceiptListActivity.this, AddProductReceiptActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean result = commonTaskManager.compareDates(ProductReceiptListActivity.this);
        Log.d("home", "onResume: result : " + result);
        if (result) {
            Log.d("home", "onResume: result if: " + result);

            TimeoutAlertDialog timeoutAlertDialog = new TimeoutAlertDialog(ProductReceiptListActivity.this, ProductReceiptListActivity.this, "");
            timeoutAlertDialog.showSimpleDialog();
        } else {

            details = commonCodeManager.getDealerDetails(ProductReceiptListActivity.this);
            apiCallForGetAllFuelInventoryDetails(ProductReceiptListActivity.this,
                    details[0], "");
        }

    }

    private void apiCallForGetAllFuelInventoryDetails(final Context context, String fuelDealerStaffId,
                                                      final String product) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage(context.getResources().getString(R.string.loading_data));
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();


        Log.d(TAG, "apiCallForGetAllFuelInventoryDetails:product:  " + product);
        final List<InventoryDetailsListModel> inventoryDetailsListModelList = new ArrayList<>();

        GetDataService service = RetrofitClientInstance.getRetrofitInstance(context).create(GetDataService.class);
        Call<JsonObject> call = service.getTankFIDetailsByFuelDealerStaffId(fuelDealerStaffId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                final String jsonData = response.body().toString();
                Log.d(TAG, "onResponse:apiCallForGetAllFuelInventoryDetails :  " + jsonData);
                try {
                    JSONObject mainObject = new JSONObject(jsonData);
                    commonTaskManager.dismissProgressDialog(context, progressDialog);
                    final String status = mainObject.getString("status");
                    if (status.equals("OK")) {
                        JSONArray dataArray = mainObject.getJSONArray("data");
                        Log.d(TAG, "onResponse: data array length : " + dataArray.length());

                        for (int i = 0; i < dataArray.length(); i++) {
                            JSONObject duObject = dataArray.getJSONObject(i);
                            String openMitDate = duObject.getString("openDipReadDate");
                            String boughtQuantity = duObject.getString("boughtQuantity");
                            String tankNo = duObject.getString("tankNo");
                            String fuelTankRefuelId = duObject.getString("fuelTankRefuelId");
                            String decanStatus = duObject.getString("decanStatus");
                            String productCategory = duObject.getString("productCategory").toLowerCase().trim();
                            if (product.isEmpty()) {
                                Log.d(TAG, "onResponse: product is empty");
                                InventoryDetailsListModel inventoryDetailsListModel =
                                        new InventoryDetailsListModel(openMitDate, boughtQuantity, tankNo,
                                                fuelTankRefuelId, decanStatus, productCategory, fuelTankRefuelId);
                                inventoryDetailsListModelList.add(inventoryDetailsListModel);
                            } else {

                                if (productCategory.equals(product)) {
                                    Log.d(TAG, "onResponse: product is not empty");
                                    InventoryDetailsListModel inventoryDetailsListModel =
                                            new InventoryDetailsListModel
                                                    (openMitDate, boughtQuantity, tankNo,
                                                            fuelTankRefuelId, decanStatus, productCategory, fuelTankRefuelId);
                                    inventoryDetailsListModelList.add(inventoryDetailsListModel);

                                }

                            }

                        }
                        //newly added code for no mathcing product found
                        if (inventoryDetailsListModelList.size() == 0) {
                            imageViewNoData.setVisibility(View.VISIBLE);
                            Toast.makeText(ProductReceiptListActivity.this, "No Similar Data Found", Toast.LENGTH_SHORT).show();
                        }

                        fillInventoryListCards(inventoryDetailsListModelList);
                         /*else {
                            //no data found
                            imageViewNoData.setVisibility(View.VISIBLE);
                        }*/
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

    private void fillInventoryListCards(List<InventoryDetailsListModel> inventoryDetailsListModelList) {
        InventoryDetailsListAdapter inventoryAdapter = new InventoryDetailsListAdapter(ProductReceiptListActivity.this, inventoryDetailsListModelList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerInventory.setLayoutManager(mLayoutManager);
        recyclerInventory.setItemAnimator(new DefaultItemAnimator());
        inventoryAdapter.notifyDataSetChanged();
        recyclerInventory.setAdapter(inventoryAdapter);
    }

    private void selectProductType() {
        final String[] listItems = {"All Products", "Petrol", "Diesel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductReceiptListActivity.this);
        builder.setTitle("Choose item");

        final int checkedItem = 0; //this will checked the item when user open the dialog
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which != 0) {
                    chipSelectProduct.setText(listItems[which]);
                    apiCallForGetAllFuelInventoryDetails(ProductReceiptListActivity.this,
                            details[0], listItems[which].toLowerCase().trim());

                    chipSelectProduct.setTypeface(chipSelectProduct.getTypeface(), Typeface.BOLD);
                    commonTaskManager.sleepProcess(dialog);
                } else {
                    chipSelectProduct.setText("Select Product");
                    commonTaskManager.sleepProcess(dialog);

                }
                if (which == 0) {

                    details = commonCodeManager.getDealerDetails(ProductReceiptListActivity.this);
                    apiCallForGetAllFuelInventoryDetails(ProductReceiptListActivity.this,
                            details[0], "");
                }


            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.imageview_back_arrow:
                Intent intent = new Intent(ProductReceiptListActivity.this, HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.chip_select_product:
                selectProductType();
                break;
            case R.id.fab_add_inventory:
                addInventory();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductReceiptListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}