package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {

    // Parse files into Hash Maps
    public HashMaps parseFiles() {
        HashMaps database = new HashMaps();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("student_information.csv"))) {
            String line;
            fileReader.readLine();
            while ((line=fileReader.readLine()) != null) {
                String [] studentDetails = line.split(",");
                database.getStudentMap().put(studentDetails[0],new Student(studentDetails[0],studentDetails[1],studentDetails[2],studentDetails[3],studentDetails[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader fileReader = new BufferedReader(new FileReader("recovery_plans.csv"))) {
            String line;
            fileReader.readLine();
            while ((line=fileReader.readLine()) != null) {
                String[] recoveryPlanDetails = line.split(",");
                database.getRecoveryPlanMap().put(recoveryPlanDetails[0],new RecoveryPlan(recoveryPlanDetails[0], recoveryPlanDetails[1], recoveryPlanDetails[2], recoveryPlanDetails[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }
    
    public void writeRecoveryPlanCSV(ArrayList<RecoveryPlan> recPlans) {   // write RecoveryPlan objects into text file in CSV format
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
            fileWriter.println("planID,studentID,createdBy,progress");
            for (RecoveryPlan Plan : recPlans) {
                fileWriter.println(Plan.getPlanID()+","+Plan.getStudentID()+","+Plan.getCreatedBy()+","+Plan.getProgress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeRecoveryTaskCSV(ArrayList<RecoveryTask> recTasks) {
        try (PrintWriter fileWriter = new PrintWriter(new FileWriter("recovery_tasks.csv"))) {
            fileWriter.println("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
