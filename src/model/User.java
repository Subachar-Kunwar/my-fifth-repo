package model;

public abstract class User {

    private int id;
    private String username;
    private String email;
    private String role;

    // ─── Constructor ──────────────────────────────────────────
    public User(int id, String username, String email, String role) {
        this.id       = id;
        this.username = username;
        this.email    = email;
        this.role     = role;
    }

    // ─── Abstract Methods (Polymorphism!) ─────────────────────
    public abstract String getWelcomeMessage();
    public abstract String getDashboardTitle();
    public abstract boolean canAddProducts();
    public abstract boolean canViewAllOrders();

    // ─── Getters ──────────────────────────────────────────────
    public int getId()         { return id; }
    public String getUsername() { return username; }
    public String getEmail()   { return email; }
    public String getRole()    { return role; }

    // ─── Common Method ────────────────────────────────────────
    @Override
    public String toString() {
        return role + ": " + username + " (" + email + ")";
    }
}