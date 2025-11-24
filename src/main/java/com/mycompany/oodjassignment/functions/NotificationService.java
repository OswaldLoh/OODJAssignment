package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.Student;
import javax.swing.*;
import java.io.File;

public class NotificationService {
    
    private final Database database;
    private final SendEmail sendEmail;
    
    public NotificationService(Database database) {
        this.database = database;
        this.sendEmail = new SendEmail("");
    }
    
    /**
     * Send notification with PDF attachment to a specific student
     * @param studentId The ID of the student to send notification to
     * @param subject The subject of the email
     * @param content The content/body of the email
     * @param pdfFilePath The path to the PDF file to attach
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendNotificationToStudent(String studentId, String subject, String content, String pdfFilePath) {
        // Get student email from database
        Student student = database.getStudent(studentId);
        if (student == null || student.getEmail() == null || student.getEmail().isEmpty()) {
            System.out.println("Student not found or email not available for student ID: " + studentId);
            return false;
        }
        
        String studentEmail = student.getEmail();
        sendEmail.setRecipientEmail(studentEmail);
        
        // Check if PDF file exists before sending
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            System.out.println("PDF file does not exist: " + pdfFilePath);
            return false;
        }
        
        try {
            sendEmail.Pdf(subject, content, pdfFilePath);
            System.out.println("Notification sent successfully to: " + studentEmail);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to send notification to: " + studentEmail);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Send notification with PDF attachment to academic officer
     * @param academicOfficerEmail The email of the academic officer
     * @param subject The subject of the email
     * @param content The content/body of the email
     * @param pdfFilePath The path to the PDF file to attach
     * @return true if email was sent successfully, false otherwise
     */
    public boolean sendNotificationToAcademicOfficer(String academicOfficerEmail, String subject, String content, String pdfFilePath) {
        sendEmail.setRecipientEmail(academicOfficerEmail);
        
        // Check if PDF file exists before sending
        File pdfFile = new File(pdfFilePath);
        if (!pdfFile.exists()) {
            System.out.println("PDF file does not exist: " + pdfFilePath);
            return false;
        }
        
        try {
            sendEmail.Pdf(subject, content, pdfFilePath);
            System.out.println("Notification sent successfully to: " + academicOfficerEmail);
            return true;
        } catch (Exception e) {
            System.out.println("Failed to send notification to: " + academicOfficerEmail);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Generate and send semester report as notification to a student
     * @param studentId The ID of the student
     * @param semester The semester number
     * @param subject Email subject
     * @param content Email content
     * @return true if successful, false otherwise
     */
    public boolean sendSemesterReportNotification(String studentId, int semester, String subject, String content) {
        String pdfFilePath = "data/student_report/student_report_" + studentId + "_sem" + semester + ".pdf";
        
        // Generate the report first
        ReportGenerator reportGenerator = new ReportGenerator("REPORT_" + studentId, pdfFilePath);
        reportGenerator.generateReport(studentId, semester);
        
        // Send the notification with the generated report
        return sendNotificationToStudent(studentId, subject, content, pdfFilePath);
    }
    
    /**
     * Generate and send yearly report as notification to a student
     * @param studentId The ID of the student
     * @param year The academic year
     * @param subject Email subject
     * @param content Email content
     * @return true if successful, false otherwise
     */
    public boolean sendYearlyReportNotification(String studentId, int year, String subject, String content) {
        String pdfFilePath = "data/student_report/student_yearly_report_" + studentId + "_year" + year + ".pdf";
        
        // Generate the report first
        ReportGenerator reportGenerator = new ReportGenerator("REPORT_" + studentId, pdfFilePath);
        reportGenerator.generateYearlyReport(studentId, year);
        
        // Send the notification with the generated report
        return sendNotificationToStudent(studentId, subject, content, pdfFilePath);
    }
    
    /**
     * Generate semester report and return the file path
     * @param studentId The ID of the student
     * @param semester The semester number
     * @return The file path of the generated report
     */
    public String generateSemesterReportPath(String studentId, int semester) {
        return "data/student_report/student_report_" + studentId + "_sem" + semester + ".pdf";
    }
    
    /**
     * Generate yearly report and return the file path
     * @param studentId The ID of the student
     * @param year The academic year
     * @return The file path of the generated report
     */
    public String generateYearlyReportPath(String studentId, int year) {
        return "data/student_report/student_yearly_report_" + studentId + "_year" + year + ".pdf";
    }
    
    /**
     * Show error message dialog
     * @param parent The parent component for the dialog
     * @param message The error message to display
     */
    public static void showErrorMessage(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Show success message dialog
     * @param parent The parent component for the dialog
     * @param message The success message to display
     */
    public static void showSuccessMessage(JComponent parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}