package controller;

import dao.ReviewDAO;
import model.Review;
import view.Review_page;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReviewController {

    private Review_page view;
    private int userId;
    private ReviewDAO dao;

    public ReviewController(Review_page view, int userId) {
        this.view = view;
        this.userId = userId;
        this.dao = new ReviewDAO();

        loadReviews();
        setupNavigation();
    }

    // ✅ LOAD REVIEWS
    private void loadReviews() {

        JPanel container = view.getReviewContainer();
        container.removeAll();
        container.setLayout(new GridLayout(0, 1, 0, 15));
        container.setBackground(new Color(232, 255, 233));

        List<Review> reviews = dao.getReviewsByUser(userId);

        if (reviews.isEmpty()) {
            JLabel empty = new JLabel("No reviews yet", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.BOLD, 20));
            empty.setForeground(Color.BLACK);
            container.add(empty);
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            for (Review r : reviews) {

                JPanel panel = new JPanel(null);
                panel.setPreferredSize(new Dimension(1000, 120));
                panel.setBackground(new Color(170, 218, 172));

                // ✅ Product Image
                JLabel img = new JLabel();
                img.setBounds(20, 25, 90, 70);

                try {
                    java.net.URL url = view.getClass().getResource("/" + r.getImagePath());
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        Image scaled = icon.getImage().getScaledInstance(90, 70, Image.SCALE_SMOOTH);
                        img.setIcon(new ImageIcon(scaled));
                    }
                } catch (Exception ignored) {}

                // ✅ Product Name (Bold + Bigger)
                JLabel name = new JLabel(r.getProductName());
                name.setBounds(130, 15, 300, 25);
                name.setFont(new Font("Segoe UI", Font.BOLD, 18));
                name.setForeground(Color.BLACK);

                // ✅ Star Rating (Bigger + Yellow)
               String filled = "★".repeat(r.getRating());
String empty = "☆".repeat(5 - r.getRating());

JLabel stars = new JLabel(filled + empty);
stars.setBounds(130, 45, 200, 25);
stars.setFont(new Font("Dialog", Font.PLAIN, 24)); // ✅ FIXED FONT
stars.setForeground(new Color(255, 180, 0));

                // ✅ Review Text (Bigger)
                JLabel reviewText = new JLabel("<html>" + r.getReviewText() + "</html>");
                reviewText.setBounds(450, 40, 350, 40);
                reviewText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                reviewText.setForeground(Color.BLACK);

                // ✅ Date (Smaller + Soft Color)
                JLabel date = new JLabel(sdf.format(r.getCreatedAt()));
                date.setBounds(130, 75, 150, 20);
                date.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                date.setForeground(new Color(90, 90, 90));

                // ✅ Delete Button (Text only)
                JButton delete = new JButton("Delete");
                delete.setBounds(850, 45, 90, 30);
                delete.setFont(new Font("Segoe UI", Font.BOLD, 14));
                delete.setBackground(Color.WHITE);

                delete.addActionListener(e -> {
                    dao.deleteReview(r.getId());
                    loadReviews();
                });

                panel.add(img);
                panel.add(name);
                panel.add(stars);
                panel.add(reviewText);
                panel.add(date);
                panel.add(delete);

                container.add(panel);
            }
        }

        container.revalidate();
        container.repaint();
    }

    // ✅ Navigation
    private void setupNavigation() {
        view.getBackButton().addActionListener(e -> view.dispose());
    }
}