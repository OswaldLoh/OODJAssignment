package com.mycompany.oodjassignment.functions;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SendEmail {

    private final String senderEmail = "markyisnice@gmail.com"; // the app password only can be generated when email is quite old
    private final String senderPassword = "dbyv ofcy vzia hngt"; // this is the app password for gmail ( app pass dif with gmail pass )
    private String recipientEmail;
    private String fileLocation;

    // constructor
    public SendEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    // getter and setter
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

            message.setSubject(subject);
            message.setText(content);

            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } 
        
        catch (MessagingException e){
            e.printStackTrace();
        }

    }

    public void Pdf(String subject,String content,String fileLocation){
        
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

            // Subject for the email
            message.setSubject(subject);

            //Create MimeBodyPart object and set your message text        
            BodyPart messageBodyPart1 = new MimeBodyPart();     
            messageBodyPart1.setText("This is message body");          

            //Create new MimeBodyPart object and set DataHandler object to this object        
            MimeBodyPart messageBodyPart2 = new MimeBodyPart();      
            DataSource source = new FileDataSource(fileLocation);    
            messageBodyPart2.setDataHandler(new DataHandler(source));    
            messageBodyPart2.setFileName(fileLocation);             

            //Create Multipart object and add MimeBodyPart objects to this object        
            Multipart multipart = new MimeMultipart();    
            multipart.addBodyPart(messageBodyPart1);     
            multipart.addBodyPart(messageBodyPart2);      

            //Set the multipart object to the message object
            message.setContent(multipart );        

            //Send message    
            Transport.send(message);      
            System.out.println("message sent....");   
            // Send the message
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } 
        
        catch (MessagingException e){
            e.printStackTrace();
        }

    }


}

