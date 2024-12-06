package com.bookstorepoly.nhom5_duan1.frag;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bookstorepoly.nhom5_duan1.dao.PhieuGiamGiaDAO;
import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.Calendar;


public class Frag_KhuyenMai extends Fragment {
    EditText edt_ngay_bat_dau , edt_ngay_ket_thuc
            ,edt_so_lan_su_dung,edt_don_han_toi_thieu,
            edt_don_han_toi_da,edt_muc_giam_gia,edt_ten_phieu_giam_gia,edt_noi_dung,edt_so_tien_giam_toi_da;
    Button btn_luu;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_khuyenmai, container, false);
        edt_ten_phieu_giam_gia = view.findViewById(R.id.edt_ten_phieu_giam_gia);
        edt_muc_giam_gia = view.findViewById(R.id.edt_muc_giam_gia);
        edt_don_han_toi_thieu = view.findViewById(R.id.edt_don_han_toi_thieu);
        edt_don_han_toi_da = view.findViewById(R.id.edt_don_han_toi_da);
        edt_noi_dung = view.findViewById(R.id.edt_noi_dung);
        edt_so_lan_su_dung = view.findViewById(R.id.edt_so_lan_su_dung);
        edt_so_tien_giam_toi_da = view.findViewById(R.id.edt_so_tien_giam_toi_da);

        btn_luu = view.findViewById(R.id.btn_luu_phieu_giam_gia);

        edt_ngay_bat_dau = view.findViewById(R.id.ngay_bat_dau);
        edt_ngay_ket_thuc = view.findViewById(R.id.ngay_ket_thuc);

       edt_ngay_bat_dau.setOnClickListener(v -> showDatePickerDialog(edt_ngay_bat_dau));
       edt_ngay_ket_thuc.setOnClickListener(v-> showDatePickerDialog(edt_ngay_ket_thuc));
        btn_luu.setOnClickListener(v -> {
            PhieuGiamGiaDTO phieuGiamGiaDTO = new PhieuGiamGiaDTO();
            String tenPhieuGiamGia = edt_ten_phieu_giam_gia.getText().toString();
            String mucGiamGia = edt_muc_giam_gia.getText().toString();
            String donHangToiThieu = edt_don_han_toi_thieu.getText().toString();
            String donHangToiDa = edt_don_han_toi_da.getText().toString();
            String ngayBatDau = edt_ngay_bat_dau.getText().toString();
            String ngayKetThuc = edt_ngay_ket_thuc.getText().toString();
            String noiDung = edt_noi_dung.getText().toString();
            String soLanSuDung = edt_so_lan_su_dung.getText().toString();
            String mucGiamToiDa = edt_so_tien_giam_toi_da.getText().toString();


            if (tenPhieuGiamGia.isEmpty() || mucGiamGia.isEmpty() || donHangToiThieu.isEmpty() || donHangToiDa.isEmpty() || ngayBatDau.isEmpty()
                    || ngayKetThuc.isEmpty() || noiDung.isEmpty() || soLanSuDung.isEmpty() || mucGiamToiDa.isEmpty()) {
                edt_ten_phieu_giam_gia.setError("Vui lòng nhập đầy đủ thông tin");
                edt_muc_giam_gia.setError("Vui lòng nhập đầy đủ thông tin");
                edt_don_han_toi_thieu.setError("Vui lòng nhập đầy đủ thông tin");
                edt_don_han_toi_da.setError("Vui lòng nhập đầy đủ thông tin");
                edt_ngay_bat_dau.setError("Vui lòng nhập đầy đủ thông tin");
                edt_ngay_ket_thuc.setError("Vui lòng nhập đầy đủ thông tin");
                edt_noi_dung.setError("Vui lòng nhập đầy đủ thông tin");
                edt_so_lan_su_dung.setError("Vui lòng nhập đầy đủ thông tin");
                edt_so_tien_giam_toi_da.setError("Vui lòng nhập đầy đủ thông tin");
                return;
            }



            // Check if the discount rate is a valid percentage
            try {
                int discount = Integer.parseInt(mucGiamGia);
                if (discount < 0 || discount > 100) {
                    edt_muc_giam_gia.setError("Mức giảm giá phải nằm trong khoảng từ 0 đến 100%");
                    return;
                }
            } catch (NumberFormatException e) {
                edt_muc_giam_gia.setError("Mức giảm giá phải là một số");
                return;
            }

            // Check if donHangToiThieu and donHangToiDa are valid integers
            try {
                int minOrder = Integer.parseInt(donHangToiThieu);
                int maxOrder = Integer.parseInt(donHangToiDa);
                if (minOrder < 0 ) {
                    edt_don_han_toi_thieu.setError("Đơn hàng tối thiểu phải là số dương");
                    return;
                }
                if (maxOrder != 0  && maxOrder < minOrder  ) {
                    edt_don_han_toi_da.setError("Đơn hàng tối đa phải lớn hơn hoặc bằng đơn hàng tối thiểu");
                    return;
                }
            } catch (NumberFormatException e) {
                edt_don_han_toi_thieu.setError("Đơn hàng phải là số");
                edt_don_han_toi_da.setError("Đơn hàng phải là số");
                return;
            }

            // Check if soLanSuDung is a valid integer
            try {
                int usageLimit = Integer.parseInt(soLanSuDung);
                if (usageLimit < 0) {
                    edt_so_lan_su_dung.setError("Số lần sử dụng phải là số dương");
                    return;
                }
            } catch (NumberFormatException e) {
                edt_so_lan_su_dung.setError("Số lần sử dụng phải là số");
                return;
            }
            try {
                int tiengiam = Integer.parseInt(mucGiamToiDa);
            } catch (NumberFormatException e) {
                edt_so_tien_giam_toi_da.setError("Số tiền giảm tối đa phải là số");
                return;
            }


            // Additional checks for ngayBatDau and ngayKetThuc can be added here

            phieuGiamGiaDTO.setTenPhieuGiamGia(tenPhieuGiamGia);
            phieuGiamGiaDTO.setMucGiamGia(mucGiamGia);
            phieuGiamGiaDTO.setDonHangToiThieu(Integer.parseInt(donHangToiThieu));
            phieuGiamGiaDTO.setDonHangToiDa(Integer.parseInt(donHangToiDa));
            phieuGiamGiaDTO.setNgayBatDau(ngayBatDau);
            phieuGiamGiaDTO.setNgayKetThuc(ngayKetThuc);
            phieuGiamGiaDTO.setNoiDung(noiDung);
            phieuGiamGiaDTO.setSoLanSuDung(Integer.parseInt(soLanSuDung));
            phieuGiamGiaDTO.setSoTienGiamToiDa(Integer.parseInt(mucGiamToiDa));

            PhieuGiamGiaDAO phieuGiamGiaDAO = new PhieuGiamGiaDAO(requireContext());
          int check =   phieuGiamGiaDAO.AddPhieuGiamGia(phieuGiamGiaDTO);
            if (check > 0) {
                edt_ten_phieu_giam_gia.setText("");
                edt_muc_giam_gia.setText("");
                edt_don_han_toi_thieu.setText("");
                edt_don_han_toi_da.setText("");
                edt_ngay_bat_dau.setText("");
                edt_ngay_ket_thuc.setText("");
                edt_noi_dung.setText("");
                edt_so_lan_su_dung.setText("");
                edt_so_tien_giam_toi_da.setText("");
            } else {
                Toast.makeText(requireContext(), "Thêm thất bại", Toast.LENGTH_SHORT).show();

            }
        });





        return view;
    }
    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            // Định dạng ngày được chọn
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
            editText.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }
}
