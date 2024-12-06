package com.bookstorepoly.nhom5_duan1.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.adapter.KhachHangAdapter;
import com.bookstorepoly.nhom5_duan1.dao.KhachHangDAO;
import com.bookstorepoly.nhom5_duan1.dto.KhachHangDTO;

import java.util.ArrayList;

public class Frag_KhachHang extends Fragment {
    private RecyclerView rcv_khachhang;
    private KhachHangAdapter khachHangAdapter;
    private KhachHangDAO khachHangDAO;
    private ArrayList<KhachHangDTO> khachHangDTOList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_khachhang, container, false);
        rcv_khachhang = view.findViewById(R.id.rcv_khachhang);

        khachHangDAO = new KhachHangDAO(getContext());
        khachHangDTOList = (ArrayList<KhachHangDTO>) khachHangDAO.getAllKhachHang();
        khachHangAdapter = new KhachHangAdapter(khachHangDTOList, getContext());
        rcv_khachhang.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_khachhang.setAdapter(khachHangAdapter);


        return view;
    }
}
