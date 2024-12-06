package com.bookstorepoly.nhom5_duan1.frag;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dao.NhanVienDAO;
import com.bookstorepoly.nhom5_duan1.dto.NhanVienDTO;

public class Frag_ThongTin extends Fragment {
    TextView tv_ten_nv, tv_ma_nv, tv_sdt_nv, tv_tk_nv;
    TextView txt_ten_nv;
    Button btn_up_ttcn;
    NhanVienDAO nhanVienDAO;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_thongtin, container, false);
        tv_ma_nv = view.findViewById(R.id.tv_ma_nhan_vien);
        tv_ten_nv = view.findViewById(R.id.tv_ten_nhan_vien);
        tv_sdt_nv = view.findViewById(R.id.tv_sdt_tt);
        tv_tk_nv = view.findViewById(R.id.tv_so_tai_khoan_tt);
        txt_ten_nv = view.findViewById(R.id.ten_nv_tt);
        btn_up_ttcn = view.findViewById(R.id.btn_up_ttcn);
        nhanVienDAO = new NhanVienDAO(getContext());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String maNhanVien = sharedPreferences.getString("MaNhanVien", "");
        String tenNhanVien = sharedPreferences.getString("TenNhanVien", "");
        String soDienThoai = sharedPreferences.getString("SoDienThoai", "");
        String soTaiKhoan = sharedPreferences.getString("SoTaiKhoan", "");



        txt_ten_nv.setText(tenNhanVien);
        tv_ma_nv.setText(maNhanVien);
        tv_ten_nv.setText(tenNhanVien);
        tv_sdt_nv.setText(soDienThoai);
        tv_tk_nv.setText(soTaiKhoan);


        btn_up_ttcn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_up_ttcn, null);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialog.show();
                EditText edt_ten_nv = dialogView.findViewById(R.id.edt_ten_cn);
                EditText edt_sdt_nv = dialogView.findViewById(R.id.edt_sdt_cn);
                EditText edt_stk_nv = dialogView.findViewById(R.id.edt_stk_cn);
                Button btn_up_nv = dialogView.findViewById(R.id.btn_up_nv);
                edt_ten_nv.setText(tenNhanVien);
                edt_sdt_nv.setText(soDienThoai);
                edt_stk_nv.setText(soTaiKhoan);

                btn_up_nv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tenMoi = edt_ten_nv.getText().toString();
                        String sdtMoi = edt_sdt_nv.getText().toString();
                        String stkMoi = edt_stk_nv.getText().toString();
                         if (tenMoi.isEmpty() || sdtMoi.isEmpty() || stkMoi.isEmpty()) {
                             edt_stk_nv.setError("Vui lòng nhập đầy đủ thông tin");
                             edt_sdt_nv.setError("Vui lòng nhập đầy đủ thông tin");
                             edt_ten_nv.setError("Vui lòng nhập đầy đủ thông tin");
                             return;
                         }
                         if (!sdtMoi.matches("\\d{10}")) {
                             edt_sdt_nv.setError("Số điện thoại phải có 10 chữ số");
                             return;

                         }
                         if (stkMoi.length()  > 6) {
                             edt_stk_nv.setError("Số tài khoản phải có 6 chữ số");
                             return;
                         }

                        NhanVienDTO nhanVienDTO = new NhanVienDTO();
                        nhanVienDTO.setMaNhanVien(maNhanVien);
                        nhanVienDTO.setTenNhanVien(tenMoi);
                        nhanVienDTO.setSoDienThoai(sdtMoi);
                        nhanVienDTO.setSoTaiKhoan(stkMoi);
                        int result = nhanVienDAO.UpdateNhanVien(nhanVienDTO);
                        if ( result > 0 ) {

                            dialog.dismiss();
                            txt_ten_nv.setText(tenMoi);
                            tv_ten_nv.setText(tenMoi);
                            tv_sdt_nv.setText(sdtMoi);
                            tv_tk_nv.setText(stkMoi);

                        }




                    }
                });





            }

        });



        return view;
    }
}
