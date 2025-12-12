package com.mycompany.oodjassignment.Classes;
import com.mycompany.oodjassignment.Helpers.CSVParser;

public class Student implements CSVParser<Student> {
    private String studentID, firstName, lastName, major, email, year;


    // constructors
    public Student() {};

    public Student(String studentID, String firstName, String lastName, String major, String year, String email) {
        this.studentID = studentID;
        this.firstName = firstName;         // Parameterized constructor to initialize new objects from CSV file
        this.lastName = lastName;
        this.major = major;
        this.email = email;
        this.year = year;
    }
    // getters
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
    public String getYear() { return year; }

    // setters
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
    public void setYear(String year) {
        this.year = year;
    }

    // Interface methods
    @Override
    public Student fromCSV(String line) {
        String[] details = line.split(",");
        return new Student(details[0],details[1],details[2],details[3],details[4],details[5]);
    }
    @Override
    public String toCSV() {
        return (studentID+","+firstName+","+lastName+","+major+","+year+","+email);
    }
    @Override
    public String getFileHeader() {
        return "StudentID,FirstName,LastName,Major,Year,Email";
    }
    @Override
    public String getFilename() {
        return "data/student_information.txt";
    }
    @Override
    public String toString() {
        return String.format("Student[ID=%s, FirstName=%s, LastName=%s, Major=%s, Year=%s, Email=%s]", 
                studentID, firstName, lastName, major, year, email);
    }
}


