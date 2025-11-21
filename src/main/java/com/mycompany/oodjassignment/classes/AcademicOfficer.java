package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.IDManager;
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
        Scanner userInput = new Scanner(System.in);
        int courseCount = 0;
        Student student;
        String targetStudentID;

        do {
            System.out.print("Please enter Student ID: ");      // ask user for student ID
            targetStudentID = userInput.nextLine();
            if (!database.studentExist(targetStudentID)) {      // if student doesn't exist
                System.out.println("Student is not found inside database. Please try again.");
            }
        } while (!database.studentExist(targetStudentID));

        student = database.getStudent(targetStudentID);         // student found, fetching the student object

        System.out.println();
        System.out.println("----------------------");
        System.out.println("Student ID: " + student.getStudentID());
        System.out.println("Name: " + student.getFirstName() + " " + student.getLastName());
        System.out.println("Major: " + student.getMajor());
        System.out.println("----------------------");
        System.out.println("Failed Modules: ");
        System.out.println();

        for (Grades grade : database.getGradeDB().values()) {           // for each grade object in grade database
            if (grade.getStudentID().equals(student.getStudentID())) {      // if the grade's studentID equals to our target student ID
                Course course = database.getCourse(grade.getCourseID());    // get the Course object by using the Grade object course ID
                grade.setCourseObject(course);                              // set the corresponding Course object to the Grade object
                double courseGPA = grade.calculateGPA();                    // calculate the GPA of the grade
                if (courseGPA < 2.0) {
                    courseCount++;
                    System.out.println(courseCount + ". " + course.getCourseName() + "-" + course.getCourseID());
                    System.out.println("   GPA: " + grade.calculateGPA());
                }
            }
        }
        if (courseCount == 0) {
            System.out.println("Student has no failed modules.");
        }
    }

    public void addRecoveryPlan(Database database) {        // Add Recovery Plan
        Scanner userInput = new Scanner(System.in);
        int failStudentCount = 0;
        boolean courseFound;
        String targetStudentID = "";
        String targetCourseID;
        ArrayList<Student> failedStudents;

        do {
            System.out.print("Please enter Course ID: ");
            targetCourseID = userInput.nextLine();
            failedStudents = database.getFailedStudents(targetCourseID, database);
            if (failedStudents == null || failedStudents.isEmpty()) {
                System.out.println("Error. Course ID " + targetCourseID + " is not found inside database, or may not have any failing students. Please try again.");
                System.out.println();
                courseFound = false;
            } else {
                courseFound = true;
            }
        } while (!courseFound);

        Course selectedCourse = database.getCourse(targetCourseID);
        System.out.println("Failed Students for Module: " + selectedCourse.getCourseID() + "-" + selectedCourse.getCourseName());
        for (Student student : failedStudents) {
            failStudentCount++;
            System.out.println(failStudentCount + ". " + student.getStudentID() + " " + student.getFirstName() + " " + student.getLastName());
        }

        boolean studentFound = false;
        do {
            System.out.print(">>>   ");
            int studentSelection = userInput.nextInt();
            userInput.nextLine();
            if (studentSelection <= 0 || studentSelection > failedStudents.size()) {
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            } else {
                final int listIndex = studentSelection - 1;
                targetStudentID = failedStudents.get(listIndex).getStudentID();
                studentFound = true;
            }
        } while (!studentFound);

        IDManager idManager = new IDManager(database.getRecPlanDB());
        idManager.getHighestTaskID();
        String nextPlanID = "P" + idManager.generateNewID();

        Grades targetGrade = database.getGrades(targetStudentID, targetCourseID);
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID, targetStudentID, targetCourseID, userID, "0.00");
        RecoveryTask newTask = newPlan.addNewTask(targetGrade, database);
        database.addRecoveryPlan(newPlan);
        database.addRecoveryTask(newTask);
        System.out.println();
        System.out.println("Recovery Plan and Task successfully added for Student " + targetStudentID + ".");
    }

    public void deleteRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;

        do {
            System.out.print("Please enter Student ID: ");
            targetStudentID = userInput.nextLine();
            if (!database.studentExist(targetStudentID)) {      // if student doesn't exist
                System.out.println("Student is not found inside database. Please try again.");
            }
        } while (!database.studentExist(targetStudentID));

        ArrayList<RecoveryPlan> studentPlan = database.findStudentRecoveryPlan(targetStudentID);
        int planCount = database.getStudentRecoveryPlanCount(targetStudentID);

        if (planCount == 0) {
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
            return;
        }

        int number = 1;
        System.out.println("Recovery Plans for Student " + targetStudentID);
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID());
            number += 1;
        }

        System.out.println("Choose PlanID to delete");

        boolean planDelete = false;
        do {
            System.out.print(">>>   ");
            int planSelection = userInput.nextInt();
            userInput.nextLine();
            if (planSelection <= 0 || planSelection > studentPlan.size()) {
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            } else {
                final int listIndex = planSelection - 1;
                String planID = studentPlan.get(listIndex).getPlanID();
                database.removeRecoveryPlan(planID);
                database.removeRecoverytask(planID);
                planDelete = true;
            }
        } while (!planDelete);
    }
    public void updateRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;
        boolean planUpdate = false, taskUpdate = false;
        int number = 1, planCount;
        ArrayList<RecoveryPlan> studentPlan;

        do {
            System.out.print("Please enter Student ID: ");
            targetStudentID = userInput.nextLine();
            if (!database.studentExist(targetStudentID)) {      // if student doesn't exist
                System.out.println("Student is not found inside database. Please try again.");
            }
            studentPlan = database.findStudentRecoveryPlan(targetStudentID);
            planCount = studentPlan.size();
            if (planCount == 0) {                               // if student has no recovery plans registered
                System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
            }
        } while (!database.studentExist(targetStudentID) || planCount == 0);

        System.out.println("Recovery Plans for Student " + targetStudentID);    // displaying recovery plans registered under student
        for (RecoveryPlan plan : studentPlan) {
            System.out.println(number + ". " + plan.getPlanID() + "     " + "Progress: " + plan.getProgress());
            number += 1;
        }

        System.out.println("Choose PlanID to update progress level");
        do {                                // first menu loop for choosing PlanID based on options given
            System.out.print(">>>   ");
            int planSelection = userInput.nextInt();
            userInput.nextLine();
            if (planSelection <= 0 || planSelection > studentPlan.size()) {     // if selection is out of bounds of the plan array list
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            } else {                                                            // selection is valid
                final int planListIndex = planSelection - 1;
                String planID = studentPlan.get(planListIndex).getPlanID();
                ArrayList<RecoveryTask> planTasks = database.findPlanRecoveryTask(planID);

                System.out.println();
                System.out.println("Recovery Tasks");                           // displaying the recovery tasks of the selected recovery plan
                for (RecoveryTask task : planTasks) {
                    System.out.println(task.getTaskID() + "    " + task.getDescription() + "     Completion: " + task.getCompletion());
                }

                do {                                                   // second menu loop asking for task ID
                    System.out.print("Enter TaskID: ");
                    String targetTaskID = userInput.nextLine();

                    if (planTasks.contains(database.getRecoveryTask(targetTaskID))) {       // if the input is valid and is registered under the PlanID
                        System.out.println("Please update completion");
                        System.out.println("1. Completed");
                        System.out.println("2. Incomplete");
                        boolean validCompletionAnswer = false;
                        do {                                            // ask user to modify complete / incomplete
                            System.out.print(">>>   ");
                            int completeSelection = userInput.nextInt();
                            userInput.nextLine();
                            if (completeSelection == 1) {
                                database.getRecoveryTask(targetTaskID).setCompletion(true);
                                validCompletionAnswer = true;
                            } else if (completeSelection == 2) {
                                database.getRecoveryTask(targetTaskID).setCompletion(false);
                                validCompletionAnswer = true;
                            } else {                                    // inputted completion status selection is invalid
                                System.out.println("Invalid selection. Please try again.");
                                System.out.println();
                            }
                        } while (!validCompletionAnswer);
                        taskUpdate = true;
                    } else {                                            // if taskID input is invalid
                        System.out.println("Invalid selection. Please try again.");
                        System.out.println();
                    }
                } while (!taskUpdate);
                database.updatePlanProgress(planID);
                planUpdate = true;
            }

        } while (!planUpdate);
    }
    public void addRecoveryTask(Database database) {
        Scanner userInput = new Scanner(System.in);
        String targetPlanID;
        System.out.println();
        System.out.println("Adding new Recovery Task.");
        boolean validAnswer = false;
        do {
            System.out.print("Plan ID: ");
            targetPlanID = userInput.nextLine();
            if (!database.planExist(targetPlanID)) {
                System.out.println("Invalid Plan ID entered. Please try again.");
                System.out.println();
            }
        } while (!database.planExist(targetPlanID));
        RecoveryTask newTask = new RecoveryTask();

        IDManager recTaskIDManager = new IDManager(database.getRecTaskDB());
        recTaskIDManager.getHighestTaskID();
        newTask.setTaskID("T" + recTaskIDManager.generateNewID());    // setting new TaskID
        newTask.setPlanID(targetPlanID);
        newTask.setCompletion(false);
        System.out.print("Task Description:");
        newTask.setDescription(userInput.nextLine());

        do {
            System.out.print("Duration (days): ");
            try {
                int duration = Integer.parseInt(userInput.nextLine());
                newTask.setDuration(duration);
                validAnswer = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid duration provided. Please try again.");
                System.out.println();
            }
        } while (!validAnswer);
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
