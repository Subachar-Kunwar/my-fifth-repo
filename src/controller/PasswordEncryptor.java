package controller;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordEncryptor {

    // ─── Encrypt (Hash) Password ──────────────────────────────
    public static String encrypt(String password) {
        try {
            // ✅ BCrypt automatically generates a unique salt
            return BCrypt.hashpw(password, BCrypt.gensalt(12));
        } catch (Exception e) {
            System.out.println("Encryption error: " + e.getMessage());
            return null;
        }
    }

    // ─── Verify Password ──────────────────────────────────────
    // Compares plain-text input with hashed password from DB
    public static boolean verify(String password, String hashedPassword) {
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            System.out.println("Verify error: " + e.getMessage());
            return false;
        }
    }

    // ─── Test Method ──────────────────────────────────────────
    public static void main(String[] args) {
        String original = "hello123";

        String encrypted = encrypt(original);
        System.out.println("Original:  " + original);
        System.out.println("Encrypted: " + encrypted);

        boolean match = verify("hello123", encrypted);
        boolean wrong = verify("wrongpass", encrypted);
        System.out.println("Correct password verify: " + match);
        System.out.println("Wrong password verify:   " + wrong);
    }
}