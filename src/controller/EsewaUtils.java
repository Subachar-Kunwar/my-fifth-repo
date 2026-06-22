package controller;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.nio.charset.StandardCharsets;

public class EsewaUtils {
    public static final String MERCHANT_CODE = "EPAYTEST";
    public static final String SECRET_KEY = "8gBm/:&EnhH.1/q";

    public static String generateSignature(String totalAmount, String transactionUuid) {
        try {
            String message = "total_amount=" + totalAmount + ",transaction_uuid=" + transactionUuid + ",product_code=" + MERCHANT_CODE;
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            
            byte[] hash = sha256_HMAC.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}