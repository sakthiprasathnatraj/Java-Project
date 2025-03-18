package ui.admin;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;
import ui.components.MessageDialog;
import ui.login.LoginForm;
import ui.staff.StaffDashboardUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookingManagementUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;
    private String role; // Added to store the user's role

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61); // Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    public BookingManagementUI(String role) {
        this.role = role; // Initialize the role
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();

        frame = new JFrame("Manage Bookings");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Booking Management", SwingConstants.LEFT);
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

        // Table Panel (50% of screen)
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setPreferredSize(new Dimension(900, 400));

        String[] columns = {"ID", "Room", "User ID", "Check-in", "Check-out", "Total Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 16));
        table.setBackground(Color.WHITE);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(new Color(224, 224, 224));

        // Center-align table values
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style Table Header
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(HEADER_COLOR);

        loadBookings();

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Button Panel (Grid Layout for Proper Placement)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton addButton = createStyledButton("Add Booking", PRIMARY_COLOR);
        JButton editButton = createStyledButton("Edit Booking", PRIMARY_COLOR);
        JButton deleteButton = createStyledButton("Delete", SECONDARY_COLOR);
        JButton refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // Positioning Elements
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(mainPanel, BorderLayout.CENTER);

        // Button Actions
        addButton.addActionListener(e -> openBookingForm(null));
        editButton.addActionListener(e -> editSelectedBooking());
        deleteButton.addActionListener(e -> deleteSelectedBooking());
        refreshButton.addActionListener(e -> loadBookings());

        // Home Button Action
        homeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.dispose(); // Close the current window
                if (role.equals("Admin")) {
                    new AdminDashboard(role); // Open AdminDashboard
                } else {
                    new StaffDashboardUI(role); // Open StaffDashboardUI
                }
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

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            Room room = roomDAO.getRoomById(booking.getRoomId());
            String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";

            tableModel.addRow(new Object[]{
                    booking.getId(),
                    roomNumber,
                    booking.getUserId(),
                    booking.getCheckIn(),
                    booking.getCheckOut(),
                    booking.getTotalPrice(),
                    booking.getStatus()
            });
        }
    }

    private void openBookingForm(Booking booking) {
        new BookingFormUI();
    }

    private void editSelectedBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarningMessage(frame, "Please select a booking to edit.");
            return;
        }
        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        Booking booking = bookingDAO.getBookingById(bookingId);
        openBookingForm(booking);
    }

    private void deleteSelectedBooking() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarningMessage(frame, "Please select a booking to delete.");
            return;
        }
        int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            bookingDAO.deleteBooking(bookingId);
            loadBookings();
        }
    }

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
}