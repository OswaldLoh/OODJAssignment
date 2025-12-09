package com.mycompany.oodjassignment.classes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String userID;
    private String username;
    private String name;
    private String email;
    private String password;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    protected User() {
        this.status = UserStatus.ACTIVE;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
    }

    protected User(String userID, String username, String password, String name, String email, UserRole role) {
        this();
        this.userID = userID;
        this.username = username != null ? username : userID;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public abstract String getPermissions();

    // region Getters
    public String getUserID() {
        return userID;
    }

    public String getUserId() {
        return getUserID();
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    public String getRoleLabel() {
        return role != null ? role.getDisplayName() : null;
    }

    public UserStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }
    // endregion

    // region Setters
    public void setUserID(String userID) {
        this.userID = userID;
        if (this.username == null || this.username.isBlank()) {
            this.username = userID;
        }
        touch();
    }

    public void setUserId(String userID) {
        setUserID(userID);
    }

    public void setUsername(String username) {
        this.username = username;
        touch();
    }

    public void setName(String name) {
        this.name = name;
        touch();
    }

    public void setFullName(String fullName) {
        setName(fullName);
    }

    public void setEmail(String email) {
        this.email = email;
        touch();
    }

    public void setPassword(String password) {
        this.password = password;
        touch();
    }

    public void setRole(String roleLabel) {
        UserRole resolved = UserRole.fromLabel(roleLabel);
        if (resolved == null) {
            throw new IllegalArgumentException("Unknown role: " + roleLabel);
        }
        setRole(resolved);
    }

    public void setRole(UserRole role) {
        this.role = role;
        touch();
    }

    public void setStatus(UserStatus status) {
        this.status = status;
        touch();
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
    // endregion

    public boolean isActive() {
        return this.status == UserStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = UserStatus.DEACTIVATED;
        touch();
    }

    public void activate() {
        this.status = UserStatus.ACTIVE;
        touch();
    }

    public void showMenu() {
        
    }

    public String toFileString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s",
                userID, username, password, name, email, role != null ? role.name() : "",
                status != null ? status.name() : "", createdDate != null ? createdDate.format(formatter) : "",
                lastModifiedDate != null ? lastModifiedDate.format(formatter) : "");
    }

    @Override
    public String toString() {
        return String.format("User[ID=%s, Username=%s, Name=%s, Role=%s, Status=%s]",
                userID, username, name, role, status);
    }

    private void touch() {
        this.lastModifiedDate = LocalDateTime.now();
    }
}
