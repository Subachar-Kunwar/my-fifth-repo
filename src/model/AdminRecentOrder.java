package model;

public class AdminRecentOrder {

    private String productName;
    private String orderDate;
    private String price;
    private String status;

    public AdminRecentOrder(String productName, String orderDate,
                            String price, String status) {
        this.productName = productName;
        this.orderDate   = orderDate;
        this.price       = price;
        this.status      = status;
    }

    public String getProductName() { return productName; }
    public String getOrderDate()   { return orderDate; }
    public String getPrice()       { return price; }
    public String getStatus()      { return status; }
}