package com.mycompany.oodjassignment.Eligibility;


import java.util.Map;
import java.util.Scanner;
import java.util.List;

/**
 * Console-only test runner for the Eligibility & Enrolment module.
 * This version allows you to run and test all logic WITHOUT any GUI.
 *
 * It loads CSV files, calculates CGPA, checks eligibility, and allows
 * manual enrolment through console input.
 */
public class EligibilityMain {

    public static void main(String[] args) {

        System.out.println("=== Eligibility Check & Enrolment (Console Version) ===\n");

        try {
            // Load all CSV files
            System.out.println("Loading CSV data...");

            Map<String, Student> students = CSVLoader.loadStudents("student_information.csv");
            Map<String, Course> courses = CSVLoader.loadCourses("course_assessment_information.csv");
            CSVLoader.loadGradesIntoStudents("student_grades.csv", students, courses);

            System.out.println("CSV files loaded successfully.\n");

            EligibilityChecker checker = new EligibilityChecker();
            Scanner sc = new Scanner(System.in);

            // Display all student data
            System.out.println("List of students and eligibility:");
            System.out.println("------------------------------------------");

            for (Student s : students.values()) {

                double cgpa = s.calculateCGPA();
                int failed = s.countFailedCourses();
                boolean eligible = checker.isEligible(s);

                System.out.println("Student ID: " + s.getId());
                System.out.println("Name      : " + s.getFullName());
                System.out.println("CGPA      : " + String.format("%.2f", cgpa));
                System.out.println("Failed    : " + failed);
                System.out.println("Eligible  : " + (eligible ? "YES" : "NO"));
                System.out.println("------------------------------------------");
            }

            // Optional — let the user pick a student to enrol
            while (true) {
                System.out.print("\nEnter a Student ID to enrol (or type EXIT): ");
                String input = sc.nextLine().trim();

                if (input.equalsIgnoreCase("EXIT")) {
                    System.out.println("Exiting program...");
                    break;
                }

                Student selected = students.get(input);

                if (selected == null) {
                    System.out.println("Student ID not found. Try again.");
                    continue;
                }

                double cgpa = selected.calculateCGPA();
                int failed = selected.countFailedCourses();
                boolean eligible = checker.isEligible(selected);

                System.out.println("\nSelected Student:");
                System.out.println("Name: " + selected.getFullName());
                System.out.println("CGPA: " + String.format("%.2f", cgpa));
                System.out.println("Failed Courses: " + failed);
                System.out.println("Eligible: " + (eligible ? "YES" : "NO"));

                if (!eligible) {
                    System.out.println("This student is NOT eligible. Cannot enrol.");
                    continue;
                }

                // Save enrolment record
                EnrolmentRecord record = new EnrolmentRecord(selected, cgpa, failed, true);
                EnrolmentFileManager.appendEnrolment(record);

                System.out.println("Enrolment successful and saved to enrolments.dat");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
