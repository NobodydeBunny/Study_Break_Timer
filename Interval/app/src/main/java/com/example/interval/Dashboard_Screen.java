package com.example.interval;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class Dashboard_Screen extends AppCompatActivity {

    private TextView tvStudyTime, tvBreakTime, tvSessionName;

    private int studyMinutes = 1;
    private int breakMinutes = 5;
    private String sessionName = "Study Session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_screen);
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            startActivity(new Intent(this, login_screen.class));
            finish();
        }

        String username = getUsername(userId);
        ImageButton logoutbtn = findViewById(R.id.logoutBtn);
        TextView Welcometxt = findViewById(R.id.welcometxt);
        tvStudyTime = findViewById(R.id.txtStudyTime);
        tvBreakTime = findViewById(R.id.txtBreakTime);
        tvSessionName = findViewById(R.id.sessionname);
        MaterialButton btnReset = findViewById(R.id.btnReset);
        MaterialButton btnStart = findViewById(R.id.btnStartSession);
        MaterialButton btnAdd = findViewById(R.id.btnAdd);
        MaterialButton btnHistory = findViewById(R.id.btnHistory);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SessionModel session = dbHelper.getLatestSession(userId);

        Welcometxt.setText("WELCOME, "+username);

        if (session != null) {
            studyMinutes = session.getFocusTime();
            breakMinutes = session.getRestTime();
            sessionName = session.getTitle();
        }
        updateUI();
        animation();

        logoutbtn.setOnClickListener(v ->{
            warning_dialog.show(
                    this,
                    "Logout",
                    "Are you sure you want to log out of your session?",
                    "Logout",
                    "Cancel",
                    R.drawable.ic_logout,
                    () -> {
                        logoutUser();
                    }
            );
        });

        btnReset.setOnClickListener(v -> {
            studyMinutes = 25;
            breakMinutes = 5;
            sessionName = "Study Session";

            updateUI();
        });

        btnStart.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard_Screen.this, Study_Running.class);

            intent.putExtra("studyTime", studyMinutes);
            intent.putExtra("breakTime", breakMinutes);
            intent.putExtra("sessionName", sessionName);

            startActivity(intent);
        });

        btnAdd.setOnClickListener(v -> {
            startActivity(new Intent(this, Add_Session.class));
        });

        btnHistory.setOnClickListener(v -> {
            startActivity(new Intent(this, Session_list.class));
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    // Functions ----------------------------------------------------------------------->
    private void updateUI() {

        tvStudyTime.setText(studyMinutes + " min");
        tvBreakTime.setText(breakMinutes + " min");
        tvSessionName.setText(sessionName);
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(Dashboard_Screen.this, login_screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private String getUsername(int userId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT username FROM users WHERE id = ?",
                new String[]{String.valueOf(userId)}
        );

        String username = "";

        if (cursor.moveToFirst()) {
            username = cursor.getString(0);
        }

        cursor.close();
        return username;
    }

    private void animation() {

        CircularProgressIndicator circularProgress = findViewById(R.id.circularProgress);

        ObjectAnimator breathing = ObjectAnimator.ofPropertyValuesHolder(
                circularProgress,
                PropertyValuesHolder.ofFloat(View.SCALE_X, 0.9f, 1.05f),
                PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.9f, 1.05f)
        );
        breathing.setDuration(5000); // 5 seconds per breath
        breathing.setRepeatMode(ValueAnimator.REVERSE);
        breathing.setRepeatCount(ValueAnimator.INFINITE);
        breathing.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator ensoGlow = ObjectAnimator.ofFloat(circularProgress, View.ALPHA, 0.4f, 0.8f);
        ensoGlow.setDuration(5000);
        ensoGlow.setRepeatMode(ValueAnimator.REVERSE);
        ensoGlow.setRepeatCount(ValueAnimator.INFINITE);
        ensoGlow.setInterpolator(new AccelerateDecelerateInterpolator());

        ValueAnimator mist = ValueAnimator.ofFloat(0.0f, 0.15f);
        mist.setDuration(2000);
        mist.setRepeatMode(ValueAnimator.REVERSE);
        mist.setRepeatCount(ValueAnimator.INFINITE);
        mist.addUpdateListener(animation -> {
            float offset = (float) animation.getAnimatedValue();
            circularProgress.setAlpha(ensoGlow.getAnimatedFraction() * 0.4f + 0.4f + offset);
        });

        AnimatorSet zenSet = new AnimatorSet();
        zenSet.playTogether(breathing, ensoGlow);
        zenSet.start();
        mist.start();

    }
}