package com.bookstorepoly.nhom5_duan1.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.adapter.KhoHangAdapter;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;

import java.util.ArrayList;

public class Frag_KhoHang extends Fragment {
    RecyclerView rcv_khohang;
    KhoHangAdapter khoHangAdapter;
    ArrayList<SachDTO> sachDTOList ;
    SachDAO sachDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_khohang, container, false);
        rcv_khohang = view.findViewById(R.id.rcv_khohang);
        sachDAO = new SachDAO(getContext());
        sachDTOList = (ArrayList<SachDTO>) sachDAO.getAllSach();
        khoHangAdapter = new KhoHangAdapter(getContext(), sachDTOList);
        rcv_khohang.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcv_khohang.setAdapter(khoHangAdapter);
        return view;
    }
}
