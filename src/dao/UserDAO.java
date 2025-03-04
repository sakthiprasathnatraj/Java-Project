package dao;

import config.DBConnection;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection conn;

    public UserDAO() {
        try {
            conn = DBConnection.getConnection();
        } catch (SQLException e) {
        e.printStackTrace();
    }
    }

    public List<User> getAllStaff() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM users WHERE role = 'STAFF'";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(new User(rs.getInt("id"), rs.getString("username"), "STAFF"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public void addUser(String username, String password, String role) {
        String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(int id, String newUsername, String newPassword) {
        String query = "UPDATE users SET username = ?, password = ? WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, newUsername);
            stmt.setString(2, newPassword);
            stmt.setInt(3, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(int id) {
        String query = "DELETE FROM users WHERE id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean validateUser(String username, String password, String role) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?";
        try {
             PreparedStatement stmt = conn.prepareStatement(query); {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();

            return rs.next(); // User exists
        }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}

