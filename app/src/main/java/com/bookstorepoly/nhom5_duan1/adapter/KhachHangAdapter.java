package com.bookstorepoly.nhom5_duan1.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dto.KhachHangDTO;

import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.KhachHangViewHolder> {
    private List<KhachHangDTO> khachHangDTOList;
    private Context context;

    public KhachHangAdapter(List<KhachHangDTO> khachHangDTOList, Context context) {
        this.khachHangDTOList = khachHangDTOList;
        this.context = context;
    }

    @NonNull
    @Override
    public KhachHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_khachhang, null);
        KhachHangViewHolder khachHangViewHolder = new KhachHangViewHolder(view);
        return khachHangViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangViewHolder holder, int position) {
        KhachHangDTO khachHangDTO = khachHangDTOList.get(position);
        holder.tv_ma_khach_hang.setText(String.valueOf(khachHangDTO.getMaKhachHang()));
        holder.tv_ten_khach_hang.setText(khachHangDTO.getTenKhachHang());
        holder.tv_sdt_khach_hang.setText(khachHangDTO.getSoDienThoai());

        holder.img_pop_khach_hang.setOnClickListener(v -> {
            // Tạo Popup Menu khi nhấn nút
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.pop_menu_kh); // Inflate menu từ XML
            popupMenu.setOnMenuItemClickListener(item -> {
                String phoneNumber = khachHangDTO.getSoDienThoai();

                if (item.getItemId() == R.id.menu_goi_dien) {
                    makePhoneCall(phoneNumber);
                    return true;
                } else if (item.getItemId() == R.id.menu_nhan_tin) {
                    sendSMS(phoneNumber);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return khachHangDTOList.size();
    }

    private void makePhoneCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền CALL_PHONE nếu chưa được cấp
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
        } else {
            context.startActivity(callIntent);
        }
    }

    private void sendSMS(String phoneNumber) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("sms:" + phoneNumber));
        smsIntent.putExtra("sms_body", "Nội dung tin nhắn của bạn");
        context.startActivity(smsIntent);
    }

    class KhachHangViewHolder extends RecyclerView.ViewHolder {

        TextView tv_ma_khach_hang, tv_ten_khach_hang, tv_sdt_khach_hang;
        ImageButton img_pop_khach_hang;

        public KhachHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ma_khach_hang = itemView.findViewById(R.id.tv_ma_khach_hang);
            tv_ten_khach_hang = itemView.findViewById(R.id.tv_ten_khach_hang);
            tv_sdt_khach_hang = itemView.findViewById(R.id.tv_sdt_khach_hang);
            img_pop_khach_hang = itemView.findViewById(R.id.btn_pop_khach_hang);
        }
    }
}
