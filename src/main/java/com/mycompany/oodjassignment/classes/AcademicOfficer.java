package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.FileHandler;

import java.util.ArrayList;
import java.util.Scanner;

public class AcademicOfficer extends User {
    public AcademicOfficer() {
        setRole("Academic Officer");
    }

    public ArrayList<RecoveryPlan> addRecoveryPlan(ArrayList<RecoveryPlan> recPlans, ArrayList<Student> studentList, String UserID) {
        boolean studentFound = false;
        Scanner userInput = new Scanner(System.in);
        do {
            System.out.print("Please enter student ID: ");
            String targetStudentID = userInput.nextLine();
            for (Student student : studentList) {
                if ((student.getStudentID()).equals(targetStudentID)) {
                    System.out.println("Student ID found.");
                    studentFound = true;
                    break;
                }
            }
            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);
        System.out.println("Just a flag for logic testing");

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
