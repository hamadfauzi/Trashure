package com.example.trashure.Feature.Beranda;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashure.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraphFragment extends Fragment {

    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_graph,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        barChart = getActivity().findViewById(R.id.barChart);
        getEntries();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(18f);
    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2f, 0));
        barEntries.add(new BarEntry(4f, 1));
        barEntries.add(new BarEntry(6f, 1));
        barEntries.add(new BarEntry(8f, 3));
        barEntries.add(new BarEntry(7f, 4));
        barEntries.add(new BarEntry(3f, 3));
    }
}
