# 🚀 Campus Safe Application - Supabase Setup

## ✅ What's Been Done

1. ✅ **Gradle Dependencies Added** - All Supabase and required libraries configured
2. ✅ **Supabase Client Configured** - Connected to your project
3. ✅ **Data Models Created** - User, HazardReport, SafetyTip
4. ✅ **Repositories Created** - AuthRepository, HazardReportRepository, SafetyTipRepository
5. ✅ **ViewModels Created** - For managing UI state and business logic
6. ✅ **Permissions Added** - Internet, Location, Biometric permissions in AndroidManifest
7. ✅ **SessionManager Updated** - For local session management

## 📋 What You Need to Do

### Step 1: Set Up Supabase Database Tables

1. Go to your Supabase Dashboard: https://app.supabase.com/project/uxafytqyohzjqcbbmyir
2. Click on **SQL Editor** in the left sidebar
3. Click **New Query**
4. Copy the entire contents of `supabase_setup.sql` file
5. Paste it into the SQL editor
6. Click **Run** button
7. You should see "Success. No rows returned" - this is normal!

### Step 2: Verify Tables Were Created

1. In Supabase Dashboard, click **Table Editor**
2. You should see three tables:
   - `users`
   - `hazard_reports`
   - `safety_tips`
3. Click on `safety_tips` - you should see 10 pre-populated safety tips

### Step 3: Enable Email Authentication

1. In Supabase Dashboard, go to **Authentication** → **Providers**
2. Make sure **Email** is enabled
3. Configure email settings:
   - Enable email confirmations (optional)
   - Set up email templates (optional)

### Step 4: Sync Gradle Files

1. In Android Studio, click **File** → **Sync Project with Gradle Files**
2. Wait for sync to complete (may take a few minutes)
3. If you see any errors, try **Build** → **Clean Project** then **Build** → **Rebuild Project**

### Step 5: Update Your Activities

Now you need to update your existing activities to use the Supabase integration. Here are the key changes:

#### LoginActivity.java → Convert to Kotlin or add ViewModel usage

**Option A: Quick Java Implementation**

```java
// In LoginActivity.java
import androidx.lifecycle.ViewModelProvider;
import com.example.campussafeapplication.viewmodels.AuthViewModel;
import com.example.campussafeapplication.utils.SessionManager;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.launch;

public class LoginActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private SessionManager sessionManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sessionManager = new SessionManager(this);
        
        Button btnLogin = findViewById(R.id.btnLogin);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            
            if (!email.isEmpty() && !password.isEmpty()) {
                authViewModel.signIn(email, password);
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Observe auth state
        authViewModel.getAuthState().observe(this, state -> {
            if (state instanceof AuthViewModel.AuthState.Success) {
                User user = ((AuthViewModel.AuthState.Success) state).getUser();
                sessionManager.saveUserSession(
                    user.getId(),
                    user.getEmail(),
                    user.getFullName() != null ? user.getFullName() : ""
                );
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else if (state instanceof AuthViewModel.AuthState.Error) {
                String message = ((AuthViewModel.AuthState.Error) state).getMessage();
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
```

## 🧪 Testing Your Implementation

### Test 1: Create Account
1. Run your app
2. Navigate to Create Account
3. Enter email, password, and full name
4. Click Sign Up
5. Check Supabase Dashboard → Authentication → Users (you should see the new user)
6. Check Table Editor → users (you should see the user profile)

### Test 2: Login
1. Use the credentials you just created
2. Click Login
3. You should be redirected to MainActivity

### Test 3: Create Hazard Report
1. After logging in, navigate to Report Hazard
2. Fill in the form
3. Submit
4. Check Supabase Dashboard → Table Editor → hazard_reports

### Test 4: View Reports
1. Navigate to My Reports
2. You should see your submitted reports

## 🔧 Troubleshooting

### Error: "Unable to resolve dependency"
- Make sure you've synced Gradle files
- Check your internet connection
- Try **File** → **Invalidate Caches** → **Invalidate and Restart**

### Error: "Supabase client not initialized"
- Make sure `SupabaseClient.kt` has the correct URL and key
- Check that you have internet permission in AndroidManifest.xml

### Error: "Row Level Security policy violation"
- Make sure you ran the `supabase_setup.sql` script completely
- Check that RLS policies are enabled in Supabase Dashboard

### Error: "User not found after signup"
- Check that the trigger `on_auth_user_created` was created successfully
- Verify in SQL Editor: `SELECT * FROM pg_trigger WHERE tgname = 'on_auth_user_created';`

## 📱 Example Usage Patterns

### Get Current User
```kotlin
val authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
authViewModel.getCurrentUser()

lifecycleScope.launch {
    authViewModel.currentUser.collect { user ->
        if (user != null) {
            // User is logged in
            textViewName.text = user.fullName
        }
    }
}
```

### Create Hazard Report
```kotlin
val reportViewModel = ViewModelProvider(this)[HazardReportViewModel::class.java]
val sessionManager = SessionManager(this)

val report = HazardReport(
    userId = sessionManager.getUserId(),
    hazardType = "Fire",
    description = "Smoke detected in building",
    location = "Main Campus Building A",
    latitude = 37.7749,
    longitude = -122.4194
)

reportViewModel.createReport(report)
```

### Load Safety Tips
```kotlin
val tipViewModel = ViewModelProvider(this)[SafetyTipViewModel::class.java]
tipViewModel.getAllSafetyTips()

lifecycleScope.launch {
    tipViewModel.safetyTips.collect { tips ->
        // Update your RecyclerView
        adapter.submitList(tips)
    }
}
```

## 📚 Next Steps

1. ✅ Complete Supabase database setup
2. ⏳ Update all activities to use ViewModels
3. ⏳ Implement RecyclerView adapters for lists
4. ⏳ Add location services for hazard reporting
5. ⏳ Implement image upload for hazard reports (using Supabase Storage)
6. ⏳ Add real-time updates using Supabase Realtime
7. ⏳ Implement biometric authentication

## 🆘 Need Help?

- **Supabase Docs**: https://supabase.com/docs
- **Kotlin Coroutines**: https://kotlinlang.org/docs/coroutines-overview.html
- **Android ViewModels**: https://developer.android.com/topic/libraries/architecture/viewmodel

## 📝 Important Notes

- **Never commit your Supabase keys to Git!** Consider using BuildConfig or local.properties
- Always handle errors gracefully in production
- Test RLS policies thoroughly before deploying
- Consider adding loading states in your UI
- Implement proper error messages for users

---

**Your Supabase Project**: https://app.supabase.com/project/uxafytqyohzjqcbbmyir
