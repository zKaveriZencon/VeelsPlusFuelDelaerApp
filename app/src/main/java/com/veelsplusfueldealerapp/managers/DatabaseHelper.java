package com.veelsplusfueldealerapp.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.veelsplusfueldealerapp.models.DailySalesInfoModel;
import com.veelsplusfueldealerapp.models.FuelTerminalModel;
import com.veelsplusfueldealerapp.models.InfraMappingDbModel;
import com.veelsplusfueldealerapp.models.OperatorDailyWorkListModel;
import com.veelsplusfueldealerapp.models.OperatorInfoFragModel;
import com.veelsplusfueldealerapp.models.PaymentModelLocal;
import com.veelsplusfueldealerapp.models.SalesInfoCardModel;
import com.veelsplusfueldealerapp.models.ShiftWiseCreditModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FuelDealer";
    private static final String TABLE_FUEL_INFRA_MAPPING = "fuelInfraMapping";
    private static final String KEY_PK = "tankDuNzMap";
    private static final String KEY_TANK_NO = "tankNo";
    private static final String KEY_DU = "duNo";
    private static final String KEY_NOZZLE = "nozzleNo";
    private static final String KEY_PRODUCT_ID = "productId";
    private static final String KEY_BRAND_NAME = "prodBrandName";
    private static final String KEY_PROD_CATEGORY = "prodCategory";
    private static final String KEY_PROD_NAME = "prodName";
    private static final String KEY_PROD_CODE = "prodCode";
    private static final String KEY_MAP_STATUS = "mappingStatus";
    private static final String KEY_FUEL_DEALER_ID = "fuelDealerId";
    private static final String KEY_FUELINFRAMAP_ID = "fuelInfraMapId";
    //for fuel terminal
    private static final String TABLE_FUEL_TERMINAL = "fuelTerminal";
    private static final String fuelTerminalsId = "fuelTerminalsId";
    private static final String terminalName = "terminalName";
    private static final String attachedBankName = "attachedBankName";
    private static final String terminalType = "terminalType";

    //for daily sales
    private static final String TABLE_SALES = "dailysales";
    private static final String sales_pk = "sales_pk";
    private static final String batchId = "batchId";
    private static final String paymentType = "paymentType";
    private static final String amount = "amount";
    private static final String transId = "transId";


    //for fuel infocardsales
    private static final String TABLE_INFO_CARD = "infocardsales";
    private static final String infocard_pk = "infocard_pk";
    private static final String product = "product";
    private static final String meter_sale = "meter_sale";
    private static final String total_amt = "total_amt";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_FUEL_INFRA_TABLE = "CREATE TABLE " + TABLE_FUEL_INFRA_MAPPING +
                " (" + KEY_PK + " TEXT PRIMARY KEY, " + KEY_TANK_NO + " TEXT, " + KEY_DU +
                " TEXT, " + KEY_NOZZLE + " TEXT, " + KEY_PRODUCT_ID + " TEXT, " +
                KEY_BRAND_NAME + " TEXT, " + KEY_PROD_CATEGORY + " TEXT, " + KEY_PROD_NAME
                + " TEXT, " + KEY_PROD_CODE + " TEXT, " + KEY_MAP_STATUS + " TEXT," + KEY_FUEL_DEALER_ID + " TEXT, " + KEY_FUELINFRAMAP_ID + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_FUEL_INFRA_TABLE);

        String CREATE_FUEL_TERMINAL = "CREATE TABLE " + TABLE_FUEL_TERMINAL +
                " (" + fuelTerminalsId + " TEXT PRIMARY KEY, " + terminalName
                + " TEXT, " + attachedBankName +
                " TEXT, " + terminalType + " TEXT)";
        sqLiteDatabase.execSQL(CREATE_FUEL_TERMINAL);


        String CREATE_DAILY_SALES = "CREATE TABLE " + TABLE_SALES +
                " (" + sales_pk + " INTEGER PRIMARY KEY AUTOINCREMENT, " + batchId + " TEXT, " + paymentType
                + " TEXT, " + amount +
                " TEXT, " + transId + " TEXT)";

        sqLiteDatabase.execSQL(CREATE_DAILY_SALES);


        String CREATE_INFO_CARD = "CREATE TABLE " + TABLE_INFO_CARD +
                " (" + infocard_pk + " INTEGER PRIMARY KEY AUTOINCREMENT, " + product +
                " TEXT, " + meter_sale
                + " TEXT, " + total_amt +
                " TEXT)";
        sqLiteDatabase.execSQL(CREATE_INFO_CARD);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_INFRA_MAPPING);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FUEL_TERMINAL);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_INFO_CARD);


        onCreate(sqLiteDatabase);
    }

    public long addInfraMappingDetails(List<InfraMappingDbModel> infraMappingDbModelList) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = 0;
        try {
            for (int i = 0; i < infraMappingDbModelList.size(); i++) {
                InfraMappingDbModel infraMappingDbModel = infraMappingDbModelList.get(i);

                ContentValues values = new ContentValues();

                values.put(KEY_PK, infraMappingDbModel.getTankDuNzMap());
                values.put(KEY_TANK_NO, infraMappingDbModel.getTankNo());
                values.put(KEY_DU, infraMappingDbModel.getDuNo());
                values.put(KEY_NOZZLE, infraMappingDbModel.getNozzleNo());
                values.put(KEY_PRODUCT_ID, infraMappingDbModel.getProductId());
                values.put(KEY_BRAND_NAME, infraMappingDbModel.getProdBrandName());
                values.put(KEY_PROD_CATEGORY, infraMappingDbModel.getProdCategory());
                values.put(KEY_PROD_NAME, infraMappingDbModel.getProdName());
                values.put(KEY_PROD_CODE, infraMappingDbModel.getProdCode());
                values.put(KEY_MAP_STATUS, infraMappingDbModel.getMappingStatus());
                values.put(KEY_FUEL_DEALER_ID, infraMappingDbModel.getFuelDealerId());
                values.put(KEY_FUELINFRAMAP_ID, infraMappingDbModel.getFuelInfraMapId());


                result = db.insert(TABLE_FUEL_INFRA_MAPPING, null, values);
                Log.d(TAG, "addInfraMappingDetails: insert result : " + result);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }
        return result;
    }

    public List<String> getAllPumpsFromInfraMapping() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> allTanks = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT DISTINCT duNo FROM fuelInfraMapping";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    allTanks.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return allTanks;
    }

    public List<String> getAllNozzlesFromInfraMapping(String duNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> allNozzles = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where duNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{duNo});

            if (cursor.moveToFirst()) {
                do {

                    allNozzles.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return allNozzles;
    }

    public List<String> getAllTanksFromInfraMapping() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> allTanks = new ArrayList<>();
        allTanks.add("Select Tank");
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT DISTINCT tankNo FROM fuelInfraMapping";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    allTanks.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
            for (int i = 0; i < allTanks.size(); i++) {
            }
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return allTanks;
    }


    public String getProductNameByTankId(String tankNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String productName = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where tankNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{tankNo});

            if (cursor.moveToFirst()) {
                do {

                    productName = (cursor.getString(6));
                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return productName;
    }

    public List<String> getProductNameAndInfraIdByTankId(String tankNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String productName = "";
        String infraMapId = "";
        List<String> detailsList = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT fuelInfraMapId,prodCategory FROM fuelInfraMapping where tankNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{tankNo});

            if (cursor.moveToFirst()) {
                do {

                    infraMapId = (cursor.getString(0));
                    productName = (cursor.getString(1));
                    detailsList.add(infraMapId);
                    detailsList.add(productName);


                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }

        return detailsList;
    }

    public List<String> getNozzleTankAndPkByDuNo(String duNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> detailsList = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where duNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{duNo});

            if (cursor.moveToFirst()) {
                do {
                    String dunzmap = (cursor.getString(0));
                    String tankNo = (cursor.getString(1));
                    String nozzleNo = (cursor.getString(3));
                    String prodCategory = (cursor.getString(6));
                    String dealerId = (cursor.getString(10));
                    String inframapId = (cursor.getString(11));
                    detailsList.add(dunzmap);
                    detailsList.add(tankNo);
                    detailsList.add(nozzleNo);
                    detailsList.add(prodCategory);
                    detailsList.add(dealerId);
                    detailsList.add(inframapId);

                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return detailsList;
    }

    public String getTankByPumpAndNozzle(String duNo, String nozNo) {
        Log.d(TAG, "getTankByPumpAndNozzle:duNo :  " + duNo);
        Log.d(TAG, "getTankByPumpAndNozzle:nozNo :  " + nozNo);

        SQLiteDatabase db = null;
        Cursor cursor = null;
        String tankNo = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT tankNo FROM fuelInfraMapping where duNo = ? and nozzleNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{duNo, nozNo});

            if (cursor.moveToFirst()) {
                do {
                    tankNo = (cursor.getString(0));

                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return tankNo;
    }

    public String getProductByTank(String tankNo) {
        Log.d(TAG, "getProductByTank:tankNo :  " + tankNo);

        SQLiteDatabase db = null;
        Cursor cursor = null;
        String prodCategory = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT prodCategory FROM fuelInfraMapping where tankNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{tankNo});

            if (cursor.moveToFirst()) {
                do {
                    prodCategory = (cursor.getString(0));

                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return prodCategory;
    }


    public String getInfraMapIdByTankNo(String tankNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String infraMapId = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where tankNo = ? limit 1";
            cursor = db.rawQuery(selectQuery, new String[]{tankNo});

            if (cursor.moveToFirst()) {
                do {

                    infraMapId = (cursor.getString(11));
                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return infraMapId;
    }

    public long saveFuelTerminals(List<FuelTerminalModel> fuelTerminalsList) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = 0;
        try {
            for (int i = 0; i < fuelTerminalsList.size(); i++) {
                FuelTerminalModel fuelTerminalModel = fuelTerminalsList.get(i);

                ContentValues values = new ContentValues();

                values.put(fuelTerminalsId, fuelTerminalModel.getFuelTerminalsId());
                values.put(terminalName, fuelTerminalModel.getTerminalName());
                values.put(attachedBankName, fuelTerminalModel.getAttachedBankName());
                values.put(terminalType, fuelTerminalModel.getTerminalType());

                result = db.insert(TABLE_FUEL_TERMINAL, null, values);
                Log.d(TAG, "saveFuelTerminals: insert result : " + result);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }
        return result;
    }

    public List<FuelTerminalModel> getAllTerminals() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<FuelTerminalModel> terminalsList = new ArrayList<>();
        FuelTerminalModel fuelTerminalModel1 = new FuelTerminalModel("",
                "Select Device", "", "");
        terminalsList.add(fuelTerminalModel1);
        try {
            db = this.getWritableDatabase();
            String selectQuery = "SELECT * FROM fuelTerminal where terminalName is not null AND TRIM(terminalName,' ') != ''";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    FuelTerminalModel fuelTerminalModel = new FuelTerminalModel();
                    fuelTerminalModel.setFuelTerminalsId(cursor.getString(0));
                    fuelTerminalModel.setTerminalName(cursor.getString(1));
                    fuelTerminalModel.setAttachedBankName(cursor.getString(2));
                    fuelTerminalModel.setTerminalType(cursor.getString(3));

                    terminalsList.add(fuelTerminalModel);
                } while (cursor.moveToNext());
            }

            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return terminalsList;
    }

    public List<String> getAllTerminalsType(String selctedTerminal) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String terminalType = "";
        String transId = "";
        List<String> details = new ArrayList<>();

        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelTerminal where terminalName = ?";


            cursor = db.rawQuery(selectQuery, new String[]{selctedTerminal});

            if (cursor.moveToFirst()) {
                do {
                    transId = cursor.getString(0);
                    terminalType = (cursor.getString(3));
                    details.add(transId);
                    details.add(terminalType);

                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return details;
    }

    public long addPaymentDetails(PaymentModelLocal paymentModelLocal) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = 0;
        try {
            ContentValues values = new ContentValues();

            values.put(batchId, paymentModelLocal.getBatchId());
            values.put(paymentType, paymentModelLocal.getPaymentType());
            values.put(amount, paymentModelLocal.getAmount());
            values.put(transId, paymentModelLocal.getTransId());

            result = db.insert(TABLE_SALES, null, values);
            Log.d(TAG, "addPaymentDetails: insert result : " + result);


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }
        return result;
    }

    public String getPaymentDetails(String batchId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String tallyAmount = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "select sum(amount) from dailysales where batchId = '" + batchId + "' group by batchId;";
            Log.d(TAG, "getPaymentDetails:selectQuery : " + selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    tallyAmount = cursor.getString(0);

                } while (cursor.moveToNext());
            }
            Log.d(TAG, "getPaymentDetails:tallyAmount :  " + tallyAmount);
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return tallyAmount;
    }

    public void deleteDailySales() {
        Log.d(TAG, "deleteDailySales: ");
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            db.execSQL("DELETE FROM dailysales");
            db.execSQL("VACUUM");

            db.close();
        } catch (Exception e) {
        }
    }

    public String getDetails() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String tallyAmount = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "select * from dailysales";
            Log.d(TAG, "getPaymentDetails:selectQuery : " + selectQuery);
            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    tallyAmount = cursor.getString(0);

                } while (cursor.moveToNext());
            }
            Log.d(TAG, "getPaymentDetails:tallyAmount :  " + tallyAmount);
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return tallyAmount;
    }

    public List<String> getFuelType() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String fuelType = "";
        List<String> details = new ArrayList<>();
        details.add("Select Fuel Type");
        // String[] deatilsArr = new String[10];

        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT DISTINCT prodCategory FROM fuelInfraMapping";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    fuelType = (cursor.getString(0));
                    details.add(fuelType);
                   /* deatilsArr[i] = fuelType;
                    i = i + 1;*/

                } while (cursor.moveToNext());
            }
            cursor.close();

            Log.d(TAG, "getFuelType: fuel type : " + fuelType);

            db.close();
        } catch (Exception e) {
        }
        return details;
    }

    public List<ShiftWiseCreditModel> getWholeProductName() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String fuelType = "";
        String prodName = "";
        List<ShiftWiseCreditModel> details = new ArrayList<>();
        ShiftWiseCreditModel shiftWiseCreditModel = new ShiftWiseCreditModel("", "", "Select Product");
        details.add(shiftWiseCreditModel);
        // String[] deatilsArr = new String[10];

        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT DISTINCT prodCategory,prodName FROM fuelInfraMapping";

            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    fuelType = (cursor.getString(0));
                    prodName = (cursor.getString(1));
                    String product = fuelType + "-" + prodName;
                    shiftWiseCreditModel = new ShiftWiseCreditModel(fuelType, prodName, product);

                    details.add(shiftWiseCreditModel);
                   /* deatilsArr[i] = fuelType;
                    i = i + 1;*/

                } while (cursor.moveToNext());
            }
            cursor.close();

            Log.d(TAG, "getFuelType: fuel type : " + fuelType);

            db.close();
        } catch (Exception e) {
        }
        return details;
    }

    public List<String> getProductNameByFuelType(String fuelType) {
        Log.d(TAG, "getProductNameByFuelType:fuel type:  " + fuelType);
        SQLiteDatabase db = null;
        Cursor cursor1 = null;
        String productName = "";
        List<String> details = new ArrayList<>();
        details.add("Select Product");
        db = this.getWritableDatabase();

        String selectQuery1 = "SELECT DISTINCT prodName FROM fuelInfraMapping where prodCategory = ?";
        Log.d(TAG, "getProductNameByFuelType: selectQuery1 : " + selectQuery1);
        cursor1 = db.rawQuery(selectQuery1, new String[]{fuelType});

        if (cursor1.moveToFirst()) {
            do {
                productName = (cursor1.getString(0));
                details.add(productName);
            } while (cursor1.moveToNext());
        }
        Log.d(TAG, "getProductNameByFuelType: prodname :" + productName);

        return details;
    }

    public String getProductId(String prodname, String subProdName) {
        Log.d(TAG, "getProductId:prodname :   " + prodname);
        Log.d(TAG, "getProductId: subProdName : " + subProdName);
        SQLiteDatabase db = null;
        Cursor cursor1 = null;
        String productId = "";
        db = this.getWritableDatabase();

        String selectQuery1 = "SELECT DISTINCT productId FROM fuelInfraMapping where prodCategory = ? AND prodName = ?";
        Log.d(TAG, "getProductId: selectQuery1 : " + selectQuery1);
        cursor1 = db.rawQuery(selectQuery1, new String[]{prodname, subProdName});

        if (cursor1.moveToFirst()) {
            do {
                productId = (cursor1.getString(0));
            } while (cursor1.moveToNext());
        }
        Log.d(TAG, "getProductId: productId :" + productId);
        cursor1.close();
        return productId;
    }


    public String getPaymentType() {
        Log.d(TAG, "getPaymentType:");
        SQLiteDatabase db = null;
        Cursor cursor1 = null;
        String payType = "";
        db = this.getWritableDatabase();

        String selectQuery1 = "SELECT paymentType FROM dailysales ORDER BY sales_pk DESC LIMIT 1;";
        Log.d(TAG, "getPaymentType: selectQuery1 : " + selectQuery1);
        cursor1 = db.rawQuery(selectQuery1, null);

        if (cursor1.moveToFirst()) {
            do {
                payType = (cursor1.getString(0));
            } while (cursor1.moveToNext());
        }
        Log.d(TAG, "getPaymentType: productId :" + payType);

        return payType;
    }

    public List<PaymentModelLocal> getDonePaymentDetails() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        List<PaymentModelLocal> detailsList = new ArrayList<>();

        try {
            db = this.getWritableDatabase();

            String selectQuery = "select * from dailysales where paymentType not in ('cash', 'submit')";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String batchId = cursor.getString(1);
                    String paymentType = (cursor.getString(2));
                    String amount = (cursor.getString(3));
                    String transId = (cursor.getString(4));

                    PaymentModelLocal paymentModelLocal = new PaymentModelLocal(batchId, paymentType, amount, transId);
                    detailsList.add(paymentModelLocal);
                    Log.d(TAG, "getDonePaymentDetails: detailsList size  :" + detailsList);
                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return detailsList;
    }

    //save info card details
    public long saveInfoCardDetails(List<OperatorDailyWorkListModel> infoModelsList) {
        SQLiteDatabase db = this.getWritableDatabase();

        long result = 0;
        try {
            for (int i = 0; i < infoModelsList.size(); i++) {
                OperatorDailyWorkListModel dailySalesInfoModel = infoModelsList.get(i);

                ContentValues values = new ContentValues();

                values.put(product, dailySalesInfoModel.getProduct());
                values.put(meter_sale, dailySalesInfoModel.getMeterSaleLiters());
                values.put(total_amt, dailySalesInfoModel.getTotalAmount());

                result = db.insert(TABLE_INFO_CARD, null, values);
                Log.d(TAG, "saveInfoCardDetails: insert result : " + result);
            }


        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            db.close();

        }
        return result;
    }

    public List<OperatorInfoFragModel> getInfoCardsForDailySales() {
        SQLiteDatabase db = null;
        Cursor cursor = null;

        List<OperatorInfoFragModel> detailsList = new ArrayList<>();

        try {
            db = this.getWritableDatabase();

            String selectQuery = "select * from infocardsales";


            cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    String product = cursor.getString(1);
                    String meterSale = (cursor.getString(2));
                    String amount = (cursor.getString(3));

                    OperatorInfoFragModel salesInfoCardModel = new OperatorInfoFragModel(product, meterSale, amount);
                    detailsList.add(salesInfoCardModel);

                } while (cursor.moveToNext());
            }
            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return detailsList;
    }

    public void deleteSalesInfoCardTable() {
        Log.d(TAG, "deleteSalesInfoCardTable: ");
        SQLiteDatabase db = null;
        try {
            db = this.getWritableDatabase();

            db.execSQL("DELETE FROM infocardsales");
            db.execSQL("VACUUM");

            db.close();
        } catch (Exception e) {
        }
    }


    public List<String> getTankAndPKByDuNozzle(String duNo, String nozzleNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> detailsList = new ArrayList<>();
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where duNo = ? and nozzleNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{duNo, nozzleNo});

            if (cursor.moveToFirst()) {
                do {
                    String dunzmap = (cursor.getString(0));
                    String tankNo = (cursor.getString(1));
                    String prodCategory = (cursor.getString(6));
                    String dealerId = (cursor.getString(10));
                    String inframapId = (cursor.getString(11));

                    detailsList.add(dunzmap);
                    detailsList.add(tankNo);
                    detailsList.add(prodCategory);
                    detailsList.add(dealerId);
                    detailsList.add(inframapId);

                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return detailsList;
    }


    public String getFuelInfraMapIdForShiftCheck(String tankNo, String duNo, String nozzleNo) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        String inframapId = "";

        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT fuelInfraMapId FROM fuelInfraMapping where tankNo = ? and duNo = ? and nozzleNo = ?";

            cursor = db.rawQuery(selectQuery, new String[]{tankNo, duNo, nozzleNo});

            if (cursor.moveToFirst()) {
                do {

                    inframapId = (cursor.getString(0));

                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return inframapId;
    }

    public List<String> getInfraMapIdByProdCategoryAndProdName(String prodCat, String prodName) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<String> detailsList = new ArrayList<>();
        String inframapId = "";
        try {
            db = this.getWritableDatabase();

            String selectQuery = "SELECT * FROM fuelInfraMapping where prodCategory = ? and prodName = ?";

            cursor = db.rawQuery(selectQuery, new String[]{prodCat, prodName});

            if (cursor.moveToFirst()) {
                do {
                    /*String dunzmap = (cursor.getString(0));
                    String tankNo = (cursor.getString(1));
                    String prodCategory = (cursor.getString(6));
                    String dealerId = (cursor.getString(10));*/
                    inframapId = (cursor.getString(11));
                    detailsList.add(inframapId);

                 /*   detailsList.add(dunzmap);
                    detailsList.add(tankNo);
                    detailsList.add(prodCategory);
                    detailsList.add(dealerId);
                    detailsList.add(inframapId);*/


                } while (cursor.moveToNext());
            }


            db.close();
            cursor.close();
        } catch (Exception e) {
        }
        return detailsList;
    }


}