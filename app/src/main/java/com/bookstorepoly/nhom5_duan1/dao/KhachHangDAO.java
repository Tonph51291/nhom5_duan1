package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.KhachHangDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {
    private MyDbHelper dbHelper;
    private SQLiteDatabase dbKhachHang;

    public KhachHangDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbKhachHang = dbHelper.getWritableDatabase();
    }

    public List<KhachHangDTO> getAllKhachHang() {
        String dl = "SELECT * FROM KhachHang";
        List<KhachHangDTO> list = getData(dl);
        return list;
    }

    public List<KhachHangDTO> getData(String dl, String... Args) {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        Cursor cursor = dbKhachHang.rawQuery(dl, Args);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        KhachHangDTO khachHangDTO = new KhachHangDTO();
                        khachHangDTO.setMaKhachHang(cursor.getInt(0));
                        khachHangDTO.setTenKhachHang(cursor.getString(1));
                        khachHangDTO.setSoDienThoai(cursor.getString(2));
                        khachHangDTO.setDiaChiKhachHang(cursor.getString(3));
                        khachHangDTO.setSoLanMua(cursor.getInt(4));

                        list.add(khachHangDTO);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();


            }


        }
        return list;

    }

    public int AddKhachHang(KhachHangDTO khachHangDTO) {
        ContentValues values = new ContentValues();

        values.put("TenKhachHang", khachHangDTO.getTenKhachHang());
        values.put("SoDienThoaiKH", khachHangDTO.getSoDienThoai());
        values.put("DiaChiKhachHang", khachHangDTO.getDiaChiKhachHang());
        values.put("SoLanMua", khachHangDTO.getSoLanMua());
        long result = dbKhachHang.insert("KhachHang", null, values);

        return (int) result ;


    }

    public KhachHangDTO getKhachHangBySDT(String soDienThoai) {

        Cursor cursor = dbKhachHang.rawQuery("SELECT * FROM KhachHang WHERE SoDienThoaiKH = ?", new String[]{soDienThoai});

        if (cursor != null && cursor.moveToFirst()) {
            KhachHangDTO khachHangDTO = new KhachHangDTO();
            khachHangDTO.setMaKhachHang(cursor.getInt(0));
            khachHangDTO.setTenKhachHang(cursor.getString(1));
            khachHangDTO.setSoDienThoai(cursor.getString(2));
            khachHangDTO.setDiaChiKhachHang(cursor.getString(3));
            khachHangDTO.setSoLanMua(cursor.getInt(4));

            cursor.close();
            return khachHangDTO;
        } else {
            return null; // Nếu không tìm thấy khách hàng
        }
    }

    public boolean isKhachHangExist(String sdt) {
        Cursor cursor = dbKhachHang.rawQuery("SELECT * FROM KhachHang WHERE SoDienThoaiKH = ?", new String[]{sdt});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public int getLatestMaKhachHang() {
        int maKhachHang = -1; // Giá trị mặc định nếu không tìm thấy

        Cursor c = dbKhachHang.rawQuery("SELECT MAX(MaKhachHang) FROM KhachHang", null);
        if (c != null && c.moveToFirst()) {
            maKhachHang = c.getInt(0); // Lấy giá trị đầu tiên trong kết quả
        }
        if (c != null) {
            c.close();
        }
        dbKhachHang.close();
        return maKhachHang;
    }
    // xoa khach hang

    public  int DeleteKhachHang(int maKhachHang) {
        int result = dbKhachHang.delete("KhachHang", "MaKhachHang=?", new String[]{String.valueOf(maKhachHang)});
        if (result == -1) {
            return 0;
        }
        return 1;
    }

    // lấy thông tin khách hàng theo id
    public KhachHangDTO getKhachHangById(int maKhachHang) {
        Cursor cursor = dbKhachHang.rawQuery("SELECT * FROM KhachHang WHERE MaKhachHang = ?", new String[]{String.valueOf(maKhachHang)});
        if (cursor != null && cursor.moveToFirst()) {
            KhachHangDTO khachHangDTO = new KhachHangDTO();
            khachHangDTO.setMaKhachHang(cursor.getInt(0));
            khachHangDTO.setTenKhachHang(cursor.getString(1));
            khachHangDTO.setSoDienThoai(cursor.getString(2));
            khachHangDTO.setDiaChiKhachHang(cursor.getString(3));
            khachHangDTO.setSoLanMua(cursor.getInt(4));
            cursor.close();
            return khachHangDTO;
        } else {
            return null; // Nếu không tìm thấy khách hàng


        }
    }
    // delete khach hang
    public int DeleteKhachHang(String soDienThoai) {
        int result = dbKhachHang.delete("KhachHang", "SoDienThoaiKH=?", new String[]{soDienThoai});
        if (result == -1) {
            return 0;
        }
        return 1;
    }



}
