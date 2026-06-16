package controller;

import dao.UserDAO;
import dao.OtpDAO;

public class ResetController {

    private final UserDAO userDAO = new UserDAO();
    private final OtpDAO otpDAO   = new OtpDAO();

    // ─── Reset Password ───────────────────────────────────────
    // Returns null on success, error message on failure
    public String resetPassword(String email,
                                String newPassword,
                                String confirmPassword) {

        // Step 1: Validate empty fields
        if (newPassword == null || newPassword.trim().isEmpty()) {
            return "New password cannot be empty!";
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Please confirm your password!";
        }

        // Step 2: Validate password length
        if (newPassword.length() < 8) {
            return "Password must be at least 8 characters long!";
        }

        // Step 3: Validate password match
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        // Step 4: Update password via DAO
        boolean success = userDAO.updatePassword(email, newPassword);
        return success ? null : "Failed to update password. Please try again.";
    }

    // ─── Email Exists ─────────────────────────────────────────
    public boolean emailExists(String email) {
        return userDAO.getUserIdByEmail(email) != -1;
    }

    // ─── Verify OTP ───────────────────────────────────────────
    public int verifyOTP(String email, String otp) {
        return otpDAO.verifyOTP(email, otp);
    }
}