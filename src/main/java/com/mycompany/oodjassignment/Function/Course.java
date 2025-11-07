package com.mycompany.oodjassignment.Function;

public class Course {
    private String courseID, courseName, semester, instructor;
    private int examWeight, assignmentWeight;

    // -------------- Getters ( to retrieve private attributes ) -------------
    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSemester() {
        return semester;
    }

    public String getInstructor() {
        return instructor;
    }

    public int getExamWeight() {
        return examWeight;
    }

    public int getAssignmentWeight() {
        return assignmentWeight;
    }

    // ------------ Setters ( set new ) -----------------
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public void setExamWeight(int examWeight) {
        this.examWeight = examWeight;
    }

    public void setAssignmentWeight(int assignmentWeight) {
        this.assignmentWeight = assignmentWeight;
    }

    public Course(String courseID, String courseName, String semester, String instructor, int examWeight, int assignmentWeight) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
    }
    
    
}
