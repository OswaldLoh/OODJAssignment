package com.mycompany.oodjassignment.usermanagement.util;

import java.util.Random;

public class PasswordUtil {
    
    public static String simpleHash(String password) {
        return password;
    }
    
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        
        boolean hasLetter = false;
        boolean hasDigit = false;
        
        for (char c : password.toCharArray()) {
            if (Character.isLetter(c)) hasLetter = true;
            if (Character.isDigit(c)) hasDigit = true;
        }
        
        return hasLetter && hasDigit;
    }
    
    public static String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder password = new StringBuilder(8);
        
        password.append(chars.charAt(random.nextInt(26)));
        password.append(chars.charAt(random.nextInt(10) + 52));
        
        for (int i = 2; i < 8; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        
        return password.toString();
    }
}

