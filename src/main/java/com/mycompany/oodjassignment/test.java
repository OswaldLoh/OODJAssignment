package com.mycompany.oodjassignment;

import com.mycompany.oodjassignment.functions.SendEmail;


public class Test {
    public static void testSendEmail(){       
        SendEmail sendEmail = new SendEmail();
        sendEmail.Pdf("test", "test","testing.txt");
        
    }
    public static void main(String[] args) {
        testSendEmail();
    }
    
}
