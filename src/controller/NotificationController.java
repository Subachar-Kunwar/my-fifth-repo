
   package controller;

import dao.NotificationDAO;
import model.Notification;
import view.Notification_page;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


public class NotificationController {

    private Notification_page view;
    private NotificationDAO dao;
    private int userId;

    // Store checkbox & id mapping
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private List<Integer> notificationIds = new ArrayList<>();

    public NotificationController(Notification_page view, int userId) {
        this.view = view;
        this.userId = userId;
        this.dao = new NotificationDAO();

        init();
    }

    private void init() {

        loadNotifications();

        view.getProfileBtn().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Dashboard open")
        );

        view.getCartBtn().addActionListener(e ->
                JOptionPane.showMessageDialog(view, "Cart view")
        );

        view.getMarkAllBtn().addActionListener(e -> {
            dao.markAllAsRead(userId);
            loadNotifications();
        });

        view.getDeleteBtn().addActionListener(e -> deleteSelected());
    }

    private void loadNotifications() {

        JPanel container = view.getNotificationContainer();
        container.removeAll();

        container.setLayout(new GridLayout(0, 1, 0, 15));
        container.setBackground(new Color(232, 255, 233));

        checkBoxes.clear();
        notificationIds.clear();

        List<Notification> list = dao.getByUser(userId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");

        for (Notification n : list) {

            JPanel panel = new JPanel();
            panel.setLayout(null);
            panel.setPreferredSize(new Dimension(900, 60));
            panel.setBackground(n.isRead()
                    ? new Color(200, 230, 200)
                    : new Color(170, 218, 172));

            JCheckBox check = new JCheckBox();
            check.setBounds(20, 15, 30, 30);
            check.setBackground(panel.getBackground());

            JLabel msg = new JLabel(n.getMessage());
            msg.setBounds(70, 20, 600, 20);

            JLabel date = new JLabel(sdf.format(n.getCreatedAt()));
            date.setBounds(720, 20, 150, 20);

            panel.add(check);
            panel.add(msg);
            panel.add(date);

            container.add(panel);

            // Store mapping
            checkBoxes.add(check);
            notificationIds.add(n.getId());
        }

        container.revalidate();
        container.repaint();
    }

    private void deleteSelected() {

        List<Integer> idsToDelete = new ArrayList<>();

        for (int i = 0; i < checkBoxes.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                idsToDelete.add(notificationIds.get(i));
            }
        }

        if (idsToDelete.isEmpty()) {
            JOptionPane.showMessageDialog(view,
                    "No notification selected",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                view,
                "Delete selected notifications?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dao.deleteByIds(idsToDelete);
            loadNotifications();
        }
    }
}
