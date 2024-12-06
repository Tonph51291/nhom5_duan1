package com.bookstorepoly.nhom5_duan1.frag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.adapter.SachAdapter;
import com.bookstorepoly.nhom5_duan1.dao.LoaiSachDAO;
import com.bookstorepoly.nhom5_duan1.dao.SachDAO;
import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
import com.bookstorepoly.nhom5_duan1.dto.PhieuGiamGiaDTO;
import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.spinner_adapter.Spinner_LoaiSach;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;

public class Frag_Sach extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FloatingActionButton btn_them_sach;
    private RecyclerView rcv_sach;
    private ArrayList<SachDTO> listSach;
    private SachAdapter sachAdapter;
    private SachDAO sachDAO;
    private ImageButton btn_pop_sach;
    private Uri imageUri;
    private String imgSachPath;
    private ImageView imgPreviewSach;
    private EditText edtTenSach, edtTacGia, edtSoLuong, edtGiaSach, edtNamXB;
    private Spinner spnLoaiSach;
    private Spinner_LoaiSach spinner_loaiSach;
    private LoaiSachDAO loaiSachDAO;
    private List<LoaiSachDTO> listLoaiSach;
    int maLoaiSach;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_sach, container, false);
        btn_them_sach = view.findViewById(R.id.btn_them_sach);
        rcv_sach = view.findViewById(R.id.rcv_sach);
        btn_pop_sach = view.findViewById(R.id.btn_pop_sach);

        sachDAO = new SachDAO(getContext());
        listSach = new ArrayList<>();
        sachAdapter = new SachAdapter(getContext(), listSach);
        rcv_sach.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rcv_sach.setAdapter(sachAdapter);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("data", Context.MODE_PRIVATE);
        int maLoaiSach = sharedPreferences.getInt("maLoaiSach", -1);
        Log.d("Frag_Sach", "maLoaiSach: " + maLoaiSach); // Log the value of maLoaiSach")
        if (maLoaiSach != 0) {
            listSach.clear();
            listSach.addAll(sachDAO.getSachByLoaiSach(maLoaiSach));
            sachAdapter.notifyDataSetChanged();


        } else {
          capnhatsach();
        }



        SearchView searchView = view.findViewById(R.id.search_view_sach);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchBooks(newText); // Tìm kiếm khi người dùng gõ
                return true;
            }
        });

        btn_them_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddSach();
            }
        });
        btn_pop_sach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortMenu(v);
            }
        });
   //     capnhatsach();
        return view;
    }

    private void showDialogAddSach() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_sach, null);
        dialog.setView(view);
        dialog.show();

        // Initialize dialog UI elements
        ImageButton btnLayAnhSach = view.findViewById(R.id.btn_layanh_sach);
        imgPreviewSach = view.findViewById(R.id.imgPreviewSach);
        edtTenSach = view.findViewById(R.id.edtTenSach);
        edtTacGia = view.findViewById(R.id.edtTacGia);
        edtSoLuong = view.findViewById(R.id.edtSoLuong);
        edtGiaSach = view.findViewById(R.id.edt_Gia_Sach);
        spnLoaiSach = view.findViewById(R.id.spLoaiSach);
        edtNamXB = view.findViewById(R.id.edt_namxb);

        Button btnLuuSach = view.findViewById(R.id.btn_luu_sach);

        // Set click listener for the image selection button
        btnLayAnhSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGalleryBook();
            }
        });

        // Initialize spinner adapter for book categories
        loaiSachDAO = new LoaiSachDAO(getContext());
        listLoaiSach = loaiSachDAO.getAllLoaiSach();
        spinner_loaiSach = new Spinner_LoaiSach(getContext(), listLoaiSach);
        spnLoaiSach.setAdapter(spinner_loaiSach);

        spnLoaiSach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 LoaiSachDTO loaiSachDTO = listLoaiSach.get(position);
                 maLoaiSach = loaiSachDTO.getMaLoaiSach();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        // Save button listener to add a new book
        btnLuuSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSach = edtTenSach.getText().toString();
                String tacGia = edtTacGia.getText().toString();
                String soLuong = edtSoLuong.getText().toString();
                String giaSach = edtGiaSach.getText().toString();
                String namXB = edtNamXB.getText().toString();

                // Check if fields are filled
                if (tenSach.isEmpty() || tacGia.isEmpty() || soLuong.isEmpty() || giaSach.isEmpty() || imgSachPath == null) {
                    Toast.makeText(getContext(), "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create and save the book data
                SachDTO sachDTO = new SachDTO();
                sachDTO.setTenSach(tenSach);
                sachDTO.setTacGia(tacGia);
                sachDTO.setSoLuong(Integer.parseInt(soLuong));
                sachDTO.setGiaSach(Integer.parseInt(giaSach));
                sachDTO.setLoaiSach(maLoaiSach);
                sachDTO.setImgSachPath(imgSachPath);
                Log.d("zzzzzzzzzzzz", imgSachPath);
                sachDTO.setNamXuatBan(Integer.parseInt(namXB));


                // Insert book into database
                sachDAO.AddSach(sachDTO);
                Log.d("Frag_Sach", "Book added: " + tenSach);

                // Refresh data and dismiss dialog
                capnhatsach();
                dialog.dismiss();
            }
        });
    }

    private void pickImageFromGalleryBook() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imgSachPath = saveImageToStorage(bitmap);
                imgPreviewSach.setImageBitmap(bitmap); // Hiển thị ảnh lên ImageView
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String saveImageToStorage(Bitmap bitmap) {
        File directory = getContext().getFilesDir();
        File file = new File(directory, "Sach_" + System.currentTimeMillis() + ".png");

        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    private void showSortMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.sortByNameAZ) {
                    sortByNameAZ();
                    return true;
                } else if (itemId == R.id.sortByNameZA) {
                    sortByNameZA();
                    return true;
                } else if (itemId == R.id.sortByPrice) {
                    sortByPrice();
                    return true;
                }
                return false;
            }
        });

        popupMenu.show();
    }


    private void sortByNameAZ() {
        listSach.sort((o1, o2) -> o1.getTenSach().compareToIgnoreCase(o2.getTenSach()));
        sachAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Sorted by name A-Z", Toast.LENGTH_SHORT).show();
    }

    // Sắp xếp theo tên từ Z-A
    private void sortByNameZA() {
        listSach.sort((o1, o2) -> o2.getTenSach().compareToIgnoreCase(o1.getTenSach()));
        sachAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Sorted by name Z-A", Toast.LENGTH_SHORT).show();
    }

    // Sắp xếp theo giá tiền
    private void sortByPrice() {
        listSach.sort((o1, o2) -> Integer.compare(o1.getGiaSach(), o2.getGiaSach()));
        sachAdapter.notifyDataSetChanged();
        Toast.makeText(getContext(), "Sorted by price", Toast.LENGTH_SHORT).show();
    }

    private void searchBooks(String keyword) {
        listSach.clear(); // Xóa danh sách cũ
        listSach.addAll(sachDAO.searchSach(keyword)); // Thêm danh sách mới từ kết quả tìm kiếm

        if (listSach.isEmpty()) {
            Toast.makeText(getContext(), "No books found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Found " + listSach.size() + " books", Toast.LENGTH_SHORT).show();
        }

        sachAdapter.notifyDataSetChanged(); // Cập nhật giao diện
    }


    private void capnhatsach() {

        listSach.clear();


        listSach.addAll(sachDAO.getAllSach());

        // Kiểm tra dữ liệu
        if (listSach.isEmpty()) {
            Toast.makeText(getContext(), "No books found", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Frag_Sach", "Number of books: " + listSach.size());
        }

        if (sachAdapter != null) {
            sachAdapter.notifyDataSetChanged();
        }
    }

}
