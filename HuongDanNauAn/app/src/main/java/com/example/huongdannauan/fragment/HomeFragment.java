package com.example.huongdannauan.fragment;



import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.huongdannauan.MainActivity;
import com.example.huongdannauan.R;
import com.example.huongdannauan.adapter.RecipeAdapter;
import com.example.huongdannauan.model.Recipe;
import com.example.huongdannauan.model.TienIch;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Gắn ID
        recyclerView1 = view.findViewById(R.id.recyclerViewHome1);
        recyclerView2 = view.findViewById(R.id.recyclerViewHome2);

        // Thiết lập ProgressDialog riêng cho mỗi RecyclerView
        ProgressDialog progressDialog1 = new ProgressDialog(getContext());
        progressDialog1.setMessage("Loading featured recipes...");
        progressDialog1.show();

        ProgressDialog progressDialog2 = new ProgressDialog(getContext());
        progressDialog2.setMessage("Loading Vietnamese recipes...");
        progressDialog2.show();

        // Cài đặt RecyclerView 1 - Món ăn Nổi bật
        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeList1 = new ArrayList<>();
        recipeAdapter1 = new RecipeAdapter(recipeList1, getContext(), recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView1.setAdapter(recipeAdapter1);

        databaseReference1 = FirebaseDatabase.getInstance().getReference("recipes");

        // Lấy dữ liệu cho RecyclerView 1
        databaseReference1.limitToFirst(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList1.clear();

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        if (recipe != null) {
                            recipeList1.add(recipe);
                        }
                    } catch (DatabaseException e) {
                        Log.e("FirebaseError", "Error deserializing data", e);
                    }
                }

                // Cập nhật Adapter và ẩn ProgressDialog
                recipeAdapter1.notifyDataSetChanged();
                progressDialog1.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                progressDialog1.dismiss();
            }
        });

        // Cài đặt RecyclerView 2 - Món ăn Việt Nam
        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recipeList2 = new ArrayList<>();
        recipeAdapter2 = new RecipeAdapter(recipeList2, getContext(), recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView2.setAdapter(recipeAdapter2);

        databaseReference2 = FirebaseDatabase.getInstance().getReference("recipes");

        // Lấy dữ liệu cho RecyclerView 2
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList2.clear();
                int count = 0;

                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    Recipe recipe = recipeSnapshot.getValue(Recipe.class);

                    if (recipe != null && recipe.getCuisines() != null && recipe.getCuisines().contains("Việt Nam")) {
                        recipeList2.add(recipe);
                        count++;
                    }

                    if (count == 10) {
                        break;
                    }
                }

                // Cập nhật Adapter và ẩn ProgressDialog
                recipeAdapter2.notifyDataSetChanged();
                progressDialog2.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                progressDialog2.dismiss();
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