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
                return reviewDAO.getAllReviews();
            } else {
                return reviewDAO.getByProduct(productId);
            }
        } catch (Exception e) {
            System.out.println("Get reviews error: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    // ─── Delete Review ────────────────────────────────────────
    public String deleteReview(int reviewId, int userId) {
        if (reviewId <= 0) return "Invalid review!";
        try {
            boolean deleted = reviewDAO.delete(reviewId, userId);
            if (deleted) {
                new dao.ActivityDAO().logActivity(
                    userId, "Deleted review #" + reviewId);
                return null;
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
        return new java.text.SimpleDateFormat("dd MMM yyyy").format(date);
    }

    // ─── Get Stars Text ───────────────────────────────────────
    public String getStarsText(int rating) {
        return "★".repeat(rating) + "☆".repeat(5 - rating);
    }

    // ─── Can Delete Review ────────────────────────────────────
    public boolean canDelete(int reviewUserId) {
        return reviewUserId == userId;
    }

    // ─── Populate Review Panel ────────────────────────────────
    public void populateReviewPanel(javax.swing.JPanel panel,
                                    java.awt.Component parent,
                                    Runnable onRefresh) {
        panel.removeAll();

        List<Review> reviews = getReviews();

        if (reviews == null || reviews.isEmpty()) {
            javax.swing.JLabel none = new javax.swing.JLabel("No reviews yet.");
            none.setFont(new java.awt.Font("Segoe UI", 1, 18));
            none.setHorizontalAlignment(javax.swing.JLabel.CENTER);
            panel.add(none);
        } else {
            for (Review r : reviews) {
                panel.add(buildReviewCard(r, parent, onRefresh));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    // ─── Build Single Review Card ─────────────────────────────
    private javax.swing.JPanel buildReviewCard(Review r,
                                               java.awt.Component parent,
                                               Runnable onRefresh) {

        javax.swing.JPanel panel = new javax.swing.JPanel(null);
        panel.setPreferredSize(new java.awt.Dimension(1000, 120));
        panel.setBackground(new java.awt.Color(170, 218, 172));

        // Product image
        javax.swing.JLabel img = new javax.swing.JLabel();
        img.setBounds(20, 25, 90, 70);
        try {
            java.net.URL url = getClass().getResource("/" + r.getImagePath());
            if (url != null) {
                javax.swing.ImageIcon icon = new javax.swing.ImageIcon(url);
                java.awt.Image scaled = icon.getImage()
                    .getScaledInstance(90, 70, java.awt.Image.SCALE_SMOOTH);
                img.setIcon(new javax.swing.ImageIcon(scaled));
            }
        } catch (Exception ignored) {}

        javax.swing.JLabel name = new javax.swing.JLabel(r.getProductName());
        name.setBounds(130, 10, 300, 25);
        name.setFont(new java.awt.Font("Segoe UI", 1, 18));

        javax.swing.JLabel user = new javax.swing.JLabel("by " + r.getUsername());
        user.setBounds(130, 35, 200, 20);
        user.setFont(new java.awt.Font("Segoe UI", 0, 13));

        javax.swing.JLabel stars = new javax.swing.JLabel(getStarsText(r.getRating()));
        stars.setBounds(130, 55, 200, 25);
        stars.setFont(new java.awt.Font("Dialog", 0, 24));
        stars.setForeground(new java.awt.Color(255, 180, 0));

        javax.swing.JLabel reviewText = new javax.swing.JLabel(
            "<html>" + r.getReviewText() + "</html>");
        reviewText.setBounds(450, 45, 350, 40);
        reviewText.setFont(new java.awt.Font("Segoe UI", 0, 16));

        javax.swing.JLabel date = new javax.swing.JLabel(formatDate(r.getCreatedAt()));
        date.setBounds(130, 80, 150, 20);
        date.setFont(new java.awt.Font("Segoe UI", 0, 13));

        panel.add(img);
        panel.add(name);
        panel.add(user);
        panel.add(stars);
        panel.add(reviewText);
        panel.add(date);

        // Delete button only for own reviews
        if (canDelete(r.getReviewUserId())) {
            javax.swing.JButton delete = new javax.swing.JButton("Delete");
            delete.setBounds(850, 45, 90, 30);
            delete.addActionListener(e -> {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(parent,
                    "Delete this review?", "Confirm",
                    javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    String result = deleteReview(r.getId(), userId);
                    if (result == null) {
                        onRefresh.run();
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(parent,
                            result, "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            panel.add(delete);
        }

        return panel;
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getProductId() { return productId; }
    public int getUserId()    { return userId; }
}