package com.bookstorepoly.nhom5_duan1.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.dao.NhanVienDAO;
import com.bookstorepoly.nhom5_duan1.dto.NhanVienDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.ArrayList;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {
    private Context context;
    private ArrayList<NhanVienDTO> list;

    public NhanVienAdapter(Context context, ArrayList<NhanVienDTO> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.item_nhanvien, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVienDTO nhanVienDTO = list.get(position);
        holder.tv_ma_nhanvien.setText("Mã nhân viên : " + nhanVienDTO.getMaNhanVien());
        holder.tv_ten_nhanvien.setText("Tên : " + nhanVienDTO.getTenNhanVien());
        holder.tv_sodienthoai.setText("SDT : " + nhanVienDTO.getSoDienThoai());

        // Thiết lập sự kiện khi nhấn vào nút Popup
        holder.btn_pop_nv.setOnClickListener(v -> {
            // Tạo Popup Menu khi nhấn nút
            PopupMenu popupMenu = new PopupMenu(context, v);
            popupMenu.inflate(R.menu.pop_menu_nv); // Inflate menu từ XML
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_update) {
                    updateNhanVien(nhanVienDTO);
                    return true;
                } else if (item.getItemId() == R.id.menu_change_status) {
                    changeStatus(nhanVienDTO);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Hàm cập nhật nhân viên
    private void updateNhanVien(NhanVienDTO nhanVienDTO) {
        showUpdateDialog(nhanVienDTO);
    }

    private void showUpdateDialog(NhanVienDTO nhanVienDTO) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.dialog_up_nv, null);
        dialog.setView(view);
        dialog.show();

        // Lấy các view từ layout
        TextView edt_tennv_up = view.findViewById(R.id.edt_tennv_up);
        TextView edt_sdt_nv_up = view.findViewById(R.id.edt_sdt_nv_up);
        TextView edt_stk_nv_up = view.findViewById(R.id.edt_stk_nv_up);
        TextView edt_luong_nv_up = view.findViewById(R.id.edt_luong_nv_up);
        RadioButton rdi_nghiviec = view.findViewById(R.id.rdi_nghiviec);
        RadioButton rdi_lamviec = view.findViewById(R.id.rdi_lamviec);
        Button btn_up_nv = view.findViewById(R.id.btn_up_nv);

        // Set các giá trị hiện tại vào các trường editText
        edt_tennv_up.setText(nhanVienDTO.getTenNhanVien());
        edt_sdt_nv_up.setText(nhanVienDTO.getSoDienThoai());
        edt_stk_nv_up.setText(nhanVienDTO.getSoTaiKhoan());
        edt_luong_nv_up.setText(String.valueOf(nhanVienDTO.getLuong()));

//        // Xử lý trạng thái và đánh dấu radio button
        if (nhanVienDTO.getTrangThai() == 0) {
            rdi_lamviec.setChecked(true);
            rdi_nghiviec.setChecked(false); // Nghỉ việc
        } else {
            rdi_lamviec.setChecked(false);  // Đang làm
            rdi_nghiviec.setChecked(true);  // Nghỉ việc
        }

        // Xử lý nút Cập nhật
        btn_up_nv.setOnClickListener(v -> {
            // Lấy thông tin từ các EditText
            String tenNhanVien = edt_tennv_up.getText().toString();
            String sdtNhanVien = edt_sdt_nv_up.getText().toString();
            String stkNhanVien = edt_stk_nv_up.getText().toString();
            String luongNhanVien = edt_luong_nv_up.getText().toString();

            // Validation kiểm tra các trường
            if (tenNhanVien.isEmpty()) {
                edt_tennv_up.setError("Tên nhân viên không được để trống");
                edt_tennv_up.requestFocus();
                return;
            }

            if (sdtNhanVien.isEmpty()) {
                edt_sdt_nv_up.setError("Số điện thoại không được để trống");
                edt_sdt_nv_up.requestFocus();
                return;
            }

            // Kiểm tra số điện thoại hợp lệ (chỉ chứa số và có độ dài hợp lệ)
            if (!sdtNhanVien.matches("^\\d{10}$")) {
                edt_sdt_nv_up.setError("Số điện thoại không hợp lệ");
                edt_sdt_nv_up.requestFocus();
                return;
            }

            if (stkNhanVien.isEmpty()) {
                edt_stk_nv_up.setError("Số tài khoản không được để trống");
                edt_stk_nv_up.requestFocus();
                return;
            }

            if (luongNhanVien.isEmpty()) {
                edt_luong_nv_up.setError("Lương không được để trống");
                edt_luong_nv_up.requestFocus();
                return;
            }

            // Kiểm tra lương có phải là một số hợp lệ
            try {
                Double.parseDouble(luongNhanVien);
            } catch (NumberFormatException e) {
                edt_luong_nv_up.setError("Lương không hợp lệ");
                edt_luong_nv_up.requestFocus();
                return;
            }

            // Cập nhật thông tin nhân viên vào đối tượng
            nhanVienDTO.setTenNhanVien(tenNhanVien);
            nhanVienDTO.setSoDienThoai(sdtNhanVien);
            nhanVienDTO.setSoTaiKhoan(stkNhanVien);
            nhanVienDTO.setLuong(Integer.parseInt(luongNhanVien));

//            // Nếu trạng thái là "nghỉ việc", set trạng thái = 1, ngược lại set = 0
            if (rdi_nghiviec.isChecked()) {
                nhanVienDTO.setTrangThai(1); // Nghỉ việc
            } else {
                nhanVienDTO.setTrangThai(0); // Đang làm
            }


            NhanVienDAO nhanVienDAO = new NhanVienDAO(context);
            int result = nhanVienDAO.UpdateNhanVien(nhanVienDTO);
            if (result == 0) {
                Toast.makeText(context, "Cập nhật nhân viên thất bại", Toast.LENGTH_SHORT).show();
                return;
            }
            list.clear();
            list.addAll(nhanVienDAO.getAllNhanVien());
            notifyDataSetChanged();


            // Thông báo cập nhật thành công
            Toast.makeText(context, "Cập nhật thành công nhân viên: " + tenNhanVien, Toast.LENGTH_SHORT).show();
            dialog.dismiss(); // Đóng dialog
        });
    }


    // Hàm thay đổi trạng thái nhân viên
    private void changeStatus(NhanVienDTO nhanVienDTO) {
        // Thực hiện thay đổi trạng thái (ví dụ, kích hoạt hoặc vô hiệu hóa nhân viên)
        int newStatus = (nhanVienDTO.getTrangThai() == 0) ? 1 : 0;
        nhanVienDTO.setTrangThai(newStatus);

        // Thông báo thay đổi trạng thái
        String status = newStatus == 0 ? "Đang làm" : "Nghỉ việc";
        Toast.makeText(context, "Thay đổi trạng thái nhân viên: " + nhanVienDTO.getTenNhanVien() + " - " + status, Toast.LENGTH_SHORT).show();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ma_nhanvien;
        TextView tv_ten_nhanvien;
        TextView tv_sodienthoai;
        ImageButton btn_pop_nv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ma_nhanvien = itemView.findViewById(R.id.tv_ma_nhanvien);
            tv_ten_nhanvien = itemView.findViewById(R.id.tv_ten_nhanvien);
            tv_sodienthoai = itemView.findViewById(R.id.tv_sdt_nhanvien);
            btn_pop_nv = itemView.findViewById(R.id.btn_pop_nv);
        }
    }
}
