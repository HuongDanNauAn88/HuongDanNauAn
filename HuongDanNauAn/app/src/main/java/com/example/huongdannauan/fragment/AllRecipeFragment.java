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
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
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
    private List<String> dishTypesList;
    private ProgressBar progressBar1;
    private Spinner spinnerLoai;
    private ArrayAdapter<String> adapterSpinnerLoai;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllRecipeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllRecipeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllRecipeFragment newInstance(String param1, String param2) {
        AllRecipeFragment fragment = new AllRecipeFragment();
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

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_recipe, container, false);

        recyclerView = view.findViewById(R.id.AllrecipeListView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        progressBar1 = view.findViewById(R.id.progressBar1);
        spinnerLoai = view.findViewById(R.id.dishTypeSpinner);

        adapterAllRecipe = new AllRecipeAdapter(getContext(), recipeList);
        recyclerView.setAdapter(adapterAllRecipe);

        // Firebase setup
        databaseReference = FirebaseDatabase.getInstance().getReference("recipes");
        loadRecipesFromFirebase();

        // Thiết lập spinner Loại
        dishTypesList = new ArrayList<>();
        adapterSpinnerLoai = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, dishTypesList);
        adapterSpinnerLoai.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoai.setAdapter(adapterSpinnerLoai);

        return view;
    }
    private void loadRecipesFromFirebase() {
        progressBar1.setVisibility(View.VISIBLE);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                Set<String> uniqueDishTypes = new HashSet<>();
                for (DataSnapshot recipeSnapshot : dataSnapshot.getChildren()) {
                    try {
                        Recipe recipe = recipeSnapshot.getValue(Recipe.class);
                        recipeList.add(recipe);
                        if(recipe.getDishTypes()!=null){
                            List<String> loai = recipe.getDishTypes();
                            uniqueDishTypes.addAll(loai);
                        }
                        progressBar1.setVisibility(View.GONE);
                    } catch (DatabaseException e) {
                        Log.e("FirebaseError", "Error deserializing data", e);
                        progressBar1.setVisibility(View.GONE);
                    }
                }
                adapterAllRecipe.notifyDataSetChanged();
                dishTypesList.clear();
                dishTypesList.addAll(uniqueDishTypes);
                adapterSpinnerLoai.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}