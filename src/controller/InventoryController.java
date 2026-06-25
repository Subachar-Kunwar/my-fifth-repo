package controller;

import view.Ghost_inventory;
import dao.InventoryDAO;
import model.Inventory;

import java.util.List;
import javax.swing.JOptionPane;

public class InventoryController {

    private Ghost_inventory view;
    private InventoryDAO dao;
    private List<Inventory> databaseItems;

    public InventoryController(Ghost_inventory view, InventoryDAO dao) {
        this.view = view;
        this.dao = dao;
        loadInventory();
    }

    // ─── Load ALL Products into View ──────────────────────────
    public void loadInventory() {
        databaseItems = dao.getAllInventory();
        
        javax.swing.JPanel panel = view.getInventoryPanel();
        panel.removeAll();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        view.clearDynamicCheckBoxes();

        for (int i = 0; i < databaseItems.size(); i++) {
            panel.add(buildRow(databaseItems.get(i), i));
        }

        // Bottom message
        panel.add(javax.swing.Box.createVerticalStrut(15));
        javax.swing.JLabel bottomMsg = new javax.swing.JLabel(
            "  Check 'Hidden' to remove product from customer catalog.");
        bottomMsg.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 12));
        bottomMsg.setForeground(java.awt.Color.GRAY);
        panel.add(bottomMsg);

        view.refreshInventory();
    }

  private javax.swing.JPanel buildRow(Inventory item, int index) {
    javax.swing.JPanel row = new javax.swing.JPanel(null);
    row.setPreferredSize(new java.awt.Dimension(1200, 45));
    row.setMaximumSize(new java.awt.Dimension(1200, 45));
    row.setMinimumSize(new java.awt.Dimension(1200, 45));
    row.setBackground(index % 2 == 0 
        ? java.awt.Color.WHITE 
        : new java.awt.Color(232, 255, 233));

    // Product name — same position as jLabel32 in your UI
    javax.swing.JLabel nameLabel = new javax.swing.JLabel(item.getName());
    nameLabel.setBounds(6, 10, 260, 25);
    nameLabel.setFont(new java.awt.Font("Arial", 0, 14));

    // Stock — same position as jLabel33 in your UI
    javax.swing.JLabel stockLabel = new javax.swing.JLabel(
        String.valueOf(item.getStock()));
    stockLabel.setBounds(360, 10, 65, 25);
    stockLabel.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14));

    // Checkbox — same position as jCheckBox3 in your UI
    javax.swing.JCheckBox checkbox = new javax.swing.JCheckBox();
    checkbox.setBounds(760, 10, 25, 25);
    checkbox.setSelected(item.isHidden());
    checkbox.setBackground(row.getBackground());
    view.addDynamicCheckBox(checkbox);

    row.add(nameLabel);
    row.add(stockLabel);
    row.add(checkbox);

    return row;

    }

    // ─── Update Inventory (called by View's button) ───────────
    public void updateInventory() {
        List<javax.swing.JCheckBox> checkBoxes = view.getDynamicCheckBoxes();

        for (int i = 0; i < databaseItems.size() && i < checkBoxes.size(); i++) {
            Inventory item = databaseItems.get(i);
            boolean isHidden = checkBoxes.get(i).isSelected();
            dao.updateGhostStatus(item.getId(), isHidden);
        }

        JOptionPane.showMessageDialog(view, 
            "Inventory updated successfully!",
            "Success",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadInventory();
    }
}