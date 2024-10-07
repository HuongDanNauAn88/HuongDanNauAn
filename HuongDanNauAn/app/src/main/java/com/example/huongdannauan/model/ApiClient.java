package com.example.huongdannauan.model;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "https://api.spoonacular.com/";
    private static Retrofit retrofit = null;

    // Khởi tạo Retrofit
    public static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



    // Trả về đối tượng API service
    public static SpoonacularApiService getApiService() {
        return getClient().create(SpoonacularApiService.class);
    }
}
