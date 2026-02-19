package com.example.interval;

/**
 * User.java
 *
 * Simple User model for the Study Break Timer app.
 * This class follows the POJO (Plain Old Java Object) pattern to store user data.
 */
public class User {
    private String userId;
    private String email;
    private String password;

    // Constructor to initialize the User object
    public User(String userId, String email, String password) {
        this.userId = userId;
        this.email = email;
        this.password = password;
    }

    // Getter and Setter for userId
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
