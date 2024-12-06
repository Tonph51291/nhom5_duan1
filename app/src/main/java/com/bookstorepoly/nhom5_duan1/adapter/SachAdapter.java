package com.bookstorepoly.nhom5_duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.frag.Frag_UpdateSach;
import com.bookstorepoly.nhom5_duan1.MainActivity;
import com.bookstorepoly.nhom5_duan1.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.SachViewHolder> {
    private List<SachDTO> sachList;
    private Context context;

    public SachAdapter(Context context, List<SachDTO> sachList) {
        this.sachList = sachList;
        this.context = context;
    }

    @NonNull
    @Override
    public SachViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_sach, null);
        return new SachViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SachViewHolder holder, int position) {
        SachDTO sach = sachList.get(position);
        holder.txtTenSach.setText("Tên Sách: " + sach.getTenSach());

        holder.txtGiaSach.setText("Giá sách: " + sach.getGiaSach());

        Bitmap bitmap = BitmapFactory.decodeFile(sach.getImgSachPath());
        if (bitmap != null) {
            holder.imgSach.setImageBitmap(bitmap);
        } else {
            holder.imgSach.setImageResource(R.drawable.sach1); // Placeholder image
        }

        // Thêm sự kiện long press vào item để hiển thị PopupMenu
        holder.itemView.setOnLongClickListener(v -> {
            showPopupMenu(v, sach);
            return true; // Trả về true để ngừng sự kiện mặc định (không hiển thị các hành động khác)
        });
    }

    @Override
    public int getItemCount() {
        return sachList.size();
    }

    private void showPopupMenu(View view, SachDTO sach) {
        // Tạo PopupMenu
        PopupMenu popupMenu = new PopupMenu(context, view);

        // Inflate menu từ file XML
        popupMenu.getMenuInflater().inflate(R.menu.menu_sach, popupMenu.getMenu());

        // Tùy chỉnh giao diện của PopupMenu (ví dụ: thay đổi background, kích thước)
        try {
            Field field = popupMenu.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object menuPopupHelper = field.get(popupMenu);
            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
            Method setForceShowIcon = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
            setForceShowIcon.invoke(menuPopupHelper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Thiết lập sự kiện click cho các item trong PopupMenu
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_update) {
                showUpdateDialog(sach);
                return true;
            } else if (item.getItemId() == R.id.menu_details) {
                showDetailsDialog(sach);
                return true;

            }
            return false;

        });

        // Hiển thị PopupMenu
        popupMenu.show();
    }

    private void showUpdateDialog(SachDTO sach) {
        FragmentManager fragmentManager = ((MainActivity) context).getSupportFragmentManager();

        // Tạo một đối tượng của Frag_UpdateSach
        Frag_UpdateSach frag_updateSach = new Frag_UpdateSach();

        // Truyền dữ liệu sách qua Bundle
        Bundle bundle = new Bundle();
        bundle.putInt("MaSach", sach.getMaSach());
        bundle.putString("TenSach", sach.getTenSach());
        bundle.putString("TacGia", sach.getTacGia());
        bundle.putInt("GiaSach", sach.getGiaSach());
        bundle.putInt("MaLoaiSach", sach.getLoaiSach());
        bundle.putInt("SoLuong", sach.getSoLuong());
        bundle.putString("ImgSachPath", sach.getImgSachPath());
        Log.d("SachAdapter", "ImgSachPath: " + sach.getImgSachPath());
        frag_updateSach.setArguments(bundle);

        // Chuyển đổi sang Frag_UpdateSach
        fragmentManager.beginTransaction()
                .replace(R.id.frag_container, frag_updateSach) // `fragment_container` là ID của FrameLayout chính
                .addToBackStack(null)
                .commit();
    }


    private void showDetailsDialog(SachDTO sach) {
        // Hiển thị thông tin chi tiết của sách
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông tin chi tiết sách");
        builder.setMessage("Tên sách: " + sach.getTenSach() + "\nSố lượng: " + sach.getSoLuong() + "\nGiá: " + sach.getGiaSach());
        builder.setPositiveButton("OK", null);
        builder.show();
    }

    class SachViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSach;
        TextView txtTenSach, txtGiaSach;

        public SachViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSach = itemView.findViewById(R.id.img_anh_sach);
            txtTenSach = itemView.findViewById(R.id.txt_ten_sach);
            txtGiaSach = itemView.findViewById(R.id.txt_gia_sach);

        }
    }
}
