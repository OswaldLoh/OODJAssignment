package com.mycompany.oodjassignment;
import com.mycompany.oodjassignment.classes.*;
import com.mycompany.oodjassignment.functions.*;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class HashMapTesting {
    public static void main(String[]args) {
        FileHandler fileHandler = new FileHandler();
        Database database = fileHandler.parseFiles();
        HashMap<String, Student> studentDB = database.getStudentMap();
        HashMap<String, RecoveryPlan> recoveryPlanDB = database.getRecoveryPlanMap();
        System.out.println("Hash Map Testing");



        recoveryPlanDB.put("P15",new RecoveryPlan("P15","S021","A01","0.0"));
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
            printWriter.println("planID,studentId,createdBy,progress");
            for (RecoveryPlan plan : recoveryPlanDB.values()) {
                printWriter.println(plan.getPlanID()+","+plan.getStudentID()+","+plan.getCreatedBy()+","+plan.getProgress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
