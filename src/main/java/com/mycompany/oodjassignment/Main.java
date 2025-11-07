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
                    login();
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
    private static void login() {

    }
}
