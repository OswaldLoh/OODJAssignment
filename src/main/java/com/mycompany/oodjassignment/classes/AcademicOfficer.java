package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.FileHandler;

import java.util.Scanner;

public class AcademicOfficer extends User {
    AcademicOfficer() {
        // no arg constructor to automatically set all newly created objects
        // to have the role "Academic Officer" using setter
        setRole("Academic Officer");
    }

    public static void createRecoveryPlan(Scanner input) {
        FileHandler fileHandler = new FileHandler();
        System.out.print("Please enter the student ID:");
        String inputStudentID = input.nextLine();


    }
}
