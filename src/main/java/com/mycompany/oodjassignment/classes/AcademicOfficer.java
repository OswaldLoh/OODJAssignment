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
        int nextPlanID = recPlans.size() + 1;

        Validation checkInput = new Validation();
        String targetStudentID = checkInput.validateStudentID(studentList);

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


    @Override
    public void showMenu() {
        System.out.println("Logged in as " + this.getRole() + " with user ID: " + this.getUserID());
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println("2. View All Recovery Plans");
        System.out.println();
        System.out.print(">>>   ");
    }
}
