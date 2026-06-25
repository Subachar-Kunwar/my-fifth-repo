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

    // Pulling the shared database connection manager from your project
    private final MySqlConnector mysql = new MySqlConnector();

    /**
     * Fetches the inventory records to map directly to your model.Inventory class
     */
    public List<Inventory> getAllInventory() {
        List<Inventory> items = new ArrayList<>();
        Connection conn = mysql.openConnection();
        if (conn == null) return items;

        // Queries the 'products' table matching your AdminDAO configuration
        String query = "SELECT id, name, stock, status FROM products LIMIT 5";
        
        try (PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                // If status is 0, the item is hidden/ghosted (sets your boolean to true)
                boolean isHidden = (rs.getInt("status") == 0); 
                
                // Matches your Inventory constructor: (id, name, stock, hidden)
                items.add(new Inventory(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("stock"),
                    isHidden
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching data into Inventory model: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return items;
    }

    /**
     * Updates the product's active status based on the checkbox selection
     */
    public void updateGhostStatus(int id, boolean isHidden) {
        Connection conn = mysql.openConnection();
        if (conn == null) return;

        // Map boolean back to database values: hidden (true) -> 0, visible (false) -> 1
        int statusValue = isHidden ? 0 : 1;
        String query = "UPDATE products SET status = ? WHERE id = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, statusValue);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving status change from Inventory model: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
    }
}