package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int selection = -1;
        System.out.println("Welcome to University Management System!");
        System.out.println("1. Login");
        System.out.println("2. Forgot Password");
        System.out.println("3. Exit");
        System.out.println("----------------------------------------");
        do {
            System.out.print(">>>   ");
            selection = input.nextInt();
            switch (selection) {
                case 1:
                    login(input);
                    break;
                case 2:
                    System.out.println("Welcome to Password Recovery. Please enter your email.");
                    break;
                case 3:
                    System.out.println("Thank you for using the system. Process is terminated.");
                    break;
                default:
                    System.out.println("Invalid selection entered. Please try again.");
                    System.out.println();
            }
        } while (selection != 3);
    }
    private static void login(Scanner input) {
        int selection = -1;
        do {
            System.out.println("(Early design, this should be a login page");
            System.out.println("Please choose your role (for quick testing and coding purposes)");
            System.out.println("1. Course Administrator");
            System.out.println("2. Academic Officer");
            System.out.println("3. Back");
            System.out.print(">>>   login menu");
            selection = input.nextInt();
            switch(selection) {
                case 1:
                    System.out.println("You have chosen Course Administrator");
                    break;
                case 2:
                    System.out.println("You have chosen Academic Officer");
                    menuAcademicOfficer(input);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (selection != 3);
    }

    private static void menuAcademicOfficer(Scanner input) {
        int selection = -1;
        System.out.println("Choose what to do:");
        System.out.println("1. Eligibility Check");
        System.out.println("2. Course Recovery Plan");
        System.out.println("3. Back");
        do {
            System.out.println(">>>   ");
            selection = input.nextInt();
            switch(selection) {
                case 1:
                    System.out.println("Entered Eligibility Check.");
                    break;
                case 2:
                    AcademicOfficer.createRecoveryPlan(input);
                    break;
                case 3:
                    break;
            }
        } while (selection != 3);
    }
}
