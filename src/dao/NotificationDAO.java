package dao;

import Database.MySqlConnector;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Notification;

public class NotificationDAO {

    public List<Notification> getByUser(int userId) {

        List<Notification> list = new ArrayList<>();

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = "SELECT * FROM notifications WHERE user_id=? ORDER BY created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Notification(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("message"),
                        rs.getTimestamp("created_at"),
                        rs.getBoolean("is_read")
                ));
            }

            rs.close();
            connector.closeConnection(conn);

        } catch (Exception e) {
            System.out.println("Notification fetch error: " + e.getMessage());
        }

        return list;
    }

    public void markAllAsRead(int userId) {
        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();

            connector.closeConnection(conn);

        } catch (Exception e) {
            System.out.println("Mark read error: " + e.getMessage());
        }
    }
    public void deleteByIds(List<Integer> ids) {

    if (ids.isEmpty()) return;

    try {
        MySqlConnector connector = new MySqlConnector();
        Connection conn = connector.openConnection();

        String sql = "DELETE FROM notifications WHERE id=?";
        PreparedStatement ps = conn.prepareStatement(sql);

        for (Integer id : ids) {
            ps.setInt(1, id);
            ps.addBatch();
        }

        ps.executeBatch();
        connector.closeConnection(conn);

    } catch (Exception e) {
        System.out.println("Delete error: " + e.getMessage());
    }
}
}