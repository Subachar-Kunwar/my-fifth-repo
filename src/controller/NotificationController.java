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
    public List<String[]> getNotificationData() {
        List<Notification> notifications = notificationDAO.getByUser(userId);
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

    // ─── Populate Panel ───────────────────────────────────────
    public void populateNotificationPanel(javax.swing.JPanel panel) {
        panel.removeAll();

        List<String[]> data = getNotificationData();

        if (data.isEmpty()) {
            javax.swing.JLabel none =
                new javax.swing.JLabel("No notifications.");
            none.setFont(new java.awt.Font("Segoe UI", 1, 16));
            none.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            panel.add(none);
        } else {
            for (String[] item : data) {
                panel.add(buildCard(
                    Integer.parseInt(item[0]),
                    item[1], item[2],
                    Boolean.parseBoolean(item[3])
                ));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    // ─── Build a single card ──────────────────────────────────
    private javax.swing.JPanel buildCard(
            int id, String message, String date, boolean isRead) {

        javax.swing.JPanel panel = new javax.swing.JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(900, 60));
        panel.setBackground(isRead
            ? new java.awt.Color(200, 230, 200)
            : new java.awt.Color(170, 218, 172));

        javax.swing.JCheckBox check = new javax.swing.JCheckBox();
        check.setBounds(20, 15, 30, 30);
        check.setBackground(panel.getBackground());
        check.putClientProperty("notifId", id);

        javax.swing.JLabel msg = new javax.swing.JLabel(message);
        msg.setBounds(70, 20, 600, 20);

        javax.swing.JLabel dateLabel = new javax.swing.JLabel(date);
        dateLabel.setBounds(720, 20, 150, 20);

        panel.add(check);
        panel.add(msg);
        panel.add(dateLabel);

        return panel;
    }

    // ─── Collect selected IDs ─────────────────────────────────
    public List<Integer> getSelectedIds(javax.swing.JPanel panel) {
        List<Integer> ids = new ArrayList<>();

        for (java.awt.Component comp : panel.getComponents()) {
            if (comp instanceof javax.swing.JPanel) {
                for (java.awt.Component inner :
                        ((javax.swing.JPanel) comp).getComponents()) {
                    if (inner instanceof javax.swing.JCheckBox) {
                        javax.swing.JCheckBox cb = (javax.swing.JCheckBox) inner;
                        if (cb.isSelected()) {
                            ids.add((Integer) cb.getClientProperty("notifId"));
                        }
                    }
                }
            }
        }
        return ids;
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
    public String deleteNotifications(List<Integer> ids) {
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