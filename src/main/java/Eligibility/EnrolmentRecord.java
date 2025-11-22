package Eligibility;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a record of a student's enrolment decision.
 * Stored in a binary file for persistence.
 */
public class EnrolmentRecord implements Serializable {

    private String studentID;
    private String studentName;
    private double cgpa;
    private int failedCourses;
    private boolean eligible;
    private LocalDateTime enrolmentTime;

    /**
     * Constructor that captures the key information at the time of enrolment.
     */
    public EnrolmentRecord(Student student, double cgpa, int failedCourses, boolean eligible) {
        this.studentID = student.getId();
        this.studentName = student.getFullName();
        this.cgpa = cgpa;
        this.failedCourses = failedCourses;
        this.eligible = eligible;
        this.enrolmentTime = LocalDateTime.now(); // timestamp of when record is created.
    }

    // Getters so that the data can be read for reporting or debugging purposes.

    public String getStudentID() {
        return studentID;
    }

    public String getStudentName() {
        return studentName;
    }

    public double getCgpa() {
        return cgpa;
    }

    public int getFailedCourses() {
        return failedCourses;
    }

    public boolean isEligible() {
        return eligible;
    }

    public LocalDateTime getEnrolmentTime() {
        return enrolmentTime;
    }
}
