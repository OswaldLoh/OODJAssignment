package com.mycompany.oodjassignment.Helpers;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class LoginLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final String action; // LOGIN or LOGOUT
    private final LocalDateTime timestamp;
    private final boolean successful;
    private final String ipAddress;

    public LoginLog(String username, String action, LocalDateTime timestamp, boolean successful, String ipAddress) {
        this.username = username;
        this.action = action;
        this.timestamp = timestamp;
        this.successful = successful;
        this.ipAddress = ipAddress;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s - %s by %s from %s",
                timestamp.format(formatter), action, successful ? "SUCCESS" : "FAILED", username, ipAddress);
    }
}

