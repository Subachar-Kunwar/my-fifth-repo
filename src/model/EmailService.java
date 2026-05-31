package model;

import java.util.Properties;
import java.sql.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailService {
    
    private static final String FROM_EMAIL = "subacharkunwar@gmail.com";
    private static final String APP_PASSWORD = "djar atbe dlii qepu";
    
    // Get connection using your existing MySqlConnector
    private Connection getConnection() {
        try {
            Database.MySqlConnector connector = new Database.MySqlConnector();
            return connector.openConnection();
        } catch (Exception e) {
            System.out.println("Connection error: " + e.getMessage());
            return null;
        }
    }
    
    // Close connection
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                Database.MySqlConnector connector = new Database.MySqlConnector();
                connector.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
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
        
        session.setDebug(true);
        
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("ReWear - Password Reset OTP");
            message.setText("Dear User,\n\nYour OTP for password reset is: " + otp + "\n\nThis code will expire in 5 minutes. For security reasons, please do not share it with anyone, including ReWear staff.\n\nRegards,\nRewear Thrift Management System");
            
            Transport.send(message);
            System.out.println("✓ Email sent to " + toEmail);
            return true;
        } catch (MessagingException e) {
            System.out.println("✗ Email failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public String generateOTP() {
        int otp = (int)(Math.random() * 9000) + 1000;      // 4 digits (1000-9999)
        System.out.println("Generated OTP: " + otp);
        return String.valueOf(otp);
    }
    
    // Deletes old OTPs before saving new one + includes failed_attempts
    public boolean saveOTP(String email, String otp) {
        Connection conn = getConnection();
        
        if (conn == null) {
            System.out.println("Failed to get database connection");
            return false;
        }
        
        try {
            // STEP 1: Delete any existing OTPs for this email (Resend OTP fix)
            String deleteSql = "DELETE FROM otp_codes WHERE email = ?";
            PreparedStatement deleteStmt = conn.prepareStatement(deleteSql);
            deleteStmt.setString(1, email);
            int deletedCount = deleteStmt.executeUpdate();
            deleteStmt.close();
            
            if (deletedCount > 0) {
                System.out.println("✓ Deleted " + deletedCount + " old OTP(s) for: " + email);
            }
            
            // STEP 2: Insert new OTP with 5 minutes expiry and 0 failed attempts
            String insertSql = "INSERT INTO otp_codes (email, otp_code, expires_at, failed_attempts) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 5 MINUTE), 0)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setString(1, email);
            insertStmt.setString(2, otp);
            int result = insertStmt.executeUpdate();
            insertStmt.close();
            
            if (result > 0) {
                System.out.println("✓ New OTP saved to database for: " + email);
                return true;
            } else {
                System.out.println("✗ Failed to save OTP - no rows affected");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("✗ SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    // FIXED: Returns int instead of boolean
    // Return values:
    // 0 = success (OTP verified) - GO TO RESET PASSWORD
    // -1 = max attempts reached, OTP expired, or no OTP found - BLOCK ACCESS
    // 1,2 = remaining attempts left - SHOW ERROR WITH REMAINING ATTEMPTS
    public int verifyOTP(String email, String otp) {
        Connection conn = getConnection();
        
        if (conn == null) return -1;
        
        try {
            // FIRST: Check if any OTP exists for this email (not expired)
            String checkSql = "SELECT * FROM otp_codes WHERE email = ? AND expires_at > NOW()";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet checkRs = checkStmt.executeQuery();
            
            if (!checkRs.next()) {
                // No OTP found for this email
                checkRs.close();
                checkStmt.close();
                System.out.println("✗ No OTP found for: " + email);
                return -1; // No OTP exists - block access
            }
            
            // Get current failed attempts and actual OTP code
            int currentFailedAttempts = checkRs.getInt("failed_attempts");
            String actualOtpCode = checkRs.getString("otp_code");
            checkRs.close();
            checkStmt.close();
            
            // Check if max attempts already reached
            if (currentFailedAttempts >= 3) {
                deleteOTP(email);
                System.out.println("✗ Max attempts already reached for: " + email);
                return -1; // Max attempts reached - block access
            }
            
            // Now verify the OTP code
            if (actualOtpCode.equals(otp)) {
                // OTP is CORRECT - success!
                deleteOTP(email);
                System.out.println("✓ OTP verified for: " + email);
                return 0; // Success - allow reset password
            } else {
                // OTP is WRONG - increment failed attempts
                int newFailedAttempts = currentFailedAttempts + 1;
                String updateSql = "UPDATE otp_codes SET failed_attempts = ? WHERE email = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setInt(1, newFailedAttempts);
                updateStmt.setString(2, email);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                int remaining = 3 - newFailedAttempts;
                System.out.println("✗ Invalid OTP for: " + email + " (Attempt " + newFailedAttempts + "/3) - " + remaining + " attempts left");
                
                // If this was the 3rd wrong attempt, delete the OTP
                if (newFailedAttempts >= 3) {
                    deleteOTP(email);
                    System.out.println("✗ Max attempts reached - OTP deleted for: " + email);
                    return -1; // Max attempts reached - block access
                }
                
                return remaining; // Returns 2 or 1 - show remaining attempts
            }
            
        } catch (SQLException e) {
            System.out.println("✗ OTP verification error: " + e.getMessage());
            return -1;
        } finally {
            closeConnection(conn);
        }
    }
    
    private void deleteOTP(String email) {
        String sql = "DELETE FROM otp_codes WHERE email = ?";
        Connection conn = getConnection();
        
        if (conn == null) return;
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.executeUpdate();
            pstmt.close();
            System.out.println("✓ OTP deleted for: " + email);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(conn);
        }
    }
    
    public boolean emailExists(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        Connection conn = getConnection();
        
        if (conn == null) return false;
        
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            return exists;
        } catch (SQLException e) {
            System.out.println("✗ Error checking email: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}