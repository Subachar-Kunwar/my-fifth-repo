package dao;

import Database.MySqlConnector;
import java.sql.*;

public class OrderDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Place Order ──────────────────────────────────────────
    public int placeOrder(int productId, int userId, double totalAmount) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO orders (product_id, user_id, " +
                     "order_date, total_amount, status) " +
                     "VALUES (?, ?, CURDATE(), ?, 'Pending')";
        try (PreparedStatement ps = conn.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setDouble(3, totalAmount);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    new ActivityDAO().logActivity(userId, "Order Placed");
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

    // ─── Get Order By ID ──────────────────────────────────────
    public String[] getOrderById(int orderId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT o.id, p.name, o.order_date, " +
                     "o.total_amount, o.status " +
                     "FROM orders o " +
                     "JOIN products p ON o.product_id = p.id " +
                     "WHERE o.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("order_date"),
                        "Rs " + (int) rs.getDouble("total_amount"),
                        rs.getString("status")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Get Recent Orders By User ────────────────────────────
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
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new String[]{
                        rs.getString("name"),
                        rs.getString("order_date"),
                        "Rs " + (int) rs.getDouble("total_amount"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    // ─── Get Full Order History By User ───────────────────────
    public java.util.List<model.OrderHistory> getOrdersByUserId(int userId) {
        Connection conn = mysql.openConnection();
        java.util.List<model.OrderHistory> orders = new java.util.ArrayList<>();

        String sql = "SELECT o.id, o.user_id, o.product_id, " +
                     "p.name, p.image_path, " +
                     "o.total_amount, o.status, o.order_date " +
                     "FROM orders o " +
                     "JOIN products p ON o.product_id = p.id " +
                     "WHERE o.user_id = ? " +
                     "ORDER BY o.order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new model.OrderHistory(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getDate("order_date")
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
    public java.util.List<model.OrderHistory> getAllOrders() {
        Connection conn = mysql.openConnection();
        java.util.List<model.OrderHistory> orders = new java.util.ArrayList<>();

        String sql = "SELECT o.id, o.user_id, o.product_id, " +
                     "p.name, p.image_path, " +
                     "o.total_amount, o.status, o.order_date " +
                     "FROM orders o " +
                     "JOIN products p ON o.product_id = p.id " +
                     "ORDER BY o.order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new model.OrderHistory(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getDate("order_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get all orders error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }
}