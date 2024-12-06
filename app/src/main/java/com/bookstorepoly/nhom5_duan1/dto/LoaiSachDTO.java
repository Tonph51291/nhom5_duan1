package com.bookstorepoly.nhom5_duan1.dto;

public class LoaiSachDTO {
    private int maLoaiSach;
    private String tenLoaiSach;
    private String imgLoaiSachPath; // Đường dẫn ảnh thay vì dữ liệu ảnh trực tiếp
    private int soLuongSach;

    public LoaiSachDTO() {
    }

    public int getMaLoaiSach() {
        return maLoaiSach;
    }

    public void setMaLoaiSach(int maLoaiSach) {
        this.maLoaiSach = maLoaiSach;
    }

    public String getTenLoaiSach() {
        return tenLoaiSach;
    }

    public void setTenLoaiSach(String tenLoaiSach) {
        this.tenLoaiSach = tenLoaiSach;
    }

    public String getImgLoaiSachPath() {
        return imgLoaiSachPath;
    }

    public void setImgLoaiSachPath(String imgLoaiSachPath) {
        this.imgLoaiSachPath = imgLoaiSachPath;
    }
    public int getSoLuongSach() {
        return soLuongSach;
    }
    public void setSoLuongSach(int soLuongSach) {
        this.soLuongSach = soLuongSach;
    }
}
