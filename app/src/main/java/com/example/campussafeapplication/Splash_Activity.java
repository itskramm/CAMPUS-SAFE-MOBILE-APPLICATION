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

import com.example.campussafeapplication.utils.SessionManager;

public class Splash_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SessionManager sessionManager = new SessionManager(this);
        
        int savedMode = sessionManager.getThemeMode();
        if (savedMode != -1) {
            AppCompatDelegate.setDefaultNightMode(savedMode);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        
        if (findViewById(R.id.main) != null) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }

        // Delay for 3 seconds then decide which activity to start
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;
            if (sessionManager.isLoggedIn()) {
                if (sessionManager.isBiometricEnabled()) {
                    intent = new Intent(Splash_Activity.this, BiometricActivity.class);
                } else {
                    intent = new Intent(Splash_Activity.this, MainActivity.class);
                }
            } else {
                intent = new Intent(Splash_Activity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 3000);
    }
}
