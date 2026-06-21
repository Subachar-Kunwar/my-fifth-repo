package controller;

import dao.OrderDAO;
import model.OrderHistory;
import java.util.List;

public class OrderHistoryController {

    private final OrderDAO orderDAO = new OrderDAO();

    // ─── Get Order History ────────────────────────────────────
    // If isAdmin = true → returns ALL orders
    // If isAdmin = false → returns only that user's orders
    public List<OrderHistory> getOrderHistory(int userId, boolean isAdmin) {
        if (isAdmin) {
            return orderDAO.getAllOrders();
        } else {
            return orderDAO.getOrdersByUserId(userId);
        }
    }

    // ─── Format Helpers ───────────────────────────────────────
    public String getEmptyMessage(boolean isAdmin) {
        return isAdmin ? "No orders in the system yet."
                       : "No orders yet.";
    }

    public String formatPrice(double amount) {
        return "Rs " + (int) amount;
    }

    public String formatDate(java.sql.Date date) {
        return date.toString();
    }

    // ─── Populate Order Panel ─────────────────────────────────
    public void populateOrderPanel(javax.swing.JPanel panel,
                                   int userId, boolean isAdmin) {
        panel.removeAll();

        List<OrderHistory> orders = getOrderHistory(userId, isAdmin);

        if (orders.isEmpty()) {
            panel.add(buildEmptyLabel(isAdmin));
        } else {
            for (OrderHistory order : orders) {
                panel.add(buildCard(order, isAdmin));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    // ─── Build Single Card ────────────────────────────────────
    private javax.swing.JPanel buildCard(OrderHistory order, boolean isAdmin) {
        javax.swing.JPanel panel = new javax.swing.JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(900, 60));
        panel.setBackground(new java.awt.Color(170, 218, 172));

        javax.swing.JLabel nameLabel = new javax.swing.JLabel(order.getProductName());
        nameLabel.setBounds(20, 20, 250, 20);
        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));

        javax.swing.JLabel dateLabel = new javax.swing.JLabel(formatDate(order.getOrderDate()));
        dateLabel.setBounds(300, 20, 150, 20);

        javax.swing.JLabel priceLabel = new javax.swing.JLabel(formatPrice(order.getTotalAmount()));
        priceLabel.setBounds(480, 20, 100, 20);

        javax.swing.JLabel statusLabel = new javax.swing.JLabel(order.getStatus());
        statusLabel.setBounds(620, 20, 150, 20);

        panel.add(nameLabel);
        panel.add(dateLabel);
        panel.add(priceLabel);
        panel.add(statusLabel);

        // ✅ Admin sees extra info: User ID
        if (isAdmin) {
            javax.swing.JLabel userLabel = new javax.swing.JLabel(
                "User #" + order.getUserId());
            userLabel.setBounds(780, 20, 100, 20);
            userLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
            userLabel.setForeground(new java.awt.Color(0, 0, 153));
            panel.add(userLabel);
        }

        return panel;
    }

    // ─── Build Empty Label ────────────────────────────────────
    private javax.swing.JLabel buildEmptyLabel(boolean isAdmin) {
        javax.swing.JLabel none = new javax.swing.JLabel(getEmptyMessage(isAdmin));
        none.setFont(new java.awt.Font("Segoe UI", 1, 16));
        none.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        return none;
    }
}