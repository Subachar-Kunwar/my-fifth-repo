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
    public Product getProduct() {
        return productDetailDAO.getProductById(productId);
    }

    // ─── Submit Review ────────────────────────────────────────
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

    // ─── Populate Product Details into Fields ─────────────────
    public Product populateProductFields(
            javax.swing.JLabel nameLabel,
            javax.swing.JLabel priceLabel,
            javax.swing.JLabel categoryLabel,
            javax.swing.JLabel descLabel,
            javax.swing.JLabel imageLabel) {

        Product product = getProduct();
        if (product == null) return null;

        nameLabel.setText(product.getName());
        priceLabel.setText(String.valueOf(product.getPrice()));
        categoryLabel.setText(product.getCategory());
        descLabel.setText(product.getDescription());

        if (product.getImagePath() != null &&
                !product.getImagePath().isEmpty()) {
            try {
                java.net.URL imgURL =
                    getClass().getResource("/" + product.getImagePath());
                if (imgURL != null) {
                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURL);
                    java.awt.Image scaled = icon.getImage()
                        .getScaledInstance(250, 220,
                            java.awt.Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new javax.swing.ImageIcon(scaled));
                    imageLabel.setText("");
                }
            } catch (Exception e) {
                imageLabel.setText("No Image");
            }
        }
        return product;
    }

    // ─── Reset Star Rating UI ─────────────────────────────────
    public void resetStarRating(javax.swing.JLabel... stars) {
        for (javax.swing.JLabel star : stars) {
            star.setText("☆");
            star.setForeground(java.awt.Color.BLACK);
        }
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getProductId() { return productId; }
    public int getUserId()    { return userId; }
}