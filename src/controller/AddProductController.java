package controller;

import dao.ProductcatalogDAO;

public class AddProductController {

    private final ProductcatalogDAO productDAO = new ProductcatalogDAO();

    /**
     * Validates and saves a new product.
     * Returns null on success, or an error message string on failure.
     */
    public String addProduct(String name, String category, String priceText,
                             String description, String stockText,
                             String selectedImagePath) {

        // Validation
        if (name == null || name.isEmpty())        return "Product name is required!";
        if (priceText == null || priceText.isEmpty()) return "Price is required!";
        if (category == null || category.isEmpty()) return "Category is required!";
        if (description == null || description.isEmpty()) return "Description is required!";
        if (stockText == null || stockText.isEmpty()) return "Stock quantity is required!";
        if (selectedImagePath == null || selectedImagePath.isEmpty())
            return "Please add a product image!";

        double price;
        int stock;

        try {
            price = Double.parseDouble(priceText);
            if (price <= 0) return "Price must be greater than 0!";
        } catch (NumberFormatException ex) {
            return "Price must be a valid number!";
        }

        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) return "Stock cannot be negative!";
        } catch (NumberFormatException ex) {
            return "Stock must be a valid whole number!";
        }

        // Copy image
        String imageName = new java.io.File(selectedImagePath).getName();
        String destPath = "src/images/" + imageName;
        try {
            java.io.File dir = new java.io.File("src/images");
            if (!dir.exists()) dir.mkdirs();
            java.nio.file.Files.copy(
                java.nio.file.Paths.get(selectedImagePath),
                java.nio.file.Paths.get(destPath),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (java.io.IOException ex) {
            System.out.println("Image copy error: " + ex.getMessage());
        }

        boolean success = productDAO.addProduct(
            name, category, price,
            "images/" + imageName, description, stock, 1);

        return success ? null : "Failed to add product. Try again.";
    }
}