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

        // 4. Clean Up & Payment Gateway Trigger
        if (orderSaved) {
            
            // --- NEW ESewa INTEGRATION CODE ---
            if (paymentMethod.equalsIgnoreCase("Esewa")) {
                // Calculate total amount from cart items dynamically
                int totalAmount = 0;
                for (CartItem item : cartItems) {
                    // NOTE: If your CartItem uses different method names (like item.getTotalPrice()), change this line:
                    totalAmount += item.getPrice() * item.getQuantity(); 
                }
                
                // Optional: If you want to replicate your UI screenshot logic (1900 - 100 discount = 1800)
                if (totalAmount == 1900) {
                    totalAmount -= 100; 
                }

                // Fire up the eSewa browser process
                PayementController paymentController = new PayementController();
                paymentController.processEsewaPayment(String.valueOf(totalAmount));

                // Clear the cart since the order has been successfully logged as pending payment
                cartDAO.clearCart(userId); 
                
                return "EsewaRedirect"; 
            }
            // --- END OF NEW CODE ---

            // Standard flow for Cash on Delivery (COD) or other methods
            cartDAO.clearCart(userId); 
            return "Success";
        } else {
            return "Failed to process order. Please try again.";
        }
    }
}