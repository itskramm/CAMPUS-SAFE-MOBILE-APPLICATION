package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Theme Toggle
        ImageView btnThemeToggle = findViewById(R.id.btnThemeToggle);
        btnThemeToggle.setOnClickListener(v -> {
            int currentMode = AppCompatDelegate.getDefaultNightMode();
            if (currentMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        });

        // Grid Menu Navigation
        findViewById(R.id.gridReportHazards).setOnClickListener(v -> 
            startActivity(new Intent(this, ReportHazardActivity.class)));

        findViewById(R.id.gridNearbyReports).setOnClickListener(v -> 
            startActivity(new Intent(this, NearbyReportsActivity.class)));

        findViewById(R.id.gridMyReports).setOnClickListener(v -> 
            startActivity(new Intent(this, MyReportsActivity.class)));

        findViewById(R.id.gridSafetyTips).setOnClickListener(v -> 
            startActivity(new Intent(this, SafetyTipsActivity.class)));

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            // Already on Home
        });

        findViewById(R.id.navReports).setOnClickListener(v -> 
            startActivity(new Intent(this, MyReportsActivity.class)));

        findViewById(R.id.navAdd).setOnClickListener(v -> 
            startActivity(new Intent(this, ReportHazardActivity.class)));

        findViewById(R.id.navMaps).setOnClickListener(v -> 
            startActivity(new Intent(this, NearbyReportsActivity.class)));

        findViewById(R.id.navSettings).setOnClickListener(v -> 
            startActivity(new Intent(this, SettingsActivity.class)));
            
        // Search (could open nearby reports or a dedicated search)
        findViewById(R.id.btnSearch).setOnClickListener(v -> 
            startActivity(new Intent(this, NearbyReportsActivity.class)));
    }
}
