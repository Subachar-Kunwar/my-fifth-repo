package controller;
import dao.UserDAO;
import dao.OrderDAO;
import dao.ActivityDAO;

public class UserDashboardController {
    private final UserDAO userDAO = new UserDAO();
    private final OrderDAO orderDAO = new OrderDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private String loggedInUsername;
    private int loggedInUserId;

    public UserDashboardController(String username, int userId) {
        this.loggedInUsername = username;
        this.loggedInUserId = userId;
    }

    public String getWelcomeMessage() {
        return "Welcome, " + loggedInUsername + "!";
    }

    public String getUsername() {
        return loggedInUsername;
    }

    public int getUserId() {
        return loggedInUserId;
    }

    public String getUserEmail() {
        return userDAO.getEmailById(loggedInUserId);
    }

    public java.util.List<String[]> getRecentOrders() {
        return orderDAO.getRecentOrdersByUser(loggedInUserId);
    }

    public java.util.List<String> getRecentActivities() {
        return activityDAO.getRecentActivitiesByUser(loggedInUserId);
    }

    public boolean confirmLogout(java.awt.Component parent) {
        int confirm = javax.swing.JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to log out?",
            "Log Out",
            javax.swing.JOptionPane.YES_NO_OPTION);
        return confirm == javax.swing.JOptionPane.YES_OPTION;
    }
}