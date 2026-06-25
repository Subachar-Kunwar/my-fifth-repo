package controller;

import dao.CartDAO;
import dao.OrderDAO;
import model.CartItem;
import java.util.List;
import java.util.UUID;

public class CheckoutController {

    private final CartDAO cartDAO   = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    // ✅ Coupon state
    private boolean couponApplied = false;
    private static final int COUPON_DISCOUNT = 50;
    private static final String VALID_COUPON = "REWEAR";

    // ─── Apply Coupon ─────────────────────────────────────────
    public String applyCoupon(String code) {
        if (code == null || code.trim().isEmpty()) {
            couponApplied = false;
            return "Please enter a coupon code!";
        }

        if (code.trim().equalsIgnoreCase(VALID_COUPON)) {
            couponApplied = true;
            return null; // ✅ success
        }

        couponApplied = false;
        return "Invalid coupon code!";
    }

    public boolean isCouponApplied() {
        return couponApplied;
    }

    public void resetCoupon() {
        couponApplied = false;
    }

    // ─── Validate Shipping Details ────────────────────────────
    public String validateShipping(String fullName, String address, 
                               String city, String phone, String postalCode) {
    if (fullName == null || fullName.isEmpty())   
        return "Please enter your full name!";
    
    // ✅ Email validation
    if (address == null || address.isEmpty())     
        return "Please enter your email address!";
    if (!address.contains("@gmail.com"))          
        return "Please enter a valid email address!\nExample: user@gmail.com";
    
    if (city == null || city.isEmpty())           
        return "Please enter your city!";
    
    // ✅ Phone number validation (exactly 10 digits)
    if (phone == null || phone.isEmpty())         
        return "Please enter your phone number!";
    if (!phone.matches("\\d{10}"))                
        return "Phone number must be exactly 10 digits!\nExample: 9800000000";
    
    if (postalCode == null || postalCode.isEmpty()) 
        return "Please enter your postal code!";
    
    return null;
}

    public String validatePayment(String paymentMethod) {
        if (paymentMethod == null || paymentMethod.isEmpty()) {
            return "Please select a payment method!";
        }
        return null;
    }

    public List<CartItem> getCartItems(int userId) {
        return cartDAO.getCartItems(userId);
    }

    public double getSubTotal(List<CartItem> items) {
        double total = 0;
        for (CartItem item : items) total += item.getTotal();
        return total;
    }

    // ✅ Only coupon discount now (no auto 5%)
    public double getDiscount(double subTotal) {
        return couponApplied ? COUPON_DISCOUNT : 0;
    }

    public double getFinalTotal(List<CartItem> items) {
        double subTotal = getSubTotal(items);
        double discount = getDiscount(subTotal);
        double total = subTotal - discount;
        return total < 0 ? 0 : total;
    }

    // ─── Process Checkout ─────────────────────────────────────
    public CheckoutResult processCheckout(int userId,
                                          String fullName, String address, String city,
                                          String phone, String postalCode,
                                          String paymentMethod) {

        String shippingError = validateShipping(fullName, address, city, phone, postalCode);
        if (shippingError != null) {
            return new CheckoutResult(false, shippingError, -1, 0, null);
        }

        String paymentError = validatePayment(paymentMethod);
        if (paymentError != null) {
            return new CheckoutResult(false, paymentError, -1, 0, null);
        }

        List<CartItem> items = cartDAO.getCartItems(userId);
        if (items == null || items.isEmpty()) {
            return new CheckoutResult(false, "Your cart is empty!", -1, 0, null);
        }

        double subTotal = getSubTotal(items);
        double discount = getDiscount(subTotal);
        int totalAmount = (int) (subTotal - discount);
        if (totalAmount < 0) totalAmount = 0;

        // Distribute discount proportionally across items
        double discountRate = subTotal > 0 ? discount / subTotal : 0;

        // ✅ Generate UUID for online payments (Esewa or FonePay)
        boolean isOnlinePayment = paymentMethod.equals("Esewa") 
                               || paymentMethod.equals("FonePay");
        String transactionUuid = isOnlinePayment ? UUID.randomUUID().toString() : null;

        int lastOrderId = -1;
        boolean allSuccess = true;

        for (CartItem item : items) {
            double itemTotal = item.getPrice() * item.getQuantity();
            double finalItemAmount = itemTotal * (1 - discountRate);

            int placedId = orderDAO.placeOrder(
                item.getProductId(), userId, finalItemAmount,
                fullName, address, city, phone, postalCode, paymentMethod,
                transactionUuid
            );

            if (placedId == -1) {
                allSuccess = false;
            } else {
                lastOrderId = placedId;
            }
        }

        if (!allSuccess) {
            return new CheckoutResult(false, "Failed to place order. Please try again.", -1, 0, null);
        }

        cartDAO.clearCart(userId);
        couponApplied = false; // reset coupon after use

        return new CheckoutResult(true, "Order placed successfully!", 
                                  lastOrderId, totalAmount, transactionUuid);
    }

    // ─── Process Online Payment (returns Transaction ID) ──────
    public String processOnlinePayment(int totalAmount, java.awt.Component parent) {
        PayementController paymentEngine = new PayementController();
        return paymentEngine.showQRCodePayment(String.valueOf(totalAmount), parent);
    }

    // ─── Save Transaction ID to Order ─────────────────────────
    public boolean saveTransactionId(int orderId, String transactionId) {
        return orderDAO.saveTransactionId(orderId, transactionId);
    }

    // ─── Result Helper Class ──────────────────────────────────
    public static class CheckoutResult {
        public final boolean success;
        public final String message;
        public final int orderId;
        public final int totalAmount;
        public final String transactionUuid;

        public CheckoutResult(boolean success, String message, int orderId, 
                              int totalAmount, String transactionUuid) {
            this.success         = success;
            this.message         = message;
            this.orderId         = orderId;
            this.totalAmount     = totalAmount;
            this.transactionUuid = transactionUuid;
        }
    }
}