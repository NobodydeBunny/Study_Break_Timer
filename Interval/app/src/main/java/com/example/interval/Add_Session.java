package com.example.interval;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import java.util.Date;
import java.util.Locale;

public class Add_Session extends AppCompatActivity {

    TextView tvStudyDuration, tvBreakDuration;
    EditText etSessionName;
    MaterialButton btnSaveSession, btnCancel;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_session);

        tvStudyDuration = findViewById(R.id.tvStudyDuration);
        tvBreakDuration = findViewById(R.id.tvBreakDuration);
        etSessionName   = findViewById(R.id.etSessionName);
        btnSaveSession  = findViewById(R.id.btnSaveSession);
        btnCancel       = findViewById(R.id.btnCancel);
        dbHelper        = new DatabaseHelper(this);

        tvStudyDuration.setOnClickListener(v ->
                showMinutePicker(tvStudyDuration, 1, 60, 25));

        tvBreakDuration.setOnClickListener(v ->
                showMinutePicker(tvBreakDuration, 1, 30, 5));

        btnSaveSession.setOnClickListener(v -> {
            Timerutils.hapticTap(this);
            saveSession();
        });

        btnCancel.setOnClickListener(v ->{
                Timerutils.hapticTap(this);
                finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showMinutePicker(TextView targetView, int min, int max, int defaultVal) {
        View view = getLayoutInflater().inflate(R.layout.dialog_minute_picker, null);
        NumberPicker picker = view.findViewById(R.id.minutePicker);

        picker.setMinValue(min);
        picker.setMaxValue(max);

        try {
            picker.setValue(Integer.parseInt(targetView.getText().toString()));
        } catch (NumberFormatException e) {
            picker.setValue(defaultVal);
        }

        new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", (d, which) ->
                        targetView.setText(String.valueOf(picker.getValue())))
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    //DB
    private void saveSession() {
        String name      = etSessionName.getText().toString().trim();
        String studyStr  = tvStudyDuration.getText().toString().trim();
        String breakStr  = tvBreakDuration.getText().toString().trim();

        if (name.isEmpty()) {
            etSessionName.setText("Study Session");
            return;
        }

        int studyMins, breakMins;
        try {
            studyMins = Integer.parseInt(studyStr);
            breakMins = Integer.parseInt(breakStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid duration values", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());

        dbHelper.insertSession(userId, name, studyMins, breakMins, date, "template");

        Toast.makeText(this, "Session saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}