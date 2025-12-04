package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.CSVParser;

public class Grades implements CSVParser<Grades> {
    private String gradeID, studentID, courseID, component;
    private double examMark, assignmentMark;
    private static final String filename = "student_grades.csv";
    private int semester, attempt;
    private Course course;

    // constructors
    public Grades () {};
    public Grades(String gradeID, String studentID, String courseID, int semester, int attempt, int examMark, int assignmentMark) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.semester = semester;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
        this.attempt = attempt;
    }
    public Grades(String gradeID, String studentID, String courseID, int semester,int attempt, int examMark, int assignmentMark, Course course) {
        this.gradeID = gradeID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.semester = semester;
        this.examMark = examMark;
        this.assignmentMark = assignmentMark;
        this.course = course;
        this.attempt = attempt;
    }
    // getters
    public String getGradeID() { return gradeID; }
    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public double getAssignmentMark() { return assignmentMark; }
    public double getExamMark() { return examMark; }
    public int getSemester() { return semester; }
    public int getAttempt() { return attempt; }

    // setters
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setExamMark(int examMark) { this.examMark = examMark; }
    public void setAssignmentMark(int assignmentMark) { this.assignmentMark = assignmentMark; }
    public void setCourseObject(Course course) { this.course = course; }
    public void setAttempt(int attempt) { this.attempt = attempt; }

    // methods
    public double calculateGPA() {
        double totalMark, GPA;
        totalMark = getWeightedExamMark() + (getWeightedAssignmentMark());
        System.out.println("Total mark: " + totalMark);

        if (totalMark >= 80) {
            return 4.00;
        } else if (totalMark >= 75) {
            return 3.70;
        } else if (totalMark >= 70) {
            return 3.30;
        } else if (totalMark >= 65) {
            return 3.00;
        } else if (totalMark >= 60) {
            return 2.70;
        } else if (totalMark >= 55) {
            return 2.30;
        } else if (totalMark >= 50) {
            return 2.00;
        } else if (totalMark >= 40) {
            return 1.70;
        } else if (totalMark >= 30) {
            return 1.30;
        } else if (totalMark >= 20) {
            return 1.00;
        } else {
            return 0.00;
        }
    }

    public String getLetterGrade() {
        double GPA = calculateGPA();

        if (GPA >= 4.00) {
            return "A";
        } else if (GPA >= 3.70) {
            return "A-";
        } else if (GPA >= 3.30) {
            return "B+";
        } else if (GPA >= 3.00) {
            return "B";
        } else if (GPA >= 2.70) {
            return "B-";
        } else if (GPA >= 2.30) {
            return "C+";
        } else if (GPA >= 2.00) {
            return "C";
        } else if (GPA >= 1.70) {
            return "D";
        } else {
            return "F";
    }

    }
    public double getWeightedExamMark() {
        System.out.println(studentID + " " + courseID + " Exam Mark: " + examMark * course.getExamWeight() / 100);
        return (examMark * course.getExamWeight() / 100);
    }

    public double getWeightedAssignmentMark() {
        System.out.println(studentID + " " + courseID + " Assignment Mark: " + assignmentMark* course.getAssignmentWeight() / 100);
        return (assignmentMark* course.getAssignmentWeight() / 100);
    }

    // interface methods
    @Override
    public Grades fromCSV(String line) {
        String[] details = line.split(",");
        return new Grades(details[0], details[1], details[2],Integer.parseInt(details[3]),Integer.parseInt(details[4]), Integer.parseInt(details[5]), Integer.parseInt(details[6]));
    }
    @Override
    public String toCSV() {
        return (gradeID+","+studentID+","+courseID+","+semester+","+attempt+","+examMark+","+assignmentMark);
    }
    @Override
    public String getFileHeader() {
        return "gradeID,studentID,courseID,semester,attempt,examMark,assignmentMark";
    }
    @Override
    public String getFilename() {
        return filename;
    }
}
