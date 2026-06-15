package dao;

import Database.MySqlConnector;
import model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // ✅ Get all reviews for a product
    public List<Review> getReviewsByProduct(int productId) {

        List<Review> list = new ArrayList<>();

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = """
                SELECT r.id,
                       r.product_id,
                       r.user_id,
                       r.rating,
                       r.review_text,
                       r.created_at,
                       p.name,
                       p.image_path,
                       u.username
                FROM product_reviews r
                JOIN products p ON r.product_id = p.id
                JOIN users u ON r.user_id = u.id
                WHERE r.product_id = ?
                ORDER BY r.created_at DESC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, productId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                list.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getInt("user_id"),     // ✅ review owner
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getInt("rating"),
                        rs.getString("review_text"),
                        rs.getTimestamp("created_at"),
                        rs.getString("username")
                ));
            }

            connector.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ Secure delete (only own review)
    public void deleteReview(int reviewId, int userId) {

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = "DELETE FROM product_reviews WHERE id=? AND user_id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reviewId);
            ps.setInt(2, userId);
            ps.executeUpdate();

            connector.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}