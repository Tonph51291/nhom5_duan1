package com.bookstorepoly.nhom5_duan1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.ArrayList;

public class PhieuGiamGiaAdapter extends RecyclerView.Adapter<PhieuGiamGiaAdapter.PhieuGiamGiaViewHolder> {
    ArrayList<PhieuGiamGiaDTO> list;
    private Context context;
    public PhieuGiamGiaAdapter(ArrayList<PhieuGiamGiaDTO> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PhieuGiamGiaAdapter.PhieuGiamGiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phieugiamgia, parent, false);
        PhieuGiamGiaViewHolder holder = new PhieuGiamGiaViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhieuGiamGiaAdapter.PhieuGiamGiaViewHolder holder, int position) {
        holder.txt_ten_phieu.setText(list.get(position).getTenPhieuGiamGia());
        holder.txt_noidung.setText(list.get(position).getNoiDung());
        holder.txt_tg.setText(list.get(position).getNgayBatDau() + " - " + list.get(position).getNgayKetThuc());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PhieuGiamGiaViewHolder extends RecyclerView.ViewHolder {
        TextView txt_ten_phieu,txt_noidung, txt_tg;
        public PhieuGiamGiaViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ten_phieu = itemView.findViewById(R.id.txt_ten_phieu);
            txt_noidung = itemView.findViewById(R.id.txt_noi_dung);
            txt_tg = itemView.findViewById(R.id.txt_tg);
        }
    }
}
