package controller;

import dao.ProductDetailDAO;
import model.ProductReview;
import view.product_details;

import javax.swing.*;
import java.awt.*;

public class ProductDetailController {

    private product_details view;
    private int selectedRating = 0;
    private int productId;
    private int userId;

    public ProductDetailController(product_details view, int productId, int userId) {

    this.view = view;
    this.productId = productId;
    this.userId = userId;

    loadProductDetails();  // ✅ moved here
 System.out.println("Opening notifications for userId: " + userId);
    setupStars();
    setupSubmit();
    setupNavigation();
}

    // ⭐ STAR SYSTEM
    private void setupStars() {

        JLabel[] stars = view.getStars();

        for (int i = 0; i < stars.length; i++) {

            final int rating = i + 1;

            stars[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {

                    selectedRating = rating;

                    for (int j = 0; j < stars.length; j++) {
                        if (j < rating) {
                            stars[j].setText("★");
                            stars[j].setForeground(new Color(255, 180, 0)); // yellow
                        } else {
                            stars[j].setText("☆");
                            stars[j].setForeground(Color.BLACK);
                        }
                    }
                }
            });
        }
    }

    // ✅ SUBMIT REVIEW
    private void setupSubmit() {

        view.getSubmitButton().addActionListener(e -> {

            if (selectedRating == 0) {
                JOptionPane.showMessageDialog(view, "Please select rating!");
                return;
            }

            String reviewText = view.getReviewText();

            ProductReview review = new ProductReview(
                    productId,
                    userId,
                    selectedRating,
                    reviewText
            );

            ProductDetailDAO dao = new ProductDetailDAO();
            boolean success = dao.insertReview(review);

            if (success) {
                JOptionPane.showMessageDialog(view, "Review submitted successfully!");
                view.clearReview();
                resetStars();
            } else {
                JOptionPane.showMessageDialog(view, "Error submitting review.");
            }
        });
    }

    // ✅ RESET STARS
    private void resetStars() {
        JLabel[] stars = view.getStars();
        for (JLabel star : stars) {
            star.setText("☆");
            star.setForeground(Color.BLACK);
        }
        selectedRating = 0;
    }

    // ✅ NAVIGATION
    private void setupNavigation() {

    view.getProfileBtn().addActionListener(e ->
            JOptionPane.showMessageDialog(view, "Home page open")
    );

    view.getHomeBtn().addActionListener(e ->
            JOptionPane.showMessageDialog(view, "Home page open")
    );

    view.getCartBtn().addActionListener(e ->
            JOptionPane.showMessageDialog(view, "View Cart")
    );

    view.getBellBtn().addActionListener(e -> {
        new view.Notification_page(userId).setVisible(true);
        view.dispose();
    });

    view.getShopBtn().addActionListener(e -> {
        new view.Product_catalog("User", userId).setVisible(true);
        view.dispose();
    });

    view.getAddToCartBtn().addActionListener(e ->
        JOptionPane.showMessageDialog(view, "Adding to cart...")
    );

    // ✅ NEW REVIEW BUTTON
    view.getReviewBtn().addActionListener(e -> {
        new view.Review_page(userId).setVisible(true);
        view.dispose();
    });
}
private void loadProductDetails() {

    try {
        Database.MySqlConnector connector = new Database.MySqlConnector();
        java.sql.Connection conn = connector.openConnection();

        String sql = "SELECT * FROM products WHERE id=?";
        java.sql.PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, productId);

        java.sql.ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            view.getProductNameLabel().setText(rs.getString("name"));
            view.getProductPriceLabel().setText(String.valueOf(rs.getDouble("price")));
            view.getProductSizeLabel().setText(rs.getString("category"));
            view.getDescriptionLabel().setText(rs.getString("description"));

            String imagePath = rs.getString("image_path");

            if (imagePath != null) {
                java.net.URL imgURL = view.getClass().getResource("/" + imagePath);
                if (imgURL != null) {
                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURL);
                    java.awt.Image scaled = icon.getImage()
                            .getScaledInstance(250, 220, java.awt.Image.SCALE_SMOOTH);
                    view.getPhotoLabel().setIcon(new javax.swing.ImageIcon(scaled));
                }
            }
        }

        connector.closeConnection(conn);

    } catch (Exception e) {
        System.out.println("Load product error: " + e.getMessage());
    }
}
   
   
}