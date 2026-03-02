package com.example.interval;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        progressBar = findViewById(R.id.loading_bar);

        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                int progress = i;
                runOnUiThread(() -> progressBar.setProgress(progress));

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(() -> {
                if (userId != -1) {
                    startActivity(new Intent(SplashScreen.this, Dashboard_Screen.class));
                } else {
                    startActivity(new Intent(SplashScreen.this, login_screen.class));
                }
                finish();
            });

        }).start();
    }
}
