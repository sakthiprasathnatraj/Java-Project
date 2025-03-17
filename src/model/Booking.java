package model;

import java.util.Date;

public class Booking {
    private int id;
    private int roomId;
    private int userId;
    private String guestName; // New field
    private Date checkIn;
    private Date checkOut;
    private double totalPrice;
    private String status; // "Confirmed", "Cancelled", "CheckedOut"

    public Booking(int id, int roomId, int userId, String guestName, Date checkIn, Date checkOut, double totalPrice, String status) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getGuestName() { return guestName; }
    public void setGuestName(String guestName) { this.guestName = guestName; }

    public Date getCheckIn() { return checkIn; }
    public void setCheckIn(Date checkIn) { this.checkIn = checkIn; }

    public Date getCheckOut() { return checkOut; }
    public void setCheckOut(Date checkOut) { this.checkOut = checkOut; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
