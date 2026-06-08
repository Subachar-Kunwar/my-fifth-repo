package controller;

import dao.UserDAO;
import model.logindata;

public class SignupController {

    private final UserDAO userDao = new UserDAO();

    public boolean registerUser(String username, String email,
                                String password, String userType) {
        // Check if user already exists
       logindata user = new logindata(username, email, password, userType);
    if (userDao.checkUser(user)) {
    return false;
}
return userDao.createUser(user);
    }

    public int getUserIdByEmail(String email) {
        return userDao.getUserIdByEmail(email);
    }

    public boolean addBuyerDetails(int userId) {
        return userDao.addBuyerDetails(userId);
    }

    public boolean addSellerDetails(int userId) {
        return userDao.addSellerDetails(userId);
    }
}