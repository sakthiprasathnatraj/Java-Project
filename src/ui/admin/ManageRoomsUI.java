package ui.admin;

import dao.PaymentDAO;
import dao.UserDAO;
import model.Payment;
import model.Room;
import model.Booking;
import service.RoomService;
import service.BookingService;
import ui.components.MessageDialog;
import ui.login.LoginForm;
import ui.staff.StaffDashboardUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ManageRoomsUI extends JFrame {
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomService roomService;
    private BookingService bookingService;
    private JButton btnToggleAvailability, btnRefresh;
    private PaymentDAO paymentDAO = new PaymentDAO();
    private UserDAO userDAO  = new UserDAO();

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61); // Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    public ManageRoomsUI(String role) {
        roomService = new RoomService();
        bookingService = new BookingService();

        setTitle("Manage Rooms");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make it full-screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Manage Rooms", SwingConstants.LEFT);
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

        add(headerPanel, BorderLayout.NORTH);

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Booking ID", "Customer Name", "Room Number", "Type", "Price", "Available", "Payment Status"}, 0);
        roomTable = new JTable(tableModel);
        roomTable.setRowHeight(30);
        roomTable.setFont(new Font("SansSerif", Font.PLAIN, 16));
        roomTable.setBackground(Color.WHITE);
        roomTable.setForeground(TEXT_COLOR);
        roomTable.setGridColor(new Color(224, 224, 224));

        // Center-align table values
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < roomTable.getColumnCount(); i++) {
            roomTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style Table Header
        JTableHeader tableHeader = roomTable.getTableHeader();
        tableHeader.setFont(new Font("SansSerif", Font.BOLD, 18));
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setBackground(HEADER_COLOR);

        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Buttons
        btnToggleAvailability = createStyledButton("Check-out Room", PRIMARY_COLOR);
        btnRefresh = createStyledButton("Refresh", PRIMARY_COLOR);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 10));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(btnToggleAvailability);
        buttonPanel.add(btnRefresh);

        // Main Panel
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Load all rooms
        loadAllRooms();

        // Button Listeners
        btnToggleAvailability.addActionListener((ActionEvent e) -> checkOutSelectedRoom());
        btnRefresh.addActionListener((ActionEvent e) -> loadAllRooms());

        // Home Button Action
        homeButton.addActionListener((ActionEvent e) -> {
            dispose(); // Close the current window
            if (role.equals("Admin")) {
                new AdminDashboard(role); // Open AdminDashboard
            } else {
                new StaffDashboardUI(role); // Open StaffDashboardUI
            }
        });

        // Logout Button Action
        logoutButton.addActionListener((ActionEvent e) -> {
            dispose(); // Close the current window
            new LoginForm(); // Open LoginForm
        });

        setVisible(true);
    }

    private void loadAllRooms() {
        tableModel.setRowCount(0); // Clear table before loading

        List<Room> rooms = roomService.getAllRooms();

        for (Room room : rooms) {
            String availability = room.isAvailable() ? "Available" : "Booked";
            String guestName = "-";  // Default value if no booking
            String paymentStatus = "-"; // Default value if no payment info
            int bookingId = -1; // Default value if no booking

            // Fetch the latest booking info for this room
            Booking booking = bookingService.getBookingByRoomId(room.getId());
            if (booking != null && "Confirmed".equals(booking.getStatus())) {
                bookingId = booking.getId();  // Assign booking ID
                guestName = booking.getGuestName(); // Assign guest name
                availability = "Booked"; // Override room availability if booked

                // Fetch payment status for this booking
                Payment payment = paymentDAO.getPaymentByBookingId(bookingId);
                if (payment != null) {
                    paymentStatus = payment.getPaymentStatus();
                }
            }

            // Add the room data to the table
            tableModel.addRow(new Object[]{
                    bookingId == -1 ? "N/A" : bookingId,
                    guestName,
                    room.getRoomNumber(),
                    room.getRoomType(),
                    room.getPrice(),
                    availability,
                    paymentStatus
            });
        }
    }

    private void checkOutSelectedRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            MessageDialog.showWarningMessage(this, "Please select a room to check out");
            return;
        }

        int roomId = roomService.getRoomIdByRoomNumber((String) tableModel.getValueAt(selectedRow, 2)); // Fetch room ID
        String availabilityStr = (String) tableModel.getValueAt(selectedRow, 5);
        Booking booking = bookingService.getBookingByRoomId(roomId);
        if ("Booked".equals(availabilityStr)) {
            roomService.updateRoomAvailability(roomId, true);
            bookingService.checkoutBooking(booking.getId());
        }
        loadAllRooms();
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