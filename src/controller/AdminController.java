package controller;

import dao.AdminDAO;
import model.AdminRecentOrder;

import java.util.ArrayList;
import java.util.List;

public class AdminController {

    private final AdminDAO adminDAO = new AdminDAO();

    // ─── Stats ────────────────────────────────────────────────
    public String getTotalSalesText() {
        return "Rs " + String.format("%,.0f", adminDAO.getTotalSales());
    }

    public String getTotalOrdersText() {
        return String.valueOf(adminDAO.getTotalOrders());
    }

    public String getTotalUsersText() {
        return String.valueOf(adminDAO.getTotalUsers());
    }

    public String getTotalProductsText() {
        return String.valueOf(adminDAO.getTotalProducts());
    }

    // ─── Recent Orders ────────────────────────────────────────
    public List<AdminRecentOrder> getRecentOrders() {
        List<String[]> raw = adminDAO.getRecentOrders();
        List<AdminRecentOrder> orders = new ArrayList<>();

        for (String[] o : raw) {
            orders.add(new AdminRecentOrder(o[0], o[1], o[2], o[3]));
        }
        return orders;
    }
}