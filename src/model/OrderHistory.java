package model;

import java.sql.Date;

public class OrderHistory {
    private int id;
    private int userId;
    private int productId;
    private String productName;
    private String imagePath;
    private double totalAmount;
    private String status;
    private Date orderDate;

    public OrderHistory(int id, int userId, int productId, String productName,
                        String imagePath, double totalAmount, String status,
                        Date orderDate) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.imagePath = imagePath;
        this.totalAmount = totalAmount;
        this.status = status;
        this.orderDate = orderDate;
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getImagePath() { return imagePath; }
    public double getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public Date getOrderDate() { return orderDate; }
}