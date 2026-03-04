package com.example.interval;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * AuthHelper.java
 *
 * Simple authentication helper for the Study Break Timer app.
 * Academic project - connected to SQLite database for user storage.
 *
 * Contains two functions:
 *   - login(context, email, password)    → returns a success or error message
 *   - register(context, email, password, confirmPassword) → returns a success or error message
 *
 * Rules:
 *   - Email must not be empty
 *   - Password must be at least 6 characters
 */
public class AuthHelper {

    // ---------------------------------------------------------------
    // LOGIN FUNCTION
    // ---------------------------------------------------------------

    /**
     * Validates login credentials against the SQLite database.
     *
     * @param context  the application context to access DatabaseHelper
     * @param email    the user's email address
     * @param password the user's password
     * @return "Login successful!" on success, or an error message string on failure
     */
    public static String login(Context context, String email, String password) {

        // Check if email is empty
        if (email == null || email.trim().isEmpty()) {
            return "Error: Email cannot be empty.";
        }

        // Check if password is empty
        if (password == null || password.isEmpty()) {
            return "Error: Password cannot be empty.";
        }

        // Hash the input password to compare it with the hashed password in the database
        String hashedInputPassword = PasswordHashUtil.hashPassword(password);
        if (hashedInputPassword == null) {
            return "Error: Hashing failed.";
        }

        // Initialize DatabaseHelper to interact with SQLite
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Query the 'users' table for a match with email and hashed password
        String selection = DatabaseHelper.COLUMN_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, hashedInputPassword};

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_USERS,   // Table name
                null,                         // All columns
                selection,                    // Filter: email and password match
                selectionArgs,                // Filter values
                null,                         // No group by
                null,                         // No having
                null                          // No limit
        );

        boolean loginSuccess = cursor.getCount() > 0;
        cursor.close();
        db.close();

        if (loginSuccess) {
            return "Login successful!";
        } else {
            return "Error: Invalid email or password.";
        }
    }

    // ---------------------------------------------------------------
    // REGISTER FUNCTION
    // ---------------------------------------------------------------

    /**
     * Reigsters a new user in the SQLite database.
     *
     * @param context         the application context to access DatabaseHelper
     * @param email           the user's email address
     * @param password        the user's chosen password
     * @param confirmPassword the password confirmation
     * @return "Registration successful!" on success, or an error message string on failure
     */
    public static String register(Context context, String email, String password, String confirmPassword) {

        // Check if email is empty
        if (email == null || email.trim().isEmpty()) {
            return "Error: Email cannot be empty.";
        }

        // Check if password is empty
        if (password == null || password.isEmpty()) {
            return "Error: Password cannot be empty.";
        }

        // Check minimum password length (6 characters)
        if (password.length() < 6) {
            return "Error: Password must be at least 6 characters.";
        }

        // Check if both passwords match
        if (!password.equals(confirmPassword)) {
            return "Error: Passwords do not match.";
        }

        // Initialize DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if the email is already registered
        String checkEmailQuery = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE " + DatabaseHelper.COLUMN_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(checkEmailQuery, new String[]{email});
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return "Error: Email is already registered.";
        }
        cursor.close();

        // Hash the password before saving for security
        String hashedPassword = PasswordHashUtil.hashPassword(password);
        if (hashedPassword == null) {
            db.close();
            return "Error: Registration failed due to encryption error.";
        }

        // Prepare data for insertion
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, hashedPassword);

        // Insert into the database
        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        db.close();

        if (result == -1) {
            return "Error: Database insertion failed.";
        } else {
            return "Registration successful!";
        }
    }
}

