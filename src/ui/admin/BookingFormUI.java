package ui.admin;

import model.Booking;
import model.Room;
import service.BookingService;
import service.RoomService;
import ui.components.MessageDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

public class BookingFormUI extends JFrame {
    private JComboBox<Integer> roomDropdown;
    private JTextField guestNameField;
    private JComboBox<Integer> checkInDayDropdown;
    private JComboBox<String> checkInMonthDropdown;
    private JComboBox<Integer> checkInYearDropdown;
    private JComboBox<Integer> checkOutDayDropdown;
    private JComboBox<String> checkOutMonthDropdown;
    private JComboBox<Integer> checkOutYearDropdown;
    private JLabel totalPriceLabel;
    private JButton btnBook;
    private JButton btnCalculatePrice;
    private RoomService roomService;
    private BookingService bookingService;

    public BookingFormUI() {
        roomService = new RoomService();
        bookingService = new BookingService();

        setTitle("Book a Room");
        setSize(800, 400); // Increased window size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout()); // Use GridBagLayout for better control
        getContentPane().setBackground(new Color(245, 245, 245)); // Light gray background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12); // Padding
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Room Selection
        JLabel roomLabel = new JLabel("Select Room:");
        roomLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(roomLabel, gbc);

        roomDropdown = new JComboBox<>();
        roomDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roomDropdown.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(roomDropdown, gbc);

        // Guest Name
        JLabel guestLabel = new JLabel("Guest Name:");
        guestLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset gridwidth
        add(guestLabel, gbc);

        guestNameField = new JTextField();
        guestNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        guestNameField.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        add(guestNameField, gbc);

        // Check-in Date
        JLabel checkInLabel = new JLabel("Check-in Date:");
        checkInLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        add(checkInLabel, gbc);

        // Check-in Day Dropdown
        checkInDayDropdown = new JComboBox<>();
        checkInDayDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkInDayDropdown.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 1; // Allow space distribution
        add(checkInDayDropdown, gbc);

        // Check-in Month Dropdown
        checkInMonthDropdown = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        checkInMonthDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkInMonthDropdown.setBackground(Color.WHITE);
        gbc.gridx = 2;
        add(checkInMonthDropdown, gbc);

        // Check-in Year Dropdown
        checkInYearDropdown = new JComboBox<>();
        checkInYearDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkInYearDropdown.setBackground(Color.WHITE);
        gbc.gridx = 3;
        add(checkInYearDropdown, gbc);

        // Check-out Date
        JLabel checkOutLabel = new JLabel("Check-out Date:");
        checkOutLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(checkOutLabel, gbc);

        // Check-out Day Dropdown
        checkOutDayDropdown = new JComboBox<>();
        checkOutDayDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkOutDayDropdown.setBackground(Color.WHITE);
        gbc.gridx = 1;
        gbc.weightx = 1;
        add(checkOutDayDropdown, gbc);

        // Check-out Month Dropdown
        checkOutMonthDropdown = new JComboBox<>(new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"});
        checkOutMonthDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkOutMonthDropdown.setBackground(Color.WHITE);
        gbc.gridx = 2;
        add(checkOutMonthDropdown, gbc);

        // Check-out Year Dropdown
        checkOutYearDropdown = new JComboBox<>();
        checkOutYearDropdown.setFont(new Font("SansSerif", Font.PLAIN, 14));
        checkOutYearDropdown.setBackground(Color.WHITE);
        gbc.gridx = 3;
        add(checkOutYearDropdown, gbc);

        // Populate Year Dropdowns
        populateYearDropdown(checkInYearDropdown);
        populateYearDropdown(checkOutYearDropdown);

        // Add listeners to update days when month or year changes
        checkInMonthDropdown.addActionListener(e -> updateDayDropdown(checkInDayDropdown, checkInMonthDropdown, checkInYearDropdown));
        checkInYearDropdown.addActionListener(e -> updateDayDropdown(checkInDayDropdown, checkInMonthDropdown, checkInYearDropdown));
        checkOutMonthDropdown.addActionListener(e -> updateDayDropdown(checkOutDayDropdown, checkOutMonthDropdown, checkOutYearDropdown));
        checkOutYearDropdown.addActionListener(e -> updateDayDropdown(checkOutDayDropdown, checkOutMonthDropdown, checkOutYearDropdown));

        // Total Price (Auto Calculated)
        JLabel totalPriceTextLabel = new JLabel("Total Price:");
        totalPriceTextLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(totalPriceTextLabel, gbc);

        totalPriceLabel = new JLabel("0.0");
        totalPriceLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        add(totalPriceLabel, gbc);

        // Book Button
        btnBook = new JButton("Book Room");
        styleButton(btnBook);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnBook, gbc);

        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bookRoom();
            }
        });

        // Calculate price Button
        btnCalculatePrice = new JButton("Calculate Price");
        styleButton(btnCalculatePrice);
        gbc.gridx = 2;
        gbc.gridy = 5;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        add(btnCalculatePrice, gbc);

        btnCalculatePrice.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTotalPriceLabel();
            }
        });

        loadAvailableRooms();
        updateDayDropdown(checkInDayDropdown, checkInMonthDropdown, checkInYearDropdown); // Initialize check-in day dropdown
        updateDayDropdown(checkOutDayDropdown, checkOutMonthDropdown, checkOutYearDropdown); // Initialize check-out day dropdown
        setVisible(true);
    }

    private void updateTotalPriceLabel() {
        totalPriceLabel.setText(String.valueOf(calculateTotalPrice(getSelectedRoomId(), Date.valueOf(getCheckInDate()), Date.valueOf(getCheckOutDate()))));
    }

    private void loadAvailableRooms() {
        List<Room> rooms = roomService.getAllRooms();
        for (Room room : rooms) {
            if (room.isAvailable()) {
                roomDropdown.addItem(room.getId());
            }
        }
    }

    private void populateYearDropdown(JComboBox<Integer> yearDropdown) {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 0; i < 3; i++) {
            yearDropdown.addItem(currentYear + i);
        }
    }

    private void updateDayDropdown(JComboBox<Integer> dayDropdown, JComboBox<String> monthDropdown, JComboBox<Integer> yearDropdown) {
        int selectedMonth = monthDropdown.getSelectedIndex() + 1; // Months are 1-based
        int selectedYear = (int) yearDropdown.getSelectedItem();
        int daysInMonth = getDaysInMonth(selectedMonth, selectedYear);

        dayDropdown.removeAllItems();
        for (int i = 1; i <= daysInMonth; i++) {
            dayDropdown.addItem(i);
        }
    }

    private int getDaysInMonth(int month, int year) {
        switch (month) {
            case 2: // February
                if (isLeapYear(year)) {
                    return 29;
                } else {
                    return 28;
                }
            case 4: // April
            case 6: // June
            case 9: // September
            case 11: // November
                return 30;
            default:
                return 31;
        }
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private void bookRoom() {
        try {
            int roomId = getSelectedRoomId();
            String guestName = guestNameField.getText();

            String checkInDate = getCheckInDate();

            String checkOutDate = getCheckOutDate();

            double totalPrice = calculateTotalPrice(roomId, Date.valueOf(checkInDate), Date.valueOf(checkOutDate));

            if(!guestName.isEmpty()){
                Booking booking = new Booking(0, roomId, 1, guestName, Date.valueOf(checkInDate), Date.valueOf(checkOutDate), totalPrice, "Confirmed");
                bookingService.bookRoom(booking);

                MessageDialog.showSuccessMessage(this, "Room booked successfully!");
                dispose();
            }else{
                MessageDialog.showWarningMessage(this, "Guest name cannot be empty!");

            }


        } catch (Exception ex) {
            MessageDialog.showErrorMessage(this, "Error: " + ex.getMessage());
        }
    }

    private String getCheckOutDate() {
        // Check-out Date
        int checkOutDay = (Integer) checkOutDayDropdown.getSelectedItem();
        int checkOutMonth = checkOutMonthDropdown.getSelectedIndex() + 1; // Months are 1-based
        int checkOutYear = (int) checkOutYearDropdown.getSelectedItem();
        return String.format("%04d-%02d-%02d", checkOutYear, checkOutMonth, checkOutDay);
    }

    private String getCheckInDate() {
        // Check-in Date
        int checkInDay = (Integer) checkInDayDropdown.getSelectedItem();
        int checkInMonth = checkInMonthDropdown.getSelectedIndex() + 1; // Months are 1-based
        int checkInYear = (int) checkInYearDropdown.getSelectedItem();
        return String.format("%04d-%02d-%02d", checkInYear, checkInMonth, checkInDay);
    }

    private int getSelectedRoomId() {
        return (Integer) roomDropdown.getSelectedItem();
    }

    private double calculateTotalPrice(int roomId, Date checkIn, Date checkOut) {
        Room room = roomService.getRoomById(roomId);
        long diff = checkOut.getTime() - checkIn.getTime();
        int days = (int) (diff / (1000 * 60 * 60 * 24));
        return days * room.getPrice();
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover Effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185)); // Darker blue on hover
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219)); // Restore original color
            }
        });
    }

    public static void main(String[] args) {
        new BookingFormUI();
    }
}