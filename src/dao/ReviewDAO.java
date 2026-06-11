package dao;

import Database.MySqlConnector;
import model.Review;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public List<Review> getReviewsByUser(int userId) {

        List<Review> list = new ArrayList<>();

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = """
                SELECT r.id, r.product_id, r.rating, r.review_text, r.created_at,
                       p.name, p.image_path
                FROM product_reviews r
                JOIN products p ON r.product_id = p.id
                WHERE r.user_id = ?
                ORDER BY r.created_at DESC
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getInt("rating"),
                        rs.getString("review_text"),
                        rs.getTimestamp("created_at")
                ));
            }

            connector.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void deleteReview(int reviewId) {
        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = "DELETE FROM product_reviews WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reviewId);
            ps.executeUpdate();

            connector.closeConnection(conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
