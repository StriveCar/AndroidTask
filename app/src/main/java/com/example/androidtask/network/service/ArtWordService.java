package com.example.androidtask.network.service;

import com.example.androidtask.response.BaseResponse;
import com.example.androidtask.response.WordResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArtWordService {
    @GET("hsjz/index?key=3e699e893566e43357356247eb3c3079")
    Call<WordResponse> getArtWord();
}
