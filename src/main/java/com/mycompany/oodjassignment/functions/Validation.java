package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.RecoveryPlan;
import com.mycompany.oodjassignment.classes.Student;
import java.util.HashMap;
import java.util.ArrayList;

public class Validation {

    public static boolean validateStudentID(String targetStudentID, HashMap<String, Student> studentDB) {
        boolean studentFound = false;
        if (studentDB.containsKey(targetStudentID)) {
            System.out.println("Student is found inside the hash map.");
            studentFound = true;
        }   else {
            System.out.println("Student is NOT FOUND.");
        }
        return studentFound;
    }

    public static ArrayList<String> validateStudentRecoveryPlan(String targetStudentID, HashMap<String, RecoveryPlan> recPlanDB) {
        ArrayList<String> studentPlanID = new ArrayList<>();
        for (RecoveryPlan plan : recPlanDB.values()) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                studentPlanID.add(plan.getPlanID());
            }
        }
        return studentPlanID;
    }
    public static int getRecoveryPlanCount(String targetStudentID, HashMap<String, RecoveryPlan> recPlanDB) {
        int planCount = 0;
        for (RecoveryPlan plan : recPlanDB.values()) {        // Finding if student has recovery plans and add them into a list if yes
            if ((targetStudentID).equals(plan.getStudentID())) {
                planCount += 1;
            }
        }
        return planCount;
    }
}
