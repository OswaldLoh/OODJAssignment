package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import java.util.*;

public class CourseRecoverySystem {
    public static void main(String[]args) {
        // dummy login
        Database database = new Database();
        AcademicOfficer testUser = new AcademicOfficer("A01", database, new RecoveryPlanManager(database));

        // creating utility objects
        Scanner userInput = new Scanner(System.in);
        RecoveryPlan RecoveryPlan = new RecoveryPlan();
        RecoveryTask RecoveryTask = new RecoveryTask();


        // variables
        boolean repeat = false;
        int selection = 0;

        do {
            try {
                testUser.showMenu();
                int secondSelection;
                int firstSelection = InputValidation.readInt(">>>   ",1,4);
                switch (firstSelection) {
                    case 1:
                        testUser.searchStudent();
                        break;
                    case 2:
                        testUser.showRecoveryPlanMenu();
                        secondSelection = InputValidation.readInt(">>>   ",1,5);
                        switch (secondSelection) {
                            case 1:
                                testUser.addRecoveryPlan();
                                break;
                            case 2:
                                testUser.updateRecoveryPlan();
                                break;
                            case 3:
                                testUser.deleteRecoveryPlan();
                                break;
                            case 4:
                                testUser.monitorRecoveryPlan();
                                break;
                            case 5:
                                break;
                        }
                        break;
                    case 3:
                        testUser.showRecoveryTaskMenu();
                        secondSelection = InputValidation.readInt(">>>   ",1,3);
                        switch (secondSelection) {
                            case 1:
                                testUser.addRecoveryTask();
                                break;
                            case 2:
                                testUser.deleteRecoveryTask();
                                break;
                            case 3:
                                break;
                        }
                        break;
                    case 4:
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
            } catch (InputMismatchException e) {
                System.out.println("Invalid selection. Please try again.");
                userInput.nextLine();
            }
        } while (repeat);
        FileHandler.writeCSV(RecoveryPlan, database.getRecPlanDB());
        FileHandler.writeCSV(RecoveryTask, database.getRecTaskDB());
    }
}
