package controller;
import dao.ProductcatalogDAO;
import dao.OrderDAO;
import model.Product;
import java.util.List;

public class ProductController {
    private final ProductcatalogDAO dao = new ProductcatalogDAO();
    private final OrderDAO orderDAO = new OrderDAO();

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

    public List<Product> sortProducts(List<Product> products, String sortOption) {
        if (products == null || sortOption == null) return products;
        if (sortOption.equals("Price: Low to High")) {
            products.sort((a, b) -> Double.compare(a.getPrice(), b.getPrice()));
        } else if (sortOption.equals("Price: High to Low")) {
            products.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
        }
        return products;
    }

    public int placeOrder(int productId, int userId, double price) {
        return orderDAO.placeOrder(productId, userId, price);
    }
}