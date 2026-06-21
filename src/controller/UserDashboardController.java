package controller;

import dao.UserDAO;
import dao.OrderDAO;
import dao.ActivityDAO;
import model.DashboardOrder;

import java.util.ArrayList;
import java.util.List;

public class UserDashboardController {

    private final UserDAO userDAO         = new UserDAO();
    private final OrderDAO orderDAO       = new OrderDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();

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

    public String getUsername() { return loggedInUsername; }
    public int getUserId()      { return loggedInUserId; }

    public String getUserEmail() {
        return userDAO.getEmailById(loggedInUserId);
    }

    // ─── Dashboard Orders (max 4) ─────────────────────────────
    public List<DashboardOrder> getDashboardOrders() {
        List<String[]> orders = orderDAO.getRecentOrdersByUser(loggedInUserId);
        List<DashboardOrder> dashboardOrders = new ArrayList<>();

        for (String[] order : orders) {
            dashboardOrders.add(new DashboardOrder(
                order[0], order[1], order[2], order[3]
            ));
        }
        return dashboardOrders;
    }

    // ─── Dashboard Activities (max 3) ─────────────────────────
    public String[] getDashboardActivities() {
        List<String> activities = activityDAO.getRecentActivitiesByUser(loggedInUserId);
        String[] result = {"", "", ""};
        for (int i = 0; i < activities.size() && i < 3; i++) {
            result[i] = activities.get(i);
        }
        return result;
    }
}