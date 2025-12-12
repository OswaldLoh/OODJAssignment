package com.mycompany.oodjassignment.Eligibility;

import com.mycompany.oodjassignment.Entities.Student;
import com.mycompany.oodjassignment.Entities.Grades;
import com.mycompany.oodjassignment.Entities.Course;
import com.mycompany.oodjassignment.Helpers.Database;

import java.util.ArrayList;

/**
 * This class centralizes all progression‐eligibility calculations.
 * It relies on the existing Database, Grades, and Course classes to
 * compute CGPA and identify failed courses for each student.
 */
public class EligibilityChecker {

    private final Database db;

    // Eligibility thresholds defined by the assignment requirement
    private static final double MIN_CGPA = 2.0;
    private static final int MAX_FAILED_COURSES = 3;

    /**
     * The checker must receive the shared Database instance so it can
     * access grades and course weights.
     */
    public EligibilityChecker(Database db) {
        this.db = db;
    }

    /**
     * Retrieves the student's overall CGPA using the Database logic.
     * Database.calculateOverallCGPA() already accounts for weighted marks.
     */
    public double getCGPA(Student student) {
        return db.calculateOverallCGPA(student.getStudentID());
    }

    /**
     * Counts how many of the student's courses are considered failed.
     * A failed course is defined as GPA < 2.0.
     * We ensure each Grades object is linked to its Course object first
     * so that the weight calculations work correctly.
     */
    public int getFailedCourses(Student student) {
        int count = 0;
        ArrayList<Grades> gradeList = db.getStudentAllGrades(student.getStudentID());

        for (Grades g : gradeList) {
            Course c = db.getCourse(g.getCourseID());
            if (c != null) {
                g.setCourseObject(c);
            }

            if (g.calculateGPA() < 2.0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Main eligibility rule used by Role 2:
     *   - CGPA must be at least 2.0
     *   - Student must have at most 3 failed courses
     */
    public boolean isEligible(Student student) {
        double cgpa = getCGPA(student);
        int fails = getFailedCourses(student);
        return cgpa >= MIN_CGPA && fails <= MAX_FAILED_COURSES;
    }
}
