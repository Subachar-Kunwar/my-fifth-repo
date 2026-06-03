package controller;

import dao.ProductcatalogDAO;
import model.Product;
import java.util.List;

public class ProductController {

    private final ProductcatalogDAO dao = new ProductcatalogDAO();

    public List<Product> getFilteredProducts(String category,
            double minPrice, double maxPrice, String searchTerm) {

        if (searchTerm != null && !searchTerm.isEmpty()) {
            return dao.searchByName(searchTerm);
        }

        boolean hasCategory = category != null && !category.isEmpty();
        boolean hasPrice = minPrice > 0 || maxPrice < Double.MAX_VALUE;

        if (hasCategory && hasPrice) {
            return dao.getByCategoryAndPrice(category, minPrice, maxPrice);
        } else if (hasCategory) {
            return dao.getByCategory(category);
        } else if (hasPrice) {
            return dao.getByPriceRange(minPrice, maxPrice);
        } else {
            return dao.getAllProducts();
        }
    }
}