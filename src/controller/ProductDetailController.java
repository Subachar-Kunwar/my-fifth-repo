package controller;

import dao.ProductDetailDAO;
import model.Product;
import model.ProductReview;

public class ProductDetailController {

    private final ProductDetailDAO productDetailDAO = new ProductDetailDAO();
    private final int productId;
    private final int userId;

    public ProductDetailController(int productId, int userId) {
        this.productId = productId;
        this.userId    = userId;
    }

    // ─── Get Product ──────────────────────────────────────────
    // Controller fetches from DAO - View displays it
    public Product getProduct() {
        return productDetailDAO.getProductById(productId);
    }

    // ─── Submit Review ────────────────────────────────────────
    // Returns null on success, error message on failure
    public String submitReview(int rating, String reviewText) {

        if (rating == 0) {
            return "Please select a star rating!";
        }

        if (reviewText == null || reviewText.trim().isEmpty()) {
            return "Please write a review!";
        }

        ProductReview review = new ProductReview(
            productId, userId, rating, reviewText);

        boolean success = productDetailDAO.insertReview(review);
        return success ? null : "Error submitting review. Please try again.";
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getProductId() {
        return productId;
    }

    public int getUserId() {
        return userId;
    }
}