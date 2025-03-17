package dao;

import config.DBConnection;
import model.Room;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    private Connection conn;

    public RoomDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                ));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public Room getRoomById(int id) {
        Room room = null;
        String query = "SELECT * FROM rooms WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                room = new Room(
                        rs.getInt("id"),
                        rs.getString("room_number"),
                        rs.getString("room_type"),
                        rs.getDouble("price"),
                        rs.getBoolean("is_available")
                );
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    public void updateRoomAvailability(int roomId, boolean isAvailable) {
        String sql = "UPDATE rooms SET is_available = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBoolean(1, isAvailable);
            stmt.setInt(2, roomId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public int getRoomIdByRoomNumber(String roomNumber) {
        String query = "SELECT id FROM rooms WHERE room_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }
}
