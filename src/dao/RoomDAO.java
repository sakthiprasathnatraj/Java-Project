package dao;

import config.DBConnection;
import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RoomDAO {
    private static final Logger LOGGER = Logger.getLogger(RoomDAO.class.getName());
    private final Connection conn;

    public RoomDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public List<Room> getAllRooms() {
        String query = "SELECT * FROM rooms";
        List<Room> rooms = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching rooms", e);
        }
        return rooms;
    }

    public Room getRoomById(int id) {
        String query = "SELECT * FROM rooms WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching room with ID: " + id, e);
        }
        return null;
    }

    public void updateRoomAvailability(int roomId, boolean isAvailable) {
        String sql = "UPDATE rooms SET is_available = ? WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating room availability for ID: " + roomId, e);
        }
    }

    public int getRoomIdByRoomNumber(String roomNumber) {
        String query = "SELECT id FROM rooms WHERE room_number = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching room ID for room number: " + roomNumber, e);
        }
        return -1; // Return -1 if not found
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("room_number"),
                rs.getString("room_type"),
                rs.getDouble("price"),
                rs.getBoolean("is_available")
        );
    }
}
