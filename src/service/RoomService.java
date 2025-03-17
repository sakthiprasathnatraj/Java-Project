package service;

import dao.RoomDAO;
import model.Room;
import java.util.List;

public class RoomService {
    private RoomDAO roomDAO;

    public RoomService() {
        roomDAO = new RoomDAO();
    }

    public List<Room> getAllRooms() {
        return roomDAO.getAllRooms();
    }

    public Room getRoomById(int id) {
        return roomDAO.getRoomById(id);
    }

    public void updateRoomAvailability(int roomId, boolean isAvailable) {
        roomDAO.updateRoomAvailability(roomId, isAvailable);
    }

    public int getRoomIdByRoomNumber(String roomNumber) {
        return roomDAO.getRoomIdByRoomNumber(roomNumber);
    }
}
