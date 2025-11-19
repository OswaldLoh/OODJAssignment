package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class AcademicOfficer extends User {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }
    public void searchStudent(Database database) {
        Scanner userInput = new Scanner(System.in);
        boolean studentFound = false;
        Student student;
        do {
            System.out.print("Please enter Student ID: ");
            String targetStudentID = userInput.nextLine();
            student = database.getStudent(targetStudentID);
            if (student != null) {
                studentFound = true;
            }
            if (!studentFound) {
                System.out.println("Student is not found inside database. Please try again.");
            }
        } while (!studentFound);
        System.out.println(student.getLastName());
    }

    // Adding new recovery plan after checking existence of user ID ( Fully Working )
    public void addRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        int failStudentCount = 0, studentSelection;
        boolean studentFound = false, courseFound;
        String targetStudentID = "", targetCourseID;
        ArrayList<Student> failedStudents;

        // parsing failed students object into array list
        do {
            System.out.print("Please enter Course ID: ");
            targetCourseID = userInput.nextLine();
            failedStudents = database.getFailedStudents(targetCourseID);
            if (failedStudents == null || failedStudents.isEmpty()) {
                System.out.println("Error. Course ID " + targetCourseID + " is not found inside database, or may not have any failing students. Please try again.");
                System.out.println();
                courseFound = false;
            } else {
                courseFound = true;
            }
        } while (!courseFound);

        // Displaying failed students for the user by iterating through the array list
        for (Student student : failedStudents) {
            failStudentCount++;
            System.out.println(failStudentCount+". "+student.getStudentID()+" "+student.getFirstName()+" "+student.getLastName());
        }

        // GUI based selection by indexing through array list of failed students
        do {
            System.out.print(">>>   ");
            studentSelection = userInput.nextInt();
            if (studentSelection <= 0 || studentSelection > failedStudents.size()) {
                System.out.println("Invalid selection. Please try again.");
                System.out.println();
            } else {
                final int listIndex = studentSelection - 1;
                targetStudentID = failedStudents.get(listIndex).getStudentID();
                studentFound = true;
            }
        } while (!studentFound);

        // Create an instance of IDManager to generate next ID for PlanID
        IDManager IDManager = new IDManager();
        IDManager.getHighestTaskID(database.getRecPlanDB());
        String nextPlanID = "P"+IDManager.generateNewID();

        // make new RecoveryPlan object
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID,targetStudentID,userID,"0.00");
        RecoveryTask newTask = newPlan.addNewTask(database);     // Call instance to create RecoveryTask
        database.addRecoveryPlan(newPlan);
        database.addRecoveryTask(newTask);
    }


    // view all recovery plans ( will be changed to single search later on )
    public void viewRecoveryPlan(HashMap<String, RecoveryPlan> recPlanDB) {
        System.out.println("PlanID   StudentID   Created By   Progress");
        for (RecoveryPlan plan : recPlanDB.values()) {
            System.out.println(plan.getPlanID()+"   "+plan.getStudentID()+"   "+plan.getCreatedBy()+"   "+plan.getProgress());
        }
    }

    // delete recovery plan
    public void deleteRecoveryPlan(Database database) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;
        boolean studentFound = false, planDelete = false;
        int planSelection;
        Student student;

        do {
            System.out.print("Please enter Student ID: ");
            targetStudentID = userInput.nextLine();
            student = database.getStudent(targetStudentID);
            if (student != null) {
                studentFound = true;
            }
            if (!studentFound) {

            }
        } while (!studentFound);

        ArrayList<String> studentPlanID = database.findStudentRecoveryPlan(targetStudentID);
        int planCount = database.getStudentRecoveryPlanCount(targetStudentID);

        if (planCount == 0) {               // no recovery plan is found for student
            System.out.println("Error. Student " + targetStudentID + " has no recovery plans.");
        } else {                            // student has more than 1 or more recovery plans
            int number = 1;
            System.out.println("Recovery Plans for Student " + targetStudentID);
            for (String planID : studentPlanID) {
                System.out.println(number+". "+planID);
                number +=1;
            }
            System.out.println("Choose PlanID to delete");
            do {
                System.out.print(">>>   ");
                planSelection = userInput.nextInt();
                if (planSelection <= 0 || planSelection > studentPlanID.size()) {
                    System.out.println("Invalid selection. Please try again.");
                    System.out.println();
                } else {
                    final int listIndex = planSelection - 1;
                    database.removeRecoveryPlan(studentPlanID.get(listIndex));
                    planDelete = true;
                }
            } while (!planDelete);
        }
    }

    @Override
    public void showMenu() {
        System.out.println("Logged in as " + this.getRole() + " with user ID: " + this.getUserID());
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. View All Recovery Plans");
        System.out.println("3. Delete Recovery Plans");
        System.out.println("4. Search student and show failed components");
        System.out.println("4. Exit");
    }
}
