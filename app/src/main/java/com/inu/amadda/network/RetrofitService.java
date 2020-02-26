package com.inu.amadda.network;

import com.inu.amadda.model.LoginResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("user/login")
    Call<LoginResponse> Login(
            @Body HashMap<String, String> login);
}
