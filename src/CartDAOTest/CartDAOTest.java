package CartDAOTest;

import dao.CartDAO;
import model.CartItem;
import java.util.List;

public class CartDAOTest {
    public static void main(String[] args) {
        CartDAO cartDAO = new CartDAO();
        
        // Define a test user and test product (ensure these IDs exist in your DB!)
        int testUserId = 1; 
        int testProductId = 1; 

        System.out.println("--- Starting CartDAO Test ---");

        // 1. Test Adding to Cart
        System.out.println("1. Adding product " + testProductId + " to user " + testUserId + "'s cart...");
        boolean added = cartDAO.addToCart(testUserId, testProductId);
        System.out.println("Result: " + (added ? "SUCCESS" : "FAILED"));

        // 2. Test Fetching Cart Items
        System.out.println("\n2. Fetching Cart Items...");
        List<CartItem> items = cartDAO.getCartItems(testUserId);
        if (items.isEmpty()) {
            System.out.println("Result: Cart is empty!");
        } else {
            for (CartItem item : items) {
                System.out.println("- Found: " + item.getProductName() + 
                    " | Qty: " + item.getQuantity() + 
                    " | Price: " + item.getPrice());
            }
        }

        // 3. Test Cart Count
        int count = cartDAO.getCartCount(testUserId);
        System.out.println("\n3. Total Cart Count: " + count);

        // 4. Test Clearing Cart
        System.out.println("\n4. Clearing Cart...");
        boolean cleared = cartDAO.clearCart(testUserId);
        System.out.println("Result: " + (cleared ? "SUCCESS" : "FAILED"));

        // Verify it was cleared
        int finalCount = cartDAO.getCartCount(testUserId);
        System.out.println("Final Cart Count (Should be 0): " + finalCount);
    }
}