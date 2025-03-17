package ui.login;

import dao.UserDAO;
import ui.admin.AdminDashboard;
import ui.staff.StaffDashboardUI;

import javax.swing.*;
import java.awt.*;

public class LoginForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;

    public LoginForm() {
        frame = new JFrame("Hotel Booking - Login");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel titleLabel = new JLabel("HOTEL BOOKING SYSTEM", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(500, 50, 400, 40);
        frame.add(titleLabel);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(500, 150, 100, 30);
        frame.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(650, 150, 200, 30);
        frame.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(500, 200, 100, 30);
        frame.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(650, 200, 200, 30);
        frame.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(500, 250, 100, 30);
        frame.add(roleLabel);

        String[] roles = {"Admin", "Staff"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(650, 250, 200, 30);
        frame.add(roleDropdown);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(650, 300, 100, 30);
        loginButton.setBackground(new Color(46, 204, 113));
        loginButton.setForeground(Color.WHITE);
        frame.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleDropdown.getSelectedItem();

            UserDAO userDAO = new UserDAO();
            if (userDAO.validateUser(username, password, role)) {
                JOptionPane.showMessageDialog(frame, "Login Successful!");
                frame.dispose();
                if (role.equals("Admin")) {
                    new AdminDashboard(role);
                } else {
                    new StaffDashboardUI(role);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid credentials");
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}
