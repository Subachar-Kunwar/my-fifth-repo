package dao;

import Database.MySqlConnector;
import model.Product;
import model.ProductReview;
import java.sql.*;

public class ProductDetailDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Get Product By ID ────────────────────────────────────
    // Added this - ProductDetailController needs it
    public Product getProductById(int productId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {       // ✅ rs closed
                if (rs.next()) {
                    return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getString("image_path"),
                        rs.getInt("seller_id"),
                        rs.getString("description"),
                        rs.getInt("stock")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Insert Review ────────────────────────────────────────
    public boolean insertReview(ProductReview review) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO product_reviews " +
                     "(product_id, user_id, rating, review_text) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) { // ✅ ps closed
            ps.setInt(1, review.getProductId());
            ps.setInt(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Review error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);                   // ✅ conn closed
        }
    }
}