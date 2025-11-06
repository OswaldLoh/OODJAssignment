package com.mycompany.oodjassignment.functions;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendEmail {
    public static void Main(String[] args) {
        // Sender’s Gmail credentials
        final String senderEmail = "markyisnice@gmail.com";
        final String senderPassword = "dbyv ofcy vzia hngt"; // use app password!

        // Receiver email
        String recipientEmail = "tp078141@mail.apu.edu.my";

        // SMTP server settings for Gmail
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Create a session with authentication
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            message.setSubject("Inforamtion regarding the notification");
            message.setText("this is the trsting");

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");

        } 
        
        catch (MessagingException e){
            e.printStackTrace();
        }
    }
}
