package com.veelsplusfueldealerapp.managers;

import android.content.Context;

import com.veelsplusfueldealerapp.commonclasses.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstanceWithoutToken {private static Retrofit retrofit = null;
    //private static final String baseUrl = "http://13.234.240.139:9000/";


    public static Retrofit getRetrofitInstance(Context mContext) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS)
                    .writeTimeout(100, TimeUnit.SECONDS)
                    .build();

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
