package model;

import Database.MySqlConnector;
import java.sql.*;
import java.util.Random;

public class OTPService {
    
    private MySqlConnector dbConnector = new MySqlConnector();
    
    // Generate 4-digit OTP (matches your 4 boxes)
    public String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);  // 1000 to 9999
        return String.valueOf(otp);
    }
    
    // Save OTP to database (with failed_attempts = 0)
    public boolean saveOTP(String email, String otpCode) {
        // Delete old OTPs first
        deleteOldOTPs(email);
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = dbConnector.openConnection();
            String query = "INSERT INTO otp_codes (email, otp_code, expires_at, failed_attempts) VALUES (?, ?, ?, 0)";
            pstmt = conn.prepareStatement(query);
            
            // Expires in 5 minutes (changed from 10)
            Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + (5 * 60 * 1000));
            
            pstmt.setString(1, email);
            pstmt.setString(2, otpCode);
            pstmt.setTimestamp(3, expiresAt);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error saving OTP: " + e.getMessage());
            return false;
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) dbConnector.closeConnection(conn);
            } catch (SQLException e) {}
        }
    }
    
    private void deleteOldOTPs(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbConnector.openConnection();
            pstmt = conn.prepareStatement("DELETE FROM otp_codes WHERE email = ?");
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (Exception e) {} finally {
            try { if (pstmt != null) pstmt.close(); if (conn != null) dbConnector.closeConnection(conn); } catch (Exception e) {}
        }
    }
    
    // NEW: Get remaining attempts
    public int getRemainingAttempts(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnector.openConnection();
            String query = "SELECT failed_attempts FROM otp_codes WHERE email = ? AND expires_at > NOW()";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int failedAttempts = rs.getInt("failed_attempts");
                int remaining = 3 - failedAttempts;
                return Math.max(remaining, 0);
            }
            return 3; // No OTP found, assume 3 attempts available
        } catch (SQLException e) {
            System.out.println("Error getting attempts: " + e.getMessage());
            return 3;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) dbConnector.closeConnection(conn);
            } catch (SQLException e) {}
        }
    }
    
    // UPDATED: Verify OTP with 3 attempts tracking
    public int verifyOTP(String email, String otpCode) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbConnector.openConnection();
            
            // Check if OTP exists and is valid
            String query = "SELECT * FROM otp_codes WHERE email = ? AND otp_code = ? AND expires_at > NOW()";
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            pstmt.setString(2, otpCode);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                // OTP is correct - check failed attempts
                int failedAttempts = rs.getInt("failed_attempts");
                
                if (failedAttempts >= 3) {
                    // Max attempts reached
                    deleteOldOTPs(email);
                    return -1; // Max attempts reached
                }
                
                // Successful verification - delete OTP
                deleteOldOTPs(email);
                return 0; // Success
            } else {
                // Wrong OTP - increment failed attempts
                rs.close();
                pstmt.close();
                
                String updateSql = "UPDATE otp_codes SET failed_attempts = failed_attempts + 1 WHERE email = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, email);
                updateStmt.executeUpdate();
                updateStmt.close();
                
                // Get remaining attempts
                String checkSql = "SELECT failed_attempts FROM otp_codes WHERE email = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, email);
                ResultSet attemptRs = checkStmt.executeQuery();
                
                int remaining = 0;
                if (attemptRs.next()) {
                    int attempts = attemptRs.getInt("failed_attempts");
                    remaining = 3 - attempts;
                    
                    if (attempts >= 3) {
                        deleteOldOTPs(email);
                        remaining = 0;
                    }
                }
                attemptRs.close();
                checkStmt.close();
                
                return remaining; // Returns 2, 1, or 0
            }
            
        } catch (SQLException e) {
            System.out.println("Error verifying OTP: " + e.getMessage());
            return -1;
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) dbConnector.closeConnection(conn);
            } catch (SQLException e) {}
        }
    }
}