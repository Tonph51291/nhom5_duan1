package com.bookstorepoly.nhom5_duan1.dto;

public class SachDTO {
    int maSach,soLuong, giaSach,loaiSach, namXuatBan;
    String  tenSach, tacGia;
    String imgSachPath;

    public int getMaSach() {
        return maSach;
    }

    public void setMaSach(int maSach) {
        this.maSach = maSach;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getGiaSach() {
        return giaSach;
    }

    public void setGiaSach(int giaSach) {
        this.giaSach = giaSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public String getTacGia() {
        return tacGia;
    }

    public void setTacGia(String tacGia) {
        this.tacGia = tacGia;
    }

    public int getLoaiSach() {
        return loaiSach;
    }

    public void setLoaiSach(int loaiSach) {
        this.loaiSach = loaiSach;
    }

    public String getImgSachPath() {
        return imgSachPath;
    }

    public void setImgSachPath(String imgSachPath) {
        this.imgSachPath = imgSachPath;
    }
    public int getNamXuatBan() {
        return namXuatBan;
    }
    public void setNamXuatBan(int namXuatBan) {
        this.namXuatBan = namXuatBan;
    }
}
