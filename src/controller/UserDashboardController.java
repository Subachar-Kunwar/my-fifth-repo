package controller;

import dao.UserDAO;
import dao.OrderDAO;
import dao.ActivityDAO;

public class UserDashboardController {

    private final UserDAO userDAO           = new UserDAO();
    private final OrderDAO orderDAO         = new OrderDAO();
    private final ActivityDAO activityDAO   = new ActivityDAO();
    private final String loggedInUsername;
    private final int loggedInUserId;

    public UserDashboardController(String username, int userId) {
        this.loggedInUsername = username;
        this.loggedInUserId   = userId;
    }

    // ─── Welcome Message ──────────────────────────────────────
    public String getWelcomeMessage() {
        return "Welcome, " + loggedInUsername + "!";
    }

    // ─── Username ─────────────────────────────────────────────
    public String getUsername() {
        return loggedInUsername;
    }

    // ─── User ID ──────────────────────────────────────────────
    public int getUserId() {
        return loggedInUserId;
    }

    // ─── User Email ───────────────────────────────────────────
    public String getUserEmail() {
        return userDAO.getEmailById(loggedInUserId);
    }

    // ─── Recent Orders ────────────────────────────────────────
    public java.util.List<String[]> getRecentOrders() {
        return orderDAO.getRecentOrdersByUser(loggedInUserId);
    }

    // ─── Recent Activities ────────────────────────────────────
    public java.util.List<String> getRecentActivities() {
        return activityDAO.getRecentActivitiesByUser(loggedInUserId);
    }

    // ❌ REMOVED confirmLogout() - UI dialogs belong in View
}