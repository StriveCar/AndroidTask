package com.example.androidtask.network.service;

import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.User;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("share")
//    Observable<BaseResponse<Data<Records>>> getShare(@Query("userId") int userId);
    Flowable<BaseResponse<Data<Records>>> getShare(@Query("current") Integer current,@Query("size") Integer size,@Query("userId") int userId);

    @POST("user/register")
    Call<BaseResponse<Object>> userRegister(@Body User user);

    @POST("user/login")
    Call<BaseResponse<LoginData>> userLogin(@Query("password") String pwd,@Query("username") String une);
}
