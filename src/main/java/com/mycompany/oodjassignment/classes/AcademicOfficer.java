package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.*;

import java.util.ArrayList;
import java.util.Scanner;

public class AcademicOfficer extends User {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }

    // adding new recovery plan after checking existence of user ID
    public ArrayList<RecoveryPlan> addRecoveryPlan(ArrayList<RecoveryPlan> recPlans, ArrayList<Student> studentList) {
        Scanner userInput = new Scanner(System.in);
        Validation checkInput = new Validation();
        String targetStudentID;
        boolean studentFound;
        // calculating the next planID
        String [] lastPlanID = recPlans.getLast().getPlanID().split("P");
        int nextPlanID = Integer.parseInt(lastPlanID[1]) + 1;

        do {
            System.out.print("Please enter student ID: ");
            targetStudentID = userInput.nextLine();
            studentFound = checkInput.validateStudentID(targetStudentID,studentList);

            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);

        recPlans.add(new RecoveryPlan("P"+nextPlanID,targetStudentID,this.getUserID(),"0.00"));
        return recPlans;
    }

    // view all recovery plans
    public void viewRecoveryPlans(ArrayList<RecoveryPlan> recPlans) {
        System.out.println("PlanID   StudentID   Created By   Progress");
        for (RecoveryPlan plan : recPlans) {
            System.out.println(plan.getPlanID()+"   "+plan.getStudentID()+"   "+plan.getCreatedBy()+"   "+plan.getProgress());
        }
    }

    // delete recovery plan
    public ArrayList<RecoveryPlan> deleteRecoveryPlans(ArrayList<RecoveryPlan> recPlans, ArrayList<Student> studentList) {
        Scanner userInput = new Scanner(System.in);
        Validation checkInput = new Validation();
        String targetStudentID;
        boolean studentFound, planFound = false, planDelete = false;
        int planCount = 0, planSelection;
        ArrayList<String> studentPlanID = new ArrayList<>();

        do {                // Finding if student exists in student database
            System.out.print("Please enter student ID: ");
            targetStudentID = userInput.nextLine();
            studentFound = checkInput.validateStudentID(targetStudentID,studentList);
            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);

        for (RecoveryPlan plan : recPlans) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                planCount += 1;
                studentPlanID.add(plan.getPlanID());
            }
        }

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
                    recPlans.removeIf(plan -> plan.getPlanID().equals(studentPlanID.get(listIndex)));
                    planDelete = true;
                }
            } while (!planDelete);
        }
        return recPlans;
    }

    @Override
    public void showMenu() {
        System.out.println("Logged in as " + this.getRole() + " with user ID: " + this.getUserID());
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. View All Recovery Plans");
        System.out.println("3. Delete Recovery Plans");
        System.out.print(">>>   ");
    }
}
