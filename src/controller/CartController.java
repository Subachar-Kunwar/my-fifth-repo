package controller;

import dao.CartDAO;
import model.CartItem;
import java.util.List;

public class CartController {
    private CartDAO cartDAO = new CartDAO();

    public boolean addToCart(int userId, int productId) {
        return cartDAO.addToCart(userId, productId);
    }

    public List<CartItem> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    public boolean updateQuantity(int cartItemId, int newQuantity) {
        return cartDAO.updateQuantity(cartItemId, newQuantity);
    }

    public boolean removeItem(int cartItemId) {
        return cartDAO.removeItem(cartItemId);
    }

    public boolean clearCart(int userId) {
        return cartDAO.clearCart(userId);
    }

    public double getSubTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) {
            total += item.getTotal();
        }
        return total;
    }

    public double getDiscount(double subTotal) {
        // 5% discount for orders above Rs. 2000
        if (subTotal >= 2000) return subTotal * 0.05;
        return 0;
    }

    public double getTotal(List<CartItem> items) {
        double subTotal = getSubTotal(items);
        return subTotal - getDiscount(subTotal);
    }

    public int getCartCount(int userId) {
        return cartDAO.getCartCount(userId);
    }
}
