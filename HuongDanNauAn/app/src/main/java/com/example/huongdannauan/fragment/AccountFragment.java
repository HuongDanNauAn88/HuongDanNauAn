package com.example.huongdannauan.fragment;


import android.net.Uri;

import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.huongdannauan.R;
import com.example.huongdannauan.model.TrangThai;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountFragment extends Fragment {
    LinearLayout cnMonAnYeuThich, cnTinTucDaLuu, cnBinhLuanCuaToi, cnChinhSuaThongTin, cnDangXuat;
    TextView userNameTextView, userEmailTextView;
    ImageView profileImageView;

    private DatabaseReference databaseReference;

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userNameTextView = view.findViewById(R.id.user_name); // Đảm bảo bạn có ID đúng
        userEmailTextView = view.findViewById(R.id.user_email);
        profileImageView = view.findViewById(R.id.profile_image); // Đảm bảo ID chính xác

        userEmailTextView.setText(TrangThai.currentUser.getEmail());
        cnMonAnYeuThich = view.findViewById(R.id.CNMonAnYeuThich);
        cnTinTucDaLuu = view.findViewById(R.id.CNTinTucDaLuu);
        cnBinhLuanCuaToi = view.findViewById(R.id.CNBinhLuanCuaToi);
        cnChinhSuaThongTin = view.findViewById(R.id.CNChinhSuaThongTin);
        cnDangXuat = view.findViewById(R.id.CNDangXuat);

        // Thêm sự kiện cho nút đăng xuất
        cnDangXuat.setOnClickListener(v -> {
            // Xóa thông tin người dùng
            TrangThai.currentUser = null; // Xóa thông tin người dùng
            TrangThai.userEmail = ""; // Xóa email người dùng

            // Chuyển hướng đến trang đăng nhập
            openFragmentOfUser(cnDangXuat ,new DangNhapFragment(), true);
        });

        // Gọi hàm để lấy thông tin người dùng
        getUserInfoByEmail();

        // Mở các fragment cho các LinearLayout khác
        openFragmentOfUser(cnMonAnYeuThich, new MonAnYeuThichFragment(), false);
        openFragmentOfUser(cnTinTucDaLuu, new TinTucDaLuuFragment(), false);
        openFragmentOfUser(cnBinhLuanCuaToi, new BinhLuanCuaToiFragment(), false);
        openFragmentOfUser(cnChinhSuaThongTin, new ChinhSuaThongTinFragment(), false);

        return view;
    }

    private void getUserInfoByEmail() {
        String emailToCheck = TrangThai.currentUser.getEmail();
        if (emailToCheck == null || emailToCheck.isEmpty()) return;

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (email != null && email.equals(emailToCheck)) {
                        String userName = userSnapshot.child("name").getValue(String.class);
                        String avatarUri = userSnapshot.child("avatar").getValue(String.class);

                        userNameTextView.setText(userName);
                        userEmailTextView.setText(email);

                        if (avatarUri != null && !avatarUri.isEmpty()) {
                            Glide.with(getContext())
                                    .load(Uri.parse(avatarUri))
                                    .into(profileImageView);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
            }
        });
    }
    void openFragmentOfUser(LinearLayout linearLayout, Fragment fragment, boolean isDangXuat) {

        if(isDangXuat) {
            TrangThai.userEmail="";
//            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.remove("userEmail"); // Xóa biến với khóa "userEmail"
//            editor.apply(); // Hoặc dùng editor.commit() nếu muốn đồng bộ ngay lập tức

        }

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }
}
