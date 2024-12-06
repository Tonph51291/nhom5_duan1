package com.bookstorepoly.nhom5_duan1;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.bookstorepoly.nhom5_duan1.frag.Frag_DanhSachPhieu;
import com.bookstorepoly.nhom5_duan1.frag.Frag_HoaDon;
import com.bookstorepoly.nhom5_duan1.frag.Frag_Home;
import com.bookstorepoly.nhom5_duan1.frag.Frag_KhachHang;
import com.bookstorepoly.nhom5_duan1.frag.Frag_KhoHang;
import com.bookstorepoly.nhom5_duan1.frag.Frag_KhuyenMai;
import com.bookstorepoly.nhom5_duan1.frag.Frag_LichSu;
import com.bookstorepoly.nhom5_duan1.frag.Frag_LoaiSach;
import com.bookstorepoly.nhom5_duan1.frag.Frag_NhanVien;
import com.bookstorepoly.nhom5_duan1.frag.Frag_Sach;
import com.bookstorepoly.nhom5_duan1.frag.Frag_ThongKe;
import com.bookstorepoly.nhom5_duan1.frag.Frag_ThongTin;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    Frag_KhachHang frag_khachhang;
    Frag_HoaDon frag_hoadon;
    Frag_LoaiSach frag_loaisach;
    Frag_Sach frag_sach;
    Frag_Home frag_home;
    Frag_LichSu frag_lichsu;
    Frag_ThongTin frag_thongtin;
    Frag_NhanVien frag_nhanvien;
    Frag_KhuyenMai frag_khuyenMai;
    Frag_DanhSachPhieu frag_danhsachphieu;
    Frag_KhoHang frag_khohang;
    Frag_ThongKe frag_thongKe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        View header = navigationView.getHeaderView(0);
        TextView tvTen = header.findViewById(R.id.tv_ten_nhan_vien_header);
        TextView tvSdt = header.findViewById(R.id.tv_sdt_header);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String ten = sharedPreferences.getString("TenNhanVien", "");
        String sdt = sharedPreferences.getString("SoDienThoai", "");
        tvTen.setText(ten);
        tvSdt.setText(sdt);





        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.chuoi_open,
                R.string.chuoi_close
        );
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();
        drawerLayout.addDrawerListener(drawerToggle);

        fragmentManager = getSupportFragmentManager();
        frag_khachhang = new Frag_KhachHang();
        frag_hoadon = new Frag_HoaDon();
        frag_loaisach = new Frag_LoaiSach();
        frag_sach = new Frag_Sach();
        frag_home = new Frag_Home();
        frag_lichsu = new Frag_LichSu();
        frag_thongtin = new Frag_ThongTin();
        frag_nhanvien = new Frag_NhanVien();
        frag_khuyenMai = new Frag_KhuyenMai();
        frag_danhsachphieu = new Frag_DanhSachPhieu();
        frag_khohang = new Frag_KhoHang();
        frag_thongKe = new Frag_ThongKe();


        fragmentManager.beginTransaction().replace(R.id.frag_container, frag_home).commit();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_trangchu) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_home).commit();
                    toolbar.setTitle("Trang chủ");

                } else if (item.getItemId() == R.id.nav_khohang) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_khohang).commit();
                    toolbar.setTitle("Quản lý kho hàng");


                } else if (item.getItemId() == R.id.nav_sach) {
                    SharedPreferences sha = getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sha.edit();
                    editor.putInt("maLoaiSach", 0);
                    editor.apply();
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_sach).commit();
                    toolbar.setTitle("Sách");

                } else if (item.getItemId() == R.id.nav_khachhang) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_khachhang).commit();
                    toolbar.setTitle("Khách hàng");

                } else if (item.getItemId() == R.id.nav_hoadon) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_hoadon).commit();
                    toolbar.setTitle("Hóa đơn");

                } else if (item.getItemId() == R.id.nav_nhanvien) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_nhanvien).commit();
                    toolbar.setTitle("Quản lý nhân viên");

                } else if (item.getItemId() == R.id.nav_phieugiamgia) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_danhsachphieu).commit();
                } else if (item.getItemId() == R.id.nav_thongke) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_thongKe).commit();
                    toolbar.setTitle("Thống kê doanh thu");
                } else if (item.getItemId() == R.id.nav_loaisach) {
                    fragmentManager.beginTransaction().replace(R.id.frag_container, frag_loaisach).commit();
                    toolbar.setTitle("Loại sách");
                } else if (item.getItemId() == R.id.nav_dangxuat) {
                    dangxuat();

                }
                drawerLayout.close();
                return true;
            }
                private void dangxuat() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Xác nhận");
                    builder.setMessage("Bạn có muốn đăng xuất không?");
                    builder.setPositiveButton("Có", (dialog, which) -> {
                        startActivity(new Intent(MainActivity.this, DangNhap.class));
                        finish();
                    });
                    builder.setNegativeButton("Không", (dialog, which) -> {
                        dialog.dismiss();


                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();



            }
        });

        int quyen = sharedPreferences.getInt("Quyen", 0);
        Log.d("MainActivity", "Quyen: " + quyen);
        if (quyen != 1 ) {
            navigationView.getMenu().findItem(R.id.nav_phieugiamgia).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_thongke).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_khohang).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_nhanvien).setVisible(false);

        }

        bottomNavigationView.setOnItemReselectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                fragmentManager.beginTransaction().replace(R.id.frag_container, frag_home).commit();
                toolbar.setTitle("Trang chủ");
            } else if (item.getItemId() == R.id.nav_lichsu) {
                fragmentManager.beginTransaction().replace(R.id.frag_container, frag_lichsu).commit();
                toolbar.setTitle("Lịch sử hóa đơn");

            } else if (item.getItemId() == R.id.nav_canhan) {
                fragmentManager.beginTransaction().replace(R.id.frag_container, frag_thongtin).commit();
                toolbar.setTitle("Thông tin cá nhân");
            }
        });


    }
}