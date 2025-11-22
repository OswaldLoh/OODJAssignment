package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class RecoveryPlan implements CSVParser<RecoveryPlan> {
    private String planID, studentID, courseID, createdBy;
    private double progress;
    private static final String filename = "recovery_plans.csv";

    // constructors
    public RecoveryPlan() {}

    public RecoveryPlan(String planID, String studentID, String courseID, String createdBy, String progress) {
        this.planID = planID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.createdBy = createdBy;
        this.progress = Double.parseDouble(progress);
    }

    // getters
    public String getPlanID() { return planID; }
    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public String getCreatedBy() { return createdBy; }
    public double getProgress() { return progress; }

    // setters
    public void setPlanID(String planID) { this.planID = planID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setProgress(double progress) { this.progress = progress; }

    // methods
    public void addNewTask(Database database) {
        Grades grade = database.getGrades(studentID,courseID);
        RecoveryTask newTask = new RecoveryTask();
        if (grade.calculateGPA() > 2.0) {
            System.out.println("Student is not eligible for Recovery Plan.");
            System.out.println("GPA: " + grade.calculateGPA());
            return;
        }
        System.out.println();
        System.out.println("Student Grades");
        System.out.println("-----------------------");
        System.out.println("Assignment Mark: " + grade.getWeightedAssignmentMark() + "/100");
        System.out.println("Final Examination Mark: " + grade.getWeightedExamMark()+ "/100");
        System.out.println("GPA: "+grade.calculateGPA());
        System.out.println();
        System.out.print("Recommended Recovery Task: ");

        if (grade.getWeightedAssignmentMark()< 40 && grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Whole Module");
        } else if (grade.getWeightedExamMark() < 40) {
            System.out.println("Final Examination");
        } else if (grade.getWeightedAssignmentMark() < 40) {
            System.out.println("Assignment");
        }

        String newDescription = InputValidation.readString("Enter Description: ");
        newTask.setDescription(newDescription);
        int newDuration = InputValidation.readInt("Enter duration for this task (days): ");
        newTask.setDuration(newDuration);

        // Creating IDManager object to generate new ID for recTask
        IDManager recTaskIDManager = new IDManager(database.getRecTaskDB());
        recTaskIDManager.getHighestTaskID();
        String nextTaskID = "T" + recTaskIDManager.generateNewID();

        newTask.setTaskID(nextTaskID);
        newTask.setPlanID(this.planID);
        database.addRecoveryTask(newTask);
    }
    public void deleteRecoveryTask(Database database) {
        boolean validPlan = false;
        int taskCount = 1;
        do {
            ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(planID);
            if (planTasks.size() == 1) {
                System.out.println("Error. You cannot delete the last remaining recovery task under this recovery plan.");
                System.out.println();
            } else {
                validPlan = true;
                for (RecoveryTask task : planTasks) {
                    System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
                }
                System.out.println();
                int index = InputValidation.readInt("Choose Recovery Task to delete: ",1,planTasks.size());
                String targetTaskID = planTasks.get(index).getTaskID();
                database.removeRecoveryTask(targetTaskID);
                System.out.println();
                System.out.println("Recovery Task '" + targetTaskID + "' has been successfully deleted.");
            }
        } while (!validPlan);
    }
    public void updateRecoveryTask(Database database) {
        int taskCount = 1;
        ArrayList<RecoveryTask> planTasks = database.getPlanRecoveryTask(planID);
        for (RecoveryTask task : planTasks) {
            System.out.println(taskCount+". "+ task.getTaskID() + "      Completed? : " + task.getCompletion());
        }
        System.out.println();
        int index = InputValidation.readInt("Choose Recovery Task to update: ",1,planTasks.size());
        String targetTaskID = planTasks.get(index).getTaskID();
        System.out.println();
        System.out.println("Please enter detail to modify");
        System.out.println("1. Description");
        System.out.println("2. Duration:");
        System.out.println("3. Completion Status");
        int detailSelection = InputValidation.readInt(">>>   ",1,3);
        switch(detailSelection) {
            case 1:
                String newDescription = InputValidation.readString("Please enter new description: ");
                database.getRecoveryTask(targetTaskID).setDescription(newDescription);
            case 2:
                int newDuration = InputValidation.readInt("Please enter new duration: ");
                database.getRecoveryTask(targetTaskID).setDuration(newDuration);
            case 3:
                System.out.println("Please choose completion status:");
                System.out.println("1. Completed");
                System.out.println("2. Incomplete");
                int completedSelection = InputValidation.readInt(">>>   ",1,2);
                if (completedSelection == 1) {
                    database.getRecoveryTask(targetTaskID).setCompletion(true);
                } else if (completedSelection == 2) {
                    database.getRecoveryTask(targetTaskID).setCompletion(false);
                }
        }
        System.out.println("Recovery Task has been updated successfully.");
    }
    @Override
    public RecoveryPlan fromCSV(String line) {
        String[] details = line.split(",");
        return new RecoveryPlan(details[0],details[1],details[2],details[3],details[4]);
    }
    @Override
        public String toCSV() {
        return (planID+","+studentID+","+courseID+","+createdBy+","+progress);
    }
    @Override
        public String getFileHeader() {
        return "planID,studentID,courseID,createdBy,progress";
    }
    @Override
        public String getFilename() {
        return filename;
    }
}


