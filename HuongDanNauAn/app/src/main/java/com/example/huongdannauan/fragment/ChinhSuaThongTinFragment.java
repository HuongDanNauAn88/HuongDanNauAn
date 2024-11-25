package com.example.huongdannauan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.cloudinary.utils.ObjectUtils;
import com.example.huongdannauan.Interface.CloudinaryUtils;
import com.example.huongdannauan.R;
import com.example.huongdannauan.model.TrangThai;
import com.example.huongdannauan.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Map;

public class ChinhSuaThongTinFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 100;

    private ImageView imgView;
    private EditText edtName, edtEmail, edtAge;
    private RadioGroup radioGroupGender;
    private Button btnSave, btnSelectImage;

    private String uploadedImageUrl; // URL của ảnh đã upload
    private DatabaseReference mDatabase; // Firebase Database reference

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chinh_sua_thong_tin, container, false);
        String currentEmail = TrangThai.currentUser.getEmail();
        // Ánh xạ các thành phần
        imgView = view.findViewById(R.id.imgAnh);
        btnSelectImage = view.findViewById(R.id.btnAnh);
        edtName = view.findViewById(R.id.edtTen);
        edtEmail = view.findViewById(R.id.edMailEdit);
        edtAge = view.findViewById(R.id.edTuoi);
        radioGroupGender = view.findViewById(R.id.ttt);
        btnSave = view.findViewById(R.id.btnLuu);

        // Firebase Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("user");

        // Chọn ảnh từ thư viện
        btnSelectImage.setOnClickListener(v -> openImagePicker());

        // Lưu thông tin
        btnSave.setOnClickListener(v -> saveUserInfo());
        getUserInfoFromFirebase(currentEmail);
        return view;
    }

    // Mở bộ chọn ảnh
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String filePath = getPathFromUri(selectedImageUri);

            if (filePath != null) {
                File imageFile = new File(filePath);
                uploadImageToCloudinary(imageFile);
            } else {
                Toast.makeText(getContext(), "Không thể lấy đường dẫn ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Lấy đường dẫn từ URI
    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(columnIndex);
            cursor.close();
        }
        return path;
    }
    private void getUserInfoFromFirebase(String email) {
        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Lấy thông tin người dùng
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        if (user != null) {
                            // Hiển thị thông tin lên các trường
                            edtName.setText(user.getName());
                            edtEmail.setText(user.getEmail());
                            edtAge.setText(user.getAge());
                            Glide.with(getContext())
                                    .load(user.getAvatar())
                                    .into(imgView);

                            // Không cho sửa email
                            edtEmail.setEnabled(false);
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Upload ảnh lên Cloudinary
    private void uploadImageToCloudinary(File imageFile) {
        new Thread(() -> {
            try {
                // Upload ảnh
                Map uploadResult = CloudinaryUtils.getInstance().uploader().upload(imageFile, ObjectUtils.emptyMap());
                uploadedImageUrl = uploadResult.get("url").toString();

                // Hiển thị ảnh vừa upload
                getActivity().runOnUiThread(() -> {
                    Glide.with(getContext())
                            .load(uploadedImageUrl)
                            .into(imgView);

                    Toast.makeText(getContext(), "Upload ảnh thành công!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e("Cloudinary", "Upload failed: " + e.getMessage());
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Upload ảnh thất bại!", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }

    // Lưu thông tin người dùng vào Firebase
    private void saveUserInfo() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String age = edtAge.getText().toString().trim();
        final String gender;
        int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedGenderId != -1) {
            RadioButton selectedGender = getView().findViewById(selectedGenderId);
            gender = selectedGender.getText().toString();  // Gán giá trị cho gender
        } else {
            Toast.makeText(getContext(), "Vui lòng chọn giới tính!", Toast.LENGTH_SHORT).show();
            return;
        }


        // Kiểm tra thông tin đầu vào
        if (name.isEmpty() || email.isEmpty() || age.isEmpty() || uploadedImageUrl == null) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin và upload ảnh!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tham chiếu tới Firebase để kiểm tra email
        mDatabase.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Nếu email đã tồn tại, lấy user và cập nhật thông tin
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String userId = snapshot.getKey();  // Lấy ID của user đã tồn tại
                        User user = snapshot.getValue(User.class); // Lấy thông tin user hiện tại

                        // Cập nhật thông tin người dùng
                        if (user != null) {
                            user.setName(name);
                            user.setEmail(email);
                            user.setAge(age);
                            user.setGender(gender);
                            user.setAvatar(uploadedImageUrl);

                            // Cập nhật lại thông tin trong Firebase
                            mDatabase.child(userId).setValue(user)
                                    .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e -> {
                                        Log.e("Firebase", "Cập nhật thất bại: " + e.getMessage());
                                        Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                                    });
                        }
                    }
                } else {
                    String userId = mDatabase.push().getKey();
                    User newUser = new User(uploadedImageUrl, email, "", "", name, "", gender, age);

                    // Lưu vào Firebase
                    if (userId != null) {
                        mDatabase.child(userId).setValue(newUser)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Lưu thông tin thành công!", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> {
                                    Log.e("Firebase", "Lưu thất bại: " + e.getMessage());
                                    Toast.makeText(getContext(), "Lưu thất bại!", Toast.LENGTH_SHORT).show();
                                });
                    }
                }
                openAccountFragment(new AccountFragment());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi khi kiểm tra email: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openAccountFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

        if (getActivity() != null) {
            Log.d("DangNhapFragment", "Opening Account Fragment");
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Log.e("DangNhapFragment", "Activity is null, cannot open fragment");
        }
    }

}
