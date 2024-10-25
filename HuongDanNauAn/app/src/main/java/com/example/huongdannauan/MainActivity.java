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
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {



    ImageView buttonImage;
     RecyclerView recyclerView;

    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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