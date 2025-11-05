package com.mycompany.oodjassignment;

public class User {
    private String studentID, firstName, lastName, Major, Year, Email;
    
    // -------------- Getters ( to retrieve private attributes ) -------------
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
        return Major;
    }

    public String getYear() {
        return Year;
    }

    public String getEmail() {
        return Email;
    }
  
    // -------------- Setters ( set values for object ) -------------

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMajor(String Major) {
        this.Major = Major;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }
}
