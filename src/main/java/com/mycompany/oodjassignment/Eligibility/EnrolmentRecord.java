package com.mycompany.oodjassignment.Eligibility;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a single enrolment action recorded by the system.
 * Stored in a binary file for progress tracking and audit purposes.
 */
public class EnrolmentRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private String studentID;
    private String studentName;
    private double cgpa;
    private int failedCourses;
    private boolean eligible;
    private LocalDateTime enrolmentTime;

    /**
     * Constructor stores all important details at the time of enrolment.
     */
    public EnrolmentRecord(String studentID, String studentName,
                           double cgpa, int failedCourses, boolean eligible) {
        this.studentID = studentID;
        this.studentName = studentName;
        this.cgpa = cgpa;
        this.failedCourses = failedCourses;
        this.eligible = eligible;
        this.enrolmentTime = LocalDateTime.now(); // timestamp
    }

    public String getStudentID() { return studentID; }
    public String getStudentName() { return studentName; }
    public double getCgpa() { return cgpa; }
    public int getFailedCourses() { return failedCourses; }
    public boolean isEligible() { return eligible; }
    public LocalDateTime getEnrolmentTime() { return enrolmentTime; }
}
