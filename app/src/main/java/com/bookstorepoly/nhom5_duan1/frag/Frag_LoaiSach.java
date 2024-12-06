package com.bookstorepoly.nhom5_duan1.frag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
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

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.adapter.LoaiSachAdapter;
import com.bookstorepoly.nhom5_duan1.adapter.OnUpdateImageListener;
import com.bookstorepoly.nhom5_duan1.dao.LoaiSachDAO;
import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
import com.bookstorepoly.nhom5_duan1.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


    public class Frag_LoaiSach extends Fragment {
        private RecyclerView recyclerView;
        private LoaiSachDAO loaiSachDAO;
        private ArrayList<LoaiSachDTO> listLoaiSach;
        private FloatingActionButton btn_add_ls;
        private ImageView imgPreview;
        private ImageButton btn_pop_menu_sach;
        private String imgLoaiSachPath;
        private static final int PICK_IMAGE_REQUEST = 1;
        private LoaiSachAdapter loaiSachAdapter;
        private boolean isAddingImage = false;
        private SearchView searchViewLoaiSach;

        public int position = -1;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.frag_loaisach, container, false);
            btn_add_ls = v.findViewById(R.id.btn_add_ls);
            recyclerView = v.findViewById(R.id.rcv_loaisach);
            searchViewLoaiSach = v.findViewById(R.id.search_loai_sach);


            listLoaiSach = new ArrayList<>();
            loaiSachDAO = new LoaiSachDAO(getContext());


            loaiSachAdapter = new LoaiSachAdapter(getContext(), listLoaiSach);

            loaiSachAdapter.setOnUpdateImageListener(new OnUpdateImageListener() {
                @Override
                public void showPicker(int index) {
                    isAddingImage = false;
                    position = index;
                    pickImageFromGallery();
                }

                @Override
                public void OnUpdate(int index, Bitmap bitmap) {

                }

                @Override
                public void OnFail() {
                    Toast.makeText(getContext(), "Failed to update image", Toast.LENGTH_SHORT).show();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(loaiSachAdapter);

            searchViewLoaiSach.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchBooks(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchBooks(newText);
                    return true;
                }
            });
            // Load data
            capNhatLoaiSach();

            return v;
        }

        private void searchBooks(String query) {
            listLoaiSach.clear();
            listLoaiSach.addAll(loaiSachDAO.searchLoaiSach(query));
            loaiSachAdapter.notifyDataSetChanged();
            if (listLoaiSach.isEmpty()) {
                Toast.makeText(getContext(), "No books found", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Search results", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            btn_add_ls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAddingImage = true; // Set to true since this is for adding
                    showAddLoaiSachDialog();
                }
            });
        }

        private void showAddLoaiSachDialog() {
            AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_loaisach, null);
            dialog.setView(dialogView);
            dialog.show();

            Button btnLayAnh = dialogView.findViewById(R.id.btnLayAnh);
            EditText edtTenLoaiSach = dialogView.findViewById(R.id.edtTenLoaiSach);
            Button btnThemLoaiSach = dialogView.findViewById(R.id.btnThemLoaiSach);
            imgPreview = dialogView.findViewById(R.id.imgPreview);

            btnLayAnh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pickImageFromGallery();
                }
            });

            btnThemLoaiSach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tenLoaiSach = edtTenLoaiSach.getText().toString();
                    if (tenLoaiSach.isEmpty() || imgLoaiSachPath == null) {
                        Toast.makeText(getContext(), "Vui lòng nhập tên loại sách và chọn ảnh", Toast.LENGTH_SHORT).show();
                    } else {
                        LoaiSachDTO loaiSachDTO = new LoaiSachDTO();
                        loaiSachDTO.setTenLoaiSach(tenLoaiSach);
                        loaiSachDTO.setImgLoaiSachPath(imgLoaiSachPath);
                        Log.e("UPDATE", imgLoaiSachPath);
                        loaiSachDAO.AddLoaiSach(loaiSachDTO);
                        capNhatLoaiSach();
                        dialog.dismiss();
                    }
                }
            });
        }

        private void pickImageFromGallery() {
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
                    imgLoaiSachPath = saveImageToStorage(bitmap);
                    Log.e("vvvvvv",imgLoaiSachPath);

                    if (isAddingImage) {
                        imgPreview.setImageBitmap(bitmap);
                    } else {
                        listLoaiSach.get(position).setImgLoaiSachPath(imgLoaiSachPath);
                        loaiSachAdapter.notifyDataSetChanged();
                        loaiSachAdapter.upImageBitMap(bitmap);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error loading image", Toast.LENGTH_SHORT).show();
                }
            }
        }

        private String saveImageToStorage(Bitmap bitmap) {
            File directory = getContext().getFilesDir();
            File file = new File(directory, "LoaiSach_" + System.currentTimeMillis() + ".png");

            try (FileOutputStream fos = new FileOutputStream(file)) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error saving image", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        public void capNhatLoaiSach() {
            listLoaiSach.clear(); // Clear the existing list to avoid duplicates
            listLoaiSach.addAll(loaiSachDAO.getAllLoaiSach()); // Load the updated data
            if (loaiSachAdapter != null) {
                loaiSachAdapter.notifyDataSetChanged(); // Notify the adapter of data change
            }
        }
    }
