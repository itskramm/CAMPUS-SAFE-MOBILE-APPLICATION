package com.example.campussafeapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MyReportsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
