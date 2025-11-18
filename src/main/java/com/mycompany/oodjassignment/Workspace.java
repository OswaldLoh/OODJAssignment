package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.FileHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Workspace {
    public static void main(String[]args) {
        // dummy login
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");

        // creating utility objects
        Scanner userInput = new Scanner(System.in);

        // parsing data from csv files into hashmaps
        Course Course = new Course();
        RecoveryPlan RecoveryPlan = new RecoveryPlan();
        RecoveryTask RecoveryTask = new RecoveryTask();
        Student Student = new Student();
        Grades Grades = new Grades();
        HashMap<String, Course> courseDB = FileHandler.readCSV(Course);
        HashMap<String, RecoveryPlan> recPlanDB = FileHandler.readCSV(RecoveryPlan);
        HashMap<String, RecoveryTask> recTaskDB = FileHandler.readCSV(RecoveryTask);
        HashMap<String, Student> studentDB = FileHandler.readCSV(Student);
        HashMap<String, Grades> gradesDB = FileHandler.readCSV(Grades);

        // variables
        boolean repeat;

        do {
            testUser.showMenu();
            System.out.print(">>>   ");
            int selection = userInput.nextInt();
            userInput.nextLine();
            switch (selection) {
                case 1:
                    testUser.addRecoveryPlan(recPlanDB, studentDB, recTaskDB);
                    break;
                case 2:
                    testUser.viewRecoveryPlan(recPlanDB);
                    break;
                case 3:
                    testUser.deleteRecoveryPlan(recPlanDB, studentDB);
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
        FileHandler.writeCSV(RecoveryPlan, recPlanDB);
        FileHandler.writeCSV(RecoveryTask, recTaskDB);
    }
}
