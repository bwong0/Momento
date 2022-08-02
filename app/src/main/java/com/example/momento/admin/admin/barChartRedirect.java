package com.example.momento.admin.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.momento.R;
import com.example.momento.databinding.ActivityBarChartRedirectBinding;
import com.example.momento.databinding.ActivityLoginBinding;

public class barChartRedirect extends AppCompatActivity {
    private ActivityBarChartRedirectBinding binding;
    private long[] videoCount;
    private long[] familySpecificCount = new long[3];
    private String Tag = "bar chart redirect: ";
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart_redirect);

        binding = ActivityBarChartRedirectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final Button chart1 = binding.btnChart1;
        final Button chart2 = binding.btnChart2;
        final Button chart3 = binding.btnChart3;
        final Button chart4 = binding.btnChart4;
        final Button chart5 = binding.btnChart5;
        final Button chart6 = binding.btnChart6;

        videoCount = getIntent().getLongArrayExtra("video counts");

        chart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[0];
                familySpecificCount[1] = videoCount[1];
                familySpecificCount[2] = videoCount[2];
                Log.d(Tag,"video counts are: "+ familySpecificCount[0] + " " + familySpecificCount[1] + " " + familySpecificCount[2]);
                goToBarChartDisplay(familySpecificCount);
            }
        });

        chart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[3];
                familySpecificCount[1] = videoCount[4];
                familySpecificCount[2] = videoCount[5];
                Log.d(Tag,"video counts are: "+ familySpecificCount[0] + " " + familySpecificCount[1] + " " + familySpecificCount[2]);
                goToBarChartDisplay(familySpecificCount);
            }
        });

        chart3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[6];
                familySpecificCount[1] = videoCount[7];
                familySpecificCount[2] = videoCount[8];
                goToBarChartDisplay(familySpecificCount);
            }
        });

        chart4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[9];
                familySpecificCount[1] = videoCount[10];
                familySpecificCount[2] = videoCount[11];
                goToBarChartDisplay(familySpecificCount);
            }
        });

        chart5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[12];
                familySpecificCount[1] = videoCount[13];
                familySpecificCount[2] = videoCount[14];
                goToBarChartDisplay(familySpecificCount);
            }
        });

        chart6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familySpecificCount[0] = videoCount[15];
                familySpecificCount[1] = videoCount[16];
                familySpecificCount[2] = videoCount[17];
                goToBarChartDisplay(familySpecificCount);
            }
        });

    }

    public void goToBarChartDisplay(long[] SvideoCount){
        Intent intent = new Intent(this, barChartDisplay.class);
        intent.putExtra("video counts specific", SvideoCount);
        startActivity(intent);
    }
}