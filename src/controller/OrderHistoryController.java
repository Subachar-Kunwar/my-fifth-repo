package controller;

import dao.OrderDAO;
import dao.UserDAO;
import dao.NotificationDAO;
import model.OrderHistory;
import java.util.List;

public class OrderHistoryController {

    private final OrderDAO orderDAO        = new OrderDAO();
    private final UserDAO userDAO          = new UserDAO();
    private final NotificationDAO notifDAO = new NotificationDAO();

    public List<OrderHistory> getOrderHistory(int userId, boolean isAdmin) {
        if (isAdmin) return orderDAO.getAllOrders();
        else return orderDAO.getOrdersByUserId(userId);
    }

    public String cancelOrder(int orderId, String reason) {
        boolean success = orderDAO.cancelOrder(orderId, reason);
        return success ? null : "Cannot cancel this order. It may have been shipped already.";
    }

    public String approvePayment(int orderId) {
        boolean success = orderDAO.approveOrder(orderId);
        return success ? null : "Failed to approve order.";
    }

    public String markAsShipped(int orderId) {
        boolean success = orderDAO.markAsShipped(orderId);
        return success ? null : "Failed to mark as shipped.";
    }

    public String markAsDelivered(int orderId) {
        boolean success = orderDAO.markAsDelivered(orderId);
        return success ? null : "Failed to mark as delivered.";
    }

    public String rejectPayment(int orderId) {
        int userId = -1;
        List<OrderHistory> all = orderDAO.getAllOrders();
        for (OrderHistory o : all) {
            if (o.getId() == orderId) { userId = o.getUserId(); break; }
        }
        boolean success = orderDAO.cancelOrder(orderId, "Payment rejected by admin");
        if (success && userId != -1) {
            notifDAO.addNotification(userId,
                "Payment for order #" + orderId + " was REJECTED. Stock restored.");
            return null;
        }
        return "Failed to reject order.";
    }

    public String getEmptyMessage(boolean isAdmin) {
        return isAdmin ? "No orders in the system yet." : "No orders yet.";
    }

    public String formatPrice(double amount) { return "Rs " + (int) amount; }

    public String formatDate(java.sql.Date date) { return date.toString(); }

    public void populateOrderPanel(javax.swing.JPanel panel,
                                   int userId, boolean isAdmin,
                                   Runnable onRefresh) {
        panel.removeAll();
        List<OrderHistory> orders = getOrderHistory(userId, isAdmin);
        if (orders.isEmpty()) {
            panel.add(buildEmptyLabel(isAdmin));
        } else {
            for (OrderHistory order : orders) {
                panel.add(buildCard(order, isAdmin, onRefresh));
            }
        }
        panel.revalidate();
        panel.repaint();
    }

    private javax.swing.JPanel buildCard(OrderHistory order, boolean isAdmin, Runnable onRefresh) {
        javax.swing.JPanel panel = new javax.swing.JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(900, 90));
        panel.setBackground(new java.awt.Color(170, 218, 172));

        javax.swing.JLabel nameLabel = new javax.swing.JLabel(order.getProductName());
        nameLabel.setBounds(20, 30, 220, 20);
        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));

        javax.swing.JLabel dateLabel = new javax.swing.JLabel(formatDate(order.getOrderDate()));
        dateLabel.setBounds(260, 30, 130, 20);

        javax.swing.JLabel priceLabel = new javax.swing.JLabel(formatPrice(order.getTotalAmount()));
        priceLabel.setBounds(410, 30, 90, 20);

        javax.swing.JLabel statusLabel = new javax.swing.JLabel(order.getStatus());
        statusLabel.setBounds(520, 30, 170, 20);
        statusLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));

        if (order.getStatus().equalsIgnoreCase("Delivered")) statusLabel.setForeground(new java.awt.Color(0, 153, 0));
        else if (order.getStatus().equalsIgnoreCase("Cancelled")) statusLabel.setForeground(java.awt.Color.RED);
        else if (order.getStatus().equalsIgnoreCase("Shipped")) statusLabel.setForeground(new java.awt.Color(0, 102, 204));
        else if (order.getStatus().equalsIgnoreCase("Paid")) statusLabel.setForeground(new java.awt.Color(0, 153, 0));
        else if (order.getStatus().equalsIgnoreCase("Pending Verification")) statusLabel.setForeground(new java.awt.Color(255, 140, 0));

        panel.add(nameLabel); panel.add(dateLabel); panel.add(priceLabel); panel.add(statusLabel);

        if (isAdmin) {
            String buyerName = userDAO.getUsernameById(order.getUserId());
            javax.swing.JLabel userLabel = new javax.swing.JLabel(buyerName);
            userLabel.setBounds(700, 30, 130, 20);
            userLabel.setFont(new java.awt.Font("Segoe UI", 1, 12));
            userLabel.setForeground(new java.awt.Color(0, 0, 153));
            panel.add(userLabel);

            // ─── Pending Verification → Approve/Reject ───────
            if (order.getStatus().equalsIgnoreCase("Pending Verification")) {
                javax.swing.JButton approveBtn = new javax.swing.JButton("Approve");
                approveBtn.setBounds(840, 25, 110, 30);
                approveBtn.setBackground(new java.awt.Color(0, 153, 0));
                approveBtn.setForeground(java.awt.Color.WHITE);
                approveBtn.setFont(new java.awt.Font("Segoe UI", 1, 11));
                approveBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                approveBtn.addActionListener(e -> {
                    String buyerName2 = userDAO.getUsernameById(order.getUserId());
                    String txnId = orderDAO.getTransactionId(order.getId());
                    if (txnId == null || txnId.isEmpty()) txnId = "Not provided";
                    
                    javax.swing.JPanel verifyPanel = new javax.swing.JPanel();
                    verifyPanel.setLayout(new javax.swing.BoxLayout(verifyPanel, javax.swing.BoxLayout.Y_AXIS));
                    verifyPanel.setBackground(new java.awt.Color(245, 255, 245));
                    verifyPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 20, 15, 20));
                    javax.swing.JLabel titleLbl = new javax.swing.JLabel("VERIFY PAYMENT");
                    titleLbl.setFont(new java.awt.Font("Arial Black", 1, 18));
                    titleLbl.setForeground(new java.awt.Color(0, 51, 153));
                    javax.swing.JLabel sep = new javax.swing.JLabel("────────────────────────────");
                    sep.setForeground(java.awt.Color.GRAY);
                    java.awt.Font infoFont = new java.awt.Font("Segoe UI", 0, 14);
                    java.awt.Font boldFont = new java.awt.Font("Segoe UI", 1, 14);
                    javax.swing.JLabel o1 = new javax.swing.JLabel("Order #: " + order.getId()); o1.setFont(boldFont);
                    javax.swing.JLabel o2 = new javax.swing.JLabel("Buyer: " + buyerName2); o2.setFont(infoFont);
                    javax.swing.JLabel o3 = new javax.swing.JLabel("Product: " + order.getProductName()); o3.setFont(infoFont);
                    javax.swing.JLabel o4 = new javax.swing.JLabel("Amount: Rs " + (int) order.getTotalAmount()); o4.setFont(boldFont); o4.setForeground(new java.awt.Color(0, 153, 0));
                    javax.swing.JLabel o5 = new javax.swing.JLabel("Date: " + order.getOrderDate()); o5.setFont(infoFont);
                    javax.swing.JLabel o6 = new javax.swing.JLabel("TXN ID: " + txnId); o6.setFont(boldFont); o6.setForeground(new java.awt.Color(0, 0, 153));
                    javax.swing.JLabel sep2 = new javax.swing.JLabel("────────────────────────────"); sep2.setForeground(java.awt.Color.GRAY);
                    javax.swing.JLabel q = new javax.swing.JLabel("Have you verified this payment?"); q.setFont(boldFont);
                    verifyPanel.add(titleLbl); verifyPanel.add(javax.swing.Box.createVerticalStrut(8)); verifyPanel.add(sep);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(8)); verifyPanel.add(o1);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(4)); verifyPanel.add(o2);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(4)); verifyPanel.add(o3);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(4)); verifyPanel.add(o4);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(4)); verifyPanel.add(o5);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(4)); verifyPanel.add(o6);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(10)); verifyPanel.add(sep2);
                    verifyPanel.add(javax.swing.Box.createVerticalStrut(8)); verifyPanel.add(q);
                    int confirm = javax.swing.JOptionPane.showOptionDialog(panel, verifyPanel, "Verify Payment",
                        javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.PLAIN_MESSAGE,
                        null, new String[]{"Yes, Approve", "Cancel"}, "Yes, Approve");
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        String result = approvePayment(order.getId());
                        if (result == null) {
                            javax.swing.JOptionPane.showMessageDialog(panel, "Order #" + order.getId() + " marked as Paid!\nCustomer has been notified.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            if (onRefresh != null) onRefresh.run();
                        } else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(approveBtn);

                javax.swing.JButton rejectBtn = new javax.swing.JButton("Reject");
                rejectBtn.setBounds(960, 25, 100, 30);
                rejectBtn.setBackground(new java.awt.Color(220, 53, 69));
                rejectBtn.setForeground(java.awt.Color.WHITE);
                rejectBtn.setFont(new java.awt.Font("Segoe UI", 1, 11));
                rejectBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                rejectBtn.addActionListener(e -> {
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(panel, "Reject payment for order #" + order.getId() + "?\nStock will be restored.", "Reject Payment", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        String result = rejectPayment(order.getId());
                        if (result == null) { javax.swing.JOptionPane.showMessageDialog(panel, "Order #" + order.getId() + " rejected.\nCustomer has been notified.", "Rejected", javax.swing.JOptionPane.INFORMATION_MESSAGE); if (onRefresh != null) onRefresh.run(); }
                        else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(rejectBtn);
            }

            // ─── Paid → Ship button ──────────────────────────
            if (order.getStatus().equalsIgnoreCase("Paid")) {
                javax.swing.JButton shipBtn = new javax.swing.JButton("Ship");
                shipBtn.setBounds(840, 25, 100, 30);
                shipBtn.setBackground(new java.awt.Color(0, 102, 204));
                shipBtn.setForeground(java.awt.Color.WHITE);
                shipBtn.setFont(new java.awt.Font("Segoe UI", 1, 11));
                shipBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                shipBtn.addActionListener(e -> {
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(panel, "Mark order #" + order.getId() + " as Shipped?", "Ship Order", javax.swing.JOptionPane.YES_NO_OPTION);
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        String result = markAsShipped(order.getId());
                        if (result == null) { javax.swing.JOptionPane.showMessageDialog(panel, "Order #" + order.getId() + " marked as Shipped!\nCustomer has been notified.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE); if (onRefresh != null) onRefresh.run(); }
                        else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(shipBtn);
            }

            // ─── Shipped → Deliver button ────────────────────
            if (order.getStatus().equalsIgnoreCase("Shipped")) {
                javax.swing.JButton deliverBtn = new javax.swing.JButton("Deliver");
                deliverBtn.setBounds(840, 25, 100, 30);
                deliverBtn.setBackground(new java.awt.Color(0, 153, 0));
                deliverBtn.setForeground(java.awt.Color.WHITE);
                deliverBtn.setFont(new java.awt.Font("Segoe UI", 1, 11));
                deliverBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                deliverBtn.addActionListener(e -> {
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(panel, "Mark order #" + order.getId() + " as Delivered?", "Deliver Order", javax.swing.JOptionPane.YES_NO_OPTION);
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        String result = markAsDelivered(order.getId());
                        if (result == null) { javax.swing.JOptionPane.showMessageDialog(panel, "Order #" + order.getId() + " marked as Delivered!\nCustomer has been notified.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE); if (onRefresh != null) onRefresh.run(); }
                        else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(deliverBtn);
            }

            // ─── Pending (COD) → Ship button ─────────────────
            if (order.getStatus().equalsIgnoreCase("Pending")) {
                javax.swing.JButton shipBtn = new javax.swing.JButton("Ship");
                shipBtn.setBounds(840, 25, 100, 30);
                shipBtn.setBackground(new java.awt.Color(0, 102, 204));
                shipBtn.setForeground(java.awt.Color.WHITE);
                shipBtn.setFont(new java.awt.Font("Segoe UI", 1, 11));
                shipBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                shipBtn.addActionListener(e -> {
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(panel, "Mark order #" + order.getId() + " as Shipped?", "Ship Order", javax.swing.JOptionPane.YES_NO_OPTION);
                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        String result = markAsShipped(order.getId());
                        if (result == null) { javax.swing.JOptionPane.showMessageDialog(panel, "Order #" + order.getId() + " marked as Shipped!\nCustomer has been notified.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE); if (onRefresh != null) onRefresh.run(); }
                        else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(shipBtn);
            }

        } else {
            // USER: Cancel button with reason
            if (order.getStatus().equalsIgnoreCase("Pending") || order.getStatus().equalsIgnoreCase("Pending Verification")) {
                javax.swing.JButton cancelBtn = new javax.swing.JButton("Cancel");
                cancelBtn.setBounds(770, 30, 100, 30);
                cancelBtn.setBackground(new java.awt.Color(220, 53, 69));
                cancelBtn.setForeground(java.awt.Color.WHITE);
                cancelBtn.setFont(new java.awt.Font("Segoe UI", 1, 12));
                cancelBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
                cancelBtn.addActionListener(e -> {
                    javax.swing.JPanel cancelPanel = new javax.swing.JPanel();
                    cancelPanel.setLayout(new javax.swing.BoxLayout(cancelPanel, javax.swing.BoxLayout.Y_AXIS));
                    cancelPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    javax.swing.JLabel reasonLabel = new javax.swing.JLabel("Reason for cancellation:");
                    reasonLabel.setFont(new java.awt.Font("Segoe UI", 1, 14));
                    String[] reasons = {"Changed my mind", "Found a better price", "Ordered by mistake", "Delivery takes too long", "Product no longer needed", "Other"};
                    javax.swing.JComboBox<String> reasonBox = new javax.swing.JComboBox<>(reasons);
                    reasonBox.setFont(new java.awt.Font("Segoe UI", 0, 14));
                    cancelPanel.add(reasonLabel);
                    cancelPanel.add(javax.swing.Box.createVerticalStrut(10));
                    cancelPanel.add(reasonBox);
                    int confirm = javax.swing.JOptionPane.showOptionDialog(panel, cancelPanel, "Cancel Order #" + order.getId(),
                        javax.swing.JOptionPane.OK_CANCEL_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE,
                        null, new String[]{"Yes, Cancel", "Go Back"}, "Go Back");
                    if (confirm == javax.swing.JOptionPane.OK_OPTION) {
                        String selectedReason = (String) reasonBox.getSelectedItem();
                        String result = cancelOrder(order.getId(), selectedReason);
                        if (result == null) { javax.swing.JOptionPane.showMessageDialog(panel, "Order cancelled successfully!\nReason: " + selectedReason + "\nStock has been restored.", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE); if (onRefresh != null) onRefresh.run(); }
                        else { javax.swing.JOptionPane.showMessageDialog(panel, result, "Error", javax.swing.JOptionPane.ERROR_MESSAGE); }
                    }
                });
                panel.add(cancelBtn);
            }
        }
        return panel;
    }

    private javax.swing.JLabel buildEmptyLabel(boolean isAdmin) {
        javax.swing.JLabel none = new javax.swing.JLabel(getEmptyMessage(isAdmin));
        none.setFont(new java.awt.Font("Segoe UI", 1, 16));
        none.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        return none;
    }
}