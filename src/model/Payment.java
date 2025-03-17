package model;
import java.util.Date;
public class Payment {

        private int id;
        private int bookingId;
        private double amount;
        private Date paymentDate;
        private String paymentStatus;
        private String paymentMethod;

        public Payment(int id, int bookingId, double amount, Date paymentDate, String paymentStatus, String paymentMethod) {
            this.id = id;
            this.bookingId = bookingId;
            this.amount = amount;
            this.paymentDate = paymentDate;
            this.paymentStatus = paymentStatus;
            this.paymentMethod = paymentMethod;
        }

        // Getters and Setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getBookingId() { return bookingId; }
        public void setBookingId(int bookingId) { this.bookingId = bookingId; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }

        public Date getPaymentDate() { return paymentDate; }
        public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

        public String getPaymentStatus() { return paymentStatus; }
        public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

