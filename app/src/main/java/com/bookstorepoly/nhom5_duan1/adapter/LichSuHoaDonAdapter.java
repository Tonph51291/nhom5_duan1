package com.bookstorepoly.nhom5_duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dao.ChiTietHoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.HoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.KhachHangDAO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonChiTietDTO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonDTO;
import com.bookstorepoly.nhom5_duan1.dto.KhachHangDTO;

import java.util.ArrayList;

public class LichSuHoaDonAdapter extends RecyclerView.Adapter<LichSuHoaDonAdapter.LichSuHoaDonViewHolder> {
    private ArrayList<HoaDonDTO> list;
    private Context context;
    private KhachHangDAO khachHangDAO;
    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    public LichSuHoaDonAdapter(ArrayList<HoaDonDTO> list, Context context) {
        this.list = list;
        this.context = context;
        khachHangDAO = new KhachHangDAO(context);
        chiTietHoaDonDAO = new ChiTietHoaDonDAO(context);
    }

    @NonNull
    @Override
    public LichSuHoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_hon, parent, false);
        return new LichSuHoaDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LichSuHoaDonViewHolder holder, int position) {
        HoaDonDTO hoaDonDTO = list.get(position);
        KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangById(hoaDonDTO.getMaKhachHang());

        holder.txtMaHoaDonLs.setText( "Mã hóa đơn: "+hoaDonDTO.getMaHoaDon()+"    Mã nhân viên : "+hoaDonDTO.getMaNhanVien());
        holder.txtNgayMuaLs.setText("Ngay mua: "+hoaDonDTO.getNgayMua());
        holder.txtTongTienLs.setText("Tổng tiền: "+hoaDonDTO.getTongTien()+"");
        holder.txtTenKHLS.setText("Tên khách hàng: "+khachHangDTO.getTenKhachHang());



        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDialogHoaDon(hoaDonDTO);
            }
        });


    }

    private void showDialogHoaDon(HoaDonDTO hoaDonDTO) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.bill_hd, null);
        dialog.setView(view);
        dialog.show();
        TextView txt_ten_kh_bill = view.findViewById(R.id.txt_ten_kh_bill);
        TextView txt_sdt_kh_bill = view.findViewById(R.id.txt_sdt_kh_bill);
        TextView txt_ngay_mua_bill = view.findViewById(R.id.txt_ngay_mua_bill);
        TextView txt_tamtinh_bill = view.findViewById(R.id.txt_tamtinh_bill);
        TextView txt_giamgia_bill = view.findViewById(R.id.txt_giamgia_bill);
        TextView txt_thantien_bill = view.findViewById(R.id.txt_thantien_bill);
        RecyclerView rcv_san_pham_bill = view.findViewById(R.id.rcv_san_pham_bill);
        Button btn_in_hoa_don = view.findViewById(R.id.btn_in_hoa_don);
        int maHoaDon = hoaDonDTO.getMaHoaDon();

        KhachHangDTO khachHangDTO = khachHangDAO.getKhachHangById(hoaDonDTO.getMaKhachHang());
//        HoaDonChiTietDTO hoaDonChiTietDTO = (HoaDonChiTietDTO) chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);
        ArrayList<HoaDonChiTietDTO> list = new ArrayList<>();
        list.addAll(chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon));
        HoaDonBillAdapter hoaDonBillAdapter = new HoaDonBillAdapter(list, context);
        rcv_san_pham_bill.setLayoutManager(new LinearLayoutManager(context));
        rcv_san_pham_bill.setAdapter(hoaDonBillAdapter);



        txt_ten_kh_bill.setText(khachHangDTO.getTenKhachHang());
        txt_sdt_kh_bill.setText(khachHangDTO.getSoDienThoai());
        txt_ngay_mua_bill.setText(hoaDonDTO.getNgayMua());
        txt_thantien_bill.setText(hoaDonDTO.getTongTien()+"");
        txt_giamgia_bill.setText(hoaDonDTO.getSoTienGiam()+"");
        txt_tamtinh_bill.setText((hoaDonDTO.getTongTien()+hoaDonDTO.getSoTienGiam())+"");





        Log.d("maHoaDon",maHoaDon+"");
        btn_in_hoa_don.setVisibility(View.GONE);





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class LichSuHoaDonViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaHoaDonLs;
        TextView txtNgayMuaLs;
        TextView txtTongTienLs;
        TextView txtTenKHLS;

        public LichSuHoaDonViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaHoaDonLs = itemView.findViewById(R.id.txtMaHoaDonLS);
            txtNgayMuaLs = itemView.findViewById(R.id.txtNgayLS);
            txtTongTienLs = itemView.findViewById(R.id.txtTongTienLS);
            txtTenKHLS = itemView.findViewById(R.id.txtTenKHLS);

        }
    }
}
