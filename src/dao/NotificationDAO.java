package dao;

import Database.MySqlConnector;
import model.Notification;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Get Notifications By User ────────────────────────────
    public List<Notification> getByUser(int userId) {
        Connection conn = mysql.openConnection();
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications " +
                     "WHERE user_id = ? " +
                     "ORDER BY created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {       // ✅ rs closed
                while (rs.next()) {
                    list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_read")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Notification fetch error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);                   // ✅ conn closed
        }
        return list;
    }

    // ─── Mark All As Read ─────────────────────────────────────
    public void markAllAsRead(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE notifications SET is_read = TRUE " +
                     "WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) { // ✅ ps closed
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Mark read error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);                   // ✅ conn closed
        }
    }

    // ─── Delete By IDs ────────────────────────────────────────
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return;

        Connection conn = mysql.openConnection();
        String sql = "DELETE FROM notifications WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) { // ✅ ps closed
            for (Integer id : ids) {
                ps.setInt(1, id);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);                   // ✅ conn closed
        }
    }
    
    // ─── Add Notification ─────────────────────────────────────
public boolean addNotification(int userId, String message) {
    Connection conn = mysql.openConnection();
    String sql = "INSERT INTO notifications (user_id, message, is_read, created_at) " +
                 "VALUES (?, ?, FALSE, NOW())";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, userId);
        ps.setString(2, message);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.out.println("Add notification error: " + e.getMessage());
        return false;
    } finally {
        mysql.closeConnection(conn);
    }
}
    
}