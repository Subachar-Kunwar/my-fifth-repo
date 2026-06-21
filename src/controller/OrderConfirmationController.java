package controller;

public class OrderConfirmationController {

    private final int orderId;
    private final String username;
    private final int userId;

    public OrderConfirmationController(int orderId, 
            String username, int userId) {
        this.orderId  = orderId;
        this.username = username;
        this.userId   = userId;
    }

    // ─── Get Order ID Text ────────────────────────────────────
    public String getOrderIdText() {
        return "Order ID : #" + orderId;
    }

    // ─── Get Username ─────────────────────────────────────────
    public String getUsername() {
        return username;
    }

    // ─── Get User ID ──────────────────────────────────────────
    public int getUserId() {
        return userId;
    }

    // ─── Get Order ID ─────────────────────────────────────────
    public int getOrderId() {
        return orderId;
    }

    // ─── Log Activity ─────────────────────────────────────────
    public void logOrderConfirmed() {
        try {
            new dao.ActivityDAO().logActivity(
                userId, "Order Confirmed: #" + orderId);
        } catch (Exception e) {
            System.out.println("Log error: " + e.getMessage());
        }
    }
}