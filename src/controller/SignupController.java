package controller;

import dao.UserDAO;
import model.logindata;

public class SignupController {

    private final UserDAO userDao = new UserDAO();

    // ─── Register User ────────────────────────────────────────
    // Returns null on success, error message on failure
    public String registerUser(String username, String email,
                               String password, String confirmPassword,
                               String userType) {

        // Step 1: Validate empty fields
        if (username == null || username.trim().isEmpty()) {
            return "Username is required!";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email is required!";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required!";
        }
        if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
            return "Please confirm your password!";
        }
        if (userType == null) {
            return "Please select user type! Choose Buyer or Seller.";
        }

        // Step 2: Validate email format
        if (!email.contains("@gmail.com")) {
            return "Please enter a valid email!\nExample: user@gmail.com";
        }

        // Step 3: Validate password length
        if (password.length() < 8) {
            return "Password must be at least 8 characters long!";
        }

        // Step 4: Validate password match
        if (!password.equals(confirmPassword)) {
            return "Passwords do not match!";
        }

        // Step 5: Check if user already exists
        // Use original (non-encrypted) email/username — only password gets encrypted
        logindata checkData = new logindata(username, email, password, userType);
        if (userDao.checkUser(checkData)) {
            return "Username or Email already exists!";
        }

        // ✅ Step 6: ENCRYPT password before saving
        String encryptedPassword = PasswordEncryptor.encrypt(password);
        if (encryptedPassword == null) {
            return "Encryption failed! Please try again.";
        }

        // Step 7: Create user with ENCRYPTED password
        logindata user = new logindata(username, email, encryptedPassword, userType);
        boolean created = userDao.createUser(user);
        if (!created) {
            return "Registration failed! Please try again.";
        }

        // Step 8: Add buyer/seller details
        int userId = userDao.getUserIdByEmail(email);
        if (userId != -1) {
            if (userType.equals("buyer")) {
                userDao.addBuyerDetails(userId);
            } else {
                userDao.addSellerDetails(userId);
            }
        }

        return null; // ✅ null means success
    }

    // ─── Helper methods ───────────────────────────────────────
    public int getUserIdByEmail(String email) {
        return userDao.getUserIdByEmail(email);
    }

    public boolean addBuyerDetails(int userId) {
        return userDao.addBuyerDetails(userId);
    }

    public boolean addSellerDetails(int userId) {
        return userDao.addSellerDetails(userId);
    }
}