package com.example.huongdannauan.model;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.huongdannauan.R;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TienIch {
    public static final String BASE_URL = "https://api.spoonacular.com/";
    public static final String API_KEY = "e5998f1d0cec429ba75bcbcf275c4cae";
    public static void loadImage(Context context, ImageView imageView, String imageUrl) {
        Glide.with(context)
                .load(imageUrl)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.icon_loading) // Hình ảnh hiển thị trong khi tải
                        .error(R.drawable.icon_error)) // Hình ảnh hiển thị nếu có lỗi
                .into(imageView);
    }


    private static SpoonacularApiService apiService = ApiClient.getApiService();

    public static void getRecipes(String query, String cuisine, boolean instructionsRequired, String apiKey, final ApiCallback callback) {
        Call<ResponseBody> call = apiService.getRecipes(query, cuisine, instructionsRequired, apiKey);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        callback.onSuccess(responseBody);
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                } else {
                    callback.onError("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public static void getAllRecipes(int number, int offset, String apiKey, final ApiCallback callback) {
        Call<ResponseBody> call = apiService.getAllRecipes(number, offset, apiKey);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseBody = response.body().string();
                        callback.onSuccess(responseBody);
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                } else {
                    callback.onError("Error code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface ApiCallback {
        void onSuccess(String result);
        void onError(String error);
    }

}
