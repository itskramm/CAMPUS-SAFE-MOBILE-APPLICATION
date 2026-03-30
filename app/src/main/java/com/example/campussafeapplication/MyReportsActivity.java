package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MyReportsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reports);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navReports).setOnClickListener(v -> {
            // Already on My Reports
        });

        findViewById(R.id.navAdd).setOnClickListener(v -> 
            startActivity(new Intent(this, ReportHazardActivity.class)));

        findViewById(R.id.navMaps).setOnClickListener(v -> 
            startActivity(new Intent(this, NearbyReportsActivity.class)));

        findViewById(R.id.navSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));
    }
}
