    package com.bookstorepoly.nhom5_duan1.adapter;

    import android.content.Context;
    import android.content.SharedPreferences;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ImageView;
    import android.widget.TextView;
    import android.widget.Toast;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.fragment.app.FragmentManager;
    import androidx.fragment.app.FragmentTransaction;
    import androidx.recyclerview.widget.RecyclerView;

    import com.bookstorepoly.nhom5_duan1.dao.LoaiSachDAO;
    import com.bookstorepoly.nhom5_duan1.dto.LoaiSachDTO;
    import com.bookstorepoly.nhom5_duan1.frag.Frag_Sach;
    import com.bookstorepoly.nhom5_duan1.R;

    import java.util.ArrayList;

    public class LoaiSachAdapter extends RecyclerView.Adapter<LoaiSachAdapter.ViewHolderLoaiSach> {
        private Context context;
        private ArrayList<LoaiSachDTO> list;
        private LoaiSachDAO loaiSachDAO;
        private static final int REQUEST_CODE_PICK_IMAGE = 1;
        private LoaiSachDTO currentLoaiSachDTO;
        private ImageView imgPreview_up;
        AlertDialog dialog;
        FragmentManager fragmentManager;



        public void setOnUpdateImageListener(OnUpdateImageListener onUpdateImageListener) {
            this.onUpdateImageListener = onUpdateImageListener;
            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        }

        public OnUpdateImageListener onUpdateImageListener;

        public LoaiSachAdapter(Context context, ArrayList<LoaiSachDTO> list) {
            this.context = context;
            this.list = list;
            loaiSachDAO = new LoaiSachDAO(context);
            dialog = new AlertDialog.Builder(context).create();
        }

        @NonNull
        @Override
        public ViewHolderLoaiSach onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(context, R.layout.item_row_loaisach, null);
            return new ViewHolderLoaiSach(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolderLoaiSach holder, int position) {
            LoaiSachDTO loaiSachDTO = list.get(position);
            holder.txtTenLoaiSach.setText(loaiSachDTO.getTenLoaiSach());


            //Log.e("AAA",loaiSachDTO.getImgLoaiSachPath());
            Bitmap bitmap = BitmapFactory.decodeFile(loaiSachDTO.getImgLoaiSachPath());
            if (bitmap != null) {
                holder.imgLoaiSach.setImageBitmap(bitmap);

            } else {
                holder.imgLoaiSach.setImageResource(R.drawable.back_btn); // Placeholder image
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    SharedPreferences sha = context.getSharedPreferences("data", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sha.edit();
                    editor.putInt("maLoaiSach", loaiSachDTO.getMaLoaiSach());
                    editor.apply();

                    fragmentManager.beginTransaction().replace(R.id.frag_container, new Frag_Sach()).commit();
                    editor.commit();



                    return true;
                }
            });

            holder.btnUpdateLoaiSach.setOnClickListener(v -> {
                showUpdateDialog(position);
                currentLoaiSachDTO = loaiSachDTO; // Store the current LoaiSachDTO


            });
        }




        private void showUpdateDialog(int index) {
            // Lấy dữ liệu của loại sách hiện tại
            currentLoaiSachDTO = list.get(index);

            // Khởi tạo và hiển thị dialog cập nhật

            dialog.setContentView(R.layout.dialog_up_loaisach);
            ImageButton btnLayAnh_up = dialog.findViewById(R.id.btnLayAnh_up);
            imgPreview_up = dialog.findViewById(R.id.imgPreview_up);
            EditText edtTenLoaiSach_up = dialog.findViewById(R.id.edtTenLoaiSach_up);
            Button btnUpLoaiSach = dialog.findViewById(R.id.btn_up_loaisach);

            // Thiết lập tên loại sách hiện tại cho EditText
            edtTenLoaiSach_up.setText(currentLoaiSachDTO.getTenLoaiSach());

            // Thiết lập hình ảnh hiện tại của loại sách (nếu có)
            Bitmap bitmap = BitmapFactory.decodeFile(currentLoaiSachDTO.getImgLoaiSachPath());
            if (bitmap != null) {
                imgPreview_up.setImageBitmap(bitmap);
            } else {
                imgPreview_up.setImageResource(R.drawable.sach1); // Hình ảnh placeholder
            }

            // Xử lý khi nhấn nút lấy ảnh
            btnLayAnh_up.setOnClickListener(v -> {
                onUpdateImageListener.showPicker(index); // Gọi listener để chọn ảnh
            });

            // Xử lý khi nhấn nút cập nhật
            btnUpLoaiSach.setOnClickListener(v -> {
                String tenLoaiSach = edtTenLoaiSach_up.getText().toString();
                if (tenLoaiSach.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập tên loại sách", Toast.LENGTH_SHORT).show();
                } else {
                    currentLoaiSachDTO.setTenLoaiSach(tenLoaiSach); // Cập nhật tên loại sách

                    // Cập nhật dữ liệu trong cơ sở dữ liệu và làm mới adapter
                    loaiSachDAO = new LoaiSachDAO(context);
                    loaiSachDAO.UpdateLoaiSach(currentLoaiSachDTO);
                    onUpdateImageListener.OnUpdate(index,bitmap);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
        }





        @Override
        public int getItemCount() {
            return list.size();
        }

        public void upImageBitMap(Bitmap bitmap) {
            imgPreview_up = dialog.findViewById(R.id.imgPreview_up);
            imgPreview_up.setImageBitmap(bitmap);
        }

        public class ViewHolderLoaiSach extends RecyclerView.ViewHolder {
            TextView txtTenLoaiSach;

            ImageView imgLoaiSach;
            ImageView btnUpdateLoaiSach;

            public ViewHolderLoaiSach(@NonNull View itemView) {
                super(itemView);
                txtTenLoaiSach = itemView.findViewById(R.id.book_title);


                imgLoaiSach = itemView.findViewById(R.id.book_image);
                btnUpdateLoaiSach = itemView.findViewById(R.id.btn_update_ls);
            }
        }
    }
