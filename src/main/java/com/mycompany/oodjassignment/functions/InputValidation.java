package com.mycompany.oodjassignment.functions;
import java.util.Scanner;

public class InputValidation {
    private static final Scanner userInput = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return userInput.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = userInput.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            int input = readInt(prompt); // Reuse the method above!
            if (input >= min && input <= max) {
                return input;
            }
            System.out.println("Error: Please enter a number between " + min + " and " + max + ".");
            System.out.println();
        }
    }

    public static String validateStudentID(Database database) {
        String targetStudentID;
        boolean studentFound = false;
        do {
            targetStudentID = InputValidation.readString("Please enter Student ID: ");
            if (database.studentExist(targetStudentID)) {
            studentFound = true;
        } else {
            System.out.println("Error: Student ID '" + targetStudentID + "' not found. Please try again.");
            System.out.println();
            }
        } while (!studentFound);
    return targetStudentID;
    }

    public static String validatePlanID(Database database) {
        String targetPlanID;
        boolean planFound = false;
        do {
            targetPlanID = InputValidation.readString("Please enter Plan ID: ");
            if (database.planExist(targetPlanID)) {
                planFound = true;
            } else {
                System.out.println("Error: Plan ID '" + targetPlanID + "' not found. Please try again.");
                System.out.println();
            }
        } while (!planFound);
        return targetPlanID;
    }
}

