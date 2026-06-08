package controller;

import dao.ProductcatalogDAO;
import model.Product;
import java.util.List;

public class EditProductController {

    private final ProductcatalogDAO productDAO = new ProductcatalogDAO();

    public List<Product> getAllProducts() {
        return productDAO.getAllProducts();
    }

    public Product getProductById(int id) {
        return productDAO.getProductById(id);
    }

    /**
     * Validates and updates a product.
     * Returns null on success, or an error message string on failure.
     */
    public String updateProduct(int productId, String name, String category,
                                String priceText, String description,
                                String stockText, String selectedImagePath) {

        if (name == null || name.isEmpty())           return "Product name is required!";
        if (priceText == null || priceText.isEmpty()) return "Price is required!";
        if (category == null || category.isEmpty())   return "Category is required!";
        if (description == null || description.isEmpty()) return "Description is required!";
        if (stockText == null || stockText.isEmpty()) return "Stock quantity is required!";

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

        String imagePath = "";
        if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
            String imageName = new java.io.File(selectedImagePath).getName();
            String destPath = "src/images/" + imageName;
            try {
                java.io.File dir = new java.io.File("src/images");
                if (!dir.exists()) dir.mkdirs();
                java.nio.file.Files.copy(
                    java.nio.file.Paths.get(selectedImagePath),
                    java.nio.file.Paths.get(destPath),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                imagePath = "images/" + imageName;
            } catch (java.io.IOException ex) {
                System.out.println("Image copy error: " + ex.getMessage());
            }
        }

        boolean success = productDAO.updateProduct(
            productId, name, category, price,
            imagePath, description, stock);

        return success ? null : "Failed to update product.";
    }

    /**
     * Deletes a product by ID.
     * Returns null on success, or an error message on failure.
     */
    public String deleteProduct(int productId) {
        boolean success = productDAO.deleteProduct(productId);
        return success ? null : "Failed to delete product. Product ID may not exist.";
    }
}