package com.mycompany.oodjassignment.classes;

public class RecoveryTask {
    String taskID, description, planID;
    int week;
    boolean completion;

    public RecoveryTask(String taskID, String planID, String description, int week) {
        this.taskID = taskID;
        this.planID = planID;
        this.description = description;
        this.week = week;
        this.completion = false;
    }

    public boolean isCompletion() {
        return completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getPlanID() {
        return planID;
    }

    public void setPlanID(String planID) {
        this.planID = planID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }
}
