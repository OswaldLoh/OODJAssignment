package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;

public class Course implements CSVParser<Course>{
    private String courseID, courseName, semester, instructor;
    private int examWeight, assignmentWeight, credit;
    private static final String filename = "course_assessment_information.csv";

    // constructors
    public Course() {};

    public Course(String courseID, String courseName, int credit, String semester, String instructor, int examWeight, int assignmentWeight) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.semester = semester;
        this.instructor = instructor;
        this.examWeight = examWeight;
        this.assignmentWeight = assignmentWeight;
        this.credit = credit;
    }
    // getters
    public String getCourseID() {
        return courseID;
    }
    public String getCourseName() {
        return courseName;
    }
    public String getSemester() {
        return semester;
    }
    public String getInstructor() {
        return instructor;
    }
    public int getExamWeight() {
        return examWeight;
    }
    public int getAssignmentWeight() {
        return assignmentWeight;
    }
    public int getCredit() {
        return credit;
    }

    @Override
    public Course fromCSV(String line) {
        String[] details = line.split(",");
        return new Course(details[0], details[1], Integer.parseInt(details[2]), details[3], details[4], Integer.parseInt(details[5]), Integer.parseInt(details[6]));
    }
    @Override
    public String toCSV() {     // Null because probably wont need to write object back to CSV file
        return null;
    }
    @Override
    public String getFileHeader() { // Again, null because don't need to write to CSV file
        return null;
    }
    @Override
    public String getFilename() {
        return filename;
    }
}
