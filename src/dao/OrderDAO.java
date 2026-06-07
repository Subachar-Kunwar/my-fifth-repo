package dao;

import Database.MySqlConnector;
import java.sql.*;

public class OrderDAO {

    MySqlConnector mysql = new MySqlConnector();

    // Place order and return generated order ID
    public int placeOrder(int productId, int userId,
            double totalAmount) {
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

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // returns the order ID
            }
        } catch (SQLException e) {
            System.out.println("Order error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    // Get order by ID
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
}