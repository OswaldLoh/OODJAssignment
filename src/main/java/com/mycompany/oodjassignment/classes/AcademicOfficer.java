package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.IDManager;
import com.mycompany.oodjassignment.functions.InputValidation;

import java.util.ArrayList;
import java.util.Scanner;

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

        // Ask user for student ID to add recovery plan ( First Menu )
        String targetStudentID = InputValidation.validateStudentID(database);

        // Student found, finding all grades for the student
        ArrayList<Grades> targetStudentGrades = database.getStudentAllGrades(targetStudentID);
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

        // Ask user to pick which course to add recovery plan ( Second Menu )
        System.out.println();
        System.out.println("Please choose course to add Recovery Plan.");

        int courseSelection = InputValidation.readInt(">>>   ",1,failedCourses.size());
        selectedCourse = failedCourses.get(courseSelection-1);

        // Generate new Plan ID
        IDManager idManager = new IDManager(database.getRecPlanDB());
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();

        Grades targetGrade = database.getGrades(targetStudentID, selectedCourse.getCourseID());
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, selectedCourse.getCourseID(), userID, "0.00");
        RecoveryTask newTask = newPlan.addNewTask(targetGrade, database);
        database.addRecoveryPlan(newPlan);
        database.addRecoveryTask(newTask);
        System.out.println();
        System.out.println("Recovery Plan and Task successfully added for Student " + targetStudentID + ".");
    }

    public void deleteRecoveryPlan(Database database) {
        int number = 1;

        // Ask user for student ID ( First Menu )
        String targetStudentID = InputValidation.validateStudentID(database);
        Student student = database.getStudent(targetStudentID);         // student found, fetching the student object

        ArrayList<RecoveryPlan> studentPlan = database.findStudentRecoveryPlan(targetStudentID);
        int planCount = studentPlan.size();

        if (planCount == 0) {
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
            return;
        }

        System.out.println("Recovery Plans for Student " + targetStudentID);
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID());
            number += 1;
        }
        System.out.println("Choose PlanID to delete");

        int planSelection = InputValidation.readInt(">>>   ",1,studentPlan.size());
        String planID = studentPlan.get(planSelection-1).getPlanID();
        database.removeRecoveryPlan(planID);
        database.removeRecoveryTask(planID);
        System.out.println("Plan " + planID + " has been successfully deleted.");
    }

    public void updateRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        boolean taskUpdate = false;
        int number = 1, planCount;

        String targetStudentID = InputValidation.validateStudentID(database);

        ArrayList<RecoveryPlan> studentPlan = database.findStudentRecoveryPlan(targetStudentID);
        planCount = studentPlan.size();
        if (planCount == 0) {                               // if student has no recovery plans registered
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
        }

        System.out.println("Recovery Plans for Student " + targetStudentID);    // displaying recovery plans registered under student
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID() + "     " + "Progress: " + plan.getProgress());
            number += 1;
        }

        int planSelection = InputValidation.readInt("Choose PlanID to update progress level: ",1,studentPlan.size());
        String planID = studentPlan.get(planSelection-1).getPlanID();
        ArrayList<RecoveryTask> planTasks = database.findPlanRecoveryTask(planID);
        System.out.println("Recovery Tasks");
        for (RecoveryTask task : planTasks) {
            System.out.println(task.getTaskID() + "    " + task.getDescription() + "     Completion: " + task.getCompletion());
        }

        do {                                                   // second menu loop asking for task ID
            String targetTaskID = InputValidation.readString("Enter TaskID: ");
            if (planTasks.contains(database.getRecoveryTask(targetTaskID))) {       // if the input is valid and is registered under the PlanID
                System.out.println("Please update completion");
                System.out.println("1. Completed");
                System.out.println("2. Incomplete");
                int completeSelection = InputValidation.readInt(">>>   ",1,2);
                if (completeSelection == 1) {
                    database.getRecoveryTask(targetTaskID).setCompletion(true);
                } else if (completeSelection == 2) {
                    database.getRecoveryTask(targetTaskID).setCompletion(false);
                }
                taskUpdate = true;
            } else {                                            // if taskID input is invalid
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            }
        } while (!taskUpdate);
        database.updatePlanProgress(planID);
    }
    public void addRecoveryTask(Database database) {
        System.out.println();
        System.out.println("Adding new Recovery Task.");

        String targetPlanID = InputValidation.validatePlanID(database);         // Get user input for PlanID and validate existence

        RecoveryTask newTask = new RecoveryTask();                              // Create new RecoveryTask object

        IDManager recTaskIDManager = new IDManager(database.getRecTaskDB());        // Generate new TaskID
        recTaskIDManager.getHighestTaskID();


        String description = InputValidation.readString("Task Description: ");         // Prompt for new task description
        newTask.setDescription(description);
        int duration = InputValidation.readInt("Duration (days): ");              // Prompt for new task duration
        newTask.setDuration(duration);

        newTask.setTaskID("T" + recTaskIDManager.generateNewID());
        newTask.setPlanID(targetPlanID);
        newTask.setCompletion(false);

        database.addRecoveryTask(newTask);
        database.updatePlanProgress(targetPlanID);
    }
    @Override
    public void showMenu() {
        System.out.println("------------------------------");
        System.out.println("1. Search Student and show Failed Components");
        System.out.println("2. Add Recovery Plan");
        System.out.println("3. Delete Recovery Plan");
        System.out.println("4. Update Recovery Plan");
        System.out.println("5. Add Recovery Task");
        System.out.println("6. Exit");
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
