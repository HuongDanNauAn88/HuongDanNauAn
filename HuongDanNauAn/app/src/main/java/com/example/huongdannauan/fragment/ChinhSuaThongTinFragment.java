package com.example.huongdannauan.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import com.example.huongdannauan.R;
import com.example.huongdannauan.model.TrangThai;
import com.example.huongdannauan.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChinhSuaThongTinFragment extends Fragment {

    private static final int PICK_IMAGE = 1;

    private ImageView imgAnh;
    private Button btnAnh, btnLuu;
    private EditText edtTen, edMailEdit, edTuoi;
    private RadioGroup radioGroup;
    private RadioButton radioButtonNam, radioButtonNu;
    private Uri selectedImageUri;

    public ChinhSuaThongTinFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chinh_sua_thong_tin, container, false);

        // Initialize UI components
        imgAnh = view.findViewById(R.id.imgAnh);
        btnAnh = view.findViewById(R.id.btnAnh);
        btnLuu = view.findViewById(R.id.btnLuu);
        edtTen = view.findViewById(R.id.edtTen);
        edMailEdit = view.findViewById(R.id.edMailEdit);
        edTuoi = view.findViewById(R.id.edTuoi);
        radioButtonNam = view.findViewById(R.id.radioButton);
        radioButtonNu = view.findViewById(R.id.radioButton2);

        // Set the email from the current User object
        if (TrangThai.currentUser != null) {
            edMailEdit.setText(TrangThai.currentUser.getEmail());
        } else {
            edMailEdit.setText(""); // Handle case when there is no user
        }
        edMailEdit.setEnabled(false); // Disable editing of email

        // Set click listener for selecting image
        btnAnh.setOnClickListener(v -> openGallery());

        // Set click listener for save button
        btnLuu.setOnClickListener(v -> saveUserInfo());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            // Display selected image
            imgAnh.setImageURI(selectedImageUri);
        }
    }

    private void saveUserInfo() {
        // Get user information from input fields
        String name = edtTen.getText().toString().trim();
        String email = edMailEdit.getText().toString().trim(); // Email được lấy từ TrangThai
        String age = edTuoi.getText().toString().trim();

        // Get gender from radio buttons
        String gender = radioButtonNam.isChecked() ? "Nam" : "Nữ";

        // Validate input
        if (name.isEmpty() || age.isEmpty() || selectedImageUri == null) {
            Log.e("Validation", "Please fill all fields and select an image.");
            Toast.makeText(getActivity(), "Vui lòng điền đầy đủ thông tin và chọn ảnh.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Call method to update user information in Firebase
        ChangeInformation(selectedImageUri.toString(), name, email, gender, age);
    }

    private void ChangeInformation(String imageUrl, String name, String email, String gender, String age) {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
        String userId = TrangThai.currentUser.getEmail(); // Use the current user's ID for updating

        if (userId != null) {
            User updatedUser = new User(imageUrl, email, "", "", name, "", gender, age);
            database.child(userId).setValue(updatedUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firebase", "User updated successfully: " + name);
                    // Show success message
                    Toast.makeText(getActivity(), "Thông tin đã được lưu thành công!", Toast.LENGTH_SHORT).show();

                    // Navigate back to AccountFragment after a short delay
                    new android.os.Handler().postDelayed(() -> openAccountFragment(new AccountFragment()), 1000);
                } else {
                    Log.e("Firebase", "Failed to update user: " + task.getException().getMessage());
                    Toast.makeText(getActivity(), "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("Firebase", "Invalid user ID.");
        }
    }

    private void openAccountFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
