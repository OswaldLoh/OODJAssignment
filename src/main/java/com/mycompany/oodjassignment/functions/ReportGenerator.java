package com.mycompany.oodjassignment.functions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

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

public class ReportGenerator {
    
    private String reportID;
    private String filePath;
    private final Database database = new Database();

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

    // Semester based report generation    
    public void generateReport(String studentId, int semester){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.filePath));
            document.open();
            
            // Get student information using existing database method
            Student student = this.database.getStudent(studentId);
            String fullName = student.getFirstName() + " " + student.getLastName();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Academic Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            
            // Add student information
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Student Name: " + fullName));
            document.add(new Paragraph("Student ID: " + studentId));
            document.add(new Paragraph("Program: " + student.getMajor()));
            document.add(new Paragraph("Semester: " + semester));
            document.add(new Paragraph(" "));
            
            // Load and filter grades for this student using existing database method
            List<Grades> studentGrades = new ArrayList<>();
            for (Grades grade : database.getGradeDB().values()) {
                if (grade.getStudentID().equals(studentId)) {
                    // Filter by semester if a specific semester is requested
                    if(grade.getSemester() != semester) {
                        continue; 
                    }
                    
                    // Get and set the course object for proper GPA calculation
                    Course course = database.getCourse(grade.getCourseID());
                    grade.setCourseObject(course);
                    studentGrades.add(grade);
                }
            }
            
            // Create table for grades
            PdfPTable table = new PdfPTable(5); // adjust it to 5 for 5 coloumn

            // Adjust the width of the coloumns
            table.setWidths(new float[]{1.2f, 2.5f, 1.0f, 0.8f, 1.0f}); 

            // Set the table max with the document width
            table.setWidthPercentage(100f); // need to set to 100 percent cause the defualt is 80 percent
            
            // Create header cells with center alignment ( Better Visual for User)
            PdfPCell headerCell1 = new PdfPCell(new Paragraph("Module Code"));
            headerCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell1);
            
            PdfPCell headerCell2 = new PdfPCell(new Paragraph("Module"));
            headerCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell2);
            
            PdfPCell headerCell3 = new PdfPCell(new Paragraph("Credit Hours"));
            headerCell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell3);
            
            PdfPCell headerCell4 = new PdfPCell(new Paragraph("Grade"));
            headerCell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell4);
            
            PdfPCell headerCell5 = new PdfPCell(new Paragraph("Grade Point"));
            headerCell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            table.addCell(headerCell5);
            
            double totalQualityPoints = 0.0;
            int totalCreditHours = 0;
            
            // Insert data rows from hashmap inside the grade 
            for (Grades grade : studentGrades) {
                // Get course information using existing database method
                Course course = database.getCourse(grade.getCourseID());
                String courseCode = course.getCourseID();
                String courseName = course.getCourseName() ;
                
                // Get GPA and letter grade
                double gpa = grade.calculateGPA();
                String letterGrade = grade.getLetterGrade();

                // Get credit hours from course using the proper getter method
                int courseCredit = course.getCredit();
                                
                // Create data cells with center alignment
                PdfPCell cell1 = new PdfPCell(new Paragraph(courseCode));
                cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell1);
                
                PdfPCell cell2 = new PdfPCell(new Paragraph(courseName));
                cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell2);
                
                PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(courseCredit)));
                cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell3);
                
                PdfPCell cell4 = new PdfPCell(new Paragraph(letterGrade));
                cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell4);
                
                PdfPCell cell5 = new PdfPCell(new Paragraph(String.format("%.2f", gpa)));
                cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                table.addCell(cell5);
                
                // Calculate quality points for GPA calculation (credit * gpa)
                totalQualityPoints += courseCredit * gpa;
                totalCreditHours += courseCredit;
            }
            
            document.add(table);
            
            // Calculate and display total credit hours and cumulative GPA
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Credit Hours: " + totalCreditHours));
            
            if (totalCreditHours > 0) {
                double cumulativeGPA = totalQualityPoints / totalCreditHours;
                document.add(new Paragraph("Cumulative GPA: " + String.format("%.2f", cumulativeGPA)));
            }
            
            document.close();
            System.out.println("Report generated successfully at: " + this.filePath);
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // Yearly based report generation
    public void generateYearlyReport(String studentId, int year){
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(this.filePath));
            document.open();
            
            // Get student information using existing database method
            Student student = this.database.getStudent(studentId);
            String fullName = student.getFirstName() + " " + student.getLastName();
            
            // Add title
            Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Academic Report", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);
            
            // Add student information
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Student Name: " + fullName));
            document.add(new Paragraph("Student ID: " + studentId));
            document.add(new Paragraph("Program: " + student.getMajor()));
            document.add(new Paragraph("Academic Year: "+ year));
            document.add(new Paragraph(" "));
            
            // Determine which semesters belong to the target year
            int startSemester = (year - 1) * 2 + 1;  
            int endSemester = startSemester + 1;      
            
            // Load and filter grades for this student for the entire year using existing database method
            for (int currentSemester = startSemester; currentSemester <= endSemester; currentSemester++) {
                // Get grades for the current semester
                List<Grades> semesterGrades = new ArrayList<>();
                for (Grades grade : database.getGradeDB().values()) {
                    if (grade.getStudentID().equals(studentId) && 
                        grade.getSemester() == currentSemester) {
                        
                        // Get and set the course object for proper GPA calculation
                        Course course = database.getCourse(grade.getCourseID());
                        grade.setCourseObject(course);
                        semesterGrades.add(grade);
                    }
                }
                
                // Only add section if there are grades for this semester
                if (!semesterGrades.isEmpty()) {
                    // Add semester header
                    Font semesterHeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
                    Paragraph semesterHeader = new Paragraph("Semester " + currentSemester, semesterHeaderFont);
                    document.add(semesterHeader);
                    document.add(new Paragraph(" ")); 
                    // Create table for semester grades
                    PdfPTable semesterTable = new PdfPTable(5); // adjust it into 5 for 5 coloumn

                    // Adjust the width of the columns
                    semesterTable.setWidths(new float[]{1.2f, 2.5f, 1.0f, 0.8f, 1.0f}); 

                    // Set the table max with the document width
                    semesterTable.setWidthPercentage(100f); // need to set to 100 percent cause the defualt is 80 percent
                    
                    // Create header cells with center alignment ( Better Visual for User)
                    PdfPCell headerCell1 = new PdfPCell(new Paragraph("Module Code"));
                    headerCell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    semesterTable.addCell(headerCell1);
                    
                    PdfPCell headerCell2 = new PdfPCell(new Paragraph("Module"));
                    headerCell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    semesterTable.addCell(headerCell2);
                    
                    PdfPCell headerCell3 = new PdfPCell(new Paragraph("Credit Hours"));
                    headerCell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    semesterTable.addCell(headerCell3);
                    
                    PdfPCell headerCell4 = new PdfPCell(new Paragraph("Grade"));
                    headerCell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    semesterTable.addCell(headerCell4);
                    
                    PdfPCell headerCell5 = new PdfPCell(new Paragraph("Grade Point"));
                    headerCell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                    semesterTable.addCell(headerCell5);
                    
                    double semesterQualityPoints = 0.0;
                    int semesterCreditHours = 0;
                    
                    // Insert data rows from hashmap inside the grade 
                    for (Grades grade : semesterGrades) {
                        // Get course information using existing database method
                        Course course = database.getCourse(grade.getCourseID());
                        String courseCode = course.getCourseID();
                        String courseName = course.getCourseName() ;
                        
                        // Calculate GPA 
                        double gpa = grade.calculateGPA();
                        
                        // Set the letter grade based on total marks
                        String letterGrade = grade.getLetterGrade();
                        
                        // Get credit hours from course using the proper getter method
                        int courseCredit = course.getCredit();
                        
                        // Create data cells with center alignment
                        PdfPCell cell1 = new PdfPCell(new Paragraph(courseCode));
                        cell1.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        semesterTable.addCell(cell1);
                        
                        PdfPCell cell2 = new PdfPCell(new Paragraph(courseName));
                        cell2.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        semesterTable.addCell(cell2);
                        
                        PdfPCell cell3 = new PdfPCell(new Paragraph(String.valueOf(courseCredit)));
                        cell3.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        semesterTable.addCell(cell3);
                        
                        PdfPCell cell4 = new PdfPCell(new Paragraph(letterGrade));
                        cell4.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        semesterTable.addCell(cell4);
                        
                        PdfPCell cell5 = new PdfPCell(new Paragraph(String.format("%.2f", gpa)));
                        cell5.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
                        semesterTable.addCell(cell5);
                        
                        // Calculate quality points for GPA calculation (credit * gpa)
                        semesterQualityPoints += courseCredit * gpa;
                        semesterCreditHours += courseCredit;
                    }
                    
                    document.add(semesterTable);
                    
                    // Calculate and display semester credit hours and GPA
                    document.add(new Paragraph(" "));
                    document.add(new Paragraph("Total Credit Hours: " + semesterCreditHours));
                    
                    if (semesterCreditHours > 0) {
                        double semesterGPA = semesterQualityPoints / semesterCreditHours;
                        document.add(new Paragraph("GPA: " + String.format("%.2f", semesterGPA)));
                    }
                    
                    document.add(new Paragraph(" ")); // Space after semester section
                }
            }
            
            // Calculate and display overall year credit hours and cumulative GPA
            List<Grades> allYearGrades = new ArrayList<>();
            for (Grades grade : database.getGradeDB().values()) {
                if (grade.getStudentID().equals(studentId) && 
                    grade.getSemester() >= startSemester && 
                    grade.getSemester() <= endSemester) {
                    
                    // Get and set the course object for proper GPA calculation
                    Course course = database.getCourse(grade.getCourseID());
                    if (course != null) {
                        grade.setCourseObject(course);
                    }
                    allYearGrades.add(grade);
                }
            }
            
            double totalQualityPoints = 0.0;
            int totalCreditHours = 0;
            
            for (Grades grade : allYearGrades) {
                Course course = database.getCourse(grade.getCourseID());
                int courseCredit = course.getCredit();
                
                double gpa = grade.calculateGPA();
                totalQualityPoints += courseCredit * gpa;
                totalCreditHours += courseCredit;
            }
            
            document.add(new Paragraph(" "));
            
            if (totalCreditHours > 0) {
                double cumulativeGPA = totalQualityPoints / totalCreditHours;
                document.add(new Paragraph("Cumulative GPA: " + String.format("%.2f", cumulativeGPA)));
            }
            
            document.close();
            System.out.println("Yearly report generated successfully at: " + this.filePath);
            
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
