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
}
