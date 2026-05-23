package view;

import controller.ResetController;
import javax.swing.JOptionPane;
import view.Resetpassword;

/**
 *
 * @author nikes
 */
public class OTPpage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = 
        java.util.logging.Logger.getLogger(OTPpage.class.getName());
    
    private String userEmail;
    private String expectedOTP;

   public OTPpage() {
    initComponents();
    setupOTPFields();
}

public OTPpage(String email, String otp) {
    initComponents();
    this.userEmail = email;
    this.expectedOTP = otp;
    setupOTPFields();
}
    
private void setupOTPFields() {
    // Set maximum size to 1 character for all fields
    jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (jTextField2.getText().length() >= 1) {
                if (jTextField2.getText().length() > 1) {
                    jTextField2.setText(jTextField2.getText().substring(0, 1));
                }
                // Auto move to next box AFTER typing
                java.awt.EventQueue.invokeLater(() -> {
                    if (jTextField2.getText().length() == 1) {
                        jTextField3.requestFocus();
                        jTextField3.selectAll();
                    }
                });
            }
        }
    });
    
    jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (jTextField3.getText().length() >= 1) {
                if (jTextField3.getText().length() > 1) {
                    jTextField3.setText(jTextField3.getText().substring(0, 1));
                }
                java.awt.EventQueue.invokeLater(() -> {
                    if (jTextField3.getText().length() == 1) {
                        jTextField4.requestFocus();
                        jTextField4.selectAll();
                    }
                });
            }
        }
    });
    
    jTextField4.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (jTextField4.getText().length() >= 1) {
                if (jTextField4.getText().length() > 1) {
                    jTextField4.setText(jTextField4.getText().substring(0, 1));
                }
                java.awt.EventQueue.invokeLater(() -> {
                    if (jTextField4.getText().length() == 1) {
                        jTextField5.requestFocus();
                        jTextField5.selectAll();
                    }
                });
            }
        }
    });
    
    // Last box - just limit to 1 character
    jTextField5.addKeyListener(new java.awt.event.KeyAdapter() {
        public void keyReleased(java.awt.event.KeyEvent evt) {
            if (jTextField5.getText().length() > 1) {
                jTextField5.setText(jTextField5.getText().substring(0, 1));
            }
        }
    });
    
    // LEFT ARROW - move to previous box (always works, even with values)
    java.awt.event.KeyAdapter leftArrowAdapter = new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_LEFT) {
                javax.swing.JTextField source = (javax.swing.JTextField) evt.getSource();
                if (source == jTextField2) {
                    // At first box, just select all
                    jTextField2.selectAll();
                } else if (source == jTextField3) {
                    jTextField2.requestFocus();
                    jTextField2.selectAll();
                } else if (source == jTextField4) {
                    jTextField3.requestFocus();
                    jTextField3.selectAll();
                } else if (source == jTextField5) {
                    jTextField4.requestFocus();
                    jTextField4.selectAll();
                }
            }
        }
    };
    
    // RIGHT ARROW - move to next box (always works, even with values)
    java.awt.event.KeyAdapter rightArrowAdapter = new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_RIGHT) {
                javax.swing.JTextField source = (javax.swing.JTextField) evt.getSource();
                if (source == jTextField2) {
                    jTextField3.requestFocus();
                    jTextField3.selectAll();
                } else if (source == jTextField3) {
                    jTextField4.requestFocus();
                    jTextField4.selectAll();
                } else if (source == jTextField4) {
                    jTextField5.requestFocus();
                    jTextField5.selectAll();
                } else if (source == jTextField5) {
                    jTextField5.selectAll();
                }
            }
        }
    };
    
    // BACKSPACE - delete current or move to previous
    java.awt.event.KeyAdapter backspaceAdapter = new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE) {
                javax.swing.JTextField source = (javax.swing.JTextField) evt.getSource();
                
                if (!source.getText().isEmpty()) {
                    // If box has content, delete it (one press)
                    source.setText("");
                } else {
                    // If box is empty, move to previous box
                    if (source == jTextField2) {
                        // First box, do nothing
                        source.selectAll();
                    } else if (source == jTextField3) {
                        jTextField2.requestFocus();
                        jTextField2.selectAll();
                    } else if (source == jTextField4) {
                        jTextField3.requestFocus();
                        jTextField3.selectAll();
                    } else if (source == jTextField5) {
                        jTextField4.requestFocus();
                        jTextField4.selectAll();
                    }
                }
            }
        }
    };
    
    // DELETE key - clear current box
    java.awt.event.KeyAdapter deleteAdapter = new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE) {
                javax.swing.JTextField source = (javax.swing.JTextField) evt.getSource();
                source.setText("");
            }
        }
    };
    
    // HOME / END keys
    java.awt.event.KeyAdapter homeEndAdapter = new java.awt.event.KeyAdapter() {
        public void keyPressed(java.awt.event.KeyEvent evt) {
            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_HOME) {
                jTextField2.requestFocus();
                jTextField2.selectAll();
            } else if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_END) {
                jTextField5.requestFocus();
                jTextField5.selectAll();
            }
        }
    };
    
    // Add all adapters to all fields
    jTextField2.addKeyListener(leftArrowAdapter);
    jTextField2.addKeyListener(rightArrowAdapter);
    jTextField2.addKeyListener(backspaceAdapter);
    jTextField2.addKeyListener(deleteAdapter);
    jTextField2.addKeyListener(homeEndAdapter);
    
    jTextField3.addKeyListener(leftArrowAdapter);
    jTextField3.addKeyListener(rightArrowAdapter);
    jTextField3.addKeyListener(backspaceAdapter);
    jTextField3.addKeyListener(deleteAdapter);
    jTextField3.addKeyListener(homeEndAdapter);
    
    jTextField4.addKeyListener(leftArrowAdapter);
    jTextField4.addKeyListener(rightArrowAdapter);
    jTextField4.addKeyListener(backspaceAdapter);
    jTextField4.addKeyListener(deleteAdapter);
    jTextField4.addKeyListener(homeEndAdapter);
    
    jTextField5.addKeyListener(leftArrowAdapter);
    jTextField5.addKeyListener(rightArrowAdapter);
    jTextField5.addKeyListener(backspaceAdapter);
    jTextField5.addKeyListener(deleteAdapter);
    jTextField5.addKeyListener(homeEndAdapter);
    
    // Select all text when focused (for easy editing)
    java.awt.event.FocusAdapter selectAllOnFocus = new java.awt.event.FocusAdapter() {
        public void focusGained(java.awt.event.FocusEvent evt) {
            javax.swing.JTextField source = (javax.swing.JTextField) evt.getSource();
            source.selectAll();
        }
    };
    
    jTextField2.addFocusListener(selectAllOnFocus);
    jTextField3.addFocusListener(selectAllOnFocus);
    jTextField4.addFocusListener(selectAllOnFocus);
    jTextField5.addFocusListener(selectAllOnFocus);
}
    
    private String getFullOTP() {
    return jTextField2.getText().trim() + 
           jTextField3.getText().trim() + 
           jTextField4.getText().trim() + 
           jTextField5.getText().trim();
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
                          

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Text1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        Text3 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        verify_btn = new javax.swing.JButton();
        Text8 = new javax.swing.JLabel();
        Button3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(232, 255, 233));
        jPanel1.setPreferredSize(new java.awt.Dimension(1550, 840));

        Text1.setFont(new java.awt.Font("Arial Black", 1, 50)); // NOI18N
        Text1.setText("Verify OTP");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/view/Logore.png"))); // NOI18N

        Text3.setFont(new java.awt.Font("Candara", 1, 24)); // NOI18N
        Text3.setText("Enter the OTP");

        jTextField2.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setPreferredSize(new java.awt.Dimension(70, 70));
        jTextField2.addActionListener(this::jTextField2ActionPerformed);

        jTextField3.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setPreferredSize(new java.awt.Dimension(70, 70));
        jTextField3.addActionListener(this::jTextField3ActionPerformed);

        jTextField4.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jTextField4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField4.setPreferredSize(new java.awt.Dimension(70, 70));
        jTextField4.addActionListener(this::jTextField4ActionPerformed);

        jTextField5.setFont(new java.awt.Font("Arial Black", 0, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setPreferredSize(new java.awt.Dimension(70, 70));
        jTextField5.addActionListener(this::jTextField5ActionPerformed);

        verify_btn.setBackground(new java.awt.Color(170, 218, 172));
        verify_btn.setFont(new java.awt.Font("Arial Black", 0, 14)); // NOI18N
        verify_btn.setForeground(new java.awt.Color(51, 51, 51));
        verify_btn.setText("Verify Otp");
        verify_btn.addActionListener(this::verify_btnActionPerformed);

        Text8.setFont(new java.awt.Font("Candara", 1, 18)); // NOI18N
        Text8.setText("Didn't received OTP?");

        Button3.setBackground(new java.awt.Color(232, 255, 233));
        Button3.setFont(new java.awt.Font("Arial Black", 0, 12)); // NOI18N
        Button3.setForeground(new java.awt.Color(255, 0, 51));
        Button3.setText("Resend");
        Button3.addActionListener(this::Button3ActionPerformed);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 573, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Text3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Text1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(verify_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(Text8)
                        .addGap(18, 18, 18)
                        .addComponent(Button3)))
                .addGap(643, 643, 643))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addGap(27, 27, 27)
                .addComponent(Text1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(Text3)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Text8)
                    .addComponent(Button3))
                .addGap(33, 33, 33)
                .addComponent(verify_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 353, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jTextField4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField4ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void verify_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_verify_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_verify_btnActionPerformed

    private void Button3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Button3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Button3ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new OTPpage().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Button3;
    private javax.swing.JLabel Text1;
    private javax.swing.JLabel Text3;
    private javax.swing.JLabel Text8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JButton verify_btn;
    // End of variables declaration//GEN-END:variables
}
