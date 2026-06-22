package model;

public class CartItem {
    private int id;           // cart_items.id
    private int cartId;
    private int productId;
    private String productName;
    private double price;
    private int quantity;
    private String imagePath;

    public CartItem(int id, int cartId, int productId,
                    String productName, double price,
                    int quantity, String imagePath) {
        this.id = id;
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    // Getters
    public int getId() { return id; }
    public int getCartId() { return cartId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getImagePath() { return imagePath; }
    public double getTotal() { return price * quantity; }

    // Setters
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
