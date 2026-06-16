package view;

public class Review_page extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(
            Review_page.class.getName());

    private int productId;
    private int userId;
    private String username;
    private controller.ReviewController reviewController;

    public Review_page(int productId, int userId, String username) {
        initComponents();
        this.productId  = productId;
        this.userId     = userId;
        this.username   = username;
        this.reviewController =
            new controller.ReviewController(productId, userId);
        loadReviews();
    }

    // ─── Load Reviews - View only displays ───────────────────
    private void loadReviews() {
        jPanel11.removeAll();

        // ✅ Controller fetches data
        java.util.List<model.Review> reviews =
            reviewController.getReviews();

        if (reviews == null || reviews.isEmpty()) {
            javax.swing.JLabel none =
                new javax.swing.JLabel("No reviews yet.");
            none.setFont(new java.awt.Font("Segoe UI", 1, 18));
            none.setHorizontalAlignment(
                javax.swing.JLabel.CENTER);
            jPanel11.add(none);
        } else {
            for (model.Review r : reviews) {
                jPanel11.add(createReviewCard(r));
            }
        }

        jPanel11.revalidate();
        jPanel11.repaint();
    }

    // ─── Create Review Card - Pure UI ────────────────────────
    private javax.swing.JPanel createReviewCard(model.Review r) {

        javax.swing.JPanel panel = new javax.swing.JPanel(null);
        panel.setPreferredSize(
            new java.awt.Dimension(1000, 120));
        panel.setBackground(
            new java.awt.Color(170, 218, 172));

        // Product image
        javax.swing.JLabel img = new javax.swing.JLabel();
        img.setBounds(20, 25, 90, 70);
        try {
            java.net.URL url =
                getClass().getResource("/" + r.getImagePath());
            if (url != null) {
                javax.swing.ImageIcon icon =
                    new javax.swing.ImageIcon(url);
                java.awt.Image scaled = icon.getImage()
                    .getScaledInstance(90, 70,
                        java.awt.Image.SCALE_SMOOTH);
                img.setIcon(new javax.swing.ImageIcon(scaled));
            }
        } catch (Exception ignored) {}

        // Product name
        javax.swing.JLabel name =
            new javax.swing.JLabel(r.getProductName());
        name.setBounds(130, 10, 300, 25);
        name.setFont(new java.awt.Font("Segoe UI", 1, 18));

        // Username
        javax.swing.JLabel user =
            new javax.swing.JLabel("by " + r.getUsername());
        user.setBounds(130, 35, 200, 20);
        user.setFont(new java.awt.Font("Segoe UI", 0, 13));

        // ✅ Stars from Controller
        javax.swing.JLabel stars =
            new javax.swing.JLabel(
                reviewController.getStarsText(r.getRating()));
        stars.setBounds(130, 55, 200, 25);
        stars.setFont(new java.awt.Font("Dialog", 0, 24));
        stars.setForeground(new java.awt.Color(255, 180, 0));

        // Review text
        javax.swing.JLabel reviewText =
            new javax.swing.JLabel(
                "<html>" + r.getReviewText() + "</html>");
        reviewText.setBounds(450, 45, 350, 40);
        reviewText.setFont(
            new java.awt.Font("Segoe UI", 0, 16));

        // ✅ Date from Controller
        javax.swing.JLabel date =
            new javax.swing.JLabel(
                reviewController.formatDate(r.getCreatedAt()));
        date.setBounds(130, 80, 150, 20);
        date.setFont(new java.awt.Font("Segoe UI", 0, 13));

        panel.add(img);
        panel.add(name);
        panel.add(user);
        panel.add(stars);
        panel.add(reviewText);
        panel.add(date);

        // ✅ Controller decides if delete shown
        if (reviewController.canDelete(r.getReviewUserId())) {
            javax.swing.JButton delete =
                new javax.swing.JButton("Delete");
            delete.setBounds(850, 45, 90, 30);
            delete.addActionListener(e -> {
                int confirm = javax.swing.JOptionPane
                    .showConfirmDialog(this,
                        "Delete this review?",
                        "Confirm",
                        javax.swing.JOptionPane.YES_NO_OPTION);
                if (confirm ==
                        javax.swing.JOptionPane.YES_OPTION) {
                    // ✅ Controller handles delete
                    String result = reviewController
                        .deleteReview(r.getId(), userId);
                    if (result == null) {
                        loadReviews(); // ✅ Refresh UI
                    } else {
                        javax.swing.JOptionPane
                            .showMessageDialog(this,
                                result, "Error",
                                javax.swing.JOptionPane
                                    .ERROR_MESSAGE);
                    }
                }
            });
            panel.add(delete);
        }

        return panel;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Main_panal_productcatalog = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        navbar_product_catalog = new javax.swing.JPanel();
        Logo_productcatalog = new javax.swing.JLabel();
        ProfileBtn = new javax.swing.JButton();
        BellBtn = new javax.swing.JButton();
        CartBtn = new javax.swing.JButton();
        Back = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_panal_productcatalog.setBackground(new java.awt.Color(232, 255, 233));
        Main_panal_productcatalog.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Reviews ");
        Main_panal_productcatalog.add(jLabel1);
        jLabel1.setBounds(60, 140, 240, 40);

        jPanel11.setBackground(new java.awt.Color(170, 218, 172));
        jPanel11.setLayout(new java.awt.GridLayout(0, 1, 0, 15));
        jScrollPane1.setViewportView(jPanel11);

        Main_panal_productcatalog.add(jScrollPane1);
        jScrollPane1.setBounds(120, 190, 1060, 390);

        navbar_product_catalog.setBackground(new java.awt.Color(58, 125, 68));
        navbar_product_catalog.setMinimumSize(new java.awt.Dimension(100, 48));
        navbar_product_catalog.setPreferredSize(new java.awt.Dimension(100, 88));

        Logo_productcatalog.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group7/rewear/rewearLogo.jpeg"))); // NOI18N

        ProfileBtn.setBackground(new java.awt.Color(58, 125, 68));
        ProfileBtn.setForeground(new java.awt.Color(58, 125, 68));
        ProfileBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group7/rewear/userrIcon.png"))); // NOI18N
        ProfileBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ProfileBtn.addActionListener(this::ProfileBtnActionPerformed);

        BellBtn.setBackground(new java.awt.Color(58, 125, 68));
        BellBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group7/rewear/bellbtn.png"))); // NOI18N
        BellBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        BellBtn.addActionListener(this::BellBtnActionPerformed);

        CartBtn.setBackground(new java.awt.Color(58, 125, 68));
        CartBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group7/rewear/cartticon.png"))); // NOI18N
        CartBtn.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        CartBtn.addActionListener(this::CartBtnActionPerformed);

        javax.swing.GroupLayout navbar_product_catalogLayout = new javax.swing.GroupLayout(navbar_product_catalog);
        navbar_product_catalog.setLayout(navbar_product_catalogLayout);
        navbar_product_catalogLayout.setHorizontalGroup(
            navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(navbar_product_catalogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logo_productcatalog)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1082, Short.MAX_VALUE)
                .addComponent(ProfileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(BellBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(CartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91))
        );
        navbar_product_catalogLayout.setVerticalGroup(
            navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, navbar_product_catalogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ProfileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BellBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
            .addGroup(navbar_product_catalogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Logo_productcatalog, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        Main_panal_productcatalog.add(navbar_product_catalog);
        navbar_product_catalog.setBounds(0, 0, 1580, 48);

        Back.setBackground(new java.awt.Color(170, 218, 172));
        Back.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Back.setForeground(new java.awt.Color(0, 0, 153));
        Back.setText("Back");
        Back.addActionListener(this::BackActionPerformed);
        Main_panal_productcatalog.add(Back);
        Back.setBounds(630, 710, 120, 50);

        jButton1.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jButton1.setText("Home");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        Main_panal_productcatalog.add(jButton1);
        jButton1.setBounds(30, 70, 72, 30);

        jButton2.setBackground(new java.awt.Color(170, 218, 172));
        jButton2.setFont(new java.awt.Font("Arial Black", 1, 12)); // NOI18N
        jButton2.setText("Reviews");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        Main_panal_productcatalog.add(jButton2);
        jButton2.setBounds(120, 70, 90, 30);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/group7/rewear/arrow.png"))); // NOI18N
        Main_panal_productcatalog.add(jLabel2);
        jLabel2.setBounds(100, 60, 20, 50);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Main_panal_productcatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 1550, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Main_panal_productcatalog, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ProfileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProfileBtnActionPerformed
UserDashboard userDash = new UserDashboard(username, userId);
    userDash.setSize(1550, 840);
    userDash.setLocationRelativeTo(null);
    userDash.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_ProfileBtnActionPerformed

    private void BellBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BellBtnActionPerformed
     Notification_page notifPage = new Notification_page(
        username,
        userId
    );
    notifPage.setSize(1550, 840);
    notifPage.setLocationRelativeTo(null);
    notifPage.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_BellBtnActionPerformed

    private void CartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartBtnActionPerformed
// ✅ Coming soon
        javax.swing.JOptionPane.showMessageDialog(this,
            "Cart page coming soon!",
            "Cart",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_CartBtnActionPerformed

    private void BackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BackActionPerformed
    
    product_details productDetails = new product_details(productId, userId);
    productDetails.setSize(1550, 840);
    productDetails.setLocationRelativeTo(null);
    productDetails.setVisible(true);
    this.dispose();
    }//GEN-LAST:event_BackActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     javax.swing.JOptionPane.showMessageDialog(this,
        "You are currently on Reviews Page!",
        "Reviews",
        javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       UserDashboard userDash = new UserDashboard(username, userId);
    userDash.setSize(1550, 840);
    userDash.setLocationRelativeTo(null);
    userDash.setVisible(true);
    this.dispose();
    
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
   public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info : 
                javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException | 
             javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

    // ✅ Fix - pass all 3 parameters
    java.awt.EventQueue.invokeLater(
        () -> new Review_page(0, 1, "TestUser").setVisible(true));
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Back;
    private javax.swing.JButton BellBtn;
    private javax.swing.JButton CartBtn;
    private javax.swing.JLabel Logo_productcatalog;
    private javax.swing.JPanel Main_panal_productcatalog;
    private javax.swing.JButton ProfileBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel navbar_product_catalog;
    // End of variables declaration//GEN-END:variables
}