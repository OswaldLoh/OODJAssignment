package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.CSVParser;

public class RecoveryTask implements CSVParser<RecoveryTask> {
    private String taskID, description, planID;
    private int duration;
    private boolean completion;

    public RecoveryTask() {
    }; // No arg constructor used to retrieve fromCSV method for object parsing

    public RecoveryTask(String planID, String taskID) {
        this.taskID = taskID;
        this.planID = planID;
        this.completion = false;
    }; // Parameterized constructor used during manual addition of Recovery Plan

    public RecoveryTask(String taskID, String planID, String description, int duration, boolean completion) { // used for parsing possibly
        this.taskID = taskID;
        this.planID = planID;
        this.description = description;
        this.duration = duration;
        this.completion = completion;
    }; // Parameterized constructor used to construct objects from the contents of CSV file

    // getters
    public String getTaskID() {
        return taskID;
    }
    public String getPlanID() { return planID; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public boolean getCompletion() {
        return completion;
    }

    // setters
    public void setCompletion(boolean completion) {
        this.completion = completion;
    }
    public void setDuration(int duration) {
        this.duration = duration;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //methods
    @Override
    public RecoveryTask fromCSV(String line) {
        String[] details = line.split(",");
        int duration = Integer.parseInt(details[3]);
        boolean completion = Boolean.parseBoolean(details[4]);
        return new RecoveryTask(details[0],details[1],details[2],duration,completion);
    }
}
