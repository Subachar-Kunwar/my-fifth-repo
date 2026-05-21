package dao;

import database.MySqlConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import model.User;

public class UserDAO {

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
}