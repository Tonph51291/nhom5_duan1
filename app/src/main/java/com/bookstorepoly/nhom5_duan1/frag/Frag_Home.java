package com.bookstorepoly.nhom5_duan1.frag;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.adapter.SachAdapter;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;

import java.util.ArrayList;

public class Frag_Home extends Fragment {
    private FragmentManager fragmentManager;
    private RecyclerView rcv_sach_moi;
    private SachAdapter sachAdapter;
    private SachDAO sachDAO;
    private ArrayList<SachDTO> listSach;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        // Initialize FragmentManager
        fragmentManager = getParentFragmentManager();

        Button btn_loaisach = view.findViewById(R.id.btn_loai_sach);
        Button btn_sach = view.findViewById(R.id.btn_sach);
        Button btn_khach_hang = view.findViewById(R.id.btn_khach_hang);
        Button btn_hoa_don = view.findViewById(R.id.btn_hoa_don);
        rcv_sach_moi = view.findViewById(R.id.rcv_top10_sach);

        btn_loaisach.setOnClickListener(v -> {
            // Replace the current fragment with Frag_LoaiSach
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, new Frag_LoaiSach()) // Ensure you have imported the Frag_LoaiSach class
                    .addToBackStack(null) // Optional: to add to back stack so you can navigate back
                    .commit();
        });
        btn_sach.setOnClickListener(v -> {
            // Replace the current fragment with Frag_Sach
            SharedPreferences sha = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sha.edit();
            editor.putInt("maLoaiSach", 0);
            editor.apply();

            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, new Frag_Sach())
                    .addToBackStack(null)
                    .commit();

        });
        btn_khach_hang.setOnClickListener(v -> {
            // Replace the current fragment with Frag_KhachHang
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, new Frag_KhachHang())
                    .addToBackStack(null)
                    .commit();

        });

        btn_hoa_don.setOnClickListener(v -> {
            // Replace the current fragment with Frag_HoaDon
            fragmentManager.beginTransaction()
                    .replace(R.id.frag_container, new Frag_HoaDon())
                    .addToBackStack(null)
                    .commit();

        });
        sachDAO = new SachDAO(getContext());
        listSach = new ArrayList<>();
        listSach.addAll(sachDAO.getTop5NewestBooks());
        sachAdapter = new SachAdapter(getContext(), listSach);
        rcv_sach_moi.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false ));
        rcv_sach_moi.setAdapter(sachAdapter);

        return view;
    }
}
