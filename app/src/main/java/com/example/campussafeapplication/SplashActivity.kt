package com.example.campussafeapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.campussafeapplication.utils.SessionManager

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val sessionManager = SessionManager(this)
        
        val savedMode = sessionManager.getThemeMode()
        if (savedMode != -1) {
            AppCompatDelegate.setDefaultNightMode(savedMode)
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        
        findViewById<android.view.View>(R.id.main)?.let { mainView ->
            ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
        }

        // Delay for 3 seconds then decide which activity to start
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = when {
                sessionManager.isLoggedIn() -> {
                    if (sessionManager.isBiometricEnabled()) {
                        Intent(this, BiometricActivity::class.java)
                    } else {
                        Intent(this, MainActivity::class.java)
                    }
                }
                else -> Intent(this, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }, 3000)
    }
}
