package dao;

import config.DBConnection;
import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    private Connection conn;

    public BookingDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (room_id, user_id, guest_name, check_in, check_out, total_price, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getRoomId());
            stmt.setInt(2, booking.getUserId());
            stmt.setString(3, booking.getGuestName());
            stmt.setDate(4, new java.sql.Date(booking.getCheckIn().getTime()));
            stmt.setDate(5, new java.sql.Date(booking.getCheckOut().getTime()));
            stmt.setDouble(6, booking.getTotalPrice());
            stmt.setString(7, "Confirmed"); // New booking should have "Confirmed" status

            stmt.executeUpdate();

            // Get generated booking ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int bookingId = generatedKeys.getInt(1);
                    updateRoomAvailability(booking.getRoomId(), false); // Mark room as booked
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("user_id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) {
        Booking booking = null;
        String query = "SELECT * FROM bookings WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("user_id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                );
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }

    public void updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBooking(int bookingId) {
        int roomId = -1;

        // Get Room ID before deleting booking
        String getRoomQuery = "SELECT room_id FROM bookings WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(getRoomQuery);
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                roomId = rs.getInt("room_id");
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Delete booking
        String deleteQuery = "DELETE FROM bookings WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(deleteQuery);
            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();
            stmt.close();

            if (rowsDeleted > 0 && roomId != -1) {
                // Mark room as available again
                updateRoomAvailability(roomId, true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRoomAvailability(int roomId, boolean isAvailable) {
        String updateQuery = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(updateQuery);
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getUnpaidOrFailedBookings() {
        List<Booking> bookings = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();
            String query = "SELECT * FROM bookings WHERE id NOT IN (SELECT booking_id FROM payments WHERE payment_status = 'Completed')";
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {
                bookings.add(new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("user_id"),
                        rs.getString("guest_name"),
                        rs.getDate("check_in"),
                        rs.getDate("check_out"),
                        rs.getDouble("total_price"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return bookings;
    }

    public Booking getBookingByRoomId(int roomId) {
        Booking booking = null;
        String query = "SELECT b.id, b.user_id, b.guest_name, b.check_in, b.check_out, b.total_price, b.status, " +
                "COALESCE(p.payment_status, 'Pending') AS payment_status " +
                "FROM bookings b " +
                "LEFT JOIN payments p ON b.id = p.booking_id " +
                "WHERE b.room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int bookingId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String guestName = rs.getString("guest_name");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double totalPrice = rs.getDouble("total_price");
                String status = rs.getString("status");

                booking = new Booking(bookingId, roomId, userId, guestName, checkIn, checkOut, totalPrice, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return booking;
    }


}
