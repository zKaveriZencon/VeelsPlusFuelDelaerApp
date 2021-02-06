package com.veelsplusfueldealerapp.managers;


import com.google.gson.JsonObject;
import com.veelsplusfueldealerapp.models.AddTankFuelInventoryModel;
import com.veelsplusfueldealerapp.models.ContactUsModel;
import com.veelsplusfueldealerapp.models.DailyInventoryDetailsModel;
import com.veelsplusfueldealerapp.models.ErCashModel;
import com.veelsplusfueldealerapp.models.ErDigitalModel;
import com.veelsplusfueldealerapp.models.ErrorLogModel;
import com.veelsplusfueldealerapp.models.FuelPriceModel;
import com.veelsplusfueldealerapp.models.NewCreditRequestModel;
import com.veelsplusfueldealerapp.models.OperatorEndShiftModel;
import com.veelsplusfueldealerapp.models.OperatorStartShiftModel;
import com.veelsplusfueldealerapp.models.PersonModel;
import com.veelsplusfueldealerapp.models.PersonModelNew;
import com.veelsplusfueldealerapp.models.UpdateFuelCreditRequest;
import com.veelsplusfueldealerapp.models.UpdateFuelInventoryModel;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetDataService {

    @POST("fuelVendor/getAllTanksByID")
    @FormUrlEncoded
    Call<JsonObject> getAllTanksByID(@Field("fuelVendorId") String loginId);

    @POST("fuelinframapping/getPumpByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getAllPumpsForOpShift(@Field("fuelVendorId") String fuelVendorId);

    @POST("fuelinframapping/getNozzelByPumpid")
    @FormUrlEncoded
    Call<JsonObject> getAllNozzlesForOpShiftByPumpId(@Field("fuelVendorId") String fuelVendorId,
                                                     @Field("pumpNo") String pumpNo);

    @POST("fuelVendor/getAllNozzels")
    @FormUrlEncoded
    Call<JsonObject> getAllNozzlesForOpShift(@Field("fuelVendorId") String loginId);

    @POST("fuelStaff/addFuelStaffPerform")
    Call<JsonObject> sendOpStartShiftDetails(@Body OperatorStartShiftModel startShiftDetails);

    @POST("fuelStaff/updateFuelStaffPerform")
    Call<JsonObject> sendOpEndShiftDetails(@Body OperatorEndShiftModel operatorEndShiftModel);


    @POST("fuelInventory/addTankFuelInventory")
    Call<JsonObject> addTankFuelInventoryDetails(@Body AddTankFuelInventoryModel
                                                         addTankFuelInventoryModel);

    @POST("serviceRequestTickets/addNewTicket")
    Call<JsonObject> contactUs(@Body ContactUsModel contactUsModel);

    @POST("fuelStaff/getFuelStaffPerform")
    @FormUrlEncoded
    Call<JsonObject> getOpertaorShiftDetails(@Field("fuelDealerStaffId") String fuelDealerStaffId);

    @POST("fuelStaff/getFuelStaffPerformById")
    @FormUrlEncoded
    Call<JsonObject> getOpertaorShiftDetailsById(@Field("fuelStaffPerformId") String fuelStaffPerformId);

   /* @POST("register/login")
    @FormUrlEncoded
    Call<JsonObject> loginToDealer(@Field("veelsPlusUser") String phone1,
                                   @Field("password") String password);*/

    @POST("register/FuelVendorlogin")
    @FormUrlEncoded
    Call<JsonObject> loginToDealer(@Field("veelsPlusUser") String veelsPlusUser,
                                   @Field("password") String password);


    @POST("fuelinframapping/getProductDetailsByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getFuelInfraDetails(@Field("fuelVendorId") String fuelVendorId);

    @POST("fuelinframapping/getTankByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getTankDetailsByDealerId(@Field("fuelVendorId") String fuelVendorId);

    @POST("fuelInventory/getTankFuelInventoryByfuelTankRefuelId")
    @FormUrlEncoded
    Call<JsonObject> getInventoryDetailsByFuelTankRefuelId(@Field("fuelTankRefuelId") String fuelVendorId);

    @POST("fuelInventory/getTankFuelInventoryById")
    @FormUrlEncoded
    Call<JsonObject> getTankFIDetailsByFuelDealerStaffId(@Field("fuelDealerStaffId") String fuelDealerStaffId);

    @POST("fuelDailySales/updateTankFuelInventoryById")
    Call<JsonObject> updateFuelTankInventoryDetails(@Body UpdateFuelInventoryModel updateFuelInventoryModel);

    @POST("fuelInventory/addStockFuelInventory")
    Call<JsonObject> addDailyFuelInventoryDetails(@Body DailyInventoryDetailsModel dailyInventoryDetailsModel);

    @POST("fuelInventory/getStockFuelInventoryById")
    @FormUrlEncoded
    Call<JsonObject> getAllStockFuelInventoryById(@Field("fuelDealerStaffId") String fuelDealerStaffId);

    @POST("fuelInventory/updateStockFuelInventoryById")
    Call<JsonObject> updateTankFuelInventoryDetails(@Body DailyInventoryDetailsModel dailyInventoryDetailsModel);


    @POST("fuelPrice/getFuelPriceByDealerTankMap")
    @FormUrlEncoded
    Call<JsonObject> getFuelPriceByDealerTankMap(@Field("dealerVFId") String dealerVFId,
                                                 @Field("dealerTankMap") String dealerTankMap);

    @POST("fuelInventory/updateStatusForFuelInventory")
    @FormUrlEncoded
    Call<JsonObject> updateStatusForFuelInventory(@Field("fuelTankRefuelId") String fuelTankRefuelId,
                                                  @Field("status") String status);


    @POST("fuelStaff/updateStatusForOperatorShiftEnd")
    @FormUrlEncoded
    Call<JsonObject> updateStatusForOperatorShiftEnd(@Field("fuelStaffPerformId") String fuelStaffPerformId,
                                                     @Field("activityStatus") String activityStatus);


    @POST("fuelStaff/getFuelStaffPerformByRecoveryStatusPENDING")
    @FormUrlEncoded
    Call<JsonObject> getFuelStaffPerformForAllPendingPayments(@Field("fuelDealerStaffId")
                                                                      String fuelDealerStaffId);


    /*@POST("accounttransaclog/addAccountTransactionLogForCash")
    Call<JsonObject> addAccountTransactionLogForCash(@Body ErCashModel erCashModel);


    @POST("accounttransaclog/addAccountTransactionLogForDigital")
    Call<JsonObject> addAccountTransactionLogForDigital(@Body ErDigitalModel erDigitalModel);
*/
    @POST("accounttransaclog/addAccountTransactionLogForApp")
    Call<JsonObject> addAccountTransactionLogForCash(@Body ErCashModel erCashModel);


    @POST("accounttransaclog/addAccountTransactionLogForDigitalForApp")
    Call<JsonObject> addAccountTransactionLogForDigital(@Body ErDigitalModel erDigitalModel);


    @POST("fuelTerminals/getFuelTerminal")
    @FormUrlEncoded
    Call<JsonObject> getFuelTerminals(@Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/getAllFuelCreditRequestDealer")
    @FormUrlEncoded
    Call<JsonObject> getAllFuelCreditRequestDealer(@Field("fuelDealerId") String fuelDealerId);


    @POST("accounttransaclog/getAccountTransactionLogForDigital")
    @FormUrlEncoded
    Call<JsonObject> getAccountTransactionLogForDigital(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                        @Field("batchId") String batchId);


    @POST("fuelDealerCustMap/getFuelCreditRequestByfuelCreditId")
    @FormUrlEncoded
    Call<JsonObject> getFuelCreditRequestByfuelCreditId(@Field("fuelCreditId") String fuelCreditId);


    @POST("fuelDealerCustMap/getFuelCreditRequestDetailsForPersonByfuelCreditId")
    @FormUrlEncoded
    Call<JsonObject> getFuelCreditRequestDetailsForPersonByfuelCreditId(@Field("fuelCreditId") String fuelCreditId);


    //for credit under sales
    @POST("fuelDealerCustMap/getFuelCreditRequestByfuelDealerIdLastSevenDays")
    @FormUrlEncoded
    Call<JsonObject> getFuelCreditRequestByfuelDealerId(@Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/getFuelCreditReqForPersonByDealer")
    @FormUrlEncoded
    Call<JsonObject> getFuelCreditReqForPersonByDealer(@Field("fuelDealerId") String fuelDealerId);


    @POST("fuelDealerCustMap/updateFuelCreditReqByfuelCreditId")
    Call<JsonObject> updateFuelCreditReqByfuelCreditId(@Body UpdateFuelCreditRequest updateFuelCreditRequest);

    //for op list at a manager section
    @POST("fuelStaff/getFuelStaffPerformByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getFuelStaffPerformByDealerId(@Field("fuelDealerId") String fuelDealerId);

    //for daily sales card
    @POST("fuelDailySales/getDailySalesTransactionDetailsByStaffId")
    @FormUrlEncoded
    Call<JsonObject> getDailySalesTransactionDetailsByStaffId(@Field("fuelDealerStaffId") String fuelDealerStaffId);

    @POST("fuelStaff/getUserProfileInfo")
    @FormUrlEncoded
    Call<JsonObject> getUserProfileInfo(@Field("fuelDealerId") String fuelDealerId,
                                        @Field("fuelDealerStaffId") String fuelDealerStaffId);

    @POST("accounttransaclog/getAllTransactionDetailsByBatchId")
    @FormUrlEncoded
    Call<JsonObject> getAllTransactionDetailsByBatchId(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                       @Field("batchId") String batchId);


    @POST("fuelDailySales/updateRecoveryStatusForDailySales")
    @FormUrlEncoded
    Call<JsonObject> updateRecoveryStatusForDailySales(@Field("fuelStaffPerformId") String fuelDealerStaffId,
                                                       @Field("batchId") String batchId);


    //need to ask for done trans

    @POST("fuelDailySales/getFuelStaffPerformByRecoveryStatusSUBMIT")
    @FormUrlEncoded
    Call<JsonObject> getFuelStaffPerformByRecoveryStatusSUBMIT(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                               @Field("batchId") String batchId);


    @POST("fuelStaff/addRecoveryBatchIdAfterSalesTransaction")
    @FormUrlEncoded
    Call<JsonObject> addRecoveryBatchIdAfterSalesTransaction(@Field("fuelStaffPerformId") String fuelStaffPerformId,
                                                             @Field("recoveryBatchId") String recoveryBatchId);


    @POST("driver/searchDriverByPhoneNumber")
    @FormUrlEncoded
    Call<JsonObject> searchDriverByPhoneNumber(@Field("phone1") String phone1);

    @POST("person/addPerson")
    @FormUrlEncoded
    Call<JsonObject> addPerson(@Field("firstName") String firstName,
                               @Field("phone1") String phone1,
                               @Field("personCreatedBy") String personCreatedBy,
                               @Field("lastName") String lastName);

    @POST("vehicle/searchVehicleByRegistrationNumber")
    @FormUrlEncoded
    Call<JsonObject> searchVehicleByRegistrationNumber(@Field("vehicleRegistrationNumber") String vehicleRegistrationNumber);


    @POST("fuelDealerCustMap/getCorporatesAllMappedRequestByDealer")
    @FormUrlEncoded
    Call<JsonObject> getCorporatesAllMappedRequestByDealer(@Field("fuelDealerId")
                                                                   String fuelDealerId);

    /*@POST("fuelDealerCustMap/addNewCreditReqByCorp")
    Call<JsonObject> addNewCreditReqByCorp(@Body NewCreditRequestModel newModel);
*/
    @POST("fuelDealerCustMap/addCreditReqByDealer")
    Call<JsonObject> addNewCreditReqByCorp(@Body NewCreditRequestModel newModel);

    @POST("fuelDealerCustMap/getRequestListByVehicleNumber")
    @FormUrlEncoded
    Call<JsonObject> getRequestListByVehicleNumber(@Field("registrationNumber") String registrationNumber,
                                                   @Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/getRequestListByPersonNumber")
    @FormUrlEncoded
    Call<JsonObject> getRequestListByPersonNumber(@Field("phoneNumber") String phoneNumber,
                                                  @Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/addCreditReqForPersonByDealer")
    Call<JsonObject> addCreditReqForPersonByDealer(@Body PersonModel personModel);

    //for person

    @POST("fuelDealerCustMap/addCreditReqPersonByDealer")
    Call<JsonObject> addCreditReqPersonByDealer(@Body PersonModelNew personModel);


    @POST("fuelDealerCustMap/getCustomerIdBycorporatePhoneNumber")
    @FormUrlEncoded
    Call<JsonObject> getCustomerIdBycorporatePhoneNumber(@Field("mobileNumber")
                                                                 String mobileNumber);

    @POST("accounttransaclog/updateTotalBatchAmtRecoveredAfterAddTransaction")
    @FormUrlEncoded
    Call<JsonObject> updateTotalBatchAmtRecoveredAfterAddTransaction(@Field("activityId") String activityId,
                                                                     @Field("batchId") String batchId,
                                                                     @Field("recoveredAmtByApp") String recoveredAmtByApp,
                                                                     @Field("shiftWiseCreditSumForApp") String shiftWiseCreditSumForApp);

    @POST("fuelCreditInvoice/getCreditAmtListByfuelDealerStaffId")
    @FormUrlEncoded
    Call<JsonObject> getCreditAmtListByfuelDealerStaffId(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                         @Field("fuelStaffperformId") String fuelStaffperformId);

    @POST("register/findPhoneNumber")
    @FormUrlEncoded
    Call<JsonObject> findPhoneNumberForPassword(@Field("phone") String phone);

    @POST("register/sendOTPsms")
    @FormUrlEncoded
    Call<JsonObject> sendOtpRequest(@Field("numbers") String phone);


    @POST("register/compareOTP")
    @FormUrlEncoded
    Call<JsonObject> compareOTP(@Field("numbers") String phone,
                                @Field("otp") String otp);

    @POST("register/updatePassword")
    @FormUrlEncoded
    Call<JsonObject> updatePassword(@Field("personId") String personId,
                                    @Field("password") String password);

    @Multipart
    @POST("api/v1/upload")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part image);


    @POST("person/updateProfilePicture")
    @FormUrlEncoded
    Call<ResponseBody> updateProfilePicture(@Field("personId") String personId,
                                            @Field("personImagePath") String personImagePath);


    @GET("groupAccess/getInfoFromSetup")
    Call<JsonObject> getInfoFromSetup();

    @POST("fuelPrice/getFuelProductIdByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getFuelProductIdByDealerId(@Field("fuelDealerId") String fuelDealerId);


    @POST("fuelPrice/getTankCountByfuelProductIdDealerId")
    @FormUrlEncoded
    Call<JsonObject> getTankCountByfuelProductIdDealerId(@Field("fuelDealerId") String fuelDealerId,
                                                         @Field("fuelProductId") String fuelProductId);

    @POST("fuelPrice/getFuelPriceDetails")
    @FormUrlEncoded
    Call<JsonObject> getFuelPriceDetails(@Field("dealerLoginId") String dealerLoginId);


    @POST("fuelPrice/getDealerTankMapCodeByfuelProductIdDealerId")
    @FormUrlEncoded
    Call<JsonObject> getDealerTankMapCodeByfuelProductIdDealerId(@Field("fuelDealerId") String fuelDealerId,
                                                                 @Field("fuelProductId") String fuelProductId);

    @POST("fuelPrice/setFuelPrice")
    Call<JsonObject> setFuelPrice(@Body FuelPriceModel fuelPriceModel);

    @POST("fuelCreditInvoice/searchPersonByPhoneNumber")
    @FormUrlEncoded
    Call<JsonObject> searchPersonByPhoneNumber(@Field("fuelDealerId") String fuelDealerId,
                                               @Field("mobileNumber") String mobileNumber);

    @POST("fuelPrice/getPriceByDealerProductId")
    @FormUrlEncoded
    Call<JsonObject> getPriceByDealerProductId(@Field("dealerId") String dealerId,
                                               @Field("fuelProductId") String fuelProductId);


    @POST("fuelPrice/getPriceByDealerProductIdByDate")
    @FormUrlEncoded
    Call<JsonObject> getPriceByDealerProductIdByDate(@Field("dealerId") String dealerId,
                                                     @Field("fuelProductId") String fuelProductId,
                                                     @Field("date") String date);

    @POST("fuelDealerCustMap/getCorporateInfoByfuelDealerCustomerMapId")
    @FormUrlEncoded
    Call<JsonObject> getCorporateInfoByfuelDealerCustomerMapId(@Field("fuelDealerCustomerMapId") String fuelDealerCustomerMapId);

    @POST("address/addErrorDetails")
    Call<JsonObject> addErrorDetails(@Body ErrorLogModel errorLogModel);

    @POST("fuelCreditInvoice/searchPersonByPhoneNumber")
    @FormUrlEncoded
    Call<JsonObject> searchPersonByPhoneNumberNew(@Field("personId") String personId,
                                                  @Field("fuelDealerId") String fuelDealerId);


    @POST("fuelDealerCustMap/getPersonAccByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getPersonAccByDealerId(@Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/getAllCreditAccByDealerId")
    @FormUrlEncoded
    Call<JsonObject> getAllCreditAccByDealerId(@Field("fuelDealerId") String fuelDealerId);

    @POST("fuelDealerCustMap/searchByCorporateName")
    @FormUrlEncoded
    Call<JsonObject> searchByCorporateName(@Field("fuelDealerCustomerMapId") String fuelDealerCustomerMapId);

    @POST("fuelStaff/getTotalCreditAmountShiftWise")
    @FormUrlEncoded
    Call<JsonObject> getTotalCreditAmountShiftWise(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                   @Field("fuelStaffperformId") String fuelStaffperformId);


    @POST("fuelinframapping/checkPumpNzStatus")
    @FormUrlEncoded
    Call<JsonObject> checkPumpNzStatus(@Field("fuelInfraMapId") String fuelInfraMapId);


    @POST("fuelCreditInvoice/getCreditAmountForfuelStaffperformId")
    @FormUrlEncoded
    Call<JsonObject> getCreditAmountForfuelStaffperformId(@Field("fuelStaffperformId")
                                                                  String fuelStaffperformId);


    @POST("accounttransaclog/getAllTransactionDetailsByBatchIdForCashOnlyNEW")
    @FormUrlEncoded
    Call<JsonObject> getAllTransactionDetailsByBatchIdForCashOnly(@Field("fuelDealerStaffId") String fuelDealerStaffId,
                                                                  @Field("batchId") String batchId);


    @POST("fuelDealerCustMap/getCorporatesAllMappedRequestByDealer")
    @FormUrlEncoded
    Call<JsonObject> getCorporatesAllMappedRequestByDealerMapped(@Field("fuelDealerId")
                                                                         String fuelDealerId, @Field("matchName")
                                                                         String matchName);

}


