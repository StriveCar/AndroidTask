package com.example.androidtask.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private volatile static RetrofitClient myInstance;

    private static final String BASE_URL = "http://47.107.52.7:88/member/photo/";

    OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)                   //设置请求超时时间
            .readTimeout(20, TimeUnit.SECONDS)                      //设置读取数据超时时间
            .writeTimeout(20, TimeUnit.SECONDS)                  //设置写入数据超时时间
            .addInterceptor(InterceptorUtil.LogInterceptor())              //绑定日志拦截器
            .addNetworkInterceptor(InterceptorUtil.HeaderInterceptor())       //绑定header拦截器
            .build();
    private Retrofit retrofit;

    private RetrofitClient() {

    }

    public static RetrofitClient getInstance() {
        if (myInstance == null) {
            synchronized (RetrofitClient.class) {
                if (myInstance == null) {
                    myInstance = new RetrofitClient();
                }
            }
        }
        return myInstance;
    }

    public <T> T getService(Class<T> cls) {
        return getRetrofit().create(cls);
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())    //设置gson转换器,将返回的json数据转为实体
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())       //设置CallAdapter
                    .baseUrl(BASE_URL).client(client)                                //设置客户端okhttp相关参数
                    .build();
        }
        return retrofit;
    }
}
