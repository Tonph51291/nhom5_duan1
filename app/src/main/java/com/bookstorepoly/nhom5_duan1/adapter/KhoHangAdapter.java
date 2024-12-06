package com.bookstorepoly.nhom5_duan1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;

import java.util.List;

public class KhoHangAdapter extends RecyclerView.Adapter<KhoHangAdapter.KhoHangViewHolder> {
    private Context context;
    private List<SachDTO> sachDTOList;
    private SachDAO sachDAO;

    public KhoHangAdapter(Context context, List<SachDTO> sachDTOList) {
        this.context = context;
        this.sachDTOList = sachDTOList;
        sachDAO = new SachDAO(context);
    }

    @NonNull
    @Override
    public KhoHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_sp_khohang, null);
        return new KhoHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoHangViewHolder holder, int position) {
        SachDTO sachDTO = sachDTOList.get(position);
        holder.txt_ten_sach_kho.setText(sachDTO.getTenSach());
        if (sachDTO.getSoLuong() < 10) {
            holder.soluongsach_kho.setTextColor(context.getResources().getColor(android.R.color.holo_red_dark));
            holder.soluongsach_kho.setText("Số lượng : " + sachDTO.getSoLuong());
            holder.btn_nhap_hang.setBackgroundColor(context.getResources().getColor(android.R.color.holo_red_dark)); // Đổi màu nút nhập hàng
        } else {
            holder.soluongsach_kho.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
            holder.soluongsach_kho.setText("Số lượng : " + sachDTO.getSoLuong());
            holder.btn_nhap_hang.setBackgroundColor(context.getResources().getColor(android.R.color.holo_green_dark)); // Đổi màu nút nhập hàng
        }

        Bitmap bitmap = BitmapFactory.decodeFile(sachDTO.getImgSachPath());
        if (bitmap != null) {
            holder.img_anh_sach_kho.setImageBitmap(bitmap);
        } else {
            holder.img_anh_sach_kho.setImageResource(R.drawable.sach1); // Placeholder image
        }
        holder.btn_nhap_hang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                View view = View.inflate(context, R.layout.dialog_up_khohang, null);
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

                EditText edt_soluong_nhap = view.findViewById(R.id.edt_nhap_hang_moi);
                Button btn_up_kho = view.findViewById(R.id.btn_nhap_hang_up);
                edt_soluong_nhap.setText(String.valueOf(sachDTO.getSoLuong()));
                btn_up_kho.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputText = edt_soluong_nhap.getText().toString().trim();

                        // Validate the input
                        if (inputText.isEmpty()) {
                            edt_soluong_nhap.setError("Số lượng không được để trống!");
                            edt_soluong_nhap.requestFocus();
                            return;
                        }

                        try {
                            int soluongmoi = Integer.parseInt(inputText);

                            // Ensure quantity is not negative
                            if (soluongmoi < 0) {
                                edt_soluong_nhap.setError("Số lượng phải lớn hơn hoặc bằng 0!");
                                edt_soluong_nhap.requestFocus();
                                return;
                            }

                            // Update the quantity
                            sachDAO.UpdateKhoHang(sachDTO.getMaSach(), soluongmoi);
                            sachDTOList = sachDAO.getAllSach();

                            notifyDataSetChanged();
                            dialog.dismiss();
                            Toast.makeText(context, "Cập nhật kho hàng thành công!", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            edt_soluong_nhap.setError("Số lượng không hợp lệ!");
                            edt_soluong_nhap.requestFocus();
                        }



                    }
                });



            }
        });




    }

    @Override
    public int getItemCount() {
        return sachDTOList.size();
    }

    class  KhoHangViewHolder extends RecyclerView.ViewHolder {
        ImageView img_anh_sach_kho;
        TextView txt_ten_sach_kho;
        TextView soluongsach_kho;
        Button btn_nhap_hang;
        public KhoHangViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anh_sach_kho = itemView.findViewById(R.id.img_anh_sach_kho);
            txt_ten_sach_kho = itemView.findViewById(R.id.txt_ten_sach_kho);
            soluongsach_kho = itemView.findViewById(R.id.soluongsach_kho);
            btn_nhap_hang = itemView.findViewById(R.id.btn_nhap_hang);

        }

    }
}
