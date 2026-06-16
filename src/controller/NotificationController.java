package controller;

import dao.NotificationDAO;
import model.Notification;
import java.util.List;
import java.util.ArrayList;

public class NotificationController {

    private final NotificationDAO notificationDAO = new NotificationDAO();
    private final int userId;

    public NotificationController(int userId) {
        this.userId = userId;
    }

    // ─── Get Notifications as formatted data ──────────────────
    // Returns list of String arrays: [id, message, date, isRead]
    // View just displays these - no model import needed
    public List<String[]> getNotificationData() {
        List<Notification> notifications = 
            notificationDAO.getByUser(userId);
        List<String[]> data = new ArrayList<>();

        java.text.SimpleDateFormat sdf = 
            new java.text.SimpleDateFormat("dd MMM yyyy");

        for (Notification n : notifications) {
            data.add(new String[]{
                String.valueOf(n.getId()),
                n.getMessage(),
                sdf.format(n.getCreatedAt()),
                String.valueOf(n.isRead())
            });
        }
        return data;
    }

    // ─── Mark All As Read ─────────────────────────────────────
    public String markAllAsRead() {
        try {
            notificationDAO.markAllAsRead(userId);
            return null;
        } catch (Exception e) {
            return "Failed to mark notifications as read.";
        }
    }

    // ─── Delete Selected ──────────────────────────────────────
    public String deleteNotifications(java.util.List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return "No notifications selected!";
        }
        try {
            notificationDAO.deleteByIds(ids);
            return null;
        } catch (Exception e) {
            return "Failed to delete notifications.";
        }
    }

    public int getUserId() { return userId; }
}