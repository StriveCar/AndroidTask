package com.example.androidtask.network.service;

import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.Comment;
import com.example.androidtask.response.Data;
import com.example.androidtask.response.LoginData;
import com.example.androidtask.response.Records;
import com.example.androidtask.response.User;
import com.example.androidtask.response.UserInfo;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PhotoService {

    @GET("share")
//    Observable<BaseResponse<Data<Records>>> getShare(@Query("userId") int userId);
    Flowable<BaseResponse<Data<Records>>> getShare(@Query("current") Integer current,@Query("size") Integer size,@Query("userId") String userId);
//    Call<BaseResponse<Data<Records>>> getShare(@Query("current") Integer current, @Query("size") Integer size, @Query("userId") String userId);
    @POST("user/register")
    Call<BaseResponse<Object>> userRegister(@Body User user);

    @POST("user/login")
    Call<BaseResponse<LoginData>> userLogin(@Query("password") String pwd,@Query("username") String une);

    @GET("user/getUserByName")
    Call<BaseResponse<UserInfo>> getUserByName(@Query("username")String username);

    @POST("like")
    Call<BaseResponse> thumbsUp(@Query("shareId") String shareId, @Query("userId") String userId);

    @POST("like/cancel")
    Call<BaseResponse> cancelLike(@Query("likeId")String likeId);

    @GET("share/detail")
    Call<BaseResponse<Records>> getDetail(@Query("shareId")String shareId, @Query("userId")String userId);

    @GET("collect")
    Flowable<BaseResponse<Data<Records>>> getCollect(@Query("current") Integer current,@Query("size") Integer size,@Query("userId") String userId);

    @POST("collect")
    Call<BaseResponse> collect(@Query("shareId")String shareId, @Query("userId")String userId);

    @POST("collect/cancel")
    Call<BaseResponse> cancelCollect(@Query("collectId")String collectId);

    @GET("comment/first")
    Call<BaseResponse<Data<Comment>>> getFirstComment(@Query("current")int current, @Query("shareId")String shareId, @Query("size")int size);

    @POST("comment/first")
    Call<BaseResponse> addFirstCommment(@Query("content")String content, @Query("shareId")String shareId,@Query("userId")String userId,@Query("userName")String username);

    @GET("comment/second")
    Flowable<BaseResponse<Data<Comment>>> getsecondComment(@Query("commentId")String ParentcommentId, @Query("current")int current, @Query("shareId")String shareId, @Query("size")int size);

    @POST("comment/second")
    Call<BaseResponse> addsecondCommment(@Query("content")String content,
                                         @Query("parentCommentId")String parentCommentId,
                                         @Query("parentCommentUserId")String parentCommentUserId,
                                         @Query("replyCommentId")String replyCommentId,
                                         @Query("replyCommentUserId")String replyCommentUserId,
                                         @Query("shareId")String shareId,
                                         @Query("userId")String userId,
                                         @Query("userName")String username);

    @GET("share/myself")
    Flowable<BaseResponse<Data<Records>>> getMyShare(@Query("current")int current,@Query("size")int size,@Query("userId")String userId);
}
