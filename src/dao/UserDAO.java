package dao;
import Database.MySqlConnector;
import java.sql.*;
import model.logindata;

public class UserDAO {
    MySqlConnector mysql = new MySqlConnector();

    public boolean createUser(logindata user) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO users(username, email, password, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, user.getUserType());
            return pstm.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

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

    public boolean updatePassword(String email, String newPassword) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, email);
            boolean updated = pstmt.executeUpdate() > 0;
            if (updated) {
                int userId = getUserIdByEmail(email);
                if (userId != -1) {
                    new ActivityDAO().logActivity(userId, "Password Changed");
                }
            }
            return updated;
        } catch (SQLException ex) {
            System.out.println(ex);
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

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

    public String getEmailById(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT email FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            System.out.println("Error getting email: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return "user@gmail.com";
    }

    public String getUsernameById(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            System.out.println("Error getting username: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return "User";
    }

    public boolean addBuyerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO buyer_details (user_id) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding buyer details: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }

    public boolean addSellerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO seller_details (user_id) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding seller details: " + e.getMessage());
            return false;
        } finally {
            mysql.closeConnection(conn);
        }
    }
}