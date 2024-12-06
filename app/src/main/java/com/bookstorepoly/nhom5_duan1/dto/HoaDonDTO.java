package com.bookstorepoly.nhom5_duan1.dto;

public class HoaDonDTO {
    private int maHoaDon;
    private String maNhanVien;
    private int maKhachHang;
    private int maPhieuGiamGia;
    private int soTienGiam;

    public int getSoTienGiam() {
        return soTienGiam;
    }

    public void setSoTienGiam(int soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    private String ngayMua;
    private int tongTien;
    private String gioMua;

    public String getGioMua() {
        return gioMua;
    }

    public void setGioMua(String gioMua) {
        this.gioMua = gioMua;
    }

    public int getTongTien() {
        return tongTien;
    }

    public void setTongTien(int tongTien) {
        this.tongTien = tongTien;
    }

    public String getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(String ngayMua) {
        this.ngayMua = ngayMua;
    }

    public int getMaPhieuGiamGia() {
        return maPhieuGiamGia;
    }

    public void setMaPhieuGiamGia(int maPhieuGiamGia) {
        this.maPhieuGiamGia = maPhieuGiamGia;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }
}
