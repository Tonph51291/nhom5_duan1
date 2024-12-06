package com.bookstorepoly.nhom5_duan1.dbHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "QuanLySach.db";
    public static int DB_VERSION = 2;


    public MyDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String tb_NhanVien = "CREATE TABLE NhanVien (" +
                "    MaNhanVien  TEXT    PRIMARY KEY" +
                "                        NOT NULL," +
                "    TenNhanVien TEXT," +
                "    SoDienThoai TEXT    UNIQUE," +
                "    Luong       INTEGER  DEFAULT 0," +
                "    HoaHong     INTEGER  DEFAULT 0," +
                "    SoTaiKhoan  TEXT    UNIQUE  DEFAULT 0," +
                "    Quyen       INTEGER  DEFAULT 0," + "  " +
                "  TrangThai       INTEGER  DEFAULT 0," +
                "    MatKhau     TEXT    NOT NULL DEFAULT '123' );" ;
        db.execSQL(tb_NhanVien);
        String tb_KhachHang = "CREATE TABLE KhachHang (\n" +
                "    MaKhachHang   INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    TenKhachHang  TEXT NOT NULL,\n" +
                "    SoDienThoaiKH TEXT NOT NULL UNIQUE,\n" +
                "    DiaChiKhachHang TEXT NOT NULL,\n" +
                "    SoLanMua      INTEGER\n" +
                ");\n";
        db.execSQL(tb_KhachHang);
        String tb_LoaiSach = "CREATE TABLE IF NOT EXISTS LoaiSach (" +
                "    MaLoaiSach  INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    TenLoaiSach TEXT UNIQUE," +
                "SoLuongSach  INTEGER DEFAULT 0,"+
                "    ImgLoaiSach TEXT)";
        Log.e("LOAI SACH",tb_LoaiSach);
        db.execSQL(tb_LoaiSach);
        String tb_Sach = "CREATE TABLE Sach (\n" +
                "    MaSach     INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    MaLoaiSach INTEGER REFERENCES LoaiSach (MaLoaiSach) \n" +
                "                       NOT NULL,\n" +
                "    TenSach    TEXT    NOT NULL\n" +
                "                       UNIQUE,"+

                "    TacGia     TEXT    NOT NULL"+

                "                       UNIQUE,\n" +
                "    Gia        INT    NOT NULL,\n" +
                "    SoLuong    INTEGER NOT NULL,\n" +
                "    ImgSach    TEXT    NOT NULL,\n" +
                "    NamXuatBan    INTERGER    NOT NULL\n" +
                ");\n";
        db.execSQL(tb_Sach);
        String tb_PhieuGiamGia = "CREATE TABLE PhieuGiamGia (\n" +
                "    MaPhieuGiamGia INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    MucGiamGia     TEXT,\n" +
                "DonHangToiDa INT ,\n"+
                "DonHangToiThieu INT ,\n"+
                "    NgayBatDau     TEXT,\n" +
                "    NgayKetThuc    TEXT,\n" +
                "    TenPhieuGiamGia    TEXT,\n" +
                "    NoiDung    TEXT,\n" +
                "    SoLanSuDung    INT,\n" +
                "    SoTienGiamToiDa   INT\n" +
                ");\n";
        db.execSQL(tb_PhieuGiamGia);
        String tb_HoaDon = "CREATE TABLE HoaDon (\n" +
                "    MaHoaDon       INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    MaNhanVien     INTEGER REFERENCES NhanVien (MaNhanVien),\n" +
                "    MaKhachHang    INTEGER REFERENCES KhachHang (MaKhachHang),\n" +
                "    MaPhieuGiamGia INTEGER REFERENCES PhieuGiamGia (MaPhieuGiamGia) ,\n" +
                "    NgayMua        TEXT    NOT NULL,\n" +
                "    TongTien        INTEGER    NOT NULL DEFAULT 0,\n" +
                "    GioMua         TEXT    NOT NULL,\n" +
                "    SoTienGiam         TEXT    NOT NULL DEFAULT 0\n" +
                ");\n";
        db.execSQL(tb_HoaDon);
        Log.d("ddddddddddddd", tb_HoaDon);
        String tb_ChiTietHoaDon = "CREATE TABLE ChiTietHoaDon (" +
                "    MaCTHD      INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    MaHoaDon    INTEGER REFERENCES HoaDon (MaHoaDon) ON DELETE CASCADE," +
                "    MaSach      INTEGER REFERENCES Sach (MaSach)," +
                "    SoLuong     INTEGER NOT NULL," +
                "    GiaTienCT     INTEGER NOT NULL" +

                ");";
        db.execSQL(tb_ChiTietHoaDon);
        String insertNhanVien = "INSERT INTO NhanVien (MaNhanVien, TenNhanVien, SoDienThoai, Luong, HoaHong, SoTaiKhoan, Quyen, MatKhau) " +
                "VALUES ('NV001', 'Nguyen Van A', '0123456789', 5000, 1000, '123456789', 1, '123');";

        db.execSQL(insertNhanVien);

//        ContentValues values = new ContentValues();
//        values.put("TenLoaiSach", "Tên sách mẫu"); // Thay "Tên sách mẫu" bằng tên bạn muốn thêm
//        values.put("ImgLoaiSach", (byte[]) null); // Thay null nếu bạn có ảnh dạng BLOB
//        db.insert("LoaiSach", null, values);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS NhanVien");
            db.execSQL("DROP TABLE IF EXISTS KhachHang");
            db.execSQL("DROP TABLE IF EXISTS LoaiSach");
            db.execSQL("DROP TABLE IF EXISTS Sach");
            db.execSQL("DROP TABLE IF EXISTS PhieuGiamGia");
            db.execSQL("DROP TABLE IF EXISTS HoaDon");
            db.execSQL("DROP TABLE IF EXISTS ChiTietHoaDon");
            onCreate(db);

        }

    }
}
