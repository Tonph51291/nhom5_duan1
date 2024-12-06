package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.HoaDonDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class HoaDonDAO {
    private MyDbHelper dbHelper;
    private SQLiteDatabase dbHoaDon;

    public HoaDonDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbHoaDon = dbHelper.getWritableDatabase();
    }

    public List<HoaDonDTO> getAllHoaDon() {
        String dl = "SELECT * FROM HoaDon";
        List<HoaDonDTO> list = getData(dl);
        return list;
    }
    // Lấy danh sách hóa đơn theo mã nhân viên
    public List<HoaDonDTO> getHoaDonByMaNhanVien(String maNhanVien) {
        String query = "SELECT * FROM HoaDon WHERE MaNhanVien = ?";
        return getData(query, maNhanVien);
    }


    private List<HoaDonDTO> getData(String dl, String... Args) {
        ArrayList<HoaDonDTO> list = new ArrayList<>();
        Cursor cursor = dbHoaDon.rawQuery(dl, Args);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        HoaDonDTO hoaDonDTO = new HoaDonDTO();
                        hoaDonDTO.setMaHoaDon(cursor.getInt(0));
                        hoaDonDTO.setMaNhanVien(cursor.getString(1));
                        hoaDonDTO.setMaKhachHang(cursor.getInt(2));
                        hoaDonDTO.setMaPhieuGiamGia(cursor.getInt(3));
                        hoaDonDTO.setNgayMua(cursor.getString(4));
                        hoaDonDTO.setTongTien(cursor.getInt(5));
                        hoaDonDTO.setGioMua(cursor.getString(6));
                        hoaDonDTO.setSoTienGiam(cursor.getInt(7));
                        list.add(hoaDonDTO);
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();


            }
        }
        return list;

    }
    public int AddHoaDon(HoaDonDTO hoaDonDTO) {
        ContentValues values = new ContentValues();
        values.put("MaNhanVien", hoaDonDTO.getMaNhanVien());
        values.put("MaKhachHang", hoaDonDTO.getMaKhachHang());
        values.put("MaPhieuGiamGia", hoaDonDTO.getMaPhieuGiamGia());
        values.put("NgayMua", hoaDonDTO.getNgayMua());
        values.put("TongTien", hoaDonDTO.getTongTien());
        values.put("GioMua", hoaDonDTO.getGioMua());
        values.put("SoTienGiam", hoaDonDTO.getSoTienGiam());
        long result = dbHoaDon.insert("HoaDon", null, values);
        if (result == -1) {
            return 0;
        }
        return 1;




    }
    // update
    public int UpdateHoaDon(HoaDonDTO hoaDonDTO) {
        ContentValues values = new ContentValues();
        values.put("MaHoaDon", hoaDonDTO.getMaHoaDon());
        values.put("MaPhieuGiamGia", hoaDonDTO.getMaPhieuGiamGia());
        values.put("TongTien", hoaDonDTO.getTongTien());
        values.put("SoTienGiam", hoaDonDTO.getSoTienGiam());

        long result = dbHoaDon.update("HoaDon", values, "MaHoaDon=?", new String[]{String.valueOf(hoaDonDTO.getMaHoaDon())});
        if (result == -1) {
            return 0;
        }
        return 1;
    }



    // Trong HoaDonDAO.java
    public int getLatestMaHoaDon() {
        int maHoaDon = 0;

        String query = "SELECT MAX(MaHoaDon) FROM HoaDon"; // Lấy mã hóa đơn lớn nhất
        Cursor cursor = dbHoaDon.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            maHoaDon = cursor.getInt(0);
            cursor.close();
        }
        return maHoaDon;
    }

    // delele hoa don
    public int DeleteHoaDon(int maHoaDon) {
        long result = dbHoaDon.delete("HoaDon", "MaHoaDon=?", new String[]{String.valueOf(maHoaDon)});
        if (result == -1) {
            return 0;
        }
        return 1;
    }
    public List<HoaDonDTO> searchHoaDonByMaNhanVien(String maNhanVien) {
        String query = "SELECT * FROM HoaDon WHERE MaNhanVien = ?";
        return getData(query, maNhanVien);
    }

    // Tính tổng doanh thu theo ngày
    public int getTotalRevenueByDay(String date) {
        String query = "SELECT SUM(TongTien) FROM HoaDon WHERE NgayMua = ?";
        Cursor cursor = dbHoaDon.rawQuery(query, new String[]{date});
        int total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        return total;
    }

    // Tính tổng doanh thu theo tuần
    public int getTotalRevenueByWeek(String startDate, String endDate) {
        String query = "SELECT SUM(TongTien) FROM HoaDon WHERE NgayMua BETWEEN ? AND ?";
        Cursor cursor = dbHoaDon.rawQuery(query, new String[]{startDate, endDate});
        int total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        return total;
    }

    // Tính tổng doanh thu theo tháng
    public int getTotalRevenueByMonth(String month) {
        String query = "SELECT SUM(TongTien) FROM HoaDon WHERE strftime('%Y-%m', NgayMua) = ?";
        Cursor cursor = dbHoaDon.rawQuery(query, new String[]{month});
        int total = 0;
        if (cursor != null && cursor.moveToFirst()) {
            total = cursor.getInt(0);
            cursor.close();
        }
        return total;
    }





}
