package Eligibility;

import java.io.Serializable;

/**
 * Stores the result of a student for a particular course,
 * including raw marks, final score, and derived grade/grade point.
 */
public class CourseResult implements Serializable {

    private Course course;          // The course this result belongs to.
    private double examMark;        // Raw exam mark (0-100).
    private double assignmentMark;  // Raw assignment mark (0-100).

    private double finalScore;      // Weighted score based on examWeight & assignmentWeight.
    private String letterGrade;     // A, A-, B+, etc.
    private double gradePoint;      // 4.0, 3.7, 3.3, etc.

    /**
     * Constructor that takes the course and raw marks.
     * Automatically calculates the final score, letter grade, and grade point.
     */
    public CourseResult(Course course, double examMark, double assignmentMark) {
        this.course = course;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
        calculateFinalScoreAndGrade();
    }

    /**
     * Calculates finalScore using the course's weightage
     * and then maps finalScore to letterGrade and gradePoint.
     */
    private void calculateFinalScoreAndGrade() {
        // Weighted components based on percentages from course object.
        double examComponent = examMark * (course.getExamWeight() / 100.0);
        double assignmentComponent = assignmentMark * (course.getAssignmentWeight() / 100.0);

        this.finalScore = examComponent + assignmentComponent;

        // Simple grading scheme. 
        if (finalScore >= 80) {
            letterGrade = "A";
            gradePoint = 4.0;
        } else if (finalScore >= 75) {
            letterGrade = "A-";
            gradePoint = 3.7;
        } else if (finalScore >= 70) {
            letterGrade = "B+";
            gradePoint = 3.3;
        } else if (finalScore >= 65) {
            letterGrade = "B";
            gradePoint = 3.0;
        } else if (finalScore >= 60) {
            letterGrade = "B-";
            gradePoint = 2.7;
        } else if (finalScore >= 55) {
            letterGrade = "C+";
            gradePoint = 2.3;
        } else if (finalScore >= 50) {
            letterGrade = "C";
            gradePoint = 2.0;
        } else if (finalScore >= 45) {
            letterGrade = "D";
            gradePoint = 1.7;
        } else {
            letterGrade = "F";
            gradePoint = 0.0;
        }
    }

    // Getters for all attributes.

    public Course getCourse() {
        return course;
    }

    public double getExamMark() {
        return examMark;
    }

    public double getAssignmentMark() {
        return assignmentMark;
    }

    public double getFinalScore() {
        return finalScore;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }
}
