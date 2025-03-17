package ui.admin;

import dao.PaymentDAO;
import model.Payment;
import model.Room;
import model.Booking;
import service.RoomService;
import service.BookingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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


    public ManageRoomsUI() {
        roomService = new RoomService();
        bookingService = new BookingService();

        setTitle("Manage Rooms");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        tableModel = new DefaultTableModel(new Object[]{"Booking ID", "Customer Name", "Room Number", "Type", "Price", "Available", "Payment Status"}, 0);
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        // Buttons
        btnToggleAvailability = new JButton("Check-out Room");
        btnRefresh = new JButton("Refresh");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnToggleAvailability);
        buttonPanel.add(btnRefresh);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load all rooms
        loadAllRooms();

        // Button Listeners
        btnToggleAvailability.addActionListener((ActionEvent e) -> checkOutSelectedRoom());
        btnRefresh.addActionListener((ActionEvent e) -> loadAllRooms());
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
            JOptionPane.showMessageDialog(this, "Please select a room to check out", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int roomId = roomService.getRoomIdByRoomNumber((String) tableModel.getValueAt(selectedRow, 2)); // Fetch room ID
        String availabilityStr = (String) tableModel.getValueAt(selectedRow, 5);
        Booking booking  = bookingService.getBookingByRoomId(roomId);
        if("Booked".equals(availabilityStr)){
            roomService.updateRoomAvailability(roomId, true);
            bookingService.checkoutBooking(booking.getId());
        }
        loadAllRooms();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManageRoomsUI().setVisible(true));
    }
}
