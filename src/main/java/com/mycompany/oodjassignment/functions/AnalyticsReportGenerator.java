package com.mycompany.oodjassignment.functions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.oodjassignment.classes.Course;
import com.mycompany.oodjassignment.classes.Grades;
import com.mycompany.oodjassignment.classes.Student;

public class AnalyticsReportGenerator {
    
    private String filePath;
    private final Database database = new Database();

    // constructor
    public AnalyticsReportGenerator(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // Generate comprehensive student analytics report
    public void generateAnalyticsReport() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.filePath));
            document.open();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Student Analytics Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            
            document.add(new Paragraph(" "));
            
            // Add report generation date
            document.add(new Paragraph("Report Generated: " + new java.util.Date()));
            document.add(new Paragraph(" "));
            
            // Generate and add overall statistics
            addOverallStats(document);
            
            // Generate and add department-wise statistics
            addDepartmentStats(document);
            
            // Generate and add course-wise statistics
            addCourseStats(document);
            
            // Generate and add semester-wise statistics
            addSemesterStats(document);
            
            document.close();
            System.out.println("Analytics report generated successfully at: " + this.filePath);
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void addOverallStats(Document document) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph section = new Paragraph("Overall Performance Statistics", sectionFont);
        document.add(section);
        document.add(new Paragraph(" "));
        
        // Get all students and grades
        HashMap<String, Student> studentDB = database.getStudentDB();
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        // Calculate overall statistics
        int totalStudents = studentDB.size();
        int totalGrades = gradeDB.size();
        
        // Calculate average GPA across all grades
        double totalGPA = 0.0;
        int gradeCount = 0;
        
        for (Grades grade : gradeDB.values()) {
            grade.setCourseObject(database.getCourse(grade.getCourseID()));
            totalGPA += grade.calculateGPA();
            gradeCount++;
        }
        double averageGPA = gradeCount > 0 ? totalGPA / gradeCount : 0.0;
        
        // Create table for overall stats
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100f);
        
        PdfPCell header1 = new PdfPCell(new Paragraph("Metric"));
        header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Paragraph("Value"));
        header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header2);
        
        table.addCell("Total Students");
        table.addCell(String.valueOf(totalStudents));
        
        table.addCell("Total Grades Recorded");
        table.addCell(String.valueOf(totalGrades));
        
        table.addCell("Average GPA");
        table.addCell(String.format("%.2f", averageGPA));
        
        document.add(table);
        document.add(new Paragraph(" "));
        
        // GPA distribution
        addGPADistribution(document, gradeDB);
    }
    
    private void addGPADistribution(Document document, HashMap<String, Grades> gradeDB) throws DocumentException {
        Font subSectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Paragraph subSection = new Paragraph("GPA Distribution", subSectionFont);
        document.add(subSection);
        document.add(new Paragraph(" "));
        
        // Count grades in different GPA ranges
        int gradeA = 0, gradeB = 0, gradeC = 0, gradeD = 0, gradeF = 0;
        
        for (Grades grade : gradeDB.values()) {
            grade.setCourseObject(database.getCourse(grade.getCourseID()));
            String letterGrade = grade.getLetterGrade();
            
            switch (letterGrade) {
                case "A":
                case "A-":
                    gradeA++;
                    break;
                case "B+":
                case "B":
                case "B-":
                    gradeB++;
                    break;
                case "C+":
                case "C":
                    gradeC++;
                    break;
                case "D":
                    gradeD++;
                    break;
                case "F":
                    gradeF++;
                    break;
            }
        }
        
        // Create distribution table
        PdfPTable distributionTable = new PdfPTable(2);
        distributionTable.setWidthPercentage(100f);
        
        PdfPCell header1 = new PdfPCell(new Paragraph("Grade Range"));
        header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        distributionTable.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Paragraph("Count"));
        header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        distributionTable.addCell(header2);
        
        distributionTable.addCell("A & A-");
        distributionTable.addCell(String.valueOf(gradeA));
        
        distributionTable.addCell("B+, B, B-");
        distributionTable.addCell(String.valueOf(gradeB));
        
        distributionTable.addCell("C+, C");
        distributionTable.addCell(String.valueOf(gradeC));
        
        distributionTable.addCell("D");
        distributionTable.addCell(String.valueOf(gradeD));
        
        distributionTable.addCell("F");
        distributionTable.addCell(String.valueOf(gradeF));
        
        document.add(distributionTable);
        document.add(new Paragraph(" "));
    }
    
    private void addDepartmentStats(Document document) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph section = new Paragraph("Department-wise Performance", sectionFont);
        document.add(section);
        document.add(new Paragraph(" "));
        
        // Group students by department/major and calculate statistics
        HashMap<String, List<Student>> studentsByMajor = new HashMap<>();
        HashMap<String, Student> studentDB = database.getStudentDB();
        
        for (Student student : studentDB.values()) {
            String major = student.getMajor();
            studentsByMajor.computeIfAbsent(major, k -> new ArrayList<>()).add(student);
        }
        
        // Create table for department stats
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        
        PdfPCell header1 = new PdfPCell(new Paragraph("Department"));
        header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Paragraph("Student Count"));
        header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header2);
        
        PdfPCell header3 = new PdfPCell(new Paragraph("Avg GPA"));
        header3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header3);
        
        PdfPCell header4 = new PdfPCell(new Paragraph("Top Performer"));
        header4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header4);
        
        for (Map.Entry<String, List<Student>> entry : studentsByMajor.entrySet()) {
            String major = entry.getKey();
            List<Student> students = entry.getValue();
            
            // Calculate average GPA for this department
            double totalGPA = 0.0;
            int gradeCount = 0;
            String topPerformer = "";
            double topGPA = 0.0;
            
            for (Student student : students) {
                List<Grades> studentGrades = database.getStudentAllGrades(student.getStudentID());
                double studentTotalGPA = 0.0;
                int studentGradeCount = 0;
                
                for (Grades grade : studentGrades) {
                    grade.setCourseObject(database.getCourse(grade.getCourseID()));
                    studentTotalGPA += grade.calculateGPA();
                    studentGradeCount++;
                }
                
                if (studentGradeCount > 0) {
                    double studentAvgGPA = studentTotalGPA / studentGradeCount;
                    totalGPA += studentAvgGPA;
                    gradeCount++;
                    
                    if (studentAvgGPA > topGPA) {
                        topGPA = studentAvgGPA;
                        topPerformer = student.getFirstName() + " " + student.getLastName();
                    }
                }
            }
            
            double avgGPA = gradeCount > 0 ? totalGPA / gradeCount : 0.0;
            
            table.addCell(major);
            table.addCell(String.valueOf(students.size()));
            table.addCell(String.format("%.2f", avgGPA));
            table.addCell(topPerformer);
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    private void addCourseStats(Document document) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph section = new Paragraph("Course-wise Performance", sectionFont);
        document.add(section);
        document.add(new Paragraph(" "));
        
        // Group grades by course and calculate statistics
        HashMap<String, List<Grades>> gradesByCourse = new HashMap<>();
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            String courseId = grade.getCourseID();
            gradesByCourse.computeIfAbsent(courseId, k -> new ArrayList<>()).add(grade);
        }
        
        // Create table for course stats
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        
        PdfPCell header1 = new PdfPCell(new Paragraph("Course ID"));
        header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Paragraph("Course Name"));
        header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header2);
        
        PdfPCell header3 = new PdfPCell(new Paragraph("Student Count"));
        header3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header3);
        
        PdfPCell header4 = new PdfPCell(new Paragraph("Avg GPA"));
        header4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header4);
        
        PdfPCell header5 = new PdfPCell(new Paragraph("Difficulty Level"));
        header5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header5);
        
        for (Map.Entry<String, List<Grades>> entry : gradesByCourse.entrySet()) {
            String courseId = entry.getKey();
            List<Grades> grades = entry.getValue();
            
            Course course = database.getCourse(courseId);
            if (course == null) continue; // Skip if course not found
            
            // Calculate average GPA for this course
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                grade.setCourseObject(course);
                totalGPA += grade.calculateGPA();
            }
            
            double avgGPA = grades.size() > 0 ? totalGPA / grades.size() : 0.0;
            
            // Determine difficulty level based on average GPA
            String difficultyLevel;
            if (avgGPA >= 3.5) {
                difficultyLevel = "Easy";
            } else if (avgGPA >= 2.5) {
                difficultyLevel = "Moderate";
            } else if (avgGPA >= 1.5) {
                difficultyLevel = "Difficult";
            } else {
                difficultyLevel = "Very Difficult";
            }
            
            table.addCell(courseId);
            table.addCell(course.getCourseName());
            table.addCell(String.valueOf(grades.size()));
            table.addCell(String.format("%.2f", avgGPA));
            table.addCell(difficultyLevel);
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
    
    private void addSemesterStats(Document document) throws DocumentException {
        Font sectionFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
        Paragraph section = new Paragraph("Semester-wise Performance", sectionFont);
        document.add(section);
        document.add(new Paragraph(" "));
        
        // Group grades by semester and calculate statistics
        Map<Integer, List<Grades>> gradesBySemester = new HashMap<>();
        HashMap<String, Grades> gradeDB = database.getGradeDB();
        
        for (Grades grade : gradeDB.values()) {
            int semester = grade.getSemester();
            gradesBySemester.computeIfAbsent(semester, k -> new ArrayList<>()).add(grade);
        }
        
        // Create table for semester stats
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100f);
        
        PdfPCell header1 = new PdfPCell(new Paragraph("Semester"));
        header1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header1);
        
        PdfPCell header2 = new PdfPCell(new Paragraph("Grade Count"));
        header2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header2);
        
        PdfPCell header3 = new PdfPCell(new Paragraph("Avg GPA"));
        header3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        table.addCell(header3);
        
        // Sort semesters in ascending order
        List<Integer> sortedSemesters = new ArrayList<>(gradesBySemester.keySet());
        java.util.Collections.sort(sortedSemesters);
        
        for (Integer semester : sortedSemesters) {
            List<Grades> grades = gradesBySemester.get(semester);
            
            // Calculate average GPA for this semester
            double totalGPA = 0.0;
            for (Grades grade : grades) {
                Course course = database.getCourse(grade.getCourseID());
                if (course != null) {
                    grade.setCourseObject(course);
                    totalGPA += grade.calculateGPA();
                }
            }
            
            double avgGPA = grades.size() > 0 ? totalGPA / grades.size() : 0.0;
            
            table.addCell("Semester " + semester);
            table.addCell(String.valueOf(grades.size()));
            table.addCell(String.format("%.2f", avgGPA));
        }
        
        document.add(table);
        document.add(new Paragraph(" "));
    }
}