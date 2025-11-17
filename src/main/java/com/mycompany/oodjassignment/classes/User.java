package com.mycompany.oodjassignment.classes;


import java.util.ArrayList;

public class User {
    protected String userID;
    private String name;
    private String email;
    private String password;
    private String role;

    public User() {                // no arg constructor
        System.out.println("A user is created!");
    }

    // getters
    public String getUserID() {
        return userID;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }

    // setters
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }

    // menu
    public void showMenu() {
        System.out.println("General Menu");
    }
}

