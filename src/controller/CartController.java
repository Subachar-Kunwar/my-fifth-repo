package controller;

import dao.CartDAO;
import model.CartItem;
import java.util.List;

public class CartController {
    private CartDAO cartDAO = new CartDAO();

    // ─── Add to Cart WITH stock check + auto-decrease ─────────
    // Returns null on success, error message on failure
    public String addToCartWithStock(int userId, int productId) {
        int stock = cartDAO.getProductStock(productId);
        if (stock <= 0) {
            return "Sorry, this product is out of stock!";
        }
        
        boolean added = cartDAO.addToCart(userId, productId);
        if (!added) {
            return "Failed to add to cart. Please try again.";
        }
        
        // ✅ Decrease stock by 1
        cartDAO.decreaseStock(productId, 1);
        return null; // success
    }

    // ─── Old method (kept for backward compatibility) ─────────
    public boolean addToCart(int userId, int productId) {
        return cartDAO.addToCart(userId, productId);
    }

    public List<CartItem> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    public boolean updateQuantity(int cartItemId, int newQuantity) {
        return cartDAO.updateQuantity(cartItemId, newQuantity);
    }

    // ─── Decrease Quantity (Restore Stock) ────────────────────
    public boolean decreaseQuantity(int cartItemId, int productId, int newQuantity) {
        boolean updated = cartDAO.updateQuantity(cartItemId, newQuantity);
        if (updated) {
            cartDAO.increaseStock(productId, 1);  // restore 1 to stock
        }
        return updated;
    }

    // ─── Remove Item (Restore ALL stock) ──────────────────────
    public boolean removeItemAndRestoreStock(int cartItemId) {
        int productId = cartDAO.getProductIdByCartItem(cartItemId);
        int qty = cartDAO.getQuantityByCartItem(cartItemId);
        
        boolean removed = cartDAO.removeItem(cartItemId);
        if (removed && productId != -1 && qty > 0) {
            cartDAO.increaseStock(productId, qty);  // restore all
        }
        return removed;
    }

    public boolean removeItem(int cartItemId) {
        return cartDAO.removeItem(cartItemId);
    }

    // ─── Clear Cart + Restore All Stock ───────────────────────
    public boolean clearCartAndRestoreStock(int userId) {
        List<CartItem> items = cartDAO.getCartItems(userId);
        for (CartItem item : items) {
            cartDAO.increaseStock(item.getProductId(), item.getQuantity());
        }
        return cartDAO.clearCart(userId);
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

    public int getProductStock(int productId) {
        return cartDAO.getProductStock(productId);
    }

    // ─── Try to Increase Quantity (decrease stock) ────────────
    public String tryIncreaseQuantity(int cartItemId, int productId, int currentQty) {
        int stock = cartDAO.getProductStock(productId);

        if (stock <= 0) {
            return "Sorry, no more stock available!";
        }

        int newQty = currentQty + 1;
        boolean updated = cartDAO.updateQuantity(cartItemId, newQty);
        if (!updated) {
            return "Failed to update quantity. Please try again.";
        }

        // ✅ Decrease stock by 1
        cartDAO.decreaseStock(productId, 1);
        return null;
    }
}