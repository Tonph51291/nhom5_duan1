package com.bookstorepoly.nhom5_duan1.spinner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.List;

public class Spinner_PhieuGiamGia extends ArrayAdapter<PhieuGiamGiaDTO> {
    private Context context;
    private List<PhieuGiamGiaDTO> listPhieuGiamGia;

    public Spinner_PhieuGiamGia(@NonNull Context context, List<PhieuGiamGiaDTO> listPhieuGiamGia) {
        super(context, 0, listPhieuGiamGia);
        this.context = context;
        this.listPhieuGiamGia = listPhieuGiamGia;
    }

    // Hàm kiểm tra xem phiếu giảm giá có hợp lệ không


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = View.inflate(context ,R.layout.item_spinner_phieugiamgia, null);
        }
        PhieuGiamGiaDTO phieuGiamGiaDTO = listPhieuGiamGia.get(position);
        ((TextView) v.findViewById(R.id.txt_nd_phieu_spinner)).setText(phieuGiamGiaDTO.getNoiDung());

        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = View.inflate(context,R.layout.item_spinner_phieugiamgia, null);
        }
        PhieuGiamGiaDTO phieuGiamGiaDTO = listPhieuGiamGia.get(position);

            ((TextView) v.findViewById(R.id.txt_nd_phieu_spinner)).setText(phieuGiamGiaDTO.getNoiDung());

        return v;
    }
}
