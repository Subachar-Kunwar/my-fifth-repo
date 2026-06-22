package group7.rewear;

import view.Login;
import controller.PayementController; // Imported your payment controller

public class Group7Rewear {

    public static void main(String[] args) {
        
        // 1. Start the local background payment listener for eSewa callbacks
        PayementController.startPaymentServer();

        // 2. Load your login screen UI (Your existing code)
        Login login = new Login();
        login.setVisible(true);

    }
}