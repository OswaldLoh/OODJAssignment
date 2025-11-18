package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.HashMap;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {
    // Parse files into Hash Maps
    public static <Class extends CSVParser<Class>> HashMap<String, Class> loadCSV(String filename, Class parseTarget) {
        HashMap<String, Class> resultHashMap = new HashMap<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(filename))) {
            String line;
            fileReader.readLine();
            while ((line = fileReader.readLine()) != null) {
                String[] details = line.split(",");
                resultHashMap.put(details[0], parseTarget.fromCSV(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultHashMap;
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
