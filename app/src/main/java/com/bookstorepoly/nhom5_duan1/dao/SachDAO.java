package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class SachDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase dbSach;

    public SachDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbSach = dbHelper.getWritableDatabase();
    }

    public List<SachDTO> getAllSach() {
        String dl = "SELECT * FROM Sach";
        List<SachDTO> list = getData(dl);
        return list;
    }

    public List<SachDTO> getData(String dl, String... Args) {
        ArrayList<SachDTO> list = new ArrayList<>();
        Cursor cursor = dbSach.rawQuery(dl, Args);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SachDTO sachDTO = new SachDTO();
                sachDTO.setMaSach(cursor.getInt(0));
                sachDTO.setLoaiSach(cursor.getInt(1));
                sachDTO.setTenSach(cursor.getString(2));
                sachDTO.setTacGia(cursor.getString(3));
                sachDTO.setSoLuong(cursor.getInt(5));
                sachDTO.setGiaSach(cursor.getInt(4));
                sachDTO.setImgSachPath(cursor.getString(6));
                sachDTO.setNamXuatBan(cursor.getInt(7));
                list.add(sachDTO);
            } while (cursor.moveToNext());


        }
        return list;

    }

    public int AddSach(SachDTO sachDTO) {
        ContentValues values = new ContentValues();
        values.put("MaLoaiSach", sachDTO.getLoaiSach());
        values.put("TenSach", sachDTO.getTenSach());
        values.put("TacGia", sachDTO.getTacGia());
        values.put("SoLuong", sachDTO.getSoLuong());
        values.put("Gia", sachDTO.getGiaSach());
        values.put("ImgSach", sachDTO.getImgSachPath());
        values.put("NamXuatBan", sachDTO.getNamXuatBan());
        return (int) dbSach.insert("Sach", null, values);
    }

    public int UpdateSach(SachDTO sachDTO) {
        ContentValues values = new ContentValues();
        values.put("MaLoaiSach", sachDTO.getLoaiSach());
        values.put("TenSach", sachDTO.getTenSach());
        values.put("TacGia", sachDTO.getTacGia());
        values.put("Gia", sachDTO.getGiaSach());
        values.put("ImgSach", sachDTO.getImgSachPath());  // Lưu đường dẫn ảnh
        values.put("NamXuatBan", sachDTO.getNamXuatBan());
        return dbSach.update("Sach", values, "MaSach=?", new String[]{String.valueOf(sachDTO.getMaSach())});


    }


    public List<SachDTO> searchSach(String keyword) {
        String query = "SELECT * FROM Sach WHERE TenSach LIKE ? OR TacGia LIKE ?";
        String searchKeyword = "%" + keyword + "%";
        return getData(query, searchKeyword, searchKeyword);
    }

    public List<SachDTO> getSachByLoaiSach(int loaiSachId) {
        ArrayList<SachDTO> list = new ArrayList<>();
        Cursor cursor = dbSach.rawQuery("SELECT * FROM Sach WHERE MaLoaiSach = ?", new String[]{String.valueOf(loaiSachId)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                SachDTO sachDTO = new SachDTO();
                sachDTO.setMaSach(cursor.getInt(0));
                sachDTO.setLoaiSach(cursor.getInt(1));
                sachDTO.setTenSach(cursor.getString(2));
                sachDTO.setTacGia(cursor.getString(3));
                sachDTO.setSoLuong(cursor.getInt(5));
                sachDTO.setGiaSach(cursor.getInt(4));
                sachDTO.setImgSachPath(cursor.getString(6));
                sachDTO.setNamXuatBan(cursor.getInt(7));
                list.add(sachDTO);
            } while (cursor.moveToNext());


        }
        cursor.close();
        return list;


    }
    public SachDTO getSachByMaSach(int maSach) {
        Cursor cursor = dbSach.query(
                "Sach",  // Table name
                new String[]{"MaSach", "TenSach", "Gia", "SoLuong", "ImgSach", "NamXuatBan"}, // Columns to fetch
                "MaSach = ?",  // WHERE clause
                new String[]{String.valueOf(maSach)},  // WHERE argument
                null,  // GROUP BY
                null,  // HAVING
                null   // ORDER BY
        );

        if (cursor != null && cursor.moveToFirst()) {
            SachDTO sachDTO = new SachDTO();
            sachDTO.setMaSach(cursor.getInt(0));
            sachDTO.setTenSach(cursor.getString(1));
            sachDTO.setGiaSach(cursor.getInt(2));
            sachDTO.setSoLuong(cursor.getInt(3));
            sachDTO.setImgSachPath(cursor.getString(4));
            sachDTO.setNamXuatBan(cursor.getInt(5));


            cursor.close();
            return sachDTO;
        }
        cursor.close();
        return null;  // Return null if no data found
    }

    public void updateQuantity(int maSach, int newQuantity) {
        Cursor cursor = dbSach.rawQuery("SELECT * FROM Sach WHERE MaSach = ?", new String[]{String.valueOf(maSach)});
        if (cursor != null && cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(5);
            int updatedQuantity = currentQuantity - newQuantity;
            ContentValues values = new ContentValues();
            values.put("SoLuong", updatedQuantity);
            dbSach.update("Sach", values, "MaSach = ?", new String[]{String.valueOf(maSach)});
            cursor.close();
        }
    }

    public void UpdateKhoHang(int maSach, int newQuantity) {
        Cursor cursor = dbSach.rawQuery("SELECT * FROM Sach WHERE MaSach = ?", new String[]{String.valueOf(maSach)});
        if (cursor != null && cursor.moveToFirst()) {
            int currentQuantity = cursor.getInt(5);
            int updatedQuantity = currentQuantity + newQuantity;
            ContentValues values = new ContentValues();
            values.put("SoLuong", updatedQuantity);
          int result =  dbSach.update("Sach", values, "MaSach = ?", new String[]{String.valueOf(maSach)});
            cursor.close();}

    }
    public List<SachDTO> getTop5NewestBooks() {
        // Lấy top 5 sách theo NamXuatBan giảm dần
        String query = "SELECT * FROM Sach ORDER BY NamXuatBan DESC LIMIT 5";
        return getData(query);
    }


}