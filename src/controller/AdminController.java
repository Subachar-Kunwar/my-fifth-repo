package controller;

import dao.AdminDAO;
import java.util.List;

public class AdminController {

    private final AdminDAO adminDAO = new AdminDAO();

    public double getTotalSales() {
        return adminDAO.getTotalSales();
    }

    public int getTotalOrders() {
        return adminDAO.getTotalOrders();
    }

    public int getTotalUsers() {
        return adminDAO.getTotalUsers();
    }

    public int getTotalProducts() {
        return adminDAO.getTotalProducts();
    }

    public List<String[]> getRecentOrders() {
        return adminDAO.getRecentOrders();
    }
}