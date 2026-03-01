package com.example.interval;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;

public class login_screen extends AppCompatActivity {

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_screen);

        Button loginButton = findViewById(R.id.loginBtn);
        TextView createAccount = findViewById(R.id.createAccount);
        TextInputLayout usernameLayout = findViewById(R.id.usernameLayout);
        TextInputLayout passwordLayout = findViewById(R.id.passwordLayout);
        TextInputEditText usernameEditText = findViewById(R.id.loginusername);
        TextInputEditText passwordEditText = findViewById(R.id.loginpassword);
        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            boolean isValid = true;

            if (username.isEmpty()) {
                usernameLayout.setError("Username cannot be empty");
                isValid = false;
            }else if (username.contains(" ")) {
                usernameLayout.setError("Username cannot contain spaces");
                isValid = false;
            }else {
                usernameLayout.setError(null);
            }

            if (password.isEmpty()) {
                passwordLayout.setError("Password cannot be empty");
                isValid = false;
            } else {
                passwordLayout.setError(null);
            }

            if(isValid){
                checkLoginCredentials(username, password);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_screen.this, Register_Screen.class);

                startActivity(intent);
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void checkLoginCredentials(String username, String password) {
        String hashedPassword = hashPassword(password);

        if (hashedPassword == null) {
            showSnackbar("Something went wrong. Please try again.");
            return;
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password_hash = ?",
                new String[]{username, hashedPassword}
        );

        if (cursor.getCount() > 0) {
            //go to dashboard
            cursor.close();
            startActivity(new Intent(this, Dashboard_Screen.class));
            finish();
        } else {
            cursor.close();
            showSnackbar("Incorrect username or password");
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(R.id.main), message, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getColor(R.color.primary))
                .setTextColor(getColor(R.color.background))
                .show();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}