package controller;

import dao.ProductcatalogDAO;
import dao.OrderDAO;
import model.Product;
import java.util.List;

public class ProductController {

    private final ProductcatalogDAO dao = new ProductcatalogDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    // ─── Get Filtered Products ────────────────────────────────
    public List<Product> getFilteredProducts(String category,
            double minPrice, double maxPrice, String searchTerm) {

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return dao.searchByName(searchTerm);
        }

        boolean hasCategory = category != null && !category.isEmpty();
        boolean hasPrice = minPrice > 0 || maxPrice < Double.MAX_VALUE;

        if (hasCategory && hasPrice) {
            return dao.getByCategoryAndPrice(category, minPrice, maxPrice);
        } else if (hasCategory) {
            return dao.getByCategory(category);
        } else if (hasPrice) {
            return dao.getByPriceRange(minPrice, maxPrice);
        } else {
            return dao.getAllProducts();
        }
    }

    // ─── Sort ─────────────────────────────────────────────────
    public List<Product> sortProducts(List<Product> products, String sortOption) {
        if (products == null || sortOption == null) return products;
        if (sortOption.equals("Price: Low to High")) {
            products.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        } else if (sortOption.equals("Price: High to Low")) {
            products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
        return products;
    }

    // ─── Place Order ──────────────────────────────────────────
    public int placeOrder(int productId, int userId, double price) {
        return orderDAO.placeOrder(productId, userId, price);
    }

    // ─── Format Filter Label ──────────────────────────────────
    public String getFilterLabelText(String category,
            double minPrice, double maxPrice, String search) {

        if (search != null && !search.isEmpty()) {
            return "<html>Currently showing:<br><b>Search: \""
                + search + "\"</b></html>";
        }
        boolean hasCategory = category != null;
        boolean hasPrice = minPrice > 0 || maxPrice < Double.MAX_VALUE;

        if (hasCategory && hasPrice) {
            return "<html>Currently showing:<br><b>"
                + category + "</b><br>Rs "
                + (int) minPrice + " - " + (int) maxPrice + "</html>";
        } else if (hasCategory) {
            return "<html>Currently showing:<br><b>"
                + category + "</b></html>";
        } else if (hasPrice) {
            return "<html>Currently showing:<br><b>Rs "
                + (int) minPrice + " - " + (int) maxPrice + "</b></html>";
        } else {
            return "<html>Currently showing:<br><b>All Products</b></html>";
        }
    }

    // ─── Populate Product Grid ────────────────────────────────
    public void populateProductGrid(javax.swing.JPanel panel,
            List<Product> products, int loggedInUserId,
            java.awt.Component parent, Runnable onBuyClick) {

        panel.removeAll();

        if (products == null || products.isEmpty()) {
            panel.setLayout(new java.awt.FlowLayout(
                java.awt.FlowLayout.LEFT, 20, 20));
            javax.swing.JLabel none = new javax.swing.JLabel("No products found.");
            none.setFont(new java.awt.Font("Candara", 1, 28));
            none.setForeground(java.awt.Color.BLACK);
            panel.add(none);
        } else {
            panel.setLayout(new java.awt.GridLayout(0, 3, 20, 20));
            for (Product p : products) {
                panel.add(buildProductCard(p, loggedInUserId,
                    parent, onBuyClick));
            }
        }

        panel.revalidate();
        panel.repaint();
    }

    // ─── Build Single Product Card ────────────────────────────
    private javax.swing.JPanel buildProductCard(Product p, int loggedInUserId,
            java.awt.Component parent, Runnable onBuyClick) {

        javax.swing.JPanel card = new javax.swing.JPanel();
        card.setLayout(new java.awt.BorderLayout());
        card.setPreferredSize(new java.awt.Dimension(200, 275));
        card.setBackground(java.awt.Color.WHITE);
        card.setBorder(javax.swing.BorderFactory.createLineBorder(
            new java.awt.Color(200, 200, 200)));

        // Image
        javax.swing.JLabel imgLabel = new javax.swing.JLabel();
        imgLabel.setPreferredSize(new java.awt.Dimension(200, 200));
        imgLabel.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        imgLabel.setBackground(new java.awt.Color(240, 240, 240));
        imgLabel.setOpaque(true);

        try {
            if (p.getImagePath() != null && !p.getImagePath().isEmpty()) {
                java.net.URL imgURL = getClass()
                    .getResource("/" + p.getImagePath());
                if (imgURL != null) {
                    javax.swing.ImageIcon icon = new javax.swing.ImageIcon(imgURL);
                    java.awt.Image scaled = icon.getImage()
                        .getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new javax.swing.ImageIcon(scaled));
                } else {
                    imgLabel.setText("No Image");
                }
            } else {
                imgLabel.setText("No Image");
            }
        } catch (Exception e) {
            imgLabel.setText("No Image");
        }

        // Bottom info panel
        javax.swing.JPanel infoPanel = new javax.swing.JPanel();
        infoPanel.setLayout(new java.awt.BorderLayout());
        infoPanel.setBackground(java.awt.Color.WHITE);
        infoPanel.setBorder(javax.swing.BorderFactory
            .createEmptyBorder(5, 8, 5, 8));

        javax.swing.JLabel nameLabel = new javax.swing.JLabel(p.getName());
        nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        nameLabel.setForeground(java.awt.Color.BLACK);
        nameLabel.setHorizontalAlignment(javax.swing.JLabel.LEFT);

        javax.swing.JLabel priceLabel = new javax.swing.JLabel(
            "Rs " + (int) p.getPrice());
        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
        priceLabel.setForeground(new java.awt.Color(58, 125, 68));
        priceLabel.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

        javax.swing.JLabel stockLabel = new javax.swing.JLabel(
            "Stock: " + p.getStock());
        stockLabel.setFont(new java.awt.Font("Segoe UI", 0, 11));
        stockLabel.setForeground(p.getStock() > 0
            ? new java.awt.Color(100, 100, 100) : java.awt.Color.RED);
        stockLabel.setHorizontalAlignment(javax.swing.JLabel.LEFT);

        javax.swing.JPanel topRow = new javax.swing.JPanel(
            new java.awt.BorderLayout());
        topRow.setBackground(java.awt.Color.WHITE);
        topRow.add(nameLabel, java.awt.BorderLayout.WEST);
        topRow.add(priceLabel, java.awt.BorderLayout.EAST);

        infoPanel.add(topRow, java.awt.BorderLayout.NORTH);
        infoPanel.add(stockLabel, java.awt.BorderLayout.SOUTH);

        // Buy button
        javax.swing.JButton buyBtn = new javax.swing.JButton("Buy Now");
        buyBtn.setBackground(new java.awt.Color(58, 125, 68));
        buyBtn.setForeground(java.awt.Color.WHITE);
        buyBtn.setFont(new java.awt.Font("Segoe UI", 1, 12));
        buyBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        buyBtn.addActionListener(e -> {
            new view.product_details(p.getId(), loggedInUserId).setVisible(true);
            if (onBuyClick != null) onBuyClick.run();
        });

        infoPanel.add(buyBtn, java.awt.BorderLayout.CENTER);
        card.add(imgLabel, java.awt.BorderLayout.CENTER);
        card.add(infoPanel, java.awt.BorderLayout.SOUTH);

        return card;
    }
}