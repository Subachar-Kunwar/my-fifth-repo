package controller;

import dao.UserDAO;
import java.sql.*;
import Database.MySqlConnector;

public class LoginController {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Attempts login. Returns null on failure, or String[3] on success:
     * [0] = username, [1] = role, [2] = userId
     */
    public String[] login(String username, String email, String password) {
        if (username == null || username.isEmpty()) return null;
        if (email == null || !email.contains("@")) return null;
        if (password == null || password.length() < 8) return null;

        MySqlConnector conn = new MySqlConnector();
        java.sql.Connection c = null;

        try {
            c = conn.openConnection();
            String sql = "SELECT id, username, role FROM users " +
                         "WHERE username = ? AND email = ? AND password = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new String[]{
                    rs.getString("username"),
                    rs.getString("role"),
                    String.valueOf(rs.getInt("id"))
                };
            }
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        } finally {
            if (c != null) conn.closeConnection(c);
        }
        return null;
    }
}