package com.example.momento.admin.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.momento.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class barChartDisplay extends AppCompatActivity {
    private long[] videoCounts;
    private ArrayList<BarEntry> dataSet = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart_display);

        BarChart chart = findViewById(R.id.barchart);
        videoCounts = getIntent().getLongArrayExtra("video counts specific");

        BarEntry barEntry1 = new BarEntry(0, (float) videoCounts[0]);
        dataSet.add(barEntry1);
        BarEntry barEntry2 = new BarEntry(1, (float) videoCounts[1]);
        dataSet.add(barEntry2);
        BarEntry barEntry3 = new BarEntry(2, (float) videoCounts[2]);
        dataSet.add(barEntry3);

        BarDataSet bardataset = new BarDataSet(dataSet, "video play counts");
        chart.animateY(5000);
        BarData data = new BarData(bardataset);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);
    }
}