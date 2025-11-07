<<<<<<< HEAD:src/main/java/com/mycompany/oodjassignment/Function/ReportGenerator.java
package com.mycompany.oodjassignment.Function;
=======
package com.mycompany.oodjassignment.functions;
>>>>>>> f0501d1f92318e6ccab1728cf110065b6df30900:src/main/java/com/mycompany/oodjassignment/functions/ReportGenerator.java

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
