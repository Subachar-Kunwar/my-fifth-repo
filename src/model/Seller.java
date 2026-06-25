package model;

public class Seller extends User {

    public Seller(int id, String username, String email) {
        super(id, username, email, "seller");
    }

    // ─── Polymorphic Methods ──────────────────────────────────
    @Override
    public String getWelcomeMessage() {
        return "Welcome back, " + getUsername() + "!\n💰 Happy selling!";
    }

    @Override
    public String getDashboardTitle() {
        return "Admin Dashboard";
    }

    @Override
    public boolean canAddProducts() {
        return true;
    }

    @Override
    public boolean canViewAllOrders() {
        return true;
    }
}