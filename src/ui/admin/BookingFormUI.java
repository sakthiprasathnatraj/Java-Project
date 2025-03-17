package ui.admin;

import model.Booking;
import model.Room;
import service.BookingService;
import service.RoomService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.List;

public class BookingFormUI extends JFrame {
    private JComboBox<Integer> roomDropdown;
    private JTextField guestNameField;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JLabel totalPriceLabel;
    private JButton btnBook;
    private RoomService roomService;
    private BookingService bookingService;

    public BookingFormUI() {
        roomService = new RoomService();
        bookingService = new BookingService();

        setTitle("Book a Room");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(6, 2));

        // Room Selection
        add(new JLabel("Select Room:"));
        roomDropdown = new JComboBox<>();
        loadAvailableRooms();
        add(roomDropdown);

        // Guest Name
        add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        add(guestNameField);

        // Check-in Date
        add(new JLabel("Check-in Date (YYYY-MM-DD):"));
        checkInField = new JTextField();
        add(checkInField);

        // Check-out Date
        add(new JLabel("Check-out Date (YYYY-MM-DD):"));
        checkOutField = new JTextField();
        add(checkOutField);

        // Total Price (Auto Calculated)
        add(new JLabel("Total Price:"));
        totalPriceLabel = new JLabel("0.0");
        add(totalPriceLabel);

        // Book Button
        btnBook = new JButton("Book Room");
        add(btnBook);

        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookRoom();
            }
        });

        setVisible(true);
    }

    private void loadAvailableRooms() {
        List<Room> rooms = roomService.getAllRooms();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                roomDropdown.addItem(room.getId());
            }
        }
    }

    private void bookRoom() {
        try {
            int roomId = (Integer) roomDropdown.getSelectedItem();
            String guestName = guestNameField.getText();
            Date checkIn = Date.valueOf(checkInField.getText());
            Date checkOut = Date.valueOf(checkOutField.getText());
            double totalPrice = calculateTotalPrice(roomId, checkIn, checkOut);

            Booking booking = new Booking(0, roomId, 1, guestName, checkIn, checkOut, totalPrice, "Confirmed");
            bookingService.bookRoom(booking);

            JOptionPane.showMessageDialog(this, "Room booked successfully!");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private double calculateTotalPrice(int roomId, Date checkIn, Date checkOut) {
        Room room = roomService.getRoomById(roomId);
        long diff = checkOut.getTime() - checkIn.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return days * room.getPrice();
    }

    public static void main(String[] args) {
        new BookingFormUI();
    }
}
