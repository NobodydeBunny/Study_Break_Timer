package com.example.interval;

import android.content.Intent;
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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class login_screen extends AppCompatActivity {

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

        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            // Clear previous errors
            usernameLayout.setError(null);
            passwordLayout.setError(null);

            // Use AuthHelper to validate and attempt login
            String result = AuthHelper.login(email, password);

            if (result.equals("Login successful!")) {
                // Login passed — go to Dashboard
                startActivity(new Intent(login_screen.this, Dashboard_Screen.class));
                finish();
            } else {
                // Show the error message from AuthHelper
                Toast.makeText(login_screen.this, result, Toast.LENGTH_SHORT).show();

                // Highlight the right field based on the error
                if (result.contains("Email")) {
                    usernameLayout.setError(result);
                } else if (result.contains("Password")) {
                    passwordLayout.setError(result);
                }
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
    // checkLoginCredentials() replaced by AuthHelper.login()
    // See AuthHelper.java for the login logic.
}