package controller;

import dao.UserDAO;

public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    // ─── Login ────────────────────────────────────────────────
    // Returns null on failure
    // Returns String[3] on success: [0]=username, [1]=role, [2]=userId
    public String[] login(String username, String email, String password) {

        // Validate inputs - Controller handles validation
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (email == null || !email.contains("@")) {
            return null;
        }
        if (password == null || password.length() < 8) {
            return null;
        }

        // ✅ Delegate DB work to DAO - not Controller
        return userDAO.login(username, email, password);
    }
}