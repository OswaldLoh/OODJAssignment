package com.mycompany.oodjassignment.classes;

import com.mycompany.oodjassignment.functions.*;
import java.util.*;

public class RecoveryPlan implements CSVParser<RecoveryPlan> {
    private String planID, studentID, courseID, createdBy, component;
    private double progress;
    private static final String filename = "data/recovery_plans.txt";

    // constructors
    public RecoveryPlan() {}

    public RecoveryPlan(String planID, String studentID, String courseID, String component, String createdBy, String progress) {
        this.planID = planID;
        this.studentID = studentID;
        this.courseID = courseID;
        this.createdBy = createdBy;
        this.component = component;
        this.progress = Double.parseDouble(progress);
    }

    // getters
    public String getPlanID() { return planID; }
    public String getStudentID() { return studentID; }
    public String getCourseID() { return courseID; }
    public String getCreatedBy() { return createdBy; }
    public String getComponent() { return component; }
    public double getProgress() { return progress; }


    // setters
    public void setPlanID(String planID) { this.planID = planID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }
    public void setCourseID(String courseID) { this.courseID = courseID; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    public void setProgress(double progress) { this.progress = progress; }
    public void setComponent(String component) {this.component = component; }

    @Override
    public RecoveryPlan fromCSV(String line) {
        String[] details = line.split(",");
        return new RecoveryPlan(details[0],details[1],details[2],details[3],details[4],details[5]);
    }
    @Override
        public String toCSV() {
        return (planID+","+studentID+","+courseID+","+component+","+createdBy+","+progress);
    }
    @Override
        public String getFileHeader() {
        return "planID,studentID,courseID,component,createdBy,progress";
    }
    @Override
        public String getFilename() {
        return filename;
    }

    @Override
    public String toString() {
        return String.format("RecoveryPlan[ID=%s, StudentID=%s, CourseID=%s, Component=%s, CreatedBy=%s, Progress=%.2f%%]", 
                planID, studentID, courseID, component, createdBy, progress);
    }
}


