package model;

public class Room {
        private int id;
        private String roomNumber;
        private String roomType;
        private double price;
        private boolean isAvailable;

        public Room(int id, String roomNumber, String roomType, double price, boolean isAvailable) {
            this.id = id;
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.price = price;
            this.isAvailable = isAvailable;
        }

        public int getId() {
            return id;
        }

        public String getRoomNumber() {
            return roomNumber;
        }

        public String getRoomType() {
            return roomType;
        }

        public double getPrice() {
            return price;
        }

        public boolean isAvailable() {
            return isAvailable;
        }

        public void setAvailable(boolean available) {
            isAvailable = available;
        }
    }


