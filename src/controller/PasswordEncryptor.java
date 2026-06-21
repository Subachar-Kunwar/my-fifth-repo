package controller;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class PasswordEncryptor {

    // ✅ Secret key (16 characters - AES requires this)
    private static final String SECRET_KEY = "ReWear2025SecKey";
    private static final String ALGORITHM = "AES";

    // ─── Encrypt Password ─────────────────────────────────────
    public static String encrypt(String password) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                SECRET_KEY.getBytes(), ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            
            byte[] encryptedBytes = cipher.doFinal(password.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
            
        } catch (Exception e) {
            System.out.println("Encryption error: " + e.getMessage());
            return null;
        }
    }

    // ─── Decrypt Password ─────────────────────────────────────
    public static String decrypt(String encryptedPassword) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(
                SECRET_KEY.getBytes(), ALGORITHM);
            
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            
            return new String(decryptedBytes);
            
        } catch (Exception e) {
            System.out.println("Decryption error: " + e.getMessage());
            return null;
        }
    }

    // ─── Test Method (Optional - for demonstration) ───────────
    public static void main(String[] args) {
        String original = "hello123";
        
        String encrypted = encrypt(original);
        String decrypted = decrypt(encrypted);
        
        System.out.println("Original:  " + original);
        System.out.println("Encrypted: " + encrypted);
        System.out.println("Decrypted: " + decrypted);
    }
}