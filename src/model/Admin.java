package model;

public class Admin extends User {

    public Admin(int id, String username, String email) {
        super(id, username, email, "admin");
    }

    // ─── Polymorphic Methods ──────────────────────────────────
    @Override
    public String getWelcomeMessage() {
        return "Welcome back, " + getUsername() + "!\n🔧 System Administrator";
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