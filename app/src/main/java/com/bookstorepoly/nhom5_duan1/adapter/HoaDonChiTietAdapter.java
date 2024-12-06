package com.bookstorepoly.nhom5_duan1.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.dao.ChiTietHoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonChiTietDTO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;


import java.util.ArrayList;

public class HoaDonChiTietAdapter extends RecyclerView.Adapter<HoaDonChiTietAdapter.HoaDonChiTietViewHolder> {
    private Context context;
    private ArrayList<HoaDonChiTietDTO> list;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private TextView tvTotal;
    private TextView txtTongTienTatCa;
    private TextView txtSoTienGiam;


    public void setTvTotal(TextView tvTotal) {
        this.tvTotal = tvTotal;
    }
    public void setTxtTongTienTatCa(TextView txtTongTienTatCa) { this.txtTongTienTatCa = txtTongTienTatCa; }
    public void setTxtSoTienGiam(TextView txtSoTienGiam) { this.txtSoTienGiam = txtSoTienGiam; }


    public HoaDonChiTietAdapter(Context context, ArrayList<HoaDonChiTietDTO> list) {
        this.context = context;
        this.list = list;
        chiTietHoaDonDAO = new ChiTietHoaDonDAO(context);
    }

    @NonNull
    @Override
    public HoaDonChiTietViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoadon, parent, false);
        return new HoaDonChiTietViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonChiTietViewHolder holder, int position) {
        HoaDonChiTietDTO hoaDonChiTietDTO = list.get(position);
        SachDTO sachDTO = new SachDAO(context).getSachByMaSach(hoaDonChiTietDTO.getMaSach());


        Log.d("DEBUG", "MaHDCT tại vị trí " + position + ": " + hoaDonChiTietDTO.getMaHoaDonChiTiet());

        if (sachDTO != null) {
            holder.txtTenSach.setText(sachDTO.getTenSach());
        } else {
            holder.txtTenSach.setText("Sách không tìm thấy");  // Fallback if the book is not found
        }

        holder.txtSoLuong.setText(String.valueOf(hoaDonChiTietDTO.getSoLuong()));
        holder.txtGiaTien.setText(String.valueOf(hoaDonChiTietDTO.getGiaTien()));

        holder.imgDeletesp.setOnClickListener(v -> {
            int check = chiTietHoaDonDAO.DeleteChiTietHoaDon(hoaDonChiTietDTO.getMaHoaDonChiTiet());
            if (check > 0) {
                list.remove(position);
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                updateTotalAmount();

                notifyDataSetChanged();
            } else {
                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgEditsp.setOnClickListener(v -> {
            AlertDialog builder = new AlertDialog.Builder(context).create();
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_chon_sp_up, null);
            builder.setView(view);
            builder.show();
            AutoCompleteTextView edtTenSach = view.findViewById(R.id.edt_tensach_sp_up);
            EditText edtSoLuong = view.findViewById(R.id.edt_soluong_sp_up);
            Button btnUp = view.findViewById(R.id.btn_up_sp_hd);
            Button btnHuy = view.findViewById(R.id.btn_huyup_sp_hd);

            edtTenSach.setText(sachDTO.getTenSach());
            edtSoLuong.setText(String.valueOf(hoaDonChiTietDTO.getSoLuong()));

            SachDAO sachDAO = new SachDAO(context);
            ArrayList<SachDTO> listSach = (ArrayList<SachDTO>) sachDAO.getAllSach();
            SachHoaDon adapter = new SachHoaDon(context, listSach);
            edtTenSach.setAdapter(adapter);
            edtTenSach.setThreshold(1);
            edtTenSach.setOnItemClickListener((parent, view1, i, id) -> {
                SachDTO sach = (SachDTO) parent.getItemAtPosition(i);
                edtTenSach.setText(sach.getTenSach());
            });
            btnHuy.setOnClickListener(v1 -> builder.dismiss());
            btnUp.setOnClickListener(v1 -> {
                String tenSachMoi = edtTenSach.getText().toString().trim();
                String soLuongMoiStr = edtSoLuong.getText().toString().trim();

                if (tenSachMoi.isEmpty() || soLuongMoiStr.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                    return;
                }

                int soLuongMoi;
                try {
                    soLuongMoi = Integer.parseInt(soLuongMoiStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(context, "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SachDTO sachMoi = null;
                for (SachDTO sach : listSach) {
                    if (sach.getTenSach().equals(tenSachMoi)) {
                        sachMoi = sach;
                        break;
                    }
                }

                if (sachMoi == null) {
                    Toast.makeText(context, "Sách không tồn tại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                hoaDonChiTietDTO.setMaSach(sachMoi.getMaSach());
                hoaDonChiTietDTO.setSoLuong(soLuongMoi);
                hoaDonChiTietDTO.setGiaTien(sachMoi.getGiaSach() * soLuongMoi);

                int isUpdated = chiTietHoaDonDAO.UpdateChiTietHoaDon(hoaDonChiTietDTO);
                if (isUpdated > 0) {
                    list.set(position, hoaDonChiTietDTO);

                    notifyItemChanged(position);
                    updateTotalAmount();
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    builder.dismiss();
                } else {
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateTotalAmount() {
        double total = 0.0;
        for (HoaDonChiTietDTO item : list) {
            total += item.getGiaTien();
        }
        tvTotal.setText(String.format("%.2f", total));
    }


    class HoaDonChiTietViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTenSach;
        private TextView txtSoLuong;
        private TextView txtGiaTien;
        private ImageView imgEditsp, imgDeletesp;

        public HoaDonChiTietViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenSach = itemView.findViewById(R.id.txt_ten_sach_hd);
            txtSoLuong = itemView.findViewById(R.id.txt_soluong_hd);
            txtGiaTien = itemView.findViewById(R.id.txt_gia_hd);
            imgEditsp = itemView.findViewById(R.id.btn_update_sp);
            imgDeletesp = itemView.findViewById(R.id.btn_delete_sp);
        }
    }
}
