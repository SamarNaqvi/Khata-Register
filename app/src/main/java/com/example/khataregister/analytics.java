package com.example.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class analytics extends Fragment {
    PieChart pieChart;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_analytics, container, false);
        pieChart =(PieChart) view.findViewById(R.id.PieChart);
        ArrayList<PieEntry> records=new ArrayList<>();
        records.add(new PieEntry(40,"TotalDebt"));
        records.add(new PieEntry(60,"TotalReceived"));

        PieDataSet pieDataSet=new PieDataSet(records,"Analytics");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(22f);

        PieData pieData=new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(true);
        pieChart.setCenterText("Payable and Receivable Report");
        pieChart.animate();

        return view;

    }
}