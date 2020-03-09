package com.inu.amadda.network;

import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.LoginResponse;
import com.inu.amadda.model.SuccessResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("user/login")
    Call<LoginResponse> Login(@Body HashMap<String, String> login);

    @POST("schedule/add")
    Call<SuccessResponse> AddSchedule(@Body AddScheduleModel addScheduleModel);

}
