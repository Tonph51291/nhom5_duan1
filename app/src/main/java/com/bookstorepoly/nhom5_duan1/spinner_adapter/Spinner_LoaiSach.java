package com.bookstorepoly.nhom5_duan1.spinner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.List;

public class Spinner_LoaiSach extends ArrayAdapter<LoaiSachDTO> {
    private Context context;
    private List<LoaiSachDTO> listLoaiSach;

    public Spinner_LoaiSach(@NonNull Context context, List<LoaiSachDTO> listLoaiSach) {
        super(context, 0, listLoaiSach);
        this.context = context;
        this.listLoaiSach = listLoaiSach;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = View.inflate(context, R.layout.item_sp_sach, null);
        }
        LoaiSachDTO loaiSachDTO = listLoaiSach.get(position);
        if (loaiSachDTO != null) {
            ((TextView) v.findViewById(R.id.txt_ten_loai_sach_sp)).setText(loaiSachDTO.getTenLoaiSach());
        }
        return v;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = View.inflate(context, R.layout.item_sp_sach, null);
        }
        LoaiSachDTO loaiSachDTO = listLoaiSach.get(position);
        if (loaiSachDTO != null) {
            ((TextView) v.findViewById(R.id.txt_ten_loai_sach_sp)).setText(loaiSachDTO.getTenLoaiSach());
        }
        return v;
    }
}
