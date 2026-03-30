package com.example.campussafeapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class NearbyReportsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_reports);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }
}
