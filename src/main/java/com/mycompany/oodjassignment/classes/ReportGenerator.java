package com.mycompany.oodjassignment.classes;

public class ReportGenerator {
    
    private String reportID;
    private String filePath;

    // constructor
    public ReportGenerator(String reportID, String filePath) {
        this.reportID = reportID;
        this.filePath = filePath;
    }

    // getter and setter
    public String getReportID() {
        return reportID;
    }

    public void setReportID(String reportID) {
        this.reportID = reportID;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    // method
    public void displayReport(){
        
    }

    public void generateReport(){

    }


}
