package com.example.huongdannauan.fragment;



import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.huongdannauan.R;
import com.example.huongdannauan.adapter.AdapterMonAnRecycleView;
import com.example.huongdannauan.model.MonAnResponse;
import com.example.huongdannauan.model.MonAn;
import com.example.huongdannauan.model.TienIch;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    AdapterMonAnRecycleView recipeAdapter;
    AdapterMonAnRecycleView recipeAdapter2;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Lấy các món ăn
        recyclerView = view.findViewById(R.id.recyclerViewHome);
        recyclerView2 = view.findViewById(R.id.recyclerViewHome2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        TienIch.getAllRecipes(10, 0, TienIch.API_KEY, new TienIch.ApiCallback() {
            @Override
            public void onSuccess(String result) {
                // Chuyển đổi JSON thành đối tượng RecipeResponse
                Gson gson = new GsonBuilder().create();
                MonAnResponse recipeResponse = gson.fromJson(result, MonAnResponse.class);

                // Lấy danh sách công thức
                List<MonAn> monAns = recipeResponse.getResults();

                // Thiết lập Adapter cho RecyclerView
                recipeAdapter = new AdapterMonAnRecycleView(getContext(), monAns, new AdapterMonAnRecycleView.OnItemClickListener() {
                    @Override
                    public void onItemClick(MonAn monAn) {
                        // Xử lí chuyển fragment
                        // Chuyển đến DetailFragment và truyền dữ liệu
                        ChiTietMonAnFragment detailFragment = new ChiTietMonAnFragment();

                        // Tạo bundle để truyền dữ liệu
                        Bundle bundle = new Bundle();
                        bundle.putString("TitleMonAn", monAn.getTitle());
                        bundle.putInt("IdMonAn", monAn.getId());
                        // Thêm bất kỳ thông tin nào cần thiết

                        detailFragment.setArguments(bundle);

                        // Thực hiện chuyển fragment
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, detailFragment);
                        transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại HomeFragment
                        transaction.commit();
                    }
                });
                recyclerView.setAdapter(recipeAdapter);
            }

            @Override
            public void onError(String error) {

            }
        });

        // Lấy các loại
        recyclerView2 = view.findViewById(R.id.recyclerViewHome2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        TienIch.getAllRecipes(10, 2, TienIch.API_KEY, new TienIch.ApiCallback() {
            @Override
            public void onSuccess(String result) {
                // Chuyển đổi JSON thành đối tượng RecipeResponse
                Gson gson = new GsonBuilder().create();
                MonAnResponse recipeResponse = gson.fromJson(result, MonAnResponse.class);

                // Lấy danh sách công thức
                List<MonAn> monAns = recipeResponse.getResults();

                // Thiết lập Adapter cho RecyclerView
                recipeAdapter2 = new AdapterMonAnRecycleView(getContext(), monAns, new AdapterMonAnRecycleView.OnItemClickListener() {
                    @Override
                    public void onItemClick(MonAn monAn) {
                        // Xử lí chuyển fragment
                    }
                });
                recyclerView2.setAdapter(recipeAdapter2);
            }

            @Override
            public void onError(String error) {

            }
        });

        return view;
    }
}