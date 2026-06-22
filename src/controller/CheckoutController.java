package controller;

import dao.CartDAO;
import dao.OrderDAO;
import model.CartItem;
import java.util.List;

public class CheckoutController {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public String processCheckout(int userId, String fullName, String address, 
                                  String city, String phone, String postalCode, 
                                  String paymentMethod) {
        
        // 1. Basic Validation
        if (fullName.isEmpty() || address.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            return "Please fill in all required shipping details.";
        }

        // 2. Fetch User's Cart
        List<CartItem> cartItems = cartDAO.getCartItems(userId);
        if (cartItems == null || cartItems.isEmpty()) {
            return "Your cart is empty! Add products before checking out.";
        }

        // 3. Process Order in Database
        boolean orderSaved = orderDAO.createOrdersFromCart(userId, cartItems, 
                                                           fullName, address, city, 
                                                           phone, postalCode, paymentMethod);

        // 4. Clean Up
        if (orderSaved) {
            cartDAO.clearCart(userId); // Empty cart after successful purchase
            return "Success";
        } else {
            return "Failed to process order. Please try again.";
        }
    }
}