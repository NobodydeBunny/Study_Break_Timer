package com.example.interval;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.CircularProgressIndicator;

public class Study_Running extends AppCompatActivity {

    CircularProgressIndicator circularProgress;
    TextView timerText, sessionNameText, focusingLabel;
    ImageButton btnPauseStart, btnStop, btnSkip;

    CountDownTimer countDownTimer;
    boolean isPaused = false;
    long timeLeftMillis;
    long totalMillis;

    int studyTime, breakTime;
    String sessionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_study_running);


        studyTime   = getIntent().getIntExtra("studyTime", 25);
        breakTime   = getIntent().getIntExtra("breakTime", 5);
        sessionName = getIntent().getStringExtra("sessionName");
        circularProgress  = findViewById(R.id.studycircularProgress);
        timerText         = findViewById(R.id.studytimerText);
        sessionNameText   = findViewById(R.id.studysessionname);
        focusingLabel     = findViewById(R.id.studyfocusing);
        btnPauseStart     = findViewById(R.id.btnPauseStart);
        btnStop           = findViewById(R.id.btnStop);
        btnSkip           = findViewById(R.id.btnSkip);
        sessionNameText.setText(sessionName != null ? sessionName : "Study Session");
        totalMillis    = studyTime * 60 * 1000L;
        timeLeftMillis = totalMillis;

        circularProgress.setMax(100);
        circularProgress.setProgress(0);

        updateTimerDisplay(timeLeftMillis);
        startTimer();

        btnPauseStart.setOnClickListener(v -> {
            Timerutils.hapticTap(this);
            if (isPaused) {
                resumeTimer();
            } else {
                pauseTimer();
            }
        });

        btnStop.setOnClickListener(v -> {
            Timerutils.hapticTap(this);
            if (countDownTimer != null) countDownTimer.cancel();
            finish();
        });

        btnSkip.setOnClickListener(v -> {
            Timerutils.hapticTap(this);
            if (countDownTimer != null) countDownTimer.cancel();
            goToBreak();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillis = millisUntilFinished;
                updateTimerDisplay(millisUntilFinished);
                updateProgress(millisUntilFinished);
            }
            @Override
            public void onFinish() {
                circularProgress.setProgress(100);
                timerText.setText("00:00");
                Timerutils.hapticFinish(Study_Running.this);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                );
                goToBreak();
            }
        }.start();

        isPaused = false;
        btnPauseStart.setImageResource(R.drawable.ic_pause);
    }

    private void pauseTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        isPaused = true;
        btnPauseStart.setImageResource(R.drawable.ic_play);
        focusingLabel.setText("PAUSED");
        int color = ContextCompat.getColor(this, R.color.blueGrey);
        focusingLabel.setTextColor(color);
    }

    private void resumeTimer() {
        focusingLabel.setText("FOCUSING");
        int color = ContextCompat.getColor(this, R.color.primary);
        focusingLabel.setTextColor(color);
        startTimer();
    }

    private void updateTimerDisplay(long millis) {
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateProgress(long millisLeft) {
        int elapsed = (int) (totalMillis - millisLeft);
        int progress = (int) ((elapsed / (float) totalMillis) * 100);
        circularProgress.setProgress(progress);
    }

    private void goToBreak() {
        Intent intent = new Intent(Study_Running.this, Break_Timer_Screen.class);
        intent.putExtra("breakTime", breakTime);
        intent.putExtra("sessionName", sessionName);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
