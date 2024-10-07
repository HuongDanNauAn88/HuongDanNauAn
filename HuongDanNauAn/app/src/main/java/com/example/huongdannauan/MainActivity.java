package com.example.huongdannauan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.example.huongdannauan.fragment.AccountFragment;
import com.example.huongdannauan.fragment.HomeFragment;
import com.example.huongdannauan.adapter.AdapterMonAnRecycleView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    ImageView buttonImage;
     RecyclerView recyclerView;
     AdapterMonAnRecycleView recipeAdapter;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


// TEST L1
//        // Tạo đối tượng Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        // Tạo đối tượng API service
//        SpoonacularApiService apiService = retrofit.create(SpoonacularApiService.class);
//
//        // Gọi API
//        Call<ResponseBody> call = apiService.getRecipes(
//                "Banh",
//                "Vietnamese",
//                true,
//                10,
//                API_KEY
//        );
//
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        String responseBody = response.body().string();
//                        txtTest.setText(responseBody);
//                    } catch (Exception e) {
//                        txtTest.setText(e.getMessage());
//                    }
//                } else {
//                    txtTest.setText("API Error: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                txtTest.setText(t.getMessage());
//            }
//        });

        // Gọi API thông qua ApiHelper
//        TienIch.getRecipes(
//                "Banh",  // query
//                "Vietnamese",  // cuisine
//                true,          // instructionsRequired
//                API_KEY,       // apiKey
//                new TienIch.ApiCallback() {
//                    @Override
//                    public void onSuccess(String result) {
//                        txtTest.setText("API Response: "+ result);
//                    }
//
//                    @Override
//                    public void onError(String error) {
//                        txtTest.setText("API Error"+ error);
//                    }
//                }
//        );

        //TEST L2
//        final String[] kq = {""};
//        // Gọi API lấy tất cả công thức nấu ăn
//        TienIch.getAllRecipes(10, 0, API_KEY, new TienIch.ApiCallback() {
//            @Override
//            public void onSuccess(String result) {
////                txtTest.setText("API Response: "+ result);
//                Gson gson = new GsonBuilder().create();
//                MonAnResponse recipeResponse = gson.fromJson(result, MonAnResponse.class);
//
//                // Lấy danh sách công thức
//                List<Recipe> recipes = recipeResponse.getResults();
//
//                // In ra danh sách công thức
//                for (Recipe recipe : recipes)
//                    kq[0] += "\nID: " + recipe.getId() + ", Title: " + recipe.getTitle() + ", Image: " + recipe.getImage();
//                txtTest.setText(kq[0]);
//            }
//
//            @Override
//            public void onError(String error) {
//                txtTest.setText("API Error"+ error);
//            }
//        });

//        TEST L3

//        // Gọi API lấy tất cả công thức nấu ăn
//        TienIch.getAllRecipes(100, 0, API_KEY, new TienIch.ApiCallback() {
//            @Override
//            public void onSuccess(String result) {
//                // Chuyển đổi JSON thành đối tượng RecipeResponse
//                Gson gson = new GsonBuilder().create();
//                MonAnResponse recipeResponse = gson.fromJson(result, MonAnResponse.class);
//
//                // Lấy danh sách công thức
//                List<Recipe> recipes = recipeResponse.getResults();
//
//                // Thiết lập Adapter cho RecyclerView
//                recipeAdapter = new AdapterMonAnRecycleView(MainActivity.this, recipes);
//                recyclerView.setAdapter(recipeAdapter);
//            }
//
//            @Override
//            public void onError(String error) {
//                Log.e("API Error", error);
//            }
//        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Đặt fragment mặc định khi mở ứng dụng
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_account) {
                    selectedFragment = new AccountFragment();
                }

                return loadFragment(selectedFragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}