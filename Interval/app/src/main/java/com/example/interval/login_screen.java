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
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString();

            boolean isValid = true;

            // Username validation
            if (username.isEmpty()) {
                usernameLayout.setError("Username cannot be empty");
                isValid = false;
            }else if (username.contains(" ")) {
                usernameLayout.setError("Username cannot contain spaces");
                isValid = false;
            }else {
                usernameLayout.setError(null);
            }

            // Password validation
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
    private void checkLoginCredentials(String username, String password){
        // MOCK database for testing purposes

        if(username.equals("testuser") && password.equals("123456")){

            startActivity(new Intent(this, Dashboard_Screen.class));
            finish();
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}