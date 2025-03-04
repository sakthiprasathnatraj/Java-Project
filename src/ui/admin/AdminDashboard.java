package ui.admin;

import dao.UserDAO;
import ui.Dashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboard {

    public AdminDashboard(String role) {
        JFrame frame = new JFrame(role + " Dashboard");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, " + role + "!");
        welcomeLabel.setBounds(150, 50, 200, 30);
        frame.add(welcomeLabel);

        JButton manageRooms = new JButton("Manage Rooms");
        manageRooms.setBounds(150, 100, 200, 30);
        frame.add(manageRooms);

        JButton manageBookings = new JButton("Manage Bookings");
        manageBookings.setBounds(150, 150, 200, 30);
        frame.add(manageBookings);

        JButton managePayments = new JButton("Manage Payments");
        managePayments.setBounds(150, 200, 200, 30);
        frame.add(managePayments);

        JButton manageUsers = null;
        if (role.equals("Admin")) {
            manageUsers = new JButton("Manage Users");
            manageUsers.setBounds(150, 250, 200, 30);
            frame.add(manageUsers);
        }

        manageUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageUsersUI();
            }
        });

        frame.setVisible(true);
    }
}
