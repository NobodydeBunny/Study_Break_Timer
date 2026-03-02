package com.example.interval;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class Dashboard_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard_screen);

        ImageButton logoutbtn = findViewById(R.id.logoutBtn);
        TextView Welcometxt = findViewById(R.id.welcometxt);


        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        int userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            startActivity(new Intent(this, login_screen.class));
            finish();
        }

        String username = getUsername(userId);
        Welcometxt.setText("Welcome, "+username);


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


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void logoutUser() {
        SharedPreferences prefs = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();   // removes user_id and login state
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
}