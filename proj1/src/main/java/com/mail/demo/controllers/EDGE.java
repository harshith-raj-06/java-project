package com.mail.demo.controllers;

public class EDGE {

    // --- THE MASTER METHOD ---
    // This combines all the separate functions below.
    // It returns a String: either the Error message or "Success".
    public String validateFullPassword(String password, String email) {

        // 1. Check Length
        if (!isLengthValid(password)) {
            return "Error: Password size is less than 8 characters.";
        }

        // 2. Check for '@'
        if (!hasSpecialChar(email)) {
            return "Error: Password must contain '@'.";
        }

        // 3. Check for Digit
        if (!hasDigit(password)) {
            return "Error: Password must contain at least one number.";
        }

        // 4. Check for Uppercase
        if (!hasUpperCase(password)) {
            return "Error: Password must contain at least one uppercase letter.";
        }

        // 5. Check if Username is hidden inside Password (Subarray/Substring match)
        if (containsUsername(password, email)) {
            return "Error: Password cannot contain your username/email.";
        }

        // If all checks pass:
        return "logging in.....";
    }

    // --- SEPARATE HELPER FUNCTIONS ---

    // Rule 1: Check Length
    private boolean isLengthValid(String password) {
        return password != null && password.length() >= 8;
    }

    // Rule 2: Check for '@' (Special Char)
    private boolean hasSpecialChar(String email) {
        return email.contains("@");
    }

    // Rule 3: Check for Number
    private boolean hasDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Found a number, return true immediately
            }
        }
        return false; // Checked all letters, no number found
    }

    // Rule 4: Check for Uppercase
    private boolean hasUpperCase(String password) {
        // If the password equals itself in lowercase, it has no Uppercase
        return !password.equals(password.toLowerCase());
    }

    // Rule 5: Substring/Subarray Match (Username in Password)
    private boolean containsUsername(String password, String email) {
        if (email == null || !email.contains("@")) return false;

        // Extract "harshith" from "harshith@gmail.com"
        String username = email.split("@")[0];

        // Check if "harshith" is inside the password
        return password.toLowerCase().contains(username.toLowerCase());
    }
}