package model;

public class Product {
    private int id;
    private String name;
    private String category;
    private double price;
    private String imagePath;
    private int sellerId;

    public Product(int id, String name, String category,
                   double price, String imagePath, int sellerId) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.imagePath = imagePath;
        this.sellerId = sellerId;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public String getImagePath() { return imagePath; }
    public int getSellerId() { return sellerId; }
}