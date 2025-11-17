package com.mycompany.oodjassignment;

import com.mycompany.oodjassignment.functions.SendEmail;


public class Test {
    public static void testSendEmail(){       
        SendEmail sendEmail = new SendEmail("Tp078141@mail.apu.edu.my");
        sendEmail.Pdf("test", "tests","testing.txt");
        
    }
    public static void main(String[] args) {
        testSendEmail();
    }
    
}
