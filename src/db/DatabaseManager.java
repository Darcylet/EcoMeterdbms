package db;

import models.Appliance;
import models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private final Connection connection;

    public DatabaseManager(String dbUrl, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, password);
    }

    // Close database connection
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    // Register a new user
    public boolean registerUser(User user) {
        String query = "INSERT INTO users (username, password, email, house_square_footage) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword()); // Ideally, password should be hashed
            stmt.setString(3, user.getEmail());
            stmt.setInt(4, user.getHouseSquareFootage());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // User login
    public User loginUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password); // Verify hashed password in a real application
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getInt("house_square_footage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update user's house square footage
    public boolean updateHouseSquareFootage(int userId, int squareFootage) {
        String query = "UPDATE users SET house_square_footage = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, squareFootage);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save custom appliance to the database
    public boolean saveCustomAppliance(int userId, Appliance appliance) {
        String query = "INSERT INTO appliances (user_id, name, power_consumption_per_hour, hours_used_per_month) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, appliance.getName());
            stmt.setDouble(3, appliance.getPowerConsumptionPerHour());
            stmt.setInt(4, appliance.getHoursUsedPerMonth());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Retrieve appliances for a user
    public List<Appliance> getUserAppliances(int userId) {
        List<Appliance> appliances = new ArrayList<>();
        String query = "SELECT * FROM appliances WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                appliances.add(new Appliance(
                        rs.getString("name"),
                        rs.getDouble("power_consumption_per_hour"),
                        rs.getInt("hours_used_per_month")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appliances;
    }
}
