package com.bookstorepoly.nhom5_duan1.frag;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bookstorepoly.nhom5_duan1.dao.LoaiSachDAO;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.spinner_adapter.Spinner_LoaiSach;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class Frag_UpdateSach extends Fragment {
    ImageButton btn_layanh_sach_up;
    ImageView img_priview_sach;
    EditText ten_sach_up, tac_gia_up,  gia_sach_up, namxb_up;
    Spinner loai_sach_up;
    Button btnSave;
    ArrayList<LoaiSachDTO> listLoaiSach;
    LoaiSachDAO loaiSachDAO;
    private String imgPath;
    int maLoaiSach;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_up_sach, container, false);
        btn_layanh_sach_up = view.findViewById(R.id.btn_layanh_sach_up);
        img_priview_sach = view.findViewById(R.id.imgPreviewSach_up);
        ten_sach_up = view.findViewById(R.id.edtTenSach_up);
        tac_gia_up = view.findViewById(R.id.edtTacGia_up);
        loai_sach_up = view.findViewById(R.id.spLoaiSach_up);
        namxb_up = view.findViewById(R.id.edt_namxb_up);
        gia_sach_up = view.findViewById(R.id.edt_Gia_Sach_up);
        btnSave = view.findViewById(R.id.btn_luu_sach_up);

        loaiSachDAO = new LoaiSachDAO(getContext());
        listLoaiSach = new ArrayList<>();
        listLoaiSach = (ArrayList<LoaiSachDTO>) loaiSachDAO.getAllLoaiSach();
        Spinner_LoaiSach spinner_loaiSach = new Spinner_LoaiSach(getContext(), listLoaiSach);
        loai_sach_up.setAdapter(spinner_loaiSach);
        if (getArguments() != null) {
             maLoaiSach = getArguments().getInt("MaLoaiSach");
            ten_sach_up.setText(getArguments().getString("TenSach"));
            tac_gia_up.setText(getArguments().getString("TacGia"));
            gia_sach_up.setText(String.valueOf(getArguments().getInt("GiaSach")));
            namxb_up.setText(String.valueOf(getArguments().getInt("NamXuatBan")));

            imgPath = getArguments().getString("ImgSachPath");
            Log.d("SachAdapter", "ImgSachPath: " + imgPath);

            // Hiển thị ảnh
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            if (bitmap != null) {
                img_priview_sach.setImageBitmap(bitmap);
            } else {
                Log.e("ImageError", "Không thể hiển thị ảnh từ đường dẫn: " + imgPath);
                Toast.makeText(getContext(), "Không thể tải ảnh từ đường dẫn: " + imgPath, Toast.LENGTH_SHORT).show();
            }

            // Chọn đúng loại sách trong Spinner
            for (int i = 0; i < listLoaiSach.size(); i++) {
                if (listLoaiSach.get(i).getMaLoaiSach() == maLoaiSach) {
                    loai_sach_up.setSelection(i);
                    break;
                }
            }
        }

        // Chọn ảnh
        btn_layanh_sach_up.setOnClickListener(v -> selectImage());

        // Lưu thông tin cập nhật
        btnSave.setOnClickListener(v -> saveUpdatedBook());

        return view;
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 100);
    }

    private String getRealPathFromURI(Uri uri) {
        String realPath = null;
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                if (idx != -1) {
                    realPath = cursor.getString(idx);
                }
            }
            cursor.close();
        }
        return realPath != null ? realPath : uri.getPath();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    img_priview_sach.setImageBitmap(bitmap);
                    // Lấy đường dẫn thực từ URI
                    imgPath = getRealPathFromURI(uri);
                    Log.d("ImagePath", "Real Path: " + imgPath);
                } else {
                    Log.e("ImageError", "Không thể tạo bitmap từ URI: " + uri);
                    Toast.makeText(getContext(), "Không thể tạo bitmap từ URI!", Toast.LENGTH_SHORT).show();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Không thể lấy ảnh!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveUpdatedBook() {
        String tenSach = ten_sach_up.getText().toString().trim();
        String tacGia = tac_gia_up.getText().toString().trim();
        String giaSachStr = gia_sach_up.getText().toString().trim();

        if (tenSach.isEmpty() || tacGia.isEmpty() || giaSachStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        int giaSach = Integer.parseInt(giaSachStr);
        int maLoaiSach = listLoaiSach.get(loai_sach_up.getSelectedItemPosition()).getMaLoaiSach();
        int maSach = getArguments().getInt("MaSach");

        SachDTO sach = new SachDTO();
        sach.setMaSach(maSach);
        sach.setTenSach(tenSach);
        sach.setTacGia(tacGia);
        sach.setGiaSach(giaSach);
        sach.setLoaiSach(maLoaiSach);
        sach.setImgSachPath(imgPath);

        SachDAO sachDAO = new SachDAO(getContext());
        int result = sachDAO.UpdateSach(sach);
        if (result > 0) {
            Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
}
