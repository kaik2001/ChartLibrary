package com.example.chartlibrary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.textView);
        textView.setText("Success");

        LineChart lineChart = findViewById(R.id.lineChart);
        lineChart.lines.remove(0);

        RadarChart radarChart = findViewById(R.id.radarChart);
        radarChart.setExampleString("Success");
    }
}