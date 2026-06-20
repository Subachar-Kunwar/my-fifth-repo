package dao;

import Database.MySqlConnector;
import java.sql.*;
import java.util.List;
import model.CartItem;

public class OrderDAO {
    MySqlConnector mysql = new MySqlConnector();

    // ==========================================
    // UPDATED: Handles Checkout AND Inventory
    // ==========================================
    public boolean createOrdersFromCart(int userId, List<CartItem> cartItems,
                                        String fullName, String address, String city,
                                        String phoneNumber, String postalCode, String paymentMethod) {
        Connection conn = mysql.openConnection();
        boolean success = false;
        
        try {
            // Disable auto-commit to save all items as a single transaction
            conn.setAutoCommit(false); 

            // Query 1: Insert into orders
            String insertOrderSql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                         "full_name, address, city, phone_number, postal_code, payment_method) " +
                         "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?)";
                         
            // Query 2: Decrease product stock
            String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {
                
                for (CartItem item : cartItems) {
                    // Prepare Order Insert
                    psOrder.setInt(1, item.getProductId());
                    psOrder.setInt(2, userId);
                    psOrder.setDouble(3, item.getTotal()); 
                    psOrder.setString(4, fullName);
                    psOrder.setString(5, address);
                    psOrder.setString(6, city);
                    psOrder.setString(7, phoneNumber);
                    psOrder.setString(8, postalCode);
                    psOrder.setString(9, paymentMethod);
                    psOrder.addBatch(); 
                    
                    // Prepare Stock Deduction
                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getProductId());
                    psStock.setInt(3, item.getQuantity()); // Ensure we don't go into negative stock
                    psStock.addBatch();
                }
                
                // Execute both batches
                psOrder.executeBatch(); 
                int[] stockResults = psStock.executeBatch();
                
                // Verify all stock updates worked (no negative stock allowed)
                for (int result : stockResults) {
                    if (result == 0) {
                        throw new SQLException("Insufficient stock for one or more items.");
                    }
                }
            }

            // Commit the transaction FIRST
            conn.commit(); 
            success = true;

            // Log the activity ONLY AFTER commit is successful
            new ActivityDAO().logActivity(userId, "Placed an order for " + cartItems.size() + " item(s)");

        } catch (SQLException e) {
            System.out.println("Cart Checkout Database Error: " + e.getMessage());
            try {
                if (conn != null) conn.rollback(); // Undo if something fails
            } catch (SQLException ex) {
                System.out.println("Rollback error: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Reset connection state
            } catch (SQLException ex) {}
            mysql.closeConnection(conn);
        }
        
        return success;
    }

    // ==========================================
    // EXISTING METHODS (Kept intact)
    // ==========================================
    public int placeOrder(int productId, int userId, double totalAmount) {
        return placeOrder(productId, userId, totalAmount, null, null, null, null, null, null);
    }

    public int placeOrder(int productId, int userId, double totalAmount,
                          String fullName, String address, String city,
                          String phoneNumber, String postalCode, String paymentMethod) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                     "full_name, address, city, phone_number, postal_code, payment_method) " +
                     "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setDouble(3, totalAmount);
            ps.setString(4, fullName);
            ps.setString(5, address);
            ps.setString(6, city);
            ps.setString(7, phoneNumber);
            ps.setString(8, postalCode);
            ps.setString(9, paymentMethod);
            
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int orderId = keys.getInt(1);
                new ActivityDAO().logActivity(userId, "Order Placed");
                return orderId;
            }
        } catch (SQLException e) {
            System.out.println("Order error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    public String[] getOrderById(int orderId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT o.id, p.name, o.order_date, " +
                     "o.total_amount, o.status " +
                     "FROM orders o " +
                     "JOIN products p ON o.product_id = p.id " +
                     "WHERE o.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                    String.valueOf(rs.getInt("id")),
                    rs.getString("name"),
                    rs.getString("order_date"),
                    "Rs " + (int) rs.getDouble("total_amount"),
                    rs.getString("status")
                };
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public java.util.List<String[]> getRecentOrdersByUser(int userId) {
        Connection conn = mysql.openConnection();
        java.util.List<String[]> orders = new java.util.ArrayList<>();
        String sql = "SELECT p.name, o.order_date, o.total_amount, o.status " +
                     "FROM orders o " +
                     "JOIN products p ON o.product_id = p.id " +
                     "WHERE o.user_id = ? " +
                     "ORDER BY o.order_date DESC LIMIT 4";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(new String[]{
                    rs.getString("name"),
                    rs.getString("order_date"),
                    "Rs " + (int) rs.getDouble("total_amount"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recent orders: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }
}