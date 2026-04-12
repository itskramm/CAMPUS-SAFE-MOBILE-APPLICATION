package com.example.campussafeapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.campussafeapplication.utils.SessionManager
import com.example.campussafeapplication.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sessionManager = SessionManager(this)
        
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnCreateAccount = findViewById<Button>(R.id.btnCreateAccount)
        val btnGoogle = findViewById<Button>(R.id.btnGoogle)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)
        val etUsername = findViewById<EditText>(R.id.etUsername) // Corrected ID from XML
        val etPassword = findViewById<EditText>(R.id.etPassword)
        
        // Observe auth state
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        btnLogin.isEnabled = false
                    }
                    is AuthViewModel.AuthState.Success -> {
                        btnLogin.isEnabled = true
                        sessionManager.saveUserSession(
                            state.user.id ?: "",
                            state.user.email,
                            state.user.fullName ?: ""
                        )
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        btnLogin.isEnabled = true
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        btnLogin.isEnabled = true
                    }
                }
            }
        }
        
        btnLogin.setOnClickListener {
            val email = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signIn(email, password)
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
        
        btnCreateAccount.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }
        
        btnGoogle.setOnClickListener {
            Toast.makeText(this, "Google Sign-In Clicked", Toast.LENGTH_SHORT).show()
        }
        
        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot Password Clicked", Toast.LENGTH_SHORT).show()
        }
    }
}
