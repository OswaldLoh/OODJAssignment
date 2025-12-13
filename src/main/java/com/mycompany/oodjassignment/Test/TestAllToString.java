package com.mycompany.oodjassignment.Test;

import java.time.LocalDateTime;

import com.mycompany.oodjassignment.Entities.AcademicOfficer;
import com.mycompany.oodjassignment.Entities.Course;
import com.mycompany.oodjassignment.Entities.CourseAdministrator;
import com.mycompany.oodjassignment.Entities.Grades;
import com.mycompany.oodjassignment.Entities.RecoveryPlan;
import com.mycompany.oodjassignment.Entities.RecoveryTask;
import com.mycompany.oodjassignment.Entities.Student;
import com.mycompany.oodjassignment.Enums.UserRole;
import com.mycompany.oodjassignment.Enums.UserStatus;
import com.mycompany.oodjassignment.Helpers.LoginLog;
import com.mycompany.oodjassignment.Helpers.ReportGenerator;
import com.mycompany.oodjassignment.Helpers.SendEmail;

/**
 * Testing file to print the toString methods for all classes in the OODJAssignment project
 */
public class Test {

    public static void main(String[] args) {
        System.out.println("=== Testing toString methods for all classes ===\n");

        // Test AcademicOfficer class (toString inherited from User parent class)
        System.out.println("--- Testing AcademicOfficer ---");
        AcademicOfficer academicOfficer = new AcademicOfficer(
            "AO001", 
            "ao_admin", 
            "password123", 
            "John Doe", 
            "john.doe@university.edu", 
            "Computer Science", 
            "Room 205"
        );
        System.out.println(academicOfficer.toString());
        System.out.println();

        // Test CourseAdministrator class (toString inherited from User parent class)
        System.out.println("--- Testing CourseAdministrator ---");
        CourseAdministrator courseAdmin = new CourseAdministrator(
            "CA001", 
            "ca_admin", 
            "password456", 
            "Jane Smith", 
            "jane.smith@university.edu", 
            "CS101,CS201", 
            "Computer Science"
        );
        System.out.println(courseAdmin.toString());
        System.out.println();

        // Test Student class (no custom toString method, will use default Object.toString())
        System.out.println("--- Testing Student ---");
        Student student = new Student("S001", "Alice", "Johnson", "Computer Science", "2", "alice.johnson@university.edu");
        System.out.println("Student (default toString): " + student.toString());
        System.out.println("Student toCSV: " + student.toCSV());
        System.out.println();

        // Test Course class (no custom toString method, will use default Object.toString())
        System.out.println("--- Testing Course ---");
        Course course = new Course("CS101", "Introduction to Programming", 3, "Semester 1", "Dr. Wilson", 60, 40);
        System.out.println("Course (default toString): " + course.toString());
        System.out.println("Course toCSV: " + course.toCSV());
        System.out.println();

        // Test Grades class (no custom toString method, will use default Object.toString())
        System.out.println("--- Testing Grades ---");
        Grades grades = new Grades("G001", "S001", "CS101", 1, 1, 75, 80);
        System.out.println("Grades (default toString): " + grades.toString());
        System.out.println("Grades toCSV: " + grades.toCSV());
        grades.setCourseObject(course); // Set course for calculating GPA
        System.out.println("Grades calculated GPA: " + grades.calculateGPA());
        System.out.println("Grades letter grade: " + grades.getLetterGrade());
        System.out.println();

        // Test RecoveryPlan class (no custom toString method, will use default Object.toString())
        System.out.println("--- Testing RecoveryPlan ---");
        RecoveryPlan recoveryPlan = new RecoveryPlan("RP001", "S001", "CS101", "Exam", "AO001", "25.0");
        System.out.println("RecoveryPlan (default toString): " + recoveryPlan.toString());
        System.out.println("RecoveryPlan toCSV: " + recoveryPlan.toCSV());
        System.out.println();

        // Test RecoveryTask class (no custom toString method, will use default Object.toString())
        System.out.println("--- Testing RecoveryTask ---");
        RecoveryTask recoveryTask = new RecoveryTask("RT001", "RP001", "Complete Chapter 1", 1, false);
        System.out.println("RecoveryTask (default toString): " + recoveryTask.toString());
        System.out.println("RecoveryTask toCSV: " + recoveryTask.toCSV());
        System.out.println();

        // Test LoginLog class (has custom toString method)
        System.out.println("--- Testing LoginLog ---");
        LoginLog loginLog = new LoginLog("user123", "LOGIN", LocalDateTime.now(), true, "192.168.1.100");
        System.out.println(loginLog.toString());
        System.out.println();

        // Test UserRole enum
        System.out.println("--- Testing UserRole Enum ---");
        System.out.println("ACADEMIC_OFFICER: " + UserRole.ACADEMIC_OFFICER);
        System.out.println("COURSE_ADMINISTRATOR: " + UserRole.COURSE_ADMINISTRATOR);
        System.out.println();

        // Test UserStatus enum
        System.out.println("--- Testing UserStatus Enum ---");
        System.out.println("ACTIVE: " + UserStatus.ACTIVE);
        System.out.println("INACTIVE: " + UserStatus.INACTIVE);
        System.out.println("DEACTIVATED: " + UserStatus.DEACTIVATED);
        System.out.println();

        // Test CSVParser interface implementations mentioned in the project
        System.out.println("--- Testing CSVParser Interface ---");
        System.out.println("CSVParser is implemented by:");
        System.out.println("- Student: " + Student.class.getSimpleName());
        System.out.println("- Course: " + Course.class.getSimpleName());
        System.out.println("- Grades: " + Grades.class.getSimpleName());
        System.out.println("- RecoveryPlan: " + RecoveryPlan.class.getSimpleName());
        System.out.println("- RecoveryTask: " + RecoveryTask.class.getSimpleName());
        System.out.println();

        // Test send email method in User class (inherited by AcademicOfficer and CourseAdministrator)
        System.out.println("--- Testing sendEmail method ---");
        SendEmail sendEmail = new SendEmail("hello@hmail.com");
        System.out.println(sendEmail.toString());
        System.out.println();

        // Report Generator testing
        System.out.println("--- Report Generator Testing ---");
        ReportGenerator reportGenerator = new ReportGenerator("D:");
        System.out.println(reportGenerator.toString());
        System.err.println(" ");
        
        System.out.println("=== Testing completed ===");
    }
}