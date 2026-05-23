package dao;

import Database.MySqlConnector;
import java.sql.*;
import model.logindata;

public class UserDAO {

    MySqlConnector mysql = new MySqlConnector();

    // CREATE USER
    public boolean createUser(logindata user) {

        Connection conn = mysql.openConnection();

        String sql = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());

            int rowsInserted = pstm.executeUpdate();

            return rowsInserted > 0;

        } catch (Exception e) {

            System.out.println(e);

        } finally {

            mysql.closeConnection(conn);

        }

        return false;
    }

    // CHECK USER EXISTS
    public boolean checkUser(logindata user) {

        Connection conn = mysql.openConnection();

        String sql = "SELECT * FROM users WHERE email = ? OR username = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getUsername());

            ResultSet result = pstmt.executeQuery();

            return result.next();

        } catch (SQLException ex) {

            System.out.println(ex);

        } finally {

            mysql.closeConnection(conn);

        }

        return false;
    }

    // UPDATE PASSWORD
    public boolean updatePassword(String email, String newPassword) {

        Connection conn = mysql.openConnection();

        String sql = "UPDATE users SET password = ? WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);

            int rowsUpdated = pstmt.executeUpdate();

            return rowsUpdated > 0;

        } catch (SQLException ex) {

            System.out.println(ex);

        } finally {

            mysql.closeConnection(conn);

        }

        return false;
    }
}
