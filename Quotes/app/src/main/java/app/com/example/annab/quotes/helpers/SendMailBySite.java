package app.com.example.annab.quotes.helpers;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * SendMailBySite
 * Provides a method that sends an email to the current user with a link of a quote.
 *
 * @author anna
 */

public class SendMailBySite {

    /**
     * Sends an email to the current user.
     *
     * @param currentUser the user that is currently logged in.
     * @param quoteurl    the url for the particular quote.
     */
    public static void sendEmail(String currentUser, String quoteurl) {

        // the account that sends the email to the user
        final String SENDER = "healthruwords98@gmail.com";
        final String SENDER_PASSWORD = "healthruwords";


        //Get the session object
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER, SENDER_PASSWORD);
                    }
                });

        //Compose the message
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(currentUser));
            message.setSubject("HealThruWords - Quote Link");
            message.setText("Link to the quote " + quoteurl + "\n" + "\n" + "" +
                    "Thank you for using HealThruWords.");

            //send the message
            Transport.send(message);

            System.out.println("message sent successfully...");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
