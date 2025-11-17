package com.mycompany.oodjassignment.functions;
import com.mycompany.oodjassignment.classes.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("CallToPrintStackTrace")

public class FileHandler {
    public void appendFile(String filename, String contents) {          // completed, method for appending text file
        try (PrintWriter fileInput = new PrintWriter(filename)) {
            fileInput.append(contents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // student file handling
    public ArrayList<Student> parseStudents(){          // completed, method for reading text file
        ArrayList<Student> students = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader("student_information.csv"))) {
            String line;
            int hi;
            fileReader.readLine();
            while ((line=fileReader.readLine()) != null) {
                String[] studentDetails = line.split(",");
                students.add(new Student(studentDetails[0],studentDetails[1],studentDetails[2],studentDetails[3],studentDetails[4]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
    }

    // Recovery Plans
    public ArrayList<RecoveryPlan> parseRecoveryPlan() {     // parse data from text file into RecoveryPlan objects in ArrayList
        ArrayList<RecoveryPlan> recoveryPlans = new ArrayList<>();

        try (BufferedReader fileReader = new BufferedReader(new FileReader("recovery_plans.csv"))) {
            String line;
            fileReader.readLine();
            while ((line=fileReader.readLine()) != null) {
                String[] recoveryPlanDetails = line.split(",");
                recoveryPlans.add(new RecoveryPlan(recoveryPlanDetails[0], recoveryPlanDetails[1], recoveryPlanDetails[2], recoveryPlanDetails[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return recoveryPlans;
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
