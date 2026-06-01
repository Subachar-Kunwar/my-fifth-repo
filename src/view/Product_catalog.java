/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

/**ss
 *
 * @author LENOVO
 */
public class Product_catalog extends javax.swing.JFrame {
    
    protected static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Product_catalog.class.getName());

    /**
     * Creates new form Product_catalog
     */
    
   
javax.swing.JScrollPane Scrollpanal_productcatalog;
javax.swing.JPanel panal_forscroll;
javax.swing.JLabel sortLabel;
private String loggedInUsername;

public Product_catalog() {
    this("Guest", -1);
}

public Product_catalog(String username, int userId) {
    initComponents();
    this.loggedInUsername = username;
    this.loggedInUserId = userId;
    
  // Add scroll pane and product panel
Scrollpanal_productcatalog = new javax.swing.JScrollPane();
panal_forscroll = new javax.swing.JPanel();
panal_forscroll.setBackground(new java.awt.Color(232, 255, 233));
panal_forscroll.setLayout(new java.awt.GridLayout(0, 3, 20, 20));
Scrollpanal_productcatalog.setViewportView(panal_forscroll);
Main_panal_productcatalog.add(Scrollpanal_productcatalog);
Scrollpanal_productcatalog.setBounds(240, 100, 1290, 780);

// Fix searchResultLabel
searchResultLabel.setBounds(240, 57, 560, 30);
searchResultLabel.setVisible(false);

// Fix sortComboBox items and position
sortComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(
    new String[] { "Default", "Price: Low to High", "Price: High to Low" }));
sortComboBox.setBounds(900, 57, 160, 30);

// Add sort label
sortLabel = new javax.swing.JLabel("Sort by:");
sortLabel.setForeground(java.awt.Color.BLACK);
sortLabel.setFont(new java.awt.Font("Candara", 1, 16));
Main_panal_productcatalog.add(sortLabel);
sortLabel.setBounds(828, 57, 70, 30);

Main_panal_productcatalog.remove(jScrollBar1);

// Smart sizing
java.awt.Dimension screenSize =
    java.awt.Toolkit.getDefaultToolkit().getScreenSize();
if (screenSize.width < 1600 || screenSize.height < 900) {
    this.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
} else {
    this.setSize(1550, 840);
    this.setLocationRelativeTo(null);
}


initBackend();

// Force placeholder on startup
javax.swing.SwingUtilities.invokeLater(() -> {
    Searchbar.setText("Search Products.....");
    Searchbar.setForeground(java.awt.Color.GRAY);
    Searchbar.transferFocus();
});

// Search button icon
searchBtn.setText("");
try {
    javax.swing.ImageIcon searchIcon = new javax.swing.ImageIcon(
        getClass().getResource("/view/search_icon.png"));
    java.awt.Image scaled = searchIcon.getImage()
        .getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
    searchBtn.setIcon(new javax.swing.ImageIcon(scaled));
} catch (Exception ex) {
   
}


// Logo clickable — resets filters
Logo_productcatalog.setCursor(
    new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
Logo_productcatalog.addMouseListener(new java.awt.event.MouseAdapter() {
    public void mouseClicked(java.awt.event.MouseEvent e) {
        categoryGroup.clearSelection();
        priceGroup.clearSelection();
        Searchbar.setText("Search Products.....");
        Searchbar.setForeground(java.awt.Color.GRAY);
        loadProducts();
    }
});

// Adjust Home and Shop button positions
Home_btn_productcatalog.setBounds(10, 63, 80, 30);
shop_btn_productcatalog.setBounds(115, 63, 80, 30);

// Arrow label between Home and Shop
javax.swing.JLabel arrowLabel = new javax.swing.JLabel("›");
arrowLabel.setFont(new java.awt.Font("Arial Black", 1, 18));
arrowLabel.setForeground(new java.awt.Color(58, 125, 68));
Main_panal_productcatalog.add(arrowLabel);
arrowLabel.setBounds(93, 63, 20, 30);

// Adjust scroll and side panel start
Scrollpanal_productcatalog.setBounds(240, 110, 1290, 780);
sidepanal_productcatalog.setBounds(0, 110, 240, 780);


    // Resize panels when window size changes
    this.addComponentListener(new java.awt.event.ComponentAdapter() {
        public void componentResized(java.awt.event.ComponentEvent e) {
            int frameWidth = getContentPane().getWidth();
            int frameHeight = getContentPane().getHeight();
            int sideWidth = 240;
            int topHeight = 100;
            
            Main_panal_productcatalog.setPreferredSize(
                new java.awt.Dimension(frameWidth, frameHeight));
            
            Scrollpanal_productcatalog.setBounds(
                sideWidth,
                topHeight,
                frameWidth - sideWidth,
                frameHeight - topHeight);
            
            sidepanal_productcatalog.setBounds(
                0, topHeight, sideWidth, frameHeight - topHeight);
            
            navbar_product_catalog.setBounds(
                0, 0, frameWidth, 48);
            
            // Reposition sort combo and label together
            // Reposition sort combo and label together
        sortComboBox.setBounds(frameWidth - 200, 57, 160, 30);
        sortLabel.setBounds(frameWidth - 270, 57, 70, 30);
        searchResultLabel.setBounds(240, 57, frameWidth - 650, 25);
        Home_btn_productcatalog.setBounds(10, 63, 80, 30);
        shop_btn_productcatalog.setBounds(100, 63, 80, 30);

            Main_panal_productcatalog.revalidate();
            Main_panal_productcatalog.repaint();
        }
    });
}



private final controller.ProductController productController = 
    new controller.ProductController();
private int loggedInUserId;

private void initBackend() {

    women_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    men_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    children_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    unisex_RadioButton_productcatalog.addActionListener(e -> loadProducts());

    g100to500g_price_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    g500to1000g_price_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    g1000to2000g_price_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    g2000to5000g_price_RadioButton_productcatalog.addActionListener(e -> loadProducts());
    g5000to10000g_price_RadioButton_productcatalog.addActionListener(e -> loadProducts());

    Searchbar.setForeground(java.awt.Color.GRAY);
    Searchbar.addFocusListener(new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent e) {
            if (Searchbar.getText().equals("Search Products.....")) {
                Searchbar.setText("");
                Searchbar.setForeground(java.awt.Color.BLACK);
            }
        }
        public void focusLost(java.awt.event.FocusEvent e) {
            if (Searchbar.getText().isEmpty()) {
                Searchbar.setText("Search Products.....");
                Searchbar.setForeground(java.awt.Color.GRAY);
            }
        }
    });

    Searchbar.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                loadProducts();
            }
        }
    });

    loadProducts();
}

private void loadProducts() {
    String category = getSelectedCategory();
    double[] range = getSelectedPriceRange();
    String search = getSearchText();

    java.util.List<model.Product> products = productController
        .getFilteredProducts(category, range[0], range[1], search);

    products = sortProducts(products);

    if (search != null && !search.isEmpty()) {
        searchResultLabel.setText(
            "Search results for: \"" + search + "\"");
        searchResultLabel.setVisible(true);
    } else {
        searchResultLabel.setVisible(false);
    }

    updateFilterLabel(category, range, search);
    productCountLabel.setText(products.size() + " products found");
    displayProducts(products);
}

private java.util.List<model.Product> sortProducts(
        java.util.List<model.Product> products) {
    String selected = (String) sortComboBox.getSelectedItem();
    if (selected.equals("Price: Low to High")) {
        products.sort((a, b) ->
            Double.compare(a.getPrice(), b.getPrice()));
    } else if (selected.equals("Price: High to Low")) {
        products.sort((a, b) ->
            Double.compare(b.getPrice(), a.getPrice()));
    }
    return products;
}

private String getSelectedCategory() {
    if (women_RadioButton_productcatalog.isSelected()) return "Women";
    if (men_RadioButton_productcatalog.isSelected()) return "Men";
    if (children_RadioButton_productcatalog.isSelected()) return "Children";
    if (unisex_RadioButton_productcatalog.isSelected()) return "Unisex";
    return null;
}

private double[] getSelectedPriceRange() {
    if (g100to500g_price_RadioButton_productcatalog.isSelected())
        return new double[]{100, 500};
    if (g500to1000g_price_RadioButton_productcatalog.isSelected())
        return new double[]{500, 1000};
    if (g1000to2000g_price_RadioButton_productcatalog.isSelected())
        return new double[]{1000, 2000};
    if (g2000to5000g_price_RadioButton_productcatalog.isSelected())
        return new double[]{2000, 5000};
    if (g5000to10000g_price_RadioButton_productcatalog.isSelected())
        return new double[]{5000, 10000};
    return new double[]{0, Double.MAX_VALUE};
}

private String getSearchText() {
    String text = Searchbar.getText().trim();
    return text.equals("Search Products.....") ? "" : text;
}

private void updateFilterLabel(String category,
                                double[] range, String search) {
    if (search != null && !search.isEmpty()) {
        currentFilterLabel.setText("<html>Currently showing:<br>"
            + "<b>Search: \"" + search + "\"</b></html>");
        return;
    }
    boolean hasCategory = category != null;
    boolean hasPrice = range[0] > 0 || range[1] < Double.MAX_VALUE;

    if (hasCategory && hasPrice) {
        currentFilterLabel.setText("<html>Currently showing:<br><b>"
            + category + "</b><br>Rs "
            + (int) range[0] + " - " + (int) range[1] + "</html>");
    } else if (hasCategory) {
        currentFilterLabel.setText("<html>Currently showing:<br><b>"
            + category + "</b></html>");
    } else if (hasPrice) {
        currentFilterLabel.setText("<html>Currently showing:<br><b>Rs "
            + (int) range[0] + " - " + (int) range[1] + "</b></html>");
    } else {
        currentFilterLabel.setText("<html>Currently showing:"
            + "<br><b>All Products</b></html>");
    }
}

private void displayProducts(java.util.List<model.Product> products) {
    panal_forscroll.removeAll();

    if (products.isEmpty()) {
        panal_forscroll.setLayout(
            new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 20, 20));
        javax.swing.JLabel none =
            new javax.swing.JLabel("No products found.");
        none.setFont(new java.awt.Font("Candara", 1, 28));
        none.setForeground(java.awt.Color.BLACK);
        panal_forscroll.add(none);
    } else {
        panal_forscroll.setLayout(
            new java.awt.GridLayout(0, 3, 20, 20));
        for (model.Product p : products) {
            panal_forscroll.add(createProductCard(p));
        }
    }

    panal_forscroll.revalidate();
    panal_forscroll.repaint();
}
private javax.swing.JPanel createProductCard(model.Product p) {
    javax.swing.JPanel card = new javax.swing.JPanel();
    card.setLayout(new java.awt.BorderLayout());
    card.setPreferredSize(new java.awt.Dimension(200, 260));
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
        java.net.URL imgURL = getClass()
            .getResource("/" + p.getImagePath());
        if (imgURL != null) {
            javax.swing.ImageIcon icon =
                new javax.swing.ImageIcon(imgURL);
            java.awt.Image scaled = icon.getImage()
                .getScaledInstance(200, 200,
                    java.awt.Image.SCALE_SMOOTH);
            imgLabel.setIcon(new javax.swing.ImageIcon(scaled));
        } else {
            imgLabel.setText("No Image");
        }
    } catch (Exception e) {
        imgLabel.setText("No Image");
    }

    // Bottom info panel with name and price separate
    javax.swing.JPanel infoPanel = new javax.swing.JPanel();
    infoPanel.setLayout(new java.awt.BorderLayout());
    infoPanel.setBackground(java.awt.Color.WHITE);
    infoPanel.setBorder(javax.swing.BorderFactory
        .createEmptyBorder(5, 8, 5, 8));

    // Product name
    javax.swing.JLabel nameLabel = new javax.swing.JLabel(p.getName());
    nameLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
    nameLabel.setForeground(java.awt.Color.BLACK);
    nameLabel.setHorizontalAlignment(javax.swing.JLabel.LEFT);

    // Price with green color
    javax.swing.JLabel priceLabel = new javax.swing.JLabel(
        "Rs " + (int) p.getPrice());
    priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 13));
    priceLabel.setForeground(new java.awt.Color(58, 125, 68));
    priceLabel.setHorizontalAlignment(javax.swing.JLabel.RIGHT);

    infoPanel.add(nameLabel, java.awt.BorderLayout.WEST);
    infoPanel.add(priceLabel, java.awt.BorderLayout.EAST);

    card.add(imgLabel, java.awt.BorderLayout.CENTER);
    card.add(infoPanel, java.awt.BorderLayout.SOUTH);

    return card;
}


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        categoryGroup = new javax.swing.ButtonGroup();
        priceGroup = new javax.swing.ButtonGroup();
        Main_panal_productcatalog = new javax.swing.JPanel();
        navbar_product_catalog = new javax.swing.JPanel();
        Logo_productcatalog = new javax.swing.JLabel();
        Searchbar = new javax.swing.JTextField();
        searchBtn = new javax.swing.JButton();
        profileBtn = new javax.swing.JButton();
        bellBtn = new javax.swing.JButton();
        cartBtn = new javax.swing.JButton();
        Home_btn_productcatalog = new javax.swing.JButton();
        shop_btn_productcatalog = new javax.swing.JButton();
        sidepanal_productcatalog = new javax.swing.JPanel();
        Filtertext_productcatalog = new javax.swing.JLabel();
        catagotytext_productcatalog = new javax.swing.JLabel();
        women_RadioButton_productcatalog = new javax.swing.JRadioButton();
        men_RadioButton_productcatalog = new javax.swing.JRadioButton();
        children_RadioButton_productcatalog = new javax.swing.JRadioButton();
        unisex_RadioButton_productcatalog = new javax.swing.JRadioButton();
        Price_range_text_productcatalog = new javax.swing.JLabel();
        g100to500g_price_RadioButton_productcatalog = new javax.swing.JRadioButton();
        g500to1000g_price_RadioButton_productcatalog = new javax.swing.JRadioButton();
        g1000to2000g_price_RadioButton_productcatalog = new javax.swing.JRadioButton();
        g2000to5000g_price_RadioButton_productcatalog = new javax.swing.JRadioButton();
        g5000to10000g_price_RadioButton_productcatalog = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        currentFilterLabel = new javax.swing.JLabel();
        productCountLabel = new javax.swing.JLabel();
        clearFiltersBtn = new javax.swing.JButton();
        jScrollBar1 = new javax.swing.JScrollBar();
        searchResultLabel = new javax.swing.JLabel();
        sortComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_panal_productcatalog.setBackground(new java.awt.Color(232, 255, 233));
        Main_panal_productcatalog.setLayout(null);

        navbar_product_catalog.setBackground(new java.awt.Color(58, 125, 68));
        navbar_product_catalog.setMinimumSize(new java.awt.Dimension(100, 48));
        navbar_product_catalog.setPreferredSize(new java.awt.Dimension(100, 88));

        Logo_productcatalog.setText("Logo");

        Searchbar.setText("Search Products.....");
        Searchbar.addActionListener(this::SearchbarActionPerformed);

        searchBtn.setBackground(new java.awt.Color(58, 125, 68));
        searchBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/search_icon.png"))); // NOI18N
        searchBtn.addActionListener(this::searchBtnActionPerformed);

        profileBtn.setBackground(new java.awt.Color(58, 125, 68));
        profileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Vector (1).png"))); // NOI18N
        profileBtn.addActionListener(this::profileBtnActionPerformed);

        bellBtn.setBackground(new java.awt.Color(58, 125, 68));
        bellBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Group (1).png"))); // NOI18N
        bellBtn.addActionListener(this::bellBtnActionPerformed);

        cartBtn.setBackground(new java.awt.Color(58, 125, 68));
        cartBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Group.png"))); // NOI18N
        cartBtn.addActionListener(this::cartBtnActionPerformed);

        javax.swing.GroupLayout navbar_product_catalogLayout = new javax.swing.GroupLayout(navbar_product_catalog);
        navbar_product_catalog.setLayout(navbar_product_catalogLayout);
        navbar_product_catalogLayout.setHorizontalGroup(
            navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbar_product_catalogLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(Logo_productcatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(170, 170, 170)
                .addComponent(Searchbar, javax.swing.GroupLayout.PREFERRED_SIZE, 561, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 592, Short.MAX_VALUE)
                .addComponent(cartBtn)
                .addGap(18, 18, 18)
                .addComponent(profileBtn)
                .addGap(18, 18, 18)
                .addComponent(bellBtn)
                .addGap(18, 18, 18))
        );
        navbar_product_catalogLayout.setVerticalGroup(
            navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(Logo_productcatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(Searchbar))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbar_product_catalogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(navbar_product_catalogLayout.createSequentialGroup()
                        .addComponent(bellBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbar_product_catalogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(searchBtn)
                            .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(profileBtn)
                                .addComponent(cartBtn)))
                        .addGap(8, 8, 8))))
        );

        Main_panal_productcatalog.add(navbar_product_catalog);
        navbar_product_catalog.setBounds(0, 0, 1560, 48);

        Home_btn_productcatalog.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        Home_btn_productcatalog.setText("Home");
        Home_btn_productcatalog.addActionListener(this::Home_btn_productcatalogActionPerformed);
        Main_panal_productcatalog.add(Home_btn_productcatalog);
        Home_btn_productcatalog.setBounds(10, 60, 70, 25);

        shop_btn_productcatalog.setBackground(new java.awt.Color(170, 218, 172));
        shop_btn_productcatalog.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        shop_btn_productcatalog.setText("Shop");
        shop_btn_productcatalog.addActionListener(this::shop_btn_productcatalogActionPerformed);
        Main_panal_productcatalog.add(shop_btn_productcatalog);
        shop_btn_productcatalog.setBounds(90, 60, 70, 25);

        sidepanal_productcatalog.setBackground(new java.awt.Color(232, 255, 233));
        sidepanal_productcatalog.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        sidepanal_productcatalog.setLayout(null);

        Filtertext_productcatalog.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Filtertext_productcatalog.setText("Filters");
        sidepanal_productcatalog.add(Filtertext_productcatalog);
        Filtertext_productcatalog.setBounds(10, 10, 150, 40);

        catagotytext_productcatalog.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        catagotytext_productcatalog.setText("Category :");
        sidepanal_productcatalog.add(catagotytext_productcatalog);
        catagotytext_productcatalog.setBounds(40, 60, 100, 30);

        categoryGroup.add(women_RadioButton_productcatalog);
        women_RadioButton_productcatalog.setText("Women");
        sidepanal_productcatalog.add(women_RadioButton_productcatalog);
        women_RadioButton_productcatalog.setBounds(20, 100, 65, 21);

        categoryGroup.add(men_RadioButton_productcatalog);
        men_RadioButton_productcatalog.setText("Men");
        sidepanal_productcatalog.add(men_RadioButton_productcatalog);
        men_RadioButton_productcatalog.setBounds(20, 130, 47, 21);

        categoryGroup.add(children_RadioButton_productcatalog);
        children_RadioButton_productcatalog.setText("Children");
        sidepanal_productcatalog.add(children_RadioButton_productcatalog);
        children_RadioButton_productcatalog.setBounds(20, 160, 68, 21);

        categoryGroup.add(unisex_RadioButton_productcatalog);
        unisex_RadioButton_productcatalog.setText("Unisex");
        sidepanal_productcatalog.add(unisex_RadioButton_productcatalog);
        unisex_RadioButton_productcatalog.setBounds(20, 190, 100, 21);

        Price_range_text_productcatalog.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Price_range_text_productcatalog.setText("Price Range :");
        sidepanal_productcatalog.add(Price_range_text_productcatalog);
        Price_range_text_productcatalog.setBounds(10, 220, 140, 40);

        priceGroup.add(g100to500g_price_RadioButton_productcatalog);
        g100to500g_price_RadioButton_productcatalog.setText("100 - 500");
        sidepanal_productcatalog.add(g100to500g_price_RadioButton_productcatalog);
        g100to500g_price_RadioButton_productcatalog.setBounds(20, 270, 98, 21);

        priceGroup.add(g500to1000g_price_RadioButton_productcatalog);
        g500to1000g_price_RadioButton_productcatalog.setText("500 - 1000");
        sidepanal_productcatalog.add(g500to1000g_price_RadioButton_productcatalog);
        g500to1000g_price_RadioButton_productcatalog.setBounds(20, 300, 120, 21);

        priceGroup.add(g1000to2000g_price_RadioButton_productcatalog);
        g1000to2000g_price_RadioButton_productcatalog.setText("1000 - 2000");
        sidepanal_productcatalog.add(g1000to2000g_price_RadioButton_productcatalog);
        g1000to2000g_price_RadioButton_productcatalog.setBounds(20, 330, 98, 21);

        priceGroup.add(g2000to5000g_price_RadioButton_productcatalog);
        g2000to5000g_price_RadioButton_productcatalog.setText("2000 - 5000");
        sidepanal_productcatalog.add(g2000to5000g_price_RadioButton_productcatalog);
        g2000to5000g_price_RadioButton_productcatalog.setBounds(20, 360, 98, 21);

        priceGroup.add(g5000to10000g_price_RadioButton_productcatalog);
        g5000to10000g_price_RadioButton_productcatalog.setText("5000 - 10000");
        sidepanal_productcatalog.add(g5000to10000g_price_RadioButton_productcatalog);
        g5000to10000g_price_RadioButton_productcatalog.setBounds(20, 390, 98, 21);

        jSeparator1.setFont(new java.awt.Font("Segoe UI Black", 1, 12)); // NOI18N
        sidepanal_productcatalog.add(jSeparator1);
        jSeparator1.setBounds(0, 440, 240, 10);

        currentFilterLabel.setText("CurrentFilterLabel");
        sidepanal_productcatalog.add(currentFilterLabel);
        currentFilterLabel.setBounds(20, 460, 210, 60);

        productCountLabel.setText("ProductCountLevel");
        sidepanal_productcatalog.add(productCountLabel);
        productCountLabel.setBounds(20, 510, 210, 60);

        clearFiltersBtn.setBackground(new java.awt.Color(58, 125, 68));
        clearFiltersBtn.setForeground(new java.awt.Color(255, 255, 255));
        clearFiltersBtn.setText("Clear all filters");
        clearFiltersBtn.addActionListener(this::clearFiltersBtnActionPerformed);
        sidepanal_productcatalog.add(clearFiltersBtn);
        clearFiltersBtn.setBounds(20, 590, 120, 23);

        Main_panal_productcatalog.add(sidepanal_productcatalog);
        sidepanal_productcatalog.setBounds(0, 100, 240, 780);
        Main_panal_productcatalog.add(jScrollBar1);
        jScrollBar1.setBounds(1540, 50, 10, 790);
        Main_panal_productcatalog.add(searchResultLabel);
        searchResultLabel.setBounds(240, 60, 560, 0);

        sortComboBox.setBackground(new java.awt.Color(170, 218, 172));
        sortComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        sortComboBox.addActionListener(this::sortComboBoxActionPerformed);
        Main_panal_productcatalog.add(sortComboBox);
        sortComboBox.setBounds(1390, 110, 72, 22);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Main_panal_productcatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 1560, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_panal_productcatalog, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Home_btn_productcatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Home_btn_productcatalogActionPerformed
        new Login().setVisible(true);
    this.dispose();
    }//GEN-LAST:event_Home_btn_productcatalogActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
     String search = getSearchText();
    if (search == null || search.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Please enter a product name to search!",
            "Search",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    loadProducts();

    }//GEN-LAST:event_searchBtnActionPerformed

    private void profileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileBtnActionPerformed
        javax.swing.JOptionPane.showMessageDialog(
        this,
        "Notification page coming soon!",
        "Info",
        javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_profileBtnActionPerformed

    private void bellBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bellBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bellBtnActionPerformed

    private void SearchbarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SearchbarActionPerformed
       loadProducts();
    }//GEN-LAST:event_SearchbarActionPerformed

    private void clearFiltersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearFiltersBtnActionPerformed
     categoryGroup.clearSelection();
    priceGroup.clearSelection();
    Searchbar.setText("Search Products.....");
    Searchbar.setForeground(java.awt.Color.GRAY);
    loadProducts();
    }//GEN-LAST:event_clearFiltersBtnActionPerformed

    private void sortComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sortComboBoxActionPerformed
                                                 
    loadProducts();

    }//GEN-LAST:event_sortComboBoxActionPerformed

    private void cartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cartBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cartBtnActionPerformed

    private void shop_btn_productcatalogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shop_btn_productcatalogActionPerformed

    javax.swing.JOptionPane.showMessageDialog(
        this,
        "You are already on the Shop page!",
        "Shop",
        javax.swing.JOptionPane.INFORMATION_MESSAGE);

    }//GEN-LAST:event_shop_btn_productcatalogActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Product_catalog().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Filtertext_productcatalog;
    private javax.swing.JButton Home_btn_productcatalog;
    private javax.swing.JLabel Logo_productcatalog;
    private javax.swing.JPanel Main_panal_productcatalog;
    private javax.swing.JLabel Price_range_text_productcatalog;
    private javax.swing.JTextField Searchbar;
    private javax.swing.JButton bellBtn;
    private javax.swing.JButton cartBtn;
    private javax.swing.JLabel catagotytext_productcatalog;
    private javax.swing.ButtonGroup categoryGroup;
    private javax.swing.JRadioButton children_RadioButton_productcatalog;
    private javax.swing.JButton clearFiltersBtn;
    private javax.swing.JLabel currentFilterLabel;
    private javax.swing.JRadioButton g1000to2000g_price_RadioButton_productcatalog;
    private javax.swing.JRadioButton g100to500g_price_RadioButton_productcatalog;
    private javax.swing.JRadioButton g2000to5000g_price_RadioButton_productcatalog;
    private javax.swing.JRadioButton g5000to10000g_price_RadioButton_productcatalog;
    private javax.swing.JRadioButton g500to1000g_price_RadioButton_productcatalog;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JRadioButton men_RadioButton_productcatalog;
    private javax.swing.JPanel navbar_product_catalog;
    private javax.swing.ButtonGroup priceGroup;
    private javax.swing.JLabel productCountLabel;
    private javax.swing.JButton profileBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JLabel searchResultLabel;
    private javax.swing.JButton shop_btn_productcatalog;
    private javax.swing.JPanel sidepanal_productcatalog;
    private javax.swing.JComboBox<String> sortComboBox;
    private javax.swing.JRadioButton unisex_RadioButton_productcatalog;
    private javax.swing.JRadioButton women_RadioButton_productcatalog;
    // End of variables declaration//GEN-END:variables
}
