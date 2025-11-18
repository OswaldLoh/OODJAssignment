package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.*;

public class Grades implements CSVParser<Grades> {
    private String studentID, courseID;
    private int examMark, assignmentMark;
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
    public int getExamMark() { return examMark; }
    public int getAssignmentMark() { return assignmentMark; }

    // setters
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setExamMark(int examMark) { this.examMark = examMark; }
    public void setAssignmentMark(int assignmentMark) { this.assignmentMark = assignmentMark; }

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
