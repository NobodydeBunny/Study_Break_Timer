package com.example.interval;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Session_list extends AppCompatActivity {

    RecyclerView recyclerView;
    SessionAdapter adapter;
    List<Object> items = new ArrayList<>();
    DatabaseHelper dbHelper;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_session_list);

        recyclerView = findViewById(R.id.recyclerSessions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        LinearLayout navTimer = findViewById(R.id.navTimer);
        navTimer.setOnClickListener(v -> {
            startActivity(new Intent(this, Dashboard_Screen.class));
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSessions();
    }

    private void loadSessions() {
        items.clear();

        Cursor cursor = dbHelper.getSessionHistory(userId);
        String lastDate = "";

        while (cursor.moveToNext()) {
            int    id        = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title     = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            int    focusTime = cursor.getInt(cursor.getColumnIndexOrThrow("focus_time"));
            int    restTime  = cursor.getInt(cursor.getColumnIndexOrThrow("rest_time"));
            String date      = cursor.getString(cursor.getColumnIndexOrThrow("date"));

            if (!date.equals(lastDate)) {
                items.add(formatHeader(date));
                lastDate = date;
            }

            items.add(new SessionListModel(id, title, focusTime, restTime, date));
        }
        cursor.close();

        adapter = new SessionAdapter(this, items, (sessionId, position) -> {
            warning_dialog.show(
                    this,
                    "Delete Session",
                    "Remove this session from history?",
                    "Delete",
                    "Cancel",
                    R.drawable.ic_delete,
                    () -> {
                        dbHelper.deleteSession(sessionId);
                        adapter.removeAt(position);
                    }
            );
        });

        recyclerView.setAdapter(adapter);
    }

    // Turns "2025-06-01" → "Today", "Yesterday", or "Jun 1, 2025"
    private String formatHeader(String dateStr) {
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
            java.util.Date date    = sdf.parse(dateStr);
            java.util.Date today   = new java.util.Date();

            java.text.SimpleDateFormat dayOnly = new java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.getDefault());

            if (dayOnly.format(date).equals(dayOnly.format(today))) return "Today";

            java.util.Calendar yesterday = java.util.Calendar.getInstance();
            yesterday.add(java.util.Calendar.DAY_OF_YEAR, -1);
            if (dayOnly.format(date).equals(dayOnly.format(yesterday.getTime()))) return "Yesterday";

            return new java.text.SimpleDateFormat("MMM d, yyyy", java.util.Locale.getDefault()).format(date);
        } catch (Exception e) {
            return dateStr;
        }
    }
}