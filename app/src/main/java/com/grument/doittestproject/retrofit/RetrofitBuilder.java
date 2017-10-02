package com.grument.doittestproject.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.grument.doittestproject.util.Constants;

import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuilder {

    private final OkHttpClient okHttpClient;

    public RetrofitBuilder() {
        okHttpClient = new OkHttpClient.Builder().build();
    }

    public Retrofit build() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .create();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constants.BASE_API_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


}