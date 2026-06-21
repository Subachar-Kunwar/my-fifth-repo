package controller;

import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class OTPFieldHelper {

    /**
     * Wires up auto-advance, backspace, arrow keys, delete, home/end,
     * and select-all-on-focus behavior across OTP digit fields.
     */
    public static void setupOTPFields(JTextField... fields) {

        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            final JTextField current = fields[i];

            // ─── Auto-advance + length limit ──────────────────
            current.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent evt) {
                    if (current.getText().length() > 1) {
                        current.setText(current.getText().substring(0, 1));
                    }
                    if (current.getText().length() == 1
                            && index < fields.length - 1) {
                        java.awt.EventQueue.invokeLater(() -> {
                            fields[index + 1].requestFocus();
                            fields[index + 1].selectAll();
                        });
                    }
                }
            });

            // ─── Arrow keys, backspace, delete, home/end ──────
            current.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent evt) {
                    int code = evt.getKeyCode();

                    if (code == KeyEvent.VK_LEFT && index > 0) {
                        fields[index - 1].requestFocus();
                        fields[index - 1].selectAll();
                    }
                    else if (code == KeyEvent.VK_RIGHT
                            && index < fields.length - 1) {
                        fields[index + 1].requestFocus();
                        fields[index + 1].selectAll();
                    }
                    else if (code == KeyEvent.VK_BACK_SPACE) {
                        if (!current.getText().isEmpty()) {
                            current.setText("");
                        } else if (index > 0) {
                            fields[index - 1].requestFocus();
                            fields[index - 1].selectAll();
                        }
                    }
                    else if (code == KeyEvent.VK_DELETE) {
                        current.setText("");
                    }
                    else if (code == KeyEvent.VK_HOME) {
                        fields[0].requestFocus();
                        fields[0].selectAll();
                    }
                    else if (code == KeyEvent.VK_END) {
                        fields[fields.length - 1].requestFocus();
                        fields[fields.length - 1].selectAll();
                    }
                }
            });

            // ─── Select all on focus ──────────────────────────
            current.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent evt) {
                    current.selectAll();
                }
            });
        }
    }

    /**
     * Combine all field values into a single OTP string.
     */
    public static String getCombinedOTP(JTextField... fields) {
        StringBuilder sb = new StringBuilder();
        for (JTextField f : fields) {
            sb.append(f.getText().trim());
        }
        return sb.toString();
    }

    /**
     * Clear all fields and focus the first.
     */
    public static void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
        if (fields.length > 0) fields[0].requestFocus();
    }
}