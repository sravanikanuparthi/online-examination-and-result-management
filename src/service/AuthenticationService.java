package service;

import database.DatabaseManager;
import exception.InvalidLoginException;
import model.Admin;
import model.Student;

import java.sql.*;

public class AuthenticationService {

    public Student studentLogin(String id, String password)
            throws InvalidLoginException {
        String sql = "SELECT student_id, name, password, email FROM students WHERE student_id = ?";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getString("password").equals(password)) {
                    return new Student(
                            rs.getString("student_id"),
                            rs.getString("name"),
                            rs.getString("password"),
                            rs.getString("email"));
                }
            }
        } catch (SQLException e) {
            throw new InvalidLoginException("Database error: " + e.getMessage());
        }

        throw new InvalidLoginException("Invalid student ID or password.");
    }

    public Admin adminLogin(String username, String password)
            throws InvalidLoginException {
        String sql = "SELECT username, password FROM admins WHERE username = ?";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getString("password").equals(password)) {
                    return new Admin(rs.getString("username"), rs.getString("password"));
                }
            }
        } catch (SQLException e) {
            throw new InvalidLoginException("Database error: " + e.getMessage());
        }

        throw new InvalidLoginException("Invalid admin username or password.");
    }

    public void registerStudent(String id, String name, String password, String email)
            throws SQLException {
        String sql = "INSERT INTO students(student_id,name,password,email) VALUES(?,?,?,?)";

        try (Connection con = DatabaseManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, password);
            ps.setString(4, email);
            ps.executeUpdate();
        }
    }
}
