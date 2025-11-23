package com.mycompany.oodjassignment.Eligibility;

import java.io.Serializable;

/**
 * Represents a course in the system, including credits and assessment weightage.
 * Information is loaded from course_assessment_information.csv.
 */
public class Course implements Serializable {

    private String courseID;
    private String courseName;
    private int credits;
    private String semester;
    private String instructor;
    private double examWeight;        // e.g. 69 means 69% of final mark
    private double assignmentWeight;  // e.g. 31 means 31% of final mark

    /**
     * Constructor to initialise a course with all necessary assessment information.
     */
    public Course(String courseID, String courseName, int credits,
                  String semester, String instructor,
                  double examWeight, double assignmentWeight) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.credits = credits;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
    }

    // Basic getters for each course attribute.

    public String getCourseID() {
        return courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public int getCredits() {
        return credits;
    }

    public String getSemester() {
        return semester;
    }

    public String getInstructor() {
        return instructor;
    }

    public double getExamWeight() {
        return examWeight;
    }

    public double getAssignmentWeight() {
        return assignmentWeight;
    }
}
