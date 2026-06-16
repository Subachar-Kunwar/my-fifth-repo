package view;

public class Notification_page extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
        java.util.logging.Logger.getLogger(
            Notification_page.class.getName());

    private String username;
    private int userId;
    private controller.NotificationController notifController;

    // ✅ No-arg constructor
    public Notification_page() {
        this("User", -1);
    }

    // ✅ Main constructor
    public Notification_page(String username, int userId) {
        initComponents();
        this.username = username;
        this.userId   = userId;
        this.notifController =
            new controller.NotificationController(userId);
        loadNotifications();
        setupButtons();
    }

    // ─── Load Notifications - Much Simpler Now ────────────────
    private void loadNotifications() {
        jPanel11.removeAll();

        // ✅ Controller returns simple String arrays
        java.util.List<String[]> data =
            notifController.getNotificationData();

        if (data.isEmpty()) {
            javax.swing.JLabel none =
                new javax.swing.JLabel("No notifications.");
            none.setFont(new java.awt.Font("Segoe UI", 1, 16));
            none.setHorizontalAlignment(
                javax.swing.JLabel.CENTER);
            jPanel11.add(none);
        } else {
            for (String[] item : data) {
                // item[0]=id, item[1]=message, 
                // item[2]=date, item[3]=isRead
                jPanel11.add(createCard(
                    Integer.parseInt(item[0]),
                    item[1], item[2],
                    Boolean.parseBoolean(item[3])));
            }
        }

        jPanel11.revalidate();
        jPanel11.repaint();
    }

    // ─── Create Card - Simple ─────────────────────────────────
    private javax.swing.JPanel createCard(
            int id, String message,
            String date, boolean isRead) {

        javax.swing.JPanel panel =
            new javax.swing.JPanel(null);
        panel.setPreferredSize(
            new java.awt.Dimension(900, 60));
        panel.setBackground(isRead
            ? new java.awt.Color(200, 230, 200)
            : new java.awt.Color(170, 218, 172));

        javax.swing.JCheckBox check =
            new javax.swing.JCheckBox();
        check.setBounds(20, 15, 30, 30);
        check.setBackground(panel.getBackground());
        check.putClientProperty("notifId", id);

        javax.swing.JLabel msg =
            new javax.swing.JLabel(message);
        msg.setBounds(70, 20, 600, 20);

        javax.swing.JLabel dateLabel =
            new javax.swing.JLabel(date);
        dateLabel.setBounds(720, 20, 150, 20);

        panel.add(check);
        panel.add(msg);
        panel.add(dateLabel);

        return panel;
    }

    // ─── Setup Buttons ────────────────────────────────────────
    private void setupButtons() {

        jButton1.addActionListener(e -> {
            String result = notifController.markAllAsRead();
            if (result == null) {
                loadNotifications();
                javax.swing.JOptionPane.showMessageDialog(this,
                    "All marked as read!",
                    "Success",
                    javax.swing.JOptionPane
                        .INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(this,
                    result, "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        });

        jButton2.addActionListener(e -> {
            java.util.List<Integer> selectedIds =
                getSelectedIds();

            if (selectedIds.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(this,
                    "No notifications selected!",
                    "Info",
                    javax.swing.JOptionPane
                        .INFORMATION_MESSAGE);
                return;
            }

            int confirm = javax.swing.JOptionPane
                .showConfirmDialog(this,
                    "Delete selected?",
                    "Confirm",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirm ==
                    javax.swing.JOptionPane.YES_OPTION) {
                String result = notifController
                    .deleteNotifications(selectedIds);
                if (result == null) {
                    loadNotifications();
                } else {
                    javax.swing.JOptionPane
                        .showMessageDialog(this,
                            result, "Error",
                            javax.swing.JOptionPane
                                .ERROR_MESSAGE);
                }
            }
        });
    }

    // ─── Get Selected IDs from checkboxes ─────────────────────
    private java.util.List<Integer> getSelectedIds() {
        java.util.List<Integer> ids =
            new java.util.ArrayList<>();

        for (java.awt.Component comp :
                jPanel11.getComponents()) {
            if (comp instanceof javax.swing.JPanel) {
                for (java.awt.Component inner :
                        ((javax.swing.JPanel) comp)
                            .getComponents()) {
                    if (inner instanceof
                            javax.swing.JCheckBox) {
                        javax.swing.JCheckBox cb =
                            (javax.swing.JCheckBox) inner;
                        if (cb.isSelected()) {
                            ids.add((Integer) cb
                                .getClientProperty(
                                    "notifId"));
                        }
                    }
                }
            }
        }
        return ids;
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
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel11 = new javax.swing.JPanel();
        navbar_product_catalog = new javax.swing.JPanel();
        Logo_productcatalog = new javax.swing.JLabel();
        ProfileBtn = new javax.swing.JButton();
        BellBtn = new javax.swing.JButton();
        CartBtn = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Main_panal_productcatalog.setBackground(new java.awt.Color(232, 255, 233));
        Main_panal_productcatalog.setLayout(null);

        jButton1.setBackground(new java.awt.Color(170, 218, 172));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText("Mark all as read");
        Main_panal_productcatalog.add(jButton1);
        jButton1.setBounds(530, 700, 170, 40);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Notifications");
        Main_panal_productcatalog.add(jLabel1);
        jLabel1.setBounds(100, 140, 240, 40);

        jPanel11.setBackground(new java.awt.Color(170, 218, 172));
        jPanel11.setLayout(new java.awt.GridLayout(0, 1, 0, 15));
        jScrollPane1.setViewportView(jPanel11);

        Main_panal_productcatalog.add(jScrollPane1);
        jScrollPane1.setBounds(100, 200, 1030, 410);

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
                .addContainerGap()
                .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Logo_productcatalog, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, navbar_product_catalogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(navbar_product_catalogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ProfileBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BellBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(201, 201, 201))
        );

        Main_panal_productcatalog.add(navbar_product_catalog);
        navbar_product_catalog.setBounds(0, 0, 1580, 48);

        jButton2.setBackground(new java.awt.Color(170, 218, 172));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("Delete");
        jButton2.addActionListener(this::jButton2ActionPerformed);
        Main_panal_productcatalog.add(jButton2);
        jButton2.setBounds(740, 700, 130, 40);

        jButton4.setBackground(new java.awt.Color(170, 218, 172));
        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 0, 204));
        jButton4.setText("Back");
        jButton4.addActionListener(this::jButton4ActionPerformed);
        Main_panal_productcatalog.add(jButton4);
        jButton4.setBounds(370, 700, 120, 40);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 6, Short.MAX_VALUE)
                .addComponent(Main_panal_productcatalog, javax.swing.GroupLayout.PREFERRED_SIZE, 1556, javax.swing.GroupLayout.PREFERRED_SIZE))
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
      
        javax.swing.JOptionPane.showMessageDialog(this,
            "You are currently on Notifications Page !",
            "Notifications",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_BellBtnActionPerformed

    private void CartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CartBtnActionPerformed
   // ✅ Coming soon
        javax.swing.JOptionPane.showMessageDialog(this,
            "Cart page coming soon!",
            "Cart",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_CartBtnActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
     // ✅ Handled in setupButtons() - nothing needed here
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        UserDashboard userDash = new UserDashboard(username, userId);
    userDash.setSize(1550, 840);
    userDash.setLocationRelativeTo(null);
    userDash.setVisible(true);
    this.dispose();

    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
    try {
        for (javax.swing.UIManager.LookAndFeelInfo info :
                javax.swing.UIManager.getInstalledLookAndFeels()) {
            if ("Nimbus".equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(
                    info.getClassName());
                break;
            }
        }
    } catch (ReflectiveOperationException |
             javax.swing.UnsupportedLookAndFeelException ex) {
        logger.log(java.util.logging.Level.SEVERE, null, ex);
    }

    // ✅ NEW - matches new constructor
    java.awt.EventQueue.invokeLater(
        () -> new Notification_page("TestUser", 1).setVisible(true));
}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BellBtn;
    private javax.swing.JButton CartBtn;
    private javax.swing.JLabel Logo_productcatalog;
    private javax.swing.JPanel Main_panal_productcatalog;
    private javax.swing.JButton ProfileBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel navbar_product_catalog;
    // End of variables declaration//GEN-END:variables
}