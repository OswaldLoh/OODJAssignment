package Eligibility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student in the system, including academic details and results.
 * Extends Person to reuse common identity information.
 */
public class Student extends Person implements Serializable {

    private String major;
    private String year;
    private String email;

    // Each student has a list of results for different courses (composition relationship).
    private List<CourseResult> results = new ArrayList<>();

    // Constructor to set all required student details.

    public Student(String studentID, String firstName, String lastName,
                   String major, String year, String email) {
        super(studentID, firstName, lastName);
        this.major = major;
        this.year = year;
        this.email = email;
    }

    // Getters for the extra student attributes.

    public String getMajor() {
        return major;
    }

    public String getYear() {
        return year;
    }

    public String getEmail() {
        return email;
    }

    // Add a course result into this student's record.

    public void addResult(CourseResult result) {
        results.add(result);
    }

    // Returns all course results associated with this student.

    public List<CourseResult> getResults() {
        return results;
    }

    /**
     * Calculates the student's CGPA based on all course results.
     * Formula: sum(gradePoint * credits) / sum(credits)
     */
    public double calculateCGPA() {
        double totalGradePoints = 0.0;
        int totalCredits = 0;

        for (CourseResult cr : results) {
            double gp = cr.getGradePoint();
            int credits = cr.getCourse().getCredits();

            totalGradePoints += gp * credits;
            totalCredits += credits;
        }

        // To prevent division by zero if the student has no results.
        if (totalCredits == 0) {
            return 0.0;
        }

        return totalGradePoints / totalCredits;
    }

    /**
     * Counts number of failed courses based on grade point.
     * Here we assume gradePoint < 2.0 is considered as a fail.
     */
    public int countFailedCourses() {
        int count = 0;
        for (CourseResult cr : results) {
            if (cr.getGradePoint() < 2.0) {
                count++;
            }
        }
        return count;
    }
}
