# Supabase Implementation Guide for Campus Safe Application

## ✅ Setup Complete

Your Supabase integration is now configured with:
- **URL**: `https://uxafytyqohzjqcbbmyir.supabase.co`
- **Anon Key**: Configured in `SupabaseClient.kt`

## 📁 Project Structure

```
app/src/main/java/com/example/campussafeapplication/
├── models/
│   ├── User.kt                    # User data model
│   ├── HazardReport.kt            # Hazard report data model
│   └── SafetyTip.kt               # Safety tip data model
├── supabase/
│   └── SupabaseClient.kt          # Supabase client configuration
├── repository/
│   ├── AuthRepository.kt          # Authentication operations
│   ├── HazardReportRepository.kt  # Hazard report CRUD operations
│   └── SafetyTipRepository.kt     # Safety tips operations
├── viewmodels/
│   ├── AuthViewModel.kt           # Authentication ViewModel
│   ├── HazardReportViewModel.kt   # Hazard reports ViewModel
│   └── SafetyTipViewModel.kt      # Safety tips ViewModel
└── utils/
    └── SessionManager.kt          # Local session management
```

## 🗄️ Required Supabase Database Tables

Make sure your Supabase database has these tables:

### 1. `users` table
```sql
CREATE TABLE users (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  email TEXT UNIQUE NOT NULL,
  full_name TEXT,
  phone_number TEXT,
  biometric_enabled BOOLEAN DEFAULT false,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

### 2. `hazard_reports` table
```sql
CREATE TABLE hazard_reports (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  user_id UUID REFERENCES users(id) ON DELETE CASCADE,
  hazard_type TEXT NOT NULL,
  description TEXT NOT NULL,
  location TEXT NOT NULL,
  latitude DOUBLE PRECISION NOT NULL,
  longitude DOUBLE PRECISION NOT NULL,
  status TEXT DEFAULT 'Pending',
  severity TEXT DEFAULT 'Medium',
  image_url TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

### 3. `safety_tips` table
```sql
CREATE TABLE safety_tips (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  title TEXT NOT NULL,
  description TEXT NOT NULL,
  category TEXT NOT NULL,
  icon_name TEXT,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

## 🔐 Row Level Security (RLS)

Enable RLS on your tables in Supabase Dashboard:

```sql
-- Enable RLS
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE hazard_reports ENABLE ROW LEVEL SECURITY;
ALTER TABLE safety_tips ENABLE ROW LEVEL SECURITY;

-- Users can read their own data
CREATE POLICY "Users can view own profile" ON users
  FOR SELECT USING (auth.uid() = id);

-- Users can update their own data
CREATE POLICY "Users can update own profile" ON users
  FOR UPDATE USING (auth.uid() = id);

-- Anyone can read safety tips
CREATE POLICY "Anyone can view safety tips" ON safety_tips
  FOR SELECT USING (true);

-- Users can create hazard reports
CREATE POLICY "Users can create reports" ON hazard_reports
  FOR INSERT WITH CHECK (auth.uid() = user_id);

-- Users can view all reports
CREATE POLICY "Anyone can view reports" ON hazard_reports
  FOR SELECT USING (true);

-- Users can update their own reports
CREATE POLICY "Users can update own reports" ON hazard_reports
  FOR UPDATE USING (auth.uid() = user_id);

-- Users can delete their own reports
CREATE POLICY "Users can delete own reports" ON hazard_reports
  FOR DELETE USING (auth.uid() = user_id);
```

## 📱 How to Use in Your Activities

### Example 1: Login Activity

```kotlin
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sessionManager = SessionManager(this)
        
        // Observe auth state
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Loading -> {
                        // Show loading indicator
                    }
                    is AuthViewModel.AuthState.Success -> {
                        // Save session and navigate to main
                        sessionManager.saveUserSession(
                            state.user.id ?: "",
                            state.user.email,
                            state.user.fullName ?: ""
                        )
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        
        findViewById<Button>(R.id.btnLogin).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.signIn(email, password)
            }
        }
    }
}
```

### Example 2: Create Account Activity

```kotlin
class CreateAccountActivity : AppCompatActivity() {
    
    private lateinit var authViewModel: AuthViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)
        
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        
        lifecycleScope.launch {
            authViewModel.authState.collect { state ->
                when (state) {
                    is AuthViewModel.AuthState.Success -> {
                        Toast.makeText(this@CreateAccountActivity, "Account created!", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@CreateAccountActivity, MainActivity::class.java))
                        finish()
                    }
                    is AuthViewModel.AuthState.Error -> {
                        Toast.makeText(this@CreateAccountActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        
        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            val fullName = findViewById<EditText>(R.id.etFullName).text.toString()
            
            authViewModel.signUp(email, password, fullName)
        }
    }
}
```

### Example 3: Report Hazard Activity

```kotlin
class ReportHazardActivity : AppCompatActivity() {
    
    private lateinit var reportViewModel: HazardReportViewModel
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_hazard)
        
        reportViewModel = ViewModelProvider(this)[HazardReportViewModel::class.java]
        sessionManager = SessionManager(this)
        
        lifecycleScope.launch {
            reportViewModel.reportState.collect { state ->
                when (state) {
                    is HazardReportViewModel.ReportState.Success -> {
                        Toast.makeText(this@ReportHazardActivity, "Report submitted!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    is HazardReportViewModel.ReportState.Error -> {
                        Toast.makeText(this@ReportHazardActivity, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
        
        findViewById<Button>(R.id.btnSubmitReport).setOnClickListener {
            val userId = sessionManager.getUserId()
            val hazardType = findViewById<Spinner>(R.id.spinnerHazardType).selectedItem.toString()
            val description = findViewById<EditText>(R.id.etDescription).text.toString()
            val location = findViewById<EditText>(R.id.etLocation).text.toString()
            
            val report = HazardReport(
                userId = userId,
                hazardType = hazardType,
                description = description,
                location = location,
                latitude = 0.0, // Get from GPS
                longitude = 0.0 // Get from GPS
            )
            
            reportViewModel.createReport(report)
        }
    }
}
```

### Example 4: My Reports Activity

```kotlin
class MyReportsActivity : AppCompatActivity() {
    
    private lateinit var reportViewModel: HazardReportViewModel
    private lateinit var sessionManager: SessionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_reports)
        
        reportViewModel = ViewModelProvider(this)[HazardReportViewModel::class.java]
        sessionManager = SessionManager(this)
        
        // Load user reports
        val userId = sessionManager.getUserId()
        reportViewModel.getUserReports(userId)
        
        // Observe reports
        lifecycleScope.launch {
            reportViewModel.reports.collect { reports ->
                // Update RecyclerView with reports
                updateReportsList(reports)
            }
        }
    }
    
    private fun updateReportsList(reports: List<HazardReport>) {
        // Implement RecyclerView adapter update
    }
}
```

## 🔧 Additional Configuration

### Add Internet Permission
In `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### Update SessionManager
The `SessionManager` class needs to store user ID as String (UUID):

```kotlin
fun saveUserSession(userId: String, email: String, name: String) {
    prefs.edit().apply {
        putString(KEY_USER_ID, userId)
        putString(KEY_USER_EMAIL, email)
        putString(KEY_USER_NAME, name)
        putBoolean(KEY_IS_LOGGED_IN, true)
        apply()
    }
}

fun getUserId(): String = prefs.getString(KEY_USER_ID, "") ?: ""
```

## 🚀 Next Steps

1. ✅ Dependencies added
2. ✅ Supabase client configured
3. ✅ Models created
4. ✅ Repositories created
5. ✅ ViewModels created
6. ⏳ Create/verify Supabase tables
7. ⏳ Set up RLS policies
8. ⏳ Update your activities to use ViewModels
9. ⏳ Test authentication flow
10. ⏳ Test CRUD operations

## 📚 Resources

- [Supabase Kotlin Documentation](https://supabase.com/docs/reference/kotlin/introduction)
- [Supabase Dashboard](https://app.supabase.com/project/uxafytqyohzjqcbbmyir)
