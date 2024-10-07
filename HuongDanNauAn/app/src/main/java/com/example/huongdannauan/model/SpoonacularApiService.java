package com.example.huongdannauan.model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpoonacularApiService {
    @GET("recipes/complexSearch")
    Call<ResponseBody> getRecipes(
            @Query("query") String query,
            @Query("cuisine") String cuisine,
            @Query("instructionsRequired") boolean instructionsRequired,
            @Query("apiKey") String apiKey);

    @GET("recipes/complexSearch")
    Call<ResponseBody> getAllRecipes(
            @Query("number") int number,
            @Query("offset") int offset,
            @Query("apiKey") String apiKey
    );
}
