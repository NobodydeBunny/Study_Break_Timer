<div align="center">

# ⏱ INTERVAL
### Study Break Timer — Android Application

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
[![Java](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.java.com)
[![SQLite](https://img.shields.io/badge/Database-SQLite-003B57?style=for-the-badge&logo=sqlite&logoColor=white)](https://www.sqlite.org)
[![Material Design](https://img.shields.io/badge/UI-Material%20Design%203-757575?style=for-the-badge&logo=material-design&logoColor=white)](https://m3.material.io)

*A Pomodoro-inspired productivity app that helps you study smarter — not longer.*

</div>

---

## 📖 About

**Interval** is a native Android application built with Java that implements the Pomodoro technique — a time management method that alternates focused study sessions with short breaks. Users can register an account, create custom study sessions, track their history, and stay on top of their productivity — all backed by a local SQLite database with secure authentication.

---

## ✨ Features

### 🔐 Authentication
- Secure user registration and login
- Passwords hashed using **SHA-256** — never stored in plain text
- Session persistence via `SharedPreferences`
- One-tap logout with confirmation dialog

### ⏱ Timer Engine
- Countdown timer with animated **circular progress indicator**
- Study phase fills progress **clockwise**; Break phase drains **counter-clockwise**
- Pause, Resume, Skip, and Stop controls
- Smart battery management — screen only wakes at timer transitions, not throughout
- **Haptic feedback** on button taps and timer completions

### 📋 Session Management — Full CRUD
- **Create** custom sessions with name, study duration, and break duration
- **Read** — dashboard auto-loads your latest saved session on resume
- **Update** — rename sessions from history
- **Delete** — remove sessions with a confirmation dialog

### 📊 Session History
- Every timer run is logged automatically as a history entry
- Grouped by date with smart labels: **Today**, **Yesterday**, or full date
- Separated from saved templates — history shows real runs only

### 🎨 UI & UX
- Minimal, clean design with custom typography (`interfont`)
- Material Design 3 components throughout
- Breathing animation on the dashboard circle
- Real-time input validation with inline error messages

---

## 🏗 Project Structure

```
com.example.interval/
│
├── 🔐 Auth
│   ├── login_screen.java          # Login with SHA-256 verification + Snackbar errors
│   └── Register_Screen.java       # Registration with real-time validation & password strength
│
├── 🏠 Dashboard
│   └── Dashboard_Screen.java      # Home screen, loads latest session, starts timer
│
├── ⏱ Timer
│   ├── Study_Running.java         # Study countdown, CW progress, logs run to DB
│   └── Break_Running.java         # Break countdown, CCW progress, wakes screen on finish
│
├── 📋 Sessions
│   ├── Add_Session.java           # Create new session templates
│   ├── Edit_Session.java          # Rename existing sessions
│   └── Session_list.java          # RecyclerView history with date headers
│
├── 🗄 Data
│   ├── DatabaseHelper.java        # SQLite helper — all queries centralised
│   ├── SessionModel.java          # Model for dashboard template loading
│   └── SessionListModel.java      # Model for history list (includes id + date)
│
├── 🔧 Utilities
│   ├── TimerUtils.java            # Haptic feedback utility (tap + finish patterns)
│   └── warning_dialog.java        # Reusable custom confirmation dialog
│
└── 🎨 Adapter
    └── SessionAdapter.java        # RecyclerView adapter with header + item view types
```

---

## 🗄 Database Schema

### `users` table
| Column | Type | Description |
|---|---|---|
| `id` | INTEGER PK | Auto-incremented user ID |
| `username` | TEXT UNIQUE | Unique username |
| `password_hash` | TEXT | SHA-256 hashed password |

### `sessions` table
| Column | Type | Description |
|---|---|---|
| `id` | INTEGER PK | Auto-incremented session ID |
| `user_id` | INTEGER FK | References `users(id)` — cascades on delete |
| `title` | TEXT | Session name |
| `focus_time` | INTEGER | Study duration in minutes |
| `rest_time` | INTEGER | Break duration in minutes |
| `date` | TEXT | Date of record (`yyyy-MM-dd`) |
| `type` | TEXT | `'template'` (saved config) or `'run'` (actual usage) |

> Foreign key enforcement is enabled via `PRAGMA foreign_keys=ON`. Users only ever see their own data — every query filters by `user_id`.

---

## 🔄 App Flow

```
Login / Register
      ↓
  Dashboard  ←──────────────────────────┐
      ↓                                 │
  [Start Session]                       │
      ↓                                 │
 Study Timer  ──(finish/skip)──►  Break Timer
      │                                 │
      └── logs 'run' to DB ────────────►┘
                                        │
                                   [Skip / Finish]
                                        │
                                   Dashboard (onResume refreshes)
```

---

## 🛠 Tech Stack

| Technology | Usage |
|---|---|
| **Java** | Primary language |
| **Android SDK** | Target API 33+, Min API 26 |
| **SQLite** | Local relational database via `SQLiteOpenHelper` |
| **Material Design 3** | UI components — TextInputLayout, MaterialButton, CircularProgressIndicator, Snackbar |
| **CountDownTimer** | Timer engine for both study and break phases |
| **SHA-256 (MessageDigest)** | Password hashing |
| **SharedPreferences** | Session/login state persistence |
| **RecyclerView** | Session history list with multiple view types |
| **VibrationEffect API** | Haptic feedback (supports API 26+) |
| **WindowManager Flags** | Battery-aware screen wake on timer events |

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android device or emulator running API 26+
- JDK 11+

### Installation

1. Clone the repository:
```bash
git clone https://github.com/NobodydeBunny/Study_Break_Timer.git
```

2. Open the project in **Android Studio**:
```
File → Open → Select the /Interval folder
```

3. Let Gradle sync complete.

4. Run on your device or emulator:
```
Run → Run 'app'  (Shift + F10)
```

> No API keys or external services required — everything runs locally on-device.

---

## 📱 How to Use

1. **Register** a new account on the registration screen
2. **Login** with your credentials
3. On the **Dashboard**, tap **Add** to create a custom study session
4. Tap **Start Session** to begin the study timer
5. When the study timer ends, the **Break Timer** starts automatically
6. After the break, you're returned to the dashboard
7. View all your past runs under **Sessions** (history tab)
8. **Edit** session names or **Delete** entries from history

---

## 👥 Team Members

| Name | Reg No |Index|
|---|---|---|
| **Sandakelum Kumarasiri** | *ICT/2022/136* | *5737* |
| **Raashidh Musaj** | *ICT/2022/135* | *5736* |
| **H.A.T.M.Hettiarachchi** | *ICT/2022/137* | *5738* |


---

## 📁 Repository Structure

```
Study_Break_Timer/
├── Interval/          # Android Studio project source
│   ├── app/
│   │   ├── src/main/java/com/example/interval/
│   │   └── src/main/res/
│   └── build.gradle
├── docs/              # Supporting documentation 
├── Screenshots/              # Screenshots
└── README.md
```

---

## 📸 Screenshots


| Login | Dashboard | Study Timer | Break Timer | Session History |
|:---:|:---:|:---:|:---:|:---:|
| <img src="/Screenshots/LoginScreen.png" width="160"> | <img src="/Screenshots/DashboardScreen.png" width="160"> | <img src="/Screenshots/StudyRunning.png" width="160"> | <img src="/Screenshots/BreakRunning.png" width="160"> | <img src="/Screenshots/SessionList.png" width="160"> |

---

<div align="center">

Made with lot of ☕ and focused study sessions

**Interval** — *Work hard. Rest smart.*

</div>
