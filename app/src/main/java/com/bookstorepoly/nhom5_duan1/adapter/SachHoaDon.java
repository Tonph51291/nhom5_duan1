package com.bookstorepoly.nhom5_duan1.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookstorepoly.nhom5_duan1.dto.SachDTO;
import com.bookstorepoly.nhom5_duan1.R;

import java.util.ArrayList;
import java.util.List;

public class SachHoaDon extends BaseAdapter implements Filterable {

    private Context context;
    private List<SachDTO> originalList;
    private List<SachDTO> filteredList;
    private CustomFilter filter;

    public SachHoaDon(Context context, List<SachDTO> listSach) {
        this.context = context;
        this.originalList = new ArrayList<>(listSach); // Lưu danh sách gốc
        this.filteredList = listSach; // Danh sách hiển thị
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra vị trí hợp lệ
        if (filteredList == null || position >= filteredList.size()) {
            return new View(context);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_sp_hoadon, parent, false);
        }

        TextView tvTenSach = convertView.findViewById(R.id.tv_ten_sach_hd);
        ImageView imgSach = convertView.findViewById(R.id.img_sach_hd);

        SachDTO sach = filteredList.get(position);

        // Hiển thị tên sách
        tvTenSach.setText(sach.getTenSach());

        // Hiển thị ảnh sách
        Bitmap bitmap = BitmapFactory.decodeFile(sach.getImgSachPath());
        if (bitmap != null) {
            imgSach.setImageBitmap(bitmap);
        } else {
            // imgSach.setImageResource(R.drawable.placeholder_image); // Ảnh mặc định
        }

        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint == null || constraint.length() == 0) {
                results.values = originalList;
                results.count = originalList.size();
            } else {
                String searchStr = constraint.toString().toLowerCase().trim();
                List<SachDTO> filteredList = new ArrayList<>();
                for (SachDTO sach : originalList) {
                    if (sach.getTenSach().toLowerCase().contains(searchStr)) {
                        filteredList.add(sach);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            }

            if (results.count == 0) {
                results.values = new ArrayList<>(); // Trả về danh sách rỗng nếu không tìm thấy
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (List<SachDTO>) results.values;
            notifyDataSetChanged();
        }
    }
}
