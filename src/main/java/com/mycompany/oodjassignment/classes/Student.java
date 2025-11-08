package com.mycompany.oodjassignment.classes;

public class Student {
    private String studentID;
    private String firstName;
    private String lastName;
    private String major;
    private String email;
    private int year;

    public Student(String studentID, String firstName, String lastName, String major, String email) {
        this.studentID = studentID;
        this.firstName = firstName;         // Parameterized constructor to initialize new objects from CSV file
        this.lastName = lastName;
        this.major = major;
        this.email = email;
    }
    // Getters
    public String getStudentID() {
        return studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMajor() {
        return major;
    }

    public String getEmail() {
        return email;
    }

    public int getYear() {
        return year;
    }
    // Setters

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
