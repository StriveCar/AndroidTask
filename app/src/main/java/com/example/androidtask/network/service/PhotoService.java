package com.example.androidtask.network.service;

import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
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
//    Flowable<BaseResponse<Data<Records>>> getShare(@Query("userId") int userId);
    Observable<BaseResponse<Data<Records>>> getShare(@Query("userId") int userId);

    @POST("user/register")
    Call<BaseResponse<Object>> userRegister(@Body User user);
}
