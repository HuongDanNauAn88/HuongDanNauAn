package com.example.huongdannauan.fragment;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.huongdannauan.R;
import com.example.huongdannauan.adapter.RecipeAdapter;
import com.example.huongdannauan.model.Recipe;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView1;
    private RecipeAdapter recipeAdapter1;
    private List<Recipe> recipeList1;
    private DatabaseReference databaseReference1;
    RecyclerView recyclerView2;
    private RecipeAdapter recipeAdapter2;
    private List<Recipe> recipeList2;
    private DatabaseReference databaseReference2;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        // Ẩn bàn phím

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ProgressBar progressBar1 = view.findViewById(R.id.progressBar1);
        progressBar1.setVisibility(View.VISIBLE);
        ProgressBar progressBar2 = view.findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.VISIBLE);

        // Gắn ID
        recyclerView1 = view.findViewById(R.id.recyclerViewHome1);
        recyclerView2 = view.findViewById(R.id.recyclerViewHome2);

        // Cài đặt RecyclerView 1 - Món ăn Nổi bật
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeList1 = new ArrayList<>();
        recipeList2 = new ArrayList<>();
        recipeAdapter1 = new RecipeAdapter(recipeList1, getContext(), recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recipeAdapter2 = new RecipeAdapter(recipeList2, getContext(), recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView1.setAdapter(recipeAdapter1);
        recyclerView2.setAdapter(recipeAdapter2);

        databaseReference1 = FirebaseDatabase.getInstance().getReference("recipes");

        // Lấy dữ liệu cho RecyclerView
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList1.clear();
                recipeList2.clear();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        if (recipeList1.size()<10 && recipe != null) {
                            recipeList1.add(recipe);
                        }
                        if (recipeList2.size()<10 && recipe != null && recipe.getCuisines() != null && recipe.getCuisines().contains("Việt Nam")) {
                            recipeList2.add(recipe);
                        }
                        if (recipeList1.size()==10 && recipeList2.size()==10) {
                            break;
                        }

                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);

                    } catch (DatabaseException e) {
                        Log.e("FirebaseError", "Error deserializing data", e);
                        progressBar1.setVisibility(View.GONE);
                        progressBar2.setVisibility(View.GONE);
                    }
                }

                // Cập nhật Adapter
                recipeAdapter1.notifyDataSetChanged();
                recipeAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    // Phương thức mở RecipeDetailFragment và truyền recipeId
    private void openRecipeDetailFragment(int recipeId) {
        ChiTietMonAnFragment chiTietMonAnFragment = ChiTietMonAnFragment.newInstance(String.valueOf(recipeId), "");
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, chiTietMonAnFragment)
                .addToBackStack(null)
                .commit();
    }

}