package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;
import java.util.HashMap;

import java.util.Scanner;

public class HashMapTesting {
    public static void main(String[]args) {
        FileHandler fileHandler = new FileHandler();
        HashMaps database = fileHandler.parseFiles();
        System.out.println("Hash Map Testing");

        HashMap<String, Student> studentDB = database.getStudentMap();
        HashMap<String, RecoveryPlan> recoveryPlanDB = database.getRecoveryPlanMap();

        for (Student student : studentDB.values()) {
            System.out.println(student.getStudentID());
        }
    }
}
