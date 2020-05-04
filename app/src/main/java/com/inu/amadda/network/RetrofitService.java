package com.inu.amadda.network;

import com.inu.amadda.model.AddGroupModel;
import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.InvitationResponse;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.model.SearchUserResponse;
import com.inu.amadda.model.SuccessResponse;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @POST("user/login")
    Call<SuccessResponse> Login(@Body HashMap<String, String> login);

    @POST("schedule/add")
    Call<SuccessResponse> AddSchedule(@Header("token") String token, @Body AddScheduleModel addScheduleModel);

    @GET("schedule/show/all")
    Call<ScheduleResponse> GetAllSchedule(@Header("token") String token);

    @GET("schedule/show/week")
    Call<ScheduleResponse> GetWeekSchedule(@Header("token") String token, @Query("date") String date);

    @POST("share/group/create")
    Call<SuccessResponse> MakeGroup(@Header("token") String token, @Body AddGroupModel addGroupModel);

    @GET("share/invite/users/search")
    Call<SearchUserResponse> SearchUser(@Header("token") String token, @Query("user") String user);

    @GET("share/invitations/show")
    Call<InvitationResponse> GetInvitations(@Header("token") String token);

}
