package dao;

import Database.MySqlConnector;
import model.ProductReview;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProductDetailDAO {

    public boolean insertReview(ProductReview review) {

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String sql = "INSERT INTO product_reviews (product_id, user_id, rating, review_text) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, review.getProductId());
            ps.setInt(2, review.getUserId());
            ps.setInt(3, review.getRating());
            ps.setString(4, review.getReviewText());

            ps.executeUpdate();

            connector.closeConnection(conn);
            return true;

        } catch (Exception e) {
    e.printStackTrace();
    return false;
}
        }
    }
