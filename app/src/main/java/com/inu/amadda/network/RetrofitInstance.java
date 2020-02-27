package com.inu.amadda.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "http://ec2-54-180-95-83.ap-northeast-2.compute.amazonaws.com/";

    private static RetrofitInstance instance = null;

    public static RetrofitInstance getInstance() {
        if (instance == null) {
            instance = new RetrofitInstance();
        }
        return instance;
    }

    private RetrofitInstance() {

    }

    public RetrofitService getService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RetrofitService.class);
    }
}
