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
    public RecoveryTask addNewTask(Grades Grade, Database database) {
        final String ASSIGNMENT = "Assignment";
        final String EXAM = "Exam";
        final String MODULE = "Module";


        RecoveryTask newTask = new RecoveryTask();
        Scanner userInput = new Scanner(System.in);
        System.out.print("Recommended Recovery Task: ");
        if (Grade.getWeightedAssignmentMark()< 40 && Grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Whole Module");
            System.out.println("Assignment Mark: " + Grade.getWeightedAssignmentMark() + "/100");
            System.out.println("Final Examination Mark: " + Grade.getWeightedExamMark()+ "/100");
            newTask.setDescription(MODULE);
            newTask.setDuration(50);
        } else if (Grade.getWeightedExamMark() < 40) {
            System.out.println("Final Examination");
            System.out.println("Final Examination Mark: " + Grade.getWeightedExamMark()+ "/100");
            newTask.setDescription(EXAM);
            newTask.setDuration(30);
        } else if (Grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Assignment");
            System.out.println("Assignment Mark: " + Grade.getWeightedAssignmentMark() + "/100");
            newTask.setDescription(ASSIGNMENT);
            newTask.setDuration(50);
        }

        // Creating IDManager object to generate new ID for recTask
        IDManager recTaskIDManager = new IDManager(database.getRecTaskDB());
        recTaskIDManager.getHighestTaskID();
        String nextTaskID = "T" + recTaskIDManager.generateNewID();

        newTask.setTaskID(nextTaskID);
        newTask.setPlanID(this.planID);
        return newTask;
    }
    public HashMap<String, RecoveryTask> deleteRecoveryTask(HashMap<String, RecoveryTask> recTaskDB) {
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


