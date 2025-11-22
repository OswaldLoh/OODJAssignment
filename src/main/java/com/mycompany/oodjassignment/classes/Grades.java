package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class Grades implements CSVParser<Grades> {
    private String gradeID, studentID, courseID;
    private double examMark, assignmentMark;
    private static final String filename = "student_grades.csv";
    private Course course;

    // constructors
    public Grades () {};
    public Grades(String gradeID, String studentID, String courseID, int examMark, int assignmentMark) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
    }
    public Grades(String gradeID, String studentID, String courseID, int examMark, int assignmentMark, Course course) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
        this.course = course;
    }
    // getters
    public String getGradeID() { return gradeID; }
    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public double getAssignmentMark() { return assignmentMark; }
    public double getExamMark() { return examMark; }

    // setters
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setExamMark(int examMark) { this.examMark = examMark; }
    public void setAssignmentMark(int assignmentMark) { this.assignmentMark = assignmentMark; }
    public void setCourseObject(Course course) { this.course = course; }

    // methods
    public double calculateGPA() {
        double totalMark, GPA;
        totalMark = getWeightedExamMark() + (getWeightedAssignmentMark());

        if (totalMark >= 80 && totalMark <= 100) {
            GPA = 4.00;
        } else if (totalMark >= 75 && totalMark <= 79) {
            GPA = 3.70;
        } else if (totalMark >= 70 && totalMark <= 74) {
            GPA = 3.30;
        } else if (totalMark >= 65 && totalMark <= 69) {
            GPA = 3.00;
        } else if (totalMark >= 60 && totalMark <= 64) {
            GPA = 2.70;
        } else if (totalMark >= 55 && totalMark <= 59) {
            GPA = 2.30;
        } else if (totalMark >= 50 && totalMark <= 54) {
            GPA = 2.00;
        } else if (totalMark >= 40 && totalMark <= 49) {
            GPA = 1.70;
        } else if (totalMark >= 30 && totalMark <= 39) {
            GPA = 1.30;
        } else if (totalMark >= 20 && totalMark <= 29) {
            GPA = 1.00;
        } else {
            GPA = 0.00;
        }
        return GPA;
    }
    public double getWeightedExamMark() {
        return (examMark * course.getExamWeight() / 100);
    }

    public double getWeightedAssignmentMark() {
        return (assignmentMark* course.getAssignmentWeight() / 100);
    }

    // interface methods
    @Override
    public Grades fromCSV(String line) {
        String[] details = line.split(",");
        return new Grades(details[0], details[1], details[2], Integer.parseInt(details[3]), Integer.parseInt(details[4]));
    }
    @Override
    public String toCSV() {
        return (gradeID+","+studentID+","+courseID+","+examMark+","+assignmentMark);
    }
    @Override
    public String getFileHeader() {
        return "gradeID,studentID,courseID,examMark,assignmentMark";
    }
    @Override
    public String getFilename() {
        return filename;
    }
}
