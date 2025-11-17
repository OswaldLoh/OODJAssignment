package com.mycompany.oodjassignment.classes;
import java.util.ArrayList;

public class RecoveryPlan {
    private String planID;
    private String studentID;
    private String createdBy;
    private double progress;
    ArrayList<RecoveryTask> recTasks = new ArrayList<>();

    public RecoveryPlan() {};
    public RecoveryPlan(String planID, String studentID, String createdBy, String progress) {
        this.planID = planID;
        this.studentID = studentID;
        this.createdBy = createdBy;
        this.progress = Double.parseDouble(progress);
    }

    public String getPlanID() {
        return planID;
    }
    public String getStudentID() {
        return studentID;
    }
    public String getCreatedBy() {
        return createdBy;
    }
    public double getProgress() {
        return progress;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }
    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public void setProgress(double progress) {
        this.progress = progress;
    }

    // methods
    public void addRecoveryTask(RecoveryTask task) {
        recTasks.add(task);
    }

    public ArrayList<RecoveryTask> getTasks() {
        return recTasks;
    }
}
