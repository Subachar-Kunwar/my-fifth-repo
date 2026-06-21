package dao;

import Database.MySqlConnector;
import model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Common SQL ───────────────────────────────────────────
    private static final String SELECT_SQL =
        "SELECT r.id, r.product_id, r.user_id, " +
        "r.rating, r.review_text, r.created_at, " +
        "p.name, p.image_path, u.username " +
        "FROM product_reviews r " +
        "JOIN products p ON r.product_id = p.id " +
        "JOIN users u ON r.user_id = u.id ";

    // ─── Get Reviews By Product ───────────────────────────────
    public List<Review> getByProduct(int productId) {
        Connection conn = mysql.openConnection();
        List<Review> list = new ArrayList<>();
        String sql = SELECT_SQL +
                     "WHERE r.product_id = ? " +
                     "ORDER BY r.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get reviews error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Get ALL Reviews ──────────────────────────────────────
    public List<Review> getAllReviews() {
        Connection conn = mysql.openConnection();
        List<Review> list = new ArrayList<>();
        String sql = SELECT_SQL +
                     "ORDER BY r.created_at DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapReview(rs));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get all reviews error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return list;
    }

    // ─── Delete Review ────────────────────────────────────────
    public boolean delete(int reviewId, int userId) {
        Connection conn = mysql.openConnection();
        String sql = "DELETE FROM product_reviews " +
                     "WHERE id = ? AND user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Delete error: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // ─── Map ResultSet to Review ──────────────────────────────
    private Review mapReview(ResultSet rs) throws SQLException {
        return new Review(
            rs.getInt("id"),
            rs.getInt("product_id"),
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("image_path"),
            rs.getInt("rating"),
            rs.getString("review_text"),
            rs.getTimestamp("created_at"),
            rs.getString("username")
        );
    }
}