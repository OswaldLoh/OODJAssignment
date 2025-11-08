package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;

public class TestingPurpose {
    private static ArrayList<Student> studentList;

    public static void main(String[] args) {
        boolean studentFound = false;
        Scanner input = new Scanner(System.in);
        // Parsing csv files into Student objects
        FileHandler handler = new FileHandler();
        studentList = handler.parseFile("student_information.csv");


        System.out.println("This class is for file handling testing purposes.");

        do {
            System.out.print("Please enter student ID: ");
            String targetStudentID = input.nextLine();

            for (Student student : studentList) {
                if ((student).getStudentID().equals(targetStudentID)) {
                    System.out.println("Student Name: " + student.getFirstName() + " " + student.getLastName());
                    studentFound = true;
                }
            }
            if (!studentFound) {
                System.out.println("Student ID: " + targetStudentID + " does not exist in the records. Please try again.");
                System.out.println("");
            }
        } while (!studentFound);
    }
}
