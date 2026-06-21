package model;

public class Product {

    private int id;
    private String name;
    private String category;
    private double price;
    private String imagePath;
    private int sellerId;
    private String description;
    private int stock;

    // ─── Constructor 1 - Basic ────────────────────────────────
    public Product(int id, String name, String category,
                   double price, String imagePath, int sellerId) {
        this.id          = id;
        this.name        = name;
        this.category    = category;
        this.price       = price;
        this.imagePath   = imagePath;
        this.sellerId    = sellerId;
        this.description = "";
        this.stock       = 0;
    }

    // ─── Constructor 2 - Full with ID ─────────────────────────
    public Product(int id, String name, String category,
                   double price, String imagePath, int sellerId,
                   String description, int stock) {
        this.id          = id;
        this.name        = name;
        this.category    = category;
        this.price       = price;
        this.imagePath   = imagePath;
        this.sellerId    = sellerId;
        this.description = description;
        this.stock       = stock;
    }

    // ─── Constructor 3 - No ID (for new products) ─────────────
    public Product(String name, String category,
                   double price, String imagePath,
                   String description, int stock) {
        this.name        = name;
        this.category    = category;
        this.price       = price;
        this.imagePath   = imagePath;
        this.description = description;
        this.stock       = stock;
    }

    // ─── Getters ──────────────────────────────────────────────
    public int getId()            { return id; }
    public String getName()       { return name; }
    public String getCategory()   { return category; }
    public double getPrice()      { return price; }
    public String getImagePath()  { return imagePath; }
    public int getSellerId()      { return sellerId; }
    public String getDescription(){ return description; }
    public int getStock()         { return stock; }

    // ─── Setters ──────────────────────────────────────────────
    public void setId(int id)                  { this.id = id; }
    public void setName(String name)           { this.name = name; }
    public void setCategory(String category)   { this.category = category; }
    public void setPrice(double price)         { this.price = price; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setSellerId(int sellerId)      { this.sellerId = sellerId; }
    public void setDescription(String desc)    { this.description = desc; }
    public void setStock(int stock)            { this.stock = stock; }
}