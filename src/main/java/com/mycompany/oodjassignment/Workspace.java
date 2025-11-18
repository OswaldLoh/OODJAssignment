package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.Database;
import com.mycompany.oodjassignment.functions.FileHandler;

import java.util.HashMap;
import java.util.Scanner;

public class Workspace {
    public static void main(String[]args) {
        // dummy login
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        // creating utility objects
        FileHandler fileHandler = new FileHandler();
        Scanner userInput = new Scanner(System.in);
        // hash maps
        Database database = fileHandler.parseFiles();
        HashMap<String, Student> studentDB = database.getStudentMap();
        HashMap<String, RecoveryPlan> recPlanDB = database.getRecoveryPlanMap();
        HashMap<String, RecoveryTask> recTaskDB = database.getRecoveryTaskMap();
        // variables
        boolean repeat = false;
        do {
            testUser.showMenu();
            System.out.print(">>>   ");
            int selection = userInput.nextInt();
            userInput.nextLine();
            switch (selection) {
                case 1:
                    testUser.addRecoveryPlan(recPlanDB, studentDB);
                    fileHandler.writeFiles(recPlanDB, recTaskDB);
                    break;
                case 2:
                    testUser.viewRecoveryPlan(recPlanDB);
                    break;
                case 3:
                    testUser.deleteRecoveryPlan(recPlanDB, studentDB);
                    fileHandler.writeFiles(recPlanDB, recTaskDB);
                    break;
                case 4:
                    for (String key : recTaskDB.keySet()) {
                        System.out.println(key);
                    }
                    break;
                case 5:
                    System.out.println("Exiting program.");
                    repeat = false;
                    continue;
                default:
                    System.out.println("Invalid selection. Please try again.");
                    break;
            }
            System.out.print("Do you want to perform another action? (Y/N): ");
            String response = userInput.nextLine().trim().toUpperCase();
            repeat = response.equals("Y");

        } while (repeat);
    }
}
