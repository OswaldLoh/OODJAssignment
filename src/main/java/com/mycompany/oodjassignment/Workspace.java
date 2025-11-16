package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Workspace {
    public static void main(String[]args) {
        // creating utility objects
        AcademicOfficer testUser = new AcademicOfficer();
        testUser.setUserID("A01");
        FileHandler fileHandler = new FileHandler();
        Scanner userInput = new Scanner(System.in);

        // parsing required objects / files
        ArrayList<RecoveryPlan> recPlans = fileHandler.parseRecoveryPlan();
        ArrayList<Student> studentList = fileHandler.parseStudents();

        testUser.showMenu();
        int selection = userInput.nextInt();

        switch (selection) {
            case 1:
                testUser.addRecoveryPlan(recPlans, studentList);
                fileHandler.writeRecoveryPlanCSV(recPlans);    // putting here for easier debug
                break;
            case 2:
                testUser.viewRecoveryPlans(recPlans);
                break;
            case 3:
                testUser.deleteRecoveryPlans(recPlans,studentList);
                fileHandler.writeRecoveryPlanCSV(recPlans);   // putting here for easier debug
        }
        fileHandler.writeRecoveryPlanCSV(recPlans);
    }
}
