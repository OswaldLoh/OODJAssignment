package com.mycompany.oodjassignment.functions;

import com.mycompany.oodjassignment.classes.Student;

import java.util.ArrayList;
import java.util.Scanner;

public class Validation {

    public boolean validateStudentID(String targetStudentID, ArrayList<Student> studentList) {
        boolean studentFound = false;
        for (Student student : studentList) {
            if ((student.getStudentID()).equals(targetStudentID)) {
                System.out.println("Student ID found.");
                studentFound = true;
                break;
            }
        }
        return studentFound;
    }




}
