package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class AcademicOfficer extends User {
    private static final long serialVersionUID = 1L;

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
    public void searchStudent(Database database) {          // Search students and list failed components
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



    public void addRecoveryPlan(Database database) {        // Add Recovery Plan
        int failedCourseCount = 0;
        Course selectedCourse;
        String targetStudentID = InputValidation.validateStudentID(database);                   // Ask user for student ID to add recovery plan ( First Menu )
        ArrayList<Grades> targetStudentGrades = database.getStudentAllGrades(targetStudentID);  // Student found, finding all grades for the student
        ArrayList<Course> failedCourses = new ArrayList<>();
        // Displaying failed modules of student
        System.out.println();
        System.out.println("Failed Modules for Student " + targetStudentID);
        for (Grades grade : targetStudentGrades) {
            Course course = database.getCourse(grade.getCourseID());
            grade.setCourseObject(course);
            if (grade.calculateGPA() < 2.0) {
                failedCourseCount++;
                System.out.println(failedCourseCount + ". " + course.getCourseName() + "-" + course.getCourseID() + "    GPA: " + grade.calculateGPA());
                failedCourses.add(course);
            }
        }
        if (failedCourses.isEmpty()) {
            System.out.println("Error. Student has no failed modules.");
            return;
        }
        ArrayList<RecoveryPlan> studentExistingPlans = database.getStudentRecoveryPlan(targetStudentID);

        // Ask user to pick which course to add recovery plan ( Second Menu )
        System.out.println();
        System.out.println("Please choose course to add Recovery Plan.");
        int courseSelection = InputValidation.readInt(">>>   ",1,failedCourses.size());
        selectedCourse = failedCourses.get(courseSelection-1);

        for (RecoveryPlan plan : studentExistingPlans) {
            if (plan.getStudentID().equals(targetStudentID) && plan.getCourseID().equals(selectedCourse.getCourseID())) {
                System.out.println("Error. Student '" + targetStudentID +"' already has a recovery plan for this course.");
                return;
            }
        }
        IDManager idManager = new IDManager(database.getRecPlanDB());       // Generate new Plan ID
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();

        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, selectedCourse.getCourseID(), userID, "0.00");
        database.addRecoveryPlan(newPlan);
        newPlan.addNewTask(database);
        System.out.println();
        System.out.println("Recovery Plan and Task successfully added for Student " + targetStudentID + ".");
    }


    public void deleteRecoveryPlan(Database database) {
        int number = 1;

        String targetStudentID = InputValidation.validateStudentID(database);           // Ask user for student ID ( First Menu )
        ArrayList<RecoveryPlan> studentPlan = database.getStudentRecoveryPlan(targetStudentID);
        int planCount = studentPlan.size();
        if (planCount == 0) {
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
            return;
        }
        // Displaying the RecoveryPlans for student
        System.out.println("Recovery Plans for Student " + targetStudentID);
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID());
            number += 1;
        }
        System.out.println("Choose PlanID to delete");

        int planSelection = InputValidation.readInt(">>>   ",1,studentPlan.size());
        String planID = studentPlan.get(planSelection-1).getPlanID();
        database.removeRecoveryPlan(planID);
        database.removeAllRecoveryTask(planID);
        System.out.println("Plan " + planID + " has been successfully deleted.");
    }

    public void updateRecoveryPlan(Database database) {
        System.out.println("--- Update Recovery Task ---");
        String targetPlanID = InputValidation.validatePlanID(database);
        RecoveryPlan targetPlan = database.getRecoveryPlan(targetPlanID);
        targetPlan.updateRecoveryTask(database);
        database.updatePlanProgress(targetPlanID);
    }

    public void addRecoveryTask(Database database) {
        System.out.println("--- Add Recovery Task ---");
        String targetPlanID = InputValidation.validatePlanID(database);         // Get user input for PlanID and validate existence
        RecoveryPlan targetPlan = database.getRecoveryPlan(targetPlanID);                             // Create new RecoveryTask object
        targetPlan.addNewTask(database);
        database.updatePlanProgress(targetPlanID);
    }

    public void deleteRecoveryTask(Database database) {
        System.out.println("--- Delete Recovery Task ---");
        String targetPlanID= InputValidation.validatePlanID(database);
        RecoveryPlan targetPlan = database.getRecoveryPlan(targetPlanID);
        targetPlan.deleteRecoveryTask(database);
        database.updatePlanProgress(targetPlanID);
    }

    public void  monitorRecoveryPlan(Database database) {
        int planCount = 1, taskCount = 1;
        System.out.println("--- Monitor Recovery Plan --- ");
        String targetStudentID = InputValidation.validateStudentID(database);       // Ask user for Student ID and check existence

        System.out.println("Showing Recovery Plans for Student '" + targetStudentID +  "' :");
        System.out.println("---------------------");
        // Display recovery plans registered under student
        ArrayList<RecoveryPlan> targetStudentPlans = database.getStudentRecoveryPlan(targetStudentID);
         for (RecoveryPlan plan : targetStudentPlans) {
            System.out.println(planCount+". " + plan.getPlanID() + "      CourseID: " + plan.getCourseID()+ "        Progress: " + plan.getProgress());
        }
         int planSelection = InputValidation.readInt(">>>   ",1,targetStudentPlans.size());
         RecoveryPlan targetPlan = targetStudentPlans.get(planSelection-1);

        System.out.println("Showing Recovery Task for Student '" + targetPlan.getPlanID() +  "' :");
        System.out.println("---------------------");
         // Display recovery tasks registered under the recovery plan
         ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(targetPlan.getPlanID());

         for (RecoveryTask task : planTasks) {
             System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
             System.out.println("Task Description: " + task.getDescription());
             System.out.println();
             taskCount++;
         }
         System.out.println("Overall Recovery Plan Progress: " + targetPlan.getProgress());
    }
    @Override
    public void showMenu() {
        System.out.println("------------------------------");
        System.out.println("1. Search Student and show Failed Components");
        System.out.println("2. Add Recovery Plan");
        System.out.println("3. Delete Recovery Plan");
        System.out.println("4. Update Recovery Plan");
        System.out.println("5. Add Recovery Task");
        System.out.println("6. Delete Recovery Task");
        System.out.println("7. Monitor Recovery Plan");
        System.out.println("8. Exit");
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
