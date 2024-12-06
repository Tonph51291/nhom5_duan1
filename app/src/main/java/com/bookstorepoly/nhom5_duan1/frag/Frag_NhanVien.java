package com.bookstorepoly.nhom5_duan1.frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.adapter.NhanVienAdapter;
import com.bookstorepoly.nhom5_duan1.dao.NhanVienDAO;
import com.bookstorepoly.nhom5_duan1.dto.NhanVienDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Frag_NhanVien extends Fragment {
    Button btn_them_nhanvien;
    FloatingActionButton fab_them_nhanvien;
    RecyclerView rcv_nhanvien;
    NhanVienAdapter nhanVienAdapter;
    NhanVienDAO nhanVienDAO;
    ArrayList<NhanVienDTO> listNhanVien;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_nhanvien, container, false);
        fab_them_nhanvien = view.findViewById(R.id.btn_them_nv);
        rcv_nhanvien = view.findViewById(R.id.rcv_nhanvien);

        nhanVienDAO = new NhanVienDAO(getContext());
        listNhanVien = new ArrayList<>(nhanVienDAO.getAllNhanVien());
        nhanVienAdapter = new NhanVienAdapter(getContext(), listNhanVien);
        rcv_nhanvien.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_nhanvien.setAdapter(nhanVienAdapter);

        fab_them_nhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddnv();
            }
        });

        return view;
    }

    private void showDialogAddnv() {
        if (getContext() == null) return;

        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_nv, null);
        dialog.setView(view);

        EditText edt_ma_nhanvien = view.findViewById(R.id.edt_ma_nhanvien);
        EditText edt_ten_nhanvien = view.findViewById(R.id.edt_tennv);
        EditText edt_sdt_nhanvien = view.findViewById(R.id.edt_sdt_nv);
        btn_them_nhanvien = view.findViewById(R.id.btn_add_nv);

        btn_them_nhanvien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maNhanVien = edt_ma_nhanvien.getText().toString().trim();
                String tenNhanVien = edt_ten_nhanvien.getText().toString().trim();
                String soDienThoai = edt_sdt_nhanvien.getText().toString().trim();


                if (maNhanVien.isEmpty() || tenNhanVien.isEmpty() || soDienThoai.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                NhanVienDTO nhanVienDTO = new NhanVienDTO();
                nhanVienDTO.setMaNhanVien(maNhanVien);
                nhanVienDTO.setTenNhanVien(tenNhanVien);
                nhanVienDTO.setSoDienThoai(soDienThoai);

                int result = nhanVienDAO.AddNhanVien(nhanVienDTO);
                if (result > 0) {
                    Toast.makeText(getContext(), "Thêm nhân viên thành công", Toast.LENGTH_SHORT).show();
                    listNhanVien.add(nhanVienDTO);
                    nhanVienAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                } else {
                    Toast.makeText(getContext(), "Thêm nhân viên thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
