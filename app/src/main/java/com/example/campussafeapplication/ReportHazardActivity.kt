package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class ReportHazardActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hazard);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navReports).setOnClickListener(v ->
            startActivity(new Intent(this, MyReportsActivity.class)));

        findViewById(R.id.navAdd).setOnClickListener(v -> {
            // Already on Report Hazard
        });

        findViewById(R.id.navMaps).setOnClickListener(v ->
            startActivity(new Intent(this, NearbyReportsActivity.class)));

        findViewById(R.id.navSettings).setOnClickListener(v ->
            startActivity(new Intent(this, SettingsActivity.class)));

        setupSpinners();
    }

    private void setupSpinners(){
        Spinner spinnerBuilding = findViewById(R.id.spinnerBuilding);
        Spinner spinnerFloor = findViewById(R.id.spinnerFloor);

        ArrayAdapter<CharSequence> bAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.building_array,
                R.layout.spinner_item
        );
        bAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBuilding.setAdapter(bAdapter);

        ArrayAdapter<CharSequence> fAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.floor_array,
                R.layout.spinner_item
        );
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFloor.setAdapter(fAdapter);

        findViewById(R.id.layoutBuildingContainer).setOnClickListener(v -> spinnerBuilding.performClick());
        findViewById(R.id.layoutFloorContainer).setOnClickListener(v -> spinnerFloor.performClick());

    }
}
