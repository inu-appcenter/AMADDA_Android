package com.inu.amadda.network;

import com.inu.amadda.model.AddGroupModel;
import com.inu.amadda.model.AddGroupResponse;
import com.inu.amadda.model.AddScheduleModel;
import com.inu.amadda.model.ClassResponse;
import com.inu.amadda.model.DeleteScheduleModel;
import com.inu.amadda.model.GroupResponse;
import com.inu.amadda.model.InvitationResponse;
import com.inu.amadda.model.MemberResponse;
import com.inu.amadda.model.RefusalModel;
import com.inu.amadda.model.ScheduleDetailResponse;
import com.inu.amadda.model.ScheduleResponse;
import com.inu.amadda.model.SearchUserResponse;
import com.inu.amadda.model.SidebarResponse;
import com.inu.amadda.model.SuccessResponse;
import com.inu.amadda.model.UserImageResponse;
import com.inu.amadda.model.UserProfileResponse;

import java.util.HashMap;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
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
    Call<AddGroupResponse> MakeGroup(@Header("token") String token, @Body AddGroupModel addGroupModel);

    @GET("share/invite/users/search")
    Call<SearchUserResponse> SearchUser(@Header("token") String token, @Query("user") String user);

    @GET("share/invitations/show")
    Call<InvitationResponse> GetInvitations(@Header("token") String token);

    @GET("appconfig/sidebar")
    Call<SidebarResponse> GetSidebar(@Header("token") String token);

    @HTTP(method = "DELETE", path = "share/invitations/group", hasBody = true)
    Call<SuccessResponse> refuseInvitation(@Header("token") String token, @Body RefusalModel refusalModel);

    @FormUrlEncoded
    @POST("share/invitations/group")
    Call<SuccessResponse> acceptInvitation(@Header("token") String token, @Field("share") int share);

    @GET("schedule/show/me")
    Call<ScheduleResponse> GetPersonalSchedules(@Header("token") String token);

    @GET("schedule/show/group")
    Call<ScheduleResponse> GetGroupSchedules(@Header("token") String token, @Query("share") int share);

    @GET("share/group/member")
    Call<MemberResponse> GetGroupMembers(@Header("token") String token, @Query("share") int share);

    @HTTP(method = "DELETE", path = "share/group/escape", hasBody = true)
    Call<SuccessResponse> LeaveGroup(@Header("token") String token, @Body RefusalModel refusalModel);

    @GET("schedule/detail")
    Call<ScheduleDetailResponse> GetScheduleDetail(@Header("token") String token, @Query("num") int num);

    @PUT("schedule/modify")
    Call<SuccessResponse> EditSchedule(@Header("token") String token, @Body AddScheduleModel addScheduleModel);

    @HTTP(method = "DELETE", path = "schedule/delete", hasBody = true)
    Call<SuccessResponse> DeleteSchedule(@Header("token") String token, @Body DeleteScheduleModel deleteScheduleModel);

    @GET("share/groups/show")
    Call<GroupResponse> GetGroups(@Header("token") String token);

    @GET("time/table/search")
    Call<ClassResponse> GetClasses(@Header("token") String token, @Query("name") String name);

    @GET("schedule/show/day")
    Call<ScheduleResponse> GetDaySchedule(@Header("token") String token, @Query("date") String date);

    @DELETE("user/secession")
    Call<SuccessResponse> Withdrawal(@Header("token") String token);

    @POST("user/account")
    Call<UserProfileResponse> GetUserProfile(@Header("token") String token);

    @GET("user/image")
    Call<UserImageResponse> GetUserImage(@Header("token") String token);

    @Multipart
    @POST("image")
    Call<SuccessResponse> UploadUserImage(@Header("token") String token, @Part MultipartBody.Part user_image);

}
