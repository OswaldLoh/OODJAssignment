package com.mycompany.oodjassignment.functions;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    private String senderEmail ; // the app password only can be generated when email is quite old
    private String senderPassword; // this is the app password for gmail ( app pass dif with gmail pass )
    private String recipientEmail;
    private String fileLocation;

    // constructor
    public SendEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
        this.senderEmail = "markyisnice@gmail.com";
        this.senderPassword = "aysj ecmc qivs rkwz";
    }

    // getter and setter
        public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public void setSenderPassword(String senderPassword) {
        this.senderPassword = senderPassword;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getFileLocation() {
        return fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }


    // method
    public void Notification(String subject,String content){

        // SMTP server configuration using Gmail
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
            // Set the recipient and sender email address
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            // Set the subject and content for the email
            message.setSubject(subject);
            message.setText(content);

            //Send message    
            System.out.println("message in sending...");
            Transport.send(message);      

            System.out.println("Email have been send successfully!");
        } 
        
        // Using the more detailed exception handling to catch the specific email sending issues
        catch (AuthenticationFailedException e) {
            System.out.println("Authentication have been failed ( Check the username and the password ) ");
        } 
        catch (SendFailedException e) {
            System.out.println("Fail to send the message" );
        }
        // Using the more detailed exception handling to catch the specific email sending issues
        catch (MessagingException e) {
            System.out.println("Error: \n" + e.getMessage());
        }

    }

    public void Pdf(String subject,String content,String fileLocation){
        
        // SMTP server configuration using Gmail
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
            // Set the recipient and sender email address
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipientEmail)
            );

            // Set the subject for the email
            message.setSubject(subject);

            // Set the file attachment to the email       
            MimeBodyPart messageAttach = new MimeBodyPart();      
            DataSource source = new FileDataSource(fileLocation);    
            messageAttach.setDataHandler(new DataHandler(source));    
            messageAttach.setFileName(fileLocation);             
            
            // Set the content for the email        
            BodyPart messageBody = new MimeBodyPart();     
            messageBody.setText(content);          

            // Combine the message body and attachment into a multipart        
            Multipart multipart = new MimeMultipart();    
            multipart.addBodyPart(messageAttach);      
            multipart.addBodyPart(messageBody);     

            //Set the multipart object to the message object
            message.setContent(multipart );        

            //Send message    
            System.out.println("message in sending...");
            Transport.send(message);      

            System.out.println("Email have been send successfully!");
        } 
        
        // Using the more detailed exception handling to catch the specific email sending issues
        catch (AuthenticationFailedException e) {
            System.out.println("Authentication failed ( Check the username and the password )" );
        } 
        catch (SendFailedException e) {
            System.out.println("Fail to send the message" );
        }
        // Using the more detailed exception handling to catch the specific email sending issues
        catch (MessagingException e) {
            System.out.println("Error: \n" + e.getMessage());
        }

    }

    @Override
    public String toString() {
        return "SendEmail{" + "senderEmail=" + senderEmail + ", senderPassword=" + senderPassword + ", recipientEmail=" + recipientEmail + ", fileLocation=" + fileLocation + '}';
    }

}

