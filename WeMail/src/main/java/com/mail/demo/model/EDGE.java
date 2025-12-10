package com.mail.demo.model;

public class EDGE {

    public String validateFullPassword(String password, String email) {

        if (!isLengthValid(password)) {
            return "Error: Password size is less than 8 characters.";
        }

        if (!hasSpecialChar(email)) {
            return "Error: Email must contain '@'.";
        }

        if (!hasDigit(password)) {
            return "Error: Password must contain at least one number.";
        }

        if (!hasUpperCase(password)) {
            return "Error: Password must contain at least one uppercase letter.";
        }

        if (containsUsername(password, email)) {
            return "Error: Password cannot contain your username/email.";
        }

        return "logging in.....";
    }


    private boolean isLengthValid(String password) {
        return password != null && password.length() >= 8;
    }

    private boolean hasSpecialChar(String email) {
        return email.contains("@");
    }

    private boolean hasDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true; // Found a number, return true immediately
            }
        }
        return false; // Checked all letters, no number found
    }

    private boolean hasUpperCase(String password) {
        return !password.equals(password.toLowerCase());
    }

    private boolean containsUsername(String password, String email) {
        if (email == null || !email.contains("@")) return false;

        String username = email.split("@")[0];

        return password.toLowerCase().contains(username.toLowerCase());
    }
}