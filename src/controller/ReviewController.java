package controller;

import dao.ReviewDAO;
import model.Review;
import java.util.List;

public class ReviewController {

    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final int productId;
    private final int userId;

    public ReviewController(int productId, int userId) {
        this.productId = productId;
        this.userId    = userId;
    }

    // ─── Get Reviews ──────────────────────────────────────────
    public List<Review> getReviews() {
        try {
            if (productId == 0) {
                // ✅ Get ALL reviews
                return reviewDAO.getAllReviews();
            } else {
                // ✅ Get reviews for specific product
                return reviewDAO.getByProduct(productId);
            }
        } catch (Exception e) {
            System.out.println("Get reviews error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    // ─── Delete Review ────────────────────────────────────────
    public String deleteReview(int reviewId, int userId) {
        // ✅ Validation in Controller
        if (reviewId <= 0) {
            return "Invalid review!";
        }
        try {
            boolean deleted = reviewDAO.delete(reviewId, userId);
            if (deleted) {
                // ✅ Log activity
                new dao.ActivityDAO().logActivity(
                    userId, "Deleted review #" + reviewId);
                return null; // ✅ success
            } else {
                return "Failed to delete review!";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ─── Format Date ──────────────────────────────────────────
    public String formatDate(java.util.Date date) {
        if (date == null) return "";
        return new java.text.SimpleDateFormat("dd MMM yyyy")
            .format(date);
    }

    // ─── Get Stars Text ───────────────────────────────────────
    public String getStarsText(int rating) {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    // ─── Can Delete Review ────────────────────────────────────
    public boolean canDelete(int reviewUserId) {
        return reviewUserId == userId;
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getProductId() { return productId; }
    public int getUserId()    { return userId; }
}