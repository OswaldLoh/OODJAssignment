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


