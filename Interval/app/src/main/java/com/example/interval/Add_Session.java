package com.example.interval;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Add_Session extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_session);

        TextView tvStudyDuration = findViewById(R.id.tvStudyDuration);
        TextView tvBreakDuration = findViewById(R.id.tvBreakDuration);

        tvStudyDuration.setOnClickListener(v -> showMinutePicker(tvStudyDuration));
        tvBreakDuration.setOnClickListener(v -> showMinutePicker(tvStudyDuration));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showMinutePicker(TextView targetView) {

        View view = getLayoutInflater().inflate(R.layout.dialog_minute_picker, null);
        NumberPicker picker = view.findViewById(R.id.minutePicker);

        picker.setMinValue(1);
        picker.setMaxValue(60);
        picker.setValue(25);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .setPositiveButton("OK", (d, which) -> {
                    int value = picker.getValue();
                    targetView.setText(String.valueOf(value));
                })
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show();
    }
}