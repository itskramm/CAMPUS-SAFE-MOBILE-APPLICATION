package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.example.campussafeapplication.utils.SessionManager;
import com.example.campussafeapplication.viewmodels.AuthViewModel;

public class SettingsActivity extends AppCompatActivity {
    private SessionManager sessionManager;
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sessionManager = new SessionManager(this);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Top Bar
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Settings Options
        findViewById(R.id.btnNotifications).setOnClickListener(v -> 
            Toast.makeText(this, "Notification Settings Clicked", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnAccount).setOnClickListener(v -> 
            startActivity(new Intent(this, ProfileActivity.class)));

        findViewById(R.id.btnPrivacy).setOnClickListener(v -> 
        {
            boolean enabled = !sessionManager.isBiometricEnabled();
            sessionManager.setBiometricEnabled(enabled);
            authViewModel.updateBiometricSetting(enabled);
            Toast.makeText(
                    this,
                    enabled ? "Biometric login enabled" : "Biometric login disabled",
                    Toast.LENGTH_SHORT
            ).show();
        });

        findViewById(R.id.btnHelp).setOnClickListener(v -> 
            startActivity(new Intent(this, SafetyTipsActivity.class)));

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            authViewModel.signOut();
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
