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
    private int productId;
    private int userId;
    private ReviewDAO dao;

    public ReviewController(Review_page view, int productId, int userId) {

        this.view = view;
        this.productId = productId;
        this.userId = userId;
        this.dao = new ReviewDAO();

        loadReviews();
        setupNavigation();
    }

    private void loadReviews() {

        JPanel container = view.getReviewContainer();
        container.removeAll();
        container.setLayout(new GridLayout(0, 1, 0, 15));
        container.setBackground(new Color(232, 255, 233));

        List<Review> reviews = dao.getReviewsByProduct(productId);

        if (reviews.isEmpty()) {

            JLabel empty = new JLabel("No reviews yet", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.BOLD, 20));
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
                        Image scaled = icon.getImage()
                                .getScaledInstance(90, 70, Image.SCALE_SMOOTH);
                        img.setIcon(new ImageIcon(scaled));
                    }
                } catch (Exception ignored) {}

                // ✅ Product Name
                JLabel name = new JLabel(r.getProductName());
                name.setBounds(130, 10, 300, 25);
                name.setFont(new Font("Segoe UI", Font.BOLD, 18));

                // ✅ Username
                JLabel username = new JLabel("by " + r.getUsername());
                username.setBounds(130, 35, 200, 20);
                username.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                // ✅ Stars
                String filled = "★".repeat(r.getRating());
                String empty = "☆".repeat(5 - r.getRating());

                JLabel stars = new JLabel(filled + empty);
                stars.setBounds(130, 55, 200, 25);
                stars.setFont(new Font("Dialog", Font.PLAIN, 24));
                stars.setForeground(new Color(255, 180, 0));

                // ✅ Review Text
                JLabel reviewText = new JLabel("<html>" + r.getReviewText() + "</html>");
                reviewText.setBounds(450, 45, 350, 40);
                reviewText.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                // ✅ Date
                JLabel date = new JLabel(sdf.format(r.getCreatedAt()));
                date.setBounds(130, 80, 150, 20);
                date.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                panel.add(img);
                panel.add(name);
                panel.add(username);
                panel.add(stars);
                panel.add(reviewText);
                panel.add(date);

                // ✅ Show delete ONLY if review belongs to logged user
                if (r.getReviewUserId() == userId) {

                    JButton delete = new JButton("Delete");
                    delete.setBounds(850, 45, 90, 30);

                    delete.addActionListener(e -> {
                        dao.deleteReview(r.getId(), userId);
                        loadReviews();
                    });

                    panel.add(delete);
                }

                container.add(panel);
            }
        }

        container.revalidate();
        container.repaint();
    }

    private void setupNavigation() {
        view.getBackButton().addActionListener(e -> view.dispose());
    }
}