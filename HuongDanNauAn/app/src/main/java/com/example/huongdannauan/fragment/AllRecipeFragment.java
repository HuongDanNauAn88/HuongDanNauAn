package com.example.huongdannauan.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.huongdannauan.R;
import com.example.huongdannauan.adapter.AllRecipeAdapter;
import com.example.huongdannauan.adapter.RecipeAdapter;
import com.example.huongdannauan.model.Recipe;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllRecipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllRecipeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private AllRecipeAdapter adapterAllRecipe;
    private List<Recipe> recipeList = new ArrayList<>();
    private DatabaseReference databaseReference;
    private List<String> dishTypesList, vungmienList;
    private ProgressBar progressBar1;
    private Spinner spinnerLoai, spinnerVungMien;
    private ArrayAdapter<String> adapterSpinnerLoai, adapterSpinnerVungMien;
    private TextView resultCountText;
    private String locLoai="Tất cả", locVungMien="Tất cả";

    // TODO: Rename and change types of parameters
    private String mParam1;
    int lanSearch=0;
    private String mParam2;

    public AllRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param txtsearch Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRecipeFragment newInstance(String txtsearch, String param2) {
        AllRecipeFragment fragment = new AllRecipeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, txtsearch);
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

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_recipe, container, false);

        resultCountText = view.findViewById(R.id.resultCountText);
        resultCountText.setText("");

        recyclerView = view.findViewById(R.id.AllrecipeListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar1 = view.findViewById(R.id.progressBar1);
        spinnerLoai = view.findViewById(R.id.dishTypeSpinner);
        spinnerVungMien = view.findViewById(R.id.cuisineSpinner);

        // Adapter All Mon an
        adapterAllRecipe = new AllRecipeAdapter(getContext(), recipeList, recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView.setAdapter(adapterAllRecipe);

        // Firebase setup
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        loadRecipesFromFirebase();

        // Thiết lập spinner Loại
        dishTypesList = new ArrayList<>();
        adapterSpinnerLoai = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dishTypesList);
        adapterSpinnerLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(adapterSpinnerLoai);
        // Thiết lập spinner Vung Mien
        vungmienList = new ArrayList<>();
        adapterSpinnerVungMien = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, vungmienList);
        adapterSpinnerVungMien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerVungMien.setAdapter(adapterSpinnerVungMien);

        // Thêm listener cho spinner
        spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locLoai = dishTypesList.get(position);
                filterRecipesByType(locLoai, locVungMien);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có gì được chọn
            }
        });
        spinnerVungMien.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                locVungMien = vungmienList.get(position);
                filterRecipesByType(locLoai, locVungMien);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì nếu không có gì được chọn
            }
        });

        return view;
    }
    private void loadRecipesFromFirebase() {
        progressBar1.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                Set<String> uniqueDishTypes = new HashSet<>();
                Set<String> uniqueDishVungMien = new HashSet<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeList.add(recipe);
                        if(recipe.getDishTypes()!=null){
                            List<String> loai = recipe.getDishTypes();
                            uniqueDishTypes.addAll(loai);
                        }
                        if(recipe.getCuisines()!=null){
                            List<String> vungmien = recipe.getCuisines();
                            uniqueDishVungMien.addAll(vungmien);
                        }
                        progressBar1.setVisibility(View.GONE);
                    } catch (DatabaseException e) {
                        Log.e("FirebaseError", "Error deserializing data", e);
                        progressBar1.setVisibility(View.GONE);
                    }
                }
                adapterAllRecipe.notifyDataSetChanged();
                resultCountText.setText(recipeList.size()+" kết quả");

                dishTypesList.clear();
                dishTypesList.add("Tất cả");
                List<String> sortedDishTypesList = new ArrayList<>(uniqueDishTypes);
                Collections.sort(sortedDishTypesList);
                dishTypesList.addAll(sortedDishTypesList);
                adapterSpinnerLoai.notifyDataSetChanged();

                vungmienList.clear();
                vungmienList.add("Tất cả");
                List<String> sortedVungMienList = new ArrayList<>(uniqueDishVungMien);
                Collections.sort(sortedVungMienList);
                vungmienList.addAll(sortedVungMienList);
                adapterSpinnerVungMien.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void filterRecipesByType(String Lloai, String Lvungmien) {
        if(mParam1!=null && !mParam1.isEmpty() && lanSearch<2){
            filterKetQuaSearch();
            lanSearch++;
            return;
        }
        List<Recipe> filteredList = new ArrayList<>();
        if(Lloai.equals("Tất cả") && Lvungmien.equals("Tất cả")){
            filteredList = recipeList;
        } else if(Lloai.equals("Tất cả") && !Lvungmien.equals("Tất cả")){
            for (Recipe recipe : recipeList) {
                if (recipe.getCuisines() != null && recipe.getCuisines().contains(Lvungmien)) {
                    filteredList.add(recipe);
                }
            }
        }else if(!Lloai.equals("Tất cả") && Lvungmien.equals("Tất cả")){
            for (Recipe recipe : recipeList) {
                if (recipe.getDishTypes() != null && recipe.getDishTypes().contains(Lloai)) {
                    filteredList.add(recipe);
                }
            }
        } else {
            for (Recipe recipe : recipeList) {
                if (recipe.getDishTypes() != null && recipe.getCuisines() != null) {
                    if (recipe.getDishTypes().contains(Lloai) && recipe.getCuisines().contains(Lvungmien)) {
                        filteredList.add(recipe);
                    }
                }
            }
        }
        adapterAllRecipe = new AllRecipeAdapter(getContext(), filteredList, recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView.setAdapter(adapterAllRecipe);
        resultCountText.setText(filteredList.size()+" kết quả");
    }
    private void filterKetQuaSearch() {
        List<Recipe> filteredList = new ArrayList<>();

            for (Recipe recipe : recipeList) {
                if (recipe.getDishTypes() != null && recipe.getCuisines() != null) {
                    if (recipe.getTitle().toLowerCase().contains(mParam1.toLowerCase()) ||
                            recipe.getCuisines().contains(mParam1) ||
                            recipe.getDishTypes().contains(mParam1) ||
                            recipe.getSummary().toLowerCase().contains(mParam1.toLowerCase())
                    ) {
                        filteredList.add(recipe);
                    }
                }
            }

        adapterAllRecipe = new AllRecipeAdapter(getContext(), filteredList, recipeId -> {
            openRecipeDetailFragment(recipeId);
        });
        recyclerView.setAdapter(adapterAllRecipe);
        resultCountText.setText(filteredList.size()+" kết quả " + mParam1);
    }
    private void openRecipeDetailFragment(int recipeId) {
        ChiTietMonAnFragment chiTietMonAnFragment = ChiTietMonAnFragment.newInstance(String.valueOf(recipeId), "");
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, chiTietMonAnFragment)
                .addToBackStack(null)
                .commit();
    }
}