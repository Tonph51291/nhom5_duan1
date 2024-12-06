package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.NhanVienDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class NhanVienDAO {
    Context context;
    MyDbHelper dbHelper;
    SQLiteDatabase dbNhanVien;

    public NhanVienDAO(Context context) {
        this.context = context;
        dbHelper = new MyDbHelper(context);
        dbNhanVien = dbHelper.getWritableDatabase();
    }

    public List<NhanVienDTO> getAllNhanVien() {
        String dl = "SELECT * FROM NhanVien";
        List<NhanVienDTO> list = getData(dl);
        return list;
    }

    public List<NhanVienDTO> getData(String dl, String... Args) {
        ArrayList<NhanVienDTO> list = new ArrayList<>();
        Cursor cursor = dbNhanVien.rawQuery(dl, Args);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        NhanVienDTO nhanVienDTO = new NhanVienDTO();
                        nhanVienDTO.setMaNhanVien(cursor.getString(0));
                        nhanVienDTO.setTenNhanVien(cursor.getString(1));
                        nhanVienDTO.setSoDienThoai(cursor.getString(2));
                        nhanVienDTO.setLuong(cursor.getInt(3));
                        nhanVienDTO.setHoaHong(cursor.getInt(4));
                        nhanVienDTO.setSoTaiKhoan(cursor.getString(5));
                        nhanVienDTO.setQuyen(cursor.getInt(6));
                        nhanVienDTO.setMatKhau(cursor.getString(7));
                        list.add(nhanVienDTO);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        return list;
    }

    public NhanVienDTO DangNhap(String maNhanVien, String matKhau) {
        NhanVienDTO nhanVienDTO = null;
        Cursor cursor = dbNhanVien.rawQuery("SELECT * FROM NhanVien WHERE MaNhanVien = ? AND MatKhau = ?", new String[]{maNhanVien, matKhau});
        if (cursor != null) {
            cursor.moveToFirst();
            SharedPreferences.Editor editor = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE).edit();
            editor.putString("MaNhanVien", cursor.getString(0));
            editor.putString("TenNhanVien", cursor.getString(1));
            editor.putString("SoDienThoai", cursor.getString(2));
            editor.putString("SoTaiKhoan", cursor.getString(5));
            editor.putInt("Quyen", cursor.getInt(6));
            editor.putInt("TrangThai", cursor.getInt(7));
            editor.apply();
            nhanVienDTO = new NhanVienDTO();
            nhanVienDTO.setMaNhanVien(cursor.getString(0));
            nhanVienDTO.setTenNhanVien(cursor.getString(1));
            nhanVienDTO.setSoDienThoai(cursor.getString(2));
            nhanVienDTO.setTrangThai(7);
            nhanVienDTO.setSoTaiKhoan(cursor.getString(5));
            cursor.close();


        }
        return nhanVienDTO;
    }

    public int AddNhanVien(NhanVienDTO nhanVienDTO) {
        ContentValues v = new ContentValues();
        v.put("MaNhanVien", nhanVienDTO.getMaNhanVien());
        v.put("TenNhanVien", nhanVienDTO.getTenNhanVien());
        v.put("SoDienThoai", nhanVienDTO.getSoDienThoai());

        long result = dbNhanVien.insert("NhanVien", null, v);
        return result == -1 ? 0 : (int) result;
    }

    public int UpdateNhanVien(NhanVienDTO nhanVienDTO) {
        ContentValues v = new ContentValues();
        v.put("TenNhanVien", nhanVienDTO.getTenNhanVien());
        v.put("SoDienThoai", nhanVienDTO.getSoDienThoai());
        v.put("SoTaiKhoan", nhanVienDTO.getSoTaiKhoan());
        v.put("Luong", nhanVienDTO.getLuong());
        v.put("TrangThai", nhanVienDTO.getTrangThai());
        int result = dbNhanVien.update("NhanVien", v, "MaNhanVien=?", new String[]{nhanVienDTO.getMaNhanVien()});
        return result;
    }

    public void saveLoginInfo(Context context, NhanVienDTO nhanVienDTO) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MaNhanVien", nhanVienDTO.getMaNhanVien());
        editor.putString("TenNhanVien", nhanVienDTO.getTenNhanVien());
        editor.putString("SoDienThoai", nhanVienDTO.getSoDienThoai());
        editor.putString("SoTaiKhoan", nhanVienDTO.getSoTaiKhoan());
        editor.apply();
    }

    public NhanVienDTO getLoginInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        NhanVienDTO nhanVienDTO = new NhanVienDTO();
        nhanVienDTO.setMaNhanVien(sharedPreferences.getString("MaNhanVien", ""));
        nhanVienDTO.setTenNhanVien(sharedPreferences.getString("TenNhanVien", ""));
        nhanVienDTO.setSoDienThoai(sharedPreferences.getString("SoDienThoai", ""));
        nhanVienDTO.setSoTaiKhoan(sharedPreferences.getString("SoTaiKhoan", ""));
        return nhanVienDTO;
    }

    // cập nhật tiên hoa hông
    public void updateHoaHong(String maNhanVien, int giaTriDonHang) {
        Cursor cursor = dbNhanVien.rawQuery("SELECT * FROM NhanVien WHERE MaNhanVien = ?", new String[]{maNhanVien});
        if (cursor != null && cursor.moveToFirst()) {
            int hoaHong = cursor.getInt(4);
            double hoaHongMoi = hoaHong + (giaTriDonHang * 0.1);
            ContentValues values = new ContentValues();
            values.put("HoaHong", hoaHongMoi);
            dbNhanVien.update("NhanVien", values, "MaNhanVien = ?", new String[]{maNhanVien});
            cursor.close();

        }
    }


}
