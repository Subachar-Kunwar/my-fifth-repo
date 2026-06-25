package controller;

import dao.UserDAO;
import model.User;
import model.Buyer;
import model.Seller;
import model.Admin;

public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    // ─── Login (returns raw data) ─────────────────────────────
    // Returns null on failure
    // Returns String[3] on success: [0]=username, [1]=role, [2]=userId
    public String[] login(String username, String email, String password) {

        if (username == null || username.trim().isEmpty()) return null;
        if (email == null || !email.contains("@"))        return null;
        if (password == null || password.length() < 8)    return null;

        // ✅ Step 1: Get user from DB by username + email
        String[] userData = userDAO.getUserByUsernameAndEmail(username, email);
        if (userData == null) {
            return null; // user not found
        }

        String dbHashedPassword = userData[2];

        // ✅ Step 2: Use BCrypt to verify password
        boolean passwordMatch = PasswordEncryptor.verify(password, dbHashedPassword);
        if (!passwordMatch) {
            return null; // wrong password
        }

        // ✅ Step 3: Return same format as before (View doesn't change)
        return new String[]{
            userData[1],   // username
            userData[3],   // role
            userData[0]    // userId
        };
    }

    // ─── Login with Polymorphism ──────────────────────────────
    // ✅ Returns correct User subclass based on role
    public User loginAsUser(String username, String email, String password) {

        String[] result = login(username, email, password);
        if (result == null) return null;

        String loggedUsername = result[0];
        String role           = result[1];
        int userId            = Integer.parseInt(result[2]);
        String userEmail      = userDAO.getEmailById(userId);

        // ✅ POLYMORPHISM — returns different object based on role
        switch (role.toLowerCase()) {
            case "buyer":
                return new Buyer(userId, loggedUsername, userEmail);
            case "seller":
                return new Seller(userId, loggedUsername, userEmail);
            case "admin":
                return new Admin(userId, loggedUsername, userEmail);
            default:
                return new Buyer(userId, loggedUsername, userEmail);
        }
    }
}