package ui.admin;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ManageUsersUI {
    private JFrame frame;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    private JTextField usernameField, passwordField;
    private JButton addButton, updateButton, deleteButton, refreshButton;

    public ManageUsersUI() {
        frame = new JFrame("Manage Staff Users");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel label = new JLabel("Staff Users:");
        label.setBounds(20, 10, 100, 20);
        frame.add(label);

       
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Username", "Role"}) {
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        usersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBounds(20, 40, 540, 150);
        frame.add(scrollPane);


        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(20, 200, 100, 25);
        frame.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(120, 200, 150, 25);
        frame.add(usernameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(20, 230, 100, 25);
        frame.add(passwordLabel);

        passwordField = new JTextField();
        passwordField.setBounds(120, 230, 150, 25);
        frame.add(passwordField);


        addButton = new JButton("Add");
        addButton.setBounds(300, 200, 100, 25);
        frame.add(addButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(300, 230, 100, 25);
        frame.add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(420, 200, 100, 25);
        frame.add(deleteButton);

        refreshButton = new JButton("Refresh");
        refreshButton.setBounds(420, 230, 100, 25);
        frame.add(refreshButton);


        loadUsers();


        addButton.addActionListener(new AddUserAction());


        updateButton.addActionListener(new UpdateUserAction());


        deleteButton.addActionListener(new DeleteUserAction());


        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadUsers();
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


    private class AddUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (!username.isEmpty() && !password.isEmpty()) {
                UserDAO userDAO = new UserDAO();
                userDAO.addUser(username, password, "STAFF");
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(frame, "Fields cannot be empty!");
            }
        }
    }

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
                    JOptionPane.showMessageDialog(frame, "Username cannot be empty!");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Select a user to update!");
            }
        }
    }

    private class DeleteUserAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int row = usersTable.getSelectedRow();
            if (row != -1) {
                int userId = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                UserDAO userDAO = new UserDAO();
                userDAO.deleteUser(userId);
                loadUsers();
            } else {
                JOptionPane.showMessageDialog(frame, "Select a user to delete!");
            }
        }
    }
}
