package com.bookstorepoly.nhom5_duan1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.dbHelper.MyDbHelper;

import java.util.ArrayList;
import java.util.List;

public class PhieuGiamGiaDAO {

    private MyDbHelper dbHelper;

    SQLiteDatabase dbNhanVien;


    public PhieuGiamGiaDAO(Context context) {
        dbHelper = new MyDbHelper(context);
        dbNhanVien = dbHelper.getWritableDatabase();
    }
    public List<PhieuGiamGiaDTO> getAllPhieuGiamGia() {
        String dl = "SELECT * FROM PhieuGiamGia";
        List<PhieuGiamGiaDTO> list = getData(dl);
        return list;
    }
    public List<PhieuGiamGiaDTO> getData(String dl, String... Args) {
        ArrayList<PhieuGiamGiaDTO> list = new ArrayList<>();
        Cursor cursor = dbNhanVien.rawQuery(dl, Args);
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        PhieuGiamGiaDTO phieuGiamGiaDTO = new PhieuGiamGiaDTO();
                        phieuGiamGiaDTO.setMaPhieuGiamGia(cursor.getInt(0));
                        phieuGiamGiaDTO.setMucGiamGia(cursor.getString(1));
                        phieuGiamGiaDTO.setDonHangToiDa(cursor.getInt(2));
                        phieuGiamGiaDTO.setDonHangToiThieu(cursor.getInt(3));
                        phieuGiamGiaDTO.setNgayBatDau(cursor.getString(4));
                        phieuGiamGiaDTO.setNgayKetThuc(cursor.getString(5));
                        phieuGiamGiaDTO.setTenPhieuGiamGia(cursor.getString(6));
                        phieuGiamGiaDTO.setNoiDung(cursor.getString(7));

                        phieuGiamGiaDTO.setSoLanSuDung(cursor.getInt(8));
                        phieuGiamGiaDTO.setSoTienGiamToiDa(cursor.getInt(9));

                        list.add(phieuGiamGiaDTO);


                    } while (cursor.moveToNext());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;

    }

    public int AddPhieuGiamGia(PhieuGiamGiaDTO phieuGiamGiaDTO) {
        ContentValues values = new ContentValues();
        values.put("MucGiamGia", phieuGiamGiaDTO.getMucGiamGia());
        values.put("DonHangToiThieu", phieuGiamGiaDTO.getDonHangToiThieu());
        values.put("NgayBatDau", phieuGiamGiaDTO.getNgayBatDau());
        values.put("NgayKetThuc", phieuGiamGiaDTO.getNgayKetThuc());
        values.put("TenPhieuGiamGia", phieuGiamGiaDTO.getTenPhieuGiamGia());
        values.put("NoiDung", phieuGiamGiaDTO.getNoiDung());
        values.put("SoLanSuDung", phieuGiamGiaDTO.getSoLanSuDung());
        values.put("DonHangToiDa", phieuGiamGiaDTO.getDonHangToiDa());
        values.put("SoTienGiamToiDa", phieuGiamGiaDTO.getSoTienGiamToiDa());

      return (int) dbNhanVien.insert("PhieuGiamGia", null, values);

    }


}
