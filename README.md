# 🛡️ Campus Safe — Mobile Application

A campus safety Android application for **STI College Global City** that allows students and staff to report hazards, view nearby incidents, and access safety resources — all from their mobile devices.

---

## 📱 Features

### 🔐 Authentication
- Email and password sign-up / sign-in via Supabase Auth
- Biometric login (fingerprint / device credential)
- Persistent session management

### 🚨 Hazard Reporting
- Submit hazard reports with title, description, building, floor, and room number
- Optional GPS location verification (must be within 200 m of the campus)
- Attach photos from the camera or gallery
- AI-powered validation using **Google Gemini 1.5 Flash** — automatically rejects spam or non-hazard submissions
- Auto-detected hazard categories: Fire, Flood, Medical, Security, Structural, Other

### 🗺️ Nearby Reports
- View the latest campus hazard reports in real time
- Filter by floor (All / Floor 1 / Floor 2)
- Color-coded status badges: Pending · In Progress · Resolved
- Inline edit and delete for report management

### 📋 My Reports
- Personal dashboard listing all reports submitted by the logged-in user
- Status tracking for each submission

### 💡 Safety Tips
- Curated safety tips fetched from the database
- Emergency hotlines section with collapsible sections

### ⚙️ Settings & Profile
- Dark / light theme toggle
- Edit profile (name, phone number)
- Privacy & security settings
- Biometric authentication toggle
- Help & support section

---

## 🏗️ Architecture

The app follows the **MVVM** (Model-View-ViewModel) pattern with a repository layer:

```
app/src/main/java/com/example/campussafeapplication/
├── models/                   # Data classes (User, HazardReport, SafetyTip)
├── supabase/                 # Supabase client configuration
├── repository/               # Data access layer
│   ├── AuthRepository.kt
│   ├── HazardReportRepository.kt
│   └── SafetyTipRepository.kt
├── viewmodels/               # UI state and business logic
│   ├── AuthViewModel.kt
│   ├── HazardReportViewModel.kt
│   └── SafetyTipViewModel.kt
├── utils/
│   ├── SessionManager.kt     # Local session storage
│   └── SwipeNavigationHelper.kt
└── [Activity files]          # UI layer
```

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin + Java |
| UI | Android Views · Material Design 3 · Jetpack Compose |
| Architecture | MVVM · Repository pattern |
| Backend | [Supabase](https://supabase.com) (Auth · PostgREST · Realtime · Storage) |
| Networking | Ktor client (Android) |
| Location | Google Play Services — Fused Location Provider |
| Maps | Google Maps SDK |
| AI Validation | Google Gemini 1.5 Flash |
| Image Loading | Glide |
| Async | Kotlin Coroutines · StateFlow |
| Min SDK | 26 (Android 8.0 Oreo) |
| Target SDK | 36 |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device or emulator running API 26+
- A [Supabase](https://supabase.com) project with the required tables

### 1. Clone the repository
```bash
git clone https://github.com/itskramm/CAMPUS-SAFE-MOBILE-APPLICATION.git
cd CAMPUS-SAFE-MOBILE-APPLICATION
```

### 2. Configure Supabase

Set up the database by running the provided SQL script in the Supabase SQL Editor:

```bash
# Open supabase_setup.sql and execute it in:
# https://app.supabase.com → SQL Editor → New Query
```

This creates the following tables along with Row Level Security (RLS) policies:

| Table | Description |
|---|---|
| `users` | User profiles linked to Supabase Auth |
| `hazard_reports` | Campus hazard submissions |
| `safety_tips` | Pre-populated safety guidance |

### 3. Add API keys

Open `app/src/main/java/com/example/campussafeapplication/supabase/SupabaseClient.kt` and set your Supabase project URL and anon key.

> ⚠️ **Never commit API keys to version control.** Consider moving them to `local.properties` or Android `BuildConfig`.

### 4. Sync and build
```bash
./gradlew assembleDebug
```

Or open the project in Android Studio and click **Sync Project with Gradle Files**, then **Run**.

---

## 🧪 Running Tests

```bash
# Lint + unit tests
./gradlew --no-daemon lintDebug testDebugUnitTest

# Unit tests only
./gradlew --no-daemon testDebugUnitTest
```

---

## 📋 Required Permissions

| Permission | Purpose |
|---|---|
| `INTERNET` | Supabase API communication |
| `ACCESS_NETWORK_STATE` | Network availability checks |
| `ACCESS_FINE_LOCATION` | GPS-based hazard location |
| `ACCESS_COARSE_LOCATION` | Fallback location |
| `USE_BIOMETRIC` | Fingerprint / biometric login |
| `CAMERA` | Photo capture for reports |

---

## 🗄️ Database Schema

See [`supabase_setup.sql`](supabase_setup.sql) for the full schema and RLS policies.

For a step-by-step Supabase setup guide, see [`README_SUPABASE.md`](README_SUPABASE.md).

---

## 📂 Key Screens

| Screen | Class |
|---|---|
| Splash | `Splash_Activity` |
| Login | `LoginActivity` |
| Create Account | `CreateAccountActivity` |
| Biometric Login | `BiometricActivity` |
| Home Dashboard | `MainActivity` |
| Report Hazard | `ReportHazardActivity` |
| Nearby Reports | `NearbyReportsActivity` |
| My Reports | `MyReportsActivity` |
| Safety Tips | `SafetyTipsActivity` |
| Profile | `ProfileActivity` |
| Settings | `SettingsActivity` |
| Privacy & Security | `PrivacySecurityActivity` |
| Help & Support | `HelpSupportActivity` |

---

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit your changes: `git commit -m "Add my feature"`
4. Push to the branch: `git push origin feature/my-feature`
5. Open a Pull Request

---

## 📄 License

This project is developed for academic purposes at STI College Global City.
