package com.mycompany.oodjassignment.Function;

public class ReportGenerator {
    private String reportID;
    private String filePath;

    public ReportGenerator(String reportID, String filePath) {
        this.reportID = reportID;
        this.filePath = filePath;
    }

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

    public void displayReport(){
        
    }

    public void generateReport(){

    }
}
