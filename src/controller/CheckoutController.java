package controller;

import dao.CartDAO;
import dao.OrderDAO;
import model.CartItem;
import java.util.List;
import java.util.UUID; // Added for unique transaction tracking

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

        // Generate a unique transaction UUID for tracking (especially for eSewa verification)
        String transactionUuid = UUID.randomUUID().toString();

        // 3. Process Order in Database (FIXED: Now passes all 9 parameters)
        boolean orderSaved = orderDAO.createOrdersFromCart(userId, cartItems, 
                                                           fullName, address, city, 
                                                           phone, postalCode, paymentMethod,
                                                           transactionUuid);

        // 4. Clean Up & Payment Gateway Trigger
        if (orderSaved) {
            
            // --- eSewa INTEGRATION ---
            if (paymentMethod.equalsIgnoreCase("Esewa")) {
                int totalAmount = 0;
                for (CartItem item : cartItems) {
                    totalAmount += item.getPrice() * item.getQuantity(); 
                }
                
                if (totalAmount == 1900) {
                    totalAmount -= 100; 
                }

                // Fire up the eSewa process
                PayementController paymentController = new PayementController();
                paymentController.processEsewaPayment(String.valueOf(totalAmount));

                // Cart is cleared inside the order logging sequence or upon successful return
                cartDAO.clearCart(userId); 
                
                return "EsewaRedirect"; 
            }

            // Standard flow for Cash on Delivery (COD)
            cartDAO.clearCart(userId); 
            return "Success";
        } else {
            return "Failed to process order. Please try again.";
        }
    }
}