package controller;

import dao.ProductcatalogDAO;
import model.Product;
import java.util.List;

public class EditProductController {

    private final ProductcatalogDAO productDAO = new ProductcatalogDAO();

    // ─── Get All Products ─────────────────────────────────────
    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    // ─── Get Product By ID ────────────────────────────────────
    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    // ─── Get Product List as formatted String ─────────────────
    // Controller builds the display string - NOT the View
    public String getProductListText() {
        List<Product> products = productDAO.getAllProducts();

        if (products.isEmpty()) {
            return null; // View will show "no products" message
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID  | Name                  | Category    | Price\n");
        sb.append("─────────────────────────────────────────────\n");
        for (Product p : products) {
            sb.append(p.getId())
              .append("   | ").append(p.getName())
              .append(" | ").append(p.getCategory())
              .append(" | Rs ").append(p.getPrice())
              .append("\n");
        }
        return sb.toString();
    }

    // ─── Validate Product ID input ────────────────────────────
    // Controller parses and validates ID - NOT the View
    public int parseProductId(String input) {
        if (input == null || input.trim().isEmpty()) {
            return -1; // invalid
        }
        try {
            int id = Integer.parseInt(input.trim());
            return id > 0 ? id : -1;
        } catch (NumberFormatException ex) {
            return -1; // invalid
        }
    }

    // ─── Update Product ───────────────────────────────────────
    public String updateProduct(int productId, String name, String category,
                                String priceText, String description,
                                String stockText, String selectedImagePath) {

        // Validate empty fields
        if (name == null || name.trim().isEmpty()) {
            return "Product name is required!";
        }
        if (category == null || category.trim().isEmpty()) {
            return "Category is required!";
        }
        if (description == null || description.trim().isEmpty()) {
            return "Description is required!";
        }
        if (priceText == null || priceText.trim().isEmpty()) {
            return "Price is required!";
        }
        if (stockText == null || stockText.trim().isEmpty()) {
            return "Stock quantity is required!";
        }

        // Parse numbers
        double price;
        int stock;

        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException ex) {
            return "Price must be a valid number!";
        }

        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) return "Stock cannot be negative!";
        } catch (NumberFormatException ex) {
            return "Stock must be a valid whole number!";
        }

        // Handle image copy
        String imagePath = "";
        if (selectedImagePath != null && !selectedImagePath.trim().isEmpty()) {
            String imageName = new java.io.File(selectedImagePath).getName();
            String destPath  = "src/images/" + imageName;
            try {
                java.io.File dir = new java.io.File("src/images");
                if (!dir.exists()) dir.mkdirs();
                java.nio.file.Files.copy(
                    java.nio.file.Paths.get(selectedImagePath),
                    java.nio.file.Paths.get(destPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                imagePath = "images/" + imageName;
            } catch (java.io.IOException ex) {
                return "Failed to copy image: " + ex.getMessage();
            }
        }

        // Save to database
        boolean success = productDAO.updateProduct(
            productId, name, category, price,
            imagePath, description, stock);

        return success ? null : "Failed to update product.";
    }

    // ─── Delete Product ───────────────────────────────────────
    public String deleteProduct(int productId) {
        boolean success = productDAO.deleteProduct(productId);
        return success ? null : "Failed to delete product.";
    }
}