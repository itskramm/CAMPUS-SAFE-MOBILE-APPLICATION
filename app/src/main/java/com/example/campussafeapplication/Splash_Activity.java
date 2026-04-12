package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int savedMode = getSharedPreferences("CampusSafePrefs", MODE_PRIVATE)
                .getInt("themeMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(savedMode);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Delay for 3 seconds then decide which activity to start
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            com.example.campussafeapplication.utils.SessionManager sessionManager = new com.example.campussafeapplication.utils.SessionManager(Splash_Activity.this);
            
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                // If user is logged in, use Biometric logic
                intent = new Intent(Splash_Activity.this, BiometricActivity.class);
            } else {
                // If not logged in, go to Login
                intent = new Intent(Splash_Activity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);
    }

}
