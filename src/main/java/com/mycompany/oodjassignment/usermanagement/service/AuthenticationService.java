package com.mycompany.oodjassignment.usermanagement.service;

import com.mycompany.oodjassignment.classes.LoginLog;
import com.mycompany.oodjassignment.classes.User;
import com.mycompany.oodjassignment.classes.UserStatus;
import com.mycompany.oodjassignment.usermanagement.util.PasswordUtil;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    
    private static final String LOGIN_LOG_FILE = "data/login_logs.dat";
    private User currentUser;
    private UserManager userManager;
    
    public AuthenticationService() {
        this.userManager = new UserManager();
        this.currentUser = null;
        ensureDataDirectory();
    }
    
    private void ensureDataDirectory() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File logFile = new File(LOGIN_LOG_FILE);
        System.out.println("Login logs will be stored at: " + logFile.getAbsolutePath());
    }
    
    public boolean login(String username, String password) {
        User user = userManager.getUserByUsername(username);
        
        if (user == null) {
            logLoginAttempt(username, "LOGIN", false);
            return false;
        }
        
        if (user.getStatus() != UserStatus.ACTIVE) {
            logLoginAttempt(username, "LOGIN", false);
            return false;
        }
        
        String hashedPassword = PasswordUtil.simpleHash(password);
        if (!user.getPassword().equals(hashedPassword)) {
            logLoginAttempt(username, "LOGIN", false);
            return false;
        }
        
        currentUser = user;
        logLoginAttempt(username, "LOGIN", true);
        return true;
    }
    
    public void logout() {
        if (currentUser != null) {
            logLoginAttempt(currentUser.getUsername(), "LOGOUT", true);
            currentUser = null;
        }
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public boolean changePassword(String oldPassword, String newPassword) {
        if (currentUser == null) {
            return false;
        }
        
        String hashedOldPassword = PasswordUtil.simpleHash(oldPassword);
        if (!currentUser.getPassword().equals(hashedOldPassword)) {
            return false;
        }
        
        if (!PasswordUtil.isPasswordStrong(newPassword)) {
            return false;
        }
        
        String hashedNewPassword = PasswordUtil.simpleHash(newPassword);
        currentUser.setPassword(hashedNewPassword);
        userManager.updateUser(currentUser);
        
        return true;
    }
    
    public String resetPassword(String username) {
        User user = userManager.getUserByUsername(username);
        if (user == null) {
            return null;
        }
        
        String tempPassword = PasswordUtil.generateTemporaryPassword();
        String hashedPassword = PasswordUtil.simpleHash(tempPassword);
        user.setPassword(hashedPassword);
        userManager.updateUser(user);
        
        return tempPassword;
    }
    
    public String recoverPassword(String email) {
        User user = userManager.getUserByEmail(email);
        if (user == null) {
            return null;
        }
        
        String tempPassword = PasswordUtil.generateTemporaryPassword();
        String hashedPassword = PasswordUtil.simpleHash(tempPassword);
        user.setPassword(hashedPassword);
        userManager.updateUser(user);
        
        return tempPassword;
    }
    
    private void logLoginAttempt(String username, String action, boolean successful) {
        LoginLog log = new LoginLog(username, action, LocalDateTime.now(), 
                                    successful, "localhost");
        
        List<LoginLog> logs = readLoginLogs();
        logs.add(log);
        writeLoginLogs(logs);
    }
    
    public List<LoginLog> readLoginLogs() {
        File file = new File(LOGIN_LOG_FILE);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<LoginLog>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
    
    private void writeLoginLogs(List<LoginLog> logs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(LOGIN_LOG_FILE))) {
            oos.writeObject(logs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<LoginLog> getUserLoginLogs(String username) {
        List<LoginLog> allLogs = readLoginLogs();
        List<LoginLog> userLogs = new ArrayList<>();
        
        for (LoginLog log : allLogs) {
            if (log.getUsername().equals(username)) {
                userLogs.add(log);
            }
        }
        
        return userLogs;
    }
}

