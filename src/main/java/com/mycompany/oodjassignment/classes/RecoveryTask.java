package com.mycompany.oodjassignment.classes;
import com.mycompany.oodjassignment.functions.CSVParser;

public class RecoveryTask implements CSVParser<RecoveryTask> {
    private String taskID, description, planID;
    private int duration;
    private boolean completion;
    private static final String filename = "recovery_tasks.txt";

    public RecoveryTask() {
    }; // No arg constructor used to retrieve fromCSV method for object parsing

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
    public void setPlanID (String planID) { this.planID = planID; }
    public void setTaskID(String taskID) { this.taskID = taskID; }
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

        return new RecoveryTask(details[0],details[1],details[2],Integer.parseInt(details[3]),Boolean.parseBoolean(details[4]));
    }
    @Override
    public String toCSV() {
        return (taskID+","+planID+","+description+","+duration+","+completion);
    }
    @Override
    public String getFileHeader() {
        return "taskID,planID,description,duration,completion";
    }
    @Override
    public String getFilename() {
        return filename;
    }
}
