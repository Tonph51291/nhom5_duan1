package com.bookstorepoly.nhom5_duan1.frag;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.adapter.HoaDonBillAdapter;
import com.bookstorepoly.nhom5_duan1.adapter.HoaDonChiTietAdapter;
import com.bookstorepoly.nhom5_duan1.adapter.SachHoaDon;
import com.bookstorepoly.nhom5_duan1.dao.ChiTietHoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.HoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dao.KhachHangDAO;
import com.bookstorepoly.nhom5_duan1.dao.NhanVienDAO;
import com.bookstorepoly.nhom5_duan1.dao.PhieuGiamGiaDAO;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonChiTietDTO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonDTO;
import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.spinner_adapter.Spinner_PhieuGiamGia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Frag_HoaDonChiTiet extends Fragment {
    int soLuongSachTrongKho = 0;

    private Button btnThemSp, btnTaoHoaDon;
    private Button btnHuy;
    private RecyclerView rcvSanPham;
    private TextView txtTongTien, txtSoTienGiam, txtTongTienTatCa;
    private Spinner spPhieuGiamGia;

    private List<HoaDonChiTietDTO> listHoaDonCT;
    private HoaDonChiTietAdapter hoaDonChiTietAdapter;

    private List<PhieuGiamGiaDTO> listPhieuGiamGia;
    private Spinner_PhieuGiamGia adapterPhieu;

    private ChiTietHoaDonDAO chiTietHoaDonDAO;
    private PhieuGiamGiaDAO phieuGiamGiaDAO;
    private HoaDonDAO hoaDonDAO;
    private KhachHangDAO khachHangDAO;
    private int maHoaDon;
    double discount;
    double total;
    private FragmentManager fragmentManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_hoadonct, container, false);

        // Ánh xạ view
        btnThemSp = view.findViewById(R.id.btn_themsp);
        btnTaoHoaDon = view.findViewById(R.id.btn_tao_hoa_don);
        btnHuy = view.findViewById(R.id.btn_huy_tao_hoa_don);
        rcvSanPham = view.findViewById(R.id.rcv_san_pham);
        txtTongTien = view.findViewById(R.id.txt_tong_tien);
        txtSoTienGiam = view.findViewById(R.id.txt_so_tien_giam_sd);
        txtTongTienTatCa = view.findViewById(R.id.txt_tong_tien_tat_ca);
        spPhieuGiamGia = view.findViewById(R.id.edt_ten_phieu_giam_gia_hdct);
        fragmentManager = getParentFragmentManager();


        // Khởi tạo DAO
        chiTietHoaDonDAO = new ChiTietHoaDonDAO(getContext());
        phieuGiamGiaDAO = new PhieuGiamGiaDAO(getContext());
        hoaDonDAO = new HoaDonDAO(getContext());
        khachHangDAO = new KhachHangDAO(getContext());

        // Lấy mã hóa đơn từ SharedPreferences
        SharedPreferences shaMaHD = getContext().getSharedPreferences("MaHoaDon", getContext().MODE_PRIVATE);
        maHoaDon = shaMaHD.getInt("MaHoaDon", 0);


        listHoaDonCT = chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon);
        hoaDonChiTietAdapter = new HoaDonChiTietAdapter(getContext(), (ArrayList<HoaDonChiTietDTO>) listHoaDonCT);
        hoaDonChiTietAdapter.setTvTotal(txtTongTien);
        rcvSanPham.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvSanPham.setAdapter(hoaDonChiTietAdapter);

        // Lấy dữ liệu phiếu giảm giá
        listPhieuGiamGia = new ArrayList<>();
        listPhieuGiamGia = phieuGiamGiaDAO.getAllPhieuGiamGia();


        adapterPhieu = new Spinner_PhieuGiamGia(getContext(), listPhieuGiamGia);
        spPhieuGiamGia.setAdapter(adapterPhieu);

        // Cập nhật tổng tiền


        // Sự kiện chọn mã giảm giá
        spPhieuGiamGia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                PhieuGiamGiaDTO selectedPhieu = listPhieuGiamGia.get(position);
                adapterPhieu.notifyDataSetChanged();
                updateTotalWithDiscount(selectedPhieu);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        updateTotalAmount();


        // Sự kiện nút thêm sản phẩm
        btnThemSp.setOnClickListener(v -> showDialogAddSP());

        // Sự kiện tạo hóa đơn
        // Sự kiện tạo hóa đơn
        btnTaoHoaDon.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            View view1 = LayoutInflater.from(getContext()).inflate(R.layout.bill_hd, null);
            dialog.setView(view1);
            dialog.show();

            // Ánh xạ các TextView và RecyclerView trong Dialog
            TextView txtTenKH = view1.findViewById(R.id.txt_ten_kh_bill);
            TextView txtSDT = view1.findViewById(R.id.txt_sdt_kh_bill);
            TextView txtNgayMua = view1.findViewById(R.id.txt_ngay_mua_bill);
            TextView txtSoTienGiam = view1.findViewById(R.id.txt_giamgia_bill);
            TextView txtTamTinh = view1.findViewById(R.id.txt_tamtinh_bill);
            TextView txtTongTien = view1.findViewById(R.id.txt_thantien_bill);
            Button btnInHoaDon = view1.findViewById(R.id.btn_in_hoa_don);
            RecyclerView rcv = view1.findViewById(R.id.rcv_san_pham_bill);

            // Lấy thông tin khách hàng từ SharedPreferences
            SharedPreferences khachhang = getContext().getSharedPreferences("KhachHang", getContext().MODE_PRIVATE);
            String tenKhachHang = khachhang.getString("TenKhachHang", "");
            String soDienThoai = khachhang.getString("SoDienThoai", "");
            txtTenKH.setText(tenKhachHang);
            txtSDT.setText(soDienThoai);

            // Lấy ngày hiện tại
            Calendar calendar = Calendar.getInstance();
            Date currentDate = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDate = dateFormat.format(currentDate);
            txtNgayMua.setText(formattedDate);

            // Hiển thị thông tin tiền
            txtTamTinh.setText(String.valueOf(total));
            int tongTienTatCa1 = Integer.parseInt(txtTongTienTatCa.getText().toString());
            txtTongTien.setText(String.valueOf(tongTienTatCa1));
            txtSoTienGiam.setText(String.valueOf(discount));

            // Thiết lập RecyclerView
            ChiTietHoaDonDAO chiTietHoaDonDAO = new ChiTietHoaDonDAO(getContext());

            ArrayList<HoaDonChiTietDTO> listBill = new ArrayList<>();
            listBill.addAll(chiTietHoaDonDAO.getChiTietHoaDonByMaHoaDon(maHoaDon));
            Log.d("zzzzz", "Ma hoa don bill" + maHoaDon);
            HoaDonBillAdapter hoaDonChiTietAdapterBill = new HoaDonBillAdapter(listBill, getContext());
            rcv.setLayoutManager(new LinearLayoutManager(getContext()));
            rcv.setAdapter(hoaDonChiTietAdapterBill);

            btnInHoaDon.setOnClickListener(v1 -> {
                ///
                PhieuGiamGiaDTO selectedPhieu = listPhieuGiamGia.get(spPhieuGiamGia.getSelectedItemPosition());
                int tongTienTatCa = Integer.parseInt(txtTongTienTatCa.getText().toString());
                HoaDonDTO hoaDonDTO = new HoaDonDTO();
                hoaDonDTO.setMaHoaDon(maHoaDon);
                hoaDonDTO.setMaPhieuGiamGia(selectedPhieu.getMaPhieuGiamGia());
                hoaDonDTO.setTongTien(tongTienTatCa);
                hoaDonDTO.setSoTienGiam((int) discount);

                HoaDonDAO hoaDonDAO = new HoaDonDAO(getContext());
                int result = hoaDonDAO.UpdateHoaDon(hoaDonDTO);

                if (result > 0) {
                    Toast.makeText(getContext(), "Tạo hóa đơn thành công!", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < listBill.size(); i++) {
                        HoaDonChiTietDTO sach = listBill.get(i);
                        int masach = sach.getMaSach();
                        int soluong = sach.getSoLuong();
                        SachDAO sachDAO = new SachDAO(getContext());
                        sachDAO.updateQuantity(masach, soluong);
                    }
                    SharedPreferences shaMaNhanVieen = getContext().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
                    String maNhanVien = shaMaNhanVieen.getString("MaNhanVien", "");
                    NhanVienDAO nhanVienDAO = new NhanVienDAO(getContext());
                    nhanVienDAO.updateHoaHong(maNhanVien, tongTienTatCa);


                    // Hiển thị thông báo thành công
                } else {
                    Toast.makeText(getContext(), "Lỗi khi tạo hóa đơn!", Toast.LENGTH_SHORT).show();
                }

            });
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listHoaDonCT != null && listHoaDonCT.size() != 0) {
                    for (int i = 0; i < listHoaDonCT.size(); i++) {
                        HoaDonChiTietDTO hoaDonChiTietDTO = listHoaDonCT.get(i);
                        int maHoaDon = hoaDonChiTietDTO.getMaHoaDon();
                        chiTietHoaDonDAO.DeleteChiTietHoaDon(maHoaDon);
                    }
                }

                int check = hoaDonDAO.DeleteHoaDon(maHoaDon);
                if (check > 0) {
                    SharedPreferences shaMaKH = getContext().getSharedPreferences("MaKhachHang", getContext().MODE_PRIVATE);
                    int maKhachHang = shaMaKH.getInt("MaKhachHang", 0);
                    int check1 = khachHangDAO.DeleteKhachHang(maKhachHang);
                    if (check1 > 0) {
                        Toast.makeText(getContext(), "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                        fragmentManager.beginTransaction().replace(R.id.frag_container, new Frag_Home()).commit();
                    }

                } else {
                    Toast.makeText(getContext(), "Xoa that bai", Toast.LENGTH_SHORT).show();
                }


            }
        });


        txtTongTien.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalWithDiscount(listPhieuGiamGia.get(spPhieuGiamGia.getSelectedItemPosition()));

            }
        });

        return view;
    }

    private boolean isPhieuGiamGiaValid(PhieuGiamGiaDTO phieuGiamGiaDTO) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();
        try {
            Date startDate = sdf.parse(phieuGiamGiaDTO.getNgayBatDau());
            Date endDate = sdf.parse(phieuGiamGiaDTO.getNgayKetThuc());

            return !currentDate.before(startDate) && !currentDate.after(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void showDialogAddSP() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_chon_sp, null);
        dialog.setView(view);
        dialog.show();

        AutoCompleteTextView edtTenSach = view.findViewById(R.id.edt_tensach_sp);
        EditText edtSoLuong = view.findViewById(R.id.edt_soluong_sp);
        Button btnThem = view.findViewById(R.id.btn_them_sp_hd);
        Button btnHuy = view.findViewById(R.id.btn_huythem_sp_hd);

        btnHuy.setOnClickListener(v -> dialog.dismiss());

        SachDAO sachDAO = new SachDAO(getContext());
        List<SachDTO> listSach = sachDAO.getAllSach();
        SachHoaDon adapter = new SachHoaDon(getContext(), listSach);


        edtTenSach.setOnItemClickListener((parent, view1, position, id) -> {
            SachDTO sach = (SachDTO) parent.getItemAtPosition(position);
            String tenSach = sach.getTenSach();
            soLuongSachTrongKho = sach.getSoLuong();
            Log.d("SelectedSach", "Tên sách: " + tenSach);
            edtTenSach.setText(tenSach);
        });
        edtTenSach.setAdapter(adapter);


        btnThem.setOnClickListener(v -> {
            String tenSach = edtTenSach.getText().toString();
            String soLuongStr = edtSoLuong.getText().toString();

            if (tenSach.isEmpty() || soLuongStr.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }


            int soLuong = Integer.parseInt(soLuongStr);
            SachDTO selectedSach = listSach.stream().filter(sach -> sach.getTenSach().equals(tenSach)).findFirst().orElse(null);

            if (selectedSach == null) {
                Toast.makeText(getContext(), "Sách không tồn tại!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (soLuong > soLuongSachTrongKho) {
                Toast.makeText(getContext(), "Số lượng không hợp lệ!", Toast.LENGTH_SHORT).show();
                return;
            }

            double giaTien = soLuong * selectedSach.getGiaSach();
            HoaDonChiTietDTO chiTiet = new HoaDonChiTietDTO();

            chiTiet.setMaHoaDon(maHoaDon);
            chiTiet.setMaSach(selectedSach.getMaSach());
            chiTiet.setSoLuong(soLuong);
            chiTiet.setGiaTien(giaTien);

            int ketqua = chiTietHoaDonDAO.AddChiTietHoaDon(chiTiet);
            if (ketqua > 0) {

                chiTiet.setMaHoaDonChiTiet(chiTietHoaDonDAO.getLatestMaHoaDonChiTiet());
                listHoaDonCT.add(chiTiet);
                hoaDonChiTietAdapter.notifyDataSetChanged();
                updateTotalAmount();
                dialog.dismiss();
            } else {
                Toast.makeText(getActivity(), "Co loi xay ra", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateTotalAmount() {
        double total = listHoaDonCT.stream().mapToDouble(HoaDonChiTietDTO::getGiaTien).sum();
        txtTongTien.setText(String.valueOf((int) total));

        updateTotalWithDiscount(listPhieuGiamGia.get(spPhieuGiamGia.getSelectedItemPosition()));
    }

    private void updateTotalWithDiscount(PhieuGiamGiaDTO selectedPhieu) {
        total = Double.parseDouble(txtTongTien.getText().toString());
        double discount = calculateDiscount(selectedPhieu, total);
        txtSoTienGiam.setText(String.valueOf((int) discount));
        txtTongTienTatCa.setText(String.valueOf((int) (total - discount)));
    }

    private double calculateDiscount(PhieuGiamGiaDTO phieu, double total) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        // lon hon don han toi thieu va nho hon don hang toi da
        if (total < phieu.getDonHangToiThieu()) return 0;
        if (phieu.getDonHangToiDa() > 0 && total > phieu.getDonHangToiDa()) return 0;


        discount = (total * Double.parseDouble(phieu.getMucGiamGia())) / 100.0;

        // neu don hang toi da = 0 thi va gia > don hang toi thieu thi giam gia nhung khong qua so tien toi da
        if (phieu.getDonHangToiDa() == 0 || total > phieu.getDonHangToiThieu()) {
            if (phieu.getSoTienGiamToiDa() > 0 && discount > phieu.getSoTienGiamToiDa()) {
                discount = phieu.getSoTienGiamToiDa();
            }

        }


        return discount;
    }

}
