package com.example.interval;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class Break_Timer_Screen extends AppCompatActivity {

    CircularProgressIndicator circularProgress;
    TextView timerText, sessionNameText;
    MaterialButton btnSkipBreak;

    CountDownTimer countDownTimer;
    long timeLeftMillis;
    long totalMillis;

    int breakTime;
    String sessionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_break_timer_screen);

        breakTime   = getIntent().getIntExtra("breakTime", 5);
        sessionName = getIntent().getStringExtra("sessionName");

        circularProgress = findViewById(R.id.breakcircularProgress);
        timerText        = findViewById(R.id.breaktimerText);
        sessionNameText  = findViewById(R.id.breaksessionname);
        btnSkipBreak     = findViewById(R.id.btnSkipBreak);

        sessionNameText.setText(sessionName != null ? sessionName : "Break Session");
        totalMillis    = breakTime * 60 * 1000L;
        timeLeftMillis = totalMillis;

        circularProgress.setMax(100);
        circularProgress.setProgress(100);

        updateTimerDisplay(timeLeftMillis);
        Timerutils.hapticTap(this);
        startTimer();

        btnSkipBreak.setOnClickListener(v -> {
            Timerutils.hapticTap(this);
            if (countDownTimer != null) countDownTimer.cancel();
            goToDashboard();
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
                circularProgress.setProgress(0);
                timerText.setText("00:00");
                Timerutils.hapticFinish(Break_Timer_Screen.this);
                getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                );
                goToDashboard();
            }
        }.start();
    }


    private void updateTimerDisplay(long millis) {
        int minutes = (int) (millis / 1000) / 60;
        int seconds = (int) (millis / 1000) % 60;
        timerText.setText(String.format("%02d:%02d", minutes, seconds));
    }

    private void updateProgress(long millisLeft) {
        int progress = (int) ((millisLeft / (float) totalMillis) * 100);
        circularProgress.setProgress(progress);
    }

    private void goToDashboard() {
        Intent intent = new Intent(Break_Timer_Screen.this, Dashboard_Screen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}