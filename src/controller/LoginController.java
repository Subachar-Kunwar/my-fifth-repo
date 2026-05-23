package controller;

import dao.UserDAO;
import model.logindata;
import view.Login;

public class LoginController {
    UserDAO dao = new UserDAO();

    LoginController(Login loginView) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean loginUser(String usernameOrEmail, String password, String confirmPassword) {
        // check if passwords match
        if (!password.equals(confirmPassword)) {
            return false;
        }
        
        // Use the existing constructor with 4 parameters
        logindata user = new logindata(usernameOrEmail, usernameOrEmail, password, confirmPassword);
        
        return dao.checkUser(user);
    }

    public LoginController(String email, String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    void open() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}