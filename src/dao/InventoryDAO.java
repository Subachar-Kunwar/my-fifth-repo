package dao;

import Database.MySqlConnector;
import model.Inventory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    public List<Inventory> getAllInventory() {
        List<Inventory> items = new ArrayList<>();
        Connection conn = mysql.openConnection();
        if (conn == null) return items;

        String query = "SELECT id, name, stock, status FROM products ORDER BY id";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                boolean isHidden = (rs.getInt("status") == 0);
                items.add(new Inventory(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    isHidden
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return items;
    }

    public void updateGhostStatus(int id, boolean isHidden) {
        Connection conn = mysql.openConnection();
        if (conn == null) return;

        int statusValue = isHidden ? 0 : 1;
        String query = "UPDATE products SET status = ? WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, statusValue);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating ghost status: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
    }
}