package com.example.huongdannauan.fragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.huongdannauan.R;
import com.example.huongdannauan.adapter.CommentAdapter;
import com.example.huongdannauan.model.Comment;
import com.example.huongdannauan.model.Ingredient;
import com.example.huongdannauan.model.Instruction;
import com.example.huongdannauan.model.Recipe;
import com.example.huongdannauan.model.Step;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTietMonAnFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTietMonAnFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Recipe recipe;
    private DatabaseReference databaseReference;
    private ImageView imageDish;
    private TextView titleDish, summaryDish, preparationMinutes, cookingMinutes, healthScore, cuisines;
    private LinearLayout ingredientsContainer, loaimonanContainer, huongdanNauContainer;
    private RecyclerView recyclerViewComment;
    private CommentAdapter adapter;

    public ChiTietMonAnFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChiTietMonAnFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChiTietMonAnFragment newInstance(String param1, String param2) {
        ChiTietMonAnFragment fragment = new ChiTietMonAnFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chi_tiet_mon_an, container, false);
        // Ánh xạ các view
        imageDish = view.findViewById(R.id.imageDish);
        titleDish = view.findViewById(R.id.titleDish);
        summaryDish = view.findViewById(R.id.summaryDish);
        preparationMinutes = view.findViewById(R.id.preparationMinutes);
        cookingMinutes = view.findViewById(R.id.cookingMinutes);
        healthScore = view.findViewById(R.id.healthScore);
        cuisines = view.findViewById(R.id.cuisines);
        ingredientsContainer = view.findViewById(R.id.ingredientsContainer);
        loaimonanContainer = view.findViewById(R.id.LoaiMonAnContainer);
        huongdanNauContainer = view.findViewById(R.id.huongDanNauAnContainer);
        recyclerViewComment = view.findViewById(R.id.commentRecyclerView);



        // Tham chiếu tới Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference().child("recipes").child(mParam1);

        // Lấy dữ liệu món ăn từ Firebase
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Chuyển đổi dữ liệu Firebase thành đối tượng Recipe
                recipe = snapshot.getValue(Recipe.class);

                // Kiểm tra và cập nhật giao diện nếu recipe không null
                if (recipe != null) {
                    updateUI(recipe);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("RecipeDetailActivity", "Lỗi khi lấy dữ liệu", error.toException());
            }
        });

        return view;
    }
    private void updateUI(Recipe recipe) {
        Glide.with(this).load(recipe.getImage()).into(imageDish);
        titleDish.setText(recipe.getTitle());
        summaryDish.setText(recipe.getSummary());
        preparationMinutes.setText("Chuẩn bị: " + recipe.getPreparationMinutes() + " phút");
        cookingMinutes.setText("Nấu: " + recipe.getCookingMinutes() + " phút");
        healthScore.setText("Điểm sức khỏe: " + recipe.getHealthScore());

        cuisines.setText(TextUtils.join(", ", recipe.getCuisines()));


        // Thêm Loai mon an
        for (String loaimonan : recipe.getDishTypes()) {
            TextView loaiView = new TextView(getContext());
            loaiView.setText("- " + loaimonan);
            loaiView.setTextSize(22);
            loaimonanContainer.addView(loaiView);
        }

        // Thêm nguyên liệu
        for (Ingredient ingredient : recipe.getExtendedIngredients()) {
            TextView ingredientView = new TextView(getContext());
            ingredientView.setText("• " + ingredient.getName());
            ingredientView.setTextSize(22);
            ingredientsContainer.addView(ingredientView);
        }

        // Thêm huong dan nau an
        if(recipe.getAnalyzedInstructions()!=null){
            for (Instruction instruction : recipe.getAnalyzedInstructions()) {
                if(!instruction.getName().isEmpty()){
                    TextView ingredientView = new TextView(getContext());
                    ingredientView.setText("• "+instruction.getName() + ": ");
                    ingredientView.setTextSize(24);
                    ingredientView.setTextColor(Color.rgb(0, 153, 0));
                    huongdanNauContainer.addView(ingredientView);
                }
                if(instruction.getSteps()!=null){
                    for(Step step : instruction.getSteps()){
                        TextView ingredientView = new TextView(getContext());
                        ingredientView.setText("- Bước "+step.getNumber() + ": " + step.getStep());
                        ingredientView.setTextSize(22);
                        huongdanNauContainer.addView(ingredientView);
                    }
                }
            }
        }


//        if(recipe.getComments()!= null){
//            recyclerViewComment.setLayoutManager(new LinearLayoutManager(getContext()));
//            adapter = new CommentAdapter(recipe.getComments(), getContext());
//            recyclerViewComment.setAdapter(adapter);
//        }


    }

}