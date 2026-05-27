package dao;

import Database.MySqlConnector;
import java.sql.*;
import model.logindata;

public class UserDAO {

    MySqlConnector mysql = new MySqlConnector();

    // CREATE USER - FIXED with role
    public boolean createUser(logindata user) {

        Connection conn = mysql.openConnection();

        // ✅ FIXED: Added 'role' column
        String sql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, user.getUserType());  // ← Added role/userType

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
    
    // Get user role by email (for login verification)
    public String getUserRole(String email) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT role FROM users WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }
    
    // ========== NEW METHODS FOR BUYER/SELLER DETAILS ==========
    
    // Get user ID by email
    public int getUserIdByEmail(String email) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT id FROM users WHERE email = ?";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Error getting user ID: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }
    
    // Insert into buyer_details
    public boolean addBuyerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO buyer_details (user_id) VALUES (?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error adding buyer details: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
    
    // Insert into seller_details
    public boolean addSellerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO seller_details (user_id) VALUES (?)";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.out.println("Error adding seller details: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
}