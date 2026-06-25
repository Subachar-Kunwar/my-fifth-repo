package model;

public class Inventory {
    private int id;
    private String name;
    private int stock;
    private boolean hidden;

    public Inventory(int id, String name, int stock, boolean hidden) {
        this.id = id;
        this.name = name;
        this.stock = stock;
        this.hidden = hidden;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }
}