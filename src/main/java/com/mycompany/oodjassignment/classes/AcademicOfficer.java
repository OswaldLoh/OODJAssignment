package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.FileHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class AcademicOfficer extends User implements RecoveryPlanManager {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }

    @Override
    public ArrayList<RecoveryPlan> addRecoveryPlan(ArrayList<RecoveryPlan> recPlans, String UserID) {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Please enter student ID: ");
        for (RecoveryPlan plan : recPlans) {
            System.out.println(plan.getPlanID());
        }
        return recPlans;
    }

    @Override
    public void showMenu() {
        System.out.println("Logged in as " + this.getRole() + " with user ID: " + this.getUserID());
        System.out.println("------------------------------");
        System.out.println("1. Add Recovery Plan");
        System.out.println();
        System.out.print(">>>   ");
    }
}
