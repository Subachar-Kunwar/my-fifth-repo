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
    
    // Get user ID by email
    public int getUserIdByEmail(String email) {
        return dao.getUserIdByEmail(email);
    }
    
    // Add buyer details
    public boolean addBuyerDetails(int userId) {
        return dao.addBuyerDetails(userId);
    }
    
    // Add seller details
    public boolean addSellerDetails(int userId) {
        return dao.addSellerDetails(userId);
    }
}