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

    private void loadReviews() {

        JPanel container = view.getReviewContainer();
        container.removeAll();
        container.setLayout(new GridLayout(0, 1, 0, 15));
        container.setBackground(new Color(232, 255, 233));

        List<Review> reviews = dao.getReviewsByUser(userId);

        if (reviews.isEmpty()) {
            JLabel empty = new JLabel("No reviews yet", SwingConstants.CENTER);
            empty.setFont(new Font("Segoe UI", Font.BOLD, 18));
            container.add(empty);
        } else {

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

            for (Review r : reviews) {

                JPanel panel = new JPanel(null);
                panel.setPreferredSize(new Dimension(1000, 100));
                panel.setBackground(new Color(170, 218, 172));

                JLabel img = new JLabel();
                img.setBounds(20, 20, 80, 60);

                try {
                    java.net.URL url = view.getClass().getResource("/" + r.getImagePath());
                    if (url != null) {
                        ImageIcon icon = new ImageIcon(url);
                        Image scaled = icon.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
                        img.setIcon(new ImageIcon(scaled));
                    }
                } catch (Exception ignored) {}

                JLabel name = new JLabel(r.getProductName());
                name.setBounds(120, 10, 200, 20);

                JLabel stars = new JLabel("★".repeat(r.getRating()));
                stars.setBounds(120, 35, 150, 20);
                stars.setForeground(new Color(255, 180, 0));

                JLabel text = new JLabel(r.getReviewText());
                text.setBounds(300, 35, 300, 20);

                JLabel date = new JLabel(sdf.format(r.getCreatedAt()));
                date.setBounds(120, 60, 150, 20);

                JButton delete = new JButton("Delete");
                delete.setBounds(850, 35, 80, 25);

                delete.addActionListener(e -> {
                    dao.deleteReview(r.getId());
                    loadReviews();
                });

                panel.add(img);
                panel.add(name);
                panel.add(stars);
                panel.add(text);
                panel.add(date);
                panel.add(delete);

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