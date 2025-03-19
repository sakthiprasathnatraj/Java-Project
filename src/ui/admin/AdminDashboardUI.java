package ui.admin;

import ui.staff.PaymentProcessingUI;
import ui.login.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminDashboardUI {
    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243, 200); // Translucent Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34, 200); // Translucent Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61, 200); // Translucent Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210, 200); // Darker translucent blue

    // Background image path
    private static final String BACKGROUND_IMAGE_PATH = "./src/ui/assets/bg.png"; // Replace with your image path

    public AdminDashboardUI(String role) {
        JFrame frame = new JFrame(role + " Dashboard");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-Screen Mode
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        // Background Panel with Image and Translucent Overlay
        JPanel backgroundPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image
                ImageIcon imageIcon = new ImageIcon(BACKGROUND_IMAGE_PATH);
                Image image = imageIcon.getImage();
                g.drawImage(image, 0, 0, getWidth(), getHeight(), this);

                // Draw a translucent black overlay
                g.setColor(new Color(0, 0, 0, 0)); // Semi-transparent black
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        frame.setContentPane(backgroundPanel);

        // Main Panel for Buttons (Center)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setOpaque(false); // Make panel translucent

        // GridBagConstraints for layout management
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Buttons with Icons
        JButton manageRooms = createStyledButtonWithIcon("Manage Rooms", "./src/ui/assets/rooms_icon-white.png", PRIMARY_COLOR);
        JButton manageBookings = createStyledButtonWithIcon("Manage Bookings", "./src/ui/assets/bookings_icon-white.png", PRIMARY_COLOR);
        JButton managePayments = createStyledButtonWithIcon("Manage Payments", "./src/ui/assets/payments_icon-white.png", PRIMARY_COLOR);
        JButton manageUsers = createStyledButtonWithIcon("Manage Users", "./src/ui/assets/staffs_icon-white.png", PRIMARY_COLOR);

        // Add Buttons to Panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(manageRooms, gbc);

        gbc.gridy = 1;
        mainPanel.add(manageBookings, gbc);

        gbc.gridy = 2;
        mainPanel.add(managePayments, gbc);

        gbc.gridy = 3;
        mainPanel.add(manageUsers, gbc);

        // Add Main Panel to the Center of the Background Panel
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // Logout Button (Top-Right Corner)
        JButton logoutButton = createStyledButton("Logout", SECONDARY_COLOR);
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false); // Make logout panel translucent
        logoutPanel.add(logoutButton);

        // Add Logout Panel to the Top of the Background Panel
        backgroundPanel.add(logoutPanel, BorderLayout.NORTH);

        // Button Actions
        manageRooms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ManageRoomsUI(role).setVisible(true);
            }
        });

        manageBookings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new BookingManagementUI(role);
            }
        });

        managePayments.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new PaymentProcessingUI(role);
            }
        });

        manageUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new ManageUsersUI(role);
            }
        });

        // Logout Action
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current dashboard
                new LoginForm(); // Open the LoginForm UI
            }
        });

        frame.setVisible(true);
    }

    // Styled Button Helper (without icon)
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true); // Show button border
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border
        button.setOpaque(false); // Make button translucent
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(160, 40)); // Consistent size: 160x40

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR); // Darker color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor); // Restore original color
            }
        });

        return button;
    }

    // Styled Button Helper with Icon
    private JButton createStyledButtonWithIcon(String text, String iconPath, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(true); // Show button border
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // White border
        button.setOpaque(false); // Make button translucent
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(250, 50)); // Updated size: 250x50
        button.setHorizontalAlignment(SwingConstants.LEFT); // Align text and icon to the left
        button.setHorizontalTextPosition(SwingConstants.RIGHT); // Place text to the right of the icon

        // Add Icon
        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image scaledIcon = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledIcon));
            button.setIconTextGap(20); // Increased space between icon and text
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        // Add padding to the left of the icon
        button.setBorder(BorderFactory.createCompoundBorder(
                button.getBorder(),
                BorderFactory.createEmptyBorder(0, 20, 0, 0) // Left padding: 20px
        ));

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_COLOR); // Darker color on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor); // Restore original color
            }
        });

        return button;
    }
}