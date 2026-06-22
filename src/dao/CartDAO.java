package dao;

import Database.MySqlConnector;
import model.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    MySqlConnector mysql = new MySqlConnector();

    // Get or create cart for a user
    public int getOrCreateCart(int userId) {
        Connection conn = mysql.openConnection();
        try {
            // Check if cart exists
            String checkSql = "SELECT id FROM cart WHERE user_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, userId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
            // Create new cart
            String insertSql = "INSERT INTO cart (user_id) VALUES (?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql,
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, userId);
                ps.executeUpdate();
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Cart error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    // Add product to cart (or increase quantity if already exists)
    public boolean addToCart(int userId, int productId) {
        Connection conn = mysql.openConnection();
        try {
            int cartId = getOrCreateCartWithConn(conn, userId);
            if (cartId == -1) return false;

            // Check if product already in cart
            String checkSql = "SELECT id, quantity FROM cart_items " +
                              "WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    // Increase quantity by 1
                    int newQty = rs.getInt("quantity") + 1;
                    String updateSql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement ups = conn.prepareStatement(updateSql)) {
                        ups.setInt(1, newQty);
                        ups.setInt(2, rs.getInt("id"));
                        return ups.executeUpdate() > 0;
                    }
                }
            }

            // Insert new item with quantity 1
            String insertSql = "INSERT INTO cart_items (cart_id, product_id, quantity) " +
                               "VALUES (?, ?, 1)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, cartId);
                ps.setInt(2, productId);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Add to cart error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Helper: get or create cart using an existing connection (avoids double open)
    private int getOrCreateCartWithConn(Connection conn, int userId) throws SQLException {
        String checkSql = "SELECT id FROM cart WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        }
        String insertSql = "INSERT INTO cart (user_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSql,
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1);
            }
        }
        return -1;
    }

    // Get all cart items for a user (JOIN with products for name/price/image)
    public List<CartItem> getCartItems(int userId) {
        List<CartItem> items = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "SELECT ci.id, ci.cart_id, ci.product_id, " +
                     "p.name, p.price, ci.quantity, p.image_path " +
                     "FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "JOIN products p ON ci.product_id = p.id " +
                     "WHERE c.user_id = ? " +
                     "ORDER BY ci.added_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                items.add(new CartItem(
                    rs.getInt("id"),
                    rs.getInt("cart_id"),
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getDouble("price"),
                    rs.getInt("quantity"),
                    rs.getString("image_path")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get cart error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return items;
    }

    // Update item quantity
    public boolean updateQuantity(int cartItemId, int newQuantity) {
        if (newQuantity <= 0) return removeItem(cartItemId);

        Connection conn = mysql.openConnection();
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Update quantity error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Remove single item from cart
    public boolean removeItem(int cartItemId) {
        Connection conn = mysql.openConnection();
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Remove item error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Clear entire cart after checkout
    public boolean clearCart(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "DELETE ci FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "WHERE c.user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() >= 0;
        } catch (SQLException e) {
            System.out.println("Clear cart error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Get total item count in cart (for badge display)
    public int getCartCount(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT COALESCE(SUM(ci.quantity), 0) AS total " +
                     "FROM cart_items ci " +
                     "JOIN cart c ON ci.cart_id = c.id " +
                     "WHERE c.user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.out.println("Cart count error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return 0;
    }
}
