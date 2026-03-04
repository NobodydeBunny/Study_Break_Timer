package com.example.interval;

/**
 * AuthHelper.java
 *
 * Simple authentication helper for the Study Break Timer app.
 * Academic project - no database connection, no external libraries.
 *
 * Contains two functions:
 *   - login(email, password)    → returns a success or error message
 *   - register(email, password) → returns a success or error message
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
     * Validates login credentials.
     *
     * @param email    the user's email address
     * @param password the user's password
     * @return "Login successful!" on success, or an error message string on failure
     */
    public static String login(String email, String password) {

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

        // Hash the input password to compare it with the hashed password in the database
        String hashedInputPassword = PasswordHashUtil.hashPassword(password);

        // TODO: Replace this mock check with a real database call later
        // Note: For demonstration, the mock test account password hash is shown here.
        // Plain text "123456" = SHA-256 hash "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92"
        String mockHashedPassword = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";

        if (email.equals("test@email.com") && hashedInputPassword != null && hashedInputPassword.equals(mockHashedPassword)) {
            return "Login successful!";
        } else {
            return "Error: Invalid email or password.";
        }
    }

    // ---------------------------------------------------------------
    // REGISTER FUNCTION
    // ---------------------------------------------------------------

    /**
     * Validates registration details.
     *
     * @param email           the user's email address
     * @param password        the user's chosen password
     * @param confirmPassword the password typed a second time for confirmation
     * @return "Registration successful!" on success, or an error message string on failure
     */
    public static String register(String email, String password, String confirmPassword) {

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

        // Hash the password before it should be saved to the database.
        // Hashing ensures that even if the database is compromised, passwords are never stored in plain text.
        String hashedPassword = PasswordHashUtil.hashPassword(password);
        
        // TODO: Save the new user (email and hashedPassword) to the database here later.
        return "Registration successful!";
    }
}
