package ui.staff;

import dao.BookingDAO;
import dao.PaymentDAO;
import dao.RoomDAO;
import model.Booking;
import model.Payment;
import ui.admin.AdminDashboard;
import ui.components.MessageDialog;
import ui.login.LoginForm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PaymentProcessingUI {
    private JComboBox<Integer> cmbBookingId;
    private JTextField txtAmount;
    private JComboBox<String> cmbPaymentMethod;
    private JComboBox<String> cmbPaymentStatus;
    private JButton btnProcess;
    private JButton btnCancel;

    private PaymentDAO paymentDAO;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;

    // Modern color palette
    private static final Color PRIMARY_COLOR = new Color(33, 150, 243); // Blue
    private static final Color SECONDARY_COLOR = new Color(255, 87, 34); // Orange
    private static final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private static final Color HEADER_COLOR = new Color(33, 47, 61); // Dark blue
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private static final Color BUTTON_HOVER_COLOR = new Color(25, 118, 210); // Darker blue

    public PaymentProcessingUI(String role) {
        paymentDAO = new PaymentDAO();
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();

        JFrame frame = new JFrame("Process Payment");
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen mode
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Use BorderLayout for the main frame
        frame.getContentPane().setBackground(BACKGROUND_COLOR); // Light gray background

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(HEADER_COLOR); // Dark blue background
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Process Payment", SwingConstants.LEFT);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Logout and Home Buttons (Top-Right Corner)
        JPanel buttonPanelTopRight = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanelTopRight.setBackground(HEADER_COLOR); // Match header background

        JButton homeButton = createStyledButton("Home", PRIMARY_COLOR);
        JButton logoutButton = createStyledButton("Logout", SECONDARY_COLOR);

        buttonPanelTopRight.add(homeButton);
        buttonPanelTopRight.add(logoutButton);
        headerPanel.add(buttonPanelTopRight, BorderLayout.EAST);

        frame.add(headerPanel, BorderLayout.NORTH);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR); // Light gray background
        mainPanel.setLayout(new GridBagLayout());
        frame.add(mainPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Booking ID
        JLabel lblBookingId = new JLabel("Booking ID:");
        lblBookingId.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblBookingId, gbc);

        cmbBookingId = new JComboBox<>();
        cmbBookingId.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbBookingId.setBackground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(cmbBookingId, gbc);

        // Amount
        JLabel lblAmount = new JLabel("Amount:");
        lblAmount.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblAmount, gbc);

        txtAmount = new JTextField();
        txtAmount.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtAmount.setBackground(Color.WHITE);
        txtAmount.setEditable(false);
        gbc.gridx = 1;
        mainPanel.add(txtAmount, gbc);

        // Payment Method
        JLabel lblPaymentMethod = new JLabel("Payment Method:");
        lblPaymentMethod.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblPaymentMethod, gbc);

        String[] paymentMethods = {"Cash", "Credit Card", "Debit Card", "UPI"};
        cmbPaymentMethod = new JComboBox<>(paymentMethods);
        cmbPaymentMethod.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbPaymentMethod.setBackground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(cmbPaymentMethod, gbc);

        // Payment Status
        JLabel lblPaymentStatus = new JLabel("Payment Status:");
        lblPaymentStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(lblPaymentStatus, gbc);

        String[] statuses = {"Pending", "Failed", "Completed"};
        cmbPaymentStatus = new JComboBox<>(statuses);
        cmbPaymentStatus.setFont(new Font("SansSerif", Font.PLAIN, 14));
        cmbPaymentStatus.setBackground(Color.WHITE);
        gbc.gridx = 1;
        mainPanel.add(cmbPaymentStatus, gbc);

        // Process Payment Button
        btnProcess = createStyledButton("Process Payment", PRIMARY_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 4;
        btnProcess.setPreferredSize(new Dimension(160, 40)); // Consistent size
        mainPanel.add(btnProcess, gbc);

        // Cancel Button
        btnCancel = createStyledButton("Cancel", SECONDARY_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 5;
        btnCancel.setPreferredSize(new Dimension(160, 40)); // Consistent size
        mainPanel.add(btnCancel, gbc);

        // Generate Invoice Button (after btnCancel)
        JButton btnGenerateInvoice = createStyledButton("Generate Invoice", PRIMARY_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 6;
        btnGenerateInvoice.setPreferredSize(new Dimension(200, 45)); // Increased size
        mainPanel.add(btnGenerateInvoice, gbc);

        // Add action listener for Generate Invoice
        btnGenerateInvoice.addActionListener(e -> generateInvoice(frame));

        // Load Bookings
        loadBookings();

        // Event Listeners
        cmbBookingId.addActionListener(e -> updateAmountAndStatus());

        btnProcess.addActionListener((ActionEvent e) -> processPayment(frame));

        btnCancel.addActionListener(e -> {
            frame.dispose();
            if (role.equals("Admin")) {
                new AdminDashboard(role);
            } else {
                new StaffDashboardUI(role);
            }
        });

        // Home Button Action
        homeButton.addActionListener(e -> {
            frame.dispose();
            if (role.equals("Admin")) {
                new AdminDashboard(role);
            } else {
                new StaffDashboardUI(role);
            }
        });

        // Logout Button Action
        logoutButton.addActionListener(e -> {
            frame.dispose();
            new LoginForm();
        });

        frame.setVisible(true);
    }

    private void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings(); // Fetch all bookings
        for (Booking booking : bookings) {
            if (!"CheckedOut".equals(booking.getStatus())) {
                cmbBookingId.addItem(booking.getId());
            }
        }
    }

    private void updateAmountAndStatus() {
        int bookingId = (int) cmbBookingId.getSelectedItem();
        Booking booking = bookingDAO.getBookingById(bookingId);
        Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

        if (booking != null) {
            txtAmount.setText(String.valueOf(booking.getTotalPrice()));
        }

        if (payment != null && "Completed".equals(payment.getPaymentStatus())) {
            btnProcess.setEnabled(false);
            MessageDialog.showWarningMessage(null, "This booking is already paid!");
        } else {
            btnProcess.setEnabled(true); // ✅ Ensure the button is enabled if not paid
        }
    }

    private void generateInvoice(JFrame frame) {
        int bookingId = (int) cmbBookingId.getSelectedItem();
        Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

        if (payment == null || !"Completed".equals(payment.getPaymentStatus())) {
            MessageDialog.showWarningMessage(frame, "Invoice can only be generated for completed payments!");
            return;
        }

        // Fetch booking details
        Booking booking = bookingDAO.getBookingById(bookingId);
        if (booking == null) {
            MessageDialog.showErrorMessage(frame, "Booking details not found!");
            return;
        }

        StringBuilder invoice = new StringBuilder();
        invoice.append("========================================================\n");
        invoice.append("               NGP GRAND HORIZON HOTEL                  \n");
        invoice.append("========================================================\n");
        invoice.append(String.format("Booking ID: %d\n", booking.getId()));
        invoice.append(String.format("Customer Name: %s\n", booking.getGuestName()));
        invoice.append(String.format("Room: %s\n", roomDAO.getRoomById(booking.getRoomId()).getRoomNumber()));
        invoice.append(String.format("Check-in: %s\n", booking.getCheckIn()));
        invoice.append(String.format("Check-out: %s\n", booking.getCheckOut()));
        invoice.append(String.format("Total Price: \u20B9%.2f\n", booking.getTotalPrice()));
        invoice.append(String.format("Payment Method: %s\n", payment.getPaymentMethod()));
        invoice.append(String.format("Payment Status: %s\n", payment.getPaymentStatus()));
        invoice.append("========================================================\n");

        JTextArea textArea = new JTextArea(invoice.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(350, 200));

        int option = JOptionPane.showConfirmDialog(frame, scrollPane, "Invoice", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            printInvoice(invoice.toString());
        }
    }

    private void printInvoice(String invoiceContent) {
        JTextArea textArea = new JTextArea(invoiceContent);
        try {
            textArea.print(); // Print the invoice
        } catch (Exception e) {
            MessageDialog.showErrorMessage(null, "Failed to print invoice!");
        }
    }


    private void processPayment(JFrame frame) {
        try {
            int bookingId = (int) cmbBookingId.getSelectedItem();
            double amount = Double.parseDouble(txtAmount.getText());
            String paymentMethod = (String) cmbPaymentMethod.getSelectedItem();
            String paymentStatus = (String) cmbPaymentStatus.getSelectedItem();
            Payment payment = paymentDAO.getPaymentByBookingId(bookingId);

            try {
                if ("Completed".equals(payment.getPaymentStatus())) {
                    MessageDialog.showWarningMessage(frame, "Payment is already completed!");
                    return;
                }
            } catch (NullPointerException npe) {
                System.out.println("Payment not available for booking id: " + bookingId);
            }

            paymentDAO.addOrUpdatePayment(bookingId, amount, paymentStatus, paymentMethod);
            MessageDialog.showSuccessMessage(frame, "Payment Processed Successfully!");
        } catch (NumberFormatException ex) {
            MessageDialog.showErrorMessage(frame, "Invalid input! Please enter valid data.");
        }
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
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