package controller;

public class PayementController {

    // ─── Show QR Code Pop-Up + Get Transaction ID ─────────────
    // Returns the entered Transaction ID, or null if cancelled
    public String showQRCodePayment(String totalAmount, java.awt.Component parent) {
        try {
            // Load QR image
            java.net.URL qrUrl = getClass().getResource("/group7/rewear/eSewaQr.jpg");
            
            if (qrUrl == null) {
                javax.swing.JOptionPane.showMessageDialog(parent,
                    "QR code image not found!\nMake sure eSewaQr.jpg exists in /group7/rewear/",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
                return null;
            }
            
            javax.swing.ImageIcon qrIcon = new javax.swing.ImageIcon(qrUrl);
            java.awt.Image scaled = qrIcon.getImage()
                .getScaledInstance(280, 280, java.awt.Image.SCALE_SMOOTH);
            javax.swing.ImageIcon finalIcon = new javax.swing.ImageIcon(scaled);
            
            // Pop-up panel
            javax.swing.JPanel panel = new javax.swing.JPanel();
            panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
            panel.setBackground(new java.awt.Color(232, 255, 233));
            
            javax.swing.JLabel amountLabel = new javax.swing.JLabel("Amount: Rs " + totalAmount);
            amountLabel.setFont(new java.awt.Font("Arial Black", java.awt.Font.BOLD, 22));
            amountLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            amountLabel.setForeground(new java.awt.Color(58, 125, 68));
            
            javax.swing.JLabel qrLabel = new javax.swing.JLabel(finalIcon);
            qrLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            
            javax.swing.JLabel instructionLabel = new javax.swing.JLabel(
                "1. Scan QR with eSewa / Bank app / FonePay");
            instructionLabel.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            instructionLabel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            
            javax.swing.JLabel instructionLabel2 = new javax.swing.JLabel(
                "2. Complete the payment");
            instructionLabel2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            instructionLabel2.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            
            javax.swing.JLabel instructionLabel3 = new javax.swing.JLabel(
                "3. Click 'Payment Done' below");
            instructionLabel3.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, 14));
            instructionLabel3.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
            
            panel.add(javax.swing.Box.createVerticalStrut(10));
            panel.add(amountLabel);
            panel.add(javax.swing.Box.createVerticalStrut(15));
            panel.add(qrLabel);
            panel.add(javax.swing.Box.createVerticalStrut(15));
            panel.add(instructionLabel);
            panel.add(instructionLabel2);
            panel.add(instructionLabel3);
            panel.add(javax.swing.Box.createVerticalStrut(10));
            
            int result = javax.swing.JOptionPane.showOptionDialog(
                parent, panel, "Payment QR",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"✓ Payment Done", "Cancel"},
                "✓ Payment Done"
            );
            
            if (result != javax.swing.JOptionPane.OK_OPTION) {
                return null; // user cancelled
            }
            
            // ✅ Now ask for Transaction ID
            return askForTransactionId(parent);
            
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(parent,
                "Error showing QR: " + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // ─── Ask User for Transaction ID ──────────────────────────
    private String askForTransactionId(java.awt.Component parent) {
        while (true) {
            String txnId = javax.swing.JOptionPane.showInputDialog(
                parent,
                "Please enter your Transaction/Reference ID:\n\n"
                + "• If paid via eSewa app → use eSewa Transaction ID\n"
                + "• If paid via bank app → use Bank Reference Number\n"
                + "• If paid via FonePay → use FonePay Reference\n\n"
                + "(You'll find this in your SMS or payment app)",
                "Payment Reference Required",
                javax.swing.JOptionPane.QUESTION_MESSAGE
            );
            
            // User cancelled
            if (txnId == null) {
                int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    parent,
                    "Cancel the order? Your payment may have been processed.",
                    "Cancel Order?",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                    return null;
                }
                continue; // ask again
            }
            
            txnId = txnId.trim();
            
            // Validate not empty
            if (txnId.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(parent,
                    "Transaction ID cannot be empty!",
                    "Required",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                continue;
            }
            
            // Validate length (TXN IDs are typically 4+ chars)
            if (txnId.length() < 4) {
                javax.swing.JOptionPane.showMessageDialog(parent,
                    "Transaction ID looks too short! Please check.",
                    "Invalid Format",
                    javax.swing.JOptionPane.WARNING_MESSAGE);
                continue;
            }
            
            return txnId; // ✅ valid
        }
    }
}