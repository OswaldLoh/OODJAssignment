package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Workspace {
    public static void main(String[]args) {
        // creating utility objects
        User testUser = new AcademicOfficer();
        FileHandler fileHandler = new FileHandler();
        Scanner userInput = new Scanner(System.in);

        // parsing required objects / files
        ArrayList<RecoveryPlan> recPlans = fileHandler.parseRecoveryPlan();

        testUser.showMenu();
        int selection = userInput.nextInt();

        switch (selection) {
            case 1:
                testUser.addRecoveryPlan(recPlans, testUser.getUserID()));
        }
    }
}
