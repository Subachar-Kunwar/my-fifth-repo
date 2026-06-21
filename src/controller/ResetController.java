package controller;

import dao.UserDAO;
import dao.OtpDAO;

public class ResetController {

    private final UserDAO userDAO = new UserDAO();
    private final OtpDAO otpDAO   = new OtpDAO();

    private static final String PLACEHOLDER = "********";

    // ─── Reset Password ───────────────────────────────────────
    public String resetPassword(String email,
                                String newPassword,
                                String confirmPassword) {

        if (newPassword == null || newPassword.trim().isEmpty()) {
            return "New password cannot be empty!";
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Please confirm your password!";
        }
        if (newPassword.length() < 8) {
            return "Password must be at least 8 characters long!";
        }
        if (!newPassword.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        // ✅ Encrypt the new password before saving
        String encryptedPassword = PasswordEncryptor.encrypt(newPassword);
        if (encryptedPassword == null) {
            return "Encryption failed! Please try again.";
        }

        // ✅ Save encrypted password to DB
        boolean success = userDAO.updatePassword(email, encryptedPassword);
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

    // ─── Password Placeholder Setup ───────────────────────────
    public static void setupPasswordPlaceholder(javax.swing.JPasswordField field) {
        field.setEchoChar((char) 0);
        field.setText(PLACEHOLDER);

        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                String current = new String(field.getPassword());
                if (current.equals(PLACEHOLDER)) {
                    field.setText("");
                    field.setEchoChar('*');
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0);
                    field.setText(PLACEHOLDER);
                }
            }
        });
    }
}