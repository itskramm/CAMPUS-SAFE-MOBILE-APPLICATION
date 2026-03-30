package com.example.campussafeapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnCreateAccount = findViewById(R.id.btnCreateAccount);
        Button btnGoogle = findViewById(R.id.btnGoogle);
        TextView tvForgotPassword = findViewById(R.id.tvForgotPassword);

        btnLogin.setOnClickListener(v -> {
            // Basic validation could be added here
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        });

        btnGoogle.setOnClickListener(v -> {
            Toast.makeText(this, "Google Sign-In Clicked", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show();
        });
        
        // Bonus: If you want to allow Biometric entry from Login, we could add a button for it.
        // For now, it's usually triggered via Splash or a specific button.
    }
}
