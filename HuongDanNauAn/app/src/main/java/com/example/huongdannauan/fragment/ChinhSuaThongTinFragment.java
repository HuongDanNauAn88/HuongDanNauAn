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

import com.example.huongdannauan.Interface.CloudflareApi;
import com.example.huongdannauan.R;
import com.example.huongdannauan.model.ImageResponseModel;
import com.example.huongdannauan.model.TrangThai;
import com.example.huongdannauan.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class ChinhSuaThongTinFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    public static final String API_TOKEN = "WplTTpteIRt8E9A7VoczMxGI9X8ZzW-QckrbEnPJ"; // API Token của bạn

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
        btnLuu.setOnClickListener(v -> {
            // Call API to verify token first
            verifyApiToken();
        });

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

    private void verifyApiToken() {
        // Set up Retrofit to verify API token
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cloudflare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CloudflareApi cloudflareApi = retrofit.create(CloudflareApi.class);
        String authHeader = "Bearer " + API_TOKEN;

        // Call the API to verify token
        Call<Void> call = cloudflareApi.verifyToken(authHeader);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Cloudflare", "API Token is valid!");
                    Toast.makeText(getActivity(), "API Token hợp lệ!", Toast.LENGTH_SHORT).show();
                    saveUserInfo();
                } else {
                    Log.e("Cloudflare", "API Token is invalid: " + response.message());
                    Toast.makeText(getActivity(), "API Token không hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Cloudflare", "API Token verification failed: " + t.getMessage());
                Toast.makeText(getActivity(), "Lỗi kết nối tới API Cloudflare", Toast.LENGTH_SHORT).show();
            }
        });
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

        // Upload image to Cloudflare and then save user info
        uploadImageToCloudflare(selectedImageUri, name, email, gender, age);
    }

    private void uploadImageToCloudflare(Uri imageUri, String name, String email, String gender, String age) {
        // Get the real path of the image URI
        File file = new File(getRealPathFromURI(imageUri)); // Method to get real path from URI

        // Create a request body for the image file
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // Set up Retrofit for Cloudflare API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cloudflare.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CloudflareApi cloudflareApi = retrofit.create(CloudflareApi.class);
        String authHeader = "Bearer " + API_TOKEN;

        // Call the API to upload the image
        Call<ImageResponseModel> call = cloudflareApi.uploadImage(authHeader, body);
        call.enqueue(new Callback<ImageResponseModel>() {
            @Override
            public void onResponse(Call<ImageResponseModel> call, Response<ImageResponseModel> response) {
                if (response.isSuccessful()) {
                    // Get the URL of the uploaded image
                    String imageUrl = response.body().getResult().getUrl();
                    saveImageUrlToFirebase(imageUrl, name, email, gender, age);
                } else {
                    Log.e("Cloudflare", "Image upload failed: " + response.message());
                    Toast.makeText(getActivity(), "Lỗi khi tải ảnh lên Cloudflare", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageResponseModel> call, Throwable t) {
                Log.e("Cloudflare", "Image upload failed: " + t.getMessage());
                Toast.makeText(getActivity(), "Lỗi kết nối đến Cloudflare", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveImageUrlToFirebase(String imageUrl, String name, String email, String gender, String age) {
        // Save the user info to Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("user");
        String userId = TrangThai.currentUser.getEmail(); // Get user ID from current session

        if (userId != null) {
            // Create a User object with the updated info
            User updatedUser = new User(imageUrl, email, "", "", name, "", gender, age);

            // Save user info to the database
            database.child(userId).setValue(updatedUser).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("Firebase", "User updated successfully: " + name);
                    Toast.makeText(getActivity(), "Thông tin đã được lưu thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Firebase", "Failed to update user: " + task.getException().getMessage());
                    Toast.makeText(getActivity(), "Lỗi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Log.e("Firebase", "Invalid user ID.");
        }
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            return filePath;
        } else {
            return null;
        }
    }
}
