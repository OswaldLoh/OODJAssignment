package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.HashMap;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {

    // Parse files into Hash Maps
    public Database parseFiles() {
        Database database = new Database();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("student_information.csv"))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] studentDetails = line.split(",");
                database.getStudentMap().put(studentDetails[0], new Student(studentDetails[0], studentDetails[1], studentDetails[2], studentDetails[3], studentDetails[5]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        int highestPlanID = 0;
        try (BufferedReader fileReader = new BufferedReader(new FileReader("recovery_plans.csv"))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] recoveryPlanDetails = line.split(",");
                database.getRecoveryPlanMap().put(recoveryPlanDetails[0], new RecoveryPlan(recoveryPlanDetails[0], recoveryPlanDetails[1], recoveryPlanDetails[2], recoveryPlanDetails[3]));
                int PlanID = Integer.parseInt(recoveryPlanDetails[0].substring(1));
                if (PlanID > highestPlanID) {
                    highestPlanID = PlanID;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }

    public void writeFiles(HashMap<String, RecoveryPlan> recPlanDB, HashMap<String, Student> studentDB) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
            printWriter.println("planID,studentId,createdBy,progress");
            for (RecoveryPlan plan : recPlanDB.values()) {
                printWriter.println(plan.getPlanID()+","+plan.getStudentID()+","+plan.getCreatedBy()+","+plan.getProgress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
