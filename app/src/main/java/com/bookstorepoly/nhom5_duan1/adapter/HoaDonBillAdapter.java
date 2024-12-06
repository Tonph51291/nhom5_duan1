package com.bookstorepoly.nhom5_duan1.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonChiTietDTO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;


import java.util.ArrayList;

public class HoaDonBillAdapter extends RecyclerView.Adapter<HoaDonBillAdapter.HoaDonBillViewHolder> {
    ArrayList<HoaDonChiTietDTO> list;
    Context context;
    public HoaDonBillAdapter(ArrayList<HoaDonChiTietDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public HoaDonBillViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hoadon_bill, parent, false);
        return new HoaDonBillViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonBillViewHolder holder, int position) {
        HoaDonChiTietDTO hoaDonChiTietDTO = list.get(position);
        SachDTO sachDTO = new SachDAO(context).getSachByMaSach(hoaDonChiTietDTO.getMaSach());
        if (sachDTO != null) {
            holder.txtTenSachBill.setText(sachDTO.getTenSach());
        }
        holder.txtSoLuongBill.setText(String.valueOf(hoaDonChiTietDTO.getSoLuong()));
        holder.txtGiaTienBill.setText(String.valueOf(hoaDonChiTietDTO.getGiaTien()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HoaDonBillViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTenSachBill, txtSoLuongBill, txtGiaTienBill;

        public HoaDonBillViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSachBill = itemView.findViewById(R.id.txt_ten_sach_hd_bill);
            txtSoLuongBill = itemView.findViewById(R.id.txt_soluong_hd_bill);
            txtGiaTienBill = itemView.findViewById(R.id.txt_gia_hd_bill);


        }
    }
}
