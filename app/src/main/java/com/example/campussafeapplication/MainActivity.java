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

        setupThemeToggle();

        setupNavigation();
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(this, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    private void setupNavigation() {
        // this is for the grid menu
        findViewById(R.id.gridReportHazards).setOnClickListener(v -> navigateTo(ReportHazardActivity.class));
        findViewById(R.id.gridNearbyReports).setOnClickListener(v -> navigateTo(NearbyReportsActivity.class));
        findViewById(R.id.gridMyReports).setOnClickListener(v -> navigateTo(MyReportsActivity.class));
        findViewById(R.id.gridSafetyTips).setOnClickListener(v -> navigateTo(SafetyTipsActivity.class));

        // bottom bar navigation
        findViewById(R.id.navReports).setOnClickListener(v -> navigateTo(MyReportsActivity.class));
        findViewById(R.id.navAdd).setOnClickListener(v -> navigateTo(ReportHazardActivity.class));
        findViewById(R.id.navMaps).setOnClickListener(v -> navigateTo(NearbyReportsActivity.class));
        findViewById(R.id.navSettings).setOnClickListener(v -> navigateTo(SettingsActivity.class));

        //search button (subject for removal)
        findViewById(R.id.btnSearch).setOnClickListener(v -> navigateTo(NearbyReportsActivity.class));

        // home (bottom navigation bar)
        findViewById(R.id.navHome).setOnClickListener(v -> {});
    }
        // Theme Toggle
        private void setupThemeToggle() {
        ImageView themeToggle = findViewById(R.id.btnThemeToggle);
        themeToggle.setOnClickListener(v -> {
            int newMode = (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                    ? AppCompatDelegate.MODE_NIGHT_NO
                    : AppCompatDelegate.MODE_NIGHT_YES;

            AppCompatDelegate.setDefaultNightMode(newMode);

            getSharedPreferences("CampusSafePrefs", MODE_PRIVATE)
                    .edit()
                    .putInt("themeMode", newMode)
                    .apply();
        });
    }
}

