package Database;

import database.db;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnector implements db {

    private final String username = "root";
    private final String password = "1234";
    private final String database = "group7_Rewear";

    @Override
    public Connection openConnection() {

        try {

            // Load Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/" + database,
                    username,
                    password
            );

            System.out.println("Database connection success");

            return connection;

        } catch (Exception e) {

            System.out.println("Connection error: " + e.getMessage());

        }

        return null;
    }

    @Override
    public void closeConnection(Connection conn) {

        try {

            if (conn != null && !conn.isClosed()) {

                conn.close();

                System.out.println("Connection closed");

            }

        } catch (SQLException e) {

            System.out.println(e);

        }
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {

        try {

            Statement stmt = conn.createStatement();

            return stmt.executeQuery(query);

        } catch (SQLException e) {

            System.out.println(e);

        }

        return null;
    }

    @Override
    public int executeUpdate(Connection conn, String query) {

        try {

            Statement stmt = conn.createStatement();

            return stmt.executeUpdate(query);

        } catch (SQLException e) {

            System.out.println(e);

        }

        return -1;
    }
}