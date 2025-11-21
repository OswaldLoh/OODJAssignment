package com.mycompany.oodjassignment.functions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.Student;

public class ReportGenerator {
    
    private String reportID;
    private String filePath;

    // constructor
    public ReportGenerator(String reportID, String filePath) {
        this.reportID = reportID;
        this.filePath = filePath;
    }

    // getter and setter
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // method
    public void displayReport(){
        
    }

    public void generateReport(String studentId){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.filePath));
            document.open();
            
            // Get student information
            String fullName = getStudentNameById(studentId);
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Academic Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            
            // Add student information
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Student Name: " + fullName));
            document.add(new Paragraph("Student ID: " + studentId));
            document.add(new Paragraph(" "));
            
            // Load and filter grades for this student
            List<Grades> studentGrades = loadStudentGrades(studentId);
            
            // Create table for grades
            PdfPTable table = new PdfPTable(4); // Course Name, Grade, GPA, Status
            table.addCell("Course Name");
            table.addCell("Grade");
            table.addCell("GPA");
            table.addCell("Status");
            
            double totalGPA = 0.0;
            int validCourses = 0;
            
            for (Grades grade : studentGrades) {
                // Get course name by course ID
                String courseName = getCourseNameById(grade.getCourseID());
                
                // Get the course object to set it in the grade
                Course course = getCourseById(grade.getCourseID());
                grade.setCourseObject(course);
                
                // Calculate GPA for this grade
                double gpa = grade.calculateGPA();
                
                // Determine status based on total mark
                double totalMark = grade.getWeightedExamMark() + grade.getWeightedAssignmentMark();
                String status = totalMark >= 50 ? "PASS" : "FAIL";
                
                table.addCell(courseName);
                table.addCell(String.format("%.2f", totalMark));
                table.addCell(String.format("%.2f", gpa));
                table.addCell(status);
                
                totalGPA += gpa;
                validCourses++;
            }
            
            document.add(table);
            
            // Add total GPA
            if (validCourses > 0) {
                double averageGPA = totalGPA / validCourses;
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Average GPA: " + String.format("%.2f", averageGPA)));
            }
            
            document.close();
            System.out.println("Report generated successfully at: " + this.filePath);
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getStudentNameById(String studentId) {
        try {
            List<Student> allStudents = loadAllRecords(new Student());
            
            for (Student student : allStudents) {
                if (student.getStudentID().equals(studentId)) {
                    return student.getFirstName() + " " + student.getLastName();
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading student data: " + e.getMessage());
        }
        
        return "Student ID: " + studentId; // Fallback if not found
    }

    // Helper method to load grades for a specific student
    private List<Grades> loadStudentGrades(String studentId) {
        try {
            List<Grades> allGrades = loadAllRecords(new Grades());
            List<Grades> studentGrades = new ArrayList<>();
            
            for (Grades grade : allGrades) {
                if (grade.getStudentID().equals(studentId)) {
                    studentGrades.add(grade);
                }
            }
            
            return studentGrades;
        } catch (Exception e) {
            System.out.println("Error loading student grades: " + e.getMessage());
            return new ArrayList<>(); // Return empty list if error occurs
        }
    }

    // Helper method to get course by ID
    private Course getCourseById(String courseID) {
        // Load courses from the CSV file and find the matching course
        try {
            List<Course> allCourses = loadAllRecords(new Course());
            
            for (Course course : allCourses) {
                if (course.getCourseID().equals(courseID)) {
                    return course;
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading course data: " + e.getMessage());
        }
        
        return null; // Return null if not found
    }

    // Helper method to get course name by ID
    private String getCourseNameById(String courseID) {
        Course course = getCourseById(courseID);
        if (course != null) {
            return course.getCourseName();
        }
        
        return "Course ID: " + courseID; // Fallback if not found
    }

    // Generic method to load all records from CSV for any class implementing CSVParser
    private <T> List<T> loadAllRecords(CSVParser<T> parser) throws IOException {
        List<T> records = new ArrayList<>();
        String filename = parser.getFilename();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            // Skip header line if it exists
            String header = br.readLine();
            
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    T record = parser.fromCSV(line);
                    records.add(record);
                }
            }
        }
        
        return records;
    }


}
