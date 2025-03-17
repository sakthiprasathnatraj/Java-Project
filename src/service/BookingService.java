package service;

import dao.BookingDAO;
import model.Booking;

import java.util.ArrayList;
import java.util.List;

public class BookingService {
    private BookingDAO bookingDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
    }

    public void bookRoom(Booking booking) {
        bookingDAO.addBooking(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingDAO.getAllBookings();
    }

    public void cancelBooking(int bookingId) {
        bookingDAO.updateBookingStatus(bookingId, "Cancelled");
    }

    public void checkoutBooking(int bookingId) {
        if (!"CheckedOut".equals(bookingDAO.getBookingById(bookingId).getStatus())) {
            bookingDAO.updateBookingStatus(bookingId, "CheckedOut");
        }
    }
    public Booking getBookingById(int bookingId) {
        return bookingDAO.getBookingById(bookingId);
    }

    public Booking getBookingByRoomId(int roomId) {
        Booking booking = bookingDAO.getBookingByRoomId(roomId);

        for(Booking booking1 : getAllBookingsForRoomId(roomId)) {
            if (!"CheckedOut".equals(booking1.getStatus())) {
                booking = booking1;
            }
        }
        return booking;
    }

    public List<Booking> getAllBookingsForRoomId(int roomId) {
        List<Booking> allBookingsForRoomId = new ArrayList<>();
        for(Booking booking : getAllBookings()){
            if(booking.getRoomId() == roomId) {
                allBookingsForRoomId.add(booking);
            }
        }
        return allBookingsForRoomId;
    }
    
    
}
