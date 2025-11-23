package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class AcademicOfficer extends User {
    private static final long serialVersionUID = 1L;
    private RecoveryPlanManager recPlanManager;
    private Database database;
    private String department;
    private String officeLocation;

    // constructors
    public AcademicOfficer() {
        super();
        setRole(UserRole.ACADEMIC_OFFICER);
        this.department = "";
        this.officeLocation = "";
    }

    public AcademicOfficer(String userID,
                           String username,
                           String password,
                           String fullName,
                           String email) {
        this(userID, username, password, fullName, email, "", "");
    }

    public AcademicOfficer(String userID,
                           String username,
                           String password,
                           String fullName,
                           String email,
                           String department,
                           String officeLocation) {
        super(userID, username, password, fullName, email, UserRole.ACADEMIC_OFFICER);
        this.department = department;
        this.officeLocation = officeLocation;
    }

    public AcademicOfficer(String userID, Database database, RecoveryPlanManager recPlanManager) {
        this.userID = userID;
        this.recPlanManager = recPlanManager;
        this.database = database;
    }

    // Getters
    public String getDepartment() {
        return department;
    }
    public String getOfficeLocation() {
        return officeLocation;
    }

    // Setters
    public void setDepartment(String department) {
        this.department = department;
    }
    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    // Methods
    public void searchStudent() {          // Search students and list failed components
        int failedCourseCount = 0;
        String targetStudentID = InputValidation.validateStudentID(database);
        Student student = database.getStudent(targetStudentID);         // student found, fetching the student object
        System.out.println();
        System.out.println("----------------------");
        System.out.println("Student ID: " + student.getStudentID());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Major: " + student.getMajor());
        System.out.println("----------------------");
        System.out.println("Failed Modules: ");
        System.out.println();
        ArrayList<Grades> targetStudentGrades = database.getStudentAllGrades(targetStudentID);
        for (Grades grade : targetStudentGrades) {
            Course course = database.getCourse(grade.getCourseID());
            grade.setCourseObject(course);
            if (grade.calculateGPA() < 2.0) {
                failedCourseCount++;
                System.out.println(failedCourseCount + ". " + course.getCourseName() + "-" + course.getCourseID());
                System.out.println("   GPA: " + grade.calculateGPA());
            }
        }
        if (failedCourseCount == 0) {
            System.out.println("Student has no failed modules.");
        }
    }

    public void addRecoveryPlan() {        // Add Recovery Plan
        System.out.println("---   Add Recovery Plan   ---");
        String targetStudentID = InputValidation.validateStudentID(database);             // Ask user for student ID to add recovery plan ( First Menu )
        recPlanManager.addPlan(targetStudentID,userID);
    }

    public void deleteRecoveryPlan() {
        System.out.println("---   Delete Recovery Plan   ---");
        String targetStudentID = InputValidation.validateStudentID(database);           // Ask user for student ID ( First Menu )
        recPlanManager.deletePlan(targetStudentID);
    }

    public void updateRecoveryPlan() {
        System.out.println("--- Update Recovery Task ---");
        String targetPlanID = InputValidation.validatePlanID(database);
        recPlanManager.updatePlan(targetPlanID);
    }

    public void addRecoveryTask() {
        System.out.println("--- Add Recovery Task ---");
        String targetPlanID = InputValidation.validatePlanID(database);
        recPlanManager.addTask(targetPlanID);
    }

    public void deleteRecoveryTask() {
        System.out.println("--- Delete Recovery Task ---");
        String targetPlanID = InputValidation.validatePlanID(database);
        recPlanManager.deleteTask(targetPlanID);
    }

    public void  monitorRecoveryPlan() {
        System.out.println("---   Monitor Recovery Plan   ---");
        String targetStudentID = InputValidation.validateStudentID(database);       // Ask user for Student ID and check existence
        recPlanManager.monitorPlan(targetStudentID);
    }

    public void showRecoveryPlanMenu() {
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. Update Recovery Plan");
        System.out.println("3. Delete Recovery Plan");
        System.out.println("4. Monitor Recovery Plan");
        System.out.println("5. Back");
    }
    public void showRecoveryTaskMenu() {
        System.out.println("1. Add Recovery Task");
        System.out.println("2. Delete Recovery Task");
        System.out.println("3. Back");
    }
    @Override
    public void showMenu() {
        System.out.println("1. Search Student");
        System.out.println("2. Recovery Plans");
        System.out.println("3. Recovery Tasks");
        System.out.println("4. Exit");
    }

    @Override
    public String getPermissions() {
        return "Full access to student records, course recovery plans, "
                + "eligibility checks, enrollment management, and academic reporting";
    }

    @Override
    public String toFileString() {
        return super.toFileString() + "," + department + "," + officeLocation;
    }

    @Override
    public String toString() {
        return String.format("AcademicOfficer[ID=%s, Username=%s, Name=%s, Department=%s]",
                getUserID(), getUsername(), getFullName(), department);
    }
}
