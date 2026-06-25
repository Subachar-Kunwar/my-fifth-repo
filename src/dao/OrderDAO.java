package dao;

import Database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.OrderHistory;

public class OrderDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Create Orders from Cart ──────────────────────────────
    public boolean createOrdersFromCart(int userId, List<CartItem> cartItems,
                                        String fullName, String address, String city,
                                        String phoneNumber, String postalCode, String paymentMethod,
                                        String transactionUuid) {
        Connection conn = mysql.openConnection();
        if (conn == null) return false;

        boolean success = false;

        try {
            conn.setAutoCommit(false);

            String insertOrderSql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                    "full_name, address, city, phone_number, postal_code, payment_method, transaction_uuid) " +
                    "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql)) {
                for (CartItem item : cartItems) {
                    psOrder.setInt(1, item.getProductId());
                    psOrder.setInt(2, userId);
                    psOrder.setDouble(3, item.getTotal());
                    psOrder.setString(4, fullName);
                    psOrder.setString(5, address);
                    psOrder.setString(6, city);
                    psOrder.setString(7, phoneNumber);
                    psOrder.setString(8, postalCode);
                    psOrder.setString(9, paymentMethod);
                    psOrder.setString(10, transactionUuid);
                    psOrder.addBatch();
                }
                psOrder.executeBatch();
            }

            conn.commit();
            success = true;
            new ActivityDAO().logActivity(userId, "Placed an order for " + cartItems.size() + " item(s)");
            new NotificationDAO().addNotification(userId, 
                "Your order has been placed successfully! " + cartItems.size() + " item(s)");

        } catch (SQLException e) {
            System.out.println("Cart checkout database error: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback error: " + ex.getMessage()); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { System.out.println("Auto-commit reset error: " + ex.getMessage()); }
            mysql.closeConnection(conn);
        }

        return success;
    }

    // ─── Place Order (backward compatibility) ─────────────────
    public int placeOrder(int productId, int userId, double totalAmount) {
        return placeOrder(productId, userId, totalAmount, null, null, null, null, null, null, null);
    }

    public int placeOrder(int productId, int userId, double totalAmount,
                          String fullName, String address, String city,
                          String phoneNumber, String postalCode, String paymentMethod) {
        return placeOrder(productId, userId, totalAmount, fullName, address, city, phoneNumber, postalCode, paymentMethod, null);
    }

    // ─── Place Order with Transaction UUID ────────────────────
    public int placeOrder(int productId, int userId, double totalAmount,
                          String fullName, String address, String city,
                          String phoneNumber, String postalCode, String paymentMethod,
                          String transactionUuid) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                "full_name, address, city, phone_number, postal_code, payment_method, transaction_uuid) " +
                "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setDouble(3, totalAmount);
            ps.setString(4, fullName);
            ps.setString(5, address);
            ps.setString(6, city);
            ps.setString(7, phoneNumber);
            ps.setString(8, postalCode);
            ps.setString(9, paymentMethod);
            ps.setString(10, transactionUuid);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    new ActivityDAO().logActivity(userId, "Order Placed" + (transactionUuid != null ? " via online payment" : ""));
                    String msg = transactionUuid != null 
                        ? "Order #" + orderId + " placed via online payment. Awaiting verification."
                        : "Order #" + orderId + " placed successfully!";
                    new NotificationDAO().addNotification(userId, msg);
                    return orderId;
                }
            }
        } catch (SQLException e) {
            System.out.println("Order error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    // ─── Save Transaction ID ──────────────────────────────────
    public boolean saveTransactionId(int orderId, String transactionId) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE orders SET transaction_id = ?, status = 'Pending Verification' WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Save transaction ID error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Get Transaction ID ───────────────────────────────────
    public String getTransactionId(int orderId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT transaction_id FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("transaction_id");
        } catch (SQLException e) {
            System.out.println("Get TXN ID error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Get Order By ID ──────────────────────────────────────
    public String[] getOrderById(int orderId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT o.id, p.name, o.order_date, o.total_amount, o.status " +
                "FROM orders o JOIN products p ON o.product_id = p.id WHERE o.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        String.valueOf(rs.getInt("id")), rs.getString("name"),
                        rs.getString("order_date"), "Rs " + (int) rs.getDouble("total_amount"),
                        rs.getString("status")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Get Recent Orders By User ────────────────────────────
    public List<String[]> getRecentOrdersByUser(int userId) {
        Connection conn = mysql.openConnection();
        List<String[]> orders = new ArrayList<>();
        String sql = "SELECT p.name, o.order_date, o.total_amount, o.status " +
                "FROM orders o JOIN products p ON o.product_id = p.id " +
                "WHERE o.user_id = ? ORDER BY o.id DESC LIMIT 4";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new String[]{
                        rs.getString("name"), rs.getString("order_date"),
                        "Rs " + (int) rs.getDouble("total_amount"), rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recent orders: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    // ─── Get All Orders By User ───────────────────────────────
    public List<OrderHistory> getOrdersByUserId(int userId) {
        Connection conn = mysql.openConnection();
        List<OrderHistory> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, o.product_id, p.name, p.image_path, " +
                "o.total_amount, o.status, o.order_date " +
                "FROM orders o JOIN products p ON o.product_id = p.id " +
                "WHERE o.user_id = ? ORDER BY o.id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new OrderHistory(
                        rs.getInt("id"), rs.getInt("user_id"), rs.getInt("product_id"),
                        rs.getString("name"), rs.getString("image_path"),
                        rs.getDouble("total_amount"), rs.getString("status"), rs.getDate("order_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get order history error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    // ─── Get ALL Orders (Admin) ───────────────────────────────
    public List<OrderHistory> getAllOrders() {
        Connection conn = mysql.openConnection();
        List<OrderHistory> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, o.product_id, p.name, p.image_path, " +
                "o.total_amount, o.status, o.order_date " +
                "FROM orders o JOIN products p ON o.product_id = p.id " +
                "ORDER BY o.id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(new OrderHistory(
                    rs.getInt("id"), rs.getInt("user_id"), rs.getInt("product_id"),
                    rs.getString("name"), rs.getString("image_path"),
                    rs.getDouble("total_amount"), rs.getString("status"), rs.getDate("order_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get all orders error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    // ─── Update Payment Status ────────────────────────────────
    public void updatePaymentStatus(String uuid, String status) {
        Connection conn = mysql.openConnection();
        if (conn == null) return;
        int orderUserId = -1; int orderId = -1;
        String getSql = "SELECT id, user_id FROM orders WHERE transaction_uuid = ?";
        try (PreparedStatement ps = conn.prepareStatement(getSql)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) { orderId = rs.getInt("id"); orderUserId = rs.getInt("user_id"); }
        } catch (SQLException e) { System.out.println("Get order error: " + e.getMessage()); }

        String query = "UPDATE orders SET status = ? WHERE transaction_uuid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status); pstmt.setString(2, uuid); pstmt.executeUpdate();
            if (orderUserId != -1) {
                new NotificationDAO().addNotification(orderUserId,
                    "Payment for order #" + orderId + " has been " + status + "!");
            }
        } catch (SQLException e) {
            System.err.println("Error updating order payment status: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Approve Order ────────────────────────────────────────
    public boolean approveOrder(int orderId) {
        Connection conn = mysql.openConnection();
        int orderUserId = -1;
        try (PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM orders WHERE id = ?")) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) orderUserId = rs.getInt("user_id");
        } catch (SQLException e) { System.out.println("Get user error: " + e.getMessage()); }
        
        try (PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = 'Paid' WHERE id = ?")) {
            ps.setInt(1, orderId);
            boolean updated = ps.executeUpdate() > 0;
            if (updated && orderUserId != -1) {
                new NotificationDAO().addNotification(orderUserId,
                    "Payment verified! Order #" + orderId + " is now marked as Paid.");
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("Approve order error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Mark as Shipped + Notify ─────────────────────────────
    public boolean markAsShipped(int orderId) {
        Connection conn = mysql.openConnection();
        int orderUserId = -1;
        try (PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM orders WHERE id = ?")) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) orderUserId = rs.getInt("user_id");
        } catch (SQLException e) { System.out.println("Get user error: " + e.getMessage()); }
        
        try (PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = 'Shipped' WHERE id = ?")) {
            ps.setInt(1, orderId);
            boolean updated = ps.executeUpdate() > 0;
            if (updated && orderUserId != -1) {
                new NotificationDAO().addNotification(orderUserId,
                    "Your order #" + orderId + " has been shipped!");
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("Ship order error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Mark as Delivered + Notify ───────────────────────────
    public boolean markAsDelivered(int orderId) {
        Connection conn = mysql.openConnection();
        int orderUserId = -1;
        try (PreparedStatement ps = conn.prepareStatement("SELECT user_id FROM orders WHERE id = ?")) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) orderUserId = rs.getInt("user_id");
        } catch (SQLException e) { System.out.println("Get user error: " + e.getMessage()); }
        
        try (PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = 'Delivered' WHERE id = ?")) {
            ps.setInt(1, orderId);
            boolean updated = ps.executeUpdate() > 0;
            if (updated && orderUserId != -1) {
                new NotificationDAO().addNotification(orderUserId,
                    "Your order #" + orderId + " has been delivered! Thank you for shopping with ReWear.");
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("Deliver order error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    // ─── Cancel Order with Reason ─────────────────────────────
    public boolean cancelOrder(int orderId, String cancelReason) {
        Connection conn = mysql.openConnection();
        if (conn == null) return false;
        boolean success = false;
        try {
            conn.setAutoCommit(false);
            int productId = -1; int userId = -1; String currentStatus = "";
            String getSql = "SELECT product_id, user_id, status FROM orders WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(getSql)) {
                ps.setInt(1, orderId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    productId = rs.getInt("product_id"); userId = rs.getInt("user_id");
                    currentStatus = rs.getString("status");
                }
            }
            if (!currentStatus.equalsIgnoreCase("Pending") && !currentStatus.equalsIgnoreCase("Pending Verification")) {
                return false;
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE products SET stock = stock + 1 WHERE id = ?")) {
                ps.setInt(1, productId); ps.executeUpdate();
            }
            try (PreparedStatement ps = conn.prepareStatement("UPDATE orders SET status = 'Cancelled', cancel_reason = ? WHERE id = ?")) {
                ps.setString(1, cancelReason); ps.setInt(2, orderId); ps.executeUpdate();
            }
            conn.commit();
            success = true;
            if (userId != -1) {
                new ActivityDAO().logActivity(userId, "Cancelled order #" + orderId);
                new NotificationDAO().addNotification(userId,
                    "Order #" + orderId + " has been cancelled. Reason: " + cancelReason);
            }
        } catch (SQLException e) {
            System.out.println("Cancel order error: " + e.getMessage());
            try { conn.rollback(); } catch (SQLException ex) { System.out.println("Rollback error: " + ex.getMessage()); }
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ex) { System.out.println("Auto-commit reset error: " + ex.getMessage()); }
            mysql.closeConnection(conn);
        }
        return success;
    }

    // ─── Cancel Order (backward compatibility) ────────────────
    public boolean cancelOrder(int orderId) {
        return cancelOrder(orderId, "No reason provided");
    }
}