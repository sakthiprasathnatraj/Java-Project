package ui.staff;

import ui.admin.ManageRoomsUI;
import ui.admin.BookingManagementUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StaffDashboardUI {
    public StaffDashboardUI(String role) {
        JFrame frame = new JFrame(role + " Dashboard");
        frame.setSize(500, 350);
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

        JButton logout = new JButton("Logout");
        logout.setBounds(150, 250, 200, 30);
        frame.add(logout);

        // Button Actions
        manageRooms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ManageRoomsUI().setVisible(true);
            }
        });

        manageBookings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new BookingManagementUI();
            }
        });

        managePayments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PaymentProcessingUI(); // Opens the Payment UI
            }
        });

        logout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                JOptionPane.showMessageDialog(null, "Logged out successfully.");
            }
        });

        frame.setVisible(true);
    }
}
