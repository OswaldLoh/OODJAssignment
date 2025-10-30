package com.mycompany.oodjassignment;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        int selection;
        boolean flag = false;
        System.out.println("Welcome to University Management System!");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.println("----------------------------------------");
        while (!flag) {
            System.out.print(">>>   ");
            selection = input.nextInt();
            switch (selection) {
                case 1:
                    System.out.println("User Interface");
                    flag = true;
                    break;
                case 2:
                    System.out.println("Register Interface");
                    flag = true;
                    break;
                default:
                    System.out.println("Invalid selection entered. Please try again.");
                    System.out.println();
                    System.out.println("This is testing.");
                    break;
            }
        }
    }
}
