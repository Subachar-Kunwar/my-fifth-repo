package group7.rewear;

import view.Login;

public class Group7Rewear {

    public static void main(String[] args) {
        
        // Load login screen UI safely on the standard Swing Thread
        javax.swing.SwingUtilities.invokeLater(() -> {
            Login login = new Login();
            login.setVisible(true);
        });
    }
}