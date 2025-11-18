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

        try (BufferedReader fileReader = new BufferedReader(new FileReader("recovery_plans.csv"))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] recoveryPlanDetails = line.split(",");
                database.getRecoveryPlanMap().put(recoveryPlanDetails[0], new RecoveryPlan(recoveryPlanDetails[0], recoveryPlanDetails[1], recoveryPlanDetails[2], recoveryPlanDetails[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader fileReader = new BufferedReader(new FileReader("recovery_tasks.csv"))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String [] recoveryTaskDetails = line.split(",");
                boolean completion = Boolean.parseBoolean(recoveryTaskDetails[4]);
                int duration = Integer.parseInt(recoveryTaskDetails[3]);
                database.getRecoveryTaskMap().put(recoveryTaskDetails[0], new RecoveryTask(recoveryTaskDetails[0],recoveryTaskDetails[1],recoveryTaskDetails[2],duration,completion));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return database;
    }

    public void writeFiles(HashMap<String, RecoveryPlan> recPlanDB, HashMap<String, RecoveryTask> recTaskDB) {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_plans.csv"))) {
            printWriter.println("planID,studentId,createdBy,progress");
            for (RecoveryPlan plan : recPlanDB.values()) {
                printWriter.println(plan.getPlanID() + "," + plan.getStudentID() + "," + plan.getCreatedBy() + "," + plan.getProgress());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (PrintWriter printWriter = new PrintWriter(new FileWriter("recovery_tasks.csv:"))) {
            printWriter.println("taskID,planID,description,duration,completion");
            for (RecoveryTask task : recTaskDB.values()) {
                printWriter.println(task.getTaskID() + "," + task.getPlanID() + "," + task.getDescription() + "," + task.getDuration() + "," + task.getCompletion());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
