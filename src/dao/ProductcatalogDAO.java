package dao;

import Database.MySqlConnector;
import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductcatalogDAO {

    MySqlConnector mysql = new MySqlConnector();

    // ─── Map ResultSet to Product ─────────────────────────────
    private Product mapProduct(ResultSet rs) throws SQLException {
        String description = "";
        int stock = 0;
        try { description = rs.getString("description"); }
        catch (SQLException e) {}
        try { stock = rs.getInt("stock"); }
        catch (SQLException e) {}

        return new Product(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getDouble("price"),
            rs.getString("image_path"),
            rs.getInt("seller_id"),
            description,
            stock
        );
    }

    // ─── Add Product ──────────────────────────────────────────
    public boolean addProduct(String name, String category, double price,
            String imagePath, String description, int stock, int sellerId) {
        Connection conn = mysql.openConnection();
        try {
            int nextId = findNextAvailableId(conn);
            String sql = "INSERT INTO products (id, name, category, price, " +
                         "image_path, description, stock, seller_id, status) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 1)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, nextId);
                ps.setString(2, name);
                ps.setString(3, category);
                ps.setDouble(4, price);
                ps.setString(5, imagePath);
                ps.setString(6, description);
                ps.setInt(7, stock);
                ps.setInt(8, sellerId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error adding product: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Find Next Available ID ───────────────────────────────
    private int findNextAvailableId(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) AS cnt FROM products";
        try (PreparedStatement ps = conn.prepareStatement(checkSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next() && rs.getInt("cnt") == 0) return 1;
        }

        String checkOneSql = "SELECT id FROM products WHERE id = 1";
        try (PreparedStatement ps = conn.prepareStatement(checkOneSql);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return 1;
        }

        String sql = "SELECT MIN(t1.id + 1) AS next_id " +
                     "FROM products t1 " +
                     "LEFT JOIN products t2 ON t1.id + 1 = t2.id " +
                     "WHERE t2.id IS NULL";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int nextId = rs.getInt("next_id");
                if (!rs.wasNull() && nextId > 0) return nextId;
            }
        }
        return 1;
    }

    // ─── Update Product ───────────────────────────────────────
    public boolean updateProduct(int id, String name, String category,
            double price, String imagePath, String description, int stock) {
        Connection conn = mysql.openConnection();

        if (imagePath == null || imagePath.isEmpty()) {
            String selectSql = "SELECT image_path FROM products WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
                ps.setInt(1, id);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) imagePath = rs.getString("image_path");
                }
            } catch (SQLException e) {
                System.out.println("Error fetching existing image: " + e.getMessage());
            }
        }

        String sql = "UPDATE products SET name=?, category=?, price=?, " +
                     "image_path=?, description=?, stock=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setString(4, imagePath);
            ps.setString(5, description);
            ps.setInt(6, stock);
            ps.setInt(7, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Delete Product ───────────────────────────────────────
    public boolean deleteProduct(int id) {
        Connection conn = mysql.openConnection();
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean deleted = ps.executeUpdate() > 0;

            if (deleted) {
                try (PreparedStatement getMax = conn.prepareStatement(
                        "SELECT IFNULL(MAX(id), 0) + 1 AS next_id FROM products");
                     ResultSet rs = getMax.executeQuery()) {
                    if (rs.next()) {
                        int nextId = rs.getInt("next_id");
                        try (PreparedStatement alter = conn.prepareStatement(
                                "ALTER TABLE products AUTO_INCREMENT = " + nextId)) {
                            alter.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Auto-increment reset failed: " + e.getMessage());
                }
            }
            return deleted;
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Get All Products (visible only) ──────────────────────
    public List<Product> getAllProducts() {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE status = 1 ORDER BY id");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapProduct(rs));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Get By Category (visible only) ───────────────────────
    public List<Product> getByCategory(String category) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE category = ? AND status = 1")) {
            ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Get By Price Range (visible only) ────────────────────
    public List<Product> getByPriceRange(double min, double max) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE price BETWEEN ? AND ? AND status = 1")) {
            ps.setDouble(1, min);
            ps.setDouble(2, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Get By Category And Price (visible only) ─────────────
    public List<Product> getByCategoryAndPrice(
            String category, double min, double max) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE category = ? " +
                "AND price BETWEEN ? AND ? AND status = 1")) {
            ps.setString(1, category);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Search By Name (visible only) ────────────────────────
    public List<Product> searchByName(String keyword) {
        Connection conn = mysql.openConnection();
        List<Product> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE name LIKE ? AND status = 1")) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Get Product By ID (any status — for admin/edit) ──────
    public Product getProductById(int id) {
        Connection conn = mysql.openConnection();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM products WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapProduct(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
}