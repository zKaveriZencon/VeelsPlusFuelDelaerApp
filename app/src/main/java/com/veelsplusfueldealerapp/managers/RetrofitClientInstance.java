package com.veelsplusfueldealerapp.managers;

import android.content.Context;
import android.util.Log;


import com.veelsplusfueldealerapp.commonclasses.CommonCodeManager;
import com.veelsplusfueldealerapp.commonclasses.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static final String TAG = "RetrofitClientInstance";
    static String accessToken;
    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitInstance(Context mContext) {
        if (retrofit == null) {
            CommonCodeManager commonCodeManager = new CommonCodeManager(mContext);
            accessToken = commonCodeManager.getAccessToken(mContext);
            Log.d(TAG, "getRetrofitInstance: accessToken : " + accessToken);


            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("authenticationToken", accessToken)
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {

            CommonCodeManager commonCodeManager = new CommonCodeManager(mContext);
            accessToken = commonCodeManager.getAccessToken(mContext);
            Log.d(TAG, "getRetrofitInstance: accessToken else part : " + accessToken);
        }
        return retrofit;
    }
}