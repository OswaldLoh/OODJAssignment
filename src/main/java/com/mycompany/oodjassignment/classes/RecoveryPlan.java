package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.IDManager;

import java.util.ArrayList;
import java.util.Scanner;

public class RecoveryPlan {
    private String planID;
    private String studentID;
    private String createdBy;
    private double progress;
    private ArrayList<RecoveryTask> recTasks;
    private final String ASSIGNMENT = "Assignment";
    private final String EXAM = "Exam";
    private final String MODULE = "Module";

    public RecoveryPlan(String planID, String studentID, String createdBy, String progress) {
        this.planID = planID;
        this.studentID = studentID;
        this.createdBy = createdBy;
        this.progress = Double.parseDouble(progress);
        this.recTasks = new ArrayList<>();
    }
    //getters
    public String getPlanID() {return planID;}
    public String getStudentID() {return studentID;}
    public String getCreatedBy() {return createdBy;}
    public double getProgress() {return progress;}

    //setters
    public void setPlanID(String planID) { this.planID = planID;}
    public void setStudentID(String studentID) { this.studentID = studentID;}
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy;}
    public void setProgress(double progress) { this.progress = progress;}

    // methods
    public void addRecoveryTask() {
        boolean validAnswer = true;
        IDManager recTaskIDManager = new IDManager();
        RecoveryTask newTask = new RecoveryTask(this.getPlanID(),"T"+recTaskIDManager.generateNewID());
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
        recTasks.add(newTask);

    }
}


