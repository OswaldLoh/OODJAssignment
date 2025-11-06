package com.mycompany.oodjassignment;

public class Course {
    private String courseID, courseName, semester, instructor;
    private int examWeight, assignmentWeight;

    // getters
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

    // setters
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // constructors
    public Course(String courseID, String courseName, String semester, String instructor, int examWeight, int assignmentWeight) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
    }
}
