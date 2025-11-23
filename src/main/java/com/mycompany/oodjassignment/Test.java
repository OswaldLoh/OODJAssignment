package com.mycompany.oodjassignment;

import com.mycompany.oodjassignment.functions.SendEmail;
import com.mycompany.oodjassignment.functions.ReportGenerator;


public class Test {
    public static void testSendEmail(){       
        SendEmail sendEmail = new SendEmail("Tp078141@mail.apu.edu.my");
        sendEmail.Pdf("test", "test","testing.txt");
        
    }
    
    public static void testReportGenerator(){
        ReportGenerator reportGen = new ReportGenerator("R001", "academic_report.pdf");
        reportGen.generateReport("S001", 1);
    }

    
    public static void main(String[] args) {
        testSendEmail();
        // testReportGenerator(); // Uncomment to test report generation
    }
    
}
