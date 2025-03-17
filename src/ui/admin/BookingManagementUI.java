package ui.admin;

import dao.BookingDAO;
import dao.RoomDAO;
import model.Booking;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookingManagementUI {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private BookingDAO bookingDAO;
    private RoomDAO roomDAO;  // Added to fetch room numbers

    public BookingManagementUI() {
        bookingDAO = new BookingDAO();
        roomDAO = new RoomDAO();

        frame = new JFrame("Manage Bookings");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Room Number", "User ID", "Check-in", "Check-out", "Total Price", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        loadBookings();

        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("Add");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Add Booking Button
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openBookingForm(null);
            }
        });

        // Update Booking Button

        // Delete Booking Button
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(frame, "Please select a booking to delete.");
                    return;
                }
                int bookingId = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this booking?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    bookingDAO.deleteBooking(bookingId);
                    loadBookings();
                }
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                    loadBookings();
                }

        });

        frame.setVisible(true);

    }

    private void loadBookings() {
        tableModel.setRowCount(0);
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking booking : bookings) {
            // Fetch Room Number instead of Room ID
            Room room = roomDAO.getRoomById(booking.getRoomId());
            String roomNumber = (room != null) ? room.getRoomNumber() : "N/A";

            tableModel.addRow(new Object[]{
                    booking.getId(),
                    roomNumber,  // Show Room Number instead of Room ID
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

    public void refreshTable() {
        loadBookings();
    }
}
