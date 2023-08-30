package com.example.androidtask.network;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;

/* 工具拦截器
 */
public class InterceptorUtil {
    public static final String TAG = "------";

    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(message -> Log.w(TAG, "log:" + message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    public static Interceptor HeaderInterceptor() {      //头部添加请求头信息
        return chain -> {
            Request request = chain.request().newBuilder()
                    .addHeader("Accept","application/json, text/plain, */*")
                    .addHeader("Content-Type","application/json")
                    .addHeader("appId", "78340245670c4b6299c088d9c5f8d1c3")
                    .addHeader("appSecret", "23948880c9d94b4834bc7b8396bdfad28504f")
                    .build();
            try {
                return chain.proceed(request);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
