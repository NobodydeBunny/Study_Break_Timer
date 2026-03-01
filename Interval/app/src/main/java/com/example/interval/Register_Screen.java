package com.example.interval;

import android.content.ContentValues;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;

public class Register_Screen extends AppCompatActivity {

    TextInputEditText username, password, confirmPassword;
    TextInputLayout usernameLayout, passwordLayout, confirmPasswordLayout;
    Button registerBtn;
    TextView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_screen);

        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confpasswordLayout);


        username = findViewById(R.id.usernamereg);
        password = findViewById(R.id.passwordreg);
        confirmPassword = findViewById(R.id.confpasswordreg);
        registerBtn = findViewById(R.id.regbtn);
        backToLogin = findViewById(R.id.btnBackToLogin);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        backToLogin.setOnClickListener(v -> finish());

        username.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                validateUsername(s.toString().trim());
            }
        });
        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) { // user left the field
                String u = username.getText().toString().trim();
                if (validateUsername(u)) { // only check DB if format is valid
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery(
                            "SELECT 1 FROM users WHERE username = ?", new String[]{u}
                    );
                    if (cursor.getCount() > 0) {
                        usernameLayout.setError("Username already taken");
                    }
                    cursor.close();
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                validatePassword(s.toString());
                // Re-check confirm field if user edits password after filling confirm
                String cp = confirmPassword.getText() != null ? confirmPassword.getText().toString() : "";
                if (!cp.isEmpty()) validateConfirmPassword(s.toString(), cp);
            }
        });

        // ── Real-time: Confirm Password ──────────────────────────────────────
        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String p = password.getText() != null ? password.getText().toString() : "";
                validateConfirmPassword(p, s.toString());
            }
        });

        registerBtn.setOnClickListener(v -> {
            String u = username.getText().toString().trim();
            String p = password.getText().toString();
            String cp = confirmPassword.getText().toString();

            boolean usernameOk = validateUsername(u) && usernameLayout.getError() == null;
            boolean passwordOk = validatePassword(p);
            boolean confirmOk  = validateConfirmPassword(p, cp);

            if (!usernameOk || !passwordOk || !confirmOk) return;

            String hashedPassword = hashPassword(p);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("username", u);
            values.put("password_hash", hashedPassword);

            db.insert("users", null, values);
            Toast.makeText(this, "Account registered successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private boolean validateUsername(String u) {
        if (u.isEmpty()) {
            usernameLayout.setError("Username is required");
            return false;
        } else if (u.length() < 3) {
            usernameLayout.setError("At least 3 characters required");
            return false;
        } else if (!u.matches("^[a-zA-Z0-9._]+$")) {
            usernameLayout.setError("Only letters, numbers, . and _ allowed");
            return false;
        } else {
            usernameLayout.setError(null);
            usernameLayout.setHelperText("✓ Looks good");
            return true;
        }
    }
    private boolean validatePassword(String p) {
        if (p.isEmpty()) {
            passwordLayout.setError("Password is required");
            passwordLayout.setHelperText(null);
            return false;
        }

        int strength = getPasswordStrength(p);

        if (p.length() < 6) {
            passwordLayout.setError("At least 6 characters required");
            passwordLayout.setHelperText(null);
            return false;
        } else if (strength == 1) {
            passwordLayout.setError(null);
            passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            passwordLayout.setHelperText("⚠ Weak — add Text and numbers and symbols");
            return true;
        } else if (strength == 2) {
            passwordLayout.setError(null);
            passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#FFA500")));
            passwordLayout.setHelperText("⚠ Fair");
            return true;
        } else {
            passwordLayout.setError(null);
            passwordLayout.setHelperText("✓ Strong password");
            passwordLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#00CC00")));
            return true;
        }
    }

    private boolean validateConfirmPassword(String p, String cp) {
        if (cp.isEmpty()) {
            confirmPasswordLayout.setError("Please confirm your password");
            return false;
        } else if (!p.equals(cp)) {
            confirmPasswordLayout.setError("Passwords do not match");
            return false;
        } else {
            confirmPasswordLayout.setError(null);
            confirmPasswordLayout.setHelperText("✓ Passwords match");
            confirmPasswordLayout.setHelperTextColor(ColorStateList.valueOf(Color.parseColor("#00CC00")));
            return true;
        }
    }

    /**
     * Returns password strength:
     *  1 = Weak  (one of input types)
     *  2 = Fair  (two of input types)
     *  3 = Strong (letters + digits + special char)
     */
    private int getPasswordStrength(String p) {
        boolean hasLetter  = p.matches(".*[a-zA-Z].*");
        boolean hasDigit   = p.matches(".*\\d.*");
        boolean hasSpecial = p.matches(".*[^a-zA-Z0-9].*");

        if (hasLetter && hasDigit && hasSpecial) return 3;
        if (hasLetter && hasDigit) return 2;
        if (hasLetter && hasSpecial) return 2;
        if (hasDigit && hasSpecial) return 2;
        return 1;
    }
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}