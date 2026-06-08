package controller;
import dao.UserDAO;

public class ResetController {

    private final UserDAO userDAO = new UserDAO();

    public boolean resetPassword(String email, String newPassword) {
        return userDAO.updatePassword(email, newPassword);
    }

    public boolean emailExists(String email) {
        return userDAO.getUserIdByEmail(email) != -1;
    }

    public boolean resendOTP(String userEmail) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}