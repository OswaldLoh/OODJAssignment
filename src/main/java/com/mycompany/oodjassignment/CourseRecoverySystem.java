package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import java.util.*;

public class CourseRecoverySystem {
    public static void main(String[]args) {
        // dummy login
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");

        // creating utility objects
        Scanner userInput = new Scanner(System.in);
        RecoveryPlan RecoveryPlan = new RecoveryPlan();
        RecoveryTask RecoveryTask = new RecoveryTask();


        // variables
        boolean repeat;

        do {
            testUser.showMenu();
            System.out.print(">>>   ");
            int selection = userInput.nextInt();
            userInput.nextLine();
            switch (selection) {
                case 1:
                    testUser.addRecoveryPlan(database);
                    break;
                case 2:
                    break;
                case 3:
                    testUser.deleteRecoveryPlan(database);
                    break;
                case 4:
                    testUser.searchStudent(database);
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    repeat = false;
                    continue;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
            System.out.println();
            System.out.print("Do you want to perform another action? (Y/N): ");
            String response = userInput.nextLine().trim().toUpperCase();
            repeat = response.equals("Y");

        } while (repeat);
        FileHandler.writeCSV(RecoveryPlan, database.getRecPlanDB());
        FileHandler.writeCSV(RecoveryTask, database.getRecTaskDB());
    }
}
