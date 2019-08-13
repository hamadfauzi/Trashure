package com.example.trashure.Feature.Beranda;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trashure.RoundedBarChartRenderer;
import com.example.trashure.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class GraphTahunan extends Fragment {

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
        return inflater.inflate(R.layout.fragment_graph_tahunan,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        barChart = getActivity().findViewById(R.id.barChartTahunan);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM.BOTTOM);
        //barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawAxisLine(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getXAxis().setDrawAxisLine(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        RoundedBarChartRenderer roundedBarChartRenderer= new RoundedBarChartRenderer(barChart,barChart.getAnimator(),barChart.getViewPortHandler());
        roundedBarChartRenderer.setmRadius(40f);
        barChart.setRenderer(roundedBarChartRenderer);

        getEntries();
        barDataSet = new BarDataSet(barEntries, "");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barChart.invalidate();
        barChart.refreshDrawableState();
        barChart.animateXY(1000,1000);
        barDataSet.setHighLightAlpha(0);
        barDataSet.setColors(getActivity().getResources().getColor(R.color.colorBtn));
        barDataSet.setValueTextColor(getActivity().getResources().getColor(R.color.colorLightBlue));
        barDataSet.setValueTextSize(10f);

        barChart.getAxisLeft().setLabelCount(5, false);
        barChart.getAxisLeft().setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        barChart.getAxisLeft().setAxisMinimum(0f);
        barChart.getAxisLeft().setAxisMaximum(50f);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisLeft().setTextColor(getActivity().getResources().getColor(R.color.colorLightBlue));

        final ArrayList<String> jml_sampah = new ArrayList<>();
        for (int i = 0; i <= 50; i++) {
            jml_sampah.add(i+"kg");
        }

        barChart.getAxisLeft().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return jml_sampah.get((int) value);
            }
        });

        barChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        barChart.setVisibleXRangeMaximum(4);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setTextColor(getActivity().getResources().getColor(R.color.colorLightBlue));
        barChart.getXAxis().setTextSize(12f);
        barChart.getBarData().setBarWidth(0.125f);
    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(2019, 28));
        barEntries.add(new BarEntry(2020, 28));
        barEntries.add(new BarEntry(2021, 28));
        barEntries.add(new BarEntry(2022, 28));
    }
}
