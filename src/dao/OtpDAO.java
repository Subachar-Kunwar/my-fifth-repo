package dao;

import Database.MySqlConnector;
import java.sql.*;

public class OtpDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Save OTP ─────────────────────────────────────────────
    public boolean saveOTP(String email, String otp) {
        Connection conn = mysql.openConnection();

        // Step 1: Delete old OTPs for this email
        String deleteSql = "DELETE FROM otp_codes WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(deleteSql)) {
            ps.setString(1, email);
            int deleted = ps.executeUpdate();
            if (deleted > 0) {
                System.out.println("Deleted old OTPs for: " + email);
            }
        } catch (SQLException e) {
            System.out.println("Delete OTP error: " + e.getMessage());
        }

        // Step 2: Insert new OTP
        String insertSql = "INSERT INTO otp_codes " +
                           "(email, otp_code, expires_at, failed_attempts) " +
                           "VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 5 MINUTE), 0)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
            ps.setString(1, email);
            ps.setString(2, otp);
            boolean saved = ps.executeUpdate() > 0;
            if (saved) System.out.println("OTP saved for: " + email);
            return saved;
        } catch (SQLException e) {
            System.out.println("Save OTP error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Verify OTP ───────────────────────────────────────────
    // Returns:
    //  0  = success
    // -1  = blocked / expired / not found
    //  1,2 = remaining attempts
    public int verifyOTP(String email, String otp) {
        Connection conn = mysql.openConnection();

        String checkSql = "SELECT otp_code, failed_attempts " +
                          "FROM otp_codes " +
                          "WHERE email = ? AND expires_at > NOW()";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {       // ✅ rs closed

                if (!rs.next()) {
                    System.out.println("No OTP found for: " + email);
                    return -1;
                }

                int failedAttempts = rs.getInt("failed_attempts");
                String actualOtp   = rs.getString("otp_code");

                // Check max attempts
                if (failedAttempts >= 3) {
                    deleteOTP(email, conn);
                    return -1;
                }

                // Check if OTP matches
                if (actualOtp.equals(otp)) {
                    deleteOTP(email, conn);
                    System.out.println("OTP verified for: " + email);
                    return 0; // ✅ success
                }

                // Wrong OTP - increment attempts
                int newFailed  = failedAttempts + 1;
                int remaining  = 3 - newFailed;

                String updateSql = "UPDATE otp_codes " +
                                   "SET failed_attempts = ? " +
                                   "WHERE email = ?";
                try (PreparedStatement up =
                        conn.prepareStatement(updateSql)) {
                    up.setInt(1, newFailed);
                    up.setString(2, email);
                    up.executeUpdate();
                }

                if (newFailed >= 3) {
                    deleteOTP(email, conn);
                    return -1;
                }

                return remaining;
            }
        } catch (SQLException e) {
            System.out.println("Verify OTP error: " + e.getMessage());
            return -1;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Delete OTP ───────────────────────────────────────────
    private void deleteOTP(String email, Connection conn) {
        String sql = "DELETE FROM otp_codes WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.executeUpdate();
            System.out.println("OTP deleted for: " + email);
        } catch (SQLException e) {
            System.out.println("Delete OTP error: " + e.getMessage());
        }
    }

    // ─── Public delete OTP ────────────────────────────────────
    public void deleteOTP(String email) {
        Connection conn = mysql.openConnection();
        deleteOTP(email, conn);
        mysql.closeConnection(conn);
    }
}