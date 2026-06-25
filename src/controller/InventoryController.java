package controller;

import view.Ghost_inventory;
import dao.InventoryDAO;
import model.Inventory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JOptionPane;

public class InventoryController {
    private Ghost_inventory view;
    private InventoryDAO dao;
    private List<Inventory> databaseItems;

    public InventoryController(Ghost_inventory view, InventoryDAO dao) {
        this.view = view;
        this.dao = dao;

        // 1. Load data from the DB to populate checkboxes and labels on startup
        refreshUIFromDatabase();

        // 2. Add an event action listener to your Update Inventory button
        this.view.getBtnUpdateInventory().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChangesToDatabase();
            }
        });
    }

    private void refreshUIFromDatabase() {
        databaseItems = dao.getAllInventory();
        
        javax.swing.JCheckBox[] checkBoxes = view.getCheckBoxes();
        javax.swing.JLabel[] stockLabels = view.getStockLabels();

        // Read values from DB and push them to the interface
        for (int i = 0; i < databaseItems.size() && i < 5; i++) {
            Inventory item = databaseItems.get(i);
            stockLabels[i].setText(String.valueOf(item.getStock()));
            checkBoxes[i].setSelected(item.isHidden());
        }
    }

    private void saveChangesToDatabase() {
        javax.swing.JCheckBox[] checkBoxes = view.getCheckBoxes();

        // Read user inputs from the UI panel and update the database row-by-row
        for (int i = 0; i < databaseItems.size() && i < 5; i++) {
            Inventory item = databaseItems.get(i);
            boolean UISelection = checkBoxes[i].isSelected();
            
            dao.updateGhostStatus(item.getId(), UISelection);
        }

        JOptionPane.showMessageDialog(view, "Inventory statuses updated successfully!");
        refreshUIFromDatabase(); // Reset UI state to verify save action
    }
}
