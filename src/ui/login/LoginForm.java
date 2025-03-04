package ui.login;

import dao.UserDAO;
import ui.Dashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleDropdown;

    public LoginForm() {
        frame = new JFrame("Hotel Booking - Login");
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(50, 50, 100, 30);
        frame.add(userLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);
        frame.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(50, 90, 100, 30);
        frame.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 200, 30);
        frame.add(passwordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 130, 100, 30);
        frame.add(roleLabel);

        String[] roles = {"Admin", "Staff"};
        roleDropdown = new JComboBox<>(roles);
        roleDropdown.setBounds(150, 130, 200, 30);
        frame.add(roleDropdown);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(150, 170, 100, 30);
        frame.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String role = (String) roleDropdown.getSelectedItem();

                UserDAO userDAO = new UserDAO();
                if (userDAO.validateUser(username, password, role)) {
                    JOptionPane.showMessageDialog(frame, "Login Successful!");
                    frame.dispose();
                    new Dashboard(role);  // Open the dashboard based on role
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials");
                }
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new LoginForm();
    }
}

