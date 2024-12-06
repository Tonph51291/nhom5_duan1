package com.bookstorepoly.nhom5_duan1.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.adapter.PhieuGiamGiaAdapter;
import com.bookstorepoly.nhom5_duan1.dao.PhieuGiamGiaDAO;
import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Frag_DanhSachPhieu extends Fragment {
    RecyclerView rcv_phieu;
    ArrayList<PhieuGiamGiaDTO> list_phieu;
    PhieuGiamGiaAdapter adapter_phieu;
    FloatingActionButton btn_them_phieu;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_danhsachphieu, container, false);
        rcv_phieu = view.findViewById(R.id.rcv_danhsachphieu);
        btn_them_phieu = view.findViewById(R.id.btn_them_phieu);
        fragmentManager = requireActivity().getSupportFragmentManager();
        list_phieu = new ArrayList<>();
        PhieuGiamGiaDAO phieuGiamGiaDAO = new PhieuGiamGiaDAO(getContext());
        list_phieu = (ArrayList<PhieuGiamGiaDTO>) phieuGiamGiaDAO.getAllPhieuGiamGia();
        adapter_phieu = new PhieuGiamGiaAdapter(list_phieu, getContext());
        rcv_phieu.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_phieu.setAdapter(adapter_phieu);

        btn_them_phieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frag_container, new Frag_KhuyenMai()).commit();

            }
        });


        return view;
    }
}
