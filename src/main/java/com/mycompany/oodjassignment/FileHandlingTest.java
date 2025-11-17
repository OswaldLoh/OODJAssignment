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

        ArrayList<RecoveryPlan> recPlans = new ArrayList<>();

        // Testing terminal
        System.out.println("This class is for file handling testing purposes.");
        System.out.println("What do you want to test today?");
        System.out.println("1. Testing recovery tasks");
        System.out.print(">>>   ");
        int selection = input.nextInt();
        switch (selection) {
            case 1:
                RecoveryPlan newPlan = new RecoveryPlan();
                newPlan.addRecoveryTask(new RecoveryTask("T1","P1","Assignment",1));
                newPlan.addRecoveryTask(new RecoveryTask("T2","P1","Exam",1));
                ArrayList<RecoveryTask> result = newPlan.getTasks();
                for (RecoveryTask task : result) {
                    System.out.println(task.getTaskID());
                    System.out.println(task.getDescription());
                }
        }
    }
}

