package dao;

import Database.MySqlConnector;
import java.sql.*;
import model.logindata;

public class UserDAO {

    private final MySqlConnector mysql = new MySqlConnector();

    // ─── Login ────────────────────────────────────────────────
    // Added this method - LoginController needs it
    public String[] login(String username, String email, String password) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT id, username, role FROM users " +
                     "WHERE username = ? AND email = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                if (rs.next()) {
                    return new String[]{
                        rs.getString("username"),
                        rs.getString("role"),
                        String.valueOf(rs.getInt("id"))
                    };
                }
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Create User ──────────────────────────────────────────
    public boolean createUser(logindata user) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO users(username, email, password, role) " +
                     "VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getUserType());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Create user error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    // ─── Check User Exists ────────────────────────────────────
    public boolean checkUser(logindata user) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM users WHERE email = ? OR username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUsername());
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Check user error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    // ─── Update Password ──────────────────────────────────────
    public boolean updatePassword(String email, String newPassword) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPassword);
            ps.setString(2, email);
            boolean updated = ps.executeUpdate() > 0;
            if (updated) {
                int userId = getUserIdByEmail(email);
                if (userId != -1) {
                    new ActivityDAO().logActivity(userId, "Password Changed");
                }
            }
            return updated;
        } catch (SQLException e) {
            System.out.println("Update password error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    // ─── Get User Role ────────────────────────────────────────
    public String getUserRole(String email) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT role FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            System.out.println("Get role error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return null;
    }

    // ─── Get User ID By Email ─────────────────────────────────
    public int getUserIdByEmail(String email) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT id FROM users WHERE email = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.out.println("Get user ID error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return -1;
    }

    // ─── Get Email By ID ──────────────────────────────────────
    public String getEmailById(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT email FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.out.println("Get email error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return "user@gmail.com";
    }

    // ─── Get Username By ID ───────────────────────────────────
    public String getUsernameById(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "SELECT username FROM users WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {        // ✅ rs closed
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
        } catch (SQLException e) {
            System.out.println("Get username error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return "User";
    }

    // ─── Add Buyer Details ────────────────────────────────────
    public boolean addBuyerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO buyer_details (user_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add buyer details error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }

    // ─── Add Seller Details ───────────────────────────────────
    public boolean addSellerDetails(int userId) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO seller_details (user_id) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Add seller details error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
        return false;
    }
}