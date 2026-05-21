package controller;

import dao.UserDAO;
import model.User;

public class LoginController {

    public boolean login(String email, String password) {

        User user = new User(email, password);

        UserDAO dao = new UserDAO();

        return dao.loginUser(user);
    }
}