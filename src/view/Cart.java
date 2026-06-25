package view;

import controller.CartController;
import model.CartItem;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Cart extends javax.swing.JFrame {

    private int userId;
    private String username;
    private CartController cartController = new CartController();

    // Dynamic components
    private JPanel cartItemsPanel;
    private JLabel subtotalValue, discountValue, totalValue;

    public Cart() {
        this("Guest", -1);
    }

    public Cart(String username, int userId) {
        this.username = username;
        this.userId = userId;

        setTitle("ReWear - My Cart");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1550, 840));

        // Main content panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(232, 255, 233));
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(1550, 840));

        buildNavbar(mainPanel);
        buildHeader(mainPanel);
        buildCartItemsArea(mainPanel);
        buildSummaryPanel(mainPanel);

        setContentPane(mainPanel);
        pack();

        // Smart sizing
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (screen.width < 1600 || screen.height < 900) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
        } else {
            setSize(1550, 840);
            setLocationRelativeTo(null);
        }

        loadCartItems();
    }

    // ========================
    // NAVBAR
    // ========================
    private void buildNavbar(JPanel parent) {
        JPanel navbar = new JPanel();
        navbar.setBackground(new Color(58, 125, 68));
        navbar.setLayout(null);
        navbar.setBounds(0, 0, 1550, 48);

        JLabel logo = new JLabel();
        try {
            logo.setIcon(new ImageIcon(getClass().getResource("/group7/rewear/rewearLogo.jpeg")));
        } catch (Exception e) {
            logo.setText("Re-WEAR");
            logo.setForeground(Color.WHITE);
            logo.setFont(new Font("Arial Black", Font.BOLD, 16));
        }
        navbar.add(logo);
        logo.setBounds(10, 10, 216, 31);

        // Profile button
        JButton profileBtn = createNavButton("/group7/rewear/userrIcon.png");
        profileBtn.setBounds(1365, 6, 44, 40);
        profileBtn.addActionListener(e -> {
            new UserDashboard(username, userId).setVisible(true);
            this.dispose();
        });
        navbar.add(profileBtn);

        // Bell button
        JButton bellBtn = createNavButton("/group7/rewear/bellbtn.png");
        bellBtn.setBounds(1421, 6, 50, 40);
        bellBtn.addActionListener(e -> {
            new Notification_page(username, userId).setVisible(true);
            this.dispose();
        });
        navbar.add(bellBtn);

        // Cart button (current page)
        JButton cartBtn = createNavButton("/group7/rewear/cartticon.png");
        cartBtn.setBounds(1477, 6, 50, 40);
        cartBtn.addActionListener(e ->
            JOptionPane.showMessageDialog(this, "You are already on the Cart page!"));
        navbar.add(cartBtn);

        parent.add(navbar);
    }

    private JButton createNavButton(String iconPath) {
        JButton btn = new JButton();
        btn.setBackground(new Color(58, 125, 68));
        btn.setBorder(BorderFactory.createLineBorder(new Color(58, 125, 68)));
        try {
            btn.setIcon(new ImageIcon(getClass().getResource(iconPath)));
        } catch (Exception e) {
            btn.setText("•");
            btn.setForeground(Color.WHITE);
        }
        return btn;
    }

    // ========================
    // HEADER (Title + Column Labels)
    // ========================
    private void buildHeader(JPanel parent) {
        JLabel title = new JLabel("My Cart");
        title.setFont(new Font("Arial Black", Font.PLAIN, 40));
        title.setBounds(50, 60, 250, 50);
        parent.add(title);

        // Column headers
        String[] headers = {"Products", "Price", "Quantity", "Total"};
        int[] xPositions = {70, 520, 800, 1200};
        for (int i = 0; i < headers.length; i++) {
            JLabel h = new JLabel(headers[i]);
            h.setFont(new Font("Arial Black", Font.PLAIN, 24));
            h.setBounds(xPositions[i], 130, 200, 40);
            parent.add(h);
        }
    }

    // ========================
    // SCROLLABLE CART ITEMS AREA
    // ========================
    private void buildCartItemsArea(JPanel parent) {
        cartItemsPanel = new JPanel();
        cartItemsPanel.setBackground(new Color(232, 255, 233));
        cartItemsPanel.setLayout(new BoxLayout(cartItemsPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(cartItemsPanel);
        scrollPane.setBounds(60, 190, 1430, 380);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        parent.add(scrollPane);
    }

    // ========================
    // SUMMARY PANEL (SubTotal, Discount, Total, Checkout Button)
    // ========================
    private void buildSummaryPanel(JPanel parent) {
        JPanel summaryPanel = new JPanel();
        summaryPanel.setBackground(new Color(170, 218, 172));
        summaryPanel.setLayout(null);
        summaryPanel.setBounds(800, 590, 680, 240);

        // Labels
        JLabel subLabel = new JLabel("Sub Total");
        subLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        subLabel.setBounds(54, 20, 200, 30);
        summaryPanel.add(subLabel);

        JLabel discLabel = new JLabel("Discount");
        discLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        discLabel.setBounds(54, 60, 200, 30);
        summaryPanel.add(discLabel);

        JLabel totLabel = new JLabel("Total");
        totLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        totLabel.setBounds(54, 100, 200, 30);
        summaryPanel.add(totLabel);

        // Values
        subtotalValue = new JLabel("Rs.  0");
        subtotalValue.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        subtotalValue.setHorizontalAlignment(SwingConstants.RIGHT);
        subtotalValue.setBounds(400, 20, 200, 30);
        summaryPanel.add(subtotalValue);

        discountValue = new JLabel("Rs.  0");
        discountValue.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        discountValue.setHorizontalAlignment(SwingConstants.RIGHT);
        discountValue.setBounds(400, 60, 200, 30);
        summaryPanel.add(discountValue);

        totalValue = new JLabel("Rs.  0");
        totalValue.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        totalValue.setHorizontalAlignment(SwingConstants.RIGHT);
        totalValue.setBounds(400, 100, 200, 30);
        summaryPanel.add(totalValue);

        // Proceed to Checkout button
        JButton checkoutBtn = new JButton("Proceed To Checkout");
        checkoutBtn.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        checkoutBtn.setBounds(179, 150, 320, 50);
        checkoutBtn.setBackground(new Color(232, 255, 233));
        checkoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkoutBtn.addActionListener(e -> proceedToCheckout());
        summaryPanel.add(checkoutBtn);

        parent.add(summaryPanel);
    }

    // ========================
    // LOAD CART ITEMS FROM DATABASE
    // ========================
    public void loadCartItems() {
        cartItemsPanel.removeAll();
        List<CartItem> items = cartController.getCartItems(userId);

        if (items.isEmpty()) {
            cartItemsPanel.add(Box.createVerticalStrut(80));

            JLabel emptyLabel = new JLabel("Your cart is empty. Start shopping!");
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 22));
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            cartItemsPanel.add(emptyLabel);

            cartItemsPanel.add(Box.createVerticalStrut(20));

            JButton shopBtn = new JButton("Browse Products");
            shopBtn.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 18));
            shopBtn.setBackground(new Color(58, 125, 68));
            shopBtn.setForeground(Color.WHITE);
            shopBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            shopBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            shopBtn.addActionListener(e -> {
                new Product_catalog(username, userId).setVisible(true);
                this.dispose();
            });
            cartItemsPanel.add(shopBtn);
        } else {
            for (CartItem item : items) {
                cartItemsPanel.add(createCartItemRow(item));
                cartItemsPanel.add(Box.createVerticalStrut(10));
            }
        }

        // Update totals
        updateTotals(items);

        cartItemsPanel.revalidate();
        cartItemsPanel.repaint();
    }

    // ==============================
    // CREATE ONE ROW FOR A CART ITEM
    // ==============================
    private JPanel createCartItemRow(CartItem item) {
        JPanel row = new JPanel();
        row.setLayout(null);
        row.setBackground(new Color(170, 218, 172));
        row.setPreferredSize(new Dimension(1400, 140));
        row.setMaximumSize(new Dimension(1400, 140));
        row.setMinimumSize(new Dimension(1400, 140));

        // ---- PRODUCT IMAGE (loaded from database image_path) ----
        JLabel imgLabel = new JLabel();
        imgLabel.setBounds(20, 10, 120, 120);
        imgLabel.setBorder(BorderFactory.createLineBorder(new Color(204, 204, 204), 2));
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imgLabel.setBackground(Color.WHITE);
        imgLabel.setOpaque(true);

        try {
            String imagePath = item.getImagePath();
            if (imagePath != null && !imagePath.isEmpty()) {
                java.net.URL imgURL = getClass().getResource("/" + imagePath);
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image scaled = icon.getImage()
                        .getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                    imgLabel.setIcon(new ImageIcon(scaled));
                    imgLabel.setText("");
                } else {
                    imgLabel.setText("No Image");
                }
            } else {
                imgLabel.setText("No Image");
            }
        } catch (Exception e) {
            imgLabel.setText("No Image");
        }
        row.add(imgLabel);

        // ---- PRODUCT NAME ----
        JLabel nameLabel = new JLabel(item.getProductName());
        nameLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 16));
        nameLabel.setBounds(160, 50, 250, 30);
        row.add(nameLabel);

        // ---- PRICE ----
        JLabel priceLabel = new JLabel("Rs.  " + (int) item.getPrice());
        priceLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        priceLabel.setBounds(450, 50, 200, 30);
        row.add(priceLabel);

        // ---- QUANTITY CONTROLS: [ - ]  qty  [ + ] ----
        JButton minusBtn = new JButton("-");
        minusBtn.setFont(new Font("Arial Black", Font.BOLD, 18));
        minusBtn.setBounds(730, 45, 50, 40);
        minusBtn.setBackground(new Color(232, 255, 233));
        minusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.add(minusBtn);

        JLabel qtyLabel = new JLabel(String.valueOf(item.getQuantity()));
        qtyLabel.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 22));
        qtyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qtyLabel.setBounds(790, 45, 50, 40);
        row.add(qtyLabel);

        JButton plusBtn = new JButton("+");
        plusBtn.setFont(new Font("Arial Black", Font.BOLD, 18));
        plusBtn.setBounds(850, 45, 50, 40);
        plusBtn.setBackground(new Color(232, 255, 233));
        plusBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.add(plusBtn);

        // ---- TOTAL FOR THIS ITEM ----
        JLabel itemTotal = new JLabel("Rs.  " + (int) item.getTotal());
        itemTotal.setFont(new Font("Arial Rounded MT Bold", Font.PLAIN, 24));
        itemTotal.setBounds(1100, 50, 200, 30);
        row.add(itemTotal);

        // ---- REMOVE BUTTON ----
        JButton removeBtn = new JButton("X");
        removeBtn.setFont(new Font("Arial", Font.BOLD, 14));
        removeBtn.setBounds(1340, 10, 40, 30);
        removeBtn.setBackground(new Color(220, 53, 69));
        removeBtn.setForeground(Color.WHITE);
        removeBtn.setBorder(BorderFactory.createEmptyBorder());
        removeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        row.add(removeBtn);

        // ---- BUTTON ACTIONS ----

        // MINUS button — restore stock when decreased
minusBtn.addActionListener(e -> {
    int newQty = item.getQuantity() - 1;
    if (newQty <= 0) {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Remove " + item.getProductName() + " from cart?",
            "Remove Item", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // ✅ Use restore-stock version
            cartController.removeItemAndRestoreStock(item.getId());
            loadCartItems();
        }
    } else {
        // ✅ Use restore-stock version
        cartController.decreaseQuantity(item.getId(), item.getProductId(), newQty);
        item.setQuantity(newQty);
        qtyLabel.setText(String.valueOf(newQty));
        itemTotal.setText("Rs.  " + (int) (item.getPrice() * newQty));
        refreshTotals();
    }
});

       // PLUS button
plusBtn.addActionListener(e -> {
    // ✅ Check stock first
    String result = cartController.tryIncreaseQuantity(
        item.getId(), 
        item.getProductId(), 
        item.getQuantity()
    );

    if (result == null) {
        // ✅ Success
        int newQty = item.getQuantity() + 1;
        item.setQuantity(newQty);
        qtyLabel.setText(String.valueOf(newQty));
        itemTotal.setText("Rs.  " + (int) (item.getPrice() * newQty));
        refreshTotals();
    } else {
        // ❌ Stock exceeded
        JOptionPane.showMessageDialog(this,
            result, "Stock Limit", JOptionPane.WARNING_MESSAGE);
    }
});

       // REMOVE button — restore stock
removeBtn.addActionListener(e -> {
    int confirm = JOptionPane.showConfirmDialog(this,
        "Remove " + item.getProductName() + " from cart?",
        "Remove Item", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        // ✅ Use restore-stock version
        cartController.removeItemAndRestoreStock(item.getId());
        loadCartItems();
    }
});
        return row;
    }

    // ========================
    // UPDATE TOTALS
    // ========================
    private void updateTotals(List<CartItem> items) {
        double subTotal = cartController.getSubTotal(items);
        double discount = cartController.getDiscount(subTotal);
        double total = subTotal - discount;

        subtotalValue.setText("Rs.  " + (int) subTotal);
        discountValue.setText("Rs.  " + (int) discount);
        totalValue.setText("Rs.  " + (int) total);
    }

    private void refreshTotals() {
        List<CartItem> items = cartController.getCartItems(userId);
        updateTotals(items);
    }

    // ========================
    // PROCEED TO CHECKOUT
    // ========================
    private void proceedToCheckout() {
        List<CartItem> items = cartController.getCartItems(userId);
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Your cart is empty! Add products first.",
                "Empty Cart", JOptionPane.WARNING_MESSAGE);
            return;
        }
        new Checkout(username, userId).setVisible(true);
        this.dispose();
    }
}
