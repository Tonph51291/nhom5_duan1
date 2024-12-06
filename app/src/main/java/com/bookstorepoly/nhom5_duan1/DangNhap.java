package com.bookstorepoly.nhom5_duan1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bookstorepoly.nhom5_duan1.dao.NhanVienDAO;
import com.bookstorepoly.nhom5_duan1.dto.NhanVienDTO;

public class DangNhap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dang_nhap);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        EditText edt_ma_nhan_vien = findViewById(R.id.edt_ma_nhan_vien);
        EditText edt_mat_khau = findViewById(R.id.edt_mat_khau);
        Button btn_dangnhap = findViewById(R.id.btn_dangnhap);

        btn_dangnhap.setOnClickListener(view -> {
            String maNhanVien = edt_ma_nhan_vien.getText().toString();
            String matKhau = edt_mat_khau.getText().toString();
            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

            int trangthai = sharedPreferences.getInt("TrangThai", 0);
            Log.d("TrangThai", "TrangThai: " + trangthai);

            if (trangthai == 1) {
                Toast.makeText(this, "Ban da nghi viec khong dang nhap dc", Toast.LENGTH_SHORT).show();
                return;
            }


            NhanVienDAO nhanVienDAO = new NhanVienDAO(DangNhap.this);
                NhanVienDTO nhanVienDTO = nhanVienDAO.DangNhap(maNhanVien, matKhau);

            if (nhanVienDTO != null) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("MaNhanVien", maNhanVien);

                startActivity(new Intent(DangNhap.this, MainActivity.class));
                Toast.makeText(this, "Đang nhập thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }

        });

    }
}