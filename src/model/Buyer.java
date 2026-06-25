package model;

public class Buyer extends User {

    public Buyer(int id, String username, String email) {
        super(id, username, email, "buyer");
    }

    // ─── Polymorphic Methods ──────────────────────────────────
    @Override
    public String getWelcomeMessage() {
        return "Welcome back, " + getUsername() + "!\n🛍️ Happy thrifting!";
    }

    @Override
    public String getDashboardTitle() {
        return "Buyer Dashboard";
    }

    @Override
    public boolean canAddProducts() {
        return false;
    }

    @Override
    public boolean canViewAllOrders() {
        return false;
    }
}