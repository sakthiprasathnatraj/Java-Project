package dao;

import config.DBConnection;
import model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

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
            stmt.setString(7, "Confirmed");

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

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBookingById(int bookingId) {
        String query = "SELECT * FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateBookingStatus(int bookingId, String status) {
        String query = "UPDATE bookings SET status = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, bookingId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBooking(int bookingId) {
        int roomId = -1;

        // Get Room ID before deleting booking
        String getRoomQuery = "SELECT room_id FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(getRoomQuery)) {

            stmt.setInt(1, bookingId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roomId = rs.getInt("room_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Delete booking
        String deleteQuery = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteQuery)) {

            stmt.setInt(1, bookingId);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0 && roomId != -1) {
                updateRoomAvailability(roomId, true); // Mark room as available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateRoomAvailability(int roomId, boolean isAvailable) {
        String updateQuery = "UPDATE rooms SET is_available = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {

            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Booking> getUnpaidOrFailedBookings() {
        List<Booking> bookings = new ArrayList<>();
        String query = "SELECT * FROM bookings WHERE id NOT IN (SELECT booking_id FROM payments WHERE payment_status = 'Completed')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapResultSetToBooking(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    public Booking getBookingByRoomId(int roomId) {
        String query = "SELECT * FROM bookings WHERE room_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBooking(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        return new Booking(
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
}
