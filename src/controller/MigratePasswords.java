package controller;

import Database.MySqlConnector;
import java.sql.*;

public class MigratePasswords {

    public static void main(String[] args) {
        MySqlConnector mysql = new MySqlConnector();
        Connection conn = mysql.openConnection();

        if (conn == null) {
            System.out.println("❌ Connection failed!");
            return;
        }

        try {
            // Get all users
            String selectSql = "SELECT id, username, password FROM users";
            PreparedStatement selectPs = conn.prepareStatement(selectSql);
            ResultSet rs = selectPs.executeQuery();

            int total = 0;
            int updated = 0;
            int skipped = 0;

            while (rs.next()) {
                total++;
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String currentPassword = rs.getString("password");

                // ✅ Skip if already encrypted (Base64 strings end with = usually)
                if (currentPassword.length() > 20 && currentPassword.endsWith("=")) {
                    System.out.println("⏭️  Skipping user " + username 
                        + " (already encrypted)");
                    skipped++;
                    continue;
                }

                // Encrypt the plain text password
                String encrypted = PasswordEncryptor.encrypt(currentPassword);
                if (encrypted == null) {
                    System.out.println("❌ Encrypt failed for: " + username);
                    continue;
                }

                // Update in DB
                String updateSql = "UPDATE users SET password = ? WHERE id = ?";
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setString(1, encrypted);
                    updatePs.setInt(2, id);
                    int rows = updatePs.executeUpdate();
                    if (rows > 0) {
                        System.out.println("✅ Updated user: " + username);
                        updated++;
                    }
                }
            }

            rs.close();
            selectPs.close();

            System.out.println("\n═══════════════════════════════════");
            System.out.println("📊 MIGRATION COMPLETE");
            System.out.println("═══════════════════════════════════");
            System.out.println("Total users:     " + total);
            System.out.println("Encrypted now:   " + updated);
            System.out.println("Already done:    " + skipped);
            System.out.println("═══════════════════════════════════");

        } catch (SQLException e) {
            System.out.println("❌ Error: " + e.getMessage());
        } finally {
            mysql.closeConnection(conn);
        }
    }
}