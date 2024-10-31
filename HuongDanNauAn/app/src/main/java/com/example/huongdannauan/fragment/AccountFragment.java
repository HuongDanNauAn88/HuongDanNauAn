package com.example.huongdannauan.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.huongdannauan.R;
import com.example.huongdannauan.model.TrangThai;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {
    LinearLayout cnMonAnYeuThich, cnTinTucDaLuu, cnBinhLuanCuaToi, cnChinhSuaThongTin, cnDangXuat;
    TextView userEmailTextView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccountFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        userEmailTextView = view.findViewById(R.id.user_email);
        userEmailTextView.setText(TrangThai.currentUser.getEmail());
        cnMonAnYeuThich = view.findViewById(R.id.CNMonAnYeuThich);
        cnTinTucDaLuu = view.findViewById(R.id.CNTinTucDaLuu);
        cnBinhLuanCuaToi = view.findViewById(R.id.CNBinhLuanCuaToi);
        cnChinhSuaThongTin = view.findViewById(R.id.CNChinhSuaThongTin);
        cnDangXuat = view.findViewById(R.id.CNDangXuat);

        openFragmentOfUser(cnMonAnYeuThich, new MonAnYeuThichFragment(), false);
        openFragmentOfUser(cnTinTucDaLuu, new TinTucDaLuuFragment(), false);
        openFragmentOfUser(cnBinhLuanCuaToi, new BinhLuanCuaToiFragment(), false);
        openFragmentOfUser(cnChinhSuaThongTin, new ChinhSuaThongTinFragment(), false);
        openFragmentOfUser(cnDangXuat, new DangNhapFragment(), true);

        return view;
    }
    void openFragmentOfUser(LinearLayout linearLayout, Fragment fragment, boolean isDangXuat){
        if(isDangXuat) TrangThai.userEmail="";
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment); // fragment_container là id của container chứa fragment
                transaction.addToBackStack(null); // Thêm vào back stack để có thể quay lại FirstFragment
                transaction.commit();
            }
        });
    }
}