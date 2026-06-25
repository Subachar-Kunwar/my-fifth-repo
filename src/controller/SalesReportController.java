package controller;

import model.SalesReport;
import Database.MySqlConnector;
import java.sql.*;

public class SalesReportController {
    private MySqlConnector db = new MySqlConnector();

// ─── Original (no date filter) ────────────────────────────
public SalesReport getSalesReport() {
    return getSalesReport(null, null);
}

// ─── Filtered by Date Range ───────────────────────────────
public SalesReport getSalesReport(java.sql.Date fromDate, java.sql.Date toDate) {
    SalesReport report = new SalesReport();
    Connection conn = db.openConnection();

    if (conn == null) {
        System.out.println("Could not connect to database");
        return report;
    }

    // Build date filter
    String dateFilter = "";
    if (fromDate != null && toDate != null) {
        dateFilter = " AND o.order_date BETWEEN '" + fromDate + "' AND '" + toDate + "'";
    }

    try {
        // 1. Total Sales (actual revenue: Paid + Shipped + Delivered)
        ResultSet rs1 = db.runQuery(conn,
            "SELECT COALESCE(SUM(o.total_amount), 0) AS total FROM orders o " +
            "WHERE o.status IN ('Paid', 'Shipped', 'Delivered')" + dateFilter);
        if (rs1 != null && rs1.next()) {
            report.setTotalSales(rs1.getDouble("total"));
        }

        // 2. Total Orders
        ResultSet rs2 = db.runQuery(conn,
            "SELECT COUNT(*) AS total FROM orders o WHERE 1=1" + dateFilter);
        if (rs2 != null && rs2.next()) {
            report.setTotalOrders(rs2.getInt("total"));
        }

        // 3. Average Order Value (only actual revenue)
        ResultSet rs3 = db.runQuery(conn,
            "SELECT COALESCE(AVG(o.total_amount), 0) AS avg FROM orders o " +
            "WHERE o.status IN ('Paid', 'Shipped', 'Delivered')" + dateFilter);
        if (rs3 != null && rs3.next()) {
            report.setAverageOrderValue(rs3.getDouble("avg"));
        }

        // 4. Pending Orders
        ResultSet rs4 = db.runQuery(conn,
            "SELECT COUNT(*) AS total FROM orders o " +
            "WHERE o.status IN ('Pending', 'Pending Verification')" + dateFilter);
        if (rs4 != null && rs4.next()) {
            report.setPendingOrders(rs4.getInt("total"));
        }

        // 5. Delivered Orders
        ResultSet rs5 = db.runQuery(conn,
            "SELECT COUNT(*) AS total FROM orders o " +
            "WHERE o.status = 'Delivered'" + dateFilter);
        if (rs5 != null && rs5.next()) {
            report.setDeliveredOrders(rs5.getInt("total"));
        }

        // 6. Cancelled Orders
        ResultSet rs6 = db.runQuery(conn,
            "SELECT COUNT(*) AS total FROM orders o " +
            "WHERE o.status = 'Cancelled'" + dateFilter);
        if (rs6 != null && rs6.next()) {
            report.setCancelledOrders(rs6.getInt("total"));
        }

        // 7. Top Selling Product (only confirmed orders)
        ResultSet rs7 = db.runQuery(conn,
            "SELECT p.name, COUNT(o.id) AS order_count " +
            "FROM orders o JOIN products p ON o.product_id = p.id " +
            "WHERE o.status IN ('Paid', 'Shipped', 'Delivered')" + dateFilter +
            " GROUP BY p.id, p.name ORDER BY order_count DESC LIMIT 1");
        if (rs7 != null && rs7.next()) {
            report.setTopSellingProduct(rs7.getString("name"));
        } else {
            report.setTopSellingProduct("N/A");
        }

        // 8. Total Users (buyers)
        ResultSet rs8 = db.runQuery(conn,
            "SELECT COUNT(*) AS total FROM users WHERE role = 'buyer'");
        if (rs8 != null && rs8.next()) {
            report.setTotalUsers(rs8.getInt("total"));
        }

    } catch (SQLException e) {
        System.out.println("Error loading sales report: " + e.getMessage());
    } finally {
        db.closeConnection(conn);
    }

    return report;
}
}