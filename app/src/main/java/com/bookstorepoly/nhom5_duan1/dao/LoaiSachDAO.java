package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;


import java.util.ArrayList;
import java.util.List;

public class LoaiSachDAO {
    MyDbHelper dbHelper;
    SQLiteDatabase dbLLoaiSach;

    public LoaiSachDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbLLoaiSach = dbHelper.getWritableDatabase();
    }

    public List<LoaiSachDTO> getAllLoaiSach() {
        String dl = "SELECT * FROM LoaiSach";
        List<LoaiSachDTO> list = getData(dl);
        return list;

    }

    public List<LoaiSachDTO> getData(String dl, String... Args) {
        ArrayList<LoaiSachDTO> list = new ArrayList<>();
        Cursor cursor = dbLLoaiSach.rawQuery(dl, Args);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                LoaiSachDTO loaiSachDTO = new LoaiSachDTO();
                loaiSachDTO.setMaLoaiSach(cursor.getInt(0));
                loaiSachDTO.setTenLoaiSach(cursor.getString(1));
                loaiSachDTO.setImgLoaiSachPath(cursor.getString(3));
                //Log.e("DBB",cursor.getString(2));
                loaiSachDTO.setSoLuongSach(cursor.getInt(2));
                list.add(loaiSachDTO);

            } while (cursor.moveToNext());
            cursor.close();


        }
        return list;

    }

    public int AddLoaiSach(LoaiSachDTO loaiSachDTO) {
        ContentValues values = new ContentValues();
        values.put("TenLoaiSach", loaiSachDTO.getTenLoaiSach());
        values.put("ImgLoaiSach", loaiSachDTO.getImgLoaiSachPath());
        values.put("SoLuongSach", loaiSachDTO.getSoLuongSach());
        return (int) dbLLoaiSach.insert("LoaiSach", null, values);

    }
    public int UpdateLoaiSach(LoaiSachDTO loaiSachDTO) {
        ContentValues values = new ContentValues();
        values.put("TenLoaiSach", loaiSachDTO.getTenLoaiSach());
        values.put("ImgLoaiSach", loaiSachDTO.getImgLoaiSachPath());
        values.put("SoLuongSach", loaiSachDTO.getSoLuongSach());
        return (int)  dbLLoaiSach.update("LoaiSach", values, "MaLoaiSach=?", new String[]{String.valueOf(loaiSachDTO.getMaLoaiSach())});
    }
    public List<LoaiSachDTO> searchLoaiSach(String keyword) {
        String query = "SELECT * FROM LoaiSach WHERE TenLoaiSach LIKE ?";
        return getData(query, "%" + keyword + "%");
    }






}
