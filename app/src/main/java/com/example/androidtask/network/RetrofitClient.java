package com.example.androidtask.network;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private volatile static RetrofitClient myInstance;

    private static final String DEFAULT_BASE_URL = "http://47.107.52.7:88/member/photo/";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(InterceptorUtil.LogInterceptor())
            .addNetworkInterceptor(InterceptorUtil.HeaderInterceptor())
            .build();

    private final Map<String, Retrofit> retrofitMap = new HashMap<>();

    private RetrofitClient() {}

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
        return getService(DEFAULT_BASE_URL, cls);
    }

    public <T> T getService(String baseUrl, Class<T> cls) {
        return getRetrofit(baseUrl).create(cls);
    }

    private Retrofit getRetrofit(String baseUrl) {
        if (!retrofitMap.containsKey(baseUrl)) {
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .baseUrl(baseUrl)
                    .client(client)
                    .build();
            retrofitMap.put(baseUrl, retrofit);
        }
        return retrofitMap.get(baseUrl);
    }
}

