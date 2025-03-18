package ui.admin;

import dao.UserDAO;
import model.User;
import ui.components.MessageDialog;
import ui.login.LoginForm;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageUsersUI {
    private JFrame frame;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField, passwordField;
    private JButton addButton, updateButton, deleteButton, refreshButton;
    private String role; // Added to store the user's role

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61); // Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    public ManageUsersUI(String role) {
        this.role = role; // Initialize the role

        frame = new JFrame("Manage Staff Users");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-Screen Mode
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Manage Staff Users", SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Logout and Home Buttons (Top-Right Corner)
        JPanel buttonPanelTopRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelTopRight.setBackground(HEADER_COLOR);

        JButton homeButton = createStyledButton("Home", PRIMARY_COLOR);
        JButton logoutButton = createStyledButton("Logout", SECONDARY_COLOR);

        buttonPanelTopRight.add(homeButton);
        buttonPanelTopRight.add(logoutButton);
        headerPanel.add(buttonPanelTopRight, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Panel (Holds Table & Buttons)
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(900, 400));

        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Username", "Role"}) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        usersTable = new JTable(tableModel);
        usersTable.setRowHeight(30);
        usersTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        usersTable.setBackground(Color.WHITE);
        usersTable.setForeground(TEXT_COLOR);
        usersTable.setGridColor(new Color(224, 224, 224));

        // Center-align table values
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < usersTable.getColumnCount(); i++) {
            usersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style Table Header
        JTableHeader tableHeader = usersTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(HEADER_COLOR);

        JScrollPane scrollPane = new JScrollPane(usersTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel (Grid Layout for Proper Placement)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        addButton = createStyledButton("Add", PRIMARY_COLOR);
        updateButton = createStyledButton("Update", PRIMARY_COLOR);
        deleteButton = createStyledButton("Delete", SECONDARY_COLOR);
        refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Positioning Elements
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Load Users
        loadUsers();

        // Button Actions
        addButton.addActionListener(new AddUserAction());
        updateButton.addActionListener(new UpdateUserAction());
        deleteButton.addActionListener(new DeleteUserAction());
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
            }
        });

        // Home Button Action
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                new AdminDashboardUI(role); // Open AdminDashboard
            }
        });

        // Logout Button Action
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                new LoginForm(); // Open LoginForm
            }
        });

        frame.setVisible(true);
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        UserDAO userDAO = new UserDAO();
        List<User> users = userDAO.getAllStaff();
        for (User user : users) {
            tableModel.addRow(new Object[]{user.getId(), user.getUsername(), user.getRole()});
        }
    }

    // Styled Button Helper
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setPreferredSize(new Dimension(160, 40)); // Consistent size: 160x40
        button.setFocusPainted(false);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE); // White text
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

    // Add User Action
    private class AddUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                UserDAO userDAO = new UserDAO();
                userDAO.addUser(username, password, "STAFF");
                loadUsers();
            } else {
                MessageDialog.showErrorMessage(frame, "Fields cannot be empty!");
            }
        }
    }

    // Update User Action
    private class UpdateUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = usersTable.getSelectedRow();
            if (row != -1) {
                int userId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                String newUsername = usernameField.getText();
                String newPassword = passwordField.getText();
                if (!newUsername.isEmpty()) {
                    UserDAO userDAO = new UserDAO();
                    userDAO.updateUser(userId, newUsername, newPassword);
                    loadUsers();
                } else {
                    MessageDialog.showErrorMessage(frame, "Username cannot be empty!");
                }
            } else {
                MessageDialog.showWarningMessage(frame, "Select a user to update!");
            }
        }
    }

    // Delete User Action
    private class DeleteUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = usersTable.getSelectedRow();
            if (row != -1) {
                int userId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(userId);
                loadUsers();
            } else {
                MessageDialog.showWarningMessage(frame, "Select a user to delete!");
            }
        }
    }
}