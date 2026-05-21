package controller;

import dao.UserDAO;

public class ResetController {

    public boolean resetPassword(String email, String newPassword) {

        UserDAO dao = new UserDAO();
        return dao.updatePassword(email, newPassword);
    }
}