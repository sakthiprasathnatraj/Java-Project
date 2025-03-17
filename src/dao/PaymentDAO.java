package dao;

import config.DBConnection;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {
    private Connection conn;

    public PaymentDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Add or Update Payment
    public void addOrUpdatePayment(int bookingId, double amount, String paymentStatus, String paymentMethod) {
        String sqlCheck = "SELECT * FROM payments WHERE booking_id = ?";
        String sqlInsert = "INSERT INTO payments (booking_id, amount, payment_date, payment_status, payment_method) VALUES (?, ?, NOW(), ?, ?)";
        String sqlUpdate = "UPDATE payments SET amount = ?, payment_date = NOW(), payment_status = ?, payment_method = ? WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck)) {

            stmtCheck.setInt(1, bookingId);
            ResultSet rs = stmtCheck.executeQuery();

            if (rs.next()) { // Payment exists, update it
                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setDouble(1, amount);
                    stmtUpdate.setString(2, paymentStatus);
                    stmtUpdate.setString(3, paymentMethod);
                    stmtUpdate.setInt(4, bookingId);
                    stmtUpdate.executeUpdate();
                }
            } else { // No payment exists, insert new one
                try (PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert)) {
                    stmtInsert.setInt(1, bookingId);
                    stmtInsert.setDouble(2, amount);
                    stmtInsert.setString(3, paymentStatus); // ✅ Make sure this is correctly assigned
                    stmtInsert.setString(4, paymentMethod);
                    stmtInsert.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Get Payment by Booking ID
    public Payment getPaymentByBookingId(int bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getDouble("amount"),
                        rs.getDate("payment_date"),
                        rs.getString("payment_status"), // Might default to "Completed"
                        rs.getString("payment_method")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // ✅ Return null if no payment exists
    }


    // Get all payments
    public List<Payment> getAllPayments() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payments";
        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                payments.add(new Payment(
                        rs.getInt("id"),
                        rs.getInt("booking_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("payment_date"),
                        rs.getString("payment_status"),
                        rs.getString("payment_method")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Delete Payment
    public void deletePayment(int paymentId) {
        String query = "DELETE FROM payments WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
