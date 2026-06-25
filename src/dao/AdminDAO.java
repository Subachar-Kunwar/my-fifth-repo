package dao;

import Database.MySqlConnector;
import java.sql.*;

public class AdminDAO {

    MySqlConnector mysql = new MySqlConnector();

    // ─── Total Sales (actual revenue: Paid + Shipped + Delivered) ──
    public double getTotalSales() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COALESCE(SUM(total_amount), 0) AS total FROM orders " +
                     "WHERE status IN ('Paid', 'Shipped', 'Delivered')";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.out.println("Get total sales error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // ─── Total Orders Count ───────────────────────────────────
    public int getTotalOrders() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) AS total FROM orders";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("Get total orders error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // ─── Total Users Count ────────────────────────────────────
    public int getTotalUsers() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) AS total FROM users";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("Get total users error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // ─── Total Products Count ─────────────────────────────────
    public int getTotalProducts() {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COUNT(*) AS total FROM products";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("Get total products error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }

    // ─── Get 3 Most Recent Orders (latest first) ──────────────
    public java.util.List<String[]> getRecentOrders() {
        Connection conn = mysql.openConnection();
        java.util.List<String[]> orders = new java.util.ArrayList<>();
        String sql = "SELECT p.name, o.order_date, o.total_amount, o.status " +
                     "FROM orders o JOIN products p ON o.product_id = p.id " +
                     "ORDER BY o.id DESC LIMIT 3";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(new String[]{
                    rs.getString("name"),
                    rs.getString("order_date"),
                    "Rs " + (int) rs.getDouble("total_amount"),
                    rs.getString("status")
                });
            }
        } catch (SQLException e) {
            System.out.println("Get recent orders error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }
}