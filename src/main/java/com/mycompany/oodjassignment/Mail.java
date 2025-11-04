package com.mycompany.oodjassignment;


import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Session;

public class Mail {
    public static void main(String[] args) {
        try {
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", "localhost");
            Session session = Session.getInstance(props);
            System.out.println("✅ JavaMail + JAF are working!");
            System.out.println("Session class: " + session.getClass().getName());
            DataHandler dh = new DataHandler("Test", "text/plain");
            System.out.println("✅ JAF working: DataHandler -> " + dh.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
