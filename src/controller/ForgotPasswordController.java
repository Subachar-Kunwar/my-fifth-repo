package controller;

import dao.UserDAO;
import dao.OtpDAO;
import model.EmailService;

public class ForgotPasswordController {

    private final UserDAO userDAO       = new UserDAO();
    private final OtpDAO otpDAO         = new OtpDAO();
    private final EmailService emailService = new EmailService();
    private String lastOTP = "";

    // ─── Send OTP ─────────────────────────────────────────────
    public String sendOTP(String email) {

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            return "Please enter your email!";
        }
        if (!email.contains("@")) {
            return "Invalid email address!";
        }

        // Check if email exists
        if (!emailExists(email)) {
            return "Email not found in our system!";
        }

        // Generate OTP
        String otp = emailService.generateOTP();

        // Save OTP via DAO
        if (!otpDAO.saveOTP(email, otp)) {
            return "Failed to generate OTP. Please try again.";
        }

        // Send email
        boolean sent = emailService.sendOTPEmail(email, otp);
        lastOTP = otp;

        if (!sent) {
            return "Email failed! Use this OTP: " + otp;
        }

        return null; // ✅ success
    }

    // ─── Verify OTP ───────────────────────────────────────────
    public int verifyOTP(String email, String otp) {
        return otpDAO.verifyOTP(email, otp);
    }

    // ─── Email Exists ─────────────────────────────────────────
    public boolean emailExists(String email) {
        return userDAO.getUserIdByEmail(email) != -1;
    }

    // ─── Get Last OTP ─────────────────────────────────────────
    public String getLastOTP() {
        return lastOTP;
    }
}