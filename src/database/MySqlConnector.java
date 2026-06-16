package Database;

import java.sql.*;

public class MySqlConnector {

    // ✅ Use EXACT same db name everywhere
    private static final String URL = "jdbc:mysql://localhost:3306/group7_rewear"
                                    + "?useSSL=false"
                                    + "&allowPublicKeyRetrieval=true"
                                    + "&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "subachar@0310";

    public Connection openConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("✅ Connection opened");
            return conn;
        } catch (ClassNotFoundException e) {
            System.out.println("❌ Driver not found: " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            return null;
        }
    }

    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("✅ Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("❌ Close error: " + e.getMessage());
        }
    }

    public ResultSet runQuery(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("❌ Query error: " + e.getMessage());
        }
        return null;
    }

    public int executeUpdate(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("❌ Update error: " + e.getMessage());
        }
        return -1;
    }
}