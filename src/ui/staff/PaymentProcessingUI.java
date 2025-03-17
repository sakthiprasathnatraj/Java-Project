package ui.staff;

import dao.BookingDAO;
import dao.PaymentDAO;
import model.Booking;
import model.Payment;

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

    private PaymentDAO paymentDAO;
    private BookingDAO bookingDAO;

    public PaymentProcessingUI() {
        paymentDAO = new PaymentDAO();
        bookingDAO = new BookingDAO();

        JFrame frame = new JFrame("Process Payment");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel lblBookingId = new JLabel("Booking ID:");
        cmbBookingId = new JComboBox<>();
        loadBookings();

        JLabel lblAmount = new JLabel("Amount:");
        txtAmount = new JTextField();
        txtAmount.setEditable(false);

        JLabel lblPaymentMethod = new JLabel("Payment Method:");
        String[] paymentMethods = {"Cash", "Credit Card", "Debit Card", "UPI"};
        cmbPaymentMethod = new JComboBox<>(paymentMethods);

        JLabel lblPaymentStatus = new JLabel("Payment Status:");
        String[] statuses = {"Pending", "Failed", "Completed"};  // Add Completed
        cmbPaymentStatus = new JComboBox<>(statuses);

        btnProcess = new JButton("Process Payment");
        JButton btnCancel = new JButton("Cancel");

        frame.add(lblBookingId);
        frame.add(cmbBookingId);
        frame.add(lblAmount);
        frame.add(txtAmount);
        frame.add(lblPaymentMethod);
        frame.add(cmbPaymentMethod);
        frame.add(lblPaymentStatus);
        frame.add(cmbPaymentStatus);
        frame.add(btnProcess);
        frame.add(btnCancel);

        cmbBookingId.addActionListener(e -> updateAmountAndStatus());

        btnProcess.addActionListener((ActionEvent e) -> processPayment(frame));
        btnCancel.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }

    private void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings(); // Fetch all bookings
        for (Booking booking : bookings) {
            if(!"CheckedOut".equals(booking.getStatus())){
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
            JOptionPane.showMessageDialog(null, "This booking is already paid!");
        } else {
            btnProcess.setEnabled(true); // ✅ Ensure the button is enabled if not paid
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
                    JOptionPane.showMessageDialog(frame, "Payment is already completed!");
                    return;
                }
            }catch (NullPointerException npe){
                System.out.println("Payment not available for booking id: "+bookingId);
            }

            paymentDAO.addOrUpdatePayment(bookingId, amount, paymentStatus, paymentMethod);
            JOptionPane.showMessageDialog(frame, "Payment Processed Successfully!");
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid input! Please enter valid data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
