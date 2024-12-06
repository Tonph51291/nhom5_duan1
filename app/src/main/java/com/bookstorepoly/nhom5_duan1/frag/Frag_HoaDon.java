package com.bookstorepoly.nhom5_duan1.frag;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bookstorepoly.nhom5_duan1.dao.HoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.KhachHangDAO;
import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonDTO;
import com.bookstorepoly.nhom5_duan1.dto.KhachHangDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Frag_HoaDon extends Fragment {
    EditText edt_ten_kh, edt_sdt_kh, edt_dia_chi, edt_ngay_mua;
    Button btn_thanhtoan;
    HoaDonDAO hoaDonDAO;
    KhachHangDAO khachHangDAO;
    FragmentManager fragmentManager;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_hoadon, container, false);
        edt_ten_kh = view.findViewById(R.id.edt_ten_kh);
        edt_sdt_kh = view.findViewById(R.id.edt_sdt_kh);
        edt_dia_chi = view.findViewById(R.id.edt_diachi_kh);
        edt_ngay_mua = view.findViewById(R.id.edt_ngay_mua);
        btn_thanhtoan = view.findViewById(R.id.btn_thanhtoan);
        fragmentManager = getParentFragmentManager();
        edt_ngay_mua.setText(getCurrentDate());

        btn_thanhtoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenKhachHang = edt_ten_kh.getText().toString();
                String soDienThoai = edt_sdt_kh.getText().toString();
                String diaChi = edt_dia_chi.getText().toString();
                String ngayMua = edt_ngay_mua.getText().toString();

                if (!isInputNotEmpty(tenKhachHang) || !isInputNotEmpty(soDienThoai) || !isInputNotEmpty(diaChi)) {
                    edt_ten_kh.setError("Vui lòng nhập đầy đủ thông tin");
                    edt_sdt_kh.setError("Vui lòng nhập đầy đủ thông tin");
                    edt_dia_chi.setError("Vui lòng nhập đầy đủ thông tin");
                    return;
                }
                if (!isCustomerNameValid(tenKhachHang)) {
                    edt_ten_kh.setError("Tên khách hàng không hợp lệ");
                    return;
                }
                if (!isPhoneNumberValid(soDienThoai)) {
                    edt_sdt_kh.setError("Số điện thoại không hợp lệ");
                    return;
                }


                KhachHangDAO khachHangDAO = new KhachHangDAO(getContext());
                KhachHangDTO existingCustomerId = khachHangDAO.getKhachHangBySDT(soDienThoai);
                Log.e("check", String.valueOf(existingCustomerId));

                if (existingCustomerId != null) {
                    // lấy mã nhân vien
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    String maNhanVien = sharedPreferences.getString("MaNhanVien", "");
                    SharedPreferences khachhang = getContext().getSharedPreferences("KhachHang", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = khachhang.edit();
                    editor1.putString("TenKhachHang", existingCustomerId.getTenKhachHang());
                    editor1.putString("SoDienThoai", existingCustomerId.getSoDienThoai());

                    HoaDonDTO hoaDonDTO = new HoaDonDTO();
                    hoaDonDTO.setMaKhachHang(existingCustomerId.getMaKhachHang());
                    hoaDonDTO.setMaNhanVien(maNhanVien);
                    hoaDonDTO.setNgayMua(ngayMua);
                    hoaDonDTO.setGioMua(getCurrentTime());
                    hoaDonDTO.setMaPhieuGiamGia(-1);
                    hoaDonDAO = new HoaDonDAO(getContext());
                    int check = hoaDonDAO.AddHoaDon(hoaDonDTO);
                    if (check > 0) {
                        int maHoaDonMoi = hoaDonDAO.getLatestMaHoaDon();
                        SharedPreferences shaMaHD = getContext().getSharedPreferences("MaHoaDon", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor = shaMaHD.edit();
                        editor.putInt("MaHoaDon", maHoaDonMoi);
                        editor.apply();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frag_container, new Frag_HoaDonChiTiet())
                                .addToBackStack(null)
                                .commit();


                    }
                } else {
                    KhachHangDTO khachHangDTO = new KhachHangDTO();

                    khachHangDTO.setTenKhachHang(tenKhachHang);
                    khachHangDTO.setSoDienThoai(soDienThoai);
                    khachHangDTO.setDiaChiKhachHang(diaChi);
                    khachHangDTO.setSoLanMua(1);
                    SharedPreferences khachhang = getContext().getSharedPreferences("KhachHang", getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = khachhang.edit();
                    editor1.putString("TenKhachHang", tenKhachHang);
                    editor1.putString("SoDienThoai", soDienThoai);

                    editor1.apply();

                    khachHangDAO = new KhachHangDAO(getContext());

                    int check = khachHangDAO.AddKhachHang(khachHangDTO);
                    if (check > 0) {
                        int maKhachHangMoi = khachHangDAO.getLatestMaKhachHang();

                        SharedPreferences shaMaKH = getContext().getSharedPreferences("MaKhachHang", getContext().MODE_PRIVATE);
                        SharedPreferences.Editor editor12 = shaMaKH.edit();

                        SharedPreferences shaMaNhanVieen = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
                        String maNhanVien = shaMaNhanVieen.getString("MaNhanVien", "");
                        editor12.putInt("MaKhachHang", maKhachHangMoi);
                        editor12.apply();
                        HoaDonDTO hoaDonDTO = new HoaDonDTO();
                        hoaDonDTO.setMaNhanVien(maNhanVien);
                        hoaDonDTO.setMaKhachHang(maKhachHangMoi);
                        hoaDonDTO.setMaPhieuGiamGia(-1);
                        hoaDonDTO.setNgayMua(ngayMua);
                        hoaDonDTO.setGioMua(getCurrentTime());
                        hoaDonDAO = new HoaDonDAO(getContext());
                        int check1 = hoaDonDAO.AddHoaDon(hoaDonDTO);
                        Log.e("check1", String.valueOf(check1));
                        if (check1 > 0) {
                            int maHoaDonMoi = hoaDonDAO.getLatestMaHoaDon();

                            SharedPreferences shaMaHD = getContext().getSharedPreferences("MaHoaDon", getContext().MODE_PRIVATE);
                            SharedPreferences.Editor editor = shaMaHD.edit();
                            editor.putInt("MaHoaDon", maHoaDonMoi);
                            Log.d("mahoadoncu", maHoaDonMoi + "");
                            editor.apply();
                            fragmentManager.beginTransaction()
                                    .replace(R.id.frag_container, new Frag_HoaDonChiTiet())
                                    .addToBackStack(null)
                                    .commit();
                        } else {
                            Toast.makeText(getContext(), "That bai", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });


        return view;
    }

    private boolean isInputNotEmpty(String input) {
        return !input.trim().isEmpty();
    }

    private boolean isCustomerNameValid(String name) {
        return name.matches("^[\\p{L} .'-]+$"); // Tên hợp lệ chỉ chứa chữ và khoảng trắng
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^(03|05|07|08|09|01[2|6|8|9])+([0-9]{8})$"); // Số điện thoại Việt Nam
    }

    private boolean isQuantityValid(String quantity) {
        try {
            int qty = Integer.parseInt(quantity);
            return qty > 0; // Số lượng phải lớn hơn 0
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    // Hàm lấy giờ hiện tại
    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }


}
