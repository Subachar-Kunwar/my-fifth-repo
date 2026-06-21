package controller;

import dao.OrderDAO;

public class OrderController {

    private final OrderDAO orderDAO = new OrderDAO();

    // ─── Place Order ──────────────────────────────────────────
    // Returns orderId on success, -1 on failure
    public int placeOrder(int productId, int userId, double price) {
        if (productId <= 0) return -1;
        if (userId <= 0)    return -1;
        if (price <= 0)     return -1;

        return orderDAO.placeOrder(productId, userId, price);
    }

    // ─── Get Order By ID ──────────────────────────────────────
    public String[] getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }
}