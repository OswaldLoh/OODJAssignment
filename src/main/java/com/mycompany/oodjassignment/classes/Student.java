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

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
