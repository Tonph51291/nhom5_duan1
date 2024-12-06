package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bookstorepoly.nhom5_duan1.dto.HoaDonChiTietDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class ChiTietHoaDonDAO {
    private MyDbHelper dbHelper;
    private SQLiteDatabase dbChiTietHoaDon;

    public ChiTietHoaDonDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbChiTietHoaDon = dbHelper.getWritableDatabase();
    }

    public List<HoaDonChiTietDTO> getAllChiTietHoaDon() {
        String dl = "SELECT * FROM ChiTietHoaDon";
        List<HoaDonChiTietDTO> list = getData(dl);
        return list;
    }
    public List<HoaDonChiTietDTO> getChiTietHoaDonByMaHoaDon(int maHoaDon) {
        List<HoaDonChiTietDTO> list = new ArrayList<>();
        String query = "SELECT * FROM ChiTietHoaDon WHERE MaHoaDon = ?";
        Cursor cursor = dbChiTietHoaDon.rawQuery(query, new String[]{String.valueOf(maHoaDon)});

        if (cursor.moveToFirst()) {
            do {
                HoaDonChiTietDTO chiTiet = new HoaDonChiTietDTO();
                chiTiet.setMaHoaDonChiTiet(cursor.getInt(0));
                chiTiet.setMaHoaDon(cursor.getInt(1));
                chiTiet.setMaSach(cursor.getInt(2));
                chiTiet.setSoLuong(cursor.getInt(3));
                chiTiet.setGiaTien(cursor.getInt(4));
                list.add(chiTiet);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }



    public List<HoaDonChiTietDTO> getData(String dl, String... Args) {
        ArrayList<HoaDonChiTietDTO> list = new ArrayList<>();
        Cursor cursor = dbChiTietHoaDon.rawQuery(dl, Args);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        HoaDonChiTietDTO hoaDonChiTietDTO = new HoaDonChiTietDTO();
                       hoaDonChiTietDTO.setMaHoaDonChiTiet(cursor.getInt(0));
                        hoaDonChiTietDTO.setMaHoaDon(cursor.getInt(1));
                        hoaDonChiTietDTO.setMaSach(cursor.getInt(2));
                        hoaDonChiTietDTO.setSoLuong(cursor.getInt(3));
                        hoaDonChiTietDTO.setGiaTien(cursor.getInt(4));

                        list.add(hoaDonChiTietDTO);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return list;

    }
    public int AddChiTietHoaDon(HoaDonChiTietDTO hoaDonChiTietDTO) {
        ContentValues values = new ContentValues();
        values.put("MaHoaDon", hoaDonChiTietDTO.getMaHoaDon());
        values.put("MaSach", hoaDonChiTietDTO.getMaSach());
        values.put("SoLuong", hoaDonChiTietDTO.getSoLuong());
        values.put("GiaTienCT", hoaDonChiTietDTO.getGiaTien());
        long result = dbChiTietHoaDon.insert("ChiTietHoaDon", null, values);

        if (result == -1) {
            return 0;
        }
        return 1;

    }
    public int UpdateChiTietHoaDon(HoaDonChiTietDTO hoaDonChiTietDTO) {
        ContentValues values = new ContentValues();

        values.put("MaHoaDon", hoaDonChiTietDTO.getMaHoaDon());
        values.put("MaSach", hoaDonChiTietDTO.getMaSach());
        values.put("SoLuong", hoaDonChiTietDTO.getSoLuong());
        values.put("GiaTienCT", hoaDonChiTietDTO.getGiaTien());

        Log.d("UpdateChiTietHoaDon", "Updating record: MaCTHD=" + hoaDonChiTietDTO.getMaHoaDonChiTiet());

        long result = dbChiTietHoaDon.update("ChiTietHoaDon", values, "MaCTHD=?", new String[]{String.valueOf(hoaDonChiTietDTO.getMaHoaDonChiTiet())});

        if (result == -1) {
            Log.e("UpdateChiTietHoaDon", "Update failed for MaCTHD=" + hoaDonChiTietDTO.getMaHoaDonChiTiet());
            return 0;
        }
        Log.d("UpdateChiTietHoaDon", "Update successful for MaCTHD=" + hoaDonChiTietDTO.getMaHoaDonChiTiet());
        return 1;
    }

    public int DeleteChiTietHoaDon(int id) {

        long result = dbChiTietHoaDon.delete("ChiTietHoaDon", "MaCTHD=?", new String[]{String.valueOf(id)});
        Log.e("KQ Xoa", "KQ " + result);

        if (result == -1) {
            return 0;
        }
        return 1;
    }
    public int calculateTotalByMaHoaDon(int maHoaDon) {
        int total = 0;
        String query = "SELECT SUM(GiaTienCT) FROM ChiTietHoaDon WHERE MaHoaDon = ?";
        Cursor cursor = dbChiTietHoaDon.rawQuery(query, new String[]{String.valueOf(maHoaDon)});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    total = cursor.getInt(0); // Lấy tổng tiền
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cursor.close();
            }
        }
        return total;
    }

    // Trong HoaDonDAO.java
    public int getLatestMaHoaDonChiTiet() {
        int maHoaDon = 0;

        String query = "SELECT MAX(MaCTHD) FROM ChiTietHoaDon"; // Lấy mã hóa đơn lớn nhất
        Cursor cursor = dbChiTietHoaDon.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            maHoaDon = cursor.getInt(0);
            cursor.close();
        }
        return maHoaDon;
    }





}


