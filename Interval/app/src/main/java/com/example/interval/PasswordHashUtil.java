package com.example.interval;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * PasswordHashUtil.java
 *
 * Simple utility to hash passwords using SHA-256.
 * For academic purpose - demonstrates basic hashing logic.
 */
public class PasswordHashUtil {

    /**
     * Hashes a plain text password using the SHA-256 algorithm.
     *
     * @param password Plain text password to hash
     * @return Hashed password as a hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Perform the hashing on the password bytes
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // Convert byte array into a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                // Convert each byte to a 2-digit hex string
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            // This should not happen since SHA-256 is a standard algorithm
            e.printStackTrace();
            return null;
        }
    }
}
