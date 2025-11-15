package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.FileHandler;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class FileHandlingTest {
    private static ArrayList<Student> studentList;

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        // Parsing csv files into Student objects
        FileHandler handler = new FileHandler();
        studentList = handler.parseStudents("student_information.csv");
        ArrayList<RecoveryPlan> recPlans = new ArrayList<>();

        // Testing terminal
        System.out.println("This class is for file handling testing purposes.");
        System.out.println("What do you want to test today?");
        System.out.println("1. Using StudentID to find Student Name (read+.equals)");
        System.out.println("2. Writing Recovery Plan objects into csv file");
        System.out.println("3. Find Student ID in student array list and create recovery plan if it exists");
        System.out.print(">>>   ");
        int selection = input.nextInt();
        switch (selection) {
            case 1:
                findStudentNameTest(studentList, input);
                break;
            case 2:
                try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
                    printWriter.println("planID,studentID,createdBy,progress");

                    for (RecoveryPlan plan : recPlans) {
                        printWriter.println(plan.getPlanID() + "," + plan.getStudentID() + "," + plan.getCreatedBy() + "," + plan.getProgress());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            case 3:
                RecoveryPlan rec1 = new RecoveryPlan("P1", "Oswald", "Rich", "45.6");
                RecoveryPlan rec2 = new RecoveryPlan("P1", "Oswald", "Rich", "45.6");
                recPlans.add(rec1);
                recPlans.add(rec2);

                input.nextLine();
                System.out.printf("Please enter student ID of student to create recovery plan:");
                String targetStudentID = input.nextLine();
                int nextPlanID = recPlans.size() + 1;
                for (Student student : studentList) {
                    if ((student.getStudentID()).equals(targetStudentID)) {
                        recPlans.add(new RecoveryPlan("P" + nextPlanID, student.getStudentID(), "Oswald", "50"));
                    }
                }

                try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
                    printWriter.println("planID,studentID,createdBy,progress");
                    for (RecoveryPlan plan : recPlans) {
                        printWriter.println(plan.getPlanID() + "," + plan.getStudentID() + "," + plan.getCreatedBy() + "," + plan.getProgress());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    private static void findStudentNameTest(ArrayList<Student> studentList, Scanner input) {
        boolean studentFound = false;
        input.nextLine();           // clearing input buffer
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

