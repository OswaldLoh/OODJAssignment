package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int selection;
        System.out.println("Welcome to University Management System!");
        System.out.println("1. Login");
        System.out.println("2. Forgot Password");
        System.out.println("3. Exit");
        System.out.println("----------------------------------------");
        while (true) {
            System.out.print(">>>   ");
            selection = input.nextInt();
            switch (selection) {
                case 1:
                    CourseAdmin courseAdmin1 = new CourseAdmin();
                    System.out.println("The role of Admin 1 is " + courseAdmin1.getRole());
                    System.out.println("File operations testing below:");
                    break;
                case 2:
                    System.out.println("Welcome to Password Recovery. Please enter your email.");
                    break;
                default:
                    System.out.println("Invalid selection entered. Please try again.");
                    System.out.println();
            }
        }
    }
}
