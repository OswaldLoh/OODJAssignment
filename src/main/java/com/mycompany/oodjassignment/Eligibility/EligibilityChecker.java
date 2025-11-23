package com.mycompany.oodjassignment.Eligibility;

// Uses CGPA and number of failed courses to determine progression eligibility.

public class EligibilityChecker implements EligibilityRule {

    /**
     * Checks whether the student is eligible to progress.
     * Rule:
     *  - CGPA must be at least 2.0
     *  - Number of failed courses must be 3 or less
     */
    @Override
    public boolean isEligible(Student student) {
        double cgpa = student.calculateCGPA();
        int failedCourses = student.countFailedCourses();

        return (cgpa >= 2.0 && failedCourses <= 3);
    }
}
