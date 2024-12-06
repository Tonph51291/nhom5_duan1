package com.bookstorepoly.nhom5_duan1.frag;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bookstorepoly.nhom5_duan1.R;
import com.bookstorepoly.nhom5_duan1.dao.HoaDonDAO;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Frag_ThongKe extends Fragment {

    private BarChart barChart;
    private TextView tvTotalRevenue;
    private Spinner spinnerFilter;
    private HoaDonDAO hoaDonDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_thongke, container, false);

        barChart = view.findViewById(R.id.barChart);
        tvTotalRevenue = view.findViewById(R.id.tvTotalRevenue);
        spinnerFilter = view.findViewById(R.id.spinnerFilter);
        hoaDonDAO = new HoaDonDAO(getContext());

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (view == null) return;
                switch (position) {
                    case 0:
                        setupBarChartByDay();
                        break;
                    case 1:
                        setupBarChartByWeek();
                        break;
                    case 2:
                        setupBarChartByMonth();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void setupBarChartByDay() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[7];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 6; i >= 0; i--) { // Đảo ngược thứ tự
            String date = dateFormat.format(calendar.getTime());
            int revenue = hoaDonDAO.getTotalRevenueByDay(date);
            entries.add(new BarEntry(i, revenue));
            labels[i] = date;
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Decrease one day
        }

        updateChart(entries, labels, calculateTotalRevenue(entries));
    }


    private void setupBarChartByWeek() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[4];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 4; i++) {
            String endDate = dateFormat.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_MONTH, -6); // Decrease six days to get the start of the week
            String startDate = dateFormat.format(calendar.getTime());
            int revenue = hoaDonDAO.getTotalRevenueByWeek(startDate, endDate);
            entries.add(new BarEntry(i, revenue));
            labels[i] = startDate + " - " + endDate;
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Decrease one more day to prepare for next week
        }

        updateChart(entries, labels, calculateTotalRevenue(entries));
    }

    private void setupBarChartByMonth() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] labels = new String[6];
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < 6; i++) {
            String month = dateFormat.format(calendar.getTime());
            int revenue = hoaDonDAO.getTotalRevenueByMonth(month);
            entries.add(new BarEntry(i, revenue));
            labels[i] = month;
            calendar.add(Calendar.MONTH, -1); // Decrease one month
        }

        updateChart(entries, labels, calculateTotalRevenue(entries));
    }

    private void updateChart(ArrayList<BarEntry> entries, String[] labels, String totalRevenue) {
        BarDataSet dataSet = new BarDataSet(entries, "Doanh thu");
        dataSet.setColor(Color.parseColor("#44B9F8")); // Hoặc bất kỳ màu nào khác
        barChart.setBackgroundColor(Color.parseColor("#F0F0F0")); // Set to a light gray or your desired color


        BarData data = new BarData(dataSet);
        barChart.setData(data);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);

        // Xoay các nhãn trục X để tránh chồng chéo
        xAxis.setLabelRotationAngle(-45);

        // Thiết lập khoảng cách giữa các nhãn
        xAxis.setAxisMinimum(-0.5f); // Bắt đầu trục X
        xAxis.setAxisMaximum(labels.length - 0.5f); // Kết thúc trục X

        // Thêm khoảng cách giữa các thanh
        data.setBarWidth(0.3f);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(1000);
        barChart.invalidate();

        tvTotalRevenue.setText("Tổng doanh thu: " + totalRevenue);
    }


    private String calculateTotalRevenue(ArrayList<BarEntry> entries) {
        int total = 0;
        for (BarEntry entry : entries) {
            total += entry.getY();
        }
        return String.valueOf(total);
    }
}
