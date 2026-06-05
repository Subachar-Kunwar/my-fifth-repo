package dao;

import Database.MySqlConnector;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductcatalogDAO {

    MySqlConnector mysql = new MySqlConnector();

    private Product mapProduct(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getDouble("price"),
            rs.getString("image_path"),
            rs.getInt("seller_id")
        );
    }

    /**
     * Inserts a new product into the database matching the UI form schema.
     * * @param name Name of the item
     * @param category Classification group
     * @param price Cost metric
     * @param imagePath Storage reference location for the visual asset
     * @param description Brief text details about the item
     * @param stock Available quantity inventory units
     * @param sellerId The unique ID key matching the seller user profile
     * @return true if row insertion evaluates successfully, false otherwise
     */
    public boolean addProduct(String name, String category, double price, String imagePath, String description, int stock, int sellerId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO products (name, category, price, image_path, description, stock, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setString(4, imagePath);
            ps.setString(5, description);
            ps.setInt(6, stock);
            ps.setInt(7, sellerId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public List<Product> getAllProducts() {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    public List<Product> getByCategory(String category) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE category = ?")) {
            ps.setString(1, category);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    public List<Product> getByPriceRange(double min, double max) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE price BETWEEN ? AND ?")) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    public List<Product> getByCategoryAndPrice(
            String category, double min, double max) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE category = ? " +
                "AND price BETWEEN ? AND ?")) {
            ps.setString(1, category);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    public List<Product> searchByName(String keyword) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE name LIKE ?")) {
            ps.setString(1, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }
}