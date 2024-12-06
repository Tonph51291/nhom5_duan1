package com.bookstorepoly.nhom5_duan1.frag;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.adapter.LichSuHoaDonAdapter;
import com.bookstorepoly.nhom5_duan1.dao.HoaDonDAO;
import com.bookstorepoly.nhom5_duan1.dto.HoaDonDTO;

import java.util.ArrayList;

public class Frag_LichSu extends Fragment {
    LichSuHoaDonAdapter lichSuHoaDonAdapter;
    ArrayList<HoaDonDTO> list;
    HoaDonDAO hoaDonDAO;
    RecyclerView rcv_lichsu;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_lichsu, container, false);
        rcv_lichsu = view.findViewById(R.id.rcv_lichsu);
        searchView = view.findViewById(R.id.search_view_ls);
        hoaDonDAO = new HoaDonDAO(getContext());
        list = new ArrayList<>();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String maNhanVien = sharedPreferences.getString("MaNhanVien", "");
        if (maNhanVien.equals("NV001")) {
            list = (ArrayList<HoaDonDTO>) hoaDonDAO.getAllHoaDon();
        } else {
            list = (ArrayList<HoaDonDTO>) hoaDonDAO.getHoaDonByMaNhanVien(maNhanVien);
            searchView.setVisibility(View.GONE);
        }


        lichSuHoaDonAdapter = new LichSuHoaDonAdapter(list, getContext());
        rcv_lichsu.setLayoutManager(new LinearLayoutManager(getContext()));
        rcv_lichsu.setAdapter(lichSuHoaDonAdapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchHoaDon(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHoaDon(newText);
                return true;
            }
        });

        return view;
    }

    private void searchHoaDon(String query) {
        list.clear();
        list.addAll(hoaDonDAO.searchHoaDonByMaNhanVien(query));
        lichSuHoaDonAdapter.notifyDataSetChanged();


    }
}
