package com.mycompany.oodjassignment.classes;

public class RecoveryTask {
    String taskID, description, planID;
    int duration;
    boolean completion;

    public RecoveryTask(String planID, String taskID) {
        this.taskID = taskID;
        this.planID = planID;
        this.completion = false;
    }; // parameterized constructor used during recPlan creation

    public RecoveryTask(String taskID, String planID, String description, int duration, boolean completion) { // used for parsing possibly
        this.taskID = taskID;
        this.planID = planID;
        this.description = description;
        this.duration = duration;
        this.completion = completion;
    }

    public boolean getCompletion() {
        return completion;
    }

    public void setCompletion(boolean completion) {
        this.completion = completion;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = this.duration;
    }

    public String getPlanID() { return planID; }


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
