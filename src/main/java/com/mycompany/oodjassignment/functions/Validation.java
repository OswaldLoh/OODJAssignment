package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.Student;

import java.util.ArrayList;
import java.util.Scanner;

public class Validation {
    public String validateStudentID(ArrayList<Student> studentList) {
        Scanner userInput = new Scanner(System.in);
        String targetStudentID;
        boolean studentFound = false;
        do {
            System.out.print("Please enter student ID: ");
            targetStudentID = userInput.nextLine();
            for (Student student : studentList) {
                if ((student.getStudentID()).equals(targetStudentID)) {
                    System.out.println("Student ID found.");
                    studentFound = true;
                    break;
                }
            }
            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " is not found in the database records. Please try again.");
                System.out.println();
            }
        } while (!studentFound);

        userInput.close();
        return targetStudentID;
    }
}
