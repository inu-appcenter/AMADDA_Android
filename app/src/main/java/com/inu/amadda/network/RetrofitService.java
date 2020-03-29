package com.inu.amadda.network;

import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.LoginResponse;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.model.SuccessResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface RetrofitService {
    @POST("user/login")
    Call<LoginResponse> Login(@Body HashMap<String, String> login);

    @POST("schedule/add")
    Call<SuccessResponse> AddSchedule(@Header("token") String token, @Body AddScheduleModel addScheduleModel);

    @GET("schedule/show/all")
    Call<ScheduleResponse> GetAllSchedule(@Header("token") String token);

}
