package com.example.interval;

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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Register_Screen extends AppCompatActivity {

    TextInputEditText username, password, confirmPassword;
    Button registerBtn;
    TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);


        username = findViewById(R.id.usernamereg);
        password = findViewById(R.id.passwordreg);
        confirmPassword = findViewById(R.id.confpasswordreg);
        registerBtn = findViewById(R.id.regbtn);
        backToLogin = findViewById(R.id.btnBackToLogin);

        backToLogin.setOnClickListener(v -> finish());

        registerBtn.setOnClickListener(v -> {
            String email = username.getText().toString().trim();
            String p = password.getText().toString();
            String cp = confirmPassword.getText().toString();

            // Use AuthHelper to validate and register
            String result = AuthHelper.register(email, p, cp);

            // Show the result message (success or error)
            Toast.makeText(Register_Screen.this, result, Toast.LENGTH_SHORT).show();

            // If registration was successful, go back to Login screen
            if (result.equals("Registration successful!")) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}