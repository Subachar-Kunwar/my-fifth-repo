package model;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {

    private static final String FROM_EMAIL = "subacharkunwar@gmail.com";
    private static final String APP_PASSWORD = "djar atbe dlii qepu";

    // ─── Send OTP Email ───────────────────────────────────────
    // Only sends email - no DB logic
    public boolean sendOTPEmail(String toEmail, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail));
            message.setSubject("ReWear - Password Reset OTP");
            message.setText(
                "Dear User,\n\n" +
                "Your OTP for password reset is: " + otp + "\n\n" +
                "This code will expire in 5 minutes.\n" +
                "Do not share it with anyone.\n\n" +
                "Regards,\nReWear Team");

            Transport.send(message);
            System.out.println("Email sent to " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.out.println("Email failed: " + e.getMessage());
            return false;
        }
    }

    // ─── Generate OTP ─────────────────────────────────────────
    // Only generates OTP - no DB logic
    public String generateOTP() {
        int otp = (int)(Math.random() * 9000) + 1000;
        System.out.println("Generated OTP: " + otp);
        return String.valueOf(otp);
    }
}