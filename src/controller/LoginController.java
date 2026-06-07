package controller;

import dao.UserDAO;
import view.Login;

/**
 * LoginController - handles login view logic
 */
public class LoginController {

    private final UserDAO dao = new UserDAO();
    private final Login loginView;

    /**
     * ✅ Primary constructor - properly initializes
     */
    public LoginController(Login loginView) {
        this.loginView = loginView;
    }

    public void open() {
        this.loginView.setVisible(true);
    }

    public void close() {
        this.loginView.dispose();
    }

    /**
     * Optional: validate login credentials
     */
    public boolean loginUser(String username, String email, String password) {
        if (username == null || username.isEmpty()) return false;
        if (email == null || !email.contains("@")) return false;
        if (password == null || password.length() < 8) return false;
        return dao.checkUser(new model.logindata(username, email, password));
    }
}