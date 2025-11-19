package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.*;

import java.util.HashMap;

public class Grades implements CSVParser<Grades> {
    private String studentID, courseID;
    private double examMark, assignmentMark;
    private static final String filename = "student_grades.csv";

    // constructors
    public Grades () {};
    public Grades(String studentID, String courseID, int examMark, int assignmentMark) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
    }
    // getters
    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public double getExamMark() { return examMark; }
    public double getAssignmentMark() { return assignmentMark; }

    // setters
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setExamMark(int examMark) { this.examMark = examMark; }
    public void setAssignmentMark(int assignmentMark) { this.assignmentMark = assignmentMark; }

    public double calculateGPA(HashMap<String, Course> courseDB) {
        double totalMark = 0, GPA = 0;
        for (String key : courseDB.keySet()) {  // if the grade's course ID equals to courseID in hashmap
            if (key.equals(courseID)) {
                Course course = courseDB.get(key);
                totalMark = (examMark/course.getExamWeight()) + (assignmentMark/course.getAssignmentWeight());
            }
        }
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

    @Override
    public Grades fromCSV(String line) {
        String[] details = line.split(",");
        return new Grades(details[0], details[1], Integer.parseInt(details[2]), Integer.parseInt(details[3]));
    }
    @Override
    public String toCSV() {
        return (studentID+","+courseID+","+examMark+","+assignmentMark);
    }
    @Override
    public String getFileHeader() {
        return "CourseID,CourseName,Credits,Semester,Instructor,ExamWeight,AssignmentWeight";
    }
    @Override
    public String getFilename() {
        return filename;
    }
}
