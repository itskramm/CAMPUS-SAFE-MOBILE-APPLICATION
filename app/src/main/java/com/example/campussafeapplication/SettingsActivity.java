package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.campussafeapplication.utils.SessionManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        sessionManager = new SessionManager(this);

        // Top Bar
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Biometric Toggle
        SwitchMaterial switchBiometric = findViewById(R.id.switchBiometric);
        if (switchBiometric != null) {
            switchBiometric.setChecked(sessionManager.isBiometricEnabled());
            switchBiometric.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sessionManager.setBiometricEnabled(isChecked);
                String status = isChecked ? "enabled" : "disabled";
                Toast.makeText(this, "Biometric login " + status, Toast.LENGTH_SHORT).show();
            });
        }
        
        // Settings Options
        findViewById(R.id.btnNotifications).setOnClickListener(v -> 
            Toast.makeText(this, "Notification Settings Clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnAccount).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.btnPrivacy).setOnClickListener(v -> 
            startActivity(new Intent(this, BiometricActivity.class)));

        findViewById(R.id.btnHelp).setOnClickListener(v -> 
            Toast.makeText(this, "Help & Support Clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sessionManager.clearSession();
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
            
        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navReports).setOnClickListener(v -> 
            startActivity(new Intent(this, MyReportsActivity.class)));

        findViewById(R.id.navAdd).setOnClickListener(v -> 
            startActivity(new Intent(this, ReportHazardActivity.class)));

        findViewById(R.id.navMaps).setOnClickListener(v -> 
            startActivity(new Intent(this, NearbyReportsActivity.class)));

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            // Already on Settings
        });
    }
}
