package dao;
import Database.MySqlConnector;
import java.sql.*;

public class ActivityDAO {
    MySqlConnector mysql = new MySqlConnector();

    // Log a new activity for a user
    public boolean logActivity(int userId, String activity) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO user_activities (user_id, activity) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, activity);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error logging activity: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Get recent activities for a user
    public java.util.List<String> getRecentActivitiesByUser(int userId) {
        Connection conn = mysql.openConnection();
        java.util.List<String> activities = new java.util.ArrayList<>();
        String sql = "SELECT activity FROM user_activities " +
                     "WHERE user_id = ? " +
                     "ORDER BY created_at DESC LIMIT 3";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                activities.add(rs.getString("activity"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching activities: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return activities;
    }
}