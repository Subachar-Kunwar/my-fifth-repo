package dao;

import Database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.CartItem;
import model.OrderHistory;

public class OrderDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    public boolean createOrdersFromCart(int userId, List<CartItem> cartItems,
                                        String fullName, String address, String city,
                                        String phoneNumber, String postalCode, String paymentMethod) {
        Connection conn = mysql.openConnection();
        if (conn == null) {
            return false;
        }

        boolean success = false;

        try {
            conn.setAutoCommit(false);

            String insertOrderSql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                    "full_name, address, city, phone_number, postal_code, payment_method) " +
                    "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?)";
            String updateStockSql = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";

            try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql);
                 PreparedStatement psStock = conn.prepareStatement(updateStockSql)) {

                for (CartItem item : cartItems) {
                    psOrder.setInt(1, item.getProductId());
                    psOrder.setInt(2, userId);
                    psOrder.setDouble(3, item.getTotal());
                    psOrder.setString(4, fullName);
                    psOrder.setString(5, address);
                    psOrder.setString(6, city);
                    psOrder.setString(7, phoneNumber);
                    psOrder.setString(8, postalCode);
                    psOrder.setString(9, paymentMethod);
                    psOrder.addBatch();

                    psStock.setInt(1, item.getQuantity());
                    psStock.setInt(2, item.getProductId());
                    psStock.setInt(3, item.getQuantity());
                    psStock.addBatch();
                }

                psOrder.executeBatch();
                int[] stockResults = psStock.executeBatch();

                for (int result : stockResults) {
                    if (result == 0) {
                        throw new SQLException("Insufficient stock for one or more items.");
                    }
                }
            }

            conn.commit();
            success = true;
            new ActivityDAO().logActivity(userId, "Placed an order for " + cartItems.size() + " item(s)");
        } catch (SQLException e) {
            System.out.println("Cart checkout database error: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Rollback error: " + ex.getMessage());
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Auto-commit reset error: " + ex.getMessage());
            }
            mysql.closeConnection(conn);
        }

        return success;
    }

    public int placeOrder(int productId, int userId, double totalAmount) {
        return placeOrder(productId, userId, totalAmount, null, null, null, null, null, null);
    }

    public int placeOrder(int productId, int userId, double totalAmount,
                          String fullName, String address, String city,
                          String phoneNumber, String postalCode, String paymentMethod) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO orders (product_id, user_id, order_date, total_amount, status, " +
                "full_name, address, city, phone_number, postal_code, payment_method) " +
                "VALUES (?, ?, CURDATE(), ?, 'Pending', ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setDouble(3, totalAmount);
            ps.setString(4, fullName);
            ps.setString(5, address);
            ps.setString(6, city);
            ps.setString(7, phoneNumber);
            ps.setString(8, postalCode);
            ps.setString(9, paymentMethod);

            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int orderId = keys.getInt(1);
                    new ActivityDAO().logActivity(userId, "Order Placed");
                    return orderId;
                }
            }
        } catch (SQLException e) {
            System.out.println("Order error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    public String[] getOrderById(int orderId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT o.id, p.name, o.order_date, o.total_amount, o.status " +
                "FROM orders o " +
                "JOIN products p ON o.product_id = p.id " +
                "WHERE o.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("name"),
                        rs.getString("order_date"),
                        "Rs " + (int) rs.getDouble("total_amount"),
                        rs.getString("status")
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching order: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    public List<String[]> getRecentOrdersByUser(int userId) {
        Connection conn = mysql.openConnection();
        List<String[]> orders = new ArrayList<>();
        String sql = "SELECT p.name, o.order_date, o.total_amount, o.status " +
                "FROM orders o " +
                "JOIN products p ON o.product_id = p.id " +
                "WHERE o.user_id = ? " +
                "ORDER BY o.order_date DESC LIMIT 4";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new String[]{
                        rs.getString("name"),
                        rs.getString("order_date"),
                        "Rs " + (int) rs.getDouble("total_amount"),
                        rs.getString("status")
                    });
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching recent orders: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    public List<OrderHistory> getOrdersByUserId(int userId) {
        Connection conn = mysql.openConnection();
        List<OrderHistory> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, o.product_id, p.name, p.image_path, " +
                "o.total_amount, o.status, o.order_date " +
                "FROM orders o " +
                "JOIN products p ON o.product_id = p.id " +
                "WHERE o.user_id = ? " +
                "ORDER BY o.order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    orders.add(new OrderHistory(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getString("image_path"),
                        rs.getDouble("total_amount"),
                        rs.getString("status"),
                        rs.getDate("order_date")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Get order history error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }

    public List<OrderHistory> getAllOrders() {
        Connection conn = mysql.openConnection();
        List<OrderHistory> orders = new ArrayList<>();
        String sql = "SELECT o.id, o.user_id, o.product_id, p.name, p.image_path, " +
                "o.total_amount, o.status, o.order_date " +
                "FROM orders o " +
                "JOIN products p ON o.product_id = p.id " +
                "ORDER BY o.order_date DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                orders.add(new OrderHistory(
                    rs.getInt("id"),
                    rs.getInt("user_id"),
                    rs.getInt("product_id"),
                    rs.getString("name"),
                    rs.getString("image_path"),
                    rs.getDouble("total_amount"),
                    rs.getString("status"),
                    rs.getDate("order_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get all orders error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return orders;
    }
}
