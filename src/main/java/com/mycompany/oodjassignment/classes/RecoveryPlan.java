package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class RecoveryPlan implements CSVParser<RecoveryPlan> {
    private String planID, studentID, createdBy;
    private double progress;
    private static final String filename = "recovery_plans.csv";

    // constructors
    public RecoveryPlan() {}

    public RecoveryPlan(String planID, String studentID, String createdBy, String progress) {
        this.planID = planID;
        this.studentID = studentID;
        this.createdBy = createdBy;
        this.progress = Double.parseDouble(progress);
    }

    // getters
    public String getPlanID() { return planID; }
    public String getStudentID() { return studentID; }
    public String getCreatedBy() { return createdBy; }
    public double getProgress() { return progress; }

    // setters
    public void setPlanID(String planID) { this.planID = planID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setProgress(double progress) { this.progress = progress; }

    // methods
    public HashMap<String, RecoveryTask> addRecoveryTask(HashMap<String, RecoveryTask> recTaskDB) {
        final String ASSIGNMENT = "Assignment";
        final String EXAM = "Exam";
        final String MODULE = "Module";
        boolean validAnswer = true;

        IDManager recTaskIDManager = new IDManager();
        recTaskIDManager.getHighestTaskID(recTaskDB);

        RecoveryTask newTask = new RecoveryTask();
        Scanner userInput = new Scanner(System.in);
        System.out.println("Select Recovery Task to be Added:");
        System.out.println("1. Assignment");
        System.out.println("2. Final Exam");
        System.out.println("3. Module"); // assignment + final exam
        System.out.print(">>>   ");
        do {
            int selection = userInput.nextInt();
            switch (selection) {
                case 1:
                    newTask.setDescription(ASSIGNMENT);
                    newTask.setDuration(50);
                    break;
                case 2:
                    newTask.setDescription(EXAM);
                    newTask.setDuration(30);
                    break;
                case 3:
                    newTask.setDescription(MODULE);
                    newTask.setDuration(100);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    validAnswer = false;
            }
        } while (!validAnswer);
        newTask.setTaskID("T" + recTaskIDManager.generateNewID());
        newTask.setPlanID(this.planID);
        recTaskDB.put(this.planID,newTask);
        return recTaskDB;
    }

    @Override
    public RecoveryPlan fromCSV(String line) {
        String[] details = line.split(",");
        return new RecoveryPlan(details[0],details[1],details[2],details[3]);
    }
    @Override
        public String toCSV() {
        return (planID+","+studentID+","+createdBy+","+progress);
    }
    @Override
        public String getFileHeader() {
        return "planID,studentId,createdBy,progress";
    }
    @Override
        public String getFilename() {
        return filename;
    }
}


