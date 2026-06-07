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

    // Original constructor — keep for compatibility
    public Product(int id, String name, String category,
                   double price, String imagePath, int sellerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath;
        this.sellerId = sellerId;
        this.description = "";
        this.stock = 0;
    }

    // Full constructor with description and stock
    public Product(int id, String name, String category,
                   double price, String imagePath, int sellerId,
                   String description, int stock) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath;
        this.sellerId = sellerId;
        this.description = description;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public int getSellerId() { return sellerId; }
    public String getDescription() { return description; }
    public int getStock() { return stock; }

    public void setDescription(String description) { 
        this.description = description; 
    }
    public void setStock(int stock) { 
        this.stock = stock; 
    }
}