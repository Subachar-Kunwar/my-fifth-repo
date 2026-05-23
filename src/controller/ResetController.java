package controller;

import dao.UserDAO;

public class ResetController {

    public boolean resetPassword(String email, String newPassword) {

        UserDAO dao = new UserDAO();
        return dao.updatePassword(email, newPassword);
    }

    public boolean resendOTP(String userEmail) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}