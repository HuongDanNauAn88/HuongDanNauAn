package com.example.huongdannauan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.huongdannauan.fragment.AccountFragment;
import com.example.huongdannauan.fragment.DangNhapFragment;
import com.example.huongdannauan.fragment.HomeFragment;
import com.example.huongdannauan.model.TrangThai;
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

        // Kiểm tra nếu đã lưu user hiện tại sau khi người dùng đăng nhập
        if (TrangThai.currentUser != null && TrangThai.currentUser.getEmail() != null && !TrangThai.currentUser.getEmail().isEmpty()) {
            // Người dùng đã đăng nhập, tải AccountFragment
            loadFragment(new AccountFragment());
        } else {
            // Nếu chưa đăng nhập, tải HomeFragment làm mặc định
            if (savedInstanceState == null) {
                loadFragment(new HomeFragment());
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                int itemId = item.getItemId();
                if (itemId == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                } else if (itemId == R.id.nav_account) {
                    // Kiểm tra TrangThai.currentUser để xác định trạng thái đăng nhập
                    if (TrangThai.currentUser == null || TrangThai.currentUser.getEmail() == null || TrangThai.currentUser.getEmail().isEmpty()) {
                        // Chuyển đến trang đăng nhập nếu chưa đăng nhập
                        selectedFragment = new DangNhapFragment();
                    } else {
                        // Chuyển đến trang tài khoản nếu đã đăng nhập
                        selectedFragment = new AccountFragment();
                    }
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