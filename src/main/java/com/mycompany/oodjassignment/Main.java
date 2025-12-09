package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        FileHandler handler = new FileHandler();

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
        System.out.println("3. Student Analytics Dashboard");
        System.out.println("4. University Management Dashboard");
        System.out.println("5. Back");
        do {
            System.out.print(">>>   ");
            selection = input.nextInt();
            switch(selection) {
                case 1:
                    System.out.println("Entered Eligibility Check.");
                    break;
                case 2:
                    System.out.println("Entered Course Recovery Plan.");
                    break;
                case 3:
                    System.out.println("Launching Student Analytics Dashboard...");
                    launchAnalyticsDashboard();
                    break;
                case 4:
                    System.out.println("Launching University Management Dashboard...");
                    launchDashboard();
                    break;
                case 5:
                    System.out.println("Returning to previous menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    System.out.println();
            }
        } while (selection != 5);
    }
    
    private static void launchAnalyticsDashboard() {
        // Run the Swing application in a separate thread
        new Thread(() -> {
            try {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    com.mycompany.oodjassignment.reportgeneratorGUI.StudentPerformancePanel dashboard = 
                        new com.mycompany.oodjassignment.reportgeneratorGUI.StudentPerformancePanel();
                    dashboard.setVisible(true);
                });
            } catch (Exception e) {
                System.out.println("Error launching analytics dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
    
    private static void launchDashboard() {
        // Run the Swing application in a separate thread
        new Thread(() -> {
            try {
                javax.swing.SwingUtilities.invokeLater(() -> {
                    com.mycompany.oodjassignment.reportgeneratorGUI.DashboardPanel dashboard = 
                        new com.mycompany.oodjassignment.reportgeneratorGUI.DashboardPanel();
                    dashboard.setVisible(true);
                });
            } catch (Exception e) {
                System.out.println("Error launching dashboard: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }
}
