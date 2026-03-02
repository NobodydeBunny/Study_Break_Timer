package com.example.interval;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

public class Edit_Session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_session);

        EditText etSessionName   = findViewById(R.id.etSessionName);
        TextView tvStudyDuration = findViewById(R.id.tvStudyDuration);
        TextView tvBreakDuration = findViewById(R.id.tvBreakDuration);
        MaterialButton btnSave   = findViewById(R.id.btnSaveSession);
        MaterialButton btnCancel = findViewById(R.id.btnCancel);

        int    sessionId = getIntent().getIntExtra("session_id", -1);
        String title     = getIntent().getStringExtra("title");
        int    focusTime = getIntent().getIntExtra("focus_time", 25);
        int    restTime  = getIntent().getIntExtra("rest_time", 5);

        etSessionName.setText(title);
        tvStudyDuration.setText(String.valueOf(focusTime));
        tvBreakDuration.setText(String.valueOf(restTime));

        tvStudyDuration.setEnabled(false);
        tvBreakDuration.setEnabled(false);
        tvStudyDuration.setAlpha(0.5f); // visually indicate non-editable
        tvBreakDuration.setAlpha(0.5f);

        btnSave.setOnClickListener(v -> {
            String newName = etSessionName.getText().toString().trim();

            if (newName.isEmpty()) {
                etSessionName.setError("Session name cannot be empty");
                etSessionName.requestFocus();
                return;
            }

            if (sessionId == -1) {
                finish();
                return;
            }

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.updateSessionTitle(sessionId, newName);
            finish();
        });

        btnCancel.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}