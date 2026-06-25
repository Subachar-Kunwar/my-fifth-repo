package model;

public class SalesReport {
private double totalSales;
private int totalOrders;
private double averageOrderValue;
private int pendingOrders;
private int deliveredOrders;
private int cancelledOrders;
private String topSellingProduct;
private int totalUsers;

// Getters and Setters
public double getTotalSales() { return totalSales; }
public void setTotalSales(double totalSales) { this.totalSales = totalSales; }

public int getTotalOrders() { return totalOrders; }
public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

public double getAverageOrderValue() { return averageOrderValue; }
public void setAverageOrderValue(double averageOrderValue) { this.averageOrderValue = averageOrderValue; }

public int getPendingOrders() { return pendingOrders; }
public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

public int getDeliveredOrders() { return deliveredOrders; }
public void setDeliveredOrders(int deliveredOrders) { this.deliveredOrders = deliveredOrders; }

public int getCancelledOrders() { return cancelledOrders; }
public void setCancelledOrders(int cancelledOrders) { this.cancelledOrders = cancelledOrders; }

public String getTopSellingProduct() { return topSellingProduct; }
public void setTopSellingProduct(String topSellingProduct) { this.topSellingProduct = topSellingProduct; }

public int getTotalUsers() { return totalUsers; }
public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

}