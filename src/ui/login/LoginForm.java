package ui.login;

import dao.UserDAO;
import ui.admin.AdminDashboard;
import ui.components.MessageDialog;
import ui.staff.StaffDashboardUI;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;


    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private static final Color DISABLED_BACKGROUND_COLOR = new Color(78, 76, 76); // gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61); // Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    public LoginForm() {
        frame = new JFrame("Hotel Booking - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode

        // Main panel with a light background
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245)); // Light gray background
        panel.setLayout(new GridBagLayout());
        frame.setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("NGP GRAND HORIZON HOTEL", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(HEADER_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        // Username Label & Field
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(new Color(51, 51, 51));
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(userLabel, gbc);

        usernameField = new JTextField(20);
        styleTextField(usernameField);
        gbc.gridx = 1;
        panel.add(usernameField, gbc);

        // Password Label & Field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(new Color(51, 51, 51));
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Login Button
        JButton loginButton = new JButton("Login");
        styleButton(loginButton);
        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            UserDAO userDAO = new UserDAO();
            if (userDAO.validateUser(username, password)) {
                String role = userDAO.getLoggedInUser(username, password).getRole();
                frame.dispose();
                if (role.equals("Admin")) {
                    new AdminDashboard(role);
                } else {
                    new StaffDashboardUI(role);
                }
            } else {
                MessageDialog.showErrorMessage(frame, "Invalid credentials");
            }
        });

        frame.setVisible(true);
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(204, 204, 204), 1), // Light gray border
                BorderFactory.createEmptyBorder(8, 10, 8, 10) // Padding
        ));
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(51, 51, 51)); // Dark gray text
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setBackground(new Color(0, 123, 255)); // Blue background
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 86, 179)); // Darker blue on hover
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255)); // Restore original color
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginForm::new);
    }
}