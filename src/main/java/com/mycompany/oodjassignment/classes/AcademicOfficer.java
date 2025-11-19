package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;


import java.util.HashMap;
import java.util.ArrayList;
import java.util.Scanner;

public class AcademicOfficer extends User {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }
    // adding new recovery plan after checking existence of user ID
    public HashMap<String, RecoveryPlan> addRecoveryPlan(HashMap<String, RecoveryPlan> recPlanDB, HashMap<String, Student> studentDB, HashMap<String, RecoveryTask> recTaskDB) {
        Scanner userInput = new Scanner(System.in);
        StudentManager checkInput = new StudentManager();
        String targetStudentID;
        boolean studentFound;

        do {
            System.out.print("Please enter student ID: ");
            targetStudentID = userInput.nextLine();
            studentFound = checkInput.validateStudentID(targetStudentID,studentDB);

            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);
        IDManager IDManager = new IDManager();
        IDManager.getHighestTaskID(recPlanDB);
        String nextPlanID = "P"+IDManager.generateNewID();
        RecoveryPlan newPlan = new RecoveryPlan(nextPlanID,targetStudentID,userID,"0.00");
        recPlanDB.put(nextPlanID,newPlan);
        newPlan.addRecoveryTask(recTaskDB);
        return recPlanDB;
    }

    // view all recovery plans ( will be changed to single search later on )
    public void viewRecoveryPlan(HashMap<String, RecoveryPlan> recPlanDB) {
        System.out.println("PlanID   StudentID   Created By   Progress");
        for (RecoveryPlan plan : recPlanDB.values()) {
            System.out.println(plan.getPlanID()+"   "+plan.getStudentID()+"   "+plan.getCreatedBy()+"   "+plan.getProgress());
        }
    }

    // delete recovery plan
    public HashMap<String, RecoveryPlan> deleteRecoveryPlan(HashMap<String, RecoveryPlan> recPlanDB, HashMap<String, Student> studentDB) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;
        boolean studentFound, planDelete = false;
        int planSelection;

        do {                // Finding if student exists in student Database
            System.out.print("Please enter student ID: ");
            targetStudentID = userInput.nextLine();
            studentFound = StudentManager.validateStudentID(targetStudentID,studentDB);
            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the Database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);

        ArrayList<String> studentPlanID = StudentManager.validateStudentRecoveryPlan(targetStudentID,recPlanDB);
        int planCount = StudentManager.getRecoveryPlanCount(targetStudentID, recPlanDB);

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
                    recPlanDB.remove(studentPlanID.get(listIndex));
                    planDelete = true;
                }
            } while (!planDelete);
        }
        return recPlanDB;
    }
    public ArrayList<String> getFailedStudents(HashMap<String,Student> studentDB, HashMap<String,Grades> gradeDB, HashMap<String, Course> courseDB) {
        ArrayList<String> failedStudentsList = new ArrayList<>();

        return failedStudentsList;
    }
    @Override
    public void showMenu() {
        System.out.println("Logged in as " + this.getRole() + " with user ID: " + this.getUserID());
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. View All Recovery Plans");
        System.out.println("3. Delete Recovery Plans");
        System.out.println("4. Exit");
    }
}
