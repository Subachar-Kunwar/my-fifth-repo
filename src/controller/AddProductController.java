package controller;

import dao.ProductcatalogDAO;
import model.Product;

public class AddProductController {

    private final ProductcatalogDAO productDAO = new ProductcatalogDAO();

    // ─── Add Product ──────────────────────────────────────────
    public String addProduct(String name, String category,
                             String priceText, String imagePath,
                             String description, String stockText) {

        // Step 1: Validate empty fields
        if (name == null || name.trim().isEmpty()) {
            return "Product name is required!";
        }
        if (category == null || category.trim().isEmpty()) {
            return "Category is required!";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is required!";
        }
        if (imagePath == null || imagePath.trim().isEmpty()) {
            return "Please add a product image!";
        }
        if (priceText == null || priceText.trim().isEmpty()) {
            return "Price is required!";
        }
        if (stockText == null || stockText.trim().isEmpty()) {
            return "Stock is required!";
        }

        // Step 2: Parse numbers
        double price;
        int stock;

        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            return "Price must be a valid number!";
        }

        try {
            stock = Integer.parseInt(stockText);
        } catch (NumberFormatException e) {
            return "Stock must be a valid whole number!";
        }

        // Step 3: Validate number values
        if (price <= 0) {
            return "Price must be greater than 0!";
        }
        if (stock < 0) {
            return "Stock cannot be negative!";
        }

        // Step 4: Create Model object
        Product product = new Product(
                name, category, price,
                imagePath, description, stock
        );

        // Step 5: Copy image to project folder
        String imageName = new java.io.File(product.getImagePath()).getName();
        String destPath  = "src/images/" + imageName;

        try {
            java.io.File dir = new java.io.File("src/images");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            java.nio.file.Files.copy(
                    java.nio.file.Paths.get(product.getImagePath()),
                    java.nio.file.Paths.get(destPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

        } catch (java.io.IOException ex) {
            return "Failed to copy image: " + ex.getMessage();
        }

        // Step 6: Save to database via DAO
        boolean success = productDAO.addProduct(
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                "images/" + imageName,
                product.getDescription(),
                product.getStock(),
                1
        );

        return success ? null : "Failed to add product. Try again.";
    }

    // ─── Clear Form Fields ────────────────────────────────────
    public void clearFields(javax.swing.JTextField name,
                            javax.swing.JTextField description,
                            javax.swing.JTextField price,
                            javax.swing.JTextField category,
                            javax.swing.JTextField stock,
                            javax.swing.JLabel imageLabel) {
        name.setText("");
        description.setText("");
        price.setText("");
        category.setText("");
        stock.setText("");
        imageLabel.setIcon(null);
        imageLabel.setText("");
    }
}