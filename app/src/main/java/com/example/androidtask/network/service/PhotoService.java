package com.example.androidtask.network.service;

import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.LoginActivity;
import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.ImageText;
import com.example.androidtask.response.ImageUrl;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.User;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("share")
//    Observable<BaseResponse<Data<Records>>> getShare(@Query("current") Integer current,@Query("size") Integer size,@Query("userId") String userId);
    Flowable<BaseResponse<Data<Records>>> getShare(@Query("current") Integer current,@Query("size") Integer size,@Query("userId") String userId);

    @POST("user/update")
    Call<BaseResponse<Object>> updateInfo(@Body LoginData loginData);

    @Multipart
    @POST("image/upload")
    Call<BaseResponse<ImageUrl>> uploadImage(@Part MultipartBody.Part imageFile);

    @Multipart
    @POST("image/upload")
    Call<BaseResponse<ImageUrl>> uploadImage(@Part List<MultipartBody.Part> imageFiles);

    @POST("user/register")
    Call<BaseResponse<Object>> userRegister(@Body User user);

    @POST("user/login")
    Call<BaseResponse<LoginData>> userLogin(@Query("password") String pwd,@Query("username") String une);


    @POST("share/add")
    Call<BaseResponse<Object>> uploadAdd(@Body ImageText imageText);

    @POST("share/save")
    Call<BaseResponse<Object>> saveAdd(@Body ImageText imageText);

    @GET("share/save")
    Call<BaseResponse<Data<Records>>> getAdd(@Query("userId") String userid);

    @POST("share/delete")
    Call<BaseResponse<Data<Records>>> deleteAdd(@Query("userId") String userid,@Query("shareId") String shareid);

    @POST("share/change")
    Call<BaseResponse<Object>> changeStatus(@Body ImageText imageText);

}
