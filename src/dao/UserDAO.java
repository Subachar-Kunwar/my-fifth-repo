package dao;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserDAO {

    // ✅ LOGIN METHOD
    public boolean loginUser(User user) {

        boolean isValidUser = false;

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String query = "SELECT * FROM users WHERE email = ? AND password = ?";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, user.getEmail());
            pst.setString(2, user.getPassword());

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                isValidUser = true;
            }

            connector.closeConnection(conn);

        } catch (Exception e) {
            System.out.println(e);
        }

        return isValidUser;
    }

    // ✅ UPDATE PASSWORD METHOD
    public boolean updatePassword(String email, String newPassword) {

        boolean updated = false;

        try {
            MySqlConnector connector = new MySqlConnector();
            Connection conn = connector.openConnection();

            String query = "UPDATE users SET password = ? WHERE email = ?";

            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, newPassword);
            pst.setString(2, email);

            int rows = pst.executeUpdate();

            if (rows > 0) {
                updated = true;
            }

            connector.closeConnection(conn);

        } catch (Exception e) {
            System.out.println(e);
        }

        return updated;
    }
}