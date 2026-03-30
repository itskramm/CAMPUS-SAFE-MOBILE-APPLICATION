package com.example.campussafeapplication;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        findViewById(R.id.btnSaveChanges).setOnClickListener(v -> {
            Toast.makeText(this, "Changes Saved Successfully", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
