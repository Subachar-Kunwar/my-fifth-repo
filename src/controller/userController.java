package controller;

import dao.UserDAO;
import model.logindata;

public class userController {

    UserDAO dao = new UserDAO();

    public boolean registerUser(logindata user) {

        // Check existing user
        if (dao.checkUser(user)) {
            return false;
        }

        return dao.createUser(user);
    }
}