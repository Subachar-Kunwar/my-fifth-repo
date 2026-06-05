package dao;

import Database.MySqlConnector;
import java.sql.*;

public class AdminDAO {

    MySqlConnector mysql = new MySqlConnector();

    // Total sales amount
    public double getTotalSales() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT SUM(total_amount) FROM orders";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // Total orders count
    public int getTotalOrders() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) FROM orders";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // Total users count
    public int getTotalUsers() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) FROM users";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // Total products count
    public int getTotalProducts() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) FROM products";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // Get 3 most recent orders
    public java.util.List<String[]> getRecentOrders() {
        Connection conn = mysql.openConnection();
        java.util.List<String[]> orders = new java.util.ArrayList<>();
        String sql = "SELECT p.name, o.order_date, o.total_amount, o.status " +
                     "FROM orders o JOIN products p ON o.product_id = p.id " +
                     "ORDER BY o.order_date DESC LIMIT 3";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
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
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }
}